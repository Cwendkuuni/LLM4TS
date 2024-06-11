package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        DetailAST protectedToken = new DetailAST();
        protectedToken.setText("protected");
        DetailAST privateToken = new DetailAST();
        privateToken.setText("private");

        ast.addChild(publicToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));

        ast.addChild(publicToken);
        ast.addChild(protectedToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));

        ast.addChild(privateToken);
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        ast.setType(14); // Class definition
        ScopeUtils.getSurroundingScope(ast);

        ast.setType(15); // Interface definition
        ScopeUtils.getSurroundingScope(ast);

        ast.setType(157); // Annotation definition
        ScopeUtils.getSurroundingScope(ast);

        ast.setType(154); // Enum definition
        ScopeUtils.getSurroundingScope(ast);

        ast.setType(136); // Anonymous inner class
        ScopeUtils.getSurroundingScope(ast);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15); // Interface definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        parent.setType(14); // Class definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        parent.setType(154); // Enum definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        parent.setType(157); // Annotation definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157); // Annotation definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        parent.setType(14); // Class definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        parent.setType(154); // Enum definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        parent.setType(15); // Interface definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157); // Annotation definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        parent.setType(15); // Interface definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        parent.setType(14); // Class definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        parent.setType(154); // Enum definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154); // Enum definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        parent.setType(15); // Interface definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        parent.setType(157); // Annotation definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        parent.setType(14); // Class definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9); // Block
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        parent.setType(8); // If statement
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        parent.setType(11); // For statement
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        parent.setType(12); // While statement
        ast.setParent(parent);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        parent.setType(10); // Method definition
        ast.setParent(parent);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14); // Class definition
        DetailAST grandparent = new DetailAST();
        grandparent.setParent(ast);
        grandparent.setType(15); // Interface definition
        ast.setParent(parent);
        assertTrue(ScopeUtils.isOuterMostType(ast));

        parent.setParent(grandparent);
        assertFalse(ScopeUtils.isOuterMostType(ast));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10); // Variable definition
        DetailAST parent = new DetailAST();
        parent.setType(7); // Local variable block
        ast.setParent(parent);

        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        parent.setType(35); // Method parameter block
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        parent.setType(156); // Catch block
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        parent.setType(96); // Array definition
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10); // Variable definition
        DetailAST parent = new DetailAST();
        parent.setType(7); // Local variable block
        ast.setParent(parent);

        assertFalse(ScopeUtils.isClassFieldDef(ast));

        parent.setType(35); // Method parameter block
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        parent.setType(156); // Catch block
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        parent.setType(96); // Array definition
        assertTrue(ScopeUtils.isClassFieldDef(ast));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        DetailAST surroundingScopeAst = new DetailAST();
        surroundingScopeAst.setType(15); // Interface definition
        ScopeUtils.getSurroundingScope(ast);
        Scope scope = Scope.ANONINNER;
        assertTrue(ScopeUtils.isInScope(ast, scope));

        scope = Scope.PRIVATE;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

}