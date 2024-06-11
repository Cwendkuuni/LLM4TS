import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeUtil.*;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.FunctionRewriter.Reducer;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reduction;
import com.google.javascript.jscomp.ReductionGatherer;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * FunctionRewriterTest
 */
public class FunctionRewriterTest {

  @SuppressWarnings("unchecked")
  private static final Class<?>[] REDUCERS = {
      FunctionRewriter.ReturnConstantReducer.class,
      FunctionRewriter.IdentityReducer.class,
      FunctionRewriter.GetterReducer.class,
      FunctionRewriter.SetterReducer.class,
      FunctionRewriter.EmptyFunctionReducer.class
  };

  @SuppressWarnings("unchecked")
  private static final Class<? extends Reducer>[] GETTER_REDUCERS = {
      FunctionRewriter.GetterReducer.class
  };

  @SuppressWarnings("unchecked")
  private static final Class<? extends Reducer>[] SETTER_REDUCERS = {
      FunctionRewriter.SetterReducer.class
  };

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testProcess_EmptyProgram() {
    Node root = Node.EMPTY_NODE;
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    assertEquals(0, compiler.getCodeSize());
  }

  @Test
  public void testProcess_NoReducibleFunctions() {
    String sourceCode = "function foo() {} function bar() {}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    assertEquals(sourceCode.length(), compiler.getCodeSize());
  }

