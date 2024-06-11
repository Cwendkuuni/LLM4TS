import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    // Test for getScopeFromMods method
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(new AST(8, "public"));
        mods.addChild(new AST(9, "protected"));
        mods.addChild(new AST(10, "private"));

        Scope scope = ScopeUtils.getScopeFromMods(mods);

        assertEquals(Scope.PUBLIC, scope); // Typical use case
        assertFalse(Scope.PROTECTED.equals(scope)); // Error scenario
        assertFalse(Scope.PRIVATE.equals(scope)); // Error scenario
    }

    // Test for getSurroundingScope method
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST(14, "class");
        DetailAST parent = new DetailAST(15, "interface");
        node.addChild(parent);

        Scope scope = ScopeUtils.getSurroundingScope(node);

        assertNull(scope); // Edge case
        assertNotNull(scope); // Typical use case
        assertNotEquals(Scope.PACKAGE, scope); // Error scenario
    }

    // Test for isInInterfaceBlock method
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST(15, "interface");

        boolean result = ScopeUtils.isInInterfaceBlock(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isInAnnotationBlock method
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST(157, "annotation");

        boolean result = ScopeUtils.isInAnnotationBlock(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isInInterfaceOrAnnotationBlock method
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST(15, "interface");

        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isInEnumBlock method
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST(154, "enum");

        boolean result = ScopeUtils.isInEnumBlock(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isInCodeBlock method
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST(9, "method");

        boolean result = ScopeUtils.isInCodeBlock(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isOuterMostType method
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST(14, "class");

        boolean result = ScopeUtils.isOuterMostType(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isLocalVariableDef method
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST(10, "localVariable");

        boolean result = ScopeUtils.isLocalVariableDef(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isClassFieldDef method
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST(10, "field");

        boolean result = ScopeUtils.isClassFieldDef(node);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }

    // Test for isInScope method
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.addChild(new AST(8, "public"));

        Scope scope = ScopeUtils.getScopeFromMods(ast);
        boolean result = ScopeUtils.isInScope(ast, scope);

        assertTrue(result); // Typical use case
        assertFalse(result); // Error scenario
    }
}