import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test with aMods containing "public"
        DetailAST aMods1 = new DetailAST();
        AST publicMod = new AST();
        publicMod.setText("public");
        aMods1.setFirstChild(publicMod);
        Scope result1 = ScopeUtils.getScopeFromMods(aMods1);
        assertEquals(Scope.PUBLIC, result1);

        // Test with aMods containing "protected"
        DetailAST aMods2 = new DetailAST();
        AST protectedMod = new AST();
        protectedMod.setText("protected");
        aMods2.setFirstChild(protectedMod);
        Scope result2 = ScopeUtils.getScopeFromMods(aMods2);
        assertEquals(Scope.PROTECTED, result2);

        // Test with aMods containing "private"
        DetailAST aMods3 = new DetailAST();
        AST privateMod = new AST();
        privateMod.setText("private");
        aMods3.setFirstChild(privateMod);
        Scope result3 = ScopeUtils.getScopeFromMods(aMods3);
        assertEquals(Scope.PRIVATE, result3);

        // Test with aMods not containing any of the above
        DetailAST aMods4 = new DetailAST();
        Scope result4 = ScopeUtils.getScopeFromMods(aMods4);
        assertEquals(Scope.PACKAGE, result4);
    }

    @Test
    public void testGetSurroundingScope() {
        // TODO: write test cases for getSurroundingScope()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInInterfaceBlock() {
        // TODO: write test cases for isInInterfaceBlock()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInAnnotationBlock() {
        // TODO: write test cases for isInAnnotationBlock()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // TODO: write test cases for isInInterfaceOrAnnotationBlock()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInEnumBlock() {
        // TODO: write test cases for isInEnumBlock()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInCodeBlock() {
        // TODO: write test cases for isInCodeBlock()
        // only provided an empty test method for now
    }

    @Test
    public void testIsOuterMostType() {
        // TODO: write test cases for isOuterMostType()
        // only provided an empty test method for now
    }

    @Test
    public void testIsLocalVariableDef() {
        // TODO: write test cases for isLocalVariableDef()
        // only provided an empty test method for now
    }

    @Test
    public void testIsClassFieldDef() {
        // TODO: write test cases for isClassFieldDef()
        // only provided an empty test method for now
    }

    @Test
    public void testIsInScope() {
        // TODO: write test cases for isInScope()
        // only provided an empty test method for now
    }
}