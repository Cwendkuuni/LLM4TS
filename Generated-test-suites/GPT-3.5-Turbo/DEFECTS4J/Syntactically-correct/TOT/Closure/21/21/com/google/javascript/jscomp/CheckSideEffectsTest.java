import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
      
        // Create test input
      
        // Test case 1: Code with a missing '+'
        String testCode1 = "var s = \"this string is \"\n" +
                "        \"continued on the next line but you forgot the +\";";
        Node testNode1 = NodeUtil.parseSourceCode(testCode1);
        root.addChildToBack(testNode1);
      
        // Test case 2: Code with a missing '='
        String testCode2 = "x == foo();";
        Node testNode2 = NodeUtil.parseSourceCode(testCode2);
        root.addChildToBack(testNode2);
      
        // Test case 3: Stray semicolon
        String testCode3 = "foo();;";
        Node testNode3 = NodeUtil.parseSourceCode(testCode3);
        root.addChildToBack(testNode3);
      
        // Create CheckSideEffects instance for testing
        CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
      
        // Set up the expected results
        DiagnosticType expectedType1 = CheckSideEffects.USELESS_CODE_ERROR;
        DiagnosticType expectedType2 = CheckSideEffects.USELESS_CODE_ERROR;
        DiagnosticType expectedType3 = CheckSideEffects.USELESS_CODE_ERROR;
        String expectedMessage1 = "Is there a missing '+' on the previous line?";
        String expectedMessage2 = "This code lacks side-effects. Is there a bug?";
        String expectedMessage3 = "The result of the '==' operator is not being used.";
      
        // Perform the test
        cse.process(externs, root);
      
        // Check the results
        assertEquals(1, compiler.getErrorCount());
        assertEquals(0, compiler.getWarningCount());
        assertEquals(3, compiler.getTypedPercent());
      
        JSError error1 = compiler.getErrors()[0];
        assertEquals(expectedType1, error1.getType());
        assertEquals(expectedMessage1, error1.getDescription());
        assertEquals(testNode1, error1.getNode());
      
        JSError error2 = error1.getCauses()[0];
        assertEquals(expectedType2, error2.getType());
        assertEquals(expectedMessage2, error2.getDescription());
        assertEquals(testNode2, error2.getNode());
      
        JSError error3 = error2.getCauses()[0];
        assertEquals(expectedType3, error3.getType());
        assertEquals(expectedMessage3, error3.getDescription());
        assertEquals(testNode3, error3.getNode());
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new Compiler();
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.ORIGINAL);
      
        // Create test input
      
        // Test case 1: Code with a missing '+'
        String testCode1 = "var s = \"this string is \"\n" +
                "        \"continued on the next line but you forgot the +\";";
        Node testNode1 = NodeUtil.parseSourceCode(testCode1);
        scriptRoot.addChildToBack(testNode1);
      
        // Test case 2: Code with a missing '='
        String testCode2 = "x == foo();";
        Node testNode2 = NodeUtil.parseSourceCode(testCode2);
        scriptRoot.addChildToBack(testNode2);
      
        // Test case 3: Stray semicolon
        String testCode3 = "foo();;";
        Node testNode3 = NodeUtil.parseSourceCode(testCode3);
        scriptRoot.addChildToBack(testNode3);
      
        // Create CheckSideEffects instance for testing
        CheckSideEffects cse = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
      
        // Set up the expected results
        DiagnosticType expectedType1 = CheckSideEffects.USELESS_CODE_ERROR;
        DiagnosticType expectedType2 = CheckSideEffects.USELESS_CODE_ERROR;
        DiagnosticType expectedType3 = CheckSideEffects.USELESS_CODE_ERROR;
        String expectedMessage1 = "Is there a missing '+' on the previous line?";
        String expectedMessage2 = "This code lacks side-effects. Is there a bug?";
        String expectedMessage3 = "The result of the '==' operator is not being used.";
      
        // Perform the test
        cse.hotSwapScript(scriptRoot, originalRoot);
      
        // Check the results
        assertEquals(1, compiler.getErrorCount());
        assertEquals(0, compiler.getWarningCount());
        assertEquals(3, compiler.getTypedPercent());
      
        JSError error1 = compiler.getErrors()[0];
        assertEquals(expectedType1, error1.getType());
        assertEquals(expectedMessage1, error1.getDescription());
        assertEquals(testNode1, error1.getNode());
      
        JSError error2 = error1.getCauses()[0];
        assertEquals(expectedType2, error2.getType());
        assertEquals(expectedMessage2, error2.getDescription());
        assertEquals(testNode2, error2.getNode());
      
        JSError error3 = error2.getCauses()[0];
        assertEquals(expectedType3, error3.getType());
        assertEquals(expectedMessage3, error3.getDescription());
        assertEquals(testNode3, error3.getNode());
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new Compiler();
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
      
        // Create test input
        String testCode = "JSCOMPILER_PRESERVE();";
        Node testNode = NodeUtil.parseSourceCode(testCode);
        root.addChildToBack(testNode);
      
        // Create StripProtection instance for testing
        CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);
      
        // Perform the test
        sp.process(externs, root);
      
        // Check the results
        assertEquals(0, compiler.getErrorCount());
        assertEquals(0, compiler.getWarningCount());
        assertEquals(100, compiler.getTypedPercent());
      
        assertEquals(0, root.getChildCount());
    }
}