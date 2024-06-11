package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private CompilerInput input;
    private ProcessCommonJSModules processor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compiler = mock(AbstractCompiler.class);
        input = new CompilerInput(new File("test.js"), null, null, null, null);
        processor = new ProcessCommonJSModules(compiler, "");
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expectedModuleName = "test";
        assertEquals(expectedModuleName, processor.guessCJSModuleName(filename));
    }

    @Test
    public void testGuessCJSModuleNameWithRelativePath() {
        String currentFilename = "folder/test.js";
        String requiredFilename = "../anotherFolder/anotherTest.js";
        String expectedModuleName = "anotherFolder/anotherTest";
        assertEquals(expectedModuleName, processor.toModuleName(requiredFilename, currentFilename));
    }

    @Test
    public void testGuessCJSModuleNameWithAbsolutePath() {
        String currentFilename = "folder/test.js";
        String requiredFilename = "/anotherFolder/anotherTest.js";
        String expectedModuleName = "anotherFolder/anotherTest";
        assertEquals(expectedModuleName, processor.toModuleName(requiredFilename, currentFilename));
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "test" + File.separator + "test.js";
        String expectedNormalizedSourceName = "test";
        assertEquals(expectedNormalizedSourceName, processor.normalizeSourceName(filename));
    }

    @Test
    public void testProcess() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor.process(input, root);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());
    }

    @Test
    public void testProcessWithReportDependencies() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor = new ProcessCommonJSModules(compiler, "", true);
        processor.process(input, root);

        CompilerInput input = mock(CompilerInput.class);
        when(input.getRequireNames()).thenReturn(Sets.newHashSet("module1", "module2"));
        processor.process(input, root);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        verify(input).addRequire("module1");
        verify(input).addRequire("module2");
    }

    @Test
    public void testProcessWithProvide() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor = new ProcessCommonJSModules(compiler, "", true);
        processor.process(input, root);

        CompilerInput input = mock(CompilerInput.class);
        input.addProvide("test");
        processor.process(input, root);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        Node testModule = root.getFirstChild();
        Node testModuleProvide = testModule.getFirstChild();
        Node testModuleName = testModuleProvide.getFirstChild();
        assertEquals("test", testModuleName.getString());
    }

    @Test
    public void testProcessWithModuleExports() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor = new ProcessCommonJSModules(compiler, "", true);
        processor.process(input, root);

        Node script = NodeFactory.createScript(new IR("module.exports = 'Hello World'"));
        script.setSourceFileName("module.js");
        root.addChildToBack(script);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        Node moduleExports = root.getLastChild();
        Node moduleName = moduleExports.getFirstChild();
        Node moduleModuleExports = moduleExports.getLastChild();
        assertEquals("module$exports", moduleModuleExports.getString());
    }

    @Test
    public void testProcessWithModuleExportsOverride() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor = new ProcessCommonJSModules(compiler, "", true);
        processor.process(input, root);

        Node script = NodeFactory.createScript(
            new IR("module.exports = 'Hello World'; module.module$exports = {'foo': 'bar'}"));
        script.setSourceFileName("module.js");
        root.addChildToBack(script);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        Node moduleExports = root.getLastChild();
        Node moduleName = moduleExports.getFirstChild();
        Node moduleModuleExports = moduleExports.getChildAtIndex(1);
        Node fooProperty = moduleModuleExports.getChildAtIndex(0);
        Node fooValue = fooProperty.getChildAtIndex(0);
        assertEquals("'bar'", fooValue.getString());
    }

    @Test
    public void testProcessWithScriptNode() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor.process(input, root);

        Node script = NodeFactory.createScript(new IR("console.log('Hello World')"));
        script.setSourceFileName("script.js");
        root.addChildToBack(script);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        Node scriptNode = root.getChildAtIndex(1);
        Node scriptName = scriptNode.getFirstChild();
        assertEquals("script", scriptName.getString());
    }

    @Test
    public void testProcessWithDependencies() throws Exception {
        Node root = NodeFactory.createScript(new IR("test"));
        when(compiler.getSourceFileName()).thenReturn("test.js");
        processor = new ProcessCommonJSModules(compiler, "", true);
        processor.process(input, root);

        Node requireCall = NodeFactory.createCall(
            NodeFactory.createName("require"), NodeFactory.createString("module1"));
        root.addChildToBack(requireCall);

        NodeTraversal.traverse(compiler, root, new NodeAssertCallback());

        Node requireNode = root.getLastChild();
        Node requireModuleName = requireNode.getFirstChild();
        Node requireModuleRef = requireNode.getChildAtIndex(1);
        Node script = NodeFactory.createScript(new IR("console.log('Hello World')"));
        script.setSourceFileName("module1.js");
        requireModuleRef.setNodeValue(script);

        Node module1 = root.getChildAtIndex(0);
        Node module1Name = module1.getFirstChild();
        Node module1ModuleExports = module1.getLastChild();
        Node exports = module1ModuleExports.getFirstChild();
        Node exportsString = exports.getChildAtIndex(0);
        assertEquals("module1", module1Name.getString());
        assertEquals("module$exports", module1ModuleExports.getString());
        assertEquals("'Hello World'", exportsString.getString());
    }

    private static class NodeAssertCallback extends AbstractPostOrderCallback {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isName()) {
                String name = n.getString();
                assertTrue("Unexpected name: " + name,
                    name.endsWith("$$") || name.equals("module") || name.equals("exports"));
            }
        }
    }
}