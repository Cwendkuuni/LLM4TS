import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for CheckGlobalThis.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  /**
   * Test method for constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(checker);
  }

  /**
   * Test method for shouldTraverse method with function nodes.
   */
  @Test
  public void testShouldTraverseFunctionNode() {
    Node functionNode = createNode(Token.FUNCTION, new int[] {1, 2});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, null));
  }

  /**
   * Test method for shouldTraverse method with prototype method nodes.
   */
  @Test
  public void testShouldTraversePrototypeMethodNode() {
    Node prototypeMethodNode = createNode(Token.FUNCTION, new int[] {1, 2, Token.GETPROP, Token.STRING_LITERAL, "prototype"});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), prototypeMethodNode, null));
  }

  /**
   * Test method for shouldTraverse method with constructor nodes.
   */
  @Test
  public void testShouldTraverseConstructorNode() {
    Node constructorNode = createNode(Token.FUNCTION, new int[] {Token.FUNCTION, 1, 2});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), constructorNode, null));
  }

  /**
   * Test method for shouldTraverse method with regular function nodes.
   */
  @Test
  public void testShouldTraverseRegularFunctionNode() {
    Node regularFunctionNode = createNode(Token.FUNCTION, new int[] {1, 2});
    Node parent = createNode(Token.NAME, new int[] {Token.NAME, 1});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), regularFunctionNode, parent));
  }

  /**
   * Test method for shouldTraverse method with assign nodes.
   */
  @Test
  public void testShouldTraverseAssignNode() {
    Node assignNode = createNode(Token.ASSIGN, new int[] {Token.NAME, 1, Token.THIS});
    Node parent = createNode(Token.ASSIGN, new int[] {Token.NAME, 1, Token.NAME, 1});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), assignNode, parent));
  }

  /**
   * Test method for shouldTraverse method with get nodes.
   */
  @Test
  public void testShouldTraverseGetNode() {
    Node getNode = createNode(Token.GETPROP, new int[] {Token.NAME, 1, Token.STRING_LITERAL, "prototype"});
    Node parent = createNode(Token.NAME, new int[] {Token.NAME, 1});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), getNode, parent));
  }

  /**
   * Test method for shouldTraverse method with get nodes with nested get property access.
   */
  @Test
  public void testShouldTraverseGetNodeWithNestedGetPropertyAccess() {
    Node getNode = createNode(Token.GETPROP, new int[] {Token.NAME, 1, Token.GETPROP, Token.STRING_LITERAL, "prototype", Token.STRING_LITERAL, "prototype"});
    Node parent = createNode(Token.NAME, new int[] {Token.NAME, 1});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), getNode, parent));
  }

  /**
   * Test method for shouldTraverse method with assign nodes with left side being a get property access.
   */
  @Test
  public void testShouldTraverseAssignNodeWithGetPropertyAccessLeftSide() {
    Node assignNode = createNode(Token.ASSIGN, new int[] {Token.GETPROP, Token.STRING_LITERAL, "prototype", Token.NAME, 1, Token.THIS});
    Node parent = createNode(Token.NAME, new int[] {Token.NAME, 1});
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), assignNode, parent));
  }

  /**
   * Test method for visit method.
   */
  @Test
  public void testVisit() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = createNode(Token.THIS, new int[0]);
    checker.visit(traversal, NodeTraversal.emptyTraversal(), node);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    checker.compiler.report(errorNodeCaptor.capture(traversal, NodeTraversal.emptyTraversal(), node, WARNING, null, GLOBAL_THIS));
    assertNotNull(errorNodeCaptor.getValue());
  }

  private Node createNode(int type, int... children) {
    Node node = new Node();
    node.setType(type);
    if (children != null) {
      for (int child : children) {
        node.addChild(createNode(Token.NULL, new int[0]));
        Node childNode = node.getLastChild();
        childNode.setType(child);
      }
    }
    return node;
  }
}