import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.setType(5);
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        aMods.addChild(publicToken);

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST mods = new DetailAST();
        mods.setType(5);
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        mods.addChild(publicToken);
        node.addChild(mods);

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);

        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154);

        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9);

        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = false;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        DetailAST surroundingScopeOfAstToken = new DetailAST();
        surroundingScopeOfAstToken.setType(14);

        boolean expected = true;
        boolean actual = ScopeUtils.isInScope(ast, Scope.PACKAGE);
        assertEquals(expected, actual);
    }

}