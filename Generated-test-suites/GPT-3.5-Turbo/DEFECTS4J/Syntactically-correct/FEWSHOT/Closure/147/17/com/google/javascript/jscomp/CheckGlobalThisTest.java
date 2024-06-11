package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForConstructorFunction() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
  
    Node parent = new Node(Token.ROOT);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithThisTypeAnnotation() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
  
    Node parent = new Node(Token.ROOT);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
  
    Node parent = new Node(Token.ROOT);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionInBlock() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionInScript() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithName() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionInAssignment() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldReturnTrueForFunctionWithoutAnnotation() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
  
    Node parent = new Node(Token.ROOT);
  
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
  
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnTrueForThisOnLeftSideOfAssign() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
  
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
  
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnTrueForThisWithPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
  
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
  
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnFalseForThisOnRightSideOfAssign() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
  
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnFalseForNonThisNode() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
  
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
  
    assertFalse(result);
  }

  @Test
  public void testVisit_shouldReportGlobalThis() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
  
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
  
    DiagnosticType diagnosticType = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
  
    t.makeError(n, CheckLevel.WARNING, diagnosticType);
    compiler.report(t.makeError(n, CheckLevel.WARNING, diagnosticType));
  }

  @Test
  public void testVisit_shouldResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
  
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
  
    DiagnosticType diagnosticType = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
  
    t.makeError(n, CheckLevel.WARNING, diagnosticType);
    compiler.report(t.makeError(n, CheckLevel.WARNING, diagnosticType));
  
    assertNull(assignLhsChild);
  }
}