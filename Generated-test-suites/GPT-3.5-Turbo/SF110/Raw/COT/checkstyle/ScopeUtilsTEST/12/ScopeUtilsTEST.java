import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test when no modifiers are passed
        DetailAST mods1 = new DetailAST();
        Scope result1 = ScopeUtils.getScopeFromMods(mods1);
        assertEquals(Scope.PACKAGE, result1);
        
        // Test when a single modifier is passed
        DetailAST mods2 = new DetailAST();
        DetailAST modifier2 = new DetailAST();
        modifier2.setText("public");
        mods2.setFirstChild(modifier2);
        Scope result2 = ScopeUtils.getScopeFromMods(mods2);
        assertEquals(Scope.PUBLIC, result2);
        
        // Test when multiple modifiers are passed
        DetailAST mods3 = new DetailAST();
        DetailAST modifier3_1 = new DetailAST();
        DetailAST modifier3_2 = new DetailAST();
        modifier3_1.setText("public");
        modifier3_2.setText("protected");
        mods3.setFirstChild(modifier3_1);
        modifier3_1.setNextSibling(modifier3_2);
        Scope result3 = ScopeUtils.getScopeFromMods(mods3);
        assertEquals(Scope.PUBLIC, result3);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test when node is not within any specific scope
        DetailAST node1 = new DetailAST();
        Scope result1 = ScopeUtils.getSurroundingScope(node1);
        assertNull(result1);
        
        // Test when node is within a class scope
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(14); // class token
        Scope result2 = ScopeUtils.getSurroundingScope(node2);
        assertEquals(Scope.PACKAGE, result2);
        
        // Test when node is within a method scope
        DetailAST node3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        parent3.setType(8); // method token
        Scope result3 = ScopeUtils.getSurroundingScope(node3);
        assertNull(result3);
        
        // Test when node is within an inner class scope
        DetailAST node4 = new DetailAST();
        DetailAST parent4 = new DetailAST();
        parent4.setType(157); // inner class token
        Scope result4 = ScopeUtils.getSurroundingScope(node4);
        assertEquals(Scope.PROTECTED, result4);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Test when node is not in interface block
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isInInterfaceBlock(node1);
        assertFalse(result1);
        
        // Test when node is in interface block
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(15); // interface token
        boolean result2 = ScopeUtils.isInInterfaceBlock(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Test when node is not in annotation block
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isInAnnotationBlock(node1);
        assertFalse(result1);
        
        // Test when node is in annotation block
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(157); // annotation token
        boolean result2 = ScopeUtils.isInAnnotationBlock(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test when node is not in interface or annotation block
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isInInterfaceOrAnnotationBlock(node1);
        assertFalse(result1);
        
        // Test when node is in interface block
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(15); // interface token
        boolean result2 = ScopeUtils.isInInterfaceOrAnnotationBlock(node2);
        assertTrue(result2);
        
        // Test when node is in annotation block
        DetailAST node3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        parent3.setType(157); // annotation token
        boolean result3 = ScopeUtils.isInInterfaceOrAnnotationBlock(node3);
        assertTrue(result3);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Test when node is not in enum block
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isInEnumBlock(node1);
        assertFalse(result1);
        
        // Test when node is in enum block
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(154); // enum token
        boolean result2 = ScopeUtils.isInEnumBlock(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Test when node is not in code block
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isInCodeBlock(node1);
        assertFalse(result1);
        
        // Test when node is in code block
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(9); // class token
        boolean result2 = ScopeUtils.isInCodeBlock(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsOuterMostType() {
        // Test when node is the outermost type
        DetailAST node1 = new DetailAST();
        node1.setType(14); // class token
        boolean result1 = ScopeUtils.isOuterMostType(node1);
        assertTrue(result1);
        
        // Test when node is not the outermost type
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(14); // class token
        node2.setParent(parent2);
        boolean result2 = ScopeUtils.isOuterMostType(node2);
        assertFalse(result2);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Test when node is not a local variable definition
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isLocalVariableDef(node1);
        assertFalse(result1);
        
        // Test when node is a local variable definition
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(7); // variable token
        node2.setParent(parent2);
        boolean result2 = ScopeUtils.isLocalVariableDef(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Test when node is not a class field definition
        DetailAST node1 = new DetailAST();
        boolean result1 = ScopeUtils.isClassFieldDef(node1);
        assertFalse(result1);
        
        // Test when node is a class field definition
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(10); // field token
        node2.setParent(parent2);
        boolean result2 = ScopeUtils.isClassFieldDef(node2);
        assertTrue(result2);
    }
    
    @Test
    public void testIsInScope() {
        // Test when ast is not in the specified scope
        DetailAST ast1 = new DetailAST();
        Scope scope1 = Scope.PACKAGE;
        boolean result1 = ScopeUtils.isInScope(ast1, scope1);
        assertFalse(result1);
        
        // Test when ast is in the specified scope
        DetailAST ast2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(14); // class token
        ast2.setParent(parent2);
        Scope scope2 = Scope.PACKAGE;
        boolean result2 = ScopeUtils.isInScope(ast2, scope2);
        assertTrue(result2);
    }
}