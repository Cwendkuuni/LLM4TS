import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(14, "public"));
        assertEquals(PUBLIC, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetSurroundingScope() {
        // test cases here
    }

    // Repeat above process for the remaining methods

}