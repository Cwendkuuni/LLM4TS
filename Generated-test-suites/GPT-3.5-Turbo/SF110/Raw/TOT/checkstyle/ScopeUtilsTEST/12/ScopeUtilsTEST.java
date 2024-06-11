import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeUtilsTest {

    @Test
    void getScopeFromMods_shouldReturnPackage() {
        DetailAST aMods = null;
        Scope expected = Scope.PACKAGE;

        Scope actual = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expected, actual);
    }

    @Test
    void getScopeFromMods_shouldReturnPublic() {
        DetailAST aMods = createDetailASTWithToken("public");
        Scope expected = Scope.PUBLIC;

        Scope actual = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expected, actual);
    }

    @Test
    void getScopeFromMods_shouldReturnProtected() {
        DetailAST aMods = createDetailASTWithToken("protected");
        Scope expected = Scope.PROTECTED;

        Scope actual = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expected, actual);
    }

    @Test
    void getScopeFromMods_shouldReturnPrivate() {
        DetailAST aMods = createDetailASTWithToken("private");
        Scope expected = Scope.PRIVATE;

        Scope actual = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expected, actual);
    }

    @Test
    void getSurroundingScope_shouldReturnNull() {
        DetailAST node = null;
        Scope expected = null;

        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInInterfaceBlock_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isInInterfaceBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInAnnotationBlock_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isInAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInInterfaceOrAnnotationBlock_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInEnumBlock_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isInEnumBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInCodeBlock_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isInCodeBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    void isOuterMostType_shouldReturnTrue() {
        DetailAST node = null;
        boolean expected = true;

        boolean actual = ScopeUtils.isOuterMostType(node);

        assertEquals(expected, actual);
    }

    @Test
    void isLocalVariableDef_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expected, actual);
    }

    @Test
    void isClassFieldDef_shouldReturnFalse() {
        DetailAST node = null;
        boolean expected = false;

        boolean actual = ScopeUtils.isClassFieldDef(node);

        assertEquals(expected, actual);
    }

    @Test
    void isInScope_shouldReturnFalse() {
        DetailAST ast = null;
        Scope scope = Scope.PACKAGE;
        boolean expected = false;

        boolean actual = ScopeUtils.isInScope(ast, scope);

        assertEquals(expected, actual);
    }

    private DetailAST createDetailASTWithToken(String token) {
        DetailAST ast = new DetailAST();
        DetailAST child = new DetailAST();
        child.setText(token);
        ast.addChild(child);
        return ast;
    }

}