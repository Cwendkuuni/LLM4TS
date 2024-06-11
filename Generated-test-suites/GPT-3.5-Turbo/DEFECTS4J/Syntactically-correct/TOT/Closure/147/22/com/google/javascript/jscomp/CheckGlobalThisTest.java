import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalThisOnLeftSideOfAssignment_ReportsError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_PropertyAccessToGlobalThis_ReportsError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_DoesNotTraverse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionsWithConstructorAnnotation_DoesNotTraverse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }


  @Test
  public void testVisit_GlobalThisOnLeftSideOfAssignment_ReportsError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType[] errors = compiler.getErrors();

    // Assert
    assertEquals(1, errors.length);
    assertEquals("JSC_USED_GLOBAL_THIS", errors[0].getKey());
  }

  @Test
  public void testVisit_GlobalThisOnRightSideOfAssignment_NoErrorReported() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType[] errors = compiler.getErrors();

    // Assert
    assertEquals(0, errors.length);
  }

  @Test
  public void testVisit_PropertyAccessToGlobalThis_ReportsError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType[] errors = compiler.getErrors();

    // Assert
    assertEquals(1, errors.length);
    assertEquals("JSC_USED_GLOBAL_THIS", errors[0].getKey());
  }

  @Test
  public void testVisit_OtherNode_NoErrorReported() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);

    // Act
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType[] errors = compiler.getErrors();

    // Assert
    assertEquals(0, errors.length);
  }

  @Test
  public void testVisit_AssignLhsChild_NullAfterHandlingAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldTraverse(t, n, parent);

    // Act
    checkGlobalThis.visit(t, n, parent);
    Node lhsChild = checkGlobalThis.getAssignLhsChild();

    // Assert
    assertNull(lhsChild);
  }

  @Test
  public void testVisit_AssignLhsChild_NotNullAfterHandlingNonAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldTraverse(t, n, parent);

    // Act
    checkGlobalThis.visit(t, n, parent);
    Node lhsChild = checkGlobalThis.getAssignLhsChild();

    // Assert
    assertNotNull(lhsChild);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDoc_HasJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    functionNode.setJSDocInfo(jsDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_NextParentHasJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    functionNode.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_NextParentIsVarWithJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME);
    Node varNode = new Node(Token.VAR, nameNode);
    JSDocInfo jsDoc = new JSDocInfo();
    varNode.setJSDocInfo(jsDoc);
    nameNode.addChildToFront(functionNode);
    functionNode.setParent(nameNode);
    nameNode.setParent(varNode);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_NoParentWithJSDocInfo() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    functionNode.setParent(parent);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNull(result);
  }
}