import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessType;
import com.google.javascript.rhino.NodeFactory;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    // Create a Compiler instance, with default options.
    Compiler compiler = new Compiler();
    CompilerOptions options = compiler.getOptions();
    options.setProcessType(ProcessType.COMPILE);

    // Create an empty NodeFactory, to be able to create Nodes for testing.
    NodeFactory nodeFactory = NodeFactory.getInitial();

    // Create the source code as a NodeTree for testing.
    Node source = createLabelSource(nodeFactory);

    // Process the NodeTree through the RenameLabels CompilerPass.
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(nodeFactory.getExterns(), source);

    // Check the results of the RenameLabels CompilerPass.
    assertFalse("There should be no unused labels.", renameLabels.hasUnusedLabels());
    NodeTraversal nodeTraversal = nodeFactory.createNodeTraversal(source);
    NodeTraversal.traverse(compiler, source, new CheckLabelRenaming(nodeTraversal));
  }

  private Node createLabelSource(NodeFactory nodeFactory) {
    // Create the source code as a NodeTree for testing.
    Node root = nodeFactory.createProgram(
        nodeFactory.createBlock(
            nodeFactory.createExpressionStatement(
                nodeFactory.createCall(
                    nodeFactory.createIdentifier("bar"),
                    Lists.newArrayList(nodeFactory.createNull())),
                nodeFactory.createLabel("a")),
            nodeFactory.createBlock(
                nodeFactory.createLabel("b:"),
                nodeFactory.createStatement(
                    nodeFactory.createExpressionStatement(
                        nodeFactory.createCall(
                            nodeFactory.createIdentifier("foo"),
                            Lists.newArrayList(nodeFactory.createNull())),
                        nodeFactory.createLabel("c"))),
                nodeFactory.createBlock(
                    nodeFactory.createLabel("d:"),
                    nodeFactory.createStatement(
                        nodeFactory.createExpressionStatement(
                            nodeFactory.createCall(
                                nodeFactory.createIdentifier("break"),
                                Lists.newArrayList(
                                    nodeFactory.createGetProperty(
                                        nodeFactory.createThis(),
                                        nodeFactory.createIdentifier("$0")),
                                    nodeFactory.createIdentifier("a")))),
                        nodeFactory.createLabel("e"))),
                nodeFactory.createBlock(
                    nodeFactory.createLabel("f:"),
                    nodeFactory.createStatement(
                        nodeFactory.createExpressionStatement(
                            nodeFactory.createCall(
                                nodeFactory.createIdentifier("continue"),
                                Lists.newArrayList(
                                    nodeFactory.createGetProperty(
                                        nodeFactory.createThis(),
                                        nodeFactory.createIdentifier("$0")),
                                    nodeFactory.createIdentifier("b")))),
                        nodeFactory.createLabel("g"))))));

    return root;
  }

  private static class CheckLabelRenaming implements NodeTraversal.Callback {

    private final NodeTraversal nodeTraversal;

    CheckLabelRenaming(NodeTraversal nodeTraversal) {
      this.nodeTraversal = nodeTraversal;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
        Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
      switch (node.getType()) {
        case Token.LABEL:
          String name = node.getFirstChild().getString();
          LabelInfo li = RenameLabelsTest.getLabelInfo(name);
          if (li != null && !li.referenced) {
            // A label that was not referenced should be removed.
            assertNull("Expected label '" + name + "' to be removed.", node);
          } else {
            // A label that was referenced should have the short name.
            String shortName = RenameLabelsTest.getNameForId(li.id);
            assertTrue("Expected label '" + name + "' to have the short name '" + shortName + "'.", node.getFirstChild().getString().equals(shortName));
          }
          break;
      }
    }
  }

  /**
   * @param name The name to retrieve information about.
   * @return The structure representing the name in the current context.
   */
  private static LabelInfo getLabelInfo(String name) {
    return RenameLabelsTest.namespaceStack.peek().renameMap.get(name);
  }

  /**
   * @param id The id, which is the depth of the label in the current context,
   *        for which to get a short name.
   * @return The short name of the identified label.
   */
  private static String getNameForId(int id) {
    return RenameLabelsTest.names.get(id - 1);
  }

  private static Map<String, LabelInfo> renameMap;
  private static ArrayList<String> names;
  private static int idCounter = 0;

  static {
    renameMap = new HashMap<String, LabelInfo>();
    names = new ArrayList<String>();
  }

  static class LabelNamespace {
    Map<String, LabelInfo> renameMap = this.renameMap;
  }

  static {
    // Create the root label namespace.
    LabelNamespace root = new LabelNamespace();
    RenameLabelsTest.renameMap = root.renameMap;
  }
}