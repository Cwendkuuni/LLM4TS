import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = IR.parse("function foo() {}");
    checkSideEffects.process(null, root);
    // Basic functionality test for an empty program
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo();");
    checkSideEffects.process(null, root);
    // Test for a method call with no side-effects
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo(); foo();");
    checkSideEffects.process(null, root);
    // Test for multiple statements with no side-effects
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo();;");
    checkSideEffects.process(null, root);
    // Test for a stray semicolon
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1;\nfoo();");
    checkSideEffects.process(null, root);
    // Test for a line break
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo(); var y = 2;");
    checkSideEffects.process(null, root);
    // Test for multiple variable declarations
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo(); var y = 2; x = 3;");
    checkSideEffects.process(null, root);
    // Test for multiple assignments to the same variable
    assertEquals(1, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo(); var y = 2; x = 3; y = 4;");
    checkSideEffects.process(null, root);
    // Test for multiple assignments to different variables
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo(); x += 1;");
    checkSideEffects.process(null, root);
    // Test for a simple operator
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("var x = 1; x = foo();; x += 1;");
    checkSideEffects.process(null, root);
    // Test for a stray semicolon followed by an operator
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("function foo() { return 1; } var x = 1; x = foo(); x += 1;");
    checkSideEffects.process(null, root);
    // Test for a method definition
    assertEquals(0, checkSideEffects.problemNodes.size());

    root = IR.parse("function foo() { return 1; } var x = 1; x = foo(); x += 1; foo();");
    checkSideEffects.process(null, root);
    // Test for a method call followed by a method definition
    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}