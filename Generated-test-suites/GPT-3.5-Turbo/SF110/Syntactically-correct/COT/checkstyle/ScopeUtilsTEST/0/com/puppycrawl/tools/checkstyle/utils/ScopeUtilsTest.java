
import org.junit.*;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        // Test with no modifiers
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, scope);

        // Test with public modifier
        aMods.addChild(new AST("public"));
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);

        // Test with protected modifier
        aMods.addChild(new AST("protected"));
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, scope);

        // Test with private modifier
        aMods.addChild(new AST("private"));
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        // Test with no parent
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        // Test with parent of type 14 (CLASS_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        parent.addChild(new DetailAST());
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);

        // Add modifiers to parent
        DetailAST mods = new DetailAST();
        parent.addChild(mods);
        // Test with public modifier
        mods.addChild(new AST("public"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        // Test with protected modifier
        mods.addChild(new AST("protected"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PROTECTED, scope);

        // Test with private modifier
        mods.addChild(new AST("private"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, scope);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(14);
        parent.setParent(grandparent);
        grandparent.addChild(new DetailAST());
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isInBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 15 (INTERFACE_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        isInBlock = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 14 (CLASS_DEF)
        parent.setType(14);
        isInBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 136 (ANNOTATION_DEF)
        parent.setType(136);
        isInBlock = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(isInBlock);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(15);
        parent.setParent(grandparent);
        isInBlock = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(isInBlock);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isInBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 157 (ANNOTATION_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        isInBlock = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 14 (CLASS_DEF)
        parent.setType(14);
        isInBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 15 (INTERFACE_DEF)
        parent.setType(15);
        isInBlock = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(isInBlock);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(157);
        parent.setParent(grandparent);
        isInBlock = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(isInBlock);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isInBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 15 (INTERFACE_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        isInBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 157 (ANNOTATION_DEF)
        parent.setType(157);
        isInBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 14 (CLASS_DEF)
        parent.setType(14);
        isInBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(isInBlock);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(15);
        parent.setParent(grandparent);
        isInBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(isInBlock);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isInBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 154 (ENUM_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        isInBlock = ScopeUtils.isInEnumBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 14 (CLASS_DEF)
        parent.setType(14);
        isInBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 15 (INTERFACE_DEF)
        parent.setType(15);
        isInBlock = ScopeUtils.isInEnumBlock(node);
        assertFalse(isInBlock);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(154);
        parent.setParent(grandparent);
        isInBlock = ScopeUtils.isInEnumBlock(node);
        assertTrue(isInBlock);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isInBlock = ScopeUtils.isInCodeBlock(node);
        assertFalse(isInBlock);

        // Test with parent of type 9 (SLIST)
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        isInBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 8 (CTOR_DEF)
        parent.setType(8);
        isInBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 11 (METHOD_DEF)
        parent.setType(11);
        isInBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInBlock);

        // Test with parent of type 12 (STATIC_INIT)
        parent.setType(12);
        isInBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInBlock);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(9);
        parent.setParent(grandparent);
        isInBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInBlock);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isOuterMost = ScopeUtils.isOuterMostType(node);
        assertTrue(isOuterMost);

        // Test with parent of type 14 (CLASS_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        isOuterMost = ScopeUtils.isOuterMostType(node);
        assertFalse(isOuterMost);

        // Test with parent of type 15 (INTERFACE_DEF)
        parent.setType(15);
        isOuterMost = ScopeUtils.isOuterMostType(node);
        assertFalse(isOuterMost);

        // Test with parent of type 157 (ANNOTATION_DEF)
        parent.setType(157);
        isOuterMost = ScopeUtils.isOuterMostType(node);
        assertFalse(isOuterMost);

        // Test with parent of type 154 (ENUM_DEF)
        parent.setType(154);
        isOuterMost = ScopeUtils.isOuterMostType(node);
        assertFalse(isOuterMost);

        // Test with multiple parent levels
        DetailAST grandparent = new DetailAST();
        grandparent.setType(14);
        parent.setParent(grandparent);
        isOuterMost = ScopeUtils.isOuterMostType(node);
        assertFalse(isOuterMost);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        // Test with no parent
        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertFalse(isLocalVariableDef);

        // Test with parent of type 7 (VARIABLE_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(isLocalVariableDef);

        // Test with parent of type 35 (RESOURCE)
        parent.setType(35);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(isLocalVariableDef);

        // Test with parent of type 156 (FOR_EACH_CLAUSE)
        parent.setType(156);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(isLocalVariableDef);

        // Test with parent of type 21 (TYPECAST)
        parent.setType(21);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertFalse(isLocalVariableDef);

        // Test with parent of type 96 (TYPE)
        parent.setType(96);
        DetailAST grandparent = new DetailAST();
        grandparent.setType(21);
        parent.setParent(grandparent);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertTrue(isLocalVariableDef);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        // Test with node type 10 (PARAMETER_DEF)
        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 7 (VARIABLE_DEF)
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 35 (RESOURCE)
        parent.setType(35);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 156 (FOR_EACH_CLAUSE)
        parent.setType(156);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 96 (TYPE)
        parent.setType(96);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 10 (PARAMETER_DEF)
        parent.setType(10);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertFalse(isClassFieldDef);

        // Test with node type 10 (PARAMETER_DEF) and parent type 14 (CLASS_DEF)
        parent.setType(14);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertTrue(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        // Test with no surrounding scope
        Scope scope = Scope.PUBLIC; // Any scope
        boolean isInScope = ScopeUtils.isInScope(ast, scope);
        assertFalse(isInScope);

        // Test with matching surrounding scope
        ast.setParent(new DetailAST());
        scope = ScopeUtils.getScopeFromMods(ast.getParent());
        isInScope = ScopeUtils.isInScope(ast, scope);
        assertTrue(isInScope);

        // Test with non-matching surrounding scope
        ast.setParent(new DetailAST());
        // Set different scope for ast's surrounding scope
        ast.getParent().addChild(new AST("private"));
        scope = ScopeUtils.getScopeFromMods(ast.getParent());
        isInScope = ScopeUtils.isInScope(ast, scope);
        assertFalse(isInScope);
    }
}

