import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {
    
    @Test
    public void testProcess() {
        // Test 1: No local variable assignments to remove
        String code1 = "function foo() {\n" +
                       "    var x = 5;\n" +
                       "    var y = 10;\n" +
                       "    var z = x + y;\n" +
                       "}\n" +
                       "foo();";
        // Test 2: Global scope
        String code2 = "var a = 1;\n" +
                       "function foo() {\n" +
                       "    a = 2;\n" +
                       "}\n" +
                       "foo();";
        // Test 3: Multiple assignments to the same variable
        String code3 = "function foo() {\n" +
                       "    var x = 1;\n" +
                       "    x = 2;\n" +
                       "    x = 3;\n" +
                       "}\n" +
                       "foo();";
        
        //Create a Compiler
        
        // Create the DeadAssignmentsElimination object
        DeadAssignmentsElimination dae = new DeadAssignmentsElimination(compiler);
        
        // Run the process method on the input codes
        // and check if the assignments are removed correctly
        dae.process(code1);
        assertEquals("function foo() {\n" +
                     "    var x = 5;\n" +
                     "    var y = 10;\n" +
                     "    var z = x + y;\n" +
                     "}\n" +
                     "foo();", code1);
        
        dae.process(code2);
        assertEquals("var a = 1;\n" +
                     "function foo() {\n" +
                     "    a = 2;\n" +
                     "}\n" +
                     "foo();", code2);
        
        dae.process(code3);
        assertEquals("function foo() {\n" +
                     "    var x = 1;\n" +
                     "    x = 3;\n" +
                     "}\n" +
                     "foo();", code3);
    }
}