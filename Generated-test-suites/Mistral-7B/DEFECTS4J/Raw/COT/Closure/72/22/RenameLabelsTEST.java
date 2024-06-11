import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

 private AbstractCompiler compiler;
 private Compiler pass;
 private NodeFactory nodeFactory;
 private Node root;

 @Before
 public void setUp() {
 compiler = new Compiler();
 pass = new RenameLabels(compiler);
 nodeFactory = compiler.getNodeFactory();
 root = nodeFactory.createScope();
 }

 /**
 * Test the basic functionality of the RenameLabels compiler pass.
 */
 @Test
 public void testBasicFunctionality() {
 pass.process(null, root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = new ProcessLabels();
 NodeTraversal.traverse(compiler, root, sc);

 // The labels should all have been renamed.
 int depth = 1;
 String expectedName = "a";
 String actualName;
 Node labelNode = null;

 for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
 if (child.getType() == Token.LABEL) {
 labelNode = child;
 break;
 }
 }

 assertNotNull(labelNode);
 actualName = labelNode.getFirstChild().getString();
 assertEquals(expectedName, actualName);

 for (Node statement = root.getLastChild(); statement != null; statement = statement.getPreviousSibling()) {
 if (statement.getType() == Token.BREAK || statement.getType() == Token.CONTINUE) {
 actualName = statement.getFirstChild().getString();
 if (statement.getType() == Token.BREAK) {
 depth++;
 }
 assertEquals(String.format("%s%d", expectedName, depth), actualName);
 }
 }
 }

 /**
 * Test the edge case where a label is defined multiple times at the same depth.
 */
 @Test
 public void testLabelDefinedMultipleTimesAtSameDepth() {
 LabelNamespace namespace = new LabelNamespace();
 namespace.renameMap.put("a", new LabelInfo(1));
 namespace.renameMap.put("a", new LabelInfo(1));
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 Node labelNode1 = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node labelNode2 = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node blockNode = nodeFactory.createBlock();
 labelNode1.setParent(blockNode);
 labelNode2.setParent(blockNode);
 blockNode.setParent(root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 String actualName1 = labelNode1.getFirstChild().getString();
 String actualName2 = labelNode2.getFirstChild().getString();
 assertEquals("a1", actualName1);
 assertEquals("a2", actualName2);
 }

 /**
 * Test the edge case where a label is defined at a deeper depth than any other label.
 */
 @Test
 public void testLabelDefinedAtDeeperDepth() {
 LabelNamespace namespace = new LabelNamespace();
 LabelNamespace namespace2 = new LabelNamespace();
 namespace.renameMap.put("a", new LabelInfo(1));
 namespace2.renameMap.put("b", new LabelInfo(2));
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 pl.namespaceStack.push(namespace2);
 Node labelNode1 = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node labelNode2 = nodeFactory.createLabel(nodeFactory.createString("b"));
 Node blockNode = nodeFactory.createBlock();
 labelNode1.setParent(blockNode);
 labelNode2.setParent(blockNode);
 blockNode.setParent(root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 String actualName1 = labelNode1.getFirstChild().getString();
 String actualName2 = labelNode2.getFirstChild().getString();
 assertEquals("a", actualName1);
 assertEquals("b", actualName2);
 }

 /**
 * Test the edge case where a label is defined at the same depth but with a longer name.
 */
 @Test
 public void testLabelDefinedAtSameDepthButLongerName() {
 LabelNamespace namespace = new LabelNamespace();
 LabelNamespace namespace2 = new LabelNamespace();
 namespace.renameMap.put("a", new LabelInfo(1));
 namespace2.renameMap.put("longerLabelName", new LabelInfo(1));
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 pl.namespaceStack.push(namespace2);
 Node labelNode1 = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node labelNode2 = nodeFactory.createLabel(nodeFactory.createString("longerLabelName"));
 Node blockNode = nodeFactory.createBlock();
 labelNode1.setParent(blockNode);
 labelNode2.setParent(blockNode);
 blockNode.setParent(root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 String actualName1 = labelNode1.getFirstChild().getString();
 String actualName2 = labelNode2.getFirstChild().getString();
 assertEquals("a", actualName1);
 assertEquals("longerLabelName", actualName2);
 }

 /**
 * Test the edge case where a label is defined and then removed before being used.
 */
 @Test
 public void testLabelDefinedAndRemovedBeforeBeingUsed() {
 LabelNamespace namespace = new LabelNamespace();
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 Node labelNode = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node blockNode = nodeFactory.createBlock();
 labelNode.setParent(blockNode);
 blockNode.setParent(root);
 LabelInfo li = namespace.renameMap.get("a");
 // Remove the label before it is used.
 NodeUtil.removeNodeRecursively(compiler, labelNode);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 // The label should have been removed.
 assertNull(li);
 }

 /**
 * Test the edge case where a label is defined but not used.
 */
 @Test
 public void testLabelDefinedButNotUsed() {
 LabelNamespace namespace = new LabelNamespace();
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 Node labelNode = nodeFactory.createLabel(nodeFactory.createString("a"));
 Node blockNode = nodeFactory.createBlock();
 labelNode.setParent(blockNode);
 blockNode.setParent(root);
 // The label should be removed because it is not used.
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 // The label should not be in the label map.
 LabelInfo li = namespace.renameMap.get("a");
 assertNull(li);
 }

 /**
 * Test the exception handling scenario where a label name is too long.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testLabelNameTooLong() {
 LabelNamespace namespace = new LabelNamespace();
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 String labelName = "labelNameThatIsTooLongToBeUsedAsLabelName";
 Node labelNode = nodeFactory.createLabel(nodeFactory.createString(labelName));
 Node blockNode = nodeFactory.createBlock();
 labelNode.setParent(blockNode);
 blockNode.setParent(root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 }

 /**
 * Test the exception handling scenario where a label name conflicts with a
 * JavaScript keyword.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testLabelNameConflictsWithJavaScriptKeyword() {
 LabelNamespace namespace = new LabelNamespace();
 ProcessLabels pl = new ProcessLabels();
 pl.namespaceStack.push(namespace);
 String labelName = "break";
 Node labelNode = nodeFactory.createLabel(nodeFactory.createString(labelName));
 Node blockNode = nodeFactory.createBlock();
 labelNode.setParent(blockNode);
 blockNode.setParent(root);
 NodeTraversal nt = new NodeTraversal(compiler, root);
 ScopedCallback sc = pl;
 NodeTraversal.traverse(compiler, root, sc);
 }
}