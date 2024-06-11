package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class CheckGlobalThisTest {
  private Checker checker;
  private CheckGlobalThis checkerUnderTest;
  private NodeTraversal traversal;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    checker = CheckerFactory.createChecker(CheckGlobalThis.class, options);
    compiler = mock(AbstractCompiler.class);
    checkerUnderTest = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(checkerUnderTest, null, null, null);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyAccess() {
    Node node = new Node(Token.NAME, "obj", null);
    Node propertyAccess = new Node(Token.GETPROP, node, new Node(Token.STRING, "prototype", null));
    Node function = new Node(Token.FUNCTION, null, null);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    node.setParent(propertyAccess);
    propertyAccess.setParent(functionCall);
    functionCall.setParent(new Node(Token.ASSIGN, new Node(Token.NAME, "x", null), functionCall));

    assertTrue(checkerUnderTest.shouldTraverse(traversal, functionCall, function));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccessInsidePrototypeMethod() {
    Node node = new Node(Token.NAME, "obj", null);
    Node propertyAccess = new Node(Token.GETPROP, node, new Node(Token.STRING, "prototype", null));
    Node prototypeMethod = new Node(Token.FUNCTION, null, null);
    Node prototypeMethodCall = new Node(Token.CALL, prototypeMethod, new Node[0]);

    Node prototypeMethodPrototypePropertyAccess =
        new Node(Token.GETPROP, new Node(Token.THIS, null), new Node(Token.STRING, "prototype", null));
    Node prototypeMethodBody = new Node(Token.BLOCK, new Node[0]);

    node.setParent(propertyAccess);
    propertyAccess.setParent(prototypeMethod);
    prototypeMethod.setFirstChild(prototypeMethodPrototypePropertyAccess);
    prototypeMethod.setLastChild(prototypeMethodBody);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, prototypeMethodPrototypePropertyAccess, prototypeMethod));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node function = new Node(Token.FUNCTION, null, null);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoPossibleThisAnnotation() {
    Node function = new Node(Token.FUNCTION, null, null);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, functionCall, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node function = new Node(Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(new Node(Token.THIS, null, null));
    function.setJSDocInfo(jsDocInfo);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, functionCall, function));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node function = new Node(Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setInterface(true);
    function.setJSDocInfo(jsDocInfo);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, functionCall, function));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node function = new Node(Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    function.setJSDocInfo(jsDocInfo);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, functionCall, function));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node function = new Node(Token.FUNCTION, null, null);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    function.setJSDocInfo(jsDocInfo);
    Node functionCall = new Node(Token.CALL, function, new Node[0]);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, functionCall, function));
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), new Node[0]);

    assertTrue(checkerUnderTest.shouldTraverse(traversal, assign, null));
  }

  @Test
  public void testShouldTraverseAssignmentToThisWithParentFunction() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), null);
    Node functionCall = new Node(Token.CALL, new Node(Token.FUNCTION, null, null), new Node[0]);

    assign.setParent(functionCall);

    assertTrue(checkerUnderTest.shouldTraverse(traversal, assign, functionCall));
  }

  @Test
  public void testShouldNotTraverseAssignmentToThisWithParentPropertyAccess() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), null);
    Node propertyAccess = new Node(Token.GETPROP, new Node(Token.NAME, "obj", null), null);

    assign.setParent(propertyAccess);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, assign, propertyAccess));
  }

  @Test
  public void testShouldNotTraverseAssignmentToThisWithParentPropertyAccessInsidePrototypeMethod() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), null);
    Node propertyAccess = new Node(Token.GETPROP, new Node(Token.NAME, "obj", null), null);
    Node prototypeMethod = new Node(Token.FUNCTION, null, null);

    propertyAccess.setParent(prototypeMethod);
    assign.setParent(prototypeMethod);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, assign, prototypeMethod));
  }

  @Test
  public void testShouldNotTraverseAssignmentToThisWithParentNonFunction() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), null);
    Node parent = new Node(Token.BLOCK, new Node[0]);

    assign.setParent(parent);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, assign, parent));
  }

  @Test
  public void testShouldReportThisKeywordAtAssignment() {
    Node assign = new Node(Token.ASSIGN, new Node(Token.THIS, null, null), new Node[0]);
    Node parent = new Node(Token.ASSIGN, null, assign);

    when(compiler.report(ArgumentMatcher.isNull(), ArgumentMatcher.isInstance(CheckLevel.WARNING), ArgumentMatcher.isInstance(DiagnosticType.class)))
        .thenReturn(new Node[0]);

    checkerUnderTest.visit(traversal, assign, parent);

    Node errorNode = new Node(Token.ERROR, null, null);

    assertTrue(parent.getChildren()[0].equals(errorNode));
  }

  @Test
  public void testShouldNotReportThisKeywordAtPropertyAccess() {
    Node propertyAccess = new Node(Token.GETPROP, new Node(Token.THIS, null, null), null);
    Node parent = new Node(Token.ASSIGN, null, propertyAccess);

    when(compiler.report(ArgumentMatcher.isNull(), ArgumentMatcher.isInstance(CheckLevel.WARNING), ArgumentMatcher.isInstance(DiagnosticType.class)))
        .thenReturn(new Node[0]);

    checkerUnderTest.visit(traversal, propertyAccess, parent);

    Node errorNode = new Node(Token.ERROR, null, null);

    assertNull(parent.getFirstChild());
  }

  @Test
  public void testShouldNotReportThisKeywordAtPropertyAccessInsidePrototypeMethod() {
    Node prototypeMethod = new Node(Token.FUNCTION, null, null);
    Node prototypeMethodPrototypePropertyAccess =
        new Node(Token.GETPROP, new Node(Token.THIS, null, null), new Node(Token.STRING, "prototype", null));
    Node prototypeMethodBody = new Node(Token.BLOCK, new Node[0]);

    prototypeMethod.setFirstChild(prototypeMethodPrototypePropertyAccess);
    prototypeMethod.setLastChild(prototypeMethodBody);

    when(compiler.report(ArgumentMatcher.isNull(), ArgumentMatcher.isInstance(CheckLevel.WARNING), ArgumentMatcher.isInstance(DiagnosticType.class)))
        .thenReturn(new Node[0]);

    checkerUnderTest.visit(traversal, prototypeMethodPrototypePropertyAccess, prototypeMethod);

    Node errorNode = new Node(Token.ERROR, null, null);

    assertNull(prototypeMethodPrototypePropertyAccess.getParent().getFirstChild());
  }
}