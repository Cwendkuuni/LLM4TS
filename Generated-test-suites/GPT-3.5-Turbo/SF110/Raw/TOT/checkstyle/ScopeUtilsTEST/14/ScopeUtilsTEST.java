import org.junit.*;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.addChild(Token.newToken("public"));
        Scope result = ScopeUtils.getScopeFromMods(mockAST);
        assertEquals(Scope.PUBLIC, result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.addChild(Token.newToken("protected"));
        mockAST.addChild(Token.newToken("private"));
        result = ScopeUtils.getScopeFromMods(mockAST);
        assertEquals(Scope.PROTECTED, result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.getScopeFromMods(mockAST);
        assertNull(result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.addChild(Token.newToken("public"));
        mockAST.addChild(Token.newToken("protected"));
        mockAST.setType(14);
        Scope result = ScopeUtils.getSurroundingScope(mockAST);
        assertEquals(Scope.PUBLIC, result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(136);
        result = ScopeUtils.getSurroundingScope(mockAST);
        assertEquals(Scope.ANONINNER, result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.getSurroundingScope(mockAST);
        assertNull(result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(15);
        boolean result = ScopeUtils.isInInterfaceBlock(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(157);
        result = ScopeUtils.isInInterfaceBlock(mockAST);
        assertFalse(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isInInterfaceBlock(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(157);
        boolean result = ScopeUtils.isInAnnotationBlock(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(15);
        result = ScopeUtils.isInAnnotationBlock(mockAST);
        assertFalse(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isInAnnotationBlock(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(15);
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(157);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(mockAST);
        assertTrue(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(154);
        boolean result = ScopeUtils.isInEnumBlock(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(136);
        result = ScopeUtils.isInEnumBlock(mockAST);
        assertFalse(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isInEnumBlock(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(9);
        boolean result = ScopeUtils.isInCodeBlock(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(14);
        result = ScopeUtils.isInCodeBlock(mockAST);
        assertFalse(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isInCodeBlock(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(14);
        boolean result = ScopeUtils.isOuterMostType(mockAST);
        assertFalse(result);
        
        // Test edge case
        mockAST = new DetailAST();
        result = ScopeUtils.isOuterMostType(mockAST);
        assertTrue(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isOuterMostType(mockAST);
        assertTrue(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(10);
        DetailAST parentMockAST = new DetailAST();
        parentMockAST.setType(7);
        mockAST.setParent(parentMockAST);
        boolean result = ScopeUtils.isLocalVariableDef(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(21);
        parentMockAST = new DetailAST();
        parentMockAST.setType(96);
        mockAST.setParent(parentMockAST);
        result = ScopeUtils.isLocalVariableDef(mockAST);
        assertTrue(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isLocalVariableDef(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(10);
        boolean result = ScopeUtils.isClassFieldDef(mockAST);
        assertTrue(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(10);
        DetailAST parentMockAST = new DetailAST();
        parentMockAST.setType(7);
        mockAST.setParent(parentMockAST);
        result = ScopeUtils.isClassFieldDef(mockAST);
        assertFalse(result);
        
        // Test error scenario
        mockAST = null;
        result = ScopeUtils.isClassFieldDef(mockAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        // Test typical use case
        DetailAST mockAST = new DetailAST();
        mockAST.setType(14);
        Scope scope = Scope.PUBLIC;
        boolean result = ScopeUtils.isInScope(mockAST, scope);
        assertFalse(result);
        
        // Test edge case
        mockAST = new DetailAST();
        mockAST.setType(14);
        scope = Scope.PACKAGE;
        result = ScopeUtils.isInScope(mockAST, scope);
        assertTrue(result);
        
        // Test error scenario
        mockAST = null;
        scope = Scope.PACKAGE;
        result = ScopeUtils.isInScope(mockAST, scope);
        assertFalse(result);
    }
}