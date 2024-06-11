package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.RETURN;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RuntimeTypeCheckTest {

    private Node inputNode;
    private Node expectedOutput;

    public RuntimeTypeCheckTest(Node input, Node expectedOutput) {
        this.inputNode = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.of(
                new Object[] {
                        NodeUtil.parseJsCode("function foo() {}"),
                        NodeUtil.parseJsCode("function foo() {}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo(x) { return x; }"),
                        NodeUtil.parseJsCode("function foo(x) {\n" +
                                "  checkType(x, [valueChecker('number')]);\n" +
                                "  return x;\n" +
                                "}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo(x) { return x + 5; }"),
                        NodeUtil.parseJsCode("function foo(x) {\n" +
                                "  checkType(x, [valueChecker('number')]);\n" +
                                "  return checkType(x + 5, [valueChecker('number')]);\n" +
                                "}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo(x) { return null; }"),
                        NodeUtil.parseJsCode("function foo(x) {\n" +
                                "  checkType(x, [nullChecker()]);\n" +
                                "  return null;\n" +
                                "}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo() { return 5; }"),
                        NodeUtil.parseJsCode("function foo() {\n" +
                                "  checkType(this, [classChecker('foo')]);\n" +
                                "  return 5;\n" +
                                "}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo() {}"),
                        NodeUtil.parseJsCode("function foo() {\n" +
                                "  checkType(this, [interfaceChecker('MyInterface')]);\n" +
                                "}")
                },
                new Object[] {
                        NodeUtil.parseJsCode("function foo() {}"),
                        NodeUtil.parseJsCode("function foo() {\n" +
                                "  checkType(this, [externClassChecker('MyExternClass')]);\n" +
                                "}")
                }
        );
    }

    @Test
    public void testRuntimeTypeCheck() {
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
        AbstractCompiler compiler = new Compiler(new SourceFile("test.js", null),
                new CompilerOptions());
        compiler.addPass(runtimeTypeCheck);
        Node output = compiler.compile(inputNode);
        assertEquals(expectedOutput, output);
    }
}