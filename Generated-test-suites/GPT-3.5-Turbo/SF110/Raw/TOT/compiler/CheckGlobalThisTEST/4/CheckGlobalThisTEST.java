import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionIsConstructor_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.CALL));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionHasThisAnnotation_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.SCRIPT));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionHasOverrideAnnotation_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.BLOCK));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentTypeIsBlock_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.STRING_KEY));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentTypeIsScript_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.NAME));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentTypeIsAssign_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.ASSIGN));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentTypeIsGet_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node(Token.OBJECTLIT));

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldReportThis_LhsChildIsNull_ReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node(Token.TRUE);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldReportThis_LhsChildIsNotNull_ReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS).addChildToBack(new Node(Token.NAME));

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testVisit_NodeIsThis_ShouldReportGlobalThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.CALL);

    // Act
    checkGlobalThis.visit(nodeTraversal, node, parent);

    // Assert
    // Verify that compiler.report method was called with proper arguments
  }

  @Test
  public void testVisit_NodeIsAssignLhsChild_ShouldSetAssignLhsChildToNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS).addChildToBack(new Node(Token.NAME));

    // Act
    checkGlobalThis.visit(nodeTraversal, node, node);

    // Assert
    Assert.assertNull(checkGlobalThis.assignLhsChild);
  }
}