import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldTraverse_globalContext_returnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void shouldTraverse_notGlobalContextAndFunctionHasThisType_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void shouldTraverse_notGlobalContextAndFunctionIsConstructor_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void shouldTraverse_notGlobalContextAndFunctionHasOverrideAnnotation_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void shouldTraverse_notGlobalContextAndParentIsNotBlockOrScriptOrNameOrAssign_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node grandParent = new Node();
    grandParent.setType(Token.CONST);
    parent.addChildToBack(grandParent);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void shouldTraverse_parentIsNull_returnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = null;

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void visit_nodeIsThis_shouldReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    boolean expected = true;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }

  @Test
  public void visit_nodeIsAssignLhsChild_shouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = n;
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }

  @Test
  public void visit_parentIsNull_shouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = null;
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void visit_nodeIsNotThis_shouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.GETPROP);
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void visit_nodeIsProtoProperty_shouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "prototype"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void visit_nodeHasQualifiedNameContainingPrototype_shouldNotReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME, "obj.prototype.property"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void visit_nodeIsLhsChildOfAssign_shouldReportThis() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node lhs = new Node(Token.NAME, "a");
    Node parent = new Node(Token.ASSIGN, lhs, n);
    checkGlobalThis.assignLhsChild = lhs;
    boolean expected = true;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
  }

  @Test
  public void visit_nodeIsNotLhsChildOfAssign_shouldNotReportThisAndAssignLhsChildIsNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node lhs = new Node(Token.NAME, "a");
    Node parent = new Node(Token.ASSIGN, lhs, n);
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
    Assert.assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void visit_nodeIsNotThis_shouldNotReportAndAssignLhsChildIsNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.ADD);
    Node parent = new Node(Token.GETPROP);
    boolean expected = false;

    // Act
    checkGlobalThis.visit(t, n, parent);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    // Assert
    Assert.assertEquals(expected, result);
    Assert.assertNull(checkGlobalThis.assignLhsChild);
  }
}