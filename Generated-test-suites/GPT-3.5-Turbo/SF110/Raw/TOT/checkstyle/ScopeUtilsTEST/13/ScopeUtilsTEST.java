import org.junit.*;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_typicalCase() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetScopeFromMods_edgeCase() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetScopeFromMods_errorCase() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST("invalid"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15)); // parent is a class
        Scope expectedScope = Scope.PUBLIC;
        DetailAST mods = new DetailAST();
        mods.addChild(new AST("public"));
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_edgeCase() {
        DetailAST node = new DetailAST(); // parent is a null
        Scope expectedScope = null;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_errorCase() {
        DetailAST node = new DetailAST(); // parent is an invalid type
        node.addChild(new DetailAST(100)); 
        Scope expectedScope = null;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15)); // parent is an interface
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_errorCase() {
        DetailAST node = new DetailAST(); // parent is an invalid type
        node.addChild(new DetailAST(100)); 
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(157)); // parent is an annotation
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_errorCase() {
        DetailAST node = new DetailAST(); // parent is an invalid type
        node.addChild(new DetailAST(100)); 
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_typicalCase_Interface() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15)); // parent is an interface
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_typicalCase_Annotation() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(157)); // parent is an annotation
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(154)); // parent is an enum
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_errorCase() {
        DetailAST node = new DetailAST(); // parent is an invalid type
        node.addChild(new DetailAST(100)); 
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(9)); // parent is a code block
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsInCodeBlock_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(15)); // parent is a class
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(10)); // node is a local variable definition
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsLocalVariableDef_edgeCase() {
        DetailAST node = new DetailAST(); // parent is null
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef_typicalCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(10)); // node is a class field definition
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsClassFieldDef_edgeCase() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST(10)); // node is a local variable definition
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope_typicalCase() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(15)); // parent is a class
        Scope scope = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast, scope));
    }

    @Test
    public void testIsInScope_edgeCase() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(15)); // parent is a class
        Scope scope = null;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

    @Test
    public void testIsInScope_errorCase() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(15)); // parent is a class
        Scope scope = Scope.PRIVATE;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

}