package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest {

    @Test
    public void testRenameVars() {
        String prefix = "test_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        Node externs = Node.newTextNode("");
        Node root = Node.newTextNode("function test() { var a = 1; var b = 2; }");
        renamer.process(externs, root);

        Map<String, String> renameMap = renamer.getVariableMap();

        // Test global variables
        assertEquals("test_a", renameMap.get("a"));
        assertEquals("test_b", renameMap.get("b"));

        // Test local variables
        assertEquals("test_a", renameMap.get("test.test()[0].a"));
        assertEquals("test_b", renameMap.get("test.test()[0].b"));

        // Test reserved variables
        assertNull(renameMap.get("function"));
        assertNull(renameMap.get("var"));

        // Test extern variables
        assertEquals("test_a", renameMap.get("a"));
        assertEquals("test_b", renameMap.get("b"));

        // Test local variables with the same name as globals
        Node rootWithGlobal = Node.newTextNode("function test() { var a = 1; } var a = 2;");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithGlobal);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[1]"));

        // Test local variables with the same name as externs
        Node rootWithExtern = Node.newTextNode("var a = 1; function test() { var a = 2; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithExtern);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].a"));

        // Test local variables with the same name as locals in outer scope
        Node rootWithLocalInOuterScope = Node.newTextNode("function test() { function foo() { var a = 1; } var a = 2; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalInOuterScope);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].a"));

        // Test local variables with the same name as locals in inner scope
        Node rootWithLocalInInnerScope = Node.newTextNode("function test() { function foo() { function bar() { var a = 1; } var a = 2; } }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalInInnerScope);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));

        // Test local variables with the same name as locals in inner scope and globals
        Node rootWithLocalInInnerScopeAndGlobal = Node.newTextNode("function test() { function foo() { var a = 1; } var a = 2; } var a = 3;");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalInInnerScopeAndGlobal);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].a"));
        assertEquals("test_a_3", renameMap.get("test.test()[0]"));

        // Test local variables with the same name as locals in outer and inner scope
        Node rootWithLocalInOuterAndInnerScope = Node.newTextNode("function test() { function foo() { function bar() { var a = 1; } var a = 2; } } function baz() { var a = 3; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalInOuterAndInnerScope);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0].bar()[0]"));

        // Test local variables with the same name as globals and externs
        Node rootWithLocalGlobalAndExtern = Node.newTextTextNode("var a = 1; function test() { var a = 2; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalGlobalAndExtern);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].a"));

        // Test local variables with the same name as globals and locals in outer scope
        Node rootWithLocalGlobalAndLocalOuter = Node.newTextTextNode("var a = 1; function test() { function foo() { var a = 2; } }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalGlobalAndLocalOuter);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].a"));

        // Test local variables with the same name as globals and locals in inner scope
        Node rootWithLocalGlobalAndLocalInner = Node.newTextTextNode("var a = 1; function test() { function foo() { function bar() { var a = 2; } } }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalGlobalAndLocalInner);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));

        // Test local variables with the same name as globals, locals in outer and inner scope
        Node rootWithLocalGlobalLocalOuterAndInner = Node.newTextTextNode("var a = 1; function test() { function foo() { function bar() { var a = 2; } } } function baz() { var a = 3; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalGlobalLocalOuterAndInner);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0].bar()[0]"));

        // Test local variables with the same name as locals in outer and inner scope and globals
        Node rootWithLocalLocalGlobalOuterAndInner = Node.newTextTextNode("function test() { function foo() { var a = 1; } } function baz() { var a = 2; } var a = 3;");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalLocalGlobalOuterAndInner);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_a_3", renameMap.get("test.test()[0]"));

        // Test local variables with the same name as locals in outer and inner scope and locals in outer scope
        Node rootWithLocalLocalLocalOuterAndInner = Node.newTextTextNode("function test() { function foo() { function bar() { var a = 1; } } function baz() { var a = 2; } } function qux() { var a = 3; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalLocalLocalOuterAndInner);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0]"));
        assertEquals("test_q_1", renameMap.get("qux"));

        // Test local variables with the same name as locals in outer and inner scope and locals in outer and inner scope
        Node rootWithLocalLocalLocalOuterAndInnerOuterAndInner = Node.newTextTextNode("function test() { function foo() { function bar() { var a = 1; } } function baz() { var a = 2; } } function qux() { function qux() { var a = 3; } } function quux() { var a = 4; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalLocalLocalOuterAndInnerOuterAndInner);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_q_1", renameMap.get("qux"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0]"));
        assertEquals("test_q_2", renameMap.get("quux"));

        // Test local variables with the same name as locals in outer and inner scope and locals in outer and inner scope and globals
        Node rootWithLocalLocalLocalOuterAndInnerOuterAndInnerGlobal = Node.newTextTextNode("function test() { function foo() { function bar() { var a = 1; } } function baz() { var a = 2; } } function qux() { function qux() { var a = 3; } } var a = 4; function quux() { var a = 5; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalLocalLocalOuterAndInnerOuterAndInnerGlobal);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_q_1", renameMap.get("qux"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0]"));
        assertEquals("test_q_2", renameMap.get("quux"));
        assertEquals("test_a_4", renameMap.get("test.test()[0]"));
        assertEquals("test_a_5", renameMap.get("test.test()[0].quux()[0]"));

        // Test local variables with the same name as locals in outer and inner scope and locals in outer and inner scope and locals in outer scope
        Node rootWithLocalLocalLocalOuterAndInnerOuterAndInnerLocalOuter = Node.newTextTextNode("function test() { function foo() { function bar() { var a = 1; } } function baz() { var a = 2; } } function qux() { function qux() { var a = 3; } } function quux() { var a = 4; } function foo2() { var a = 5; }");
        renamer = new RenameVars(null, prefix, false, false, false, null, null, null);
        renamer.process(externs, rootWithLocalLocalLocalOuterAndInnerOuterAndInnerLocalOuter);
        assertEquals("test_a_1", renameMap.get("a"));
        assertEquals("test_a_2", renameMap.get("test.test()[0].foo()[0].bar()[0].a"));
        assertEquals("test_b_1", renameMap.get("baz"));
        assertEquals("test_q_1", renameMap.get("qux"));
        assertEquals("test_a_3", renameMap.get("test.test()[0].foo()[0]"));
        assertEquals("test_q_2", renameMap.get("quux"));
        assertEquals("test_f_1", renameMap.get("foo2"));
    }
}