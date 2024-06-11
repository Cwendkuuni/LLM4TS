import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_shouldOnlyTraverseFunctionsThatAreNotConstructors() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    // Act
    boolean result = cg.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_shouldNotTraverseConstructors() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    n.setJSDocInfo(new JSDocInfo());
    n.getJSDocInfo().setConstructor(true);

    // Act
    boolean result = cg.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldNotTraverseFunctionsAnnotatedWithThis() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    n.setJSDocInfo(new JSDocInfo());
    n.getJSDocInfo().setThisType(new JSTypeExpression(new Node(Token.STRING), "myType"));

    // Act
    boolean result = cg.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_shouldNotTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    ParentNode grandparent = new ParentNode(ParserRegistry.DEFAULT_LANGUAGE);
    grandparent.setType(Token.BLOCK);
    parent.setParent(grandparent);

    // Act
    boolean result = cg.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_shouldReportThisWhenOnLeftSideOfAssignment() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    cg.visit(t, n, parent);
    DiagnosticType error = cg.GLOBAL_THIS;

    // Assert
    assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), error);
  }

  @Test
  public void testVisit_shouldReportThisWhenInsidePropertyAccess() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    cg.visit(t, n, parent);
    DiagnosticType error = cg.GLOBAL_THIS;

    // Assert
    assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), error);
  }

  @Test
  public void testShouldReportThis_shouldReturnTrueWhenAssignLhsChildNotNull() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    cg.assignLhsChild = new Node();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    boolean result = cg.shouldReportThis(n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnFalseWhenParentIsNull() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = null;

    // Act
    boolean result = cg.shouldReportThis(n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnFalseWhenParentIsNotGet() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);

    // Act
    boolean result = cg.shouldReportThis(n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_shouldReturnFalseWhenAssignLhsChildIsNull() {
    // Arrange
    CheckGlobalThis cg = new CheckGlobalThis(null, null);
    cg.assignLhsChild = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    boolean result = cg.shouldReportThis(n, parent);

    // Assert
    assertFalse(result);
  }
}