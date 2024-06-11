import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().addChild(new AST(1, "public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));

        // Edge cases
        ast = new DetailAST();
        assertNull(ScopeUtils.getScopeFromMods(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().addChild(new AST(1, "protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
        
        ast = new DetailAST();
        ast.addChild(new AST());
        ast.getFirstChild().addChild(new AST(1, "private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));

        // Error scenario
        ast = null;
        assertNull(ScopeUtils.getScopeFromMods(ast));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(14);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(15);
        parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(157);
        parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast = new DetailAST();
        ast.setType(154);
        parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        // Error scenario
        ast = null;
        assertNull(ScopeUtils.getSurroundingScope(ast));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(15);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(14);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        ast.setType(154);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        ast.setType(157);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(157);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(14);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(154);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(15);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(157);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(15);
        parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(14);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(154);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }

    @Test
    public void testIsInEnumBlock() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(154);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(15);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        ast.setType(157);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        ast.setType(14);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(9);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(8);
        parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        ast.setType(11);
        parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = new DetailAST();
        ast.setType(12);
        parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(15);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isOuterMostType(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(14);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        ast.setType(157);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        ast.setType(154);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        ast = new DetailAST();
        ast.setType(136);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isOuterMostType(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isOuterMostType(ast));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(21);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(96);
        grandParent.addChild(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        parent.setType(7);
        parent.addChild(ast);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        parent.setType(35);
        parent.addChild(ast);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        parent.setType(156);
        parent.addChild(ast);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(10);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        // Edge cases
        ast = new DetailAST();
        ast.setType(21);
        parent = new DetailAST();
        parent.addChild(ast);
        assertTrue(ScopeUtils.isClassFieldDef(ast));

        ast = new DetailAST();
        ast.setType(10);
        parent = new DetailAST();
        parent.addChild(ast);
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        // Error scenario
        ast = null;
        assertFalse(ScopeUtils.isClassFieldDef(ast));
    }
    
    @Test
    public void testIsInScope() {
        // Typical use case
        DetailAST ast = new DetailAST();
        ast.setType(14);
        DetailAST parent = new DetailAST();
        parent.addChild(ast);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        // Error scenario
        ast = null;
        assertNull(ScopeUtils.getSurroundingScope(ast));
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }

}