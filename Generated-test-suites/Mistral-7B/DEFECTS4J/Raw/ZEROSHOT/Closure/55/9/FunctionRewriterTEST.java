import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseSyntheticCode(String name, String source) {
        return null;
      }

      @Override
      public Node getNodeForCodeInsertion(Node parent) {
        return null;
      }

      @Override
      public void reportCodeChange() {
        // NO-OP
      }
    };

    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    Node expectedHelper = new Node(Token.FUNCTION, 0, 0,
            Token.FUNCTION, "emptyFn", Token.LEFT_PAREN, Token.RIGHT_PAREN,
            Token.BLOCK, 0, 0, null, null);

    Node actualHelper = NodeUtil.getFirstChild(bMethod);
    assertEquals(expectedHelper.toString(), actualHelper.toString());
  }

  @org.junit.Test
  public void testIdentityReducer() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.LEFT_PAREN, Token.NAME, "a", Token.RIGHT_PAREN));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    Node expectedHelper = new Node(Token.FUNCTION, 0, 0,
            Token.FUNCTION, "identityFn", Token.LEFT_PAREN, Token.NAME,
            "JSCompiler_identityFn", Token.RIGHT_PAREN, Token.BLOCK, 0, 0,
            null, null);

    Node actualHelper = NodeUtil.getFirstChild(bMethod);
    assertEquals(expectedHelper.toString(), actualHelper.toString());
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.RETURN, Token.INT_LITERAL, 10));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    Node expectedHelper = new Node(Token.FUNCTION, 0, 0,
            Token.FUNCTION, "returnArg", Token.LEFT_PAREN, Token.INT_LITERAL,
                10, Token.RIGHT_PAREN, Token.BLOCK, 0, 0,
            null, null);

    Node actualHelper = NodeUtil.getFirstChild(bMethod);
    assertEquals(expectedHelper.toString(), actualHelper.toString());
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.LEFT_PAREN, Token.NAME, "this", Token.DOT,
            Token.NAME, "b", Token.RIGHT_PAREN));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    Node expectedHelper = new Node(Token.FUNCTION, 0, 0,
            Token.FUNCTION, "get", Token.LEFT_PAREN, Token.NAME,
            "JSCompiler_get", Token.RIGHT_PAREN, Token.BLOCK, 0, 0,
            null, null);

    Node actualHelper = NodeUtil.getFirstChild(bMethod);
    assertEquals(expectedHelper.toString(), actualHelper.toString());
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.LEFT_PAREN, Token.NAME, "this", Token.DOT,
            Token.NAME, "b", Token.ASSIGN, Token.NAME, "value"));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    Node expectedHelper = new Node(Token.FUNCTION, 0, 0,
            Token.FUNCTION, "get", Token.LEFT_PAREN, Token.NAME,
            "JSCompiler_set", Token.RIGHT_PAREN, Token.BLOCK, 0, 0,
            null, null);

    Node actualHelper = NodeUtil.getFirstChild(bMethod);
    assertEquals(expectedHelper.toString(), actualHelper.toString());
  }

  @org.junit.Test
  public void testNoReduction() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.RETURN, Token.NAME, "x"));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    assertNotNull(bMethod);
  }

  @org.junit.Test
  public void testNoReductionWithArguments() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.LEFT_PAREN, Token.NAME, "x", Token.COMMA, Token.INT_LITERAL, 1,
            Token.RIGHT_PAREN));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    assertNotNull(bMethod);
  }

  @org.junit.Test
  public void testNoReductionWithReturn() {
    Node root = new Node(Token.BLOCK, 0, 0);
    root.addChildToBack(
        new Node(Token.ASSIGN, 0, 0,
            Token.NAME, "a", Token.DOT, "prototype", Token.DOT, "b",
            Token.EQUALS, Token.FUNCTION, 0, 0, null, null,
            Token.RETURN, Token.NAME, "x"));

    functionRewriter.process(null, root);

    List<Node> bMethods = NodeUtil.findChildrenOfType(root, Token.FUNCTION);
    assertEquals(1, bMethods.size());

    Node bMethod = bMethods.get(0);
    assertNotNull(bMethod);
  }
}