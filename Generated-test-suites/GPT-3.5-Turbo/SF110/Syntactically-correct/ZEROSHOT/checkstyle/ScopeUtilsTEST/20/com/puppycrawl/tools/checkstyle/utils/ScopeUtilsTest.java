import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(createToken("public"));
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);
        
        aMods = new DetailAST();
        aMods.addChild(createToken("protected"));
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, scope);
        
        aMods = new DetailAST();
        aMods.addChild(createToken("private"));
        scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, scope);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PROTECTED, scope);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, scope);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        result = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        result = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        result = ScopeUtils.isInEnumBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createToken("public"));
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("protected"));
        result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.addChild(createToken("private"));
        result = ScopeUtils.isInCodeBlock(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isOuterMostType(node);
        assertTrue(result);
        
        node = new DetailAST();
        node.setType(14);
        result = ScopeUtils.isOuterMostType(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        result = ScopeUtils.isLocalVariableDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
        
        node = new DetailAST();
        node.setType(10);
        result = ScopeUtils.isClassFieldDef(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        boolean result = ScopeUtils.isInScope(ast, Scope.PUBLIC);
        assertFalse(result);
    }
    
    private AST createToken(String text) {
        AST token = new AST();
        token.setText(text);
        return token;
    }
}