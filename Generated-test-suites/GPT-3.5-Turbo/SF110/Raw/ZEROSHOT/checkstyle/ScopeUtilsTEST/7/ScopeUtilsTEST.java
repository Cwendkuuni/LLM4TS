import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, result);

        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);

        aMods.getFirstChild().setText("protected");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);

        aMods.getFirstChild().setText("private");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertNull(result);

        node.setParent(new DetailAST());
        node.getParent().setType(14); // CLASS_DEF
        DetailAST mods = new DetailAST();
        mods.addChild(new DetailAST());
        mods.getFirstChild().setText("public");
        node.getParent().addChild(mods);
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);

        node.getParent().getChild(0).setType(15); // INTERFACE_DEF
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);

        node.getParent().getChild(0).setType(157); // ANNOTATION_DEF
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);

        node.getParent().getChild(0).setType(154); // ENUM_DEF
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);

        node.setParent(new DetailAST());
        node.getParent().setType(15); // INTERFACE_DEF
        result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);

        node.getParent().setType(14); // CLASS_DEF
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);

        node.getParent().setType(154); // ENUM_DEF
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);

        node.setParent(new DetailAST());
        node.getParent().setType(157); // ANNOTATION_DEF
        result = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(result);

        node.getParent().setType(14); // CLASS_DEF
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);

        node.getParent().setType(15); // INTERFACE_DEF
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);

        node.setParent(new DetailAST());
        node.getParent().setType(15); // INTERFACE_DEF
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);

        node.getParent().setType(157); // ANNOTATION_DEF
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);

        node.getParent().setType(14); // CLASS_DEF
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);

        node.getParent().setType(154); // ENUM_DEF
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);

        node.setParent(new DetailAST());
        node.getParent().setType(154); // ENUM_DEF
        result = ScopeUtils.isInEnumBlock(node);
        assertTrue(result);

        node.getParent().setType(14); // CLASS_DEF
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);

        node.getParent().setType(15); // INTERFACE_DEF
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);

        node.getParent().setType(157); // ANNOTATION_DEF
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);

        node.setParent(new DetailAST());
        node.getParent().setType(9); // STATIC_INIT
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);

        node.getParent().setType(8); // INSTANCE_INIT
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);

        node.getParent().setType(11); // LITERAL_FOR
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);

        node.getParent().setType(12); // LITERAL_DO
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);

        node.getParent().setType(154); // ENUM_DEF
        result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);

        node.setParent(new DetailAST());
        node.getParent().setType(14); // CLASS_DEF
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);

        node.getParent().setType(15); // INTERFACE_DEF
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);

        node.getParent().setType(157); // ANNOTATION_DEF
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);

        node.getParent().setType(154); // ENUM_DEF
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);

        node.getParent().setType(136); // TYPE
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);

        node.setType(10); // VARIABLE_DEF
        node.setParent(new DetailAST());
        node.getParent().setType(7); // METHOD_DEF
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);

        node.getParent().setType(35); // CTOR_DEF
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);

        node.getParent().setType(156); // LITERAL_NEW
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);

        node.setType(21); // TYPE
        node.getParent().setType(96); // LABELED_STAT
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);

        node.getParent().setType(10); // VARIABLE_DEF
        result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);

        node.setType(10); // VARIABLE_DEF
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);

        node.setParent(new DetailAST());
        node.getParent().setType(10); // VARIABLE_DEF
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);

        ast.setParent(new DetailAST());
        ast.getParent().setType(14); // CLASS_DEF
        ast.setType(10); // VARIABLE_DEF
        DetailAST mods = new DetailAST();
        mods.addChild(new DetailAST());
        mods.getFirstChild().setText("public");
        ast.getParent().addChild(mods);
        result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);

        scope = Scope.PRIVATE;
        ast.getParent().getChild(0).setType(14); // CLASS_DEF
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);

        ast.getParent().getChild(0).setType(15); // INTERFACE_DEF
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);

        ast.getParent().getChild(0).setType(157); // ANNOTATION_DEF
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);

        ast.getParent().getChild(0).setType(154); // ENUM_DEF
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);

        ast.getParent().setType(136); // TYPE
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
    }
}