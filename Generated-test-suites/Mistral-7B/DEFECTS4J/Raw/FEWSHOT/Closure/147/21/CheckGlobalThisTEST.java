package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraversePrototypeMethod() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node prototypeNode = new Node(Token.NAME, "prototype");
    Node functionNode = new Node(Token.FUNCTION, null);
    Node getPropertyNode = new Node(Token.GETPROP, null, functionNode, prototypeNode);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldTraverse(traversal, getPropertyNode, functionNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoJsDocInfo() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(Token.FUNCTION, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJsDocConstructor() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, jsDoc);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithJsDocThis() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo jsDoc = new JSDocInfo("this", new String[]{"foo"});
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, jsDoc);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(Token.FUNCTION, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldTraverse(traversal, functionNode, parentBlockNode));
  }

  @Test
  public void testShouldNotTraverseLocalFunction() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node functionNode = new Node(Token.FUNCTION, null);
    Node parentAssignmentNode = new Node(Token.ASSIGN, null, null, functionNode);
    Node parentVarNode = new Node(Token.VAR, null, null, null, null, null, null, null, null, null, null, null, null, null, parentAssignmentNode);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, functionNode, parentVarNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocInterface() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo jsDoc = new JSDocInfo("interface");
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, jsDoc);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocOverride() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo jsDoc = new JSDocInfo("override");
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, jsDoc);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseNonFunctionNode() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node nonFunctionNode = new Node(Token.NUMBER, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, nonFunctionNode, null));
  }

  @Test
  public void testShouldNotTraverseParentNode() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parentNode = new Node(Token. Name, "parent");
    Node childNode = new Node(Token.THIS, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, childNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseParentPrototypeProperty() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parentNode = new Node(Token.NAME, "parent");
    Node getPropertyNode = new Node(Token.GETPROP, null, parentNode, new Node(Token.STRING, "prototype"));
    Node childNode = new Node(Token.THIS, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, childNode, getPropertyNode));
  }

  @Test
  public void testShouldNotTraverseParentPrototypeSubProperty() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node parentNode = new Node(Token.NAME, "parent");
    Node getPropertyNode = new Node(Token.GETPROP, null, parentNode, new Node(Token.NAME, "foo"));
    Node getPropertySubPropertyNode = new Node(Token.GETPROP, null, getPropertyNode, new Node(Token.STRING, "prototype"));
    Node childNode = new Node(Token.THIS, null);
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertFalse(check.shouldTraverse(traversal, childNode, getPropertySubPropertyNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignmentNode = new Node(Token.ASSIGN, null, null, new Node(Token.THIS, null));
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldReportThis(traversal.getCurrentNode(), assignmentNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node getPropertyNode = new Node(Token.GETPROP, null, null, new Node(Token.THIS, null));
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    assertTrue(check.shouldReportThis(traversal.getCurrentNode(), getPropertyNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node assignmentNode = new Node(Token.ASSIGN, null, null, new Node(Token.THIS, null));
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node rhsNode = new Node(Token.STRING, "rhs");
    Node lhsNode = new Node(Token.NAME, "lhs");
    Node assignmentNodeWithLhs = new Node(Token.ASSIGN, null, lhsNode, assignmentNode);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    traversal.traverse(lhsNode);
    traversal.traverse(assignmentNodeWithLhs);
    traversal.traverse(rhsNode);

    assertNull(check.assignLhsChild);
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfPropertyAccess() {
    CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
    Node getPropertyNode = new Node(Token.GETPROP, null, null, new Node(Token.THIS, null));
    Node parentBlockNode = new Node(Token.BLOCK, null);
    Node getPropertyNodeWithThis = new Node(Token.GETPROP, null, null, getPropertyNode);
    Node propertyNameNode = new Node(Token.STRING, "propertyName");
    Node getPropertyNodeWithThisAndPropertyName = new Node(Token.GETPROP, null, getPropertyNode, propertyNameNode);
    Node traversal = new NodeTraversal(check, parentBlockNode);

    traversal.traverse(getPropertyNodeWithThisAndPropertyName);

    assertNull(check.assignLhsChild);
  }
}