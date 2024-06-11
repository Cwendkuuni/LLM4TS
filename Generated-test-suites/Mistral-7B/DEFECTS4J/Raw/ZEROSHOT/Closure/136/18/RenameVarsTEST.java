import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.isLocal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.*;

/**
 * RenameVarsTest class to test the RenameVars compiler pass.
 */
public class RenameVarsTest {
  @SuppressWarnings("unchecked")
  private static Node createSourceNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    NodeFactory nodeFactory = new NodeFactory(options);
    return nodeFactory.parseTree(code, null, null);
  }

  /**
   * Tests the RenameVars compiler pass.
   */
  @Test
  public void testRenameVars() {
    // Create an empty compiler and pass in the options.
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackTraces();

    // Set up the RenameVars compiler pass.
    String prefix = "";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = null;
    Set<String> reservedNames = Sets.newHashSet();
    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    // Create a source node for the test.
    String sourceCode = "function foo() { var x = 1; }";
    Node sourceNode = createSourceNode(sourceCode);

    // Perform the renaming pass.
    renameVars.process(null, sourceNode);

    // Verify the expected output.
    Node expectedOutput = createSourceNode("function foo() { var a = 1; }");

    // Compare the output nodes.
    NodeTraversal.traverse(compiler, expectedOutput, new NodeMatcher(sourceNode));

    // Verify the variable assignments.
    VariableMap variableMap = renameVars.getVariableMap();
    ListMultimap<String, String> assignments = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      String oldName = entry.getKey();
      String newName = entry.getValue();
      assignments.put(oldName, newName);
    }
    assertEquals("Expected assignments:\n" + assignments,
        "Expected assignments:\n{ x=a }");
  }

  /**
   * Helper class to compare two nodes for equality.
   */
  private static class NodeMatcher extends NodeTraversal.AbstractPostOrderCallback {
    private final Node expectedNode;

    NodeMatcher(Node node) {
      this.expectedNode = node;
    }

    @Override
    public void visit(NodeTraversal traversal, Node currentNode, Node parentNode) {
      assertEquals("Unexpected node type", expectedNode.getType(), currentNode.getType());
      assertEquals("Unexpected node string", expectedNode.getString(), currentNode.getString());
    }
  }
}