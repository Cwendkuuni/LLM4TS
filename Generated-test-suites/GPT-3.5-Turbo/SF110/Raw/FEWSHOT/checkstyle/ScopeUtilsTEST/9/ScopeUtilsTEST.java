package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = null;
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));

        aMods = new DetailAST();
        aMods.addChild(new DetailAST());

        DetailAST token = aMods.getFirstChild();
        token.setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));

        token.setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));

        token.setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = null;
        assertNull(ScopeUtils.getSurroundingScope(node));

        node = new DetailAST();

        DetailAST token = new DetailAST();
        token.setType(14);
        node.addChild(token);

        DetailAST mods = new DetailAST();
        token.addChild(mods);

        token = new DetailAST();
        token.setType(11);
        node.addChild(token);

        token = new DetailAST();
        token.setType(12);
        node.addChild(token);

        token = new DetailAST();
        token.setType(136);
        node.addChild(token);

        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));

        mods.addChild(new DetailAST());
        token.setType(14);
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));

        mods.addChild(new DetailAST());
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));

        token.setType(15);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = new DetailAST();
        DetailAST token = new DetailAST();

        token.setType(15);
        node.addChild(token);

        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        token.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        token.setType(154);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        token.setType(136);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = new DetailAST();
        DetailAST token = new DetailAST();

        token.setType(157);
        node.addChild(token);

        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        token.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        token.setType(154);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        token.setType(15);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        token.setType(136);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        DetailAST token = new DetailAST();
        token.setType(157);
        node = new DetailAST();
        node.addChild(token);

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        token.setType(14);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        token.setType(154);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        token.setType(15);
        node = new DetailAST();
        node.addChild(token);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        token.setType(136);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isInEnumBlock(node));

        DetailAST token = new DetailAST();
        token.setType(154);
        node = new DetailAST();
        node.addChild(token);

        assertTrue(ScopeUtils.isInEnumBlock(node));

        token.setType(15);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        token.setType(157);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        token.setType(14);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        token.setType(136);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isInCodeBlock(node));

        DetailAST token = new DetailAST();
        token.setType(9);
        node = new DetailAST();
        node.addChild(token);

        assertTrue(ScopeUtils.isInCodeBlock(node));

        token.setType(8);
        node = new DetailAST();
        node.addChild(token);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        token.setType(11);
        node = new DetailAST();
        node.addChild(token);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        token.setType(12);
        node = new DetailAST();
        node.addChild(token);
        assertTrue(ScopeUtils.isInCodeBlock(node));

        token.setType(14);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        token.setType(15);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        token.setType(157);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        token.setType(154);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        token.setType(136);
        node = new DetailAST();
        node.addChild(token);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = null;
        assertTrue(ScopeUtils.isOuterMostType(node));

        DetailAST parent = new DetailAST();
        DetailAST token = new DetailAST();
        token.setType(14);
        parent.addChild(token);

        token = new DetailAST();
        token.setType(15);
        parent.addChild(token);

        token = new DetailAST();
        token.setType(157);
        parent.addChild(token);

        token = new DetailAST();
        token.setType(154);
        parent.addChild(token);

        token = new DetailAST();
        token.setType(136);
        parent.addChild(token);

        node = new DetailAST();
        node.addChild(parent);

        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = new DetailAST();
        node.setType(10);

        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        parent.setType(35);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        parent.setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        parent.setType(96);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = null;
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        node.setParent(parent);

        assertFalse(ScopeUtils.isClassFieldDef(node));

        parent.setType(7);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        parent.setType(35);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        parent.setType(156);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        parent.setType(96);
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = null;
        Scope scope = Scope.PACKAGE;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = new DetailAST();
        assertNull(ScopeUtils.isInScope(ast, scope));

        ScopeUtils.getSurroundingScope(ast);
        assertFalse(ScopeUtils.isInScope(ast, scope));

        DetailAST token = new DetailAST();
        token.setType(14);
        ast.addChild(token);

        DetailAST mods = new DetailAST();
        token.addChild(mods);

        token = new DetailAST();
        token.setType(136);
        ast.addChild(token);

        assertTrue(ScopeUtils.isInScope(ast, scope));

        token = new DetailAST();
        token.setType(7);
        ast.addChild(token);

        assertFalse(ScopeUtils.isInScope(ast, Scope.PROTECTED));

        assertTrue(ScopeUtils.isInScope(ast, Scope.PRIVATE));
    }
}