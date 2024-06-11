import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    // Test getScopeFromMods method
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = createDetailAST();
        
        // Test with public modifier
        aMods.addChild(createAST("public"));
        assertEquals(ScopeUtils.getScopeFromMods(aMods), Scope.PUBLIC);
        
        // Test with protected modifier
        aMods = createDetailAST();
        aMods.addChild(createAST("protected"));
        assertEquals(ScopeUtils.getScopeFromMods(aMods), Scope.PROTECTED);
        
        // Test with private modifier
        aMods = createDetailAST();
        aMods.addChild(createAST("private"));
        assertEquals(ScopeUtils.getScopeFromMods(aMods), Scope.PRIVATE);
        
        // Test with package modifier
        aMods = createDetailAST();
        assertEquals(ScopeUtils.getScopeFromMods(aMods), Scope.PACKAGE);
    }

    // Test getSurroundingScope method
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createDetailAST();
        
        // Test with class declaration
        node.addChild(createASTAccordingToType(15)); // Class declaration
        DetailAST modifiers = createDetailAST();
        modifiers.addChild(createAST("public"));
        node.addChild(modifiers);
        assertEquals(ScopeUtils.getSurroundingScope(node), Scope.PUBLIC);
        
        // Test with enum declaration
        node = createDetailAST();
        node.addChild(createASTAccordingToType(154)); // Enum declaration
        modifiers = createDetailAST();
        modifiers.addChild(createAST("private"));
        node.addChild(modifiers);
        assertEquals(ScopeUtils.getSurroundingScope(node), Scope.PRIVATE);
        
        // Test with interface declaration
        node = createDetailAST();
        node.addChild(createASTAccordingToType(15)); // Interface declaration
        modifiers = createDetailAST();
        modifiers.addChild(createAST("protected"));
        node.addChild(modifiers);
        assertEquals(ScopeUtils.getSurroundingScope(node), Scope.PROTECTED);
        
        // Test with anonymous inner class declaration
        node = createDetailAST();
        node.addChild(createASTAccordingToType(136)); // Anonymous inner class declaration
        assertEquals(ScopeUtils.getSurroundingScope(node), Scope.ANONINNER);
    }
    
    // Test isInInterfaceBlock method
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createDetailAST();
        
        // Test with node in an interface block
        node.addChild(createASTAccordingToType(15)); // Interface declaration
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        // Test with node not in an interface block
        node = createDetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    // Test isInAnnotationBlock method
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createDetailAST();
        
        // Test with node in an annotation block
        node.addChild(createASTAccordingToType(157)); // Annotation declaration
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        // Test with node not in an annotation block
        node = createDetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    // Test isInInterfaceOrAnnotationBlock method
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createDetailAST();
        
        // Test with node in an interface block
        node.addChild(createASTAccordingToType(15)); // Interface declaration
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test with node in an annotation block
        node = createDetailAST();
        node.addChild(createASTAccordingToType(157)); // Annotation declaration
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test with node not in an interface or annotation block
        node = createDetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    // Test isInEnumBlock method
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createDetailAST();
        
        // Test with node in an enum block
        node.addChild(createASTAccordingToType(154)); // Enum declaration
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        // Test with node not in an enum block
        node = createDetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    // Test isInCodeBlock method
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createDetailAST();
        
        // Test with node in a code block
        node.addChild(createASTAccordingToType(9)); // If statement
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        // Test with node not in a code block
        node = createDetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    // Test isOuterMostType method
    @Test
    public void testIsOuterMostType() {
        DetailAST node = createDetailAST();
        
        // Test with node as outer most type
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        // Test with node not as outer most type
        DetailAST parent = createDetailAST();
        parent.addChild(createASTAccordingToType(15)); // Interface declaration
        parent.addChild(node);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }
    
    // Test isLocalVariableDef method
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createDetailAST();
        
        // Test with node as local variable definition
        node.setType(10); // Variable definition
        DetailAST parent = createDetailAST();
        parent.setType(7); // Method definition
        parent.addChild(node);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        // Test with node not as local variable definition
        node = createDetailAST();
        node.setType(11); // Field definition
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    // Test isClassFieldDef method
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createDetailAST();
        
        // Test with node as class field definition
        node.setType(10); // Variable definition
        DetailAST parent = createDetailAST();
        parent.addChild(node);
        assertTrue(ScopeUtils.isClassFieldDef(node));
        
        // Test with node not as class field definition
        node = createDetailAST();
        node.setType(9); // Local variable reference
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    // Test isInScope method
    @Test
    public void testIsInScope() {
        DetailAST ast = createDetailAST();
        
        // Test with ast in the given scope
        DetailAST surroundingAst = createDetailAST();
        surroundingAst.addChild(createAST("public"));
        Scope scope = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast, scope));
        
        // Test with ast not in the given scope
        ast = createDetailAST();
        surroundingAst = createDetailAST();
        surroundingAst.addChild(createAST("private"));
        scope = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }
    
    // Helper method to create DetailAST with the provided type
    private DetailAST createASTAccordingToType(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }
    
    // Helper method to create DetailAST with text value
    private DetailAST createAST(String text) {
        DetailAST ast = new DetailAST();
        ast.setText(text);
        return ast;
    }
    
    // Helper method to create DetailAST
    private DetailAST createDetailAST() {
        return new DetailAST();
    }
}



// List of public methods in the ScopeUtils class:

// 1. public static Scope getScopeFromMods(final DetailAST aMods)
// 2. public static Scope getSurroundingScope(final DetailAST node)
// 3. public static boolean isInInterfaceBlock(final DetailAST node)
// 4. public static boolean isInAnnotationBlock(final DetailAST node)
// 5. public static boolean isInInterfaceOrAnnotationBlock(final DetailAST node)
// 6. public static boolean isInEnumBlock(final DetailAST node)
// 7. public static boolean isInCodeBlock(final DetailAST node)
// 8. public static boolean isOuterMostType(final DetailAST node)
// 9. public static boolean isLocalVariableDef(final DetailAST node)
// 10. public static boolean isClassFieldDef(final DetailAST node)
// 11. public static boolean isInScope(final DetailAST ast, final Scope scope)

// JUnit 4 test cases to test each method's functionality, edge cases, and exception handling scenarios

// ...

