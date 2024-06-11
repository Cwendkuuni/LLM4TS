import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.setType(100);
        ast.addChild(new DetailAST());
        ast.getFirstChild().setText("public");

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        ast.getFirstChild().setText("protected");
        expected = Scope.PROTECTED;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        ast.getFirstChild().setText("private");
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        // TODO: Add test cases for getSurroundingScope method
    }

    @Test
    public void testIsInInterfaceBlock() {
        // TODO: Add test cases for isInInterfaceBlock method
    }

    @Test
    public void testIsInAnnotationBlock() {
        // TODO: Add test cases for isInAnnotationBlock method
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // TODO: Add test cases for isInInterfaceOrAnnotationBlock method
    }

    @Test
    public void testIsInEnumBlock() {
        // TODO: Add test cases for isInEnumBlock method
    }

    @Test
    public void testIsInCodeBlock() {
        // TODO: Add test cases for isInCodeBlock method
    }

    @Test
    public void testIsOuterMostType() {
        // TODO: Add test cases for isOuterMostType method
    }

    @Test
    public void testIsLocalVariableDef() {
        // TODO: Add test cases for isLocalVariableDef method
    }

    @Test
    public void testIsClassFieldDef() {
        // TODO: Add test cases for isClassFieldDef method
    }

    @Test
    public void testIsInScope() {
        // TODO: Add test cases for isInScope method
    }
}