package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class RuntimeTypeCheckTest {

    private static AbstractCompiler compiler;

    @BeforeClass
    public static void setUp() {
        compiler = new Compiler();
    }

    @Test
    public void testProcess_WithNullLogFunction() {
        String logFunction = null;
        String sourceCode = "var x = 10;";

        CompilerOptions options = new CompilerOptions();
        SourceFile input = SourceFile.fromCode("input.js", sourceCode);
        Compiler compiler = new Compiler();
        compiler.compile(
                ImmutableList.of(input), ImmutableList.<SourceFile>of(), options);
        Node root = compiler.getRoot();

        RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, logFunction);

        pass.process(null, root);

        // Assertions here
    }

    @Test
    public void testProcess_WithLogFunction() {
        String logFunction = "console.warn";
        String sourceCode = "var x = 10;";

        CompilerOptions options = new CompilerOptions();
        SourceFile input = SourceFile.fromCode("input.js", sourceCode);
        Compiler compiler = new Compiler();
        compiler.compile(
                ImmutableList.of(input), ImmutableList.<SourceFile>of(), options);
        Node root = compiler.getRoot();

        RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, logFunction);

        pass.process(null, root);

        // Assertions here
    }

    // More test cases here

}