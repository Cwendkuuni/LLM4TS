import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checker;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    compiler.setPassConfig(new PassConfig.Builder().addPass(new HotSwapCompilerPassFactory("checkSideEffects", checker)).build());
  }

  @Test
  public void testProcess_NoSideEffect_Success() {
    Node externs = IR.root();
    Node root = IR.root();

    Node name = IR.name("x").srcref(1);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    Node assign = IR.assign(name, IR.number(5)).srcref(1);

    root.addChildToBack(externs);
    root.addChildToBack(assign);

    Node externsCopy = externs.cloneNode();
    Node rootCopy = root.cloneNode();

    // Run the process method
    checker.process(externs, root);

    // Ensure no diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the AST remained unchanged
    assertEquals(externsCopy.toStringTree(), externs.toStringTree());
    assertEquals(rootCopy.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_SideEffect_Error() {
    Node externs = IR.root();
    Node root = IR.root();

    Node expr = IR.exprResult(IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1)).srcref(1);

    root.addChildToBack(externs);
    root.addChildToBack(expr);

    Node externsCopy = externs.cloneNode();
    Node rootCopy = root.cloneNode();

    // Run the process method
    checker.process(externs, root);

    // Ensure a diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 1);
    assertTrue(compiler.getWarnings().get(0).getType().equals(CheckSideEffects.USELESS_CODE_ERROR));

    // Ensure the AST remained unchanged
    assertEquals(externsCopy.toStringTree(), externs.toStringTree());
    assertEquals(rootCopy.toStringTree(), root.toStringTree());
  }

  @Test
  public void testHotSwapScript_NoSideEffect_Success() {
    Node scriptRoot = IR.root();
    Node originalRoot = IR.root();

    Node name = IR.name("x").srcref(1);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    Node assign = IR.assign(name, IR.number(5)).srcref(1);

    scriptRoot.addChildToBack(assign);

    Node scriptRootCopy = scriptRoot.cloneNode();
    Node originalRootCopy = originalRoot.cloneNode();

    // Run the hotSwapScript method
    checker.hotSwapScript(scriptRoot, originalRoot);

    // Ensure no diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the AST remained unchanged
    assertEquals(scriptRootCopy.toStringTree(), scriptRoot.toStringTree());
    assertEquals(originalRootCopy.toStringTree(), originalRoot.toStringTree());
  }

  @Test
  public void testHotSwapScript_SideEffect_Error() {
    Node scriptRoot = IR.root();
    Node originalRoot = IR.root();

    Node expr = IR.exprResult(IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1)).srcref(1);

    scriptRoot.addChildToBack(expr);

    Node scriptRootCopy = scriptRoot.cloneNode();
    Node originalRootCopy = originalRoot.cloneNode();

    // Run the hotSwapScript method
    checker.hotSwapScript(scriptRoot, originalRoot);

    // Ensure a diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 1);
    assertTrue(compiler.getWarnings().get(0).getType().equals(CheckSideEffects.USELESS_CODE_ERROR));

    // Ensure the AST remained unchanged
    assertEquals(scriptRootCopy.toStringTree(), scriptRoot.toStringTree());
    assertEquals(originalRootCopy.toStringTree(), originalRoot.toStringTree());
  }

  @Test
  public void testVisit_NoSideEffect_Success() {
    NodeTraversal t = new NodeTraversal(compiler, new RecordingErrorManager());

    Node expr = IR.exprResult(IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1)).srcref(1);
    Node parent = IR.block(expr).srcref(1);

    Node exprCopy = expr.cloneNode();
    Node parentCopy = parent.cloneNode();

    // Run the visit method
    checker.visit(t, expr, parent);

    // Ensure no diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the AST remained unchanged
    assertEquals(exprCopy.toStringTree(), expr.toStringTree());
    assertEquals(parentCopy.toStringTree(), parent.toStringTree());
  }

  @Test
  public void testVisit_SideEffect_Error() {
    NodeTraversal t = new NodeTraversal(compiler, new RecordingErrorManager());

    Node expr = IR.exprResult(IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1)).srcref(1);
    Node parent = IR.statement(expr).srcref(1);

    Node exprCopy = expr.cloneNode();
    Node parentCopy = parent.cloneNode();

    // Run the visit method
    checker.visit(t, expr, parent);

    // Ensure a diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 1);
    assertTrue(compiler.getWarnings().get(0).getType().equals(CheckSideEffects.USELESS_CODE_ERROR));

    // Ensure the AST remained unchanged
    assertEquals(exprCopy.toStringTree(), expr.toStringTree());
    assertEquals(parentCopy.toStringTree(), parent.toStringTree());
  }

  @Test
  public void testProtectSideEffects_NoSideEffect_Success() {
    Node expr = IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1);
    Node exprCopy = expr.cloneNode();
    Node block = IR.block(expr).srcref(1);
    Node blockCopy = block.cloneNode();
    Node root = IR.root();
    root.addChildToBack(block);

    // Add a diagnostic warning to trigger the execution of the protectSideEffects method
    Diagnostic warning = CheckLevel.WARNING.diagnostic(CheckSideEffects.USELESS_CODE_ERROR, expr);
    compiler.getWarnings().add(warning);

    // Mock the protectSideEffects method to ensure it is called
    boolean[] protectSideEffectsCalled = {false};
    CheckSideEffects checkerMock = new CheckSideEffects(compiler, CheckLevel.WARNING, true) {
      @Override
      private void protectSideEffects() {
        protectSideEffectsCalled[0] = true;
      }
    };

    // Manually invoke the process method
    checkerMock.process(IR.root(), root);

    // Ensure no diagnostic warnings left in the compiler
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the protectSideEffects method was called
    assertTrue(protectSideEffectsCalled[0]);

    // Ensure the AST remained unchanged
    assertEquals(exprCopy.toStringTree(), expr.toStringTree());
    assertEquals(blockCopy.toStringTree(), block.toStringTree());
  }

  @Test
  public void testProtectSideEffects_SideEffect_Success() {
    Node expr = IR.exprResult(IR.assign(IR.name("x").srcref(1), IR.number(5)).srcref(1)).srcref(1);
    Node exprCopy = expr.cloneNode();
    Node block = IR.statement(expr).srcref(1);
    Node blockCopy = block.cloneNode();
    Node root = IR.root();
    root.addChildToBack(block);

    // Add a diagnostic warning to trigger the execution of the protectSideEffects method
    Diagnostic warning = CheckLevel.WARNING.diagnostic(CheckSideEffects.USELESS_CODE_ERROR, expr);
    compiler.getWarnings().add(warning);

    // Mock the protectSideEffects method to ensure it is not called
    boolean[] protectSideEffectsCalled = {false};
    CheckSideEffects checkerMock = new CheckSideEffects(compiler, CheckLevel.WARNING, false) {
      @Override
      private void protectSideEffects() {
        protectSideEffectsCalled[0] = true;
      }
    };

    // Manually invoke the process method
    checkerMock.process(IR.root(), root);

    // Ensure no diagnostic warnings left in the compiler
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the protectSideEffects method was not called
    assertFalse(protectSideEffectsCalled[0]);

    // Ensure the AST remained unchanged
    assertEquals(exprCopy.toStringTree(), expr.toStringTree());
    assertEquals(blockCopy.toStringTree(), block.toStringTree());
  }

  @Test
  public void testStripProtection_NoProtection_Success() {
    Node externs = IR.root();
    Node root = IR.root();

    Node script = IR.script();
    Node name = IR.name("alert").srcref(1);
    Node var = IR.var(name).srcref(1);
    Node call = IR.call(name).srcref(1);

    root.addChildToBack(externs);
    root.addChildToBack(script);
    script.addChildToBack(var);
    script.addChildToBack(call);

    Node externsCopy = externs.cloneNode();
    Node rootCopy = root.cloneNode();
    
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

    // Run the process method
    stripper.process(externs, root);

    // Ensure no diagnostic warning was reported
    assertTrue(compiler.getWarnings().getWarningCount() == 0);

    // Ensure the AST remained unchanged
    assertEquals(externsCopy.toStringTree(), externs.toStringTree());
    assertEquals(rootCopy.toStringTree(), root.toStringTree());
  }
}