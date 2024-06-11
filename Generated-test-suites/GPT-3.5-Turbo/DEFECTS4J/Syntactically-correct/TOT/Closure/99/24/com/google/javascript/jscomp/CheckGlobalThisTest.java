import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalThisInFunction() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInFunctionWithConstructorAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInFunctionWithThisTypeAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInFunctionWithOverrideAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    n.setJSDocInfo(jsDocInfo);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisWhenParentIsNotBlockScriptNameAssign() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    Node grandParent = new Node(Token.VAR);
    parent.addChildToFront(grandParent);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInAssignment_LeftSide() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInAssignment_RightSide_PrototypeProperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    lhs.addChildToBack(n);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInAssignment_RightSide_PrototypeSubproperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node lhs = new Node(Token.NAME, "prototype.property");
    Node grandParent = new Node(Token.ASSIGN);
    grandParent.addChildToFront(lhs);
    Node parent = new Node(Token.NAME);
    lhs.addChildToBack(parent);
    grandParent.addChildToBack(n);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_GlobalThisInAssignment_RightSide_NotPrototypeProperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node lhs = new Node(Token.NAME, "property");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_GlobalThis_ShouldReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Need to check if the report method is called
  }

  @Test
  public void testVisit_GlobalThis_NotShouldReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // Need to check if the report method is not called
  }

  @Test
  public void testVisit_AssignLhsChild_AssignLhsChildShouldBeNullAfterVisit() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldTraverse(t, n, parent);

    // Act
    checkGlobalThis.visit(t, n, parent);
    Node assignLhsChild = checkGlobalThis.getAssignLhsChild();

    // Assert
    assertNull(assignLhsChild);
  }

}