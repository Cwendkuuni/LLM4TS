import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.ACCESS;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.EXPR;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.STATEMENT;
import static com.google.javascript.jscomp.NodeUtil.isLabel;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Map;

public class RenameLabelsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testRenameLabels() {
    String input =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      break a;\n"
            + "    }\n"
            + "  }\n"
            + "  a: {\n"
            + "    b: break a;\n"
            + "  }\n"
            + "}";

    Node node = parser(input);
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, node);
    NodeTraversal traversal = new NodeTraversal(compiler, node);

    // Test that the labels were renamed.
    int depth = 1;
    Map<Integer, String> expectedLabels = Maps.newHashMap();
    expectedLabels.put(0, "a");
    expectedLabels.put(1, "b");

    traversal.traverse(new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == Token.LABEL) {
          String labelName = node.getFirstChild().getString();
          int labelDepth = depth;
          LabelInfo labelInfo = getLabelInfo(labelName);
          assertNotNull(labelInfo);
          assertEquals(expectedLabels.get(labelDepth), labelName);
        }
        super.visit(nodeTraversal, node, parent);
      }

      private LabelInfo getLabelInfo(String labelName) {
        for (Node child = nodeTraversal.currentNode().getFirstChild();
            child != null;
            child = child.getNext()) {
          if (child.getType() == Token.LABEL &&
              child.getFirstChild().getString().equals(labelName)) {
            return (LabelInfo) child.getData();
          }
        }
        return null;
      }

      private static class LabelInfo implements Node.Data {
        String labelName;

        LabelInfo(String labelName) {
          this.labelName = labelName;
        }
      }
    });

    // Test that the label references were updated.
    traversal.traverse(new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == Token.BREAK || node.getType() == Token.CONTINUE) {
          Node labelNode = node.getFirstChild();
          if (labelNode != null) {
            String labelName = labelNode.getString();
            LabelInfo labelInfo = getLabelInfo(labelName);
            assertNotNull(labelInfo);
            Node newLabelNode = node.getLastChild();
            String newLabelName = newLabelNode.getString();
            assertEquals(labelInfo.labelName, newLabelName);
          }
        }
        super.visit(nodeTraversal, node, parent);
      }

      private LabelInfo getLabelInfo(String labelName) {
        for (Node child = nodeTraversal.currentNode().getFirstChild();
            child != null;
            child = child.getNext()) {
          if (child.getType() == Token.LABEL &&
              child.getFirstChild().getString().equals(labelName)) {
            return (LabelInfo) child.getData();
          }
        }
        return null;
      }

      private static class LabelInfo implements Node.Data {
        String labelName;

        LabelInfo(String labelName) {
          this.labelName = labelName;
        }
      }
    });

    // Test that unreferenced labels were removed.
    Node expectedOutput = parser("function foo() {}");
    Node actualOutput = compiler.getProgram();
    NodeUtil.assertNodeEquals(expectedOutput, actualOutput);
  }

  private Node parser(String input) {
    Node root = new Node(Token.PROGRAM);
    Parser parser = new Parser(compiler);
    parser.parse(input, root);
    return root;
  }
}