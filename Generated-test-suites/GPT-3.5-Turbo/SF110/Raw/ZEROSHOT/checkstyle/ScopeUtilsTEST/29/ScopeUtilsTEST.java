import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test behavior when aMods is null
        DetailAST aMods = null;
        Scope expectedScope = Scope.PACKAGE;
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(aMods));
        
        // Test behavior when aMods has no children
        aMods = new DetailAST();
        expectedScope = Scope.PACKAGE;
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(aMods));
        
        // Test behavior when aMods has children
        aMods.addChild(createToken("public"));
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(aMods));
        
        aMods.addChild(createToken("protected"));
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(aMods));
        
        aMods.addChild(createToken("private"));
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test behavior when node is null
        DetailAST node = null;
        Scope expectedScope = null;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedScope = null;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("14"));
        Scope expectedParentScope = Scope.PUBLIC;
        DetailAST mods = new DetailAST();
        mods.addChild(createToken("public"));
        node.getParent().addChild(mods);
        
        expectedScope = Scope.PUBLIC;
        assertEquals(expectedScope, ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceBlock(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceBlock(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("15"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceBlock(node));
        
        node.setParent(createToken("14"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInAnnotationBlock(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInAnnotationBlock(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("157"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInAnnotationBlock(node));
        
        node.setParent(createToken("14"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("157"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node.setParent(createToken("15"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        node.setParent(createToken("14"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInEnumBlock(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInEnumBlock(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("154"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInEnumBlock(node));
        
        node.setParent(createToken("15"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInCodeBlock(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInCodeBlock(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("9"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInCodeBlock(node));
        
        node.setParent(createToken("14"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isOuterMostType(node));
        
        // Test behavior when node has no parent
        node = new DetailAST();
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isOuterMostType(node));
        
        // Test behavior when node has parent
        node.setParent(createToken("15"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Test behavior when node is null
        DetailAST node = null;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
        
        // Test behavior when node type is 10 and parent type is not 7, 35 or 156
        node = createTokenOfType(10);
        node.setParent(createToken("11"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
        
        // Test behavior when node type is 10 and parent type is 7
        node.setParent(createToken("7"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
        
        // Test behavior when node type is 10 and parent type is 35
        node.setParent(createToken("35"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
        
        // Test behavior when node type is 10 and parent type is 156
        node.setParent(createToken("156"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
        
        // Test behavior when node type is 21 and parent type is 96
        node = createTokenOfType(21);
        node.setParent(createToken("96"));
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Test behavior when node type is 10 and isLocalVariableDef is false
        DetailAST node = createTokenOfType(10);
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isClassFieldDef(node));
        
        // Test behavior when node type is 10 and isLocalVariableDef is true
        node = createTokenOfType(10);
        node.setParent(createToken("7"));
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isClassFieldDef(node));
        
        // Test behavior when node type is not 10
        node = createTokenOfType(14);
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        // Test behavior when ast is null
        DetailAST ast = null;
        Scope scope = Scope.PUBLIC;
        boolean expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInScope(ast, scope));
        
        // Test behavior when surroundingScopeOfAstToken is not equal to scope
        ast = new DetailAST();
        ast.setParent(createToken("14"));
        scope = Scope.PUBLIC;
        expectedValue = false;
        assertEquals(expectedValue, ScopeUtils.isInScope(ast, scope));
        
        // Test behavior when surroundingScopeOfAstToken is equal to scope
        ast = new DetailAST();
        ast.setParent(createToken("14"));
        DetailAST mods = new DetailAST();
        mods.addChild(createToken("public"));
        ast.getParent().addChild(mods);
        scope = Scope.PUBLIC;
        expectedValue = true;
        assertEquals(expectedValue, ScopeUtils.isInScope(ast, scope));
    }
    
    private DetailAST createToken(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
    
    private DetailAST createTokenOfType(int type) {
        DetailAST token = new DetailAST();
        token.setType(type);
        return token;
    }
}