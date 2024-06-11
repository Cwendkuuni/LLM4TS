import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollapseVariableDeclarationsTest {
    
    private Compiler compiler;
    private CollapseVariableDeclarations collapseVariableDeclarations;
    
    @Before
    public void setUp() {
        compiler = new Compiler();
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }
    
    @Test
    public void testCollapse_VariableDeclarations_CorrectlyCollapses() {
        String input = "var a; var b = 1; var c = 2;";
        String expectedOutput = "var a, b = 1, c = 2;";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCollapse_VariableDeclarations_WithFunction_CorrectlyCollapses() {
        String input = "function test() { var a; var b = 1; var c = 2; }";
        String expectedOutput = "function test() { var a, b = 1, c = 2; }";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCollapse_VariableDeclarations_MultipleVarDeclarations_CorrectlyCollapses() {
        String input = "var a; var b = 1; var c = 2; var d; var e = 3;";
        String expectedOutput = "var a, b = 1, c = 2, d, e = 3;";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCollapse_VariableAssignments_CorrectlyCollapses() {
        String input = "a = true; b = true; var c = true;";
        String expectedOutput = "var c = b = a = true;";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCollapse_VariableAssignments_MultipleAssignments_CorrectlyCollapses() {
        String input = "a = true; b = true; var c = true; d = false;";
        String expectedOutput = "var c = b = a = true, d = false;";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCollapse_VariableAssignments_WithFunction_CorrectlyCollapses() {
        String input = "function test() { a = true; b = true; var c = true; }";
        String expectedOutput = "function test() { var c = b = a = true; }";
        
        Node root = compiler.parseTestCode(input);
        collapseVariableDeclarations.process(null, root);
        String actualOutput = compiler.toSource(root);
        
        assertEquals(expectedOutput, actualOutput);
    }
}