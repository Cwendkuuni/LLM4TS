package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        DetailAST child1 = new DetailAST();
        child1.setType(15);
        child1.setText("public");
        ast.addChild(child1);
        DetailAST child2 = new DetailAST();
        child2.setType(14);
        child2.setText("protected");
        ast.addChild(child2);
        DetailAST child3 = new DetailAST();
        child3.setType(154);
        child3.setText("private");
        ast.addChild(child3);

        Scope scope = ScopeUtils.getScopeFromMods(ast);

        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        DetailAST parent1 = new DetailAST();
        parent1.setType(14);
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        parent1.addChild(parent2);
        ast.setParent(parent1);

        Scope scope = ScopeUtils.getSurroundingScope(ast);

        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(54);

        boolean result = ScopeUtils.isInInterfaceBlock(ast);

        assertFalse(result);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(157);

        boolean result = ScopeUtils.isInAnnotationBlock(ast);

        assertTrue(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(54);

        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);

        assertFalse(result);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(154);

        boolean result = ScopeUtils.isInEnumBlock(ast);

        assertTrue(result);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(11);

        boolean result = ScopeUtils.isInCodeBlock(ast);

        assertTrue(result);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        ast.setType(156);
        DetailAST parent1 = new DetailAST();
        parent1.setType(14);
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        parent1.addChild(parent2);
        ast.setParent(parent1);

        boolean result = ScopeUtils.isOuterMostType(ast);

        assertFalse(result);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.setType(96);

        boolean result = ScopeUtils.isLocalVariableDef(ast);

        assertTrue(result);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10);

        boolean result = ScopeUtils.isClassFieldDef(ast);

        assertTrue(result);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(11);
        Scope scope = Scope.PUBLIC;

        boolean result = ScopeUtils.isInScope(ast, scope);

        assertFalse(result);
    }
}