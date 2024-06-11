import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_typicalUseCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.var(IR.name("x"), IR.number(5));
    Node n2 = IR.exprResult(IR.assign(IR.name("x"), IR.number(10)));
    Node n3 = IR.exprResult(IR.call(IR.name("console.log"), IR.name("x")));
    scriptRoot.addChildToBack(n1);
    scriptRoot.addChildToBack(n2);
    scriptRoot.addChildToBack(n3);

    // Invoke the process method on the CheckSideEffects instance
    cse.process(null, scriptRoot);

    // Verify that no warnings were generated
    assertTrue(cse.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_edgeCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.var(IR.name("x"), IR.number(5));
    Node n2 = IR.exprResult(IR.assign(IR.name("x"), IR.name("x")));
    scriptRoot.addChildToBack(n1);
    scriptRoot.addChildToBack(n2);

    // Invoke the process method on the CheckSideEffects instance
    cse.process(null, scriptRoot);

    // Verify that a warning was generated
    assertFalse(cse.problemNodes.isEmpty());
  }

  @Test
  public void testHotSwapScript_errorScenario() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.var(IR.name("x"), IR.number(5));
    Node n2 = IR.exprResult(IR.assign(IR.name("x"), IR.number(10)));
    Node n3 = IR.exprResult(IR.call(IR.name("console.log"), IR.name("x")));
    scriptRoot.addChildToBack(n1);
    scriptRoot.addChildToBack(n2);
    scriptRoot.addChildToBack(n3);

    // Invoke the hotSwapScript method on the CheckSideEffects instance
    cse.hotSwapScript(scriptRoot, null);

    // Verify that no warnings were generated
    assertTrue(cse.problemNodes.isEmpty());
  }

  @Test
  public void testVisit_edgeCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.var(IR.name("x"), IR.number(5));
    Node n2 = IR.exprResult(IR.assign(IR.name("x"), IR.name("x")));
    scriptRoot.addChildToBack(n1);
    scriptRoot.addChildToBack(n2);

    // Invoke the visit method on the CheckSideEffects instance
    NodeTraversal t = new NodeTraversal(compiler, cse);
    t.traverse(scriptRoot);

    // Verify that a warning was generated
    assertFalse(cse.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects_typicalUseCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.var(IR.name("x"), IR.number(5));
    Node n2 = IR.exprResult(IR.assign(IR.name("x"), IR.number(10)));

    // Add the sample nodes to the problemNodes list
    cse.problemNodes.add(n1);
    cse.problemNodes.add(n2);

    // Invoke the protectSideEffects method on the CheckSideEffects instance
    cse.protectSideEffects();

    // Verify that the problem nodes were successfully protected
    assertEquals(PROTECTOR_FN, n1.getFirstChild().getString());
    assertEquals(n1, n1.getNext());
    assertEquals(PROTECTOR_FN, n2.getFirstChild().getString());
    assertEquals(n2, n2.getNext());
  }

  @Test
  public void testAddExtern_typicalUseCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new CheckSideEffects instance with a specific CheckLevel and protectSideEffectFreeCode
    CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.ERROR, true);

    // Invoke the addExtern method on the CheckSideEffects instance
    cse.addExtern();

    // Verify that the extern function was added to the synthesized externs input
    CompilerInput input = compiler.getSynthesizedExternsInput();
    Node externsRoot = input.getAstRoot(compiler);

    // The extern function should be the last child of the externs root
    Node externFunction = externsRoot.getLastChild();
    assertNotNull(externFunction);
    assertEquals(PROTECTOR_FN, externFunction.getFirstChild().getString());
  }

  @Test
  public void testStripProtection_typicalUseCase() {
    // Create a new compiler
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a new StripProtection instance
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);

    // Create a test script
    Node scriptRoot = IR.script();

    // Add some sample code to the script
    Node n1 = IR.exprResult(IR.call(IR.name(PROTECTOR_FN)));
    scriptRoot.addChildToBack(n1);

    // Invoke the process method on the StripProtection instance
    sp.process(null, scriptRoot);

    // Verify that the protection call was removed
    assertFalse(scriptRoot.hasChildren());
  }

}