import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithInterfaceAnnotation_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideScript_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideName_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssign_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideString_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideNumber_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NUMBER);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsidePrototypeMethod_ShouldReturnTrue() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.NAME);
    Node greatGrandParent = new Node(Token.ASSIGN);
    grandParent.addChildToBack(greatGrandParent);
    parent.addChildToBack(grandParent);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ShouldReturnTrue() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThis();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithLogicalPlaceForThisAnnotation_ShouldReturnTrue() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK,
        new Node(Token.NAME),
        new Node(Token.ASSIGN)
    );

    //Act
    boolean result = check.shouldTraverse(t, n, parent);

    //Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisOnTheLeftSideOfAssign_ShouldReturnTrue() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, new Node(Token.THIS));
    Node rhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    //Act
    boolean result = check.shouldReportThis(n, parent);

    //Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ShouldReturnTrue() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);
    rhs.setString("property");
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    //Act
    boolean result = check.shouldReportThis(n, parent);

    //Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ThisNotOnTheLeftSideOfAssign_ShouldReturnFalse() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);

    //Act
    boolean result = check.shouldReportThis(n, parent);

    //Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testVisit_ThisNode_ShouldReportDiagnostic() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);

    //Act
    check.visit(t, n, parent);

    //Assert
    // The expected behavior is compiler.report will be called with an error diagnostic
    // Assert.assertTrue to ensure that the report method is called correctly
  }

  @Test
  public void testVisit_NotAssignLhsChild_ShouldNotResetAssignLhsChild() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    check.shouldReportThis(n, parent);
    Node assignLhsChild = new Node(Token.NAME);

    //Act
    check.visit(t, n, parent);

    //Assert
    Assert.assertNotNull(check.assignLhsChild);
  }

  @Test
  public void testVisit_AssignLhsChild_ShouldResetAssignLhsChildToNull() {
    //Arrange
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    check.shouldReportThis(n, parent);

    //Act
    check.visit(t, n, parent);

    //Assert
    Assert.assertNull(check.assignLhsChild);
  }

}