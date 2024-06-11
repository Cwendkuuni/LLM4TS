import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST(53, "public"));

        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "public"));

        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "interface"));

        boolean isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(isInInterfaceBlock);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(157, "@interface"));

        boolean isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(isInAnnotationBlock);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "interface"));

        boolean isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(isInInterfaceOrAnnotationBlock);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(154, "enum"));

        boolean isInEnumBlock = ScopeUtils.isInEnumBlock(node);
        assertTrue(isInEnumBlock);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(9, "for"));

        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(node);
        assertTrue(isInCodeBlock);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "public"));

        boolean isOuterMostType = ScopeUtils.isOuterMostType(node);
        assertTrue(isOuterMostType);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(10, "int"));

        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        assertFalse(isLocalVariableDef);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(10, "int"));

        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        assertTrue(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        Scope scope = Scope.PUBLIC;
        DetailAST ast = new DetailAST();

        boolean isInScope = ScopeUtils.isInScope(ast, scope);
        assertFalse(isInScope);
    }
}


