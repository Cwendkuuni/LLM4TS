import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.prettyPrint;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceReference;

import java.io.IOException;
import java.util.List;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  public static void main(String[] args) throws Exception {
    runTest(new String[]{"ScopedAliasesTest.java"});
  }

  /**
   * Runs the tests.
   *
   * @param sourceFiles the source files to compile
   */
  private static void runTest(String[] sourceFiles) throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPES);

    Compiler compiler = new Compiler();
    compiler.setPrintDiagnosticInfo(true);

    List<JSError> errors = compiler.compile(sourceFiles, options);
    assertEquals(0, errors.size());

    // Perform alias transformations as part of the test to ensure that
    // the transformations are correct.
    Node root = compiler.getRootNode();
    new ScopedAliases(compiler, null, null).process(null, root);

    // Check that all alias definitions are used.
    for (AliasDefinition aliasDefinition : root.getDescendantsOfType(AliasDefinition.class)) {
      Node usage = findUsage(root, aliasDefinition.getName());
      assertNotNull(usage);
    }

    // Check error reporting.
    Node errorNode = root.getDescendantsOfType(ErrorNode.class).get(0);
    Diagnostics diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.getErrorCount());
    assertEquals("Error: " + errorNode.getFirstChild().getString(),
        prettyPrint(diagnostics.getErrors()[0].getErrors()[0]));
  }

  /**
   * Finds the usage of an alias in the root node.
   *
   * @param root the root node
   * @param name the name of the alias
   * @return the node using the alias, or null if it's not found
   */
  private static Node findUsage(Node root, String name) {
    for (Node node = root; node != null; node = node.getParent()) {
      if (node.getType() == Token.NAME && NodeUtil.isIdentifier(node) &&
          node.getString().equals(name)) {
        if (NodeUtil.isAssignmentOp(node.getParent())) {
          // Check if the left-hand side is an alias.
          Var var = node.getScope().getVar(name);
          if (var != null && var.isAlias()) {
            return node.getParent();
          }
        } else if (node.getType() == Token.CALL &&
            SCOPING_METHOD_NAME.equals(node.getFirstChild().getQualifiedName())) {
          // Check if the argument is an alias.
          Node argument = node.getChildAtIndex(1);
          if (argument.getType() == Token.NAME &&
              NodeUtil.isIdentifier(argument) &&
              NodeUtil.isAlias(argument, root)) {
            return node;
          }
        }
      }
    }
    return null;
  }

  /**
   * A node representing a ScopedAliases definition.
   */
  private static class AliasDefinition {
    private final String name;

    AliasDefinition(String name) {
      this.name = name;
    }
  }

  /**
   * Checks if a node is an alias.
   *
   * @param node the node to check
   * @param root the root node for context
   * @return true if the node is an alias, false otherwise
   */
  private static boolean isAlias(Node node, Node root) {
    Var var = node.getScope().getVar(node.getString());
    return var != null && var.isAlias();
  }
}

class AliasDefinition {
  // Not used
}