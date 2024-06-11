import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockDetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(mods);

        // Typical use case: one token with "public"
        mods.addChild(createMockToken("public"));
        assertEquals(Scope.PUBLIC, scope);

        // Edge case: multiple tokens with "public" and "protected"
        mods.addChild(createMockToken("public"));
        mods.addChild(createMockToken("protected"));
        assertEquals(Scope.PUBLIC, scope);

        // Error scenario: no token
        mods = null;
        assertNull(ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createMockDetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(node);

        // Typical use case: parent node is public class declaration
        node.addChild(createMockToken("TestClass"));
        node.setType(15);
        DetailAST modToken = createMockToken("public");
        node.addChild(modToken);
        assertEquals(Scope.PUBLIC, scope);

        // Edge case: parent node is anonymous inner class declaration
        node.setType(136);
        assertEquals(Scope.ANONINNER, scope);

        // Error scenario: no parent node
        node = null;
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createMockDetailAST();
        boolean isInInterface = ScopeUtils.isInInterfaceBlock(node);

        // Typical use case: parent node is interface declaration
        node.setType(15);
        assertTrue(isInInterface);

        // Edge case: parent node is class declaration
        node.setType(14);
        assertFalse(isInInterface);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createMockDetailAST();
        boolean isInAnnotation = ScopeUtils.isInAnnotationBlock(node);

        // Typical use case: parent node is annotation declaration
        node.setType(157);
        assertTrue(isInAnnotation);

        // Edge case: parent node is class declaration
        node.setType(14);
        assertFalse(isInAnnotation);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createMockDetailAST();
        boolean isInInterfaceOrAnnotation = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        // Typical use case: parent node is interface declaration
        node.setType(15);
        assertTrue(isInInterfaceOrAnnotation);

        // Typical use case: parent node is annotation declaration
        node.setType(157);
        assertTrue(isInInterfaceOrAnnotation);

        // Edge case: parent node is class declaration
        node.setType(14);
        assertFalse(isInInterfaceOrAnnotation);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createMockDetailAST();
        boolean isInEnum = ScopeUtils.isInEnumBlock(node);

        // Typical use case: parent node is enum declaration
        node.setType(154);
        assertTrue(isInEnum);

        // Edge case: parent node is class declaration
        node.setType(14);
        assertFalse(isInEnum);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createMockDetailAST();
        boolean isInCode = ScopeUtils.isInCodeBlock(node);

        // Typical use case: parent node is method body
        node.setType(9);
        assertTrue(isInCode);

        // Edge case: parent node is class declaration
        node.setType(14);
        assertFalse(isInCode);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createMockDetailAST();
        boolean isOuterMostType = ScopeUtils.isOuterMostType(node);

        // Typical use case: no parent nodes are public, protected, private, or package
        node.setType(20);
        assertTrue(isOuterMostType);

        // Edge case: parent node is private class declaration
        node.addChild(createMockToken("TestClass"));
        node.setType(14);
        DetailAST modToken = createMockToken("private");
        node.addChild(modToken);
        assertFalse(isOuterMostType);

        // Error scenario: no parent node
        node = null;
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createMockDetailAST();
        boolean isLocalVariable = ScopeUtils.isLocalVariableDef(node);

        // Typical use case: parent node is regular local variable
        node.setType(10);
        DetailAST parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(7);
        assertTrue(isLocalVariable);

        // Typical use case: parent node is enhanced for loop local variable
        node.setType(10);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(35);
        assertTrue(isLocalVariable);

        // Typical use case: parent node is lambda parameter
        node.setType(21);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(96);
        assertTrue(isLocalVariable);

        // Edge case: parent node is method parameter
        node.setType(10);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(96);
        assertFalse(isLocalVariable);

        // Error scenario: parent node has no type
        node.setType(10);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(-1);
        assertFalse(isLocalVariable);

        // Error scenario: node has no parent node
        node.setType(10);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createMockDetailAST();
        boolean isClassField = ScopeUtils.isClassFieldDef(node);

        // Typical use case: parent node is class field declaration
        node.setType(10);
        DetailAST parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(14);
        assertTrue(isClassField);

        // Edge case: parent node is local variable declaration
        node.setType(10);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(7);
        assertFalse(isClassField);

        // Error scenario: parent node has no type
        node.setType(10);
        parent = createMockDetailAST();
        node.setParent(parent);
        parent.setType(-1);
        assertFalse(isClassField);

        // Error scenario: node has no parent node
        node.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createMockDetailAST();
        Scope scope = Scope.PUBLIC;
        boolean isInScope = ScopeUtils.isInScope(ast, scope);

        // Typical use case: surrounding scope is equal to given scope
        ast = createMockDetailAST();
        ast.addChild(createMockToken("public"));
        assertTrue(isInScope);

        // Edge case: surrounding scope is not equal to given scope
        ast = createMockDetailAST();
        ast.addChild(createMockToken("protected"));
        assertFalse(isInScope);

        // Error scenario: ast is null
        ast = null;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

    private DetailAST createMockDetailAST() {
        return new DetailAST();
    }

    private AST createMockToken(String tokenText) {
        AST token = new DetailAST();
        token.setText(tokenText);
        return token;
    }
}