package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcess() {
        // Initialize the compiler and create a mock source node
        Compiler compiler = new Compiler();
        Node externs = compiler.parseTestCode("");
        Node source = compiler.parseTestCode("var a = require('test');");
        JSModule module = new JSModule("test");
        module.add(SourceFile.fromCode("test", source.getSourceFileName()));
        SourceFile externsFile = SourceFile.fromCode("externs", "");
        CompilerInput externsInput = new CompilerInput(externsFile);
        CompilerInput sourceInput = new CompilerInput(module.getOriginalName(), source.getSourceFileName());
        Node root = new Node(new Node(Token.ROOT, externs), new Node(Token.ROOT, source));
        SimpleSourceMap sourceMap = null;

        // Run the processCommonJSModules() method
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
        processCommonJSModules.process(externs, root);

        // Check if the module is correctly added to the input
        assertEquals(module, processCommonJSModules.getModule());
    }

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        String moduleName = processCommonJSModules.guessCJSModuleName("test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleName() {
        String moduleName = ProcessCommonJSModules.toModuleName("test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String moduleName = ProcessCommonJSModules.toModuleName("./test.js", "src/test/main.js");
        assertEquals("module$src$test$test", moduleName);
        moduleName = ProcessCommonJSModules.toModuleName("../test.js", "src/test/main.js");
        assertEquals("module$src$test$test", moduleName);
        moduleName = ProcessCommonJSModules.toModuleName("../../test.js", "src/test/main.js");
        assertEquals("module$src$test$test", moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall() {
        Compiler compiler = new Compiler();
        Node root = new Node(Token.ROOT);
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        Node parent = new Node(Token.FUNCTION);
        Node require = new Node(Token.CALL, new Node(Token.NAME, "require"), new Node(Token.STRING, "test.js"));
        parent.addChildToFront(require);
        callback.visit(null, require, parent);

        // Check if the require node is replaced with the module reference node
        Node expectedNode = new Node(Token.NAME, "module$test");
        assertEquals(expectedNode, parent.getFirstChild());
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall_withReportDependencies() {
        Compiler compiler = new Compiler();
        Node root = new Node(Token.ROOT);
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "", true);
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        Node parent = new Node(Token.FUNCTION);
        Node require = new Node(Token.CALL, new Node(Token.NAME, "require"), new Node(Token.STRING, "test.js"));
        parent.addChildToFront(require);
        callback.visit(null, require, parent);

        // Check if the module name is added to the dependencies
        assertTrue(compiler.getInputForModuleName("test").getRequires().contains("module$test"));

    }

    @Test
    public void testProcessCommonJsModulesCallback_visitScript() {
        Compiler compiler = new Compiler();
        Node root = new Node(Token.ROOT);
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        Node script = new Node(Token.SCRIPT);
        Node require = new Node(Token.CALL, new Node(Token.NAME, "require"), new Node(Token.STRING, "test.js"));
        script.addChildToFront(require);
        callback.visit(null, script, root);

        // Check if the module name variable is added to the front of the script node
        Node expectedNode = new Node(Token.VAR, new Node(Token.NAME, "module$test"), new Node(Token.OBJECTLIT));
        assertEquals(expectedNode, script.getFirstChild());
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitModuleExports() {
        Compiler compiler = new Compiler();
        Node root = new Node(Token.ROOT);
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        Node moduleExports = new Node(Token.GETPROP, new Node(Token.NAME, "module"), new Node(Token.STRING, "exports"));
        callback.visit(null, moduleExports, root);

        // Check if the module and exports node are correctly modified
        Node expectedNode = new Node(Token.NAME, "module$test");
        assertEquals(expectedNode, moduleExports.getFirstChild());
        expectedNode = new Node(Token.STRING, "module$exports");
        assertEquals(expectedNode, moduleExports.getLastChild());
    }

    @Test
    public void testSuffixVarsCallback_visit_WithValidVar() {
        Scope.Var var = new Scope.Var("var", null, true);
        var.setGlobal();
        Scope scope = new Scope(var);
        NodeTraversal t = new NodeTraversal(null, scope, null, null);
        Node node = new Node(Token.NAME, "test");
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("module");
        callback.visit(t, node, null);

        // Check if the variable name is suffixed with module$
        Node expectedNode = new Node(Token.NAME, "test$$module");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testSuffixVarsCallback_visit_WithExportsVar() {
        Scope.Var var = new Scope.Var("exports", null, true);
        var.setGlobal();
        Scope scope = new Scope(var);
        NodeTraversal t = new NodeTraversal(null, scope, null, null);
        Node node = new Node(Token.NAME, "exports");
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("module");
        callback.visit(t, node, null);

        // Check if the variable name is replaced with the module$
        Node expectedNode = new Node(Token.NAME, "module");
        assertEquals(expectedNode, node);
    }
}