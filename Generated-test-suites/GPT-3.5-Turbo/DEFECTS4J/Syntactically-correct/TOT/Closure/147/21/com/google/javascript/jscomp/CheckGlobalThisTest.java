import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, new Node(Token.FUNCTION), new Node(Token.ASSIGN));

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, new Node(Token.FUNCTION), new Node(Token.SCRIPT));

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldReturnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, new Node(Token.FUNCTION), new Node(Token.BLOCK));

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithoutAnnotation_ShouldReturnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, new Node(Token.FUNCTION), new Node(Token.NAME));

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisNodeOnLeftSideOfAssignment_ShouldReportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Ensure that compiler.report() is called with the expected error
    assertTrue(compiler.reportedErrors.contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisNodeWithPropertyAccess_ShouldReportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.STRING, "property"));

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Ensure that compiler.report() is called with the expected error
    assertTrue(compiler.reportedErrors.contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisNodeOnLeftSideOfAssignment_NestedAssignment_ShouldNotReportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Ensure that compiler.report() is not called
    assertFalse(compiler.reportedErrors.contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisNodeWithPropertyAccess_PrototypeProperty_ShouldNotReportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.STRING, "prototype"));

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Ensure that compiler.report() is not called
    assertFalse(compiler.reportedErrors.contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_ThisNodeInsideOtherNode_ShouldNotReportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(new Node(Token.STRING, "statement"));

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Ensure that compiler.report() is not called
    assertFalse(compiler.reportedErrors.contains(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_AssignLhsChildNotSet_ShouldNotChangeAssignLhsChildToNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node assignLhsChildBefore = new Node(Token.NAME, "a");
    Node n = new Node(Token.STRING, "b");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(assignLhsChildBefore);
    parent.addChildToBack(n);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertEquals(assignLhsChildBefore, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_AssignLhsChildSet_ShouldResetAssignLhsChildToNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING, "b");
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME, "a");
    parent.addChildToBack(checkGlobalThis.assignLhsChild);
    parent.addChildToBack(n);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }

}