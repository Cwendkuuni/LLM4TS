import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        // Typical Use Case
        DetailAST aMods1 = new DetailAST();
        aMods1.addChild(new DetailAST());
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods1));
        
        // Edge Case: No modifiers
        DetailAST aMods2 = new DetailAST();
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods2));
        
        // Error Scenario: Invalid modifier
        DetailAST aMods3 = new DetailAST();
        aMods3.addChild(new DetailAST());
        aMods3.getFirstChild().setText("invalid");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods3));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Typical Use Case
        DetailAST node1 = new DetailAST();
        node1.setType(14);
        Scope expectedScope1 = Scope.PACKAGE;
        assertEquals(expectedScope1, ScopeUtils.getSurroundingScope(node1));
        
        // Edge Case: No surrounding scope
        DetailAST node2 = new DetailAST();
        Scope expectedScope2 = null;
        assertEquals(expectedScope2, ScopeUtils.getSurroundingScope(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        Scope expectedScope3 = null;
        assertEquals(expectedScope3, ScopeUtils.getSurroundingScope(node3));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical Use Case
        DetailAST node1 = new DetailAST();
        node1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node1));
        
        // Edge Case: Not in interface block
        DetailAST node2 = new DetailAST();
        node2.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        assertFalse(ScopeUtils.isInInterfaceBlock(node3));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Typical Use Case
        DetailAST node1 = new DetailAST();
        node1.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node1));
        
        // Edge Case: Not in annotation block
        DetailAST node2 = new DetailAST();
        node2.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        assertFalse(ScopeUtils.isInAnnotationBlock(node3));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical Use Case: In interface block
        DetailAST node1 = new DetailAST();
        node1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node1));
        
        // Typical Use Case: In annotation block
        DetailAST node2 = new DetailAST();
        node2.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node2));
        
        // Typical Use Case: Not in interface or annotation block
        DetailAST node3 = new DetailAST();
        node3.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node3));
        
        // Error Scenario: Invalid token type
        DetailAST node4 = new DetailAST();
        node4.setType(100);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node4));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Typical Use Case
        DetailAST node1 = new DetailAST();
        node1.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(node1));
        
        // Edge Case: Not in enum block
        DetailAST node2 = new DetailAST();
        node2.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        assertFalse(ScopeUtils.isInEnumBlock(node3));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Typical Use Case: In code block
        DetailAST node1 = new DetailAST();
        node1.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(node1));
        
        // Edge Case: Not in code block
        DetailAST node2 = new DetailAST();
        node2.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        assertFalse(ScopeUtils.isInCodeBlock(node3));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Typical Use Case: Not outermost type
        DetailAST node1 = new DetailAST();
        DetailAST parent1 = new DetailAST();
        parent1.setType(14);
        node1.setParent(parent1);
        assertFalse(ScopeUtils.isOuterMostType(node1));
        
        // Edge Case: Outermost type
        DetailAST node2 = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        parent3.setType(100);
        node3.setParent(parent3);
        assertTrue(ScopeUtils.isOuterMostType(node3));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical Use Case: Local variable definition
        DetailAST node1 = new DetailAST();
        node1.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        node1.setParent(parent1);
        assertTrue(ScopeUtils.isLocalVariableDef(node1));
        
        // Edge Case: Not a local variable definition
        DetailAST node2 = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(node2));
        
        // Error Scenario: Invalid token type
        DetailAST node3 = new DetailAST();
        node3.setType(100);
        assertFalse(ScopeUtils.isLocalVariableDef(node3));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Typical Use Case: Class field definition
        DetailAST node1 = new DetailAST();
        node1.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        node1.setParent(parent1);
        assertTrue(ScopeUtils.isClassFieldDef(node1));
        
        // Edge Case: Not a class field definition
        DetailAST node2 = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(node2));
    }
    
    @Test
    public void testIsInScope() {
        // Typical Use Case: Scope matches
        DetailAST ast1 = new DetailAST();
        assertTrue(ScopeUtils.isInScope(ast1, Scope.PACKAGE));
        
        // Typical Use Case: Scope doesn't match
        DetailAST ast2 = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast2, Scope.PRIVATE));
        
        // Error Scenario: Invalid token type
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast3, Scope.PROTECTED));
    }
}