import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        // typical use case: checking if scope for public access modifier is returned as PUBLIC
        DetailAST mods = new DetailAST();
        mods.addChild(new AST("public"));
        Scope expected = Scope.PUBLIC;
        
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        
        assertEquals(expected, actual);
        
        // edge case: checking if scope for no access modifier is returned as PACKAGE
        mods = new DetailAST();
        expected = Scope.PACKAGE;
        
        actual = ScopeUtils.getScopeFromMods(mods);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if scope for unknown access modifier is returned as PACKAGE
        mods = new DetailAST();
        mods.addChild(new AST("unknown"));
        expected = Scope.PACKAGE;
        
        actual = ScopeUtils.getScopeFromMods(mods);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // typical use case: checking if surrounding scope for a class is returned as PUBLIC
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        Scope expected = Scope.PUBLIC;
        
        Scope actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if surrounding scope for an anonymous inner class is returned as ANONINNER
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        node.setParent(parent);
        expected = Scope.ANONINNER;
        
        actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if surrounding scope for unknown type is returned as null
        node = new DetailAST();
        expected = null;
        
        actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // typical use case: checking if a node is inside an interface block
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not inside an interface block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isInInterfaceBlock(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        node = new DetailAST();
        expected = false;
        
        actual = ScopeUtils.isInInterfaceBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // typical use case: checking if a node is inside an annotation block
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not inside an annotation block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isInAnnotationBlock(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        node = new DetailAST();
        expected = false;
        
        actual = ScopeUtils.isInAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // typical use case: checking if a node is inside an interface or annotation block
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is inside an interface block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        expected = true;
        
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any interface or annotation block
        node = new DetailAST();
        expected = false;
        
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // typical use case: checking if a node is inside an enum block
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInEnumBlock(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not inside an enum block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isInEnumBlock(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        node = new DetailAST();
        expected = false;
        
        actual = ScopeUtils.isInEnumBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInCodeBlock() {
        // typical use case: checking if a node is inside a code block
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInCodeBlock(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not inside a code block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(136);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isInCodeBlock(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        node = new DetailAST();
        expected = false;
        
        actual = ScopeUtils.isInCodeBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOuterMostType() {
        // typical use case: checking if a node is the outermost type
        DetailAST node = new DetailAST();
        boolean expected = true;
        
        boolean actual = ScopeUtils.isOuterMostType(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not the outermost type
        node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isOuterMostType(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        node = new DetailAST();
        expected = true;
        
        actual = ScopeUtils.isOuterMostType(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // typical use case: checking if a node is a local variable definition
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not a local variable definition
        node = new DetailAST();
        node.setType(5);
        parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isLocalVariableDef(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not a local variable definition or field definition
        node = new DetailAST();
        node.setType(32);
        expected = false;
        
        actual = ScopeUtils.isLocalVariableDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsClassFieldDef() {
        // typical use case: checking if a node is a class field definition
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(20);
        node.setParent(parent);
        boolean expected = true;
        
        boolean actual = ScopeUtils.isClassFieldDef(node);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not a class field definition
        node = new DetailAST();
        node.setType(20);
        parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        expected = false;
        
        actual = ScopeUtils.isClassFieldDef(node);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not a class field definition or local variable definition
        node = new DetailAST();
        node.setType(32);
        expected = false;
        
        actual = ScopeUtils.isClassFieldDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope() {
        // typical use case: checking if a node is in the specified scope
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PUBLIC;
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInScope(ast, scope);
        
        assertEquals(expected, actual);
        
        // edge case: checking if a node is not in the specified scope
        ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        scope = Scope.PUBLIC;
        expected = false;
        
        actual = ScopeUtils.isInScope(ast, scope);
        
        assertEquals(expected, actual);
        
        // error scenario: checking if a node is not inside any block
        ast = new DetailAST();
        scope = Scope.PACKAGE;
        expected = false;
        
        actual = ScopeUtils.isInScope(ast, scope);
        
        assertEquals(expected, actual);
    }
}