import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createForStatement;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createScript;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createForStatement(Token.FOR, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createFunction(Token.FUNCTION, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createLabel("label");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = createToken(Token.NUMBER, "42");
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createFunction(Token.FUNCTION, null, null, null, null, null);
    functionDeclaration.setName("myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createFunction(Token.FUNCTION, null, null, null, null, null);
    functionExpression.setName(null);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createFunction(Token.FUNCTION, null, null, null, null, null);
    hoistedFunctionDeclaration.setName("myFunction");
    Node parentScript = createScript(null, null, null, null);
    parentScript.addChild(hoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node notHoistedFunctionDeclaration = createFunction(Token.FUNCTION, null, null, null, null, null);
    notHoistedFunctionDeclaration.setName("myFunction");
    Node parentBlock = createBlock(null, null, null);
    parentBlock.addChild(notHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }

  private Node createFunction(int type, Node[] params, Node body, Node generator, Node prototype, Node[] source) {
    Node functionNode = createNodeFactory().createFunction(type, params, body, generator, prototype, source);
    return functionNode;
  }

  private Node createLabel(String name) {
    return createNodeFactory().createLabel(name);
  }

  private Node createBlock(Node[] body, Node generator, Node[] source) {
    return createNodeFactory().createBlock(body, generator, source);
  }

  private Node createScript(Node[] body, Node[] source) {
    return createNodeFactory().createScript(body, source);
  }

  private Node createToken(int type, String value) {
    return createNodeFactory().createToken(type, value);
  }

  private NodeFactory createNodeFactory() {
    return NodeFactory.getFactory();
  }
}