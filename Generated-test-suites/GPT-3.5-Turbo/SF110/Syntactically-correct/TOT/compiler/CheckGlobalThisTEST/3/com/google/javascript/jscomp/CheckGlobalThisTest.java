import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentIsNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(null, null, null);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenFunctionIsNotConstructorOrHasThisAnnotation() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentTypeIsBlock() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentTypeIsScript() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentTypeIsName() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentTypeIsAssign() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsTrueWhenParentTypeIsStringKey() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ReturnsFalseWhenFunctionIsConstructor() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ReturnsFalseWhenFunctionHasThisAnnotation() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType(new Node(Token.TYPE_VOID));
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ReturnsFalseWhenFunctionIsInterface() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ReturnsFalseWhenFunctionIsOverride() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    // Assert
    Assert.assertFalse(result);
  }

  // Other test cases for shouldTraverse() can be added

  @Test
  public void testVisit_ReportsErrorWhenNodeIsThisAndShouldReportThisIsTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);

    // Act
    checkGlobalThis.visit(t, n, null);

    // Assert
    // Check that the error was reported
    Assert.assertNotNull(t.getError());
    Assert.assertEquals(DiagnosticType.warning, t.getError().getType());
    Assert.assertEquals("dangerous use of the global 'this' object", t.getError().getMessage());
  }

  @Test
  public void testShouldReportThis_ReturnsTrueWhenAssignLhsChildIsNotNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.FUNCTION);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ReturnsFalseWhenParentIsNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.THIS);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ReturnsTrueWhenParentIsGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.THIS));

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ReturnsFalseWhenParentIsNotGet() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);

    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);

    // Assert
    Assert.assertFalse(result);
  }

  // Other test cases for shouldReportThis() can be added

  @Test
  public void testGetFunctionJsDocInfo_ReturnsNullWhenJsDocIsNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.FUNCTION);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ReturnsJsDocInfoFromParentWhenJsDocIsNull() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ReturnsJsDocInfoFromGrampsWhenJsDocIsNullAndParentIsVar() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    Assert.assertEquals(jsDoc, result);
  }

  // Other test cases for getFunctionJsDocInfo() can be added

}