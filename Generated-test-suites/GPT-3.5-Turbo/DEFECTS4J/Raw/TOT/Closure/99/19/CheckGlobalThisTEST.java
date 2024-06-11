import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

  @Test
  public void shouldReportThis_onAssignmentToNonPrototypeProperty() {
    // Arrange
    Node n = createNodeWithToken(Token.THIS);
    Node parent = createNodeWithToken(Token.ASSIGN);
    parent.setType(Token.GETPROP);
    Node lhs = createNodeWithQualifiedName("object.property");
    parent.addChildToBack(lhs);
    parent.addChildToBack(createNodeWithToken(Token.STRING, "property"));

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void shouldNotReportThis_onAssignmentToPrototypeProperty() {
    // Arrange
    Node n = createNodeWithToken(Token.THIS);
    Node parent = createNodeWithToken(Token.ASSIGN);
    parent.setType(Token.GETPROP);
    Node lhs = createNodeWithQualifiedName("object.prototype.property");
    parent.addChildToBack(lhs);
    parent.addChildToBack(createNodeWithToken(Token.STRING, "property"));

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void shouldNotReportThis_onOtherTokenType() {
    // Arrange
    Node n = createNodeWithToken(Token.NAME);
    Node parent = createNodeWithToken(Token.GETPROP);
    Node lhs = createNodeWithQualifiedName("object.property");
    parent.addChildToBack(lhs);
    parent.addChildToBack(createNodeWithToken(Token.STRING, "property"));

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  private Node createNodeWithToken(int token) {
    Node node = new Node();
    node.setType(token);
    return node;
  }

  private Node createNodeWithQualifiedName(String qualifiedName) {
    Node node = new Node();
    node.setQualifiedName(qualifiedName);
    return node;
  }
}