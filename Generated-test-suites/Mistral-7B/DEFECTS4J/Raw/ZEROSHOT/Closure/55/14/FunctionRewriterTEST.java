import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private FunctionRewriter functionRewriter;
  private Node root;

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSyntheticCode(String name, String code) {
        return null;
      }
    };
    functionRewriter = new FunctionRewriter(compiler);
    root = new Node(Token.BLOCK, 1, 1);
  }

  @org.junit.Test
  public void testIdentityReducer_SingleReturnStatement() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.addChildToBack(new Node(Token.VAR, 1, 1, "a", null));
    functionNode.addChildToBack(new Node(Token.RETURN, 1, 1, null, new Node(Token.NAME, 1, 1, "a")));

    Node reducedNode = functionRewriter.new IdentityReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_identityFn"), null);
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testIdentityReducer_EmptyFunction() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    Node reducedNode = functionRewriter.new IdentityReducer().reduce(functionNode);
    assertNull(reducedNode);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    Node reducedNode = functionRewriter.new EmptyFunctionReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_emptyFn"), null);
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.addChildToBack(new Node(Token.RETURN, 1, 1, null, new Node(Token.INT_LITERAL, 1, 1, 10)));

    Node reducedNode = functionRewriter.new ReturnConstantReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"), new Node(Token.INT_LITERAL, 1, 1, 10));
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testReturnConstantReducer_ComplexFunction() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    functionNode.addChildToBack(new Node(Token.VAR, 1, 1, "a", null));
    functionNode.addChildToBack(new Node(Token.RETURN, 1, 1, null, new Node(Token.NAME, 1, 1, "a")));
    functionNode.addChildToBack(new Node(Token.VAR, 1, 1, "b", new Node(Token.INT_LITERAL, 1, 1, 20)));

    Node reducedNode = functionRewriter.new ReturnConstantReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.BLOCK, 1, 1, null, null);
    expectedNode.addChildToBack(new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"), new Node(Token.INT_LITERAL, 1, 1, 10)));
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    Node thisNode = new Node(Token.THIS, 1, 1);
    Node getPropertyNode = new Node(Token.GETPROP, 1, 1, thisNode, new Node(Token.NAME, 1, 1, "a"));
    Node returnNode = new Node(Token.RETURN, 1, 1, null, getPropertyNode);

    Node reducedNode = functionRewriter.new GetterReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_get"), new Node(Token.NAME, 1, 1, "a"));
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node functionNode = new Node(Token.FUNCTION, 1, 1);
    Node thisNode = new Node(Token.THIS, 1, 1);
    Node setPropertyNode = new Node(Token.SETPROP, 1, 1, thisNode, new Node(Token.NAME, 1, 1, "a"), new Node(Token.INT_LITERAL, 1, 1, 10));

    Node reducedNode = functionRewriter.new SetterReducer().reduce(functionNode);
    Node expectedNode = new Node(Token.BLOCK, 1, 1, null, null);
    expectedNode.addChildToBack(new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_set"), new Node(Token.NAME, 1, 1, "a")));
    expectedNode.addChildToBack(new Node(Token.INT_LITERAL, 1, 1, 10));
    assertSame(expectedNode, reducedNode);
  }

  @org.junit.Test
  public void testReductionGatherer() {
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    Node emptyFunctionNode = new Node(Token.FUNCTION, 1, 1);
    Node identityFunctionNode = new Node(Token.FUNCTION, 1, 1);
    Node identityReturnNode = new Node(Token.RETURN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"));
    Node getPropertyNode = new Node(Token.GETPROP, 1, 1, new Node(Token.THIS, 1, 1), new Node(Token.NAME, 1, 1, "a"));
    Node setPropertyNode = new Node(Token.SETPROP, 1, 1, new Node(Token.THIS, 1, 1), new Node(Token.NAME, 1, 1, "a"), new Node(Token.INT_LITERAL, 1, 1, 10));
    Node returnConstantNode = new Node(Token.RETURN, 1, 1, null, new Node(Token.INT_LITERAL, 1, 1, 10));

    functionRewriter.process(null, root);

    Node functionRewrittenNode = root.getLastChild();
    List<Node> children = functionRewrittenNode.getChildren();

    Node emptyFunctionReducerNode = children.get(0);
    assertSame(emptyFunctionNode, emptyFunctionReducerNode);

    Node identityFunctionReducerNode = children.get(1);
    Node identityFunctionReducedNode = identityFunctionReducerNode.getLastChild();
    assertSame(identityFunctionNode, identityFunctionReducedNode);
    Node identityFunctionHelperNode = identityFunctionReducerNode.getLastChild().getLastChild();
    assertSame(identityReturnNode, identityFunctionHelperNode);

    Node getterFunctionReducerNode = children.get(2);
    Node getterFunctionReducedNode = getterFunctionReducerNode.getLastChild();
    assertSame(getPropertyNode, getterFunctionReducedNode.getLastChild());
    Node getterFunctionHelperNode = getterFunctionReducerNode.getLastChild().getLastChild();
    assertSame(identityReturnNode, getterFunctionHelperNode);

    Node setterFunctionReducerNode = children.get(3);
    Node setterFunctionReducedNode = setterFunctionReducerNode.getLastChild();
    assertSame(setPropertyNode, setterFunctionReducedNode.getLastChild());
    Node setterFunctionHelperNode = setterFunctionReducerNode.getLastChild().getLastChild();
    assertSame(identityReturnNode, setterFunctionHelperNode.getLastChild());
    Node setterFunctionHelperNameNode = setterFunctionHelperNode.getLastChild().getLastChild();
    assertSame(new Node(Token.NAME, 1, 1, "JSCompiler_set"), setterFunctionHelperNameNode);

    Node returnConstantFunctionReducerNode = children.get(4);
    Node returnConstantFunctionReducedNode = returnConstantFunctionReducerNode.getLastChild();
    assertSame(returnConstantNode, returnConstantFunctionReducedNode);
    Node returnConstantFunctionHelperNode = returnConstantFunctionReducerNode.getLastChild().getLastChild();
    assertSame(new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"), returnConstantFunctionHelperNode);
  }

  @org.junit.Test
  public void testParseHelperCode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {
      @Override
      public Node parseSyntheticCode(String name, String code) {
        return new Node(Token.BLOCK, 1, 1, null, null);
      }
    });

    Node expectedNode = new Node(Token.BLOCK, 1, 1, null, null);
    assertSame(expectedNode, functionRewriter.parseHelperCode(new EmptyFunctionReducer()));

    String helperSource =
        "function " + EmptyFunctionReducer.FACTORY_METHOD_NAME + "() {" +
        "  return function() {}" +
        "}";
    expectedNode = new Node(Token.BLOCK, 1, 1, null, null);
    expectedNode.addChildToBack(new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_emptyFn"), null));
    assertSame(expectedNode, functionRewriter.parseHelperCode(new EmptyFunctionReducer()));

    expectedNode = new Node(Token.BLOCK, 1, 1, null, null);
    expectedNode.addChildToBack(new Node(Token.CALL, 1, 1, new Node(Token.NAME, 1, 1, "JSCompiler_identityFn"), null));
    assertSame(expectedNode, functionRewriter.parseHelperCode(new IdentityReducer()));

    helperSource =
        "function " + IdentityReducer.FACTORY_METHOD_NAME + "() {" +
        "  return function(" + IdentityReducer.FACTORY_METHOD_NAME + "_value) {" +
        "return " + IdentityReducer.FACTORY_METHOD_NAME + "_value}" +
        "}";
    assertSame(expectedNode, functionRewriter.parseHelperCode(new IdentityReducer()));
  }
}