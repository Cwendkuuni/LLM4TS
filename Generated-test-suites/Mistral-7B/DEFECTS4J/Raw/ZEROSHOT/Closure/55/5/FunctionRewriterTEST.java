import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Map;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parse(String code, int lineno, int charno) {
        return null;
      }

      @Override
      public Node parseSyntheticCode(String name, String code) {
        return Node.newText(Token.TEXT, code);;
      }
    };
  }

  @org.junit.Test
  public void emptyFunctionTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    functionRewriter.process(null, root);

    Node expectedHelperCode = Node.newText(Token.TEXT,
            "function emptyFn() {\n" +
            "  return function() {}" +
            "}");

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    FunctionRewriter.Reduction reduction = reductions.values().iterator().next();

    Node actualHelperCode = reduction.newChild;

    assertEquals("Expected helper code to be equal",
            expectedHelperCode.toString(),
            actualHelperCode.toString());

    Node expectedEmptyFunction = Node.newCall(Token.CALL, null,
            NodeUtil.newNameNode("emptyFn"), null);

    Node actualEmptyFunction = root.getFirstChild();

    assertEquals("Expected empty function to be equal",
            expectedEmptyFunction.toString(),
            actualEmptyFunction.toString());
  }

  @org.junit.Test
  public void identityFunctionTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                Node.newThis(),
                Node.newName(Token.NAME, "a"),
                Node.newName(Token.NAME, "b"))));
    root.addChildToBack(Node.newReturnStatement(
            Node.newName(Token.NAME, "a")));
    functionRewriter.process(null, root);

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    FunctionRewriter.Reduction reduction = reductions.values().iterator().next();

    Node expectedHelperCode = Node.newText(Token.TEXT,
            "function identityFn(identityFn_value) {\n" +
            "  return function(" +
            "      identityFn__value)" +
            "  {" +
            "    return identityFn__value" +
            "  }\n" +
            "}");

    Node actualHelperCode = reduction.newChild;

    assertEquals("Expected helper code to be equal",
            expectedHelperCode.toString(),
            actualHelperCode.toString());

    Node expectedIdentityFunction = Node.newCall(Token.CALL, null,
            NodeUtil.newNameNode("identityFn"),
            NodeUtil.newThis());

    Node actualIdentityFunction = root.getFirstChild();

    assertEquals("Expected identity function to be equal",
            expectedIdentityFunction.toString(),
            actualIdentityFunction.toString());
  }

  @org.junit.Test
  public void returnConstantTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    root.addChildToBack(Node.newReturnStatement(
            Node.newNumberLiteral(10)));
    functionRewriter.process(null, root);

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    FunctionRewriter.Reduction reduction = reductions.values().iterator().next();

    Node expectedHelperCode = Node.newText(Token.TEXT,
            "function returnArg(returnArg_value) {\n" +
            "  return function() {\n" +
            "    return returnArg_value" +
            "  }\n" +
            "}");

    Node actualHelperCode = reduction.newChild;

    assertEquals("Expected helper code to be equal",
            expectedHelperCode.toString(),
            actualHelperCode.toString());

    Node expectedReturnConstantFunction = Node.newCall(Token.CALL, null,
            NodeUtil.newNameNode("returnArg"), null);

    Node actualReturnConstantFunction = root.getFirstChild();

    assertEquals("Expected return constant function to be equal",
            expectedReturnConstantFunction.toString(),
            actualReturnConstantFunction.toString());
  }

  @org.junit.Test
  public void nonReducibleFunctionTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                Node.newName(Token.NAME, "a"),
                Node.newNumberLiteral(10))));
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                Node.newName(Token.NAME, "b"),
                Node.newName(Token.NAME, "a"))));
    root.addChildToBack(Node.newReturnStatement(
            Node.newName(Token.NAME, "b")));
    functionRewriter.process(null, root);

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    assertNull("Expected no reductions", reductions);
  }

  @org.junit.Test
  public void reducibleFunctionWithGetPropTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                Node.newName(Token.NAME, "a"),
                Node.newObjectLiteral())));
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                NodeUtil.newThis(),
                NodeUtil.newNameNode("b"),
                Node.newName(Token.NAME, "a"),
                Node.newName(Token.NAME, "c"))));
    root.addChildToBack(Node.newReturnStatement(
            Node.newGetProp(Token.GETPROP,
                NodeUtil.newThis(),
                Node.newName(Token.NAME, "b"))));
    functionRewriter.process(null, root);

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    FunctionRewriter.Reducer getterReducer =
        FunctionRewriter.Reducer.class.cast(
                reductions.keySet().iterator().next());
    FunctionRewriter.Reduction reduction = reductions.values().iterator().next();

    Node expectedHelperCode = Node.newText(Token.TEXT,
            "function getter(getter_name) {\n" +
            "  return function() {\n" +
            "    return this[" + getter_name + "]" +
            "  }\n" +
            "}");

    Node actualHelperCode = reduction.newChild;

    assertEquals("Expected helper code to be equal",
            expectedHelperCode.toString(),
            actualHelperCode.toString());

    Node expectedGetPropertyFunction = Node.newCall(Token.CALL, null,
            NodeUtil.newNameNode("getter"),
            NodeUtil.newNameNode("b"));

    Node actualGetPropertyFunction = root.getFirstChild();

    assertEquals("Expected get property function to be equal",
            expectedGetPropertyFunction.toString(),
            actualGetPropertyFunction.toString());
  }

  @org.junit.Test
  public void reducibleFunctionWithSetPropTest() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = Node.newFunction(Token.FUNCTION, null, null, null, null);
    root.addChildToBack(Node.newExpressionStatement(
            Node.newAssignment(Token.ASSIGN,
                NodeUtil.newThis(),
                NodeUtil.newNameNode("b"),
                Node.newUndefined(),
                Node.newName(Token.NAME, "a"))));
    root.addChildToBack(Node.newReturnStatement(Token.RETURN));
    functionRewriter.process(null, root);

    Map<FunctionRewriter.Reducer, FunctionRewriter.Reduction> reductions =
        functionRewriter.getReductions();
    FunctionRewriter.Reducer setterReducer =
        FunctionRewriter.Reducer.class.cast(
                reductions.keySet().iterator().next());
    FunctionRewriter.Reduction reduction = reductions.values().iterator().next();

    Node expectedHelperCode = Node.newText(Token.TEXT,
            "function setter(setter_name) {\n" +
            "  return function(setter_value) {\n" +
            "this[" + setter_name + "] = setter_value" +
            "  }\n" +
            "}");

    Node actualHelperCode = reduction.newChild;

    assertEquals("Expected helper code to be equal",
            expectedHelperCode.toString(),
            actualHelperCode.toString());

    Node expectedSetPropertyFunction = Node.newCall(Token.CALL, null,
            NodeUtil.newNameNode("setter"),
            NodeUtil.newThis(),
            Node.newName(Token.NAME, "a"));

    Node actualSetPropertyFunction = root.getFirstChild();

    assertEquals("Expected set property function to be equal",
            expectedSetPropertyFunction.toString(),
            actualSetPropertyFunction.toString());
  }
}