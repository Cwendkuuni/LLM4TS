import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("public");
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);
    }
    
    @Test
    public void testGetScopeFromMods_private() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("private");
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetScopeFromMods_protected() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("protected");
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST parent = new DetailAST();
        parent.setType(13);
        node.setParent(parent);
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, result);
    }
    
    @Test
    public void testGetSurroundingScope_innerClass() {
        DetailAST node = new DetailAST();
        node.setType(136);
        DetailAST parent = new DetailAST();
        parent.setType(156);
        node.setParent(parent);
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154);
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9);
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsOuterMostType_true() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(16);
        node.setParent(parent);
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsOuterMostType_false() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        boolean result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef_true() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsLocalVariableDef_false() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(8);
        node.setParent(parent);
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsClassFieldDef_true() {
        DetailAST node = new DetailAST();
        node.setType(10);
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef_false() {
        DetailAST node = new DetailAST();
        node.setType(8);
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope_true() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        ast.setParent(parent);
        Scope scope = Scope.PACKAGE;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);
    }
    
    @Test
    public void testIsInScope_false() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(136);
        ast.setParent(parent);
        Scope scope = Scope.PACKAGE;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
    }
}