package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Test empty object literal
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        null, null);
    Node root = new Node(Token.END_OF_INPUT);
    inlineObjectLiterals.process(null, root);

    // No object literals to inline
    Node externs = new Node(Token.END_OF_INPUT);
    Node emptyObjectLiteral = new Node(Token.OBJECT_LITERAL, null, null);
    root = new Node(Token.PROGRAM, null, Lists.newArrayList(externs, emptyObjectLiteral));
    inlineObjectLiterals.process(externs, root);

    Node emptyObject = root.getLastChild();
    assertTrue(emptyObject.isObjectLit());
    assertEquals(0, emptyObject.getFirstChildCount());

    // Test inlineable object literal
    externs = new Node(Token.END_OF_INPUT);
    emptyObjectLiteral = new Node(Token.OBJECT_LITERAL, null, null);
    Node property1 = new Node(Token.PROPERTIES, null, null);
    Node propertyName1 = new Node(Token.IDENT, "property1", null);
    Node propertyValue1 = new Node(Token.INTEGER, 1, null);
    Node property1Value = new Node(Token.ASSIGN, null, propertyName1, propertyValue1);
    property1.addChildToFront(property1Value);
    emptyObjectLiteral.addChildToFront(property1);

    root = new Node(Token.PROGRAM, null, Lists.newArrayList(externs, emptyObjectLiteral));
    inlineObjectLiterals.process(externs, root);

    emptyObject = root.getLastChild();
    assertTrue(emptyObject.isObjectLit());
    assertEquals(1, emptyObject.getFirstChildCount());

    Node propertyNode = emptyObject.getFirstChild();
    assertEquals("property1", propertyNode.getString());

    Node propertyValueNode = propertyNode.getFirstChild();
    assertEquals(1, propertyValueNode.getFirstChild().getIntValue());

    // Test uninlineable object literal
    externs = new Node(Token.END_OF_INPUT);
    emptyObjectLiteral = new Node(Token.OBJECT_LITERAL, null, null);
    property1 = new Node(Token.PROPERTIES, null, null);
    propertyName1 = new Node(Token.IDENT, "property1", null);
    propertyValue1 = new Node(Token.OBJECT_LITERAL, null, null);
    Node propertyName2 = new Node(Token.IDENT, "property2", null);
    Node propertyValue2 = new Node(Token.INTEGER, 2, null);
    Node property2Value = new Node(Token.ASSIGN, null, propertyName2, propertyValue2);
    property1.addChildToFront(property1Value);
    propertyValue1.addChildToFront(property1);
    emptyObjectLiteral.addChildToFront(propertyValue1);

    root = new Node(Token.PROGRAM, null, Lists.newArrayList(externs, emptyObjectLiteral));
    inlineObjectLiterals.process(externs, root);

    emptyObject = root.getLastChild();
    assertTrue(emptyObject.isObjectLit());
    assertEquals(1, emptyObject.getFirstChildCount());

    propertyNode = emptyObject.getFirstChild();
    assertEquals("property1", propertyNode.getString());

    propertyValueNode = propertyNode.getFirstChild();
    assertTrue(propertyValueNode.isObjectLit());
    assertEquals(1, propertyValueNode.getFirstChildCount());

    Node propertyValueNodeChild = propertyValueNode.getFirstChild();
    assertEquals("property2", propertyValueNodeChild.getString());

    // Test inlineable object literal with self-referential assignment
    externs = new Node(Token.END_OF_INPUT);
    emptyObjectLiteral = new Node(Token.OBJECT_LITERAL, null, null);
    property1 = new Node(Token.PROPERTIES, null, null);
    propertyName1 = new Node(Token.IDENT, "property1", null);
    propertyValue1 = new Node(Token.ASSIGN, null, propertyName1, emptyObjectLiteral);
    property1.addChildToFront(property1Value);
    emptyObjectLiteral.addChildToFront(property1);

    root = new Node(Token.PROGRAM, null, Lists.newArrayList(externs, emptyObjectLiteral));
    inlineObjectLiterals.process(externs, root);

    emptyObject = root.getLastChild();
    assertFalse(emptyObject.isObjectLit());
  }
}