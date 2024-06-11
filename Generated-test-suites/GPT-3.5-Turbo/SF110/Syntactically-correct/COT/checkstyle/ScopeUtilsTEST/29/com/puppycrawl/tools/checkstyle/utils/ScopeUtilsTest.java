
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(createTokenNode("public"));
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("class"));
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("interface"));
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("@interface"));
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("@interface"));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("enum"));
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("{"));
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("int"));
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.addChild(createTokenNode("int"));
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(ast);
        assertTrue(ScopeUtils.isInScope(ast, scope));
    }

    private DetailAST createTokenNode(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }

}
