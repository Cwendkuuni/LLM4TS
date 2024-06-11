import com.google.common.collect.Sets;
import com.google.javascript.jscomp.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        processCommonJSModules.process(externs, root);

        // Verify that the expected method was called
        Mockito.verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testGuessCJSModuleName() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        String filename = "sample.js";

        String moduleName = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals("module$sample", moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = ".\\sample.js";

        String moduleName = ProcessCommonJSModules.toModuleName(filename);

        assertEquals("module$sample", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "../../module2.js";
        String currentFilename = ".\\module1.js";

        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

        assertEquals("module$module2", moduleName);
    }

    @Test
    public void testNormalizeSourceName() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        String filename = ".\\sample.js";

        String normalizedFilename = processCommonJSModules.normalizeSourceName(filename);

        assertEquals("sample.js", normalizedFilename);
    }

    @Test
    public void testProcessRequireCall() {
        CompilerOptions options = new CompilerOptions();
        options.setLanguageIn(LanguageMode.ECMASCRIPT5);

        Compiler compiler = new Compiler();
        compiler.initOptions(options);
        Node externs = compiler.parseTestCode("var goog = {}; var module = {};var require = function(name){};");
        Node root = compiler.parseTestCode("require('module1');");

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".\\");

        processCommonJSModules.process(externs, root);

        Node expected = compiler.parseTestCode("module1;");

        assertThat(compiler.toSource(root)).isEqualTo(compiler.toSource(expected));
    }

    @Test
    public void testVisitScript() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        NodeTraversal t = mock(NodeTraversal.class);
        Node scriptNode = mock(Node.class);
        scriptNode.getParent();

        processCommonJSModules.visitScript(t, scriptNode);

        // Verify that the expected methods were called
        Mockito.verify(scriptNode, times(1)).addChildToFront(any());
        Mockito.verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitModuleExports() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        Node propNode = mock(Node.class);
        propNode.getChildAtIndex(0).setString("module");
        propNode.getChildAtIndex(1).setString("exports");

        processCommonJSModules.visitModuleExports(propNode);

        assertEquals("module$sample", propNode.getChildAtIndex(0).getString());
        assertEquals("module$exports", propNode.getChildAtIndex(1).getString());
        assertEquals("module", propNode.getChildAtIndex(0).getProp(Node.ORIGINALNAME_PROP));
        assertEquals("exports", propNode.getChildAtIndex(1).getProp(Node.ORIGINALNAME_PROP));
    }

    @Test
    public void testSuffixVarsCallback() {
        SuffixVarsCallback suffixVarsCallback = new SuffixVarsCallback("sample");
        NodeTraversal t = mock(NodeTraversal.class);
        Node nameNode = mock(Node.class);
        nameNode.setString("var");

        suffixVarsCallback.visit(t, nameNode, null);

        assertEquals("var_sample", nameNode.getString());
        assertEquals("var", nameNode.getProp(Node.ORIGINALNAME_PROP));
    }
}