import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    nodeParentBlock.addChildToEnd(nodeFunction);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentBlock);

    assertTrue(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentBlock));
  }

  @Test
  public void testShouldNotTraverseFunctionConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(true);
    nodeFunction.setJSDocInfo(jsDoc);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    nodeParentBlock.addChildToEnd(nodeFunction);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentBlock);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentBlock));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisType() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setHasThisType(true);
    nodeFunction.setJSDocInfo(jsDoc);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    nodeParentBlock.addChildToEnd(nodeFunction);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentBlock);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentBlock));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverride() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsOverride(true);
    nodeFunction.setJSDocInfo(jsDoc);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    nodeParentBlock.addChildToEnd(nodeFunction);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentBlock);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentBlock));
  }

  @Test
  public void testShouldNotTraverseFunctionNotInBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node nodeParentScript = new Node(Token.SCRIPT, null);
    nodeParentScript.addChildToEnd(nodeFunction);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentScript);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentScript));
  }

  @Test
  public void testShouldNotTraverseFunctionNotInFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node nodeParentVar = new Node(Token.VAR, null);
    nodeParentVar.addChildToEnd(nodeFunction);
    Node nodeParentBlock = new Node(Token.BLOCK, null);
    nodeParentBlock.addChildToEnd(nodeParentVar);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeFunction);
    when(nodeTraversal.getParentNode()).thenReturn(nodeParentBlock);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeFunction, nodeParentBlock));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeAssign = new Node(Token.ASSIGN, null);
    Node nodeLhs = new Node(Token.THIS, null);
    nodeAssign.addChildToEnd(nodeLhs);
    Node nodeRhs = new Node(Token.NUMBER, null);
    nodeAssign.addChildToEnd(nodeRhs);
    nodeParentBlock.addChildToEnd(nodeAssign);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeLhs);
    when(nodeTraversal.getParentNode()).thenReturn(nodeAssign);

    assertTrue(checker.shouldTraverse(nodeTraversal, nodeLhs, nodeAssign));
  }

  @Test
  public void testShouldTraverseRightSideOfAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeAssign = new Node(Token.ASSIGN, null);
    Node nodeLhs = new Node(Token.GETPROP, null, null);
    nodeLhs.getLastChild().setString("prototype");
    Node nodeRhs = new Node(Token.THIS, null);
    nodeAssign.addChildToEnd(nodeLhs);
    nodeAssign.addChildToEnd(nodeRhs);
    nodeParentBlock.addChildToEnd(nodeAssign);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeRhs);
    when(nodeTraversal.getParentNode()).thenReturn(nodeAssign);

    assertTrue(checker.shouldTraverse(nodeTraversal, nodeRhs, nodeAssign));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeAssign = new Node(Token.ASSIGN, null);
    Node nodeLhs = new Node(Token.GETPROP, null, null);
    Node nodeProc = new Node(Token.NAME, null);
    nodeProc.setString("prototype");
    Node nodeLhsPrototype = new Node(Token.GETPROP, null, nodeProc);
    nodeLhsPrototype.addChildToEnd(new Node(Token.GETPROP, null, null));
    nodeLhs = nodeLhsPrototype;
    Node nodeRhs = new Node(Token.THIS, null);
    nodeAssign.addChildToEnd(nodeLhs);
    nodeAssign.addChildToEnd(nodeRhs);
    nodeParentBlock.addChildToEnd(nodeAssign);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeRhs);
    when(nodeTraversal.getParentNode()).thenReturn(nodeAssign);

    assertFalse(checker.shouldTraverse(nodeTraversal, nodeRhs, nodeAssign));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToVariable() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeAssign = new Node(Token.ASSIGN, null);
    Node nodeLhs = new Node(Token.VAR, null);
    nodeAssign.addChildToEnd(nodeAssign);
    nodeAssign.addChildToEnd(nodeLhs);
    nodeLhs.addChildToEnd(new Node(Token.THIS, null));
    nodeParentBlock.addChildToEnd(nodeAssign);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeLhs);
    when(nodeTraversal.getParentNode()).thenReturn(nodeAssign);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    when(checker.compiler).thenReturn(compiler);

    checker.visit(nodeTraversal, null, nodeFunction);

    assertTrue(nodeAssign.getChildCount() == 2);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeAssign = new Node(Token.ASSIGN, null);
    Node nodeLhs = new Node(Token.THIS, null);
    nodeAssign.addChildToEnd(nodeLhs);
    nodeParentBlock.addChildToEnd(nodeAssign);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeLhs);
    when(nodeTraversal.getParentNode()).thenReturn(nodeAssign);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    when(checker.compiler).thenReturn(compiler);

    checker.visit(nodeTraversal, null, nodeFunction);

    assertTrue(nodeAssign.getChildCount() == 1);
    assertTrue(compiler.reportWasCalledWith(nodeTraversal, level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object")));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node nodeParentBlock = new Node(Token.BLOCK, null);
    Node nodeGetProp = new Node(Token.GETPROP, null, null);
    Node nodeThis = new Node(Token.THIS, null);
    nodeGetProp.addChildToEnd(nodeThis);
    nodeParentBlock.addChildToEnd(nodeGetProp);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentNode()).thenReturn(nodeThis);
    when(nodeTraversal.getParentNode()).thenReturn(nodeGetProp);

    Node nodeFunction = new Node(Token.FUNCTION, null);
    when(checker.compiler).thenReturn(compiler);

    checker.visit(nodeTraversal, null, nodeFunction);

    assertTrue(nodeGetProp.getChildCount() == 1);
    assertTrue(compiler.reportWasCalledWith(nodeTraversal, level, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object")));
  }
}