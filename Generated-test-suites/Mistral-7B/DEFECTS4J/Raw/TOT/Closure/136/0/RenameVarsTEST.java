import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String jsCode = "function foo(a, b) {" +
        "  var c = function(d, e) { return a; }" +
        "  return c(b, 10);" +
        "}";

    CompilerInput input = new CompilerInput(null, jsCode, null);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = Node.root();
    Node root = Node.parse(jsCode);
    renameVars.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(compiler, externs, root);
    traversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("c")) {
          String newName = renameVars.getVariableMap().lookupName(n.getString());
          assertEquals("Expected new name for function c", "a", newName);
        }
      }
    });
  }

  @Test
  public void testRenameVarsLocalVariables() {
    String jsCode = "function foo() {" +
        "  var a = 1, b = 2, c = function(x, y) { return a; }" +
        "}" +
        "function bar() {" +
        "  var a = 3, b = 4, c = function(x, y) { return b; }" +
        "}" +
        "function baz() {" +
        "  var a = 1, b = 2, c = function(x, y) { return a; }" +
        "  return c(3, 4);" +
        "}";

    CompilerInput input = new CompilerInput(null, jsCode, null);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = Node.root();
    Node root = Node.parse(jsCode);
    renameVars.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(compiler, externs, root);

    int aCount = 0, bCount = 0, cCount = 0;
    traversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("a")) {
          aCount++;
        } else if (n.getType() == Token.NAME && n.getString().equals("b")) {
          bCount++;
        } else if (n.getType() == Token.FUNCTION_DECL &&
            ((FunctionNode) n).getName().equals("c")) {
          cCount++;
        }
      }
    });

    assertEquals("Expected 3 a variables", 3, aCount);
    assertEquals("Expected 3 b variables", 3, bCount);
    assertEquals("Expected 3 c functions", 3, cCount);

    NodeTraversal traversal2 = new NodeTraversal(compiler, externs, root);
    traversal2.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("a")) {
          String newName = renameVars.getVariableMap().lookupName(n.getString());
          assertTrue("Expected new name for variable a", !newName.equals("a"));
        }
      }
    });

    assertTrue("Expected at least one variable to be renamed", 
        traversal2.getRootNode().getChild(0).getString().length() > 1);
  }

  @Test
  public void testRenameVarsGlobalVariables() {
    String jsCode = "var a = 1, b = 2;";

    CompilerInput input = new CompilerInput(null, jsCode, null);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = Node.root();
    Node root = Node.parse(jsCode);
    renameVars.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(compiler, externs, root);
    traversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME) {
          String newName = renameVars.getVariableMap().lookupName(n.getString());
          if (newName != null) {
            assertEquals("Expected global variable name to be renamed",
                n.getString(), newName);
          } else {
            assertNull("Expected null for extern variable", n.getString());
          }
        }
      }
    });
  }

  @Test
  public void testRenameVarsEmptyCode() {
    String jsCode = "";

    CompilerInput input = new CompilerInput(null, jsCode, null);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = Node.root();
    Node root = Node.parse(jsCode);
    renameVars.process(externs, root);

    assertTrue("Expected empty map", renameVars.getVariableMap().isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testRenameVarsError() {
    String jsCode = "function foo() {" +
        "  var a = 1;" +
        "  try {" +
        "    throw new Error('Test error');" +
        "  } catch (e) {" +
        "    // Cannot rename variables inside try/catch blocks." +
        "    var b = e.message;" +
        "  }" +
        "}" +
        "";

    CompilerInput input = new CompilerInput(null, jsCode, null);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = Node.root();
    Node root = Node.parse(jsCode);
    renameVars.process(externs, root);
  }
}