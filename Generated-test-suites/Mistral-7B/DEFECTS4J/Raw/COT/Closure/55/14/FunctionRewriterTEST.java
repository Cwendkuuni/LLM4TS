import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private static final String CLASS_NAME = "FunctionRewriter";
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testProcess_EmptyProgram() {
    Node root = Node.NULL;
    assertSame(root, functionRewriter.process(Node.NULL, root));
  }

  @org.junit.Test
  public void testProcess_NoReduction() {
    String source = "function F() {return 1;}" +
        "function G() {}";
    Node externs = Node.newText("", Token.EOL, 1, 0);
    Node root = Node.parse(source);

    Node originalRoot = root.cloneTree();
    Node processedRoot = functionRewriter.process(externs, root);

    assertSame(originalRoot, processedRoot);
  }

  @org.junit.Test
  public void testProcess_SingleReduction() {
    String source = "function F() {return 1;}" +
        "function G() {getter(\"x\")}" +
        "function H() {setter(\"x\")}";
    Node externs = Node.newText("", Token.EOL, 1, 0);
    Node root = Node.parse(source);

    Node originalRoot = root.cloneTree();
    Node processedRoot = functionRewriter.process(externs, root);

    Node getterCall = NodeUtil.findNodeByType(processedRoot, Token.CALL);
    assertNotNull(getterCall);
    String getterMethodName = getterCall.getFirstChild().getString();
    assertEquals("JSCompiler_get", getterMethodName);

    Node setterCall = NodeUtil.findNodeByType(processedRoot, Token.CALL);
    assertNotNull(setterCall);
    String setterMethodName = getterCall.getFirstChild().getString();
    assertEquals("JSCompiler_set", setterMethodName);

    assertSame(originalRoot, processedRoot);
  }

  @org.junit.Test
  public void testProcess_EmptyFunctionReduction() {
    String source = "function C() {}" +
        "function C.prototype.a = function() {}" +
        "function C.prototype.b = function() {}";
    Node externs = Node.newText("", Token.EOL, 1, 0);
    Node root = Node.parse(source);

    Node originalRoot = root.cloneTree();
    Node processedRoot = functionRewriter.process(externs, root);

    Node originalCProto = NodeUtil.findNodeByType(root, Token.OBJECT_LITERAL);
    Node processedCProto = NodeUtil.findNodeByType(processedRoot, Token.OBJECT_LITERAL);

    Node originalA = NodeUtil.findNodeByType(originalCProto, Token.PROPERTY);
    Node processedA = NodeUtil.findNodeByType(processedCProto, Token.PROPERTY);
    assertNotNull(originalA);
    assertNotNull(processedA);
    assertEquals("a", originalA.getLastChild().getString());
    assertEquals("a", processedA.getLastChild().getString());

    Node originalB = NodeUtil.findNodeByType(originalCProto, Token.PROPERTY);
    Node processedB = NodeUtil.findNodeByType(processedCProto, Token.PROPERTY);
    assertNotNull(originalB);
    assertNotNull(processedB);
    assertEquals("b", originalB.getLastChild().getString());
    assertEquals("b", processedB.getLastChild().getString());

    assertSame(originalRoot, processedRoot);
  }

  @org.junit.Test
  public void testProcess_IdentityFunctionReduction() {
    String source = "function F(x) {return x}" +
        "function G(x) {return x}";
    Node externs = Node.newText("", Token.EOL, 1, 0);
    Node root = Node.parse(source);

    Node originalRoot = root.cloneTree();
    Node processedRoot = functionRewriter.process(externs, root);

    List<Node> functionFNodes = NodeUtil.findNodesByType(processedRoot, Token.FUNCTION);
    List<Node> functionGNodes = NodeUtil.findNodesByType(processedRoot, Token.FUNCTION);

    Node functionF = functionFNodes.get(0);
    Node functionG = functionGNodes.get(0);

    String functionFHelperName = functionF.getFirstChild().getString();
    String functionGHelperName = functionG.getFirstChild().getString();

    assertEquals("JSCompiler_identityFn", functionFHelperName);
    assertEquals("JSCompiler_identityFn", functionGHelperName);

    assertSame(originalRoot, processedRoot);
  }

  @org.junit.Test
  public void testProcess_ReturnConstantReduction() {
    String source = "function F() {return 10}" +
        "function G() {return 'Hello World'}";
    Node externs = Node.newText("", Token.EOL, 1, 0);
    Node root = Node.parse(source);

    Node originalRoot = root.cloneTree();
    Node processedRoot = functionRewriter.process(externs, root);

    List<Node> functionFNodes = NodeUtil.findNodesByType(processedRoot, Token.FUNCTION);
    List<Node> functionGNodes = NodeUtil.findNodesByType(processedRoot, Token.FUNCTION);

    Node functionF = functionFNodes.get(0);
    Node functionG = functionGNodes.get(0);

    String functionFHelperName = functionF.getFirstChild().getString();
    String functionGHelperName = functionG.getFirstChild().getString();

    assertEquals("JSCompiler_returnArg", functionFHelperName);
    assertEquals("JSCompiler_returnArg", functionGHelperName);

    Node functionFFirstChild = functionF.getFirstChild();
    Node functionFReturnNode = functionFFirstChild.getNext();
    assertEquals(10, functionFReturnNode.getDoubleValue());

    Node functionGFirstChild = functionG.getFirstChild();
    Node functionGReturnNode = functionGFirstChild.getNext();
    assertEquals("Hello World", functionGReturnNode.getString());

    assertSame(originalRoot, processedRoot);
  }

  @org.junit.Test
  public void testParseHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String emptyFunctionHelperSource =
        "function emptyFn() {" +
        "  return function() {}" +
        "}";
    Node emptyFunctionHelperRoot = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNotNull(emptyFunctionHelperRoot);

    String identityFunctionHelperSource =
        "function identityFn() {" +
        "  return function(identityFn_value) {" +
        "return identityFn_value}" +
        "}";
    Node identityFunctionHelperRoot = functionRewriter.parseHelperCode(IdentityReducer.class);
    assertNotNull(identityFunctionHelperRoot);

    String returnConstantHelperSource =
        "function returnArg(returnArg_value) {" +
        "  return function() {return returnArg_value}" +
        "}";
    Node returnConstantHelperRoot = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    assertNotNull(returnConstantHelperRoot);
  }

  @org.junit.Test
  public void testIsReduceableFunctionExpression() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node functionNode1 = Node.newText("function F() {}", Token.EOL, 1, 0);
    assertFalse(functionRewriter.isReduceableFunctionExpression(functionNode1));

    Node functionNode2 = Node.newText("function F(x) {}", Token.EOL, 1, 0);
    assertFalse(functionRewriter.isReduceableFunctionExpression(functionNode2));

    Node functionNode3 = Node.newText("function F() {return 1}", Token.EOL, 1, 0);
    assertTrue(functionRewriter.isReduceableFunctionExpression(functionNode3));

    Node functionNode4 = Node.newText("function F(x) {return x}", Token.EOL, 1, 0);
    assertTrue(functionRewriter.isReduceableFunctionExpression(functionNode4));

    Node functionNode5 = Node.newText("function F() {return this}", Token.EOL, 1, 0);
    assertTrue(functionRewriter.isReduceableFunctionExpression(functionNode5));

    Node functionNode6 = Node.newText("function F() {return this.a}", Token.EOL, 1, 0);
    assertTrue(functionRewriter.isReduceableFunctionExpression(functionNode6));

    Node functionNode7 = Node.newText("function F(x) {this.a = x}", Token.EOL, 1, 0);
    assertTrue(functionRewriter.isReduceableFunctionExpression(functionNode7));
  }

  @org.junit.Test
  public void testReductionGatherer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    Node root = Node.parse("function F() {return 1}");

    ReductionGatherer reductionGatherer = new ReductionGatherer(
        ImmutableList.of(new EmptyFunctionReducer(), new IdentityReducer()),
        HashMultimap.create());

    NodeTraversal.traverse(functionRewriter, root, reductionGatherer);

    Multimap<Reducer, Reduction> reductions = reductionGatherer.reductions;
    assertEquals(0, reductions.size());

    root = Node.parse("function F(x) {return x}");

    reductions = HashMultimap.create();
    NodeTraversal.traverse(functionRewriter, root, reductionGatherer);

    Reducer identityReducer = new IdentityReducer();
    Reduction identityReduction = reductions.get(identityReducer).get(0);

    Node functionNode = NodeUtil.findNodeByType(root, Token.FUNCTION);
    Node identityFunctionHelper = functionRewriter.parseHelperCode(identityReducer);

    assertEquals("JSCompiler_identityFn", identityFunctionHelper.getFirstChild().getString());
    assertEquals(1, reductions.get(identityReducer).size());

    // Check that the identity reduction was applied
    Node processedRoot = functionRewriter.process(null, root);
    Node processedIdentityFunction = NodeUtil.findNodeByType(processedRoot, Token.FUNCTION);
    Node processedIdentityFunctionHelper = processedIdentityFunction.getFirstChild();

    assertEquals("JSCompiler_identityFn", processedIdentityFunctionHelper.getString());
  }
}