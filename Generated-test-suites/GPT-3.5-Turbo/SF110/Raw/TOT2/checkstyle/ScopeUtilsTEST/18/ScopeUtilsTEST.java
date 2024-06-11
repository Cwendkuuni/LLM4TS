import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(new DetailAST(15, "public"));
        Scope result = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, result);
    }

    @Test
    public void testGetSurroundingScope() {
        // Test for null node
        Scope result = ScopeUtils.getSurroundingScope(null);
        assertNull(result);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST(15, "interface");
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);
    }

    // Add test methods for the rest of the public methods

}