import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;
import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildToBack(new Node(Token.BLOCK, 1, 1));
    Node emptyFunction = new Node(Token.FUNCTION, 2, 2);
    emptyFunction.putBooleanProp(Node.IS_STRICT, true);
    emptyFunction.addChildToBack(new Node(Token.VAR, 3, 3, "a"));
    emptyFunction.addChildToBack(new Node(Token.FUNCTION, 4, 4, "b", null, null));
    emptyFunction.addChildToBack(new Node(Token.BLOCK, 5, 5));
    emptyFunction.addChildToBack(new Node(Token.RETURN, 6, 6));

    root.getLastChild().addChildToBack(emptyFunction);
    Node originalRoot = root.cloneTree();

    functionRewriter.process(null, root);

    Node rewrittenRoot = root.cloneTree();
    Node expectedHelper = new Node(Token.FUNCTION, 7, 7, "JSCompiler_emptyFn", null, null);
    Node emptyFunctionHelperCall =
        new Node(Token.CALL, 8, 8, expectedHelper.cloneTree());

    assertEquals(rewrittenRoot.getChildCount(), originalRoot.getChildCount() - 3);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getType(), Token.CALL);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getType(), Token.NAME);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getString(), "JSCompiler_emptyFn");

    Node helperCode = rewrittenRoot.getChild(rewrittenRoot.getChildCount() - 1);
    assertEquals(helperCode.getChildCount(), 1);
    assertEquals(helperCode.getChild(0).getType(), Token.FUNCTION);
  }

  @Test
  public void testIdentityReducer() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildToBack(new Node(Token.BLOCK, 1, 1));
    Node identityFunction = new Node(Token.FUNCTION, 2, 2, "identity", null, null);
    identityFunction.putBooleanProp(Node.IS_STRICT, true);
    identityFunction.addChildToBack(new Node(Token.VAR, 3, 3, "a"));
    identityFunction.addChildToBack(new Node(Token.FUNCTION, 4, 4, "b", null, null));
    identityFunction.addChildToBack(new Node(Token.BLOCK, 5, 5));
    identityFunction.addChildToBack(new Node(Token.VAR, 6, 6, "value"));
    identityFunction.addChildToBack(new Node(Token.RETURN, 7, 7, new Node(Token.NAME, 8, 8, "value")));

    root.getLastChild().addChildToBack(identityFunction);
    Node originalRoot = root.cloneTree();

    functionRewriter.process(null, root);

    Node rewrittenRoot = root.cloneTree();
    Node expectedHelper = new Node(Token.FUNCTION, 9, 9, "JSCompiler_identityFn", null, null);
    Node identityFunctionHelperCall = buildCallNode("JSCompiler_identityFn", null, 9, 9);

    assertEquals(rewrittenRoot.getChildCount(), originalRoot.getChildCount() - 1);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getType(), Token.CALL);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getType(), Token.NAME);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getString(), "JSCompiler_identityFn");

    Node helperCode = rewrittenRoot.getChild(rewrittenRoot.getChildCount() - 1);
    assertEquals(helperCode.getChildCount(), 1);
    assertEquals(helperCode.getChild(0).getType(), Token.FUNCTION);
  }

  @Test
  public void testReturnConstantReducer() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildToBack(new Node(Token.BLOCK, 1, 1));
    Node returnConstantFunction = new Node(Token.FUNCTION, 2, 2, "returnConstant", null, null);
    returnConstantFunction.putBooleanProp(Node.IS_STRICT, true);
    returnConstantFunction.addChildToBack(new Node(Token.VAR, 3, 3, "a"));
    returnConstantFunction.addChildToBack(new Node(Token.RETURN, 4, 4, new Node(Token.INT_LITERAL, 5, 5, 10)));

    root.getLastChild().addChildToBack(returnConstantFunction);
    Node originalRoot = root.cloneTree();

    functionRewriter.process(null, root);

    Node rewrittenRoot = root.cloneTree();
    Node expectedHelper = new Node(Token.FUNCTION, 6, 6, "JSCompiler_returnArg", null, null);
    Node returnConstantFunctionHelperCall = buildCallNode("JSCompiler_returnArg", new Node(Token.INT_LITERAL, 7, 7, 10), 6, 6);

    assertEquals(rewrittenRoot.getChildCount(), originalRoot.getChildCount() - 1);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getType(), Token.CALL);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getType(), Token.NAME);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getString(), "JSCompiler_returnArg");

    Node helperCode = rewrittenRoot.getChild(rewrittenRoot.getChildCount() - 1);
    assertEquals(helperCode.getChildCount(), 1);
    assertEquals(helperCode.getChild(0).getType(), Token.FUNCTION);
  }

  @Test
  public void testGetterReducer() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildToBack(new Node(Token.BLOCK, 1, 1));
    Node getterFunction = new Node(Token.FUNCTION, 2, 2, "getter", null, null);
    getterFunction.putBooleanProp(Node.IS_STRICT, true);
    getterFunction.addChildToBack(new Node(Token.VAR, 3, 3, "a"));
    getterFunction.addChildToBack(new Node(Token.RETURN, 4, 4, new Node(Token.GET_PROPERTY, 5, 5, new Node(Token.THIS, 6, 6))));

    root.getLastChild().addChildToBack(getterFunction);
    Node originalRoot = root.cloneTree();

    functionRewriter.process(null, root);

    Node rewrittenRoot = root.cloneTree();
    Node expectedHelper = new Node(Token.FUNCTION, 7, 7, "JSCompiler_get", null, null);
    Node getterFunctionHelperCall = buildCallNode("JSCompiler_get", new Node(Token.NAME, 8, 8, "a"), 7, 7);

    assertEquals(rewrittenRoot.getChildCount(), originalRoot.getChildCount() - 1);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getType(), Token.CALL);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getType(), Token.NAME);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getString(), "JSCompiler_get");

    Node helperCode = rewrittenRoot.getChild(rewrittenRoot.getChildCount() - 1);
    assertEquals(helperCode.getChildCount(), 1);
    assertEquals(helperCode.getChild(0).getType(), Token.FUNCTION);
  }

  @Test
  public void testSetterReducer() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildToBack(new Node(Token.BLOCK, 1, 1));
    Node setterFunction = new Node(Token.FUNCTION, 2, 2, "setter", null, null);
    setterFunction.putBooleanProp(Node.IS_STRICT, true);
    setterFunction.addChildToBack(new Node(Token.VAR, 3, 3, "a"));
    setterFunction.addChildToBack(new Node(Token.VAR, 4, 4, "value"));
    setterFunction.addChildToBack(new Node(Token.SET_PROPERTY, 5, 5, new Node(Token.THIS, 6, 6), new Node(Token.NAME, 7, 7, "a")));

    root.getLastChild().addChildToBack(setterFunction);
    Node originalRoot = root.cloneTree();

    functionRewriter.process(null, root);

    Node rewrittenRoot = root.cloneTree();
    Node expectedHelper = new Node(Token.FUNCTION, 8, 8, "JSCompiler_set", null, null);
    Node setterFunctionHelperCall = buildCallNode("JSCompiler_set", new Node(Token.NAME, 9, 9, "a"), new Node(Token.INT_LITERAL, 10, 10, 10), 8, 8);

    assertEquals(rewrittenRoot.getChildCount(), originalRoot.getChildCount() - 1);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getType(), Token.CALL);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getType(), Token.NAME);
    assertEquals(rewrittenRoot.getChild(4).getChild(0).getFirstChild().getString(), "JSCompiler_set");

    Node helperCode = rewrittenRoot.getChild(rewrittenRoot.getChildCount() - 1);
    assertEquals(helperCode.getChildCount(), 3);
    assertEquals(helperCode.getChild(0).getType(), Token.FUNCTION);
  }
}