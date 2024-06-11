package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;
import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.FunctionRewriter.Reducer;
import org.apache.commons.cli.FunctionRewriter.Reduction;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node emptyFunction = new Node(Token.FUNCTION, 1, 1);
    emptyFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "emptyFunction"));
    emptyFunction.addChildrenToBack(new Node(Token.LEFT_BRACE, 1, 1));
    emptyFunction.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(emptyFunction);

    rewriter.process(null, root);

    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "emptyFn"));

    Node actual = root.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node identityFunction = new Node(Token.FUNCTION, 1, 1);
    identityFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "identityFunction"));
    identityFunction.addChildrenToBack(new Node(Token.LEFT_PAREN, 1, 1));
    identityFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "value"));
    identityFunction.addChildrenToBack(new Node(Token.RIGHT_PAREN, 1, 1));
    identityFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    identityFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "value"));
    identityFunction.addChildrenToBack(new Node(Token.SEMICOLON, 1, 1));
    identityFunction.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(identityFunction);

    rewriter.process(null, root);

    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "identityFn"));
    expected.addChildToBack(new Node(Token.IDENT, 1, 1, "value"));

    Node actual = root.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node returnConstantFunction = new Node(Token.FUNCTION, 1, 1);
    returnConstantFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "returnConstantFunction"));
    returnConstantFunction.addChildrenToBack(new Node(Token.LEFT_PAREN, 1, 1));
    returnConstantFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    returnConstantFunction.addChildrenToBack(new Node(Token.NUMBER, 1, 1, 42));
    returnConstantFunction.addChildrenToBack(new Node(Token.RIGHT_PAREN, 1, 1));
    returnConstantFunction.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(returnConstantFunction);

    rewriter.process(null, root);

    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "returnArg"));
    expected.addChildToBack(new Node(Token.NUMBER, 1, 1, 42));

    Node actual = root.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node getterFunction = new Node(Token.FUNCTION, 1, 1);
    getterFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "getProperty"));
    getterFunction.addChildrenToBack(new Node(Token.LEFT_PAREN, 1, 1));
    getterFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "obj"));
    getterFunction.addChildrenToBack(new Node(Token.RIGHT_PAREN, 1, 1));
    getterFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    getterFunction.addChildrenToBack(new Node(Token.GETPROP, 1, 1, "propName"));
    getterFunction.addChildrenToBack(new Node(Token.SEMICOLON, 1, 1));
    getterFunction.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(getterFunction);

    rewriter.process(null, root);

    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "getter"));
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "propName"));

    Node actual = root.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node setterFunction = new Node(Token.FUNCTION, 1, 1);
    setterFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "setProperty"));
    setterFunction.addChildrenToBack(new Node(Token.LEFT_PAREN, 1, 1));
    setterFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "obj"));
    setterFunction.addChildrenToBack(new Node(Token.COMMA, 1, 1));
    setterFunction.addChildrenToBack(new Node(Token.IDENT, 1, 1, "newValue"));
    setterFunction.addChildrenToBack(new Node(Token.RIGHT_PAREN, 1, 1));
    setterFunction.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    setterFunction.addChildrenToBack(new Node(Token.SETPROP, 1, 1, "propName", new Node(Token.IDENT, 1, 1, "newValue")));
    setterFunction.addChildrenToBack(new Node(Token.SEMICOLON, 1, 1));
    setterFunction.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(setterFunction);

    rewriter.process(null, root);

    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "setter"));
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "propName"));
    expected.addChildToBack(new Node(Token.NUMBER, 1, 1, 42)); // newValue is hard-coded as 42 in the test for simplicity

    Node actual = root.getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testReducerGatherer() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Node root = new Node(Token.BLOCK, 1, 1);
    Node function1 = new Node(Token.FUNCTION, 1, 1);
    function1.addChildrenToBack(new Node(Token.IDENT, 1, 1, "func1"));
    function1.addChildrenToBack(new Node(Token.LEFT_BRACE, 1, 1));
    function1.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    function1.addChildrenToBack(new Node(Token.NUMBER, 1, 1, 42));
    function1.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(function1);

    Node function2 = new Node(Token.FUNCTION, 1, 1);
    function2.addChildrenToBack(new Node(Token.IDENT, 1, 1, "func2"));
    function2.addChildrenToBack(new Node(Token.LEFT_BRACE, 1, 1));
    function2.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    function2.addChildrenToBack(new Node(Token.NUMBER, 1, 1, 43));
    function2.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));
    root.addChildToBack(function2);

    FunctionRewriter.ReductionGatherer reducerGatherer = new FunctionRewriter.ReductionGatherer(
        ImmutableList.of(new FunctionRewriter.ReturnConstantReducer(),
                          new FunctionRewriter.GetterReducer(),
                          new FunctionRewriter.SetterReducer()),
        HashMultimap.create());

    NodeTraversal.traverse(rewriter, root, reducerGatherer);
    List<Reduction> reductions = reducerGatherer.reductions.get("SetterReducer");

    assertNull(reductions.isEmpty());
    Reduction reduction = reductions.get(0);
    Node expected = new Node(Token.CALL, 1, 1);
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "setter"));
    expected.addChildToBack(new Node(Token.STRING, 1, 1, "propName"));
    expected.addChildToBack(new Node(Token.NUMBER, 1, 1, 43));

    Node actual = reduction.newChild;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testParseHelperCode() {
    FunctionRewriter rewriter = new FunctionRewriter(null);
    Reducer getterReducer = new FunctionRewriter.GetterReducer();
    Node helperCode = rewriter.parseHelperCode(getterReducer);

    assertNull(helperCode == null);

    String getterHelperSource = getterReducer.getClass().toString() + ":helper=" +
        "function getter(name) {" +
        "  return function() {" +
        "    return this[" + getterReducer.getClass().toString() + "_name]" +
        "  }" +
        "}";

    Node expected = new Node(Token.FUNCTION, 1, 1);
    expected.addChildrenToBack(new Node(Token.IDENT, 1, 1, "getter"));
    expected.addChildrenToBack(new Node(Token.LEFT_PAREN, 1, 1));
    expected.addChildrenToBack(new Node(Token.IDENT, 1, 1, "name"));
    expected.addChildrenToBack(new Node(Token.RIGHT_PAREN, 1, 1));
    expected.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    expected.addChildrenToBack(new Node(Token.FUNCTION, 1, 1));
    expected.addChildrenToBack(expected.getLastChild());
    expected.addChildrenToBack(new Node(Token.LEFT_BRACE, 1, 1));
    expected.addChildrenToBack(new Node(Token.RETURN, 1, 1));
    expected.addChildrenToBack(new Node(Token.GETPROP, 1, 1, getterReducer.getClass().toString() + "_name"));
    expected.addChildrenToBack(new Node(Token.SEMICOLON, 1, 1));
    expected.addChildrenToBack(new Node(Token.RIGHT_BRACE, 1, 1));

    Node actual = helperCode;
    assertEquals(expected.toString(), actual.toString());
  }
}