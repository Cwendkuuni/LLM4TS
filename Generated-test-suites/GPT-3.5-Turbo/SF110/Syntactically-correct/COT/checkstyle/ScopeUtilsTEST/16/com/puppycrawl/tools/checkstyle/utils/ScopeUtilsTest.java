import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test with no mods
        DetailAST ast1 = new DetailAST();
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast1));

        // Test with public mod
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast2));

        // Test with protected mod
        DetailAST ast3 = new DetailAST();
        ast3.addChild(new DetailAST());
        ast3.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast3));

        // Test with private mod
        DetailAST ast4 = new DetailAST();
        ast4.addChild(new DetailAST());
        ast4.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast4));
    }

    @Test
    public void testGetSurroundingScope() {
        // Test with null parent
        DetailAST ast1 = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(ast1));

        // Test with valid parent
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(14); // Class token
        ast2.addChild(new DetailAST());
        ast2.getLastChild().addChild(new DetailAST());
        ast2.getLastChild().getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast2));
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Test when not in interface block
        DetailAST ast1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(ast1));

        // Test when in interface block
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(15); // Interface token
        assertTrue(ScopeUtils.isInInterfaceBlock(ast2));
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Test when not in annotation block
        DetailAST ast1 = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(ast1));

        // Test when in annotation block
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(157); // Annotation token
        assertTrue(ScopeUtils.isInAnnotationBlock(ast2));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test when not in interface or annotation block
        DetailAST ast1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast1));

        // Test when in interface block
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(15); // Interface token
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast2));

        // Test when in annotation block
        DetailAST ast3 = new DetailAST();
        ast3.addChild(new DetailAST());
        ast3.getFirstChild().setType(157); // Annotation token
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast3));
    }

    @Test
    public void testIsInEnumBlock() {
        // Test when not in enum block
        DetailAST ast1 = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(ast1));

        // Test when in enum block
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(154); // Enum token
        assertTrue(ScopeUtils.isInEnumBlock(ast2));
    }

    @Test
    public void testIsInCodeBlock() {
        // Test when not in code block
        DetailAST ast1 = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(ast1));

        // Test when in code block
        DetailAST ast2 = new DetailAST();
        ast2.addChild(new DetailAST());
        ast2.getFirstChild().setType(9); // Method token
        assertTrue(ScopeUtils.isInCodeBlock(ast2));
    }

    @Test
    public void testIsOuterMostType() {
        // Test when not outer most type
        DetailAST ast1 = new DetailAST();
        ast1.setType(15); // Interface token
        ast1.addChild(new DetailAST());
        ast1.getFirstChild().setType(14); // Class token
        assertFalse(ScopeUtils.isOuterMostType(ast1.getFirstChild()));

        // Test when outer most type
        DetailAST ast2 = new DetailAST();
        ast2.setType(14); // Class token
        assertTrue(ScopeUtils.isOuterMostType(ast2));
    }

    @Test
    public void testIsLocalVariableDef() {
        // Test when not local variable def
        DetailAST ast1 = new DetailAST();
        ast1.setType(10); // Identifier token
        assertFalse(ScopeUtils.isLocalVariableDef(ast1));

        // Test local variable def in variable declaration
        DetailAST ast2 = new DetailAST();
        ast2.setType(10); // Identifier token
        ast2.setParent(new DetailAST());
        ast2.getParent().setType(7); // VariableDef token
        assertTrue(ScopeUtils.isLocalVariableDef(ast2));

        // Test local variable def in for loop
        DetailAST ast3 = new DetailAST();
        ast3.setType(10); // Identifier token
        ast3.setParent(new DetailAST());
        ast3.getParent().setType(35); // ForLoop token
        assertTrue(ScopeUtils.isLocalVariableDef(ast3));
    }

    @Test
    public void testIsClassFieldDef() {
        // Test when not class field def
        DetailAST ast1 = new DetailAST();
        ast1.setType(10); // Identifier token
        assertFalse(ScopeUtils.isClassFieldDef(ast1));

        // Test when class field def
        DetailAST ast2 = new DetailAST();
        ast2.setType(10); // Identifier token
        ast2.setParent(new DetailAST());
        ast2.getParent().setType(7); // VariableDef token
        assertFalse(ScopeUtils.isClassFieldDef(ast2));
    }

    @Test
    public void testIsInScope() {
        // Test when in scope
        DetailAST ast1 = new DetailAST();
        ast1.setType(10); // Identifier token
        assertTrue(ScopeUtils.isInScope(ast1, Scope.PACKAGE));

        // Test when not in scope
        DetailAST ast2 = new DetailAST();
        ast2.setType(10); // Identifier token
        assertFalse(ScopeUtils.isInScope(ast2, Scope.PRIVATE));
    }
}