import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_whenFunctionIsConstructorOrHasThisOrOverrideAnnotation_returnFalse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_whenFunctionIsNotConstructorOrHasNoThisOrOverrideAnnotation_returnTrue() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void visit_whenNodeIsThisAndShouldReportThis_reportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.GETPROP);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertTrue(t.hasError());
  }

  @Test
  public void visit_whenNodeIsThisAndShouldNotReportThis_doNotReportError() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.FUNCTION);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertFalse(t.hasError());
  }
}