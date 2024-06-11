import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.*;

import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;

  @Before
  public void setupCompiler() {
    compiler = new Compiler();
  }

  @Test
  public void testShouldTraverse_globalContext_returnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.SCRIPT);
    Node parent = null;

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_functionConstructorOrAnnotatedWithThis_returnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Mock function annotation
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_functionWithInvalidParent_returnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_assignmentWithThisOnLeftSide_returnTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Set assignLhsChild to n
    checkGlobalThis.visit(t, n, parent);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_assignmentToPrototypeProperty_returnFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertEquals(false, result);
  }

  @Test
  public void testVisit_thisOnGlobalContext_reportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);

    // Mock compiler report method
    boolean[] reportCalled = {false};
    AbstractCompiler mockCompiler = new Compiler() {
      @Override
      public void report(JSError error) {
        reportCalled[0] = true;
        assertEquals("dangerous use of the global 'this' object", error.description);
      }
    };

    checkGlobalThis = new CheckGlobalThis(mockCompiler, CheckLevel.WARNING);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertEquals(true, reportCalled[0]);
  }

  @Test
  public void testVisit_thisOnLeftSideOfAssignment_clearAssignLhsChild() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Set assignLhsChild to n
    checkGlobalThis.visit(t, n, parent);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_thisOnRightSideOfAssignment_dontClearAssignLhsChild() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    checkGlobalThis.assignLhsChild = lhs;

    // Act
    checkGlobalThis.visit(t, rhs, parent);

    // Assert
    assertEquals(lhs, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_thisOnRightSideOfAssignmentWithAssignmentToPrototypeProperty_dontReportError() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    Node rhs = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    checkGlobalThis.assignLhsChild = null;

    // Mock compiler report method
    boolean[] reportCalled = {false};
    AbstractCompiler mockCompiler = new Compiler() {
      @Override
      public void report(JSError error) {
        reportCalled[0] = true;
      }
    };
    checkGlobalThis = new CheckGlobalThis(mockCompiler, CheckLevel.WARNING);

    // Act
    checkGlobalThis.visit(t, rhs, parent);

    // Assert
    assertEquals(false, reportCalled[0]);
  }

  @Test
  public void testGetFunctionJsDocInfo_functionHasJSDocInfo_returnJSDocInfo() {
    // Arrange
    Node n = new Node(Token.FUNCTION);
    JSDocInfo expected = new JSDocInfo();
    n.setJSDocInfo(expected);

    // Act
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_parentHasJSDocInfo_returnJSDocInfoNotInParent() {
    // Arrange
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo());
    Node n = new Node(Token.FUNCTION);
    parent.addChildToBack(n);

    // Act
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(null, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_grandParentHasJSDocInfo_returnJSDocInfoNotInGrandParent() {
    // Arrange
    Node grandParent = new Node(Token.VAR);
    grandParent.setJSDocInfo(new JSDocInfo());
    Node parent = new Node(Token.NAME);
    grandParent.addChildToBack(parent);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToBack(n);

    // Act
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(null, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_parentHasJSDocInfo_returnJSDocInfoInParent() {
    // Arrange
    JSDocInfo expected = new JSDocInfo();
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(expected);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToBack(n);

    // Act
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_grandParentHasJSDocInfo_returnJSDocInfoInGrandParent() {
    // Arrange
    JSDocInfo expected = new JSDocInfo();
    Node grandParent = new Node(Token.VAR);
    grandParent.setJSDocInfo(expected);
    Node parent = new Node(Token.NAME);
    grandParent.addChildToBack(parent);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToBack(n);

    // Act
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    // Assert
    assertEquals(expected, result);
  }
}