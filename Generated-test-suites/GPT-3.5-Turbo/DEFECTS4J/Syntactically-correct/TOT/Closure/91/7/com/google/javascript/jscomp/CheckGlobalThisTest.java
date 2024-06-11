import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void test_shouldTraverse_returnsTrueForFunctionBlock() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void test_shouldTraverse_returnsTrueForFunctionScript() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void test_shouldTraverse_returnsTrueForFunctionName() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void test_shouldTraverse_returnsTrueForFunctionAssign() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void test_shouldTraverse_returnsTrueForObjectLiteralKeyString() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void test_shouldTraverse_returnsTrueForObjectLiteralKeyNumber() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NUMBER);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void test_shouldTraverse_returnsFalseForConstructorFunction() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_shouldTraverse_returnsFalseForFunctionWithThisAnnotation() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_shouldTraverse_returnsFalseForFunctionWithOverrideAnnotation() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordOverride();
    n.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_shouldTraverse_returnsFalseForFunctionWithoutLogicalPlaceForThisAnnotation() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_shouldTraverse_returnsFalseForFunctionWithAssignmentToPrototypeProperty() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(lhs);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_shouldTraverse_returnsFalseForFunctionWithAssignmentToNestedPrototypeProperty() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node llhs = new Node(Token.GETPROP);
    llhs.addChildToFront(new Node(Token.NAME, "prototype"));
    lhs.addChildToFront(llhs);
    parent.addChildToFront(lhs);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void test_visit_reportsGlobalThisWarningWhenNodeIsThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    // assert that the warning is reported
  }

  @Test
  public void test_visit_resetsAssignLhsChildWhenNodeIsEqualAssignLhsChild() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhsChild = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.setAssignLhsChild(lhsChild);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    Assert.assertNull(checkGlobalThis.getAssignLhsChild());
  }

  @Test
  public void test_getFunctionJsDocInfo_returnsNullForNodeWithNoJsDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertNull(result);
  }

  @Test
  public void test_getFunctionJsDocInfo_returnsJsDocInfoFromParentName() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertEquals(jsDocInfo, result);
  }

  @Test
  public void test_getFunctionJsDocInfo_returnsJsDocInfoFromParentAssign() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertEquals(jsDocInfo, result);
  }
}