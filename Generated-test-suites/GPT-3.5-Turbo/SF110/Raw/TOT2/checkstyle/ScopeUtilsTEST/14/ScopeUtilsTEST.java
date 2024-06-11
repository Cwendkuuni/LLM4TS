import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(null));
    }
    
    @Test
    public void testGetSurroundingScope() {
        assertNull(ScopeUtils.getSurroundingScope(null));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        assertFalse(ScopeUtils.isInInterfaceBlock(null));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        assertFalse(ScopeUtils.isInAnnotationBlock(null));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(null));
    }
    
    @Test
    public void testIsInEnumBlock() {
        assertFalse(ScopeUtils.isInEnumBlock(null));
    }
    
    @Test
    public void testIsInCodeBlock() {
        assertFalse(ScopeUtils.isInCodeBlock(null));
    }
    
    @Test
    public void testIsOuterMostType() {
        assertTrue(ScopeUtils.isOuterMostType(null));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        assertFalse(ScopeUtils.isLocalVariableDef(null));
    }
    
    @Test
    public void testIsClassFieldDef() {
        assertFalse(ScopeUtils.isClassFieldDef(null));
    }
    
    @Test
    public void testIsInScope() {
        assertFalse(ScopeUtils.isInScope(null, Scope.PACKAGE));
    }
}