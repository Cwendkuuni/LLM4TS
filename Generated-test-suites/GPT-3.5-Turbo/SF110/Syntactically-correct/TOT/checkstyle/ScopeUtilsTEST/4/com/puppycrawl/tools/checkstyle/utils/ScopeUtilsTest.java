import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast1 = new DetailAST();
        ast1.setText("public");

        Scope actual1 = ScopeUtils.getScopeFromMods(ast1);

        assertEquals(Scope.PUBLIC, actual1);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast1 = new DetailAST();
        DetailAST ast2 = new DetailAST();
        DetailAST ast3 = new DetailAST();

        ast1.setType(14);
        ast2.setType(15);
        ast3.setType(154);

        ast2.addChild(ast1);
        ast3.addChild(ast2);

        Scope actual1 = ScopeUtils.getSurroundingScope(ast1);
        Scope actual2 = ScopeUtils.getSurroundingScope(ast2);
        Scope actual3 = ScopeUtils.getSurroundingScope(ast3);

        assertEquals(Scope.PACKAGE, actual1);
        assertEquals(Scope.PUBLIC, actual2);
        assertEquals(Scope.PRIVATE, actual3);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);

        boolean actual1 = ScopeUtils.isInInterfaceBlock(ast1);

        assertTrue(actual1);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(157);

        boolean actual1 = ScopeUtils.isInAnnotationBlock(ast1);

        assertTrue(actual1);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);

        boolean actual1 = ScopeUtils.isInInterfaceOrAnnotationBlock(ast1);

        assertTrue(actual1);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(154);

        boolean actual1 = ScopeUtils.isInEnumBlock(ast1);

        assertTrue(actual1);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(9);

        boolean actual1 = ScopeUtils.isInCodeBlock(ast1);

        assertTrue(actual1);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);

        boolean actual1 = ScopeUtils.isOuterMostType(ast1);

        assertFalse(actual1);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);

        boolean actual1 = ScopeUtils.isLocalVariableDef(ast1);

        assertFalse(actual1);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);

        boolean actual1 = ScopeUtils.isClassFieldDef(ast1);

        assertFalse(actual1);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);

        Scope scope1 = Scope.PUBLIC;
        Scope scope2 = Scope.PRIVATE;

        boolean actual1 = ScopeUtils.isInScope(ast1, scope1);
        boolean actual2 = ScopeUtils.isInScope(ast1, scope2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

}