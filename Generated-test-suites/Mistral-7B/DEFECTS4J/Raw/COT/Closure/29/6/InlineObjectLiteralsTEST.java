package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

    @Override
    protected CompilerPass getPass() {
        return new InlineObjectLiterals(compiler, safeNameIdSupplier);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_simpleObject() {
        String jsCode = "var obj = {a: 1, b: 2};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var a = 1,\n" +
                "       b = 2;\n" +
                "var obj = {\n" +
                "  a: a,\n" +
                "  b: b\n" +
                "};", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_complexObject() {
        String jsCode = "var obj = {\n" +
                "  a: 1,\n" +
                "  b: {\n" +
                "    c: 2,\n" +
                "    d: function() {}\n" +
                "  },\n" +
                "  e: [1, 2, 3],\n" +
                "  f: function() {}\n" +
                "};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var a = 1,\n" +
                "       b = {\n" +
                "         c: 2,\n" +
                "         d: function d() {}\n" +
                "       },\n" +
                "       e = [1, 2, 3],\n" +
                "       f = function f() {}\n" +
                "      ;\n" +
                "var obj = {\n" +
                "  a: a,\n" +
                "  b: b,\n" +
                "  e: e\n" +
                "};", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithUndefinedProperty() {
        String jsCode = "var obj = {x: 1, y: undefined};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var x = 1;\n" +
                "var obj = {\n" +
                "  x: x\n" +
                "};", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithFunctionProperty() {
        String jsCode = "var obj = {getA: function() {}};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var getA = function() {},\n" +
                "       obj = {\n" +
                "         getA: getA\n" +
                "       };", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithArrayProperty() {
        String jsCode = "var obj = {arr: [1, 2, 3]};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var arr = [1, 2, 3],\n" +
                "       obj = {\n" +
                "         arr: arr\n" +
                "       };", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithNestedArrayProperty() {
        String jsCode = "var obj = {arr: [1, [2, 3], 4]};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var arr = [1, [2, 3], 4],\n" +
                "       obj = {\n" +
                "         arr: arr\n" +
                "       };", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithCyclicReference() {
        String jsCode = "var obj1 = {x: obj2};\n" +
                "var obj2 = {x: obj1};";
        Node root = parseJsCode(jsCode);
        try {
            processAndCheck(root);
            fail("Expected CompilerException due to cyclic reference");
        } catch (CompilerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithSelfReference() {
        String jsCode = "var obj = {x: function() {return this}};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var obj = {\n" +
                "  x: function x() {\n" +
                "    return x.caller;\n" +
                "  }\n" +
                "};", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithPropertyReference() {
        String jsCode = "var obj = {x: {y: 1}};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var y = 1,\n" +
                "       obj = {\n" +
                "         x: {\n" +
                "           y: y\n" +
                "         }\n" +
                "       };", output);
    }

    @org.junit.Test
    public void testInlineObjectLiterals_objectWithPropertyReferenceAndSelfReference() {
        String jsCode = "var obj = {x: {y: function() {return this.x.y}}};";
        Node root = parseJsCode(jsCode);
        processAndCheck(root);
        String output = printToSource(root);
        assertEquals("var x = {\n" +
                "  y: 1,\n" +
                "},\n" +
                "       obj = {\n" +
                "         x: x,\n" +
                "         x: {\n" +
                "           getY: function getY() {\n" +
                "             return this.x.y;\n" +
                "           }\n" +
                "         }\n" +
                "       };", output);
    }
}