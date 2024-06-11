package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcessCommonJSModules() {
        String filename = "test.js";
        AbstractCompiler compiler = new AbstractCompiler() {};
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");

        CompilerInput input = new CompilerInput(filename, null, null, null, null);
        Node root = IR.program();

        processor.process(input.getExterns(), root);

        assertEquals(1, root.getChildCount());
        Node script = root.getChildAtIndex(0);

        // Check that module name is added as a global variable.
        assertNotNull(script.getChildWithName("test$"));

        // Check that require calls are replaced with direct references.
        Node requireCall = script.getChildWithName("require");
        assertNull(requireCall);

        // Check that module exports are renamed to module$exports.
        Node moduleExports = script.getChildWithName("exports");
        assertNotNull(moduleExports);
        assertEquals("module$exports", moduleExports.getName());
    }

    @Test
    public void testProcessCommonJSModulesWithDependencies() {
        String testFile = "test.js";
        String depFile = "dep.js";

        AbstractCompiler compiler = new AbstractCompiler() {};
        ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", true);

        CompilerInput testInput = new CompilerInput(testFile, null, null, null, null);
        Node testRoot = IR.program();

        CompilerInput depInput = new CompilerInput(depFile, null, null, null, null);
        Node depRoot = IR.program();

        depRoot.addChildToBack(IR.exprResult(IR.string("depModule$exports")));

        processor.process(testInput.getExterns(), testRoot);
        processor.process(depInput.getExterns(), depRoot);

        testRoot.addChildToBack(IR.require(IR.name("dep")));

        processor.process(testInput.getExterns(), testRoot);

        Node moduleExports = testRoot.getChildWithName("exports");
        assertNotNull(moduleExports);
        assertEquals("depModule$exports", moduleExports.getString());

        Node requireCall = testRoot.getChildWithName("require");
        assertNull(requireCall);

        Node depModule = testRoot.getChildAtIndex(1);
        assertEquals("dep", depModule.getName());
        assertNotNull(depModule.getChildWithName("module$exports"));
    }
}