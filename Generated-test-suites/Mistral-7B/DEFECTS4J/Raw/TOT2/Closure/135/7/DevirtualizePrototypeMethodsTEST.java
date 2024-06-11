import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private CompilerPass devirtualizer;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    devirtualizer = new DevirtualizePrototypeMethods(compiler);
  }

  /**
   * Test for a simple prototype method with no arguments.
   */
  @Test
  public void testSimplePrototypeMethodNoArgs() {
    String jsCode = "function A() {} A.prototype.test = function() { return 'Hello'; }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);

    Node testFunction = getFunctionByName("JSCompiler_StaticMethods_test", root);
    assertSame(NodeOperations.createFunctionDeclaration(
        "test", "function() { return 'Hello'; }", null, null), testFunction);
  }

  /**
   * Test for a prototype method with one argument.
   */
  @Test
  public void testPrototypeMethodOneArg() {
    String jsCode = "function A(a) {} A.prototype.test = function(b) { return a + b; }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);

    Node testFunction = getFunctionByName("JSCompiler_StaticMethods_test", root);
    Node testFunctionThis = testFunction.getFirstChild();
    assertSame(NodeOperations.createVariableDeclaration("self", null, null), testFunctionThis);
    Node testFunctionArguments = testFunction.getLastChild();
    assertEquals(2, testFunctionArguments.getNumChildren());
    Node argA = testFunctionArguments.getChild(0);
    Node argB = testFunctionArguments.getChild(1);
    assertSame(argA, testFunctionThis.getNextSibling());
    assertSame(NodeOperations.createIdentifier("a"), argA);
    assertSame(argB, testFunctionArguments.getNextSibling());
    assertSame(NodeOperations.createIdentifier("b"), argB);
  }

  /**
   * Test for a prototype method with multiple arguments.
   */
  @Test
  public void testPrototypeMethodMultiArgs() {
    String jsCode = "function A(a, b, c) {} A.prototype.test = function(x, y, z) { return x + y + z; }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);

    Node testFunction = getFunctionByName("JSCompiler_StaticMethods_test", root);
    Node testFunctionThis = testFunction.getFirstChild();
    assertSame(NodeOperations.createVariableDeclaration("self", null, null), testFunctionThis);
    Node testFunctionArguments = testFunction.getLastChild();
    assertEquals(4, testFunctionArguments.getNumChildren());
    Node argA = testFunctionArguments.getChild(0);
    Node argX = testFunctionArguments.getChild(1);
    Node argB = testFunctionArguments.getChild(2);
    Node argC = testFunctionArguments.getChild(3);
    assertSame(argA, testFunctionThis.getNextSibling());
    assertSame(NodeOperations.createIdentifier("a"), argA);
    assertSame(argX, testFunctionArguments.getNextSibling());
    assertSame(NodeOperations.createIdentifier("x"), argX);
    assertSame(argB, testFunctionArguments.getNextSibling());
    assertSame(NodeOperations.createIdentifier("y"), argB);
    assertSame(argC, testFunctionArguments.getNextSibling());
    assertSame(NodeOperations.createIdentifier("c"), argC);
  }

  /**
   * Test for a prototype method with a name collision.
   */
  @Test
  public void testPrototypeMethodNameCollision() {
    String jsCode = "function A() {} A.prototype.test = function() { return 1; }; function B() {} B.prototype.test = function() { return 2; }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);

    Node testA = getFunctionByName("JSCompiler_StaticMethods_test", root);
    Node testB = getFunctionByName("JSCompiler_StaticMethods_test_B", root);
    Node testAThis = testA.getFirstChild();
    Node testBThis = testB.getFirstChild();
    assertSame(NodeOperations.createVariableDeclaration("self", null, null), testAThis);
    assertSame(NodeOperations.createVariableDeclaration("self_B", null, null), testBThis);
    Node testAArguments = testA.getLastChild();
    Node testBArguments = testB.getLastChild();
    assertSame(NodeOperations.createFunctionDeclaration("test", "function() { return 1; }", null, null), testA);
    assertSame(NodeOperations.createFunctionDeclaration("test", "function() { return 2; }", null, null), testB);
    assertSame(Token.NAME, testAArguments.getType().getTokenType());
    assertSame(Token.NAME, testBArguments.getType().getTokenType());
  }

  /**
   * Test for a prototype method with a return statement.
   */
  @Test
  public void testPrototypeMethodReturn() {
    String jsCode = "function A() {} A.prototype.test = function() { return 1; }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);

    Node testFunction = getFunctionByName("JSCompiler_StaticMethods_test", root);
    Node testFunctionThis = testFunction.getFirstChild();
    Node testFunctionBody = testFunction.getLastChild();
    Node testFunctionReturn = testFunctionBody.getLastChild();
    assertSame(NodeOperations.createVariableDeclaration("self", null, null), testFunctionThis);
    assertSame(Token.RETURN, testFunctionReturn.getType().getTokenType());
    assertSame(NodeOperations.createNumber(1), testFunctionReturn.getFirstChild());
  }

  /**
   * Test for a prototype method with an exception.
   */
  @Test(expected = AssertionError.class)
  public void testPrototypeMethodException() {
    String jsCode = "function A() {} A.prototype.test = function() { throw new Error('Test error'); }";
    Node root = NodeOperations.parseTree(jsCode);

    devirtualizer.process(null, root);
  }

  /**
   * Helper method to get a function by its name.
   */
  private Node getFunctionByName(String name, Node root) {
    for (Node function = root.getFirstChild(); function != null;
         function = function.getNextSibling()) {
      if (NodeUtil.isFunction(function) &&
          function.getLastChild().getString().equals(name)) {
        return function;
      }
    }
    throw new AssertionError("Function not found: " + name);
  }
}