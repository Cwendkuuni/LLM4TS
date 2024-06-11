import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mockAST = new DetailAST();
        mockAST.addChild(new DetailAST());
        mockAST.getFirstChild().setText("public");
        Scope scope = ScopeUtils.getScopeFromMods(mockAST);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetScopeFromModsNoAccessMod() {
        DetailAST mockAST = new DetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(mockAST);
        assertEquals(Scope.PACKAGE, scope);
    }

    // Include test cases for other methods as well

}