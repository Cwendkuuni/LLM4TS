package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcessCommonJSModules() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        File inputFile = new File("input.js");
        String inputCode = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n" +
                "require('dep');\n";
        Node inputNode = IR.parse(inputCode, inputFile, null).getTree();
        rootNode.addChildToBack(inputNode);

        processor.process(null, rootNode);

        // Check that 'dep' was replaced with 'dep$$0' and 'module.exports' was renamed to 'module$$exports'
        Node depNode = rootNode.getChildAtIndex(1);
        assertEquals("dep$$0", depNode.getString());
        Node exportsNode = rootNode.getChildAtIndex(2).getChildAtIndex(1);
        assertEquals("module$$exports", exportsNode.getString());
        assertSame(exportsNode, depNode.getChildAtIndex(1));

        // Check that 'require' call was replaced with goog.require and 'module.exports' was emitted
        Node requireCallNode = depNode.getChildAtIndex(0);
        assertEquals("goog.require('dep$$0')", requireCallNode.toString());
        Node exportsNode2 = rootNode.getChildAtIndex(2).getChildAtIndex(1);
        assertEquals("goog.provide('dep$$0');\nmodule$$exports = module.exports;\n",
                exportsNode2.toString());

        // Check that 'foo' function was not renamed
        Node fooFunctionNode = rootNode.getChildAtIndex(2).getChildAtIndex(0);
        assertEquals("function foo() {\n" +
                "  return 'bar';\n" +
                "}", fooFunctionNode.toString());

        // Check that 'module' and 'exports' variables were not emitted
        Set<String> providedModules = Sets.newHashSet();
        Set<String> requiredModules = Sets.newHashSet();
        for (Node node : rootNode.getChildren()) {
            if (node.isCall() && "goog.provide".equals(node.getFirstChild().getQualifiedName())) {
                providedModules.add(node.getChildAtIndex(1).getString());
            }
            if (node.isCall() && "require".equals(node.getFirstChild().getQualifiedName())) {
                requiredModules.add(node.getChildAtIndex(1).getString());
            }
        }
        assertEquals(1, providedModules.size());
        assertEquals(1, requiredModules.size());
        assertSame("dep$$0", requiredModules.iterator().next());
        assertSame("dep$$0", providedModules.iterator().next());
    }

    @Test
    public void testProcessCommonJSModulesWithRelativeRequires() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        File inputFile1 = new File("input1.js");
        String inputCode1 = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode1 = IR.parse(inputCode1, inputFile1, null).getTree();
        rootNode.addChildToBack(inputNode1);

        File inputFile2 = new File("input2.js");
        String inputCode2 = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return require('./dep').foo();\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode2 = IR.parse(inputCode2, inputFile2, inputFile1).getTree();
        rootNode.addChildToBack(inputNode2);

        processor.process(null, rootNode);

        // Check that './dep' was resolved to 'dep$$1'
        Node depNode = rootNode.getChildAtIndex(2).getChildAtIndex(1);
        assertEquals("dep$$1", depNode.getString());

        // Check that 'foo' function was called with 'dep$$1.foo'
        Node fooCallNode = rootNode.getChildAtIndex(2).getChildAtIndex(2);
        assertEquals("dep$$1.foo()", fooCallNode.toString());
    }

    @Test
    public void testProcessCommonJSModulesWithMultipleScripts() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        File inputFile1 = new File("input1.js");
        String inputCode1 = "var module1 = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode1 = IR.parse(inputCode1, inputFile1, null).getTree();
        rootNode.addChildToBack(inputNode1);

        File inputFile2 = new File("input2.js");
        String inputCode2 = "var module2 = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return module1.exports.foo();\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode2 = IR.parse(inputCode2, inputFile2, inputFile1).getTree();
        rootNode.addChildToBack(inputNode2);

        processor.process(null, rootNode);

        // Check that 'module1' and 'module2' were not renamed
        Node module1Node = rootNode.getChildAtIndex(0);
        assertEquals("module1", module1Node.getString());
        Node module2Node = rootNode.getChildAtIndex(1);
        assertEquals("module2", module2Node.getString());

        // Check that 'exports' was renamed in 'module1' to 'module1$$exports'
        Node exportsNode1 = module1Node.getChildAtIndex(0);
        assertEquals("module1$$exports", exportsNode1.getString());

        // Check that 'exports' was not renamed in 'module2'
        Node exportsNode2 = module2Node.getChildAtIndex(0);
        assertEquals("exports", exportsNode2.getString());

        // Check that 'foo' function in 'module2' was called with 'module1.exports.foo'
        Node fooCallNode = module2Node.getChildAtIndex(1).getChildAtIndex(0);
        assertEquals("module1.exports.foo()", fooCallNode.toString());
    }

    @Test
    public void testProcessCommonJSModulesWithNoModuleName() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        File inputFile = new File("input.js");
        String inputCode = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode = IR.parse(inputCode, inputFile, null).getTree();
        rootNode.addChildToBack(inputNode);

        processor.process(null, rootNode);

        // Check that no module name was added
        assertNull(rootNode.getChildAtIndex(0).getChildAtIndex(0).getProp(Node.ORIGINALNAME_PROP));
    }

    @Test
    public void testProcessCommonJSModulesWithEmptyFilename() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        String inputCode = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode = IR.parse(inputCode, null, null).getTree();
        rootNode.addChildToBack(inputNode);

        try {
            processor.process(null, rootNode);
            fail("Expected ProcessCommonJSModules to throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProcessCommonJSModulesWithInvalidFilename() {
        AbstractCompiler compiler = new TestCompiler();
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

        Node rootNode = IR.program();
        String inputCode = "var module = {\n" +
                "  exports: {\n" +
                "    foo: function() {\n" +
                "      return 'bar';\n" +
                "    }\n" +
                "  }\n" +
                "};\n";
        Node inputNode = IR.parse(inputCode, new File("invalid"), null).getTree();
        rootNode.addChildToBack(inputNode);

        try {
            processor.process(null, rootNode);
            fail("Expected ProcessCommonJSModules to throw an exception");
        } catch (URISyntaxException e) {
            // Expected
        }
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module1", ProcessCommonJSModules.toModuleName("input1.js"));
        assertEquals("module2$dep1", ProcessCommonJSModules.toModuleName("input2/dep.js"));
        assertEquals("module3$dep1$dep2", ProcessCommonJSModules.toModuleName("input3/dep1/dep2.js"));
    }

    @Test
    public void testToModuleNameWithRelativeRequires() {
        assertEquals("module1$dep1", ProcessCommonJSModules.toModuleName("dep.js", "input1.js"));
        assertEquals("module2$dep1$dep2", ProcessCommonJSModules.toModuleName("dep2.js", "dep.js", "input2.js"));
    }

    @Test
    public void testNormalizeSourceName() {
        assertEquals("input1", ProcessCommonJSModules.normalizeSourceName("input1.js"));
        assertEquals("input2", ProcessCommonJSModules.normalizeSourceName("input2/input.js"));
        assertEquals("input3", ProcessCommonJSModules.normalizeSourceName("input3/input.js"));
    }

    private static class TestCompiler implements Compiler {
        @Override
        public void compile(Node externs, Node rootNode, CompilerOptions options, CompilerPass pass) {
        }

        @Override
        public Node getTopLevelCode() {
            return null;
        }

        @Override
        public void reportCodeChange(int lineNumber, int columnNumber, String message) {
        }
    }
}