
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    n.setParent(parent);

    assertFalse(checker.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionInsideScript_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);

    assertFalse(checker.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionWithName_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);

    assertFalse(checker.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionWithAssign_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);

    assertTrue(checker.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionWithVarAndAssign_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VAR);
    Node grandparent = new Node(Token.ASSIGN);
    parent.setParent(grandparent);
    n.setParent(parent);

    assertTrue(checker.shouldTraverse(t, n, grandparent));
  }

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssign_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);
    Node grandparent = new Node(Token.BLOCK);
    parent.setParent(grandparent);
    t.setStateForNode(parent, new CheckGlobalThis(compiler, level));

    assertTrue(checker.shouldReportThis(n, parent));
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    n.setParent(parent);
    Node grandparent = new Node(Token.BLOCK);
    parent.setParent(grandparent);
    t.setStateForNode(parent, new CheckGlobalThis(compiler, level));

    assertTrue(checker.shouldReportThis(n, parent));
  }

  @Test
  public void testShouldReportThis_ThisNotOnLeftSideOfAssignAndNotPropertyAccess_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.VAR);
    n.setParent(parent);
    Node grandparent = new Node(Token.BLOCK);
    parent.setParent(grandparent);
    t.setStateForNode(parent, new CheckGlobalThis(compiler, level));

    assertFalse(checker.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_ThisWithFunctionOnLeftSideOfAssign_ReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN, thisNode, new Node(Token.STRING));
    t.traverse(t.createEmptyScope(), assignNode);
    DiagnosticType expectedError = new DiagnosticType("JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");

    assertEquals(1, compiler.getErrors().length);
    assertEquals(expectedError, compiler.getErrors()[0].getType());
  }

}
