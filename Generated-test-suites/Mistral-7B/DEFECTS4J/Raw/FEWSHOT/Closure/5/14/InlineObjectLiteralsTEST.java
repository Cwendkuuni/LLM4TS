package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() {
        // Given a simple object literal assignment
        String input = "function foo() {" +
                "  var obj = {" +
                "    a: 1," +
                "    b: 'two'" +
                "  };" +
                "}" +
                "function bar() {" +
                "  var x = obj.a;" +
                "  var y = obj.b;" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        Compiler compiler = new Compiler();
        AbstractCompiler abstractCompiler = compiler.newAbstractCompiler();
        InlineObjectLiterals inlineObjectLiterals =
                new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        String expectedOutput = "function foo() {" +
                "  var a = 1;" +
                "  var b = 'two';" +
                "  var obj = {a: a, b: b};" +
                "}" +
                "function bar() {" +
                "  var x = a;" +
                "  var y = b;" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given a complex object literal assignment with multiple levels
        input = "function foo() {" +
                "  var obj = {" +
                "    a: 1," +
                "    b: {" +
                "      c: 2," +
                "      d: 'three'" +
                "    }" +
                "  };" +
                "}" +
                "function bar() {" +
                "  var x = obj.b.c;" +
                "  var y = obj.b.d;" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        expectedOutput = "function foo() {" +
                "  var c = 2;" +
                "  var d = 'three';" +
                "  var b = {c: c, d: d};" +
                "  var a = 1;" +
                "  var obj = {a: a, b: b};" +
                "}" +
                "function bar() {" +
                "  var x = c;" +
                "  var y = d;" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given an object literal assignment with a variable being assigned an object literal
        input = "function foo() {" +
                "  var obj = {" +
                "    a: {" +
                "      b: 1," +
                "      c: 2" +
                "    }" +
                "  };" +
                "  var x = obj.a;" +
                "  x.b = 3;" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        expectedOutput = "function foo() {" +
                "  var b = 1;" +
                "  var c = 2;" +
                "  var a = {b: b, c: c};" +
                "  var x = a;" +
                "  x.b = 3;" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given an object literal assignment with a property being assigned a function
        input = "function foo() {" +
                "  var obj = {" +
                "    func: function() { return 1; }" +
                "  };" +
                "  var x = obj.func();" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        expectedOutput = "function foo() {" +
                "  var func = function() { return 1; };" +
                "  var obj = {func: func};" +
                "  var x = func();" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given an object literal assignment with a cyclic reference
        input = "function foo() {" +
                "  var obj1 = {" +
                "    a: 1," +
                "    b: obj2" +
                "  };" +
                "  var obj2 = {" +
                "    a: obj1" +
                "  };" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should not be inlined due to the cyclic reference and the output should be the same
        expectedOutput = input;

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given an object literal assignment with a property being assigned an object literal that is assigned to a variable
        input = "function foo() {" +
                "  var obj = {" +
                "    a: {" +
                "      b: 1," +
                "      c: 2" +
                "    }" +
                "  };" +
                "  var x = obj.a;" +
                "  x.b = 3;" +
                "  var y = x;" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        expectedOutput = "function foo() {" +
                "  var b = 1;" +
                "  var c = 2;" +
                "  var a = {b: b, c: c};" +
                "  var x = a;" +
                "  x.b = 3;" +
                "  var y = a;" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());

        // Given an object literal assignment with a property being assigned an object literal that is assigned to a property of an object literal
        input = "function foo() {" +
                "  var obj1 = {" +
                "    a: {" +
                "      b: 1," +
                "      c: {d: 2}" +
                "    }" +
                "  };" +
                "  var obj2 = obj1.a;" +
                "  obj1.a.c.d = 3;" +
                "}" +
                "foo();";

        // When processing the code through the InlineObjectLiterals pass
        compiler = new Compiler();
        abstractCompiler = compiler.newAbstractCompiler();
        inlineObjectLiterals = new InlineObjectLiterals(abstractCompiler, () -> "tempVar");
        inlineObjectLiterals.process(null, NodePrinter.parse(input));

        // Then the object literal should be inlined and replaced with individual variable declarations
        expectedOutput = "function foo() {" +
                "  var d = 3;" +
                "  var c = {d: d};" +
                "  var b = 1;" +
                "  var a = {b: b, c: c};" +
                "  var obj1 = {a: a};" +
                "  obj1.a.c.d = 3;" +
                "}" +
                "foo();";

        // And the resulting output should match the expected output
        assertEquals(expectedOutput, compiler.getSourceOutput());
    }
}