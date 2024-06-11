package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private ProcessCommonJSModules pcm;
    private CompilerInput input;
    private Node root;
    private JSModule module;

    @org.junit.Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        input = mock(CompilerInput.class);
        root = mock(Node.class);
        pcm = new ProcessCommonJSModules(compiler, ".", true);
        when(input.getSourceFileName()).thenReturn("test.js");
        when(input.addProvide(anyString())).thenAnswer(invocation -> {
            String moduleName = invocation.getArgument(0);
            module = new JSModule(moduleName);
            return null;
        });
        when(input.addRequire(anyString())).thenAnswer(invocation -> {
            String moduleName = invocation.getArgument(0);
            module.addAndOverrideModule(input);
            return null;
        });
    }

    @org.junit.Test
    public void testGuessCJSModuleName() {
        assertEquals("module$test", pcm.guessCJSModuleName("test.js"));
        assertEquals("module$./sub/folder/file.js", pcm.guessCJSModuleName("./sub/folder/file.js"));
        assertEquals("module$../sub/folder/file.js", pcm.guessCJSModuleName("../sub/folder/file.js"));
    }

    @org.junit.Test
    public void testProcess() throws URISyntaxException {
        Node require = IR.call(IR.getprop(IR.name("require"), IR.string("test")),
                IR.string("test"));

        Node script = IR.script(
                IR.exprResult(IR.assign(IR.name("exports"), IR.objectlit()))
                        .copyInformationFromForTree(require),
                IR.string("test.js"));

        Node moduleExports = IR.getprop(IR.name("module$exports"), IR.name("exports"));

        when(compiler.getInput()).thenReturn(input);
        when(input.getSourceName()).thenReturn("test.js");

        pcm.process(input, root);

        verify(compiler, times(1)).reportCodeChange();

        Node requireInRoot = root.getChildAtIndex(0);
        assertEquals(IR.name("goog.require")
                .getQualifiedName(), requireInRoot.getQualifiedName());
        assertEquals(IR.name("test"), requireInRoot.getChildAtIndex(1));

        Node exports = root.getChildAtIndex(1);
        assertEquals(IR.name("module$test"), exports.getQualifiedName());

        Node module = root.getChildAtIndex(2);
        assertEquals(IR.objectlit().copyInformationFromForTree(script), module);

        Node moduleExports = root.getChildAtIndex(3);
        assertEquals(moduleExports, moduleExportsProp);
    }
}