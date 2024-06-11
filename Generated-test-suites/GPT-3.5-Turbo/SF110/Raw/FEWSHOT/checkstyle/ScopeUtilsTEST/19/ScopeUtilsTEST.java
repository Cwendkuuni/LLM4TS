package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast1 = createAst("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast1));

        DetailAST ast2 = createAst("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast2));

        DetailAST ast3 = createAst("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast3));

        DetailAST ast4 = createAst("package");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast4));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        ast1.addChild(createAst("public"));
        ast1.addChild(createAst("protected"));
        ast1.addChild(createAst("private"));

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast1));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast2));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast3));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast4));
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast5));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertFalse(ScopeUtils.isInInterfaceBlock(ast1));
        assertTrue(ScopeUtils.isInInterfaceBlock(ast2));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast3));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast4));
        assertFalse(ScopeUtils.isInInterfaceBlock(ast5));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertFalse(ScopeUtils.isInAnnotationBlock(ast1));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast2));
        assertTrue(ScopeUtils.isInAnnotationBlock(ast3));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast4));
        assertFalse(ScopeUtils.isInAnnotationBlock(ast5));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast1));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast2));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast3));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast4));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast5));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertFalse(ScopeUtils.isInEnumBlock(ast1));
        assertFalse(ScopeUtils.isInEnumBlock(ast2));
        assertFalse(ScopeUtils.isInEnumBlock(ast3));
        assertTrue(ScopeUtils.isInEnumBlock(ast4));
        assertFalse(ScopeUtils.isInEnumBlock(ast5));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast1 = createAstWithType(9);
        DetailAST ast2 = createAstWithType(8);
        DetailAST ast3 = createAstWithType(11);
        DetailAST ast4 = createAstWithType(12);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);

        assertTrue(ScopeUtils.isInCodeBlock(ast1));
        assertTrue(ScopeUtils.isInCodeBlock(ast2));
        assertTrue(ScopeUtils.isInCodeBlock(ast3));
        assertTrue(ScopeUtils.isInCodeBlock(ast4));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast1 = createAstWithType(14);
        DetailAST ast2 = createAstWithType(15);
        DetailAST ast3 = createAstWithType(157);
        DetailAST ast4 = createAstWithType(154);
        DetailAST ast5 = createAstWithType(136);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);
        ast.addChild(ast4);
        ast.addChild(ast5);

        assertTrue(ScopeUtils.isOuterMostType(ast1));
        assertFalse(ScopeUtils.isOuterMostType(ast2));
        assertFalse(ScopeUtils.isOuterMostType(ast3));
        assertFalse(ScopeUtils.isOuterMostType(ast4));
        assertFalse(ScopeUtils.isOuterMostType(ast5));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast1 = createAstWithType(10);
        DetailAST ast2 = createAstWithType(21);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);

        assertFalse(ScopeUtils.isLocalVariableDef(ast1));
        assertFalse(ScopeUtils.isLocalVariableDef(ast2));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast1 = createAstWithType(10);
        DetailAST ast2 = createAstWithType(21);

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);

        assertTrue(ScopeUtils.isClassFieldDef(ast1));
        assertFalse(ScopeUtils.isClassFieldDef(ast2));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast1 = createAst("test1");
        DetailAST ast2 = createAst("test2");
        DetailAST ast3 = createAst("test3");

        DetailAST ast = createAst("test");
        ast.addChild(ast1);
        ast.addChild(ast2);
        ast.addChild(ast3);

        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast1));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast2));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast3));
    }

    private DetailAST createAst(String text) {
        DetailAST ast = new DetailAST();
        ast.setType(0);
        ast.setText(text);
        return ast;
    }

    private DetailAST createAstWithType(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }
}