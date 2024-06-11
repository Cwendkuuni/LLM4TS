import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerPassManager passManager;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    passManager = new CompilerPassManager(compiler, nodeFactory);
  }

  @org.junit.Test
  public void testRenameLabelsSimple() {
    // given
    String sourceCode = "function foo() {\n" +
        "  a: {\n" +
        "    b: 1;\n" +
        "    break a;\n" +
        "  }\n" +
        "}";
    TestSourceFile sourceFile = new TestSourceFile("TestRenameLabelsSimple.js", sourceCode);
    RenameLabels renamer = new RenameLabels(passManager);

    // when
    Node externs = Node.root();
    Node root = sourceFile.getTree();
    renamer.process(externs, root);

    // then
    NodeTraversal nodeTraversal = NodeTraversal.create(passManager, root);
    Node labelNode = findLabelWithName("a", nodeTraversal);
    String expectedNewLabelName = "c";
    String actualNewLabelName = labelNode.getFirstChild().getString();
    assertEquals(expectedNewLabelName, actualNewLabelName);
  }

  @org.junit.Test
  public void testRenameLabelsNested() {
    // given
    String sourceCode = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      c: 1;\n" +
        "      break b;\n" +
        "    }\n" +
        "  }\n" +
        "}";
    TestSourceFile sourceFile = new TestSourceFile("TestRenameLabelsNested.js", sourceCode);
    RenameLabels renamer = new RenameLabels(passManager);

    // when
    Node externs = Node.root();
    Node root = sourceFile.getTree();
    renamer.process(externs, root);

    // then
    NodeTraversal nodeTraversal = NodeTraversal.create(passManager, root);
    Node labelNode = findLabelWithName("c", nodeTraversal);
    String expectedNewLabelName = "d";
    String actualNewLabelName = labelNode.getFirstChild().getString();
    assertEquals(expectedNewLabelName, actualNewLabelName);
  }

  @org.junit.Test
  public void testRenameLabelsUnused() {
    // given
    String sourceCode = "function foo() {\n" +
        "  a: {\n" +
        "  }\n" +
        "}";
    TestSourceFile sourceFile = new TestSourceFile("TestRenameLabelsUnused.js", sourceCode);
    RenameLabels renamer = new RenameLabels(passManager);

    // when
    Node externs = Node.root();
    Node root = sourceFile.getTree();
    if (renamer.removeUnused) {
      renamer.process(externs, root);
    }

    // then
    NodeTraversal nodeTraversal = NodeTraversal.create(passManager, root);
    Node labelNode = findLabelWithName("a", nodeTraversal);
    Map<String, Boolean> labels = new HashMap<>();
    nodeTraversal.inOrderTraverse(new NodeTraversal.Callback() {
      @Override
      public void visit(Node node) {
        if (node.getType() == Token.LABEL && node.getFirstChild() != null) {
          String labelName = node.getFirstChild().getString();
          labels.put(labelName, true);
        }
      }
    });
    assertEquals(0, labels.size());
    // The label "a" should be removed
    assertEquals(null, findLabelWithName("a", nodeTraversal));
  }

  private Node findLabelWithName(String labelName, NodeTraversal nodeTraversal) {
    NodeTraversal.traverse(nodeTraversal, root -> {
      if (root.getType() == Token.LABEL && root.getFirstChild() != null) {
        if (root.getFirstChild().getString().equals(labelName)) {
          return true;
        }
      }
      return false;
    });
    return null;
  }
}