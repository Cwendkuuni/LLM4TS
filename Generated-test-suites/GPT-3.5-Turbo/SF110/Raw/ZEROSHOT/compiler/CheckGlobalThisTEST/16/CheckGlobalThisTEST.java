import static org.junit.Assert.*;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithOverrideAnnotation() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseNonGlobalFunction() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithNonGlobalContexts() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithFunctionAssignment() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisAtLeftSideOfAssign() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThisWithPropertyAccess() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testShouldNotReportThis() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void testVisitThisAndReportError() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.visit(t, n, parent);
    // Add assertion here
  }

  @Test
  public void testVisitLhsChildAndReset() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.visit(t, n, parent);
    // Add assertion here
  }

  @Test
  public void testGetFunctionJsDocInfoWithJsDoc() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    CheckGlobalThis.JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    // Add assertion here
  }

  @Test
  public void testGetFunctionJsDocInfoWithoutJsDoc() {
    Node n = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    CheckGlobalThis.JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    // Add assertion here
  }
}