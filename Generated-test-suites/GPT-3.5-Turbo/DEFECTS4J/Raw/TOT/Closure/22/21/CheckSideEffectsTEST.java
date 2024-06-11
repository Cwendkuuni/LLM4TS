import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerInput;

public class CheckSideEffectsTest {

  // Test Data
  private Node externs;
  private Node root;
  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    // Create test data
    externs = new Node();
    root = new Node();
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @Test
  public void testProcess_NoCode_ReturnsNoErrors() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    // Execute
    checkSideEffects.process(externs, root);

    // Verify
    verify(compiler, never()).report(any());
    assertNull(root.getFirstChild());
  }

  @Test
  public void testProcess_SimpleCode_ReturnsNoErrors() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    Node code = new Node();
    root.addChildToBack(code);

    // Execute
    checkSideEffects.process(externs, root);

    // Verify
    verify(compiler, never()).report(any());
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testProcess_CodeWithSideEffect_ReturnsWarning() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    Node code = new Node();
    root.addChildToBack(code);

    // Add a node with side effects
    Node nodeWithSideEffect = new Node();
    code.addChildToBack(nodeWithSideEffect);
    nodeWithSideEffect.setSideEffectFlags(Node.SideEffectFlags.CONSTANT_EXPRESSION);

    // Execute
    checkSideEffects.process(externs, root);

    // Verify
    verify(compiler).report(any());
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testHotSwapScript_NoCode_ReturnsNoErrors() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    // Execute
    checkSideEffects.hotSwapScript(root, externs);

    // Verify
    verify(compiler, never()).report(any());
    assertNull(root.getFirstChild());
  }

  @Test
  public void testHotSwapScript_SimpleCode_ReturnsNoErrors() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    Node code = new Node();
    root.addChildToBack(code);

    // Execute
    checkSideEffects.hotSwapScript(root, externs);

    // Verify
    verify(compiler, never()).report(any());
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testHotSwapScript_CodeWithSideEffect_ReturnsWarning() {
    // Setup
    compiler.getSynthesizedExternsInput();
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    Node code = new Node();
    root.addChildToBack(code);

    // Add a node with side effects
    Node nodeWithSideEffect = new Node();
    code.addChildToBack(nodeWithSideEffect);
    nodeWithSideEffect.setSideEffectFlags(Node.SideEffectFlags.CONSTANT_EXPRESSION);

    // Execute
    checkSideEffects.hotSwapScript(root, externs);

    // Verify
    verify(compiler).report(any());
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testVisit_NoNode_ReturnsNoErrors() {
    // Setup
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = null;
    Node parent = null;

    // Execute
    checkSideEffects.visit(t, n, parent);

    // Verify
    verify(t, never()).makeError(any(), any(), any(), anyString());
    verify(compiler, never()).report(any());
  }

  @Test
  public void testVisit_CodeWithSideEffect_ReturnsWarning() {
    // Setup
    NodeTraversal t = mock(NodeTraversal.class);
    Node code = new Node();
    Node n = new Node();
    code.addChildToBack(n);
    Node parent = code;
    when(t.getCompiler()).thenReturn(compiler);

    // Add a node with side effects
    Node nodeWithSideEffect = new Node();
    n.addChildToBack(nodeWithSideEffect);
    nodeWithSideEffect.setSideEffectFlags(Node.SideEffectFlags.CONSTANT_EXPRESSION);

    // Execute
    checkSideEffects.visit(t, n, parent);

    // Verify
    verify(t, never()).makeError(any(), any(), any(), anyString());
    verify(compiler).report(any());
  }

  @Test
  public void testProtectSideEffects_NoProblemNodes_NoChange() {
    // Setup
    Node code = new Node();
    root.addChildToBack(code);

    // Execute
    checkSideEffects.protectSideEffects();

    // Verify
    verify(compiler, never()).reportCodeChange();
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testProtectSideEffects_ProblemNodes_ChangesCode() {
    // Setup
    Node code = new Node();
    root.addChildToBack(code);

    // Add a problem node
    Node problemNode = new Node();
    code.addChildToBack(problemNode);

    // Execute
    checkSideEffects.protectSideEffects();

    // Verify
    verify(compiler).reportCodeChange();
    assertEquals(1, root.getChildCount());
    Node replacement = code.getFirstChild();
    assertNotSame(problemNode, replacement);
    assertTrue(replacement.isCall());
    assertEquals("JSCOMPILER_PRESERVE", replacement.getFirstChild().getString());
  }

  @Test
  public void testAddExtern_CodeWithProblemNodes_AddsProtectionFunction() {
    // Setup
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    // Execute
    checkSideEffects.addExtern();

    // Verify
    verify(compiler).reportCodeChange();
    assertEquals(1, externs.getChildCount());
    Node protectionFunction = externs.getFirstChild();
    assertTrue(protectionFunction.isVar());
    assertEquals("JSCOMPILER_PRESERVE", protectionFunction.getFirstChild().getString());
    assertTrue(protectionFunction.getJSDocInfo().isNoAlias());
  }

  @Test
  public void testAddExtern_CodeWithExistingProtectionFunction_AddsProtectionFunctionOnlyOnce() {
    // Setup
    when(compiler.getSynthesizedExternsInput().getAstRoot(compiler)).thenReturn(externs);

    // Add existing protection function
    Node existingProtectionFunction = IR.var(IR.name("JSCOMPILER_PRESERVE"));
    existingProtectionFunction.getJSDocInfo().recordNoAlias();
    externs.addChildToBack(existingProtectionFunction);

    // Execute
    checkSideEffects.addExtern();

    // Verify
    verify(compiler).reportCodeChange();
    assertEquals(1, externs.getChildCount());
    assertSame(existingProtectionFunction, externs.getFirstChild());
  }

  @Test
  public void testStripProtection_NoProtectionFunction_NoChange() {
    // Setup
    Node code = new Node();
    root.addChildToBack(code);

    // Execute
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(externs, root);

    // Verify
    verify(compiler, never()).reportCodeChange();
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testStripProtection_CodeWithProtectionFunction_ChangesCode() {
    // Setup
    Node code = new Node();
    root.addChildToBack(code);

    // Add a protection function call
    Node protectionFunctionCall = new Node();
    code.addChildToBack(protectionFunctionCall);
    protectionFunctionCall.addChildToBack(IR.name("JSCOMPILER_PRESERVE"));

    // Execute
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(externs, root);

    // Verify
    verify(compiler).reportCodeChange();
    assertEquals(1, root.getChildCount());
    assertSame(protectionFunctionCall.getFirstChild(), root.getFirstChild());
  }
}