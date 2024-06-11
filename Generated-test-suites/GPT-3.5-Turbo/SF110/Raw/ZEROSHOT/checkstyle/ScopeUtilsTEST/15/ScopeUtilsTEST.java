package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_PublicScope_ReturnsPublic() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(createToken("public"));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PUBLIC;

        ScopeUtils.Scope actualScope = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_ProtectedScope_ReturnsProtected() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(createToken("protected"));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PROTECTED;

        ScopeUtils.Scope actualScope = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_PrivateScope_ReturnsPrivate() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(createToken("private"));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PRIVATE;

        ScopeUtils.Scope actualScope = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_NoScopeSpecified_ReturnsPackage() {
        DetailAST aMods = new DetailAST();
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PACKAGE;

        ScopeUtils.Scope actualScope = ScopeUtils.getScopeFromMods(aMods);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ScopeIsClass_ReturnsExpectedScope() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));
        node.getParent().addChild(createToken("public"));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PUBLIC;

        ScopeUtils.Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ScopeIsInterface_ReturnsExpectedScope() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(15));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PACKAGE;

        ScopeUtils.Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ScopeIsEnum_ReturnsExpectedScope() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(154));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PACKAGE;

        ScopeUtils.Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ScopeIsAnnotation_ReturnsExpectedScope() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(157));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.PACKAGE;

        ScopeUtils.Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ScopeIsAnonymousInner_ReturnsExpectedScope() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(136));
        ScopeUtils.Scope expectedScope = ScopeUtils.Scope.ANONINNER;

        ScopeUtils.Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock_NodeIsInInterfaceBlock_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(15));

        boolean result = ScopeUtils.isInInterfaceBlock(node);

        assertTrue(result);
    }

    @Test
    public void testIsInInterfaceBlock_NodeIsNotInInterfaceBlock_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));

        boolean result = ScopeUtils.isInInterfaceBlock(node);

        assertFalse(result);
    }

    @Test
    public void testIsInAnnotationBlock_NodeIsInAnnotationBlock_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(157));

        boolean result = ScopeUtils.isInAnnotationBlock(node);

        assertTrue(result);
    }

    @Test
    public void testIsInAnnotationBlock_NodeIsNotInAnnotationBlock_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));

        boolean result = ScopeUtils.isInAnnotationBlock(node);

        assertFalse(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NodeIsInInterfaceOrAnnotationBlock_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(157));

        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertTrue(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NodeIsNotInInterfaceOrAnnotationBlock_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));

        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertFalse(result);
    }

    @Test
    public void testIsInEnumBlock_NodeIsInEnumBlock_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(154));

        boolean result = ScopeUtils.isInEnumBlock(node);

        assertTrue(result);
    }

    @Test
    public void testIsInEnumBlock_NodeIsNotInEnumBlock_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));

        boolean result = ScopeUtils.isInEnumBlock(node);

        assertFalse(result);
    }

    @Test
    public void testIsInCodeBlock_NodeIsInCodeBlock_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(8));

        boolean result = ScopeUtils.isInCodeBlock(node);

        assertTrue(result);
    }

    @Test
    public void testIsInCodeBlock_NodeIsNotInCodeBlock_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));

        boolean result = ScopeUtils.isInCodeBlock(node);

        assertFalse(result);
    }

    @Test
    public void testIsOuterMostType_NodeIsNotOuterMostType_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(14));
        boolean expectedValue = false;

        boolean actualValue = ScopeUtils.isOuterMostType(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsOuterMostType_NodeIsOuterMostType_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setParent(createParentToken(157));
        boolean expectedValue = true;

        boolean actualValue = ScopeUtils.isOuterMostType(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsLocalVariableDef_NodeIsLocalVariableDef_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.setParent(createParentToken(7));
        boolean expectedValue = true;

        boolean actualValue = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsLocalVariableDef_NodeIsNotLocalVariableDef_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.setParent(createParentToken(14));
        boolean expectedValue = false;

        boolean actualValue = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsClassFieldDef_NodeIsClassFieldDef_ReturnsTrue() {
        DetailAST node = new DetailAST();
        node.setType(10);
        boolean expectedValue = true;

        boolean actualValue = ScopeUtils.isClassFieldDef(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsClassFieldDef_NodeIsNotClassFieldDef_ReturnsFalse() {
        DetailAST node = new DetailAST();
        node.setType(7);
        boolean expectedValue = false;

        boolean actualValue = ScopeUtils.isClassFieldDef(node);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInScope_AstIsInScope_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.setParent(createParentToken(14));
        ScopeUtils.Scope scope = ScopeUtils.Scope.PUBLIC;
        boolean expectedValue = true;

        boolean actualValue = ScopeUtils.isInScope(ast, scope);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInScope_AstIsNotInScope_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        ast.setParent(createParentToken(157));
        ScopeUtils.Scope scope = ScopeUtils.Scope.PUBLIC;
        boolean expectedValue = false;

        boolean actualValue = ScopeUtils.isInScope(ast, scope);

        assertEquals(expectedValue, actualValue);
    }

    private DetailAST createParentToken(int type) {
        DetailAST parent = new DetailAST();
        parent.setType(type);
        return parent;
    }

    private DetailAST createToken(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
}