  @Test
  public void testProcess_EmptyFunctionReducer() {
    String sourceCode = "function C() {} C.prototype.getA = function() {} C.prototype.setA = function() {}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    String expectedCode = "function C() {} C.prototype.getA = emptyFn(); C.prototype.setA = emptyFn();";
    String actualCode = compiler.getSource();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testProcess_IdentityFunctionReducer() {
    String sourceCode = "function C() {} C.prototype.b = function(a) {return a}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    String expectedCode = "function C() {} C.prototype.b = identityFn(IdentityFunction.IdentityFunction_value);";
    String actualCode = compiler.getSource();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testProcess_ReturnConstantReducer() {
    String sourceCode = "function C() {} C.prototype.b = function() {return 10}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    String expectedCode = "function C() {} C.prototype.b = returnconst(10);";
    String actualCode = compiler.getSource();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testProcess_GetterReducer() {
    String sourceCode = "function C() {} C.prototype.b = function() {return this.b_}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    String expectedCode = "function C() {} C.prototype.b = getter(\"b_\");";
    String actualCode = compiler.getSource();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testProcess_SetterReducer() {
    String sourceCode = "function C() {} C.prototype.setB = function(value) {this.b_ = value}"
        + " C.prototype.getB = function() {return this.b_}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(Node.EMPTY_NODE, root);
    String expectedCode = "function C() {} C.prototype.setB = getter(\"b_\"); C.prototype.getB = getter(\"b_\");";
    String actualCode = compiler.getSource();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testReductionGatherer() {
    Node root = Node.EMPTY_NODE;
    List<Reducer> reducers = ImmutableList.copyOf(REDUCERS);
    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
    FunctionRewriter.ReductionGatherer reductionGatherer =
        new FunctionRewriter.ReductionGatherer(reducers, reductionMap);
    NodeTraversal.traverse(compiler, root, reductionGatherer);
    Collection<Reduction> reductions = reductionMap.values();
    assertFalse(reductions.isEmpty());
    for (Reducer reducer : reducers) {
      assertTrue(reductions.stream().anyMatch(r -> r.reducer == reducer));
    }
  }

  @Test
  public void testParseHelperCodeEmptyFunctionReducer() {
    Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
    Node helperCode = functionRewriter.parseHelperCode(reducer);
    assertNotNull(helperCode);
    assertEquals(1, helperCode.getChildCount());
    assertEquals(0, helperCode.getChild(0).getChildCount());
  }

  @Test
  public void testParseHelperCodeIdentityFunctionReducer() {
    Reducer reducer = new FunctionRewriter.IdentityReducer();
    String helperSource = reducer.getHelperSource();
    Node helperCode = functionRewriter.parseSyntheticCode(
        "FunctionRewriterTest:helper", helperSource);
    assertNotNull(helperCode);
    assertEquals(3, helperCode.getChildCount());
    assertEquals("JSCompiler_identityFn", helperCode.getFirstChild().getString());
    assertEquals("function", helperCode.getLastChild().getFirstChild().getString());
  }

  @Test
  public void testReducer_EmptyFunctionReducer() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Reducer reducer = new FunctionRewriter.EmptyFunctionReducer();
    Node reducedNode = reducer.reduce(functionNode);
    assertNotNull(reducedNode);
    assertEquals(1, reducedNode.getChildCount());
    assertEquals("emptyFn", reducedNode.getString());
  }

  @Test
  public void testReducer_IdentityFunctionReducer() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Node argumentNode = Node.newName("a");
    Reducer reducer = new FunctionRewriter.IdentityReducer();
    Node reducedNode = reducer.reduce(functionNode);
    assertNotNull(reducedNode);
    assertEquals(0, reducedNode.getChildCount());
    Reducer identityReducer = new FunctionRewriter.IdentityReducer();
    Node identityHelperCode = functionRewriter.parseHelperCode(identityReducer);
    Node argumentNodeClone = argumentNode.cloneTree();
    Node callNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    callNode.addChildToBack(new Node(Token.NAME, "JSCompiler_identityFn"));
    callNode.addChildToBack(argumentNodeClone);
    Node expectedReducedNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    expectedReducedNode.addChildToBack(callNode);
    assertEquals(expectedReducedNode, reducedNode);
  }

  @Test
  public void testReducer_IdentityFunctionReducer_IdentityFunction() {
    String helperSource = "function IdentityFunction() {" +
        "  return function(IdentityFunction_value) {" +
        "    return IdentityFunction_value" +
        "  }" +
        "}";
    Node helperCode = functionRewriter.parseSyntheticCode(
        "FunctionRewriterTest:helper", helperSource);
    Node identityFunctionNode = helperCode.getLastChild();
    Node argumentNode = Node.newName("IdentityFunction_value");
    Reducer reducer = new FunctionRewriter.IdentityReducer();
    Node reducedNode = reducer.reduce(identityFunctionNode);
    assertNotNull(reducedNode);
    assertEquals(1, reducedNode.getChildCount());
    assertEquals(argumentNode, reducedNode.getFirstChild());
  }

  @Test(expected = IllegalStateException.class)
  public void testReducer_IdentityFunctionReducer_NonIdentityFunction() {
    String sourceCode = "function C() {} C.prototype.b = function(a) {return a + 1}";
    Node root = compiler.parse(sourceCode, null);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node bFunctionNode = findFunctionNode(root, "C.prototype.b");
    Reducer reducer = new FunctionRewriter.IdentityReducer();
    Node reducedNode = reducer.reduce(bFunctionNode);
  }

  @Test
  public void testReducer_ReturnConstantReducer() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Node constantNode = Node.newNumber(10);
    Reducer reducer = new FunctionRewriter.ReturnConstantReducer();
    Node reducedNode = reducer.reduce(functionNode);
    assertNotNull(reducedNode);
    assertEquals(1, reducedNode.getChildCount());
    assertEquals("returnconst", reducedNode.getString());
    assertEquals(1, reducedNode.getChild(0).getChildCount());
    assertEquals(constantNode, reducedNode.getChild(0).getFirstChild());
  }

  @Test
  public void testReducer_ReturnConstantReducer_ReturnConstant() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Node constantNode = Node.newNumber(10);
    Reducer reducer = new FunctionRewriter.ReturnConstantReducer();
    Node argumentNode = Node.newName("C_returnArg");
    Node helperCode = functionRewriter.parseHelperCode(reducer);
    Node helperFunctionNode = helperCode.getLastChild();
    Node callNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    callNode.addChildToBack(new Node(Token.NAME, "JSCompiler_returnArg"));
    callNode.addChildToBack(argumentNode);
    Node expectedReducedNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    expectedReducedNode.addChildToBack(callNode);
    assertEquals(expectedReducedNode, reducer.reduce(functionNode));
  }

  @Test
  public void testReducer_GetterReducer() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Node thisNode = Node.newThis();
    Node propertyNameNode = Node.newString("b_");
    Node getPropertyNode = Node.newGetProp(thisNode, propertyNameNode);
    Node expectedReducedNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    expectedReducedNode.addChildToBack(Node.newString("getter"));
    expectedReducedNode.addChildToBack(propertyNameNode);
    Reducer reducer = new FunctionRewriter.GetterReducer();
    Node reducedNode = reducer.reduce(functionNode);
    assertNotNull(reducedNode);
    assertEquals(expectedReducedNode, reducedNode);
  }

  @Test
  public void testReducer_SetterReducer() {
    Node functionNode = Node.newFunction("C", ImmutableList.of(), null);
    Node thisNode = Node.newThis();
    Node propertyNameNode = Node.newString("b_");
    Node argumentNode = Node.newName("value");
    Node setPropertyNode = Node.newSetProp(thisNode, propertyNameNode, argumentNode);
    Node expectedReducedNode = new Node(Token.CALL, functionNode.getLineno(), functionNode.getCharno());
    expectedReducedNode.addChildToBack(Node.newString("getter"));
    expectedReducedNode.addChildToBack(propertyNameNode);
    Reducer reducer = new FunctionRewriter.SetterReducer();
    Node reducedNode = reducer.reduce(functionNode);
    assertNotNull(reducedNode);
    assertEquals(expectedReducedNode, reducedNode);
  }

  private Node findFunctionNode(Node root, String name) {
    for (Node child : root.children()) {
      if (NodeUtil.isFunctionDeclaration(child)) {
        Node functionName = child.getLastChild();
        if (NodeUtil.isName(functionName) && functionName.getString().equals(name)) {
          return child;
        }
      } else if (child.getChildCount() > 0) {
        Node childResult = findFunctionNode(child, name);
        if (childResult != null) {
          return childResult;
        }
      }
    }
    return null;
  }

  private static AbstractCompiler compiler = new Compiler();
  private static FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
}