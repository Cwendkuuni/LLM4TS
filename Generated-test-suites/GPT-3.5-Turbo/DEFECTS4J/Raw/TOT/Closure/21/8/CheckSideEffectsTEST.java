import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckSideEffectsTest {
  
  @Test
  public void testProcess() {
    // typical use case
    // if there are non-side effecting statements, a warning should be generated
    // create a simple JavaScript file with non-side effecting statements
    String code = "var x = 5; var y = 10;";
    // compile the code and check for warnings
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(null, code);
    // make sure a warning is generated
    assertEquals(1, compiler.getWarnings().size());
  }
  
  @Test
  public void testHotSwapScript() {
    // edge case
    // if there are non-side effecting statements, a warning should be generated
    // create a simple JavaScript file with non-side effecting statements
    String code = "var x = 5; var y = 10;";
    // compile the code and check for warnings
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.hotSwapScript(null, code);
    // make sure a warning is generated
    assertEquals(1, compiler.getWarnings().size());
  }
  
  @Test
  public void testVisit() {
    // error scenario
    // if the code lacks side-effects, a warning should be generated
    // create a simple JavaScript statement with no side-effects
    String code = "var x = 5;";
    // compile the code and check for warnings
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.hotSwapScript(null, code);
    // make sure a warning is generated
    assertEquals(1, compiler.getWarnings().size());
  }
  
  @Test
  public void testProtectSideEffects() {
    // typical use case
    // if there are side-effect free statements, they should be protected by a function call
    // create a simple JavaScript file with a side-effect free statement
    String code = "var x = 5; var y = 10;";
    // compile the code and check if the side-effect free statement is protected
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(null, code);
    // make sure the side-effect free statement is protected
    assertEquals("JSCOMPILER_PRESERVE(x = 5)", compiler.toSource());
  }
  
  @Test
  public void testStripProtection() {
    // edge case
    // if there are side-effect sync functions, they should be removed
    // create a JavaScript file with a side-effect sync function
    String code = "JSCOMPILER_PRESERVE(x = 5);";
    // compile the code and check if the side-effect sync function is removed
    Compiler compiler = new Compiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(null, code);
    // make sure the side-effect sync function is removed
    assertEquals("x = 5;", compiler.toSource());
  }
}