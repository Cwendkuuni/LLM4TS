import static com.google.javascript.jscomp.NodeUtil.isFunctionExpression;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isImmutableValue;
import static com.google.javascript.jscomp.NodeUtil.isGetProp;
import static com.google.javascript.jscomp.NodeUtil.isThis;
import static com.google.javascript.jscomp.NodeUtil.isExprAssign;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class FunctionRewriterTest {

  private static Node createTestFunction(String functionName, int lineNumber,
                                        int charPositionInLine, String body) {
    String funcSource =
        "function " + functionName + "() {" + body + "}";
    Node root = Compiler.newCompiler().compile(null, funcSource, null);
    assertEquals(lineNumber, root.getLineNumber());
    assertEquals(charPositionInLine, root.getCharPositionInLine());
    return root;
  }

  @Test
  public void testEmptyFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test empty function reduction
    Node emptyFunction = createTestFunction("emptyFunction", 2, 1, "");
    Node result = functionRewriter.process(null, emptyFunction).getRoot();
    Node expectedHelperCall =
        NodeUtil.getFirstDescendantByType(result, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_emptyFn", expectedHelperCall.getFirstChild().getString());
  }

  @Test
  public void testIdentityFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test identity function reduction
    Node identityFunction = createTestFunction("identityFunction", 2, 1,
                                            "return this;");
    Node result = functionRewriter.process(null, identityFunction).getRoot();
    Node expectedHelperCall =
        NodeUtil.getFirstDescendantByType(result, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_identityFn", expectedHelperCall.getFirstChild().getString());
  }

  @Test
  public void testReturnConstantReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test return constant reduction
    Node returnConstantFunction = createTestFunction("returnConstantFunction", 2, 1,
                                                  "return 10;");
    Node result = functionRewriter.process(null, returnConstantFunction).getRoot();
    Node expectedHelperCall =
        NodeUtil.getFirstDescendantByType(result, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_returnArg", expectedHelperCall.getFirstChild().getString());
    assertEquals(10, expectedHelperCall.getLastChild().getNumber());
  }

  @Test
  public void testGetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test getter reduction
    Node getterFunction = createTestFunction("getterFunction", 2, 1,
                                           "return this.a;");
    Node result = functionRewriter.process(null, getterFunction).getRoot();
    Node expectedHelperCall =
        NodeUtil.getFirstDescendantByType(result, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_get", expectedHelperCall.getFirstChild().getString());
    assertEquals("a", expectedHelperCall.getLastChild().getString());
  }

  @Test
  public void testSetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test setter reduction
    Node setterFunction = createTestFunction("setterFunction", 2, 1,
                                           "this.a = value;");
    Node result = functionRewriter.process(null, setterFunction).getRoot();
    Node expectedHelperCall =
        NodeUtil.getFirstDescendantByType(result, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_set", expectedHelperCall.getFirstChild().getString());
    assertEquals("a", expectedHelperCall.getLastChild(0).getString());
  }

  @Test
  public void testComplexFunction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test complex function with no reduction
    String complexFunctionSource =
        "function complexFunction(a, b) {" +
        "  var result = a + b; " +
        "  return result; " +
        "}";
    Node complexFunction = Compiler.newCompiler().compile(null, complexFunctionSource, null).getRoot();
    Node complexFunctionResult = functionRewriter.process(null, complexFunction).getRoot();
    assertEquals(complexFunction, complexFunctionResult);

    // Test complex function with identity reduction
    String identityFunctionSource =
        "function identityFunction(a) {" +
        "  return a;" +
        "}";
    Node identityFunction = Compiler.newCompiler().compile(null, identityFunctionSource, null).getRoot();
    Node identityFunctionResult = functionRewriter.process(null, identityFunction).getRoot();
    Node expectedHelperCall = NodeUtil.getFirstDescendantByType(identityFunctionResult, Token.CALL);
    assertNotNull(expectedHelperCall);
    assertEquals("JSCompiler_identityFn", expectedHelperCall.getFirstChild().getString());
  }

  @Test
  public void testFunctionWithArguments() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test function with arguments
    String functionWithArgumentsSource =
        "function testFunction(a, b) {" +
        "  return a + b;" +
        "}";
    Node functionWithArguments = Compiler.newCompiler().compile(null, functionWithArgumentsSource, null).getRoot();
    Node functionWithArgumentsResult = functionRewriter.process(null, functionWithArguments).getRoot();
    assertEquals(functionWithArguments, functionWithArgumentsResult);
  }

  @Test
  public void testFunctionWithLoops() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test function with loops
    String functionWithLoopsSource =
        "function testFunction() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    for (var j = 0; j < 10; j++) {" +
        "      var sum = i + j;" +
        "      return sum;" +
        "    }" +
        "  }" +
        "}";
    Node functionWithLoops = Compiler.newCompiler().compile(null, functionWithLoopsSource, null).getRoot();
    Node functionWithLoopsResult = functionRewriter.process(null, functionWithLoops).getRoot();
    assertEquals(functionWithLoops, functionWithLoopsResult);
  }

  @Test
  public void testFunctionWithConditionalStatements() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test function with conditional statements
    String functionWithConditionalStatementsSource =
        "function testFunction() {" +
        "  var a = 10;" +
        "  if (a > 5) {" +
        "    return 1;" +
        "  } else {" +
        "    return 0;" +
        "  }" +
        "}";
    Node functionWithConditionalStatements = Compiler.newCompiler().compile(null, functionWithConditionalStatementsSource, null).getRoot();
    Node functionWithConditionalStatementsResult = functionRewriter.process(null, functionWithConditionalStatements).getRoot();
    assertEquals(functionWithConditionalStatements, functionWithConditionalStatementsResult);
  }

  @Test
  public void testFunctionWithReturnTypeAnnotation() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test function with return type annotation
    String functionWithReturnTypeAnnotationSource =
        "@CompilableType('Number')" +
        "function testFunction() {" +
        "  return 1;" +
        "}";
    Node functionWithReturnTypeAnnotation = Compiler.newCompiler().compile(null, functionWithReturnTypeAnnotationSource, null).getRoot();
    Node functionWithReturnTypeAnnotationResult = functionRewriter.process(null, functionWithReturnTypeAnnotation).getRoot();
    assertEquals(functionWithReturnTypeAnnotation, functionWithReturnTypeAnnotationResult);
  }

  @Test
  public void testFunctionWithThrowStatement() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    // Test function with throw statement
    String functionWithThrowStatementSource =
        "function testFunction() {" +
        "  throw new Error('Test error');" +
        "}";
    Node functionWithThrowStatement = Compiler.newCompiler().compile(null, functionWithThrowStatementSource, null).getRoot();
    try {
      Node functionWithThrowStatementResult = functionRewriter.process(null, functionWithThrowStatement).getRoot();
      fail("Expected IllegalStateException: Reduction not applicable to throw statements.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}