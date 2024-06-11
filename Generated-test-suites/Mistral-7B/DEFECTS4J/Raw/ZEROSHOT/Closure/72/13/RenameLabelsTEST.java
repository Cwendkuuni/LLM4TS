import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private RenameLabels renameLabels;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    nodeFactory = compiler.getFactory();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testRenameLabels() {
    String input = "function foo() {\n"
        + "  a: { break a; }\n"
        + "  b: { break a; break b; }\n"
        + "  c: { break c; }\n"
        + "}";

    ProcessNode processNode = new ProcessNode(nodeFactory, input);
    renameLabels.process(processNode.getExterns(), processNode.getRoot());
    Node root = processNode.getProcessedNode();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, null);
    Node labelNode = null;

    // Test top level label 'a'
    labelNode = nodeTraversal.traverse(Token.LABEL, null);
    assertTrue(labelNode != null);
    String nameBefore = labelNode.getFirstChild().getString();
    String nameAfter = getNameForId(1);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test inner label 'b'
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertTrue(labelNode != null);
    nameBefore = labelNode.getFirstChild().getString();
    nameAfter = getNameForId(2);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test inner label 'b' referenced again
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertTrue(labelNode != null);
    nameBefore = labelNode.getFirstChild().getString();
    nameAfter = getNameForId(2);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test inner label 'c'
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertTrue(labelNode != null);
    nameBefore = labelNode.getFirstChild().getString();
    nameAfter = getNameForId(3);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test unreferenced label 'd'
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertNull(labelNode);
  }

  @Test
  public void testRemoveUnusedLabels() {
    String input = "function foo() {\n"
        + "  a: { break a; }\n"
        + "  b: { break b; }\n"
        + "  c: { break c; }\n"
        + "  d: {}\n"
        + "}";

    renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    ProcessNode processNode = new ProcessNode(nodeFactory, input);
    renameLabels.process(processNode.getExterns(), processNode.getRoot());
    Node root = processNode.getProcessedNode();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, null);
    Node labelNode = null;

    // Test top level label 'a'
    labelNode = nodeTraversal.traverse(Token.LABEL, null);
    assertTrue(labelNode != null);
    String nameBefore = labelNode.getFirstChild().getString();
    String nameAfter = getNameForId(1);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test inner label 'b'
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertTrue(labelNode != null);
    nameBefore = labelNode.getFirstChild().getString();
    nameAfter = getNameForId(2);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test inner label 'c'
    labelNode = nodeTraversal.advance();
    labelNode = nodeTraversal.traverse(Token.LABEL, labelNode);
    assertTrue(labelNode != null);
    nameBefore = labelNode.getFirstChild().getString();
    nameAfter = getNameForId(3);
    assertTrue(nameAfter.length() > 0);
    assertTrue(nameBefore.equals(nameAfter));

    // Test unreferenced label 'd' shouldn't exist
    labelNode = nodeTraversal.traverse(Token.LABEL, null);
    assertNull(labelNode);
  }

  private String getNameForId(int id) {
    if (renameLabels != null) {
      LabelNamespace current = (LabelNamespace) renameLabels.namespaceStack.peek();
      LabelInfo li = current.renameMap.get(Integer.toString(id));
      return li != null ? li.id > 0 ? renameLabels.names.get(li.id - 1) : "" : "";
    }
    return "";
  }
}