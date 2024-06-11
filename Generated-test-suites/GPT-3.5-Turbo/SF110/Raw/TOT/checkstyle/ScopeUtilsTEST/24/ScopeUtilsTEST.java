import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_typicalCase() {
        DetailAST aMods = new DetailAST();
        DetailAST token1 = new AST("public");
        aMods.addChild(token1);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetScopeFromMods_invalidCase() {
        DetailAST aMods = new DetailAST();
        DetailAST token1 = new AST("invalid");
        aMods.addChild(token1);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope_typicalCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("public");
        DetailAST mods = new DetailAST();
        mods.addChild(token1);
        node.addChild(mods);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testGetSurroundingScope_nullCase() {
        DetailAST node = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("valid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInInterfaceBlock_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("invalid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("valid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("invalid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("valid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("invalid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("valid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("invalid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("valid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST token1 = new AST("invalid");
        DetailAST parent = new DetailAST();
        parent.addChild(token1);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType_trueCase() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsOuterMostType_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsLocalVariableDef_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef_trueCase() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsClassFieldDef_falseCase() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope_trueCase() {
        DetailAST ast = new DetailAST();
        DetailAST token1 = new AST("public");
        DetailAST mods = new DetailAST();
        mods.addChild(token1);
        ast.addChild(mods);
        Scope scope = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast, scope));
    }
    
    @Test
    public void testIsInScope_falseCase() {
        DetailAST ast = new DetailAST();
        DetailAST token1 = new AST("private");
        DetailAST mods = new DetailAST();
        mods.addChild(token1);
        ast.addChild(mods);
        Scope scope = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

}