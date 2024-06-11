package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST firstChild = new DetailAST();
        DetailAST secondChild = new DetailAST();
        DetailAST thirdChild = new DetailAST();
        
        firstChild.setType(DetailAST.IDENT);
        firstChild.setText("public");
        
        secondChild.setType(DetailAST.IDENT);
        secondChild.setText("protected");
        
        thirdChild.setType(DetailAST.IDENT);
        thirdChild.setText("private");
        
        DetailAST ast = new DetailAST();
        ast.addChild(firstChild);
        
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
        ast.addChild(secondChild);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
        ast.addChild(thirdChild);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
        ast.removeChild(thirdChild);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
        
        ast.removeChild(secondChild);
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
        
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        DetailAST grandParent = new DetailAST();
        
        parent.setType(DetailAST.MODIFIERS);
        parent.addChild(ast);
        
        grandParent.addChild(parent);
        
        ast.setParent(parent);
        parent.setParent(grandParent);
        
        assertNull(ScopeUtils.getSurroundingScope(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast));
        
        grandParent.setType(DetailAST.LITERAL_int);
        grandParent.addChild(ast);
        
        parent.setType(DetailAST.MODIFIERS);
        parent.addChild(grandParent);
        parent.setParent(ast);
        
        ast.setParent(parent);
        
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(ast));
        
        DetailAST nestedClass = new DetailAST();
        nestedClass.setType(DetailAST.LITERAL_class);
        
        ast.setParent(nestedClass);
        
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));
        
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        
        parent.setType(DetailAST.LITERAL_class);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
        
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        
        parent.setType(DetailAST.LITERAL_class);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
        
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        DetailAST parent = new DetailAST();
        parent.setType(DetailAST.LITERAL_class);
        
        ast.setParent(parent);
        
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));
        
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        
        parent.setType(DetailAST.LITERAL_class);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertTrue(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
        
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        
        parent.setType(DetailAST.LITERAL_class);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_for);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_while);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_if);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
        parent.setType(DetailAST.LITERAL_else);
        assertTrue(ScopeUtils.isInCodeBlock(ast));
        
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        
        parent.setType(DetailAST.LITERAL_class);
        assertTrue(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_interface);
        assertTrue(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_enum);
        assertTrue(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_annotation);
        assertTrue(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_protected);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_private);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
        parent.setType(DetailAST.LITERAL_222);
        assertFalse(ScopeUtils.isOuterMostType(ast));
        
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        ast.setType(DetailAST.LITERAL_public);
        
        parent.setType(DetailAST.LITERAL_class);
        
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        parent.setType(DetailAST.LITERAL_int);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        parent.setType(DetailAST.VARIABLE_DEF);
        assertFalse(ScopeUtils.isLocalVariableDef(ast));
        
        parent.setType(DetailAST.LITERAL_int);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertTrue(ScopeUtils.isLocalVariableDef(ast));
        
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        
        ast.setParent(parent);
        ast.setType(DetailAST.LITERAL_public);
        
        parent.setType(DetailAST.LITERAL_class);
        
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        parent.setType(DetailAST.LITERAL_int);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        parent.setType(DetailAST.VARIABLE_DEF);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
        parent.setType(DetailAST.LITERAL_int);
        assertTrue(ScopeUtils.isClassFieldDef(ast));
        
        parent.setType(DetailAST.LITERAL_public);
        assertTrue(ScopeUtils.isClassFieldDef(ast));
        
        ast.setType(DetailAST.INC);
        assertFalse(ScopeUtils.isClassFieldDef(ast));
        
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        DetailAST parent = new DetailAST();
        parent.setType(DetailAST.MODIFIERS);
        
        DetailAST grandParent = new DetailAST();
        grandParent.setType(DetailAST.LITERAL_class);
        
        grandParent.addChild(parent);
        parent.addChild(ast);
        
        ast.setParent(parent);
        parent.setParent(grandParent);
        
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        parent.setType(DetailAST.LITERAL_public);
        
        assertTrue(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        parent.setType(DetailAST.LITERAL_protected);
        
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
    }
}