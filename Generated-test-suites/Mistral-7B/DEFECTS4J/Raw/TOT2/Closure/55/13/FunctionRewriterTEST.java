import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import org.junit.Test;

/**
 * Test cases for FunctionRewriter class.
 */
public class FunctionRewriterTest {

  /**
   * Test empty function reduction.
   */
  @Test
  public void testEmptyFunctionReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node emptyFunction = new Node(Token.FUNCTION, 1, 0);
    emptyFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    emptyFunction.addChildToBack(emptyFunction);
    root.addChildToBack(emptyFunction);

    String expectedHelperCode = "function JSCompiler_emptyFn() {" +
                                "  return function() {}" +
                                "}";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    assertTrue(isFunctionExpression(root.getLastChild()));
    Node expectedEmptyFunction = parseHelperCode(EmptyFunctionReducer.class);
    assertEquals(expectedEmptyFunction, root.getLastChild());
  }

  /**
   * Test identity function reduction.
   */
  @Test
  public void testIdentityFunctionReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node identityFunction = new Node(Token.FUNCTION, 1, 0);
    identityFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    identityFunction.addChildToBack(new Node(Token.IDENT, 2, 0, "a"));
    identityFunction.addChildToBack(new Node(Token.RETURN, 3, 0, identityFunction));
    root.addChildToBack(identityFunction);

    String expectedHelperCode = "function JSCompiler_identityFn() {" +
                                "  return function(" +
                                FACTORY_METHOD_NAME + "_value) {" +
                                "    return " + FACTORY_METHOD_NAME + "_value" +
                                "  }" +
                                "}";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    assertTrue(isFunctionExpression(root.getLastChild()));
    Node expectedIdentityFunction = parseHelperCode(IdentityReducer.class);
    assertEquals(expectedIdentityFunction, root.getLastChild());
  }

  /**
   * Test constant function reduction.
   */
  @Test
  public void testConstantFunctionReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node constantFunction = new Node(Token.FUNCTION, 1, 0);
    constantFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    constantFunction.addChildToBack(new Node(Token.RETURN, 2, 0, new Node(Token.INT_LITERAL, 3, 0, 10)));
    root.addChildToBack(constantFunction);

    String expectedHelperCode = "function JSCompiler_returnArg(" +
                                "returnArgValue) {" +
                                "  return function() {" +
                                "    return returnArgValue" +
                                "  }" +
                                "}";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    assertTrue(isFunctionExpression(root.getLastChild()));
    Node expectedConstantFunction = parseHelperCode(ReturnConstantReducer.class);
    assertEquals(expectedConstantFunction, root.getLastChild());
  }

  /**
   * Test getter method reduction.
   */
  @Test
  public void testGetterMethodReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node getterFunction = new Node(Token.FUNCTION, 1, 0);
    getterFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    getterFunction.addChildToBack(new Node(Token.IDENT, 2, 0, "a"));
    getterFunction.addChildToBack(new Node(Token.GETPROP, 3, 0, new Node(Token.IDENT, 4, 0, "b")));
    getterFunction.addChildToBack(new Node(Token.RETURN, 5, 0, getterFunction));
    root.addChildToBack(getterFunction);

    String expectedHelperCode = "function JSCompiler_get(" +
                                FACTORY_METHOD_NAME + "_name) {" +
                                "  return function() {" +
                                "    return this[" + FACTORY_METHOD_NAME + "_name]" +
                                "  }" +
                                "}";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    assertTrue(isFunctionExpression(root.getLastChild()));
    Node expectedGetterFunction = parseHelperCode(GetterReducer.class);
    assertEquals(expectedGetterFunction, root.getLastChild());
  }

  /**
   * Test setter method reduction.
   */
  @Test
  public void testSetterMethodReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node setterFunction = new Node(Token.FUNCTION, 1, 0);
    setterFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    setterFunction.addChildToBack(new Node(Token.IDENT, 2, 0, "a"));
    setterFunction.addChildToBack(new Node(Token.GETPROP, 3, 0, new Node(Token.IDENT, 4, 0, "b")));
    setterFunction.addChildToBack(new Node(Token.ASSIGN, 5, 0,
        new Node(Token.IDENT, 6, 0, "value")));
    root.addChildToBack(setterFunction);

    String expectedHelperCode = "function JSCompiler_set(" +
                                FACTORY_METHOD_NAME + "_name) {" +
                                "  return function(" + FACTORY_METHOD_NAME + "_value) {" +
                                "this[" + FACTORY_METHOD_NAME + "_name] = " +
                                FACTORY_METHOD_NAME + "_value" +
                                "  }" +
                                "}";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    assertTrue(isFunctionExpression(root.getLastChild()));
    Node expectedSetterFunction = parseHelperCode(SetterReducer.class);
    assertEquals(expectedSetterFunction, root.getLastChild());
  }

  /**
   * Test that reductions are not applied to constant expressions.
   */
  @Test
  public void testConstantExpressionReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node constantFunction = new Node(Token.FUNCTION, 1, 0);
    constantFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    constantFunction.addChildToBack(new Node(Token.RETURN, 2, 0, new Node(Token.INT_LITERAL, 3, 0, 10)));
    root.addChildToBack(constantFunction);

    Node constantExpressionFunction = new Node(Token.FUNCTION, 1, 0);
    constantExpressionFunction.putBooleanProp(Node.IS_INITIALIZER, true);
    constantExpressionFunction.addChildToBack(new Node(Token.RETURN, 2, 0, new Node(Token.INT_LITERAL, 3, 0, 10)));
    root.addChildToBack(constantExpressionFunction);

    String expectedHelperCode = "";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    Node expectedConstantFunction = constantFunction;
    Node expectedConstantExpressionFunction = constantExpressionFunction;

    assertTrue(isFunctionExpression(root.getLastChild()));
    assertTrue(isFunctionExpression(root.getLastChild().getNext()));

    assertEquals(expectedConstantFunction, root.getLastChild());
    assertEquals(expectedConstantExpressionFunction, root.getLastChild().getNext());
  }

  /**
   * Test that reductions are not applied to non-function expressions.
   */
  @Test
  public void testNonFunctionExpressionReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node nonFunctionExpression = new Node(Token.IDENT, 1, 0, "a");
    root.addChildToBack(nonFunctionExpression);

    String expectedHelperCode = "";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    Node expectedNonFunctionExpression = nonFunctionExpression;

    assertTrue(isNode(root.getLastChild(), Node.class));
    assertEquals(expectedNonFunctionExpression, root.getLastChild());
  }

  /**
   * Test that no reductions are applied to functions with side effects.
   */
  @Test
  public void testFunctionWithSideEffectReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node functionWithSideEffect = new Node(Token.FUNCTION, 1, 0);
    functionWithSideEffect.putBooleanProp(Node.IS_INITIALIZER, true);
    functionWithSideEffect.addChildToBack(new Node(Token.ASSIGN, 2, 0, new Node(Token.IDENT, 3, 0, "a")));
    functionWithSideEffect.addChildToBack(new Node(Token.RETURN, 4, 0, new Node(Token.INT_LITERAL, 5, 0, 10)));
    root.addChildToBack(functionWithSideEffect);

    String expectedHelperCode = "";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    Node expectedFunctionWithSideEffect = functionWithSideEffect;

    assertTrue(isFunctionExpression(root.getLastChild()));
    assertEquals(expectedFunctionWithSideEffect, root.getLastChild());
  }

  /**
   * Test that no reductions are applied to functions with loops.
   */
  @Test
  public void testFunctionWithLoopReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node functionWithLoop = new Node(Token.FUNCTION, 1, 0);
    functionWithLoop.putBooleanProp(Node.IS_INITIALIZER, true);
    functionWithLoop.addChildToBack(new Node(Token.FOR, 2, 0));
    functionWithLoop.addChildToBack(new Node(Token.RETURN, 3, 0, new Node(Token.INT_LITERAL, 4, 0, 10)));
    root.addChildToBack(functionWithLoop);

    String expectedHelperCode = "";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    Node expectedFunctionWithLoop = functionWithLoop;

    assertTrue(isFunctionExpression(root.getLastChild()));
    assertEquals(expectedFunctionWithLoop, root.getLastChild());
  }

  /**
   * Test that no reductions are applied to functions with conditionals.
   */
  @Test
  public void testFunctionWithConditionalReduction() {
    AbstractCompiler compiler = new Compiler();
    Node root = new Node(Token.PROGRAM, 0, 0);
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node functionWithConditional = new Node(Token.FUNCTION, 1, 0);
    functionWithConditional.putBooleanProp(Node.IS_INITIALIZER, true);
    functionWithConditional.addChildToBack(new Node(Token.IF, 2, 0, new Node(Token.COND, 3, 0, new Node(Token.LITERAL, 4, 0, 0), new Node(Token.RETURN, 5, 0, new Node(Token.INT_LITERAL, 6, 0, 10))),
        new Node(Token.RETURN, 7, 0, new Node(Token.INT_LITERAL, 8, 0, 20))));
    root.addChildToBack(functionWithConditional);

    String expectedHelperCode = "";

    assertEquals(expectedHelperCode, compiler.getHelperCode());

    Node expectedFunctionWithConditional = functionWithConditional;

    assertTrue(isFunctionExpression(root.getLastChild()));
    assertEquals(expectedFunctionWithConditional, root.getLastChild());
  }
}