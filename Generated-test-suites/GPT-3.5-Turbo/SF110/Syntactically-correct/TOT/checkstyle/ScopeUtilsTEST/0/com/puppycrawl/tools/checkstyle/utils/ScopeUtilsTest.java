package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Typical use case - token is public
        DetailAST aMods = createDetailAST("public");
        Scope expected = Scope.PUBLIC;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));

        // Typical use case - token is protected
        aMods = createDetailAST("protected");
        expected = Scope.PROTECTED;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));

        // Typical use case - token is private
        aMods = createDetailAST("private");
        expected = Scope.PRIVATE;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));

        // Edge case - token is null
        aMods = null;
        expected = Scope.PACKAGE;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));

        // Error scenario - token is not public, protected, or private
        aMods = createDetailAST("static");
        expected = Scope.PACKAGE;
        assertEquals(expected, ScopeUtils.getScopeFromMods(aMods));
    }

    @Test
    public void testGetSurroundingScope() {
        // Typical use case - token is of type CLASS_DEF
        DetailAST node = createDetailASTWithType("CLASS_DEF");
        Scope expected = Scope.PACKAGE; // Assuming surrounding scope is PACKAGE
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));

        // Typical use case - token is of type METHOD_DEF
        node = createDetailASTWithType("METHOD_DEF");
        expected = Scope.PUBLIC; // Assuming surrounding scope is PUBLIC
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));

        // Typical use case - token is of type INTERFACE_DEF
        node = createDetailASTWithType("INTERFACE_DEF");
        expected = Scope.PROTECTED; // Assuming surrounding scope is PROTECTED
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));

        // Typical use case - token is of type ENUM_DEF
        node = createDetailASTWithType("ENUM_DEF");
        expected = Scope.PRIVATE; // Assuming surrounding scope is PRIVATE
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));

        // Edge case - token is null
        node = null;
        expected = null;
        assertEquals(expected, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Typical use case - token is of type INTERFACE_DEF
        DetailAST node = createDetailASTWithType("INTERFACE_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInInterfaceBlock(node));

        // Typical use case - token is of type CLASS_DEF
        node = createDetailASTWithType("CLASS_DEF");
        expected = false;
        assertEquals(expected, ScopeUtils.isInInterfaceBlock(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Typical use case - token is of type ANNOTATION_DEF
        DetailAST node = createDetailASTWithType("ANNOTATION_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInAnnotationBlock(node));

        // Typical use case - token is of type ENUM_DEF
        node = createDetailASTWithType("ENUM_DEF");
        expected = false;
        assertEquals(expected, ScopeUtils.isInAnnotationBlock(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical use case - token is in interface block
        DetailAST node = createDetailASTWithType("INTERFACE_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Typical use case - token is in annotation block
        node = createDetailASTWithType("ANNOTATION_DEF");
        expected = true;
        assertEquals(expected, ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Typical use case - token is not in interface or annotation block
        node = createDetailASTWithType("CLASS_DEF");
        expected = false;
        assertEquals(expected, ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        // Typical use case - token is of type ENUM_DEF
        DetailAST node = createDetailASTWithType("ENUM_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInEnumBlock(node));

        // Typical use case - token is of type CLASS_DEF
        node = createDetailASTWithType("CLASS_DEF");
        expected = false;
        assertEquals(expected, ScopeUtils.isInEnumBlock(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        // Typical use case - token is of type SLIST (static initializer)
        DetailAST node = createDetailASTWithType("SLIST");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInCodeBlock(node));

        // Typical use case - token is of type OBJBLOCK (instance initializer)
        node = createDetailASTWithType("OBJBLOCK");
        expected = true;
        assertEquals(expected, ScopeUtils.isInCodeBlock(node));

        // Typical use case - token is of type LITERAL_NEW
        node = createDetailASTWithType("LITERAL_NEW");
        expected = true;
        assertEquals(expected, ScopeUtils.isInCodeBlock(node));

        // Typical use case - token is of type LITERAL_ELSE
        node = createDetailASTWithType("LITERAL_ELSE");
        expected = false;
        assertEquals(expected, ScopeUtils.isInCodeBlock(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        // Typical use case - token is outermost type
        DetailAST node = createDetailASTWithType("CLASS_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isOuterMostType(node));

        // Typical use case - token is not outermost type
        node = createDetailASTWithType("INSTANCE_INIT");
        expected = false;
        assertEquals(expected, ScopeUtils.isOuterMostType(node));

        // Edge case - token is null
        node = null;
        expected = true; // Assuming null is considered as outermost type
        assertEquals(expected, ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        // Typical use case - token is of type VARIABLE_DEF with valid parent type
        DetailAST node = createDetailASTWithType("VARIABLE_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isLocalVariableDef(node));

        // Typical use case - token is of type VARIABLE_DEF with invalid parent type
        node = createDetailASTWithType("VARIABLE_DEF");
        node.getParent().setType(14); // Assuming parent is of type IDENT
        expected = false;
        assertEquals(expected, ScopeUtils.isLocalVariableDef(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        // Typical use case - token is of type VARIABLE_DEF and not local variable def
        DetailAST node = createDetailASTWithType("VARIABLE_DEF");
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isClassFieldDef(node));

        // Typical use case - token is of type VARIABLE_DEF and local variable def
        node = createDetailASTWithType("VARIABLE_DEF");
        node.getParent().setType(14); // Assuming parent is of type IDENT
        expected = false;
        assertEquals(expected, ScopeUtils.isClassFieldDef(node));

        // Edge case - token is null
        node = null;
        expected = false;
        assertEquals(expected, ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        // Typical use case - surrounding scope matches expected scope
        DetailAST ast = createDetailASTWithType("METHOD_DEF");
        Scope scope = Scope.PUBLIC;
        boolean expected = true;
        assertEquals(expected, ScopeUtils.isInScope(ast, scope));

        // Typical use case - surrounding scope does not match expected scope
        ast = createDetailASTWithType("METHOD_DEF");
        scope = Scope.PRIVATE;
        expected = false;
        assertEquals(expected, ScopeUtils.isInScope(ast, scope));

        // Edge case - surrounding scope is null
        ast = null;
        scope = Scope.PUBLIC;
        expected = false;
        assertEquals(expected, ScopeUtils.isInScope(ast, scope));
    }

    // Helper method to create a DetailAST with specified token type
    private DetailAST createDetailASTWithType(String type) {
        DetailAST ast = new DetailAST();
        ast.setType(getTokenType(type));
        ast.setParent(new DetailAST());
        return ast;
    }

    // Helper method to create a DetailAST with specified token text
    private DetailAST createDetailAST(String text) {
        DetailAST ast = new DetailAST();
        ast.setText(text);
        ast.setParent(new DetailAST());
        return ast;
    }

    // Helper method to get token type based on string representation
    private int getTokenType(String type) {
        switch (type) {
            case "CLASS_DEF":
                return 14;
            case "INTERFACE_DEF":
                return 15;
            case "ENUM_DEF":
                return 154;
            case "ANNOTATION_DEF":
                return 157;
            case "METHOD_DEF":
                return 96;
            case "VARIABLE_DEF":
                return 10;
            case "SLIST":
                return 9;
            case "OBJBLOCK":
                return 8;
            case "LITERAL_NEW":
                return 11;
            case "LITERAL_ELSE":
                return 12;
            default:
                return -1;
        }
    }

}