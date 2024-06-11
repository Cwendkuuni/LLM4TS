import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);
    }
}


