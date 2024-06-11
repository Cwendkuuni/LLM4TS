import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for");
    Node child1 = new Node("init");
    Node child2 = new Node("condition");
    Node child3 = new Node("next");
    node.setChildren(child1, child2, child3);

    assertTrue(isForIn(node));

    node.setChildren(child1, child2); // less than 3 children
    assertFalse(isForIn(node));

    Node nodeWithWrongType = new Node("if");
    assertFalse(isForIn(nodeWithWrongType));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node("print", "1");
    Node expressionNode = new Node("1 + 1");

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(expressionNode));

    Node parentScript = new Node("script");
    Node parentBlock = new Node("{ }");
    Node parentLabel = new Node("label");

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(parentScript));
    assertTrue(isStatement(parentBlock));
    assertFalse(isStatement(parentLabel)); // labels are not statements
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node("function", "foo");
    Node functionExpression = new Node("function", "bar");
    functionExpression.setParent(new Node("expression"));

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));

    Node functionDeclarationInBlock = new Node("function", "baz", null, new Node("{}"));
    assertTrue(isFunctionDeclaration(functionDeclarationInBlock));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node("function", "qux");
    Node hoistedFunctionDeclarationInScript = new Node("script", null, new Node("function", "qux"));
    Node hoistedFunctionDeclarationInFunction = new Node("function", "quux", null, new Node("function", "qux"));

    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction)); // hoisted inside another function

    Node hoistedFunctionDeclarationInGlobal = new Node("script", null, new Node("function", "qux"));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInGlobal));
  }
}