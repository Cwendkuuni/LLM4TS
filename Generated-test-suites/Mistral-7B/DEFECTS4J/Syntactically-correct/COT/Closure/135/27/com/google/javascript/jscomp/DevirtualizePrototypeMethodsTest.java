package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.google.javascript.rhino.NodeUtil.isCall;
import static com.google.javascript.rhino.NodeUtil.isPrototypeMethodDefinition;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DevirtualizePrototypeMethodsTest {

    private Compiler compiler;
    private CompilerPass devirtualizePrototypeMethods;
    private String inputCode;
    private String expectedOutputCode;

    public DevirtualizePrototypeMethodsTest(String inputCode, String expectedOutputCode) {
        this.inputCode = inputCode;
        this.expectedOutputCode = expectedOutputCode;
    }

    @Parameterized.Parameters(name = "input={0}, output={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"function A() { function a() { this.total = 0; } } ",
                        "var a = function(self) { self.total = 0; } "},
                {"function A() { function a() { this.total += 1; return this.total; } } ",
                        "var a = function(self) { self.total += 1; return self.total; } "},
                {"function A() { function a(b) { this.total += b; return this.total; } } ",
                        "var a = function(self, b) { self.total += b; return self.total; } "},
                {"function A() { function a(b, c) { this.total += b + c; return this.total; } } ",
                        "var a = function(self, b, c) { self.total += b + c; return self.total; } "},
                {"function A() { function a(b, c) { return this.total += b + c; } } ",
                        "var a = function(self, b, c) { return self.total += b + c; } "},
                {"function A() { function a(b, c) { this.prototype.total += b + c; } } ",
                        "var a = function(self, b, c) { self.prototype.total += b + c; } "},
                {"function A() { function a() { this.a = function() { return 1; } } } ",
                        "var a = function(self) { self.a = function() { return 1; }; return self.a; } "},
                {"function A() { function a() { this.a = 1; } } ",
                        "var a = function(self) { this.a = 1; return self.a; } "},
                {"function A() { function a() { this.total = 1; return this; } } ",
                        "var a = function(self) { this.total = 1; return self; } "},
                {"function A() { function a() { return this; } } ",
                        "var a = function(self) { return self; } "},
                {"function A() { function a(b) { this.total += b; } } ",
                        "var a = function(self, b) { self.total += b; } "},
                {"function A() { function a() { this.total += arguments[0]; } } ",
                        "CompilationError: Invalid usage of arguments in this context."},
                {"function A() { function a(b, c) { this.total += arguments[0] + arguments[1]; } } ",
                        "CompilationError: Invalid usage of arguments in this context."},
        });
    }

    @Before
    public void setup() {
        compiler = new Compiler();
        compiler.addPass(new CompilerPass.Phase(CompilerPass.Phase.EARLY,
                new DevirtualizePrototypeMethods(compiler)));
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    }

    @Test
    public void testDevirtualizePrototypeMethods() {
        Node ast = Node.parse(inputCode);
        compiler.process(ast, null);
        String outputCode = NodePrinter.printToNodeString(compiler.getRootNode());
        assertEquals(expectedOutputCode, outputCode);
    }
}