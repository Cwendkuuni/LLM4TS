import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_WithTypeFunction_ReturnFalse() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // When
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithNonGlobalContext_ReturnFalse() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING);
    Node parent = null;

    // When
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithAssignmentToPrototype_ReturnFalse() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.STRING, "prototype"));
    parent.getFirstChild().addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().getFirstChild().addChildToFront(new Node(Token.NAME, "createClass"));

    // When
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_WithAssignmentToNonPrototype_ReturnTrue() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.STRING, "property"));
    parent.getFirstChild().addChildToFront(new Node(Token.THIS));

    // When
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Then
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_WithAssignLhsChildNotNull_ReturnTrue() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = null;
    checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.STRING), null);

    // When
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Then
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_WithParentNotNullAndIsGet_ReturnTrue() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getFirstChild().addChildToFront(new Node(Token.NAME, "property"));

    // When
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Then
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_WithParentNotNullAndIsNotGet_ReturnFalse() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NOT);
    parent.addChildToFront(new Node(Token.THIS));

    // When
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Then
    assertFalse(result);
  }

}