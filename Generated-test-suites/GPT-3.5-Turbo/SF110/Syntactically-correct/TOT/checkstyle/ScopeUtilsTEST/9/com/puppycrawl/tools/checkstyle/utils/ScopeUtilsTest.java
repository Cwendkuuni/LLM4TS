import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils.Scope;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        // Typical use case
        aMods.addChild(createToken("public"));
        Scope expected = Scope.PUBLIC;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));
        
        // Edge case: No access modifier found
        aMods = new DetailAST();
        expected = Scope.PACKAGE;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        // Typical use case
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(createModsToken("private"));
        node.setParent(parent);
        Scope expected = Scope.PRIVATE;
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));
        
        // Error scenario: No parent node
        node = new DetailAST();
        Scope expected = null;
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        // Typical use case
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        // Error scenario: Not in an interface block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        // Typical use case
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        // Error scenario: Not in an annotation block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        // Typical use case: In an interface block
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Typical use case: In an annotation block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Error scenario: Not in an interface or annotation block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        // Typical use case
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        // Error scenario: Not in an enum block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        // Typical use case: In a code block
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        // Error scenario: Not in a code block
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        // Typical use case: Outermost type
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        // Error scenario: Not outermost type
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        // Typical use case: Local variable definition
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        // Error scenario: Not a local variable definition
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        // Typical use case: Class field definition
        DetailAST parent = new DetailAST();
        parent.setType(10);
        node.setParent(parent);
        assertTrue(ScopeUtils.isClassFieldDef(node));
        
        // Error scenario: Not a class field definition
        node = new DetailAST();
        parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PUBLIC;
        
        // Typical use case: In desired scope
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(createModsToken("public"));
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInScope(ast, scope));
        
        // Typical use case: Not in desired scope
        ast = new DetailAST();
        parent = new DetailAST();
        parent.setType(15);
        parent.addChild(createModsToken("private"));
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }
    
    // Create a token for testing
    private DetailAST createToken(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
    
    // Create a modifier token for testing
    private DetailAST createModsToken(String modifier) {
        DetailAST mods = new DetailAST();
        mods.addChild(createToken(modifier));
        return mods;
    }
}