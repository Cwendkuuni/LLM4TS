import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;

public class RenameLabelsTest {

 private AbstractCompiler compiler;
 private RenameLabels renameLabels;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 renameLabels = new RenameLabels(compiler);
 }

 @Test
 public void testProcessLabelsEnterScope() {
 ProcessLabels processLabels = new ProcessLabels();
 assertNotNull(processLabels.enterScope(mock(NodeTraversal.class)));
 }

 @Test
 public void testProcessLabelsExitScope() {
 ProcessLabels processLabels = new ProcessLabels();
 assertNotNull(processLabels.exitScope(mock(NodeTraversal.class)));
 }

 @Test
 public void testShouldTraverseLabelNode() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);

 processLabels.shouldTraverse(nodeTraversal, labelNode, nodeTraversal.getParent());
 }

 @Test
 public void testShouldTraverseLabelNodeAlreadyRenamed() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(labelNode.getFirstChild()).thenReturn(new Node("renamedLabelName"));

 processLabels.shouldTraverse(nodeTraversal, labelNode, nodeTraversal.getParent());
 }

 @Test (expected = AssertionError.class)
 public void testShouldTraverseLabelNodeDuplicateName() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(labelNode.getFirstChild()).thenReturn(new Node("labelName"));

 processLabels.shouldTraverse(nodeTraversal, labelNode, nodeTraversal.getParent());
 }

 @Test
 public void testVisitLabelUnreferenced() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);

 processLabels.visit(nodeTraversal, labelNode, nodeTraversal.getParent());
 }

 @Test
 public void testVisitLabelReferenced() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 Node breakNode = new Node(Token.BREAK);
 Node breakNameNode = new Node("labelName");
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(nodeTraversal.getNode("break", labelNode)).thenReturn(breakNode);
 when(breakNode.getFirstChild()).thenReturn(breakNameNode);
 when(breakNameNode.getString()).thenReturn("labelName");
 when(labelNode.getFirstChild()).thenReturn(new Node("renamedLabelName"));

 processLabels.visit(nodeTraversal, labelNode, nodeTraversal.getParent());
 }

 @Test
 public void testVisitBreakOrContinue() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node breakNode = new Node(Token.BREAK);
 Node breakNameNode = new Node("labelName");
 when(nodeTraversal.getParent(breakNode)).thenReturn(new Node(Token.ROOT));
 when(breakNode.getFirstChild()).thenReturn(breakNameNode);
 when(breakNameNode.getString()).thenReturn("labelName");

 processLabels.visitBreakOrContinue(breakNode);
 }

 @Test
 public void testProcessLabelsRemoveUnusedLabel() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 Node labelNameNode = new Node("labelName");
 Node blockNode = new Node(Token.BLOCK);
 NodeUtil.tryMergeBlock(blockNode);

 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(labelNode.getFirstChild()).thenReturn(labelNameNode);
 when(labelNameNode.getString()).thenReturn("labelName");
 when(nodeTraversal.getParent(blockNode)).thenReturn(labelNode);

 processLabels.visit(nodeTraversal, labelNode, nodeTraversal.getParent());
 processLabels.visit(nodeTraversal, blockNode, labelNode);

 assertNotNull(labelNode.getParent());
 assertNotNull(labelNode.getParent().getParent());
 }

 @Test
 public void testProcessLabelsRemoveUnusedLabelWithEmptyBlock() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 Node labelNameNode = new Node("labelName");
 Node blockNode = new Node(Token.BLOCK);

 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(labelNode.getFirstChild()).thenReturn(labelNameNode);
 when(labelNameNode.getString()).thenReturn("labelName");
 when(nodeTraversal.getParent(blockNode)).thenReturn(labelNode);
 when(blockNode.getLastChild()).thenReturn(new Node(Token.EMPTY));

 processLabels.visit(nodeTraversal, labelNode, nodeTraversal.getParent());
 processLabels.visit(nodeTraversal, blockNode, labelNode);

 assertNotNull(labelNode.getParent());
 assertNotNull(labelNode.getParent().getParent());
 }

 @Test (expected = AssertionError.class)
 public void testProcessLabelsRemoveUnusedLabelWithBlockChildren() {
 ProcessLabels processLabels = new ProcessLabels();
 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 Node labelNode = new Node(Token.LABEL);
 Node labelNameNode = new Node("labelName");
 Node blockNode = new Node(Token.BLOCK);
 Node childNode = new Node(Token.VAR);
 Node childNameNode = new Node("childName");
 when(nodeTraversal.getParent(labelNode)).thenReturn(new Node(Token.ROOT));
 when(nodeTraversal.getNode("labelName", labelNode.getParent())).thenReturn(labelNode);
 when(labelNode.getFirstChild()).thenReturn(labelNameNode);
 when(labelNameNode.getString()).thenReturn("labelName");
 when(nodeTraversal.getParent(blockNode)).thenReturn(labelNode);
 when(blockNode.getFirstChild()).thenReturn(childNode);
 when(childNode.getFirstChild()).thenReturn(childNameNode);
 when(childNameNode.getString()).thenReturn("childName");

 processLabels.visit(nodeTraversal, labelNode, nodeTraversal.getParent());
 processLabels.visit(nodeTraversal, blockNode, labelNode);
 }

}