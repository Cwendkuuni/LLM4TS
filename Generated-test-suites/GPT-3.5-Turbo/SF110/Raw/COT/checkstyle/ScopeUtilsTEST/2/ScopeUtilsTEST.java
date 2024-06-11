import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test when aMods is null
        DetailAST aMods = null;
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        // Test when aMods does not contain any scope modifiers
        aMods = new DetailAST();
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        // Test when aMods contains the "public" modifier
        aMods.addChild(createToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        // Test when aMods contains the "protected" modifier
        aMods = new DetailAST();
        aMods.addChild(createToken("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));

        // Test when aMods contains the "private" modifier
        aMods = new DetailAST();
        aMods.addChild(createToken("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        // Test when node is null
        DetailAST node = null;
        assertNull(ScopeUtils.getSurroundingScope(node));

        // Test when node is a class or interface declaration
        node = new DetailAST();
        node.setType(14);
        node.addChild(createToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));

        // Test when node is an enum declaration
        node = new DetailAST();
        node.setType(154);
        node.addChild(createToken("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));

        // Test when node is an annotation declaration
        node = new DetailAST();
        node.setType(157);
        node.addChild(createToken("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

        // Test when node is an anonymous inner class declaration
        node = new DetailAST();
        node.setType(136);
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        // Test when node is inside an interface declaration
        node = new DetailAST();
        node.setParent(createToken("interface"));
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        // Test when node is inside a class declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        // Test when node is inside a method
        node = new DetailAST();
        node.setParent(createToken("method"));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        // Test when node is inside an annotation
        node = new DetailAST();
        node.setParent(createToken("@interface"));
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        // Test when node is inside an annotation declaration
        node = new DetailAST();
        node.setParent(createToken("@interface"));
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        // Test when node is inside a class declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        // Test when node is inside a method
        node = new DetailAST();
        node.setParent(createToken("method"));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        // Test when node is inside an interface
        node = new DetailAST();
        node.setParent(createToken("interface"));
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Test when node is inside an interface declaration
        node = new DetailAST();
        node.setParent(createToken("interface"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Test when node is inside an annotation declaration
        node = new DetailAST();
        node.setParent(createToken("@interface"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Test when node is inside a class declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Test when node is inside a method
        node = new DetailAST();
        node.setParent(createToken("method"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isInEnumBlock(node));

        // Test when node is inside an enum declaration
        node = new DetailAST();
        node.setParent(createToken("enum"));
        assertTrue(ScopeUtils.isInEnumBlock(node));

        // Test when node is inside a class declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isInEnumBlock(node));

        // Test when node is inside a method
        node = new DetailAST();
        node.setParent(createToken("method"));
        assertFalse(ScopeUtils.isInEnumBlock(node));

        // Test when node is inside an interface declaration
        node = new DetailAST();
        node.setParent(createToken("interface"));
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isInCodeBlock(node));

        // Test when node is inside a code block
        node = new DetailAST();
        node.setParent(createToken("block"));
        assertTrue(ScopeUtils.isInCodeBlock(node));

        // Test when node is inside a class declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isInCodeBlock(node));

        // Test when node is inside an interface declaration
        node = new DetailAST();
        node.setParent(createToken("interface"));
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        // Test when node is null
        DetailAST node = null;
        assertTrue(ScopeUtils.isOuterMostType(node));

        // Test when node is inside a class or interface declaration
        node = new DetailAST();
        node.setParent(createToken("class"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        // Test when node is inside an enum declaration
        node = new DetailAST();
        node.setParent(createToken("enum"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        // Test when node is inside an annotation declaration
        node = new DetailAST();
        node.setParent(createToken("@interface"));
        assertFalse(ScopeUtils.isOuterMostType(node));

        // Test when node is inside an anonymous inner class declaration
        node = new DetailAST();
        node.setParent(createToken("anonymous"));
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        // Test when node is null
        DetailAST node = null;
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        // Test when node is a local variable declaration
        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        // Test when node is a parameter declaration inside a lambda expression
        node = new DetailAST();
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        // Test when node is not a local variable declaration or parameter declaration
        node = new DetailAST();
        node.setType(9);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        // Test when node is a class field definition
        DetailAST node = new DetailAST();
        node.setType(10);
        assertTrue(ScopeUtils.isClassFieldDef(node));

        // Test when node is a local variable definition
        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        // Test when node is not a field definition
        node = new DetailAST();
        node.setType(9);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        // Test when ast is null
        DetailAST ast = null;
        Scope scope = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        // Test when ast is in the specified scope
        ast = new DetailAST();
        ast.setParent(createToken("class"));
        ast.addChild(createToken("public"));
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));

        // Test when ast is not in the specified scope
        ast = new DetailAST();
        ast.setParent(createToken("class"));
        ast.addChild(createToken("private"));
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
    }

    private DetailAST createToken(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
}