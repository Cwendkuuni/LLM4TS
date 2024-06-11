package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.util.NodeUtil;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNull(root);

    Node input = rewriter.compileHelperCode(root);
    assertNull(input);

    Node function = new Node(Token.FUNCTION, 1, 1);
    Node result = rewriter.reduceFunction(function, EmptyFunctionReducer.class);
    assertEquals(EmptyFunctionReducer.FACTORY_METHOD_NAME,
                result.getString());
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.parseHelperCode(IdentityReducer.class);
    assertNull(root);

    Node input = rewriter.compileHelperCode(root);
    assertNull(input);

    Node function = new Node(Token.FUNCTION, 1, 1);
    Node arg = new Node(Token.NAME, "arg");
    function.addChildToBack(new Node(Token.FORMAL_PARAM, 1, 1, arg));
    Node body = new Node(Token.RETURN, 1, 1);
    body.addChildToBack(arg);
    function.addChildToBack(body);

    Node result = rewriter.reduceFunction(function, IdentityReducer.class);
    assertEquals(IdentityReducer.FACTORY_METHOD_NAME + "_value",
                result.getString());

    Node argNode = new Node(Token.NAME, "argValue");
    Node call = rewriter.buildCallNode(IdentityReducer.FACTORY_METHOD_NAME, argNode,
                                       function.getLineno(), function.getCharno());

    assertEquals(argNode.getString(), call.getLastChild().getString());
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.parseHelperCode(ReturnConstantReducer.class);
    assertNull(root);

    Node input = rewriter.compileHelperCode(root);
    assertNull(input);

    Node function = new Node(Token.FUNCTION, 0, 1);
    Node constant = new Node(Token.INT_LITERAL, 10, 1);
    Node body = new Node(Token.RETURN, 1, 1);
    body.addChildToBack(constant);
    function.addChildToBack(body);

    Node result = rewriter.reduceFunction(function, ReturnConstantReducer.class);
    assertEquals(ReturnConstantReducer.FACTORY_METHOD_NAME + "_value",
                result.getString());

    Node constantNode = new Node(Token.INT_LITERAL, 10, 1);
    Node call = rewriter.buildCallNode(ReturnConstantReducer.FACTORY_METHOD_NAME,
                                       constantNode,
                                       function.getLineno(), function.getCharno());

    assertEquals(Integer.toString(10), call.getLastChild().getString());
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.parseHelperCode(GetterReducer.class);
    assertNull(root);

    Node input = rewriter.compileHelperCode(root);
    assertNull(input);

    Node function = new Node(Token.FUNCTION, 1, 1);
    Node propName = new Node(Token.STRING, "propName", 1, 1);
    function.addChildToBack(new Node(Token.FORMAL_PARAM, 1, 1, propName));
    Node body = new Node(Token.RETURN, 1, 1);
    body.addChildToBack(new Node(Token.GETPROP, 1, 1, new Node(Token.THIS, 1, 1),
                                 propName));
    function.addChildToBack(body);

    Node result = rewriter.reduceFunction(function, GetterReducer.class);
    assertEquals(GetterReducer.FACTORY_METHOD_NAME,
                result.getString());

    Node call = rewriter.buildCallNode(GetterReducer.FACTORY_METHOD_NAME, propName,
                                       function.getLineno(), function.getCharno());

    assertEquals(propName.getString(), call.getLastChild().getFirstChild().getString());
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = rewriter.parseHelperCode(SetterReducer.class);
    assertNull(root);

    Node input = rewriter.compileHelperCode(root);
    assertNull(input);

    Node function = new Node(Token.FUNCTION, 2, 1);
    Node propName = new Node(Token.STRING, "propName", 1, 1);
    function.addChildToBack(new Node(Token.FORMAL_PARAM, 1, 1, propName));
    function.addChildToBack(new Node(Token.FORMAL_PARAM, 1, 1, null));
    Node body = new Node(Token.ASSIGN, 1, 1);
    Node lhs = new Node(Token.GETPROP, 1, 1, new Node(Token.THIS, 1, 1), propName);
    Node rhs = new Node(Token.NAME, "value");
    body.addChildToBack(lhs);
    body.addChildToBack(rhs);
    function.addChildToBack(body);

    Node result = rewriter.reduceFunction(function, SetterReducer.class);
    assertEquals(SetterReducer.FACTORY_METHOD_NAME, result.getString());

    Node valueNode = new Node(Token.NAME, "value");
    Node call = rewriter.buildCallNode(SetterReducer.FACTORY_METHOD_NAME, propName,
                                       function.getLineno(), function.getCharno());
    call.addChildToBack(valueNode);

    assertEquals(propName.getString(), call.getLastChild().getFirstChild().getString());
    assertEquals("value", valueNode.getString());
  }

  @Test
  public void testReducerGatherer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node input = new Node(Token.BLOCK, 1, 1);
    Node function1 = new Node(Token.FUNCTION, 1, 1);
    Node function2 = new Node(Token.FUNCTION, 1, 1);
    Node function3 = new Node(Token.FUNCTION, 1, 1);

    input.addChildToBack(function1);
    input.addChildToBack(function2);
    input.addChildToBack(function3);

    Node reducer = new ReductionGatherer(
        ImmutableList.of(new EmptyFunctionReducer(),
                          new IdentityReducer(),
                          new ReturnConstantReducer(),
                          new GetterReducer(),
                          new SetterReducer()),
        HashMultimap.create());

    NodeTraversal.traverse(rewriter, input, reducer);

    for (Node function : input.getChildren()) {
      if (function instanceof FunctionRewriter.Reduction) {
        FunctionRewriter.Reduction reduction = (FunctionRewriter.Reduction) function;
        FunctionRewriter.Reducer reducerType = reduction.reducer;

        if (reducerType instanceof EmptyFunctionReducer) {
          Node helperCode = rewriter.parseHelperCode(reducerType);
          Node parsedHelperCode = rewriter.compileHelperCode(helperCode);
          assertEquals(reducerType.getClass().getName() + ":helper",
                      parsedHelperCode.getString());
        } else if (reducerType instanceof IdentityReducer) {
          Node argNode = new Node(Token.NAME, "arg");
          Node call = rewriter.buildCallNode(reducerType.getClass().getName() + "_helper",
                                             argNode,
                                             function.getLineno(), function.getCharno());
          assertEquals(argNode.getString(), call.getLastChild().getString());
        } else if (reducerType instanceof ReturnConstantReducer) {
          Node constantNode = new Node(Token.INT_LITERAL, 10, 1);
          Node call = rewriter.buildCallNode(reducerType.getClass().getName() + "_helper",
                                             constantNode,
                                             function.getLineno(), function.getCharno());
          assertEquals(Integer.toString(10), call.getLastChild().getString());
        } else if (reducerType instanceof GetterReducer) {
          Node propNameNode = new Node(Token.STRING, "propName", 1, 1);
          Node call = rewriter.buildCallNode(reducerType.getClass().getName() + "_helper",
                                             propNameNode,
                                             function.getLineno(), function.getCharno());
          assertEquals("propName", call.getLastChild().getFirstChild().getString());
        } else if (reducerType instanceof SetterReducer) {
          Node propNameNode = new Node(Token.STRING, "propName", 1, 1);
          Node valueNode = new Node(Token.NAME, "value");
          Node call = rewriter.buildCallNode(reducerType.getClass().getName() + "_helper",
                                             propNameNode, function.getLineno(), function.getCharno());
          call.addChildToBack(valueNode);
          assertEquals("propName", call.getLastChild().getFirstChild().getString());
          assertEquals("value", valueNode.getString());
        }
      }
    }
  }
}

class Reduction extends FunctionRewriter.Reduction {
  // Empty
}

class ReductionGatherer extends FunctionRewriter.ReductionGatherer {
  // Empty
}