import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import antlr.collections.AST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, result);
        
        AST token = new AST();
        token.setText("public");
        aMods.setFirstChild(token);
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);
        
        token.setText("protected");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);
        
        token.setText("private");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertNull(result);
        
        DetailAST token = new DetailAST();
        token.setType(14);
        node.setParent(token);
        result = ScopeUtils.getSurroundingScope(node);
        assertNull(result);
        
        token.setType(15);
        DetailAST mods = new DetailAST();
        mods.setType(5);
        token.setFirstChild(mods);
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, result);
        
        DetailAST childToken = new DetailAST();
        childToken.setText("public");
        mods.setFirstChild(childToken);
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, result);
        
        token.setType(136);
        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(15);
        node.setParent(token);
        result = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(result);
        
        token.setType(14);
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        token.setType(14);
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        token.setType(136);
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(157);
        node.setParent(token);
        result = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(result);
        
        token.setType(14);
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        token.setType(15);
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        token.setType(136);
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(15);
        node.setParent(token);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
        
        token.setType(157);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(result);
        
        token.setType(14);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        token.setType(136);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(154);
        node.setParent(token);
        result = ScopeUtils.isInEnumBlock(node);
        assertTrue(result);
        
        token.setType(14);
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        token.setType(15);
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        token.setType(157);
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        token.setType(136);
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(9);
        node.setParent(token);
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        token.setType(8);
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        token.setType(11);
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
        
        token.setType(12);
        result = ScopeUtils.isInCodeBlock(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
        
        DetailAST parentToken = new DetailAST();
        DetailAST token1 = new DetailAST();
        token1.setType(14);
        parentToken.setParent(token1);
        node.setParent(parentToken);
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(10);
        node.setParent(token);
        result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        token.setType(7);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        token.setType(35);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        token.setType(156);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
        
        token.setType(21);
        result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        DetailAST parentToken = new DetailAST();
        parentToken.setType(96);
        token.setParent(parentToken);
        result = ScopeUtils.isLocalVariableDef(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        DetailAST token = new DetailAST();
        token.setType(10);
        node.setParent(token);
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        token.setType(7);
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
        
        token.setType(35);
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
        
        token.setType(156);
        result = ScopeUtils.isClassFieldDef(node);
        assertTrue(result);
        
        token.setType(21);
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PUBLIC;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        DetailAST surroundingScopeOfAstToken = new DetailAST();
        surroundingScopeOfAstToken.setType(14);
        ast.getParent();
        ScopeUtils.getSurroundingScope(ast);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(15);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(157);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(154);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(8);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        scope = Scope.PACKAGE;
        surroundingScopeOfAstToken.setType(14);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(15);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(157);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(154);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(8);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        scope = Scope.PRIVATE;
        surroundingScopeOfAstToken.setType(14);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(15);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(157);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(154);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(8);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        scope = Scope.PROTECTED;
        surroundingScopeOfAstToken.setType(14);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(15);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(157);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(154);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
        
        surroundingScopeOfAstToken.setType(8);
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
    }
}