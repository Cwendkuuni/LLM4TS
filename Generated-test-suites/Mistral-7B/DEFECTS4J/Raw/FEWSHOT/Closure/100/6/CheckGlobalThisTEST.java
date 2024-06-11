package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_nonFunction() {
    Node node = Node.newIntLiteral(1);
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, parent));
  }

  @Test
  public void testShouldTraverse_function() {
    Node functionNode = Node.newFunction("functionName", false, Node.newList(), Node.newList(), Node.newList(), null);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_constructorFunction() {
    JSDocInfo jsDoc = Node.newJsDocInfo("constructor", true);
    Node functionNode = Node.newFunction("functionName", jsDoc, Node.newList(), Node.newList(), Node.newList(), null);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation() {
    JSDocInfo jsDoc = Node.newJsDocInfo("@this", null);
    Node functionNode = Node.newFunction("functionName", null, Node.newList(), Node.newList(), Node.newList(), jsDoc);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotation() {
    Node functionNode = Node.newFunction("functionName", null, Node.newList(), Node.newList(), Node.newList(), null);
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newThis());
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testShouldReportThis_propertyAccess() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("property"));
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypeAccess() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("prototype"));
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccess() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("prototype"), Node.newIdentifier("property"));
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithDotNotation() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("prototype").getString(), "property");
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithBracketNotation() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newList(Node.newIdentifier("prototype"), Node.newString("property")), null);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithDotNotationAndIndex() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("prototype"), Node.newList(Node.newIdentifier("property"), Node.newIntLiteral(0)));
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithBracketNotationAndIndex() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newList(Node.newIdentifier("prototype"), Node.newString("property"), Node.newIntLiteral(0)), null);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithDotNotationAndNoIndex() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newIdentifier("prototype").getString(), "property");
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypePropertyAccessWithBracketNotationAndNoIndex() {
    Node getPropNode = Node.newGetProp(Node.newIdentifier("getPropNode"), Node.newList(Node.newIdentifier("prototype"), Node.newString("property")), null);
    Node parent = Node.newIdentifier("parent");
    assertFalse(checker.shouldReportThis(getPropNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypeAndPropertyAccess() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newIdentifier("assignNode"), Node.newIdentifier("prototype"), Node.newIdentifier("property")));
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypeAndPropertyAccessWithDotNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newIdentifier("assignNode"), Node.newIdentifier("prototype"), "property"));
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignmentWithPrototypeAndPropertyAccessWithBracketNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newIdentifier("assignNode"), Node.newList(Node.newIdentifier("prototype"), Node.newString("property")), null));
    Node parent = Node.newIdentifier("parent");
    assertTrue(checker.shouldReportThis(assignNode, parent));
  }

  @Test
  public void testVisit_thisKeywordInsideFunction() {
    Node functionNode = Node.newFunction("functionName", null, Node.newList(), Node.newList(), Node.newList(), null);
    Node thisKeywordNode = Node.newThis();
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, functionNode, parent);
    checker.visit(NodeTraversal.EMPTY, thisKeywordNode, parent);
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newThis());
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypePropertyAccess() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), Node.newIdentifier("prototype"), Node.newIdentifier("property")));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypePropertyAccessWithDotNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), "prototype", "property"));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypePropertyAccessWithBracketNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), Node.newList(Node.newIdentifier("prototype"), Node.newString("property")), null));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypeAndPropertyAccess() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), Node.newIdentifier("prototype"), Node.newIdentifier("property")));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypeAndPropertyAccessWithDotNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), "prototype", "property"));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypeAndPropertyAccessWithBracketNotation() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), Node.newList(Node.newIdentifier("prototype"), Node.newString("property")), null));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypeAndPropertyAccessWithDotNotationAndIndex() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), "prototype", Node.newList(Node.newIdentifier("property"), Node.newIntLiteral(0))));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testVisit_thisKeywordOnLeftSideOfAssignmentWithPrototypeAndPropertyAccessWithBracketNotationAndIndex() {
    Node assignNode = Node.newAssign(Node.newIdentifier("assignNode"), Node.newGetProp(Node.newThis(), Node.newList(Node.newIdentifier("prototype"), Node.newString("property"), Node.newIntLiteral(0)), null));
    Node parent = Node.newIdentifier("parent");
    checker.visit(NodeTraversal.EMPTY, assignNode, parent);
    assertTrue(compiler.hasErrors());
  }
}