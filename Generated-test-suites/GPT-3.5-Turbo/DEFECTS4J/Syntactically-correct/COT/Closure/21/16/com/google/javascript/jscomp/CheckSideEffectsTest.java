import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckForSideEffects() {
    CheckSideEffects checker = new CheckSideEffects();
    
    // Test case 1: Side-effect free code
    String code1 = "var x = 10;";
    checker.checkForSideEffects(code1);
    // Expected result: No warning generated
    
    // Test case 2: Code with missing '+'
    String code2 = "var str = 'Hello'"
                  + " 'World';";
    checker.checkForSideEffects(code2);
    // Expected result: Generates warning for missing '+'
    
    // Test case 3: Code with unused operator
    String code3 = "var a = 10 + 5;";
    checker.checkForSideEffects(code3);
    // Expected result: Generates warning for unused operator
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checker = new CheckSideEffects();
    
    // Test case 1: Side-effect free code
    String code1 = "var x = 10;";
    checker.protectSideEffects(code1);
    // Expected result: Code remains unchanged
    
    // Test case 2: Side-effect free code with protection
    String code2 = "var x = 10;";
    checker.protectSideEffects(code2);
    // Expected result: Code converted to protected format
    
    // Test case 3: Code with side effects
    String code3 = "console.log('Hello World');";
    checker.protectSideEffects(code3);
    // Expected result: Code remains unchanged
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checker = new CheckSideEffects();
    
    // Test case 1: Code with protected side-effect free code
    String code1 = "JSCOMPILER_PRESERVE();";
    checker.stripProtection(code1);
    // Expected result: Code converted to unprotected format
    
    // Test case 2: Code without protected side-effect free code
    String code2 = "console.log('Hello World');";
    checker.stripProtection(code2);
    // Expected result: Code remains unchanged
  }
}