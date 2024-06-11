import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Typical use case
        DetailAST aMods = new DetailAST();
        aMods.setType(14); // "public"
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(expected, actual);
        
        // Edge case
        aMods.setType(157); // An invalid mod
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(expected, actual);
        
        // Error scenario
        aMods = null;
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        // Typical use case
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15); // Class declaration
        node.setParent(parent);
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
        
        // Edge case
        parent.setType(136); // Anonymous inner class
        expected = Scope.ANONINNER;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
        
        // Error scenario
        node = null;
        expected = null;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical use case
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15); // Interface declaration
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);

        // Edge case
        parent.setType(14); // Class declaration
        expected = false;
        actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Typical use case
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157); // Annotation declaration
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);

        // Edge case
        parent.setType(15); // Interface declaration
        expected = false;
        actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical use cases
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        
        parent.setType(15); // Interface declaration
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
        
        parent.setType(157); // Annotation declaration
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        // Edge cases
        parent.setType(14); // Class declaration
        expected = false;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        parent.setType(154); // Enum declaration
        expected = false;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Typical use case
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154); // Enum declaration
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);

        // Edge case
        parent.setType(15); // Interface declaration
        expected = false;
        actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Typical use cases
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        
        parent.setType(8); // Static initializer
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
        
        parent.setType(9); // Instance initializer
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
        
        parent.setType(11); // Method declaration
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
        
        parent.setType(12); // Constructor declaration
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        // Edge cases
        parent.setType(14); // Class declaration
        expected = false;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        parent.setType(15); // Interface declaration
        expected = false;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        parent.setType(157); // Annotation declaration
        expected = false;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        parent.setType(154); // Enum declaration
        expected = false;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOuterMostType() {
        // Typical use case
        DetailAST node = new DetailAST();
        
        DetailAST parent = new DetailAST();
        parent.setType(14); // Class declaration
        node.setParent(parent);
        boolean expected = false;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
        
        parent.setType(15); // Interface declaration
        node.setParent(parent);
        expected = false;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
        
        parent.setType(157); // Annotation declaration
        node.setParent(parent);
        expected = false;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
        
        parent.setType(154); // Enum declaration
        node.setParent(parent);
        expected = false;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);

        // Edge case
        parent = null;
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = true;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical use cases
        DetailAST node = new DetailAST();
        
        DetailAST parent = new DetailAST();
        parent.setType(7); // Variable definition in a method
        node.setParent(parent);
        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
        
        parent.setType(35); // Variable definition as a parameter
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
        
        parent.setType(156); // Variable definition in a for loop
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
        
        parent.setType(96); // Resource in a try-with-resources
        node.setType(21); // Resource definition
        node.setParent(parent);
        expected = true;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        // Edge cases
        parent.setType(14); // Class declaration
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        parent.setType(15); // Interface declaration
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        parent.setType(157); // Annotation declaration
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        parent.setType(154); // Enum declaration
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Typical use case
        DetailAST node = new DetailAST();
        node.setType(10); // Class field declaration
        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
        
        // Edge case
        node.setType(10); // Local variable declaration
        expected = false;
        actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);

        // Error scenario
        node = null;
        expected = false;
        actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope() {
        // Typical use case
        DetailAST ast = new DetailAST();
        DetailAST token = new DetailAST();
        DetailAST parent = new DetailAST();
        
        parent.setType(14); // Class declaration
        token.setParent(parent);
        ast.setParent(token);
        ast.setType(9); // Instance initializer - method inside a class
        Scope scope = Scope.PACKAGE;
        boolean expected = true;
        boolean actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
        
        // Edge case
        scope = Scope.PUBLIC;
        expected = false;
        actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
        
        // Error scenario
        ast = null;
        expected = false;
        actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
    }
}