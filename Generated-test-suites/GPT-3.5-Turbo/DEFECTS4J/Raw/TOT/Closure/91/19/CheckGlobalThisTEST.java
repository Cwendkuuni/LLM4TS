import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalContext_ReturnTrue() {
    // 1. Arrange
    CheckGlobalThis checker = new CheckGlobalThis();

    // 2. Act
    boolean result = checker.shouldTraverse(null, null, null);

    // 3. Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // Set up function with constructor annotation
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    // 2. Act
    boolean result = checker.shouldTraverse(t, n, parent);

    // 3. Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // Set up function with @this annotation
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("MyObject");
    n.setJSDocInfo(jsDoc);

    // 2. Act
    boolean result = checker.shouldTraverse(t, n, parent);

    // 3. Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation_ReturnFalse() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // Set up function with no logical place for @this annotation
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.VAR);

    // 2. Act
    boolean result = checker.shouldTraverse(t, n, parent);

    // 3. Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NonFunctionNode_ReturnFalse() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING);
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // 2. Act
    boolean result = checker.shouldTraverse(t, n, parent);

    // 3. Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_NodeIsGlobalThis_ProducesWarning() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // 2. Act
    checker.visit(t, n, parent);
    DiagnosticType result = compiler.getLastReportedDiag();

    // 3. Assert
    assertEquals(DiagnosticType.GLOBAL_THIS, result);
  }

  @Test
  public void testVisit_NodeIsNotGlobalThis_NoWarningProduced() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "not_this");
    Node parent = new Node();
    CheckGlobalThis checker = new CheckGlobalThis();

    // 2. Act
    checker.visit(t, n, parent);
    DiagnosticType result = compiler.getLastReportedDiag();

    // 3. Assert
    assertNull(result);
  }

  @Test
  public void testVisit_NodeIsAssignLhsChild_NoWarningProduced() {
    // 1. Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "this");
    Node parent = new Node(Token.ASSIGN);
    CheckGlobalThis checker = new CheckGlobalThis();

    // Set up assignLhsChild variable
    checker.assignLhsChild = n;

    // 2. Act
    checker.visit(t, n, parent);
    DiagnosticType result = compiler.getLastReportedDiag();

    // 3. Assert
    assertNull(result);
    assertNull(checker.assignLhsChild);
  }

  // Other test cases for the remaining methods in the CheckGlobalThis class can be added similarly

}