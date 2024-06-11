package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrossModuleMethodMotionTest {

    @Test
    public void testProcess() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        assertTrue(idGenerator.hasGeneratedAnyIds());
        assertFalse(input.getDiagnostics().isEmpty());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_stubMethod".equals(name)) {
                        Node body = n.getFirstChildAfter();
                        assertTrue(body.isFunction());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", root);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessNoMovement() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_stubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessMultipleMethods() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}\n" +
                "class C extends B {\n" +
                "  methodC() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        assertTrue(idGenerator.hasGeneratedAnyIds());
        assertFalse(input.getDiagnostics().isEmpty());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_stubMethod".equals(name)) {
                        Node body = n.getFirstChildAfter();
                        assertTrue(body.isFunction());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessNoMovementMultipleMethods() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}\n" +
                "class C extends B {\n" +
                "  methodC() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_stubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessNoMovementNoStubs() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, false);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_stubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessMovement() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}\n" +
                "class C extends B {\n" +
                "  methodC() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertTrue(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_unstubMethod".equals(name)) {
                        Node body = n.getFirstChildAfter();
                        assertTrue(body.isFunction());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessMovementNoStubs() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}\n" +
                "class C extends B {\n" +
                "  methodC() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, false);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_unstubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessMovementNoMovement() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, true);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_unstubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    @Test
    public void testProcessMovementNoMovementNoStubs() {
        CompilerOptions options = new CompilerOptions();
        SourceFile input = createSourceFile("input.js", "class A {\n" +
                "  methodA() {}\n" +
                "}\n" +
                "class B extends A {\n" +
                "  methodB() {}\n" +
                "}");

        Node root = Parser.parse(input, options);

        IdGenerator idGenerator = new IdGenerator();
        CompilerPass pass = new CrossModuleMethodMotion(
                new CompilerPassManager(), idGenerator, false);

        Node result = pass.process(null, root);

        List<DiagnosticType> diagnostics = input.getDiagnostics();
        assertTrue(diagnostics.isEmpty());
        assertFalse(idGenerator.hasGeneratedAnyIds());

        JSContext context = JSContext.enter();
        NodeTraversal.traverse(root, new NodeTraversal.SimpleCallBack() {
            @Override
            public void visit(Node n) {
                if (n.isCall() && n.getCallType() == Node.CALL_FUNCTION) {
                    String name = n.getFirstChild().getString();
                    if ("JSCompiler_unstubMethod".equals(name)) {
                        assertNull(n.getFirstChildAfter());
                    }
                }
            }
        });
        JSContext.exit();

        SourceFile output = new SourceFile("output.js", "", result);
        Parser.printTo(System.out, output.getDiagnostics(), output.getSourceNode());
    }

    private SourceFile createSourceFile(String name, String content) {
        return new SourceFile(name, content);
    }
}