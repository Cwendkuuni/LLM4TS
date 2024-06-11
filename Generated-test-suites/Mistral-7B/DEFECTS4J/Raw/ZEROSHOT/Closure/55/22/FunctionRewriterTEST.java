import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.InlineCostEstimator;

import java.util.List;
import java.util.Map;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private InlineCostEstimator costEstimator;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    costEstimator = mock(InlineCostEstimator.class);
    when(compiler.getInlineCostEstimator()).thenReturn(costEstimator);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    // No savings: empty function vs empty function
    Node emptyFunction1 = Node.newFunction(null, null, null);
    Node emptyFunction2 = Node.newFunction(null, null, null);
    Node emptyFunctionRoot = Node.newRootNode(emptyFunction1);

    rewriter.process(Node.newTextNode(""), emptyFunctionRoot);

    Node actual = emptyFunctionRoot;
    Node expected = Node.newRootNode(rewriter.parseHelperCode(EmptyFunctionReducer.class).removeFirstChild());
    assertEquals(expected, actual);

    // Savings: empty function vs helper function
    Node helperCode = Node.newRootNode(rewriter.parseHelperCode(EmptyFunctionReducer.class).removeFirstChild());
    when(costEstimator.getCost(helperCode)).thenReturn(10);

    Node savingsFunction = Node.newFunction(null, null, null);
    Node savingsFunctionRoot = Node.newRootNode(savingsFunction);

    rewriter.process(Node.newTextNode(""), savingsFunctionRoot);

    actual = savingsFunctionRoot;
    expected = Node.newRootNode(helperCode);
    assertEquals(expected, actual);

    int savings = rewriter.estimateSavings(emptyFunction1, emptyFunction2);
    assertEquals(-10, savings);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    // No savings: identity function vs identity function
    Node identityFunction1 = Node.newFunction(
        Node.newList(Node.newName("value"), null), null,
        Node.newReturn(Node.newName("value"))) ;
    Node identityFunction2 = Node.newFunction(
        Node.newList(Node.newName("value"), null), null,
        Node.newReturn(Node.newName("value"))) ;
    Node identityFunctionRoot1 = Node.newRootNode(identityFunction1);
    Node identityFunctionRoot2 = Node.newRootNode(identityFunction2);

    rewriter.process(Node.newTextNode(""), identityFunctionRoot1);

    Node actual = identityFunctionRoot1;
    Node expected = identityFunctionRoot2;
    assertEquals(expected, actual);

    // Savings: identity function vs helper function
    when(costEstimator.getCost(identityFunction1)).thenReturn(10);

    Node helperCode = rewriter.parseHelperCode(IdentityReducer.class).removeFirstChild();
    Node helperFunction = Node.newFunction(null, null, null);
    helperFunction.addChildrenToBack(helperCode);

    rewriter.process(Node.newTextNode(""), identityFunctionRoot1);

    actual = identityFunctionRoot1;
    expected = Node.newRootNode(helperFunction);
    assertEquals(expected, actual);

    int savings = rewriter.estimateSavings(identityFunction1, identityFunction2);
    assertEquals(-10, savings);

    // No savings: non-identity function vs identity function
    Node nonIdentityFunction = Node.newFunction(
        Node.newList(Node.newName("value"), null), null,
        Node.newReturn(Node.newNumber(1))) ;
    Node nonIdentityFunctionRoot = Node.newRootNode(nonIdentityFunction);

    rewriter.process(Node.newTextNode(""), nonIdentityFunctionRoot);

    actual = nonIdentityFunctionRoot;
    expected = identityFunctionRoot2;
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(nonIdentityFunction, identityFunction2);
    assertEquals(0, savings);
  }

  private int estimateSavings(Node oldNode, Node newNode) {
    return rewriter.estimateSavings(oldNode, newNode, compiler, costEstimator);
  }

  private FunctionRewriter rewriter() {
    return new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testSingleReturnStatementReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    // No savings: return constant vs return constant
    Node constantFunction1 = Node.newFunction(null, null, null);
    Node constantFunction1Value = Node.newNumber(1);
    Node constantFunction1Root = Node.newRootNode(constantFunction1);
    constantFunction1.addChildrenToBack(constantFunction1Value);

    Node constantFunction2 = Node.newFunction(null, null, null);
    Node constantFunction2Value = Node.newNumber(1);
    Node constantFunction2Root = Node.newRootNode(constantFunction2);
    constantFunction2.addChildrenToBack(constantFunction2Value);

    rewriter.process(Node.newTextNode(""), constantFunction1Root);

    Node actual = constantFunction1Root;
    Node expected = constantFunction2Root;
    assertEquals(expected, actual);

    int savings = rewriter.estimateSavings(constantFunction1, constantFunction2);
    assertEquals(0, savings);

    // Savings: return constant vs helper function
    when(costEstimator.getCost(constantFunction1)).thenReturn(10);

    Node helperCode = rewriter.parseHelperCode(ReturnConstantReducer.class).removeFirstChild();
    Node helperFunction = Node.newFunction(null, null, null);
    helperFunction.addChildrenToBack(helperCode);

    rewriter.process(Node.newTextNode(""), constantFunction1Root);

    actual = constantFunction1Root;
    expected = Node.newRootNode(helperFunction);
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(constantFunction1, constantFunction2);
    assertEquals(-10, savings);

    // No savings: non-constant return vs non-constant return
    Node nonConstantFunction1 = Node.newFunction(null, null, null);
    Node nonConstantFunction1Value1 = Node.newNumber(1);
    Node nonConstantFunction1Value2 = Node.newNumber(2);
    Node nonConstantFunction1Root = Node.newRootNode(nonConstantFunction1);
    nonConstantFunction1.addChildrenToBack(nonConstantFunction1Value1);
    nonConstantFunction1.addChildrenToBack(nonConstantFunction1Value2);

    Node nonConstantFunction2 = Node.newFunction(null, null, null);
    Node nonConstantFunction2Value1 = Node.newNumber(1);
    Node nonConstantFunction2Value2 = Node.newNumber(2);
    Node nonConstantFunction2Root = Node.newRootNode(nonConstantFunction2);
    nonConstantFunction2.addChildrenToBack(nonConstantFunction2Value1);
    nonConstantFunction2.addChildrenToBack(nonConstantFunction2Value2);

    rewriter.process(Node.newTextNode(""), nonConstantFunction1Root);

    actual = nonConstantFunction1Root;
    expected = nonConstantFunction2Root;
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(nonConstantFunction1, nonConstantFunction2);
    assertEquals(0, savings);

    // Savings: return constant vs helper function with different arguments
    when(costEstimator.getCost(constantFunction1)).thenReturn(10);

    Node helperCode2 = Node.newFunction(
        Node.newList(Node.newName("arg"), null), null,
        Node.newReturn(Node.newName("arg")))
        .removeFirstChild();

    rewriter.process(Node.newTextNode(""), constantFunction1Root);

    actual = constantFunction1Root;
    expected = Node.newRootNode(helperCode2);
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(constantFunction1, Node.newFunction(
        Node.newList(Node.newName("arg"), null), null,
        Node.newReturn(Node.newName("arg"))));
    assertEquals(-10, savings);
  }

  @org.junit.Test
  public void testGetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    // No savings: getter vs getter
    Node getterFunction1 = Node.newFunction(null, null, null);
    Node getterFunction1Prototype = Node.newObjectProperty(
        Node.newName("prototype"), getterFunction1, null);
    Node getterFunction1Value1 = Node.newName("value1");
    Node getterFunction1PrototypeSetter = Node.newObjectProperty(
        Node.newName("prototype"), getterFunction1, getterFunction1Value1);
    Node getterFunction1Root = Node.newRootNode(getterFunction1);

    Node getterFunction2 = Node.newFunction(null, null, null);
    Node getterFunction2Prototype = Node.newObjectProperty(
        Node.newName("prototype"), getterFunction2, null);
    Node getterFunction2Value1 = Node.newName("value1");
    Node getterFunction2PrototypeSetter = Node.newObjectProperty(
        Node.newName("prototype"), getterFunction2, getterFunction2Value1);
    Node getterFunction2Root = Node.newRootNode(getterFunction2);

    rewriter.process(Node.newTextNode(""), getterFunction1Root);

    Node actual = getterFunction1Root;
    Node expected = getterFunction2Root;
    assertEquals(expected, actual);

    int savings = rewriter.estimateSavings(getterFunction1, getterFunction2);
    assertEquals(0, savings);

    // Savings: getter vs helper function
    when(costEstimator.getCost(getterFunction1)).thenReturn(10);

    Node helperCode = rewriter.parseHelperCode(GetterReducer.class).removeFirstChild();

    rewriter.process(Node.newTextNode(""), getterFunction1Root);

    actual = getterFunction1Root;
    expected = Node.newRootNode(helperCode);
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(getterFunction1, getterFunction2);
    assertEquals(-10, savings);
  }

  @org.junit.Test
  public void testSetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    // No savings: setter vs setter
    Node setterFunction1 = Node.newFunction(
        Node.newList(Node.newName("value"), null), null, null);
    Node setterFunction1Prototype = Node.newObjectProperty(
        Node.newName("prototype"), setterFunction1, null);
    Node setterFunction1PrototypeSetter = Node.newObjectProperty(
        Node.newName("prototype"), setterFunction1, null);
    Node setterFunction1Root = Node.newRootNode(setterFunction1);

    Node setterFunction2 = Node.newFunction(
        Node.newList(Node.newName("value"), null), null, null);
    Node setterFunction2Prototype = Node.newObjectProperty(
        Node.newName("prototype"), setterFunction2, null);
    Node setterFunction2PrototypeSetter = Node.newObjectProperty(
        Node.newName("prototype"), setterFunction2, null);
    Node setterFunction2Root = Node.newRootNode(setterFunction2);

    rewriter.process(Node.newTextNode(""), setterFunction1Root);

    Node actual = setterFunction1Root;
    Node expected = setterFunction2Root;
    assertEquals(expected, actual);

    int savings = rewriter.estimateSavings(setterFunction1, setterFunction2);
    assertEquals(0, savings);

    // Savings: setter vs helper function
    when(costEstimator.getCost(setterFunction1)).thenReturn(10);

    Node helperCode = rewriter.parseHelperCode(SetterReducer.class).removeFirstChild();

    rewriter.process(Node.newTextNode(""), setterFunction1Root);

    actual = setterFunction1Root;
    expected = Node.newRootNode(helperCode);
    assertEquals(expected, actual);

    savings = rewriter.estimateSavings(setterFunction1, setterFunction2);
    assertEquals(-10, savings);
  }

  @org.junit.Test
  public void testReducerPass() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node input = Node.newFunction(
        Node.newList(Node.newName("arg"), null), null,
        Node.newReturn(Node.newNumber(1))) ;
    Node root = Node.newRootNode(input);

    rewriter.process(Node.newTextNode(""), root);

    CompilerPass reducer = new FunctionRewriter(compiler);
    Node expected = rewriter.parseHelperCode(EmptyFunctionReducer.class).removeFirstChild();
    Node actual = reducer.process(Node.newTextNode(""), root).getFirstChild();

    assertEquals(expected, actual);

    assertEquals(CompilerPass.PASS, reducer.getPassName());
  }

  @org.junit.Test
  public void testReducerDoesNotApplyWhenNoSavings() {
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node input = Node.newFunction(
        Node.newList(Node.newName("arg"), null), null,
        Node.newReturn(Node.newNumber(1))) ;
    Node root = Node.newRootNode(input);

    Node helperCode = Node.newFunction(
        Node.newList(Node.newName("arg"), null), null,
        Node.newReturn(Node.newNumber(1)))
        .removeFirstChild();
    int helperCost = 10;

    when(costEstimator.getCost(helperCode)).thenReturn(helperCost);

    rewriter.process(Node.newTextNode(""), root);

    Node actual = root.getFirstChild();
    assertEquals(input, actual);

    assertEquals(0, rewriter.estimateSavings(input, root.getFirstChild()));
  }
}