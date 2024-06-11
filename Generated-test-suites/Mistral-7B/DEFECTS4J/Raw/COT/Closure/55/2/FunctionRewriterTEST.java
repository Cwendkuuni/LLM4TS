import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reducer;
import com.google.javascript.jscomp.Reduction;
import com.google.javascript.jscomp.ReductionGatherer;
import com.google.javascript.jscomp.FunctionRewriter;

import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testProcess_NoReduction_ShouldDoNothing() {
    Node root = Node.newObject(Token.NAME, "root");
    functionRewriter.process(null, root);
    assertFalse("No reduction should be applied", functionRewriter.hasReductions());
  }

  @Test
  public void testProcess_Reduction_ShouldApplyReductions() {
    Node root = Node.newObject(Token.NAME, "root");
    Node functionNode = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newName(Token.NAME, "a"),
        Node.newList(Node.newName(Token.NAME, "b"), Node.newList()),
        Node.newBlock(
            Node.newReturnStatement(
                Node.newObject(
                    Token.NAME,
                    Node.newString(Token.NAME, "returnValue")
                )
            )
        )
    );
    root.addChildToBack(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.traverse(root, NodeTraversal.EMPTY_VISITOR)).thenReturn(traversal);
    when(traversal.getParent()).thenReturn(root);

    Reducer reducer = mock(Reducer.class);
    when(reducer.getClass()).thenReturn(Reducer.class);

    ReductionGatherer reductionGatherer = new ReductionGatherer(
        ImmutableList.of(reducer), HashMultimap.create());
    NodeTraversal.traverse(compiler, root, reductionGatherer);

    List<Reducer> reducers = ImmutableList.of(reducer);
    Multimap<Reducer, Reduction> reductionMap = reductionGatherer.reductions;

    functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    assertFalse("No reduction should be applied initially", functionRewriter.hasReductions());

    Reduction reduction = reductionMap.get(reducer).get(0);
    Node expectedHelperCode = Node.newString(Token.NAME, reducer.getHelperSource());

    functionRewriter.applyReduction(reducer, reduction);

    Node actualHelperCode = functionRewriter.compiledCode.getFirstChild();
    assertNotNull("Helper code should be present", actualHelperCode);
    Node actualReducedCode = functionRewriter.compiledCode.getLastChild();
    assertNotNull("Reduced code should be present", actualReducedCode);

    Node actualHelperCodeRoot = NodeUtil.getRootOfSubTree(actualHelperCode);
    Node expectedHelperCodeRoot = NodeUtil.getRootOfSubTree(expectedHelperCode);
    assertNotNull("Helper code root should be present", actualHelperCodeRoot);
    assertNotNull("Expected helper code root should be present", expectedHelperCodeRoot);
    assertNull("Helper code should not have children", actualHelperCodeRoot.getFirstChild());
    assertNull("Helper code should not have children", expectedHelperCodeRoot.getFirstChild());
    assertNotNull("Helper code root should have a name", actualHelperCodeRoot.getName());
    assertNotNull("Expected helper code root should have a name", expectedHelperCodeRoot.getName());
    assertEquals("Helper code name should match", expectedHelperCodeRoot.getName(), actualHelperCodeRoot.getName());

    Node actualReducedCodeRoot = NodeUtil.getRootOfSubTree(actualReducedCode);
    Node expectedReducedCodeRoot = functionNode;
    assertNotNull("Reduced code root should be present", actualReducedCodeRoot);
    assertNotNull("Expected reduced code root should be present", expectedReducedCodeRoot);
    assertEquals("Reduced code root should match", expectedReducedCodeRoot, actualReducedCodeRoot);

    Node actualReturnNode = actualReducedCode.getLastChild();
    Node expectedReturnNode = functionNode.getLastChild();
    assertNotNull("Return node should be present", actualReturnNode);
    assertNotNull("Expected return node should be present", expectedReturnNode);
    assertEquals("Return node should match", expectedReturnNode, actualReturnNode);

    Node actualCallNode = NodeUtil.findSingleChildOfType(actualReturnNode, Token.CALL);
    Node expectedCallNode = functionNode.getLastChild().getLastChild();
    assertNotNull("Call node should be present", actualCallNode);
    assertNotNull("Expected call node should be present", expectedCallNode);
    assertEquals("Call node should match", expectedCallNode, actualCallNode);
  }

  @Test
  public void testIsReduceableFunctionExpression() {
    Node node1 = Node.newObject(Token.NAME, "node1");
    Node node2 = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newName(Token.NAME, "a"),
        Node.newList(),
        Node.newBlock(
            Node.newReturnStatement(
                Node.newNumber(Token.INT_LITERAL, 10)
            )
        )
    );
    assertFalse("Non-function node should not be reduceable", functionRewriter.isReduceableFunctionExpression(node1));
    assertTrue("Function node should be reduceable", functionRewriter.isReduceableFunctionExpression(node2));
  }

  @Test
  public void testParseHelperCode_EmptyFunction_ShouldReturnNull() {
    String helperSource = "";
    Node expectedHelperCode = null;
    Node actualHelperCode = functionRewriter.parseHelperCode(null);
    assertEquals("Helper code should be null", expectedHelperCode, actualHelperCode);
  }

  @Test
  public void testParseHelperCode_ValidHelperSource_ShouldReturnHelperCode() {
    String helperSource = "function helper() {}";
    String expectedHelperCodeString = helperSource;
    Node expectedHelperCode = Node.newString(Token.NAME, helperSource);
    Node actualHelperCode = functionRewriter.parseHelperCode(null);
    assertNotNull("Helper code should be present", actualHelperCode);
    Node actualHelperCodeRoot = NodeUtil.getRootOfSubTree(actualHelperCode);
    assertEquals("Helper code root should match", expectedHelperCodeRoot, actualHelperCodeRoot);
  }

  @Test
  public void testReduction_IdentityFunction_ShouldReturnIdentityFunction() {
    Node functionNode = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newName(Token.NAME, "a"),
        Node.newList(),
        Node.newBlock(
            Node.newReturnStatement(
                Node.newName(Token.NAME, "a")
            )
        )
    );
    Reducer reducer = mock(IdentityReducer.class);
    when(reducer.reduce(functionNode)).thenReturn(functionNode);

    Reduction reduction = new Reduction(
        functionRewriter.compiledCode,
        functionNode,
        functionNode
    );

    boolean expectedResult = false;
    boolean actualResult = functionRewriter.isReduceableFunctionExpression(functionNode);
    assertEquals("Function should be reduceable", expectedResult, actualResult);

    Node expectedReducedCode = Node.newObject(
        Token.NAME,
        Node.newString(Token.NAME, "JSCompiler_identityFn") + "_value"
    );
    Node actualReducedCode = functionRewriter.reduceFunction(reducer, reduction);
    assertNotNull("Reduced code should be present", actualReducedCode);
    Node actualReducedCodeRoot = NodeUtil.getRootOfSubTree(actualReducedCode);
    Node expectedReducedCodeRoot = expectedReducedCode;
    assertEquals("Reduced code root should match", expectedReducedCodeRoot, actualReducedCodeRoot);
  }

  @Test
  public void testReduction_ConstantValueFunction_ShouldReturnConstantValueFunction() {
    Node functionNode = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newList(),
        Node.newBlock(
            Node.newReturnStatement(
                Node.newNumber(Token.INT_LITERAL, 10)
            )
        )
    );
    Reducer reducer = mock(ReturnConstantReducer.class);
    when(reducer.reduce(functionNode)).thenReturn(functionNode);

    Reduction reduction = new Reduction(
        functionRewriter.compiledCode,
        functionNode,
        functionNode
    );

    boolean expectedResult = false;
    boolean actualResult = functionRewriter.isReduceableFunctionExpression(functionNode);
    assertEquals("Function should be reduceable", expectedResult, actualResult);

    int expectedSavings = 5;
    int actualSavings = functionRewriter.estimateSavings(reducer, reduction);
    assertEquals("Estimated savings should match", expectedSavings, actualSavings);

    Node expectedReducedCode = Node.newNumber(Token.INT_LITERAL, 10);
    Node actualReducedCode = functionRewriter.reduceFunction(reducer, reduction);
    assertNotNull("Reduced code should be present", actualReducedCode);
    Node actualReducedCodeRoot = NodeUtil.getRootOfSubTree(actualReducedCode);
    Node expectedReducedCodeRoot = expectedReducedCode;
    assertEquals("Reduced code root should match", expectedReducedCodeRoot, actualReducedCodeRoot);

    Node expectedCallNode = Node.newCall(
        Node.newName(Token.NAME, "returnconst"),
        Node.newNumber(Token.INT_LITERAL, 10)
    );
    Node actualCallNode = NodeUtil.findSingleChildOfType(actualReducedCode, Token.CALL);
    assertEquals("Call node should match", expectedCallNode, actualCallNode);
  }

  @Test
  public void testReduction_EmptyFunction_ShouldNotApplyReduction() {
    Node functionNode = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newList(),
        Node.newBlock(Node.newList())
    );
    Reducer reducer = mock(EmptyFunctionReducer.class);

    boolean expectedResult = false;
    boolean actualResult = functionRewriter.isReduceableFunctionExpression(functionNode);
    assertEquals("Function should not be reduceable", expectedResult, actualResult);

    assertNull("No reduction should be applied", functionRewriter.reduceFunction(reducer, null));
  }

  @Test
  public void testReduction_ReduceableFunctionWithMultipleArguments_ShouldReturnReducedFunction() {
    Node functionNode = Node.newFunction(
        Token.FUNCTION,
        null,
        Node.newList(
            Node.newName(Token.NAME, "a"),
            Node.newName(Token.NAME, "b")
        ),
        Node.newBlock(
            Node.newReturnStatement(
                Node.newName(Token.NAME, "a")
            )
        )
    );
    Reducer reducer = mock(IdentityReducer.class);

    boolean expectedResult = true;
    boolean actualResult = functionRewriter.isReduceableFunctionExpression(functionNode);
    assertEquals("Function should be reduceable", expectedResult, actualResult);

    int expectedSavings = 5;
    int actualSavings = functionRewriter.estimateSavings(reducer, null);
    assertEquals("Estimated savings should match", expectedSavings, actualSavings);

    Node expectedReducedCode = Node.newObject(
        Token.NAME,
        Node.newString(Token.NAME, "JSCompiler_identityFn") + "_value"
    );
    Node actualReducedCode = functionRewriter.reduceFunction(reducer, null);
    assertNotNull("Reduced code should be present", actualReducedCode);
    Node actualReducedCodeRoot = NodeUtil.getRootOfSubTree(actualReducedCode);
    Node expectedReducedCodeRoot = expectedReducedCode;
    assertEquals("Reduced code root should match", expectedReducedCodeRoot, actualReducedCodeRoot);

    Node expectedCallNode = Node.newCall(
        Node.newName(Token.NAME, "JSCompiler_identityFn"),
        Node.newList(
            Node.newThis(),
            Node.newName(Token.NAME, "a")
        )
    );
    Node actualCallNode = NodeUtil.findSingleChildOfType(actualReducedCode, Token.CALL);
    assertEquals("Call node should match", expectedCallNode, actualCallNode);
  }
}