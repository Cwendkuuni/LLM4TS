
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        // Typical Use Case
        DetailAST aMods1 = createDetailAST("private");
        Scope expected1 = Scope.PRIVATE;
        assertEquals(expected1, ScopeUtils.getScopeFromMods(aMods1));
        
        // Edge Cases
        // No mods provided
        DetailAST aMods2 = createDetailAST(null);
        Scope expected2 = Scope.PACKAGE;
        assertEquals(expected2, ScopeUtils.getScopeFromMods(aMods2));
        
        // Invalid mod value
        DetailAST aMods3 = createDetailAST("invalid");
        Scope expected3 = Scope.PACKAGE;
        assertEquals(expected3, ScopeUtils.getScopeFromMods(aMods3));
        
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("PUBLIC");
        Scope expected1 = Scope.PUBLIC;
        assertEquals(expected1, ScopeUtils.getSurroundingScope(node1));
        
        // Edge Cases
        // No parent node available
        DetailAST node2 = null;
        assertNull(ScopeUtils.getSurroundingScope(node2));
        
        // Node type is not assignable to any particular scope
        DetailAST node3 = createDetailAST("INVALID");
        assertNull(ScopeUtils.getSurroundingScope(node3));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("INTERFACE");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInInterfaceBlock(node1));
        
        // Edge Cases
        // Node is not in an interface block
        DetailAST node2 = createDetailAST("CLASS");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInInterfaceBlock(node2));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("ANNOTATION");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInAnnotationBlock(node1));
        
        // Edge Cases
        // Node is not in an annotation block
        DetailAST node2 = createDetailAST("CLASS");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInAnnotationBlock(node2));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("INTERFACE");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node1));
        
        // Edge Cases
        // Node is not in an interface or annotation block
        DetailAST node2 = createDetailAST("INVALID");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node2));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("ENUM");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInEnumBlock(node1));
        
        // Edge Cases
        // Node is not in an enum block
        DetailAST node2 = createDetailAST("CLASS");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInEnumBlock(node2));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("CODE_BLOCK");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInCodeBlock(node1));
        
        // Edge Cases
        // Node is not in a code block
        DetailAST node2 = createDetailAST("CLASS");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInCodeBlock(node2));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("CLASS");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isOuterMostType(node1));
        
        // Edge Cases
        // Node is not the outermost type
        DetailAST node2 = createDetailAST("INNER_CLASS");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isOuterMostType(node2));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("LOCAL_VARIABLE");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isLocalVariableDef(node1));
        
        // Edge Cases
        // Node is not a local variable
        DetailAST node2 = createDetailAST("GLOBAL_VARIABLE");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isLocalVariableDef(node2));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Typical Use Case
        DetailAST node1 = createDetailAST("CLASS_FIELD");
        boolean expected1 = true;
        assertTrue(ScopeUtils.isClassFieldDef(node1));
        
        // Edge Cases
        // Node is not a class field
        DetailAST node2 = createDetailAST("LOCAL_VARIABLE");
        boolean expected2 = false;
        assertFalse(ScopeUtils.isClassFieldDef(node2));
    }
    
    @Test
    public void testIsInScope() {
        // Typical Use Case
        DetailAST ast1 = createDetailAST("VARIABLE_AST");
        Scope scope1 = Scope.PUBLIC;
        boolean expected1 = true;
        assertTrue(ScopeUtils.isInScope(ast1, scope1));
        
        // Edge Cases
        // Node is not in the specified scope
        DetailAST ast2 = createDetailAST("VARIABLE_AST");
        Scope scope2 = Scope.PRIVATE;
        boolean expected2 = false;
        assertFalse(ScopeUtils.isInScope(ast2, scope2));
        
    }
    
    private DetailAST createDetailAST(String name) {
        DetailAST mockAST = new DetailAST();
        mockAST.setText(name);
        return mockAST;
    }
}
