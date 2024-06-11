import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNodeForType(Token.FOR, 0, new Object[3]);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = createNodeForType(Token.IF, 0, new Object[1]);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNodeForType(Token.IF, 0, new Object[1]);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = createNodeForType(Token.NUMBER, 0, 1);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNodeFunctionDeclaration("myFunction");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = createNodeForType(Token.VAR, 0, new Object[]{"myFunction"});
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNodeForFunctionDeclaration("myFunction", new Object[0]);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = createNodeForFunctionDeclaration("myFunction", new Object[]{new NodeExpression(createNodeForName("x"))});
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  private Node createNodeForType(int type, int lineNumber, Object[] children) {
    Node node = new Node(type);
    node.setLineNumber(lineNumber);
    node.setChildren(children);
    return node;
  }

  private Node createNodeFunctionDeclaration(String name) {
    Node nameNode = new NodeName(name);
    Node functionNode = new NodeFunction(nameNode, new NodeList(new Node[]{}), new NodeList(new Node[]{}), new NodeList(new Node[0]), NodeFlags.NONE);
    return functionNode;
  }

  @Test
  public void testPreconditionsCheckState() {
    Preconditions.checkState(true, "This should not throw an exception");
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFalse() {
    Preconditions.checkState(false, "This should throw an AssertionError");
  }

  private static class NodeExpression extends Node {
    NodeExpression(Node node) {
      super(Token.EXPRESSION, 0, new Object[]{node});
    }
  }

  private static class NodeName extends Node {
    NodeName(String name) {
      super(Token.IDENTIFIER, 0, new Object[]{name});
    }
  }

  private static class NodeList extends NodeListBase {
    NodeList(Node[] nodes) {
      super(Token.LIST, 0, nodes);
    }
  }
}