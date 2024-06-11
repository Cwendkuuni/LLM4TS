import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  var a;" +
                        "  var b = 1;" +
                        "  var c = 2;" +
                        "  var d = 3;" +
                        "  return a + b + c + d;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Scope functionScope = function.asFunction().getFunctionScope();

    assertEquals(1, functionScope.getVars().size());
    Var a = functionScope.getVar("a");
    Var b = functionScope.getVar("b");
    Var c = functionScope.getVar("c");
    Var d = functionScope.getVar("d");

    assertEquals(Token.VAR, a.getNode().getNodeType());
    assertEquals(Token.ASSIGN, b.getNode().getNodeType());
    assertEquals(Token.VAR, c.getNode().getNodeType());
    assertNull(d.getNode());

    assertEquals("a, b = 1, c = 2", function.toString());
  }

  @Test
  public void testCollapseMultipleVariableDeclarationsWithSameType() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  var a, b = 1, c = 2, d = 3;" +
                        "  return a + b + c + d;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Scope functionScope = function.asFunction().getFunctionScope();

    assertEquals(1, functionScope.getVars().size());
    Var a = functionScope.getVar("a");
    Var b = functionScope.getVar("b");
    Var c = functionScope.getVar("c");
    Var d = functionScope.getVar("d");

    assertEquals(Token.VAR, a.getNode().getNodeType());
    assertEquals(Token.ASSIGN, b.getNode().getNodeType());
    assertEquals(Token.ASSIGN, c.getNode().getNodeType());
    assertNull(d.getNode());

    assertEquals("a, b = 1, c = 2", function.toString());
  }

  @Test
  public void testCollapseMultipleVariableDeclarationsWithDifferentTypes() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  var a = 1;" +
                        "  var b;" +
                        "  var c = 2;" +
                        "  return a + b + c;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Scope functionScope = function.asFunction().getFunctionScope();

    assertEquals(3, functionScope.getVars().size());
    Var a = functionScope.getVar("a");
    Var b = functionScope.getVar("b");
    Var c = functionScope.getVar("c");

    assertEquals(Token.ASSIGN, a.getNode().getNodeType());
    assertEquals(Token.VAR, b.getNode().getNodeType());
    assertEquals(Token.ASSIGN, c.getNode().getNodeType());

    assertEquals("a, b = 1, c = 2", function.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationWithInitializerInsideFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  function bar() {" +
                        "    var a = 1;" +
                        "  }" +
                        "  return a;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    assertThrows(AssertionError.class, () -> {
      NodeTraversal traversal2 = new NodeTraversal(function, compiler);
      traversal2.traverse();
    });
  }

  @Test
  public void testCollapseVariableDeclarationsWithInsideFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  function bar() {" +
                        "    var a;" +
                        "    var b = 1;" +
                        "    var c = 2;" +
                        "  }" +
                        "  return bar();" +
                        "}" +
                        "function bar() {" +
                        "  return 1;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Node functionScope = function.getFirstChild();
    Node barFunction = traversal.findFunctionByName("bar");
    Node barFunctionScope = barFunction.getFirstChild();

    assertEquals(2, functionScope.getChildCount());
    assertEquals(3, barFunctionScope.getChildCount());

    assertEquals("function bar() {var a, b = 1, c = 2; return 1;}",
                 barFunction.toString());

    assertEquals("function foo() {function bar() {var a, b = 1, c = 2;}; return bar();}",
                function.toString());
  }

  @Test
  public void testCollapseVariableAssignments() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  var a = 1;" +
                        "  var b = 1;" +
                        "  a = 2;" +
                        "  b = 2;" +
                        "  return a + b;" +
                        "}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Scope functionScope = function.asFunction().getFunctionScope();

    assertEquals(2, functionScope.getVars().size());
    Var a = functionScope.getVar("a");
    Var b = functionScope.getVar("b");

    assertEquals(Token.ASSIGN, a.getNode().getNodeType());
    assertEquals(Token.ASSIGN, b.getNode().getNodeType());

    assertEquals("var a = 1, b = 1; a = 2, b = 2", function.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithInlineFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {" +
                        "  var bar = function() {};" +
                        "  var a, b = 1, c = 2;" +
                        "  return a + b + c;" +
                        "}" +
                        "function bar() {}";

    Node root = AbstractCompiler.parse(sourceCode, null, null);
    compiler.process(root, options);

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    Node function = traversal.findFunctionByName("foo");
    Scope functionScope = function.asFunction().getFunctionScope();

    assertEquals(3, functionScope.getVars().size());
    Var a = functionScope.getVar("a");
    Var b = functionScope.getVar("b");

    assertEquals(Token.VAR, a.getNode().getNodeType());
    assertEquals(Token.ASSIGN, b.getNode().getNodeType());

    assertEquals("function foo() {var bar = function() {}, a, b = 1, c = 2; return a + b + c;}",
                function.toString());
  }
}