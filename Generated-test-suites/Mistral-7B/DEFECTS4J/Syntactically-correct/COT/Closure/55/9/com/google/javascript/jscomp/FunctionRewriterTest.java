import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.NodeUtil.isFunctionExpression;
import static com.google.javascript.jscomp.NodeUtil.isImmutableValue;
import static com.google.javascript.jscomp.NodeUtil.isGetProp;
import static com.google.javascript.jscomp.NodeUtil.isThis;
import static com.google.javascript.jscomp.NodeUtil.isExprAssign;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseSyntheticCode(String name, String code) {
        return null;
      }
    };
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertNotNull(functionNode.getFirstChild());
    assertNotNull(functionNode.getFirstChild().getNext());
    Node expected = new Node(Token.CALL, 1, 1, null, Arrays.asList(
        new Node(Token.NAME, 1, 1, "JSCompiler_emptyFn")
    ));
    Node actual = functionNode.getFirstChild();
    Node expectedNext = functionNode.getFirstChild().getNext();
    Node actualNext = actual.getNext();
    NodeUtil.assertEqualsNodes(expected, actual);
    NodeUtil.assertEqualsNodes(expectedNext, actualNext);
  }

  @Test
  public void testIdentityReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.IDENTIFIER, 1, 1, "a")),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertNotNull(functionNode.getFirstChild());
    assertNotNull(functionNode.getFirstChild().getNext());
    Node expected = new Node(Token.CALL, 1, 1, null, Arrays.asList(
        new Node(Token.NAME, 1, 1, "JSCompiler_identityFn"),
        new Node(Token.IDENTIFIER, 1, 1, "a")
    ));
    Node actual = functionNode.getFirstChild();
    Node expectedNext = functionNode.getFirstChild().getNext();
    Node actualNext = actual.getNext();
    NodeUtil.assertEqualsNodes(expected, actual);
    NodeUtil.assertEqualsNodes(expectedNext, actualNext);
  }

  @Test
  public void testReturnConstantReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.INTEGER_LITERAL, 1, 1, "10")),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertNotNull(functionNode.getFirstChild());
    assertNotNull(functionNode.getFirstChild().getNext());
    Node expected = new Node(Token.CALL, 1, 1, null, Arrays.asList(
        new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"),
        new Node(Token.INTEGER_LITERAL, 1, 1, "10")
    ));
    Node actual = functionNode.getFirstChild();
    Node expectedNext = functionNode.getFirstChild().getNext();
    Node actualNext = actual.getNext();
    NodeUtil.assertEqualsNodes(expected, actual);
    NodeUtil.assertEqualsNodes(expectedNext, actualNext);
  }

  @Test
  public void testGetterReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.GETPROP, 1, 1, new Node(Token.THIS), new Node(Token.IDENTIFIER, 1, 1, "b"))),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertNotNull(functionNode.getFirstChild());
    assertNotNull(functionNode.getFirstChild().getNext());
    Node expected = new Node(Token.CALL, 1, 1, null, Arrays.asList(
        new Node(Token.NAME, 1, 1, "JSCompiler_get"),
        new Node(Token.STRING_LITERAL, 1, 1, "b")
    ));
    Node actual = functionNode.getFirstChild();
    Node expectedNext = functionNode.getFirstChild().getNext();
    Node actualNext = actual.getNext();
    NodeUtil.assertEqualsNodes(expected, actual);
    NodeUtil.assertEqualsNodes(expectedNext, actualNext);
  }

  @Test
  public void testSetterReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.ARGLIST, 1, 1, Arrays.asList(new Node(Token.IDENTIFIER, 1, 1, "value"))),
        new Node(Token.RETURN, 1, 1, new Node(Token.ASSIGN, 1, 1, new Node(Token.GETPROP, 1, 1, new Node(Token.THIS), new Node(Token.IDENTIFIER, 1, 1, "b")), new Node(Token.GETARG, 1, 1, 0))),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertNotNull(functionNode.getFirstChild());
    assertNotNull(functionNode.getFirstChild().getNext());
    Node expected = new Node(Token.CALL, 1, 1, null, Arrays.asList(
        new Node(Token.NAME, 1, 1, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, 1, 1, "b")
    ));
    Node actual = functionNode.getFirstChild();
    Node expectedNext = functionNode.getFirstChild().getNext();
    Node actualNext = actual.getNext();
    NodeUtil.assertEqualsNodes(expected, actual);
    NodeUtil.assertEqualsNodes(expectedNext, actualNext);
  }

  @Test
  public void testComplexFunctionReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.BINOP, 1, 1, Token.PLUS,
            new Node(Token.IDENTIFIER, 1, 1, "a"),
            new Node(Token.INTEGER_LITERAL, 1, 1, "1"))),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);

    assertFalse(isFunctionExpression(functionNode));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunctionReducer() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.RETURN, 1, 1, null)),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunctionReducer2() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.ASSIGN, 1, 1, null, new Node(Token.IDENTIFIER, 1, 1, "a"))),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunctionReducer3() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.IDENTIFIER, 1, 1, "a")),
        new Node(Token.RETURN, 1, 1, null),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunctionReducer4() {
    Node root = new Node(Token.PROGRAM, 1, 1);
    Node functionNode = new Node(Token.FUNCTION, 1, 1, null, Arrays.asList(
        new Node(Token.IDENTIFIER, 1, 1, "a"),
        new Node(Token.LEFT_BRACE, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.ASSIGN, 1, 1, new Node(Token.IDENTIFIER, 1, 1, "a"), new Node(Token.RETURN, 1, 1, null))),
        new Node(Token.RIGHT_BRACE, 1, 1)
    ));
    root.addChildrenToBack(functionNode);

    functionRewriter.process(null, root);
  }
}