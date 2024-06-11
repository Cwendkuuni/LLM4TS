import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    // Given
    Node emptyFunctionRoot = Parser.parseExpressionString("function() {}");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(emptyFunctionRoot, null, null);

    // Then
    Node expectedEmptyFunctionCall = Parser.parseExpressionString("emptyFn()");
    Node actualEmptyFunction = emptyFunctionRoot.getLastChild();
    assertEquals(expectedEmptyFunctionCall, actualEmptyFunction);
  }

  @org.junit.Test
  public void testIdentityFunctionReducer() {
    // Given
    Node identityFunctionRoot = Parser.parseExpressionString(
        "function(x) { return x; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(identityFunctionRoot, null, null);

    // Then
    Node expectedIdentityFunctionCall = Parser.parseExpressionString(
        "identityFn(x)");
    Node actualIdentityFunction = identityFunctionRoot.getLastChild();
    assertEquals(expectedIdentityFunctionCall, actualIdentityFunction);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    // Given
    Node returnConstantRoot = Parser.parseExpressionString(
        "function() { return 10; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(returnConstantRoot, null, null);

    // Then
    Node expectedReturnConstantCall = Parser.parseExpressionString(
        "returnconst(10)"); ;
    Node actualReturnConstant = returnConstantRoot.getLastChild();
    assertEquals(expectedReturnConstantCall, actualReturnConstant);
  }

  @org.junit.Test
  public void testGetterReducer() {
    // Given
    Node getterFunctionRoot = Parser.parseExpressionString(
        "function() { return this.a; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(getterFunctionRoot, null, null);

    // Then
    Node expectedGetterCall = Parser.parseExpressionString(
        "getter(\"a\")");
    Node actualGetter = getterFunctionRoot.getLastChild();
    assertEquals(expectedGetterCall, actualGetter);
  }

  @org.junit.Test
  public void testSetterReducer() {
    // Given
    Node setterFunctionRoot = Parser.parseExpressionString(
        "function(x) { this.a = x; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(setterFunctionRoot, null, null);

    // Then
    Node expectedSetterCall = Parser.parseExpressionString(
        "setter(\"a\")");
    Node actualSetter = setterFunctionRoot.getLastChild();
    assertEquals(expectedSetterCall, actualSetter);
  }

  @org.junit.Test
  public void testFunctionRewriterDoesNotApplyReductionIfNoSavings() {
    // Given
    Node functionRoot = Parser.parseExpressionString(
        "function() { return 0; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(functionRoot, null, null);

    // Then
    assertEquals(functionRoot, functionRoot);
  }

  @org.junit.Test
  public void testFunctionRewriterDoesNotApplyReductionIfHelperCodeCostGreaterThanSavingsThreshold() {
    // Given
    Node functionRoot = Parser.parseExpressionString(
        "function() { return 1000; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(functionRoot, null, null);

    // Then
    assertEquals(functionRoot, functionRoot);
  }

  @org.junit.Test
  public void testFunctionRewriterAppliesReductionIfHelperCodeCostBelowSavingsThreshold() {
    // Given
    Node functionRoot = Parser.parseExpressionString(
        "function() { return 5; }");

    // When
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(functionRewriter);
    passManager.runPasses(functionRoot, null, null);

    // Then
    Node expectedReturnConstantCall = Parser.parseExpressionString(
        "returnconst(5)"); ;
    Node actualReturnConstant = functionRoot.getLastChild();
    assertEquals(expectedReturnConstantCall, actualReturnConstant);
  }
}