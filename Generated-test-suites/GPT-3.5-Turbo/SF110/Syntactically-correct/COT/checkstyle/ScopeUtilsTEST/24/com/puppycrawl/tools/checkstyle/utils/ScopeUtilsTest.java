import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createASTWithTokens("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

        mods = createASTWithTokens("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

        mods = createASTWithTokens("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(mods));

        mods = createASTWithTokens("default");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createASTWithParent(14);
        Scope expectedScope = Scope.PACKAGE;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));

        node = createASTWithParent(15);
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));

        node = createASTWithParent(157);
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));

        node = createASTWithParent(154);
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));

        node = createASTWithParent(136);
        expectedScope = Scope.ANONINNER;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createASTWithParent(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createASTWithParent(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createASTWithParent(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createASTWithParent(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createASTWithParent(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createASTWithParent(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createASTWithParent(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createASTWithParent(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createASTWithParent(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createASTWithParent(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createASTWithParent(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createASTWithParent(154);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createASTWithParent(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createASTWithParent(136);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createASTWithParent(14);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createASTWithParent(154);
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createASTWithParent(15);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createASTWithParent(157);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createASTWithParent(136);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createASTWithParent(9);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(8);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(11);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(12);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(7);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(35);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createASTWithParent(156);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createASTWithParent(14);
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createASTWithParent(15);
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createASTWithParent(157);
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createASTWithParent(154);
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createASTWithParent(96);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createASTWithParent(10);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createASTWithType(10);
        node = createASTWithParent(node, 7);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createASTWithType(10);
        node = createASTWithParent(node, 35);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createASTWithType(10);
        node = createASTWithParent(node, 156);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createASTWithType(21);
        node = createASTWithParent(node, 96);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createASTWithType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = createASTWithType(21);
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ScopeUtils.getSurroundingScope(ast); // dummy call to initialize
        ScopeUtils.getSurroundingScope(ast); // dummy call to initialize
        ScopeUtils.getSurroundingScope(ast); // dummy call to initialize

        ast = new DetailAST();
        DetailAST parentNode = createASTWithParent(14);
        ast.setParent(parentNode);
        assertTrue(ScopeUtils.isInScope(ast, Scope.PACKAGE));

        ast = new DetailAST();
        parentNode = createASTWithParent(15);
        ast.setParent(parentNode);
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));
    }

    private DetailAST createASTWithTokens(String... tokens) {
        DetailAST ast = new DetailAST();
        for (String token : tokens) {
            AST child = new AST();
            child.setText(token);
            ast.addChild(new AST(token));
        }
        return ast;
    }

    private DetailAST createASTWithParent(int parentType) {
        DetailAST parent = new DetailAST();
        parent.setType(parentType);
        DetailAST ast = new DetailAST();
        ast.setParent(parent);
        return ast;
    }

    private DetailAST createASTWithParent(DetailAST ast, int parentType) {
        DetailAST parent = new DetailAST();
        parent.setType(parentType);
        ast.setParent(parent);
        return ast;
    }

    private DetailAST createASTWithType(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }
}