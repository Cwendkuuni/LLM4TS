import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = createMods("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        aMods = createMods("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));

        aMods = createMods("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));

        aMods = createMods("static");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        aMods = createMods("public", "static");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        aMods = createMods("public", "final");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createNode(15, "public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

        node = createNode(15, "protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));

        node = createNode(154, "private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

        node = createNode(157, "static");
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));

        node = createNode(136, "public");
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));

        node = createNode(15, "public", "protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));

        node = createNode(15, "public", "private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

        node = createNode(15, "public", "private", "protected");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createNode(15, "public");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createNode(154, "private");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createNode(136, "public");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createNode(136, "private");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createNode(15, "public", "private");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createNode(157, "public");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createNode(154, "private");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createNode(136, "public");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createNode(15, "public");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createNode(157, "public", "private");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createNode(15, "public");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNode(157, "private");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNode(136, "public");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNode(136, "private");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNode(15, "public", "private");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createNode(154, "public");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createNode(157, "private");
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createNode(136, "public");
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createNode(15, "public");
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createNode(154, "public", "private");
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createNode(9, "public");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createNode(8, "private");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createNode(11, "public");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createNode(12, "protected");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createNode(15, "public");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createNode(154, "public");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createNode(157, "public");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createNode(15, "public");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createNode(14, "private");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createNode(157, "public");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createNode(154, "private");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createNode(136, "public");
        assertFalse(ScopeUtils.isOuterMostType(node));

        DetailAST parent = createNode(15, "public");
        node = createNode(parent, 15, "private");
        assertFalse(ScopeUtils.isOuterMostType(node));

        parent = createNode(15, "public");
        node = createNode(parent, 154, "private");
        assertFalse(ScopeUtils.isOuterMostType(node));

        parent = createNode(15, "public");
        node = createNode(parent, 14, "private");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createNode(10, "public");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createNode(10, "private");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createNode(10, "protected");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createNode(21, "protected");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createNode(10, "static");
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = createNode(10, "final");
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = createNode(21, "private");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createNode(10, "public");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createNode(10, "private");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createNode(10, "protected");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createNode(21, "protected");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = createNode(10, "static");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = createNode(10, "final");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = createNode(21, "private");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createNode(15, "public");
        assertFalse(ScopeUtils.isInScope(ast, Scope.PROTECTED));

        ast = createNode(14, "private");
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        ast = createNode(157, "public");
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));

        ast = createNode(154, "private");
        assertFalse(ScopeUtils.isInScope(ast, Scope.ANONINNER));

        ast = createNode(154, "private");
        assertTrue(ScopeUtils.isInScope(ast, Scope.PRIVATE));

        ast = createNode(136, "public");
        assertFalse(ScopeUtils.isInScope(ast, Scope.ANONINNER));

        ast = createNode(15, "public");
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        ast = createNode(154, "private");
        assertTrue(ScopeUtils.isInScope(ast, Scope.PRIVATE));

        ast = createNode(15, "public", "private");
        assertTrue(ScopeUtils.isInScope(ast, Scope.PRIVATE));
    }

    private DetailAST createMods(String... mods) {
        DetailAST aMods = createNode(26, null);
        for (String mod : mods) {
            aMods.addChild(createNode(26, mod));
        }
        return aMods;
    }

    private DetailAST createNode(int type, String token) {
        return createNode(null, type, token);
    }

    private DetailAST createNode(DetailAST parent, int type, String token) {
        DetailAST node = new DetailAST();
        node.setParent(parent);
        node.setType(type);
        node.setText(token);
        return node;
    }
}