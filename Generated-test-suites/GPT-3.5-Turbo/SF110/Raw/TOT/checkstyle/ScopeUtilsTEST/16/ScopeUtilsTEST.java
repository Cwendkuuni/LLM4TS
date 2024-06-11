
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.Scope;
import antlr.collections.AST;
import antlr.collections.DetailAST;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        // Typical use case
        DetailAST ast = createMockTokens("public");
        Scope result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PUBLIC, result);
        
        // Edge case: no modifiers
        ast = createMockTokens(null);
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);
        
        // Error scenario: invalid modifier
        ast = createMockTokens("invalid");
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Typical use case: parent is public
        DetailAST ast = createMockTokens("public class");
        Scope result = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PUBLIC, result);
        
        // Typical use case: parent is protected
        ast = createMockTokens("protected class");
        result = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PROTECTED, result);
        
        // Typical use case: parent is private
        ast = createMockTokens("private class");
        result = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PRIVATE, result);
        
        // Edge case: parent is package level
        ast = createMockTokens("class");
        result = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PACKAGE, result);
        
        // Error scenario: invalid parent type
        ast = createMockTokens("invalid");
        result = ScopeUtils.getSurroundingScope(ast);
        assertNull(result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical use case: inside interface
        DetailAST ast = createMockTokens("", "interface");
        boolean result = ScopeUtils.isInInterfaceBlock(ast);
        assertTrue(result);
        
        // Edge case: inside enum
        ast = createMockTokens("", "enum");
        result = ScopeUtils.isInInterfaceBlock(ast);
        assertFalse(result);
        
        // Error scenario: inside class
        ast = createMockTokens("", "class");
        result = ScopeUtils.isInInterfaceBlock(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Typical use case: inside annotation
        DetailAST ast = createMockTokens("", "@interface");
        boolean result = ScopeUtils.isInAnnotationBlock(ast);
        assertTrue(result);
        
        // Edge case: inside class
        ast = createMockTokens("", "class");
        result = ScopeUtils.isInAnnotationBlock(ast);
        assertFalse(result);
        
        // Error scenario: inside enum
        ast = createMockTokens("", "enum");
        result = ScopeUtils.isInAnnotationBlock(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical use case: inside interface
        DetailAST ast = createMockTokens("", "interface");
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(result);
        
        // Typical use case: inside annotation
        ast = createMockTokens("", "@interface");
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(result);
        
        // Edge case: inside enum
        ast = createMockTokens("", "enum");
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertFalse(result);
        
        // Error scenario: inside class
        ast = createMockTokens("", "class");
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Typical use case: inside enum
        DetailAST ast = createMockTokens("", "enum");
        boolean result = ScopeUtils.isInEnumBlock(ast);
        assertTrue(result);
        
        // Edge case: inside interface
        ast = createMockTokens("", "interface");
        result = ScopeUtils.isInEnumBlock(ast);
        assertFalse(result);
        
        // Error scenario: inside class
        ast = createMockTokens("", "class");
        result = ScopeUtils.isInEnumBlock(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Typical use case: inside code block
        DetailAST ast = createMockTokens("", "{}");
        boolean result = ScopeUtils.isInCodeBlock(ast);
        assertTrue(result);
        
        // Edge case: inside class declaration
        ast = createMockTokens("", "class");
        result = ScopeUtils.isInCodeBlock(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        // Typical use case: outermost type
        DetailAST ast = createMockTokens("", "class", "public");
        boolean result = ScopeUtils.isOuterMostType(ast);
        assertTrue(result);
        
        // Edge case: not outermost type
        ast = createMockTokens("class", "private");
        result = ScopeUtils.isOuterMostType(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical use case: local variable definition
        DetailAST ast = createMockTokens("int", "x");
        boolean result = ScopeUtils.isLocalVariableDef(ast);
        assertTrue(result);
        
        // Edge case: not local variable definition
        ast = createMockTokens("int", "x");
        result = ScopeUtils.isLocalVariableDef(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Typical use case: class field definition
        DetailAST ast = createMockTokens("private", "static", "final", "int", "x");
        boolean result = ScopeUtils.isClassFieldDef(ast);
        assertTrue(result);
        
        // Edge case: not class field definition
        ast = createMockTokens("int", "x");
        result = ScopeUtils.isClassFieldDef(ast);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        // Typical use case: in scope
        DetailAST ast = createMockTokens("public class", "public");
        Scope scope = Scope.PUBLIC;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertTrue(result);
        
        // Typical use case: not in scope
        ast = createMockTokens("public class", "protected");
        scope = Scope.PUBLIC;
        result = ScopeUtils.isInScope(ast, scope);
        assertFalse(result);
    }
    
    // Helper method to create mock tokens for testing
    private DetailAST createMockTokens(String... tokens) {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        DetailAST current = ast;
        for (String token : tokens) {
            DetailAST newToken = new DetailAST();
            newToken.setType(1);
            newToken.setText(token);
            current.addChild(newToken);
            current = newToken;
        }
        return ast;
    }

}
