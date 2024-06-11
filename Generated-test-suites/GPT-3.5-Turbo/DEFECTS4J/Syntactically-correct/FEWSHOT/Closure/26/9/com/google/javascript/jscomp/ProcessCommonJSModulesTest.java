import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;
import org.mockito.Mockito;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        
        Node externs = IR.empty();
        Node root = IR.empty();
        
        processCommonJSModules.process(externs, root);
        
        // verify process is called on the compiler
        Mockito.verify(compiler).process(Mockito.any(Node.class), Mockito.any(Node.class));
    }

    @Test
    public void testGuessCJSModuleName() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);
        
        String filename = ".\\test.js";
        
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = ".\\test.js";
        
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = ".\\required.js";
        String currentFilename = ".\\test.js";
        
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        
        assertEquals("module$required", moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visit(t, n, parent);

        // verify visitRequireCall is called
        Mockito.verify(callback).visitRequireCall(Mockito.any(NodeTraversal.class), Mockito.any(Node.class), Mockito.any(Node.class));

        // verify visitScript is called
        Mockito.verify(callback).visitScript(Mockito.any(NodeTraversal.class), Mockito.any(Node.class));

        // verify visitModuleExports is called
        Mockito.verify(callback).visitModuleExports(Mockito.any(Node.class));

        // verify getCurrentScriptNode is called
        Mockito.verify(callback).getCurrentScriptNode(Mockito.any(Node.class));
    }

    @Test
    public void testVisitRequireCall() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node require = IR.empty();
        Node parent = IR.empty();

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitRequireCall(t, require, parent);

        // verify addRequire is called
        Mockito.verify(t.getInput()).addRequire(Mockito.anyString());

        // verify call node is replaced with moduleRef node
        Mockito.verify(parent).replaceChild(require, IR.empty());
        
        // verify reportCodeChange is called
        Mockito.verify(compiler).reportCodeChange();
    }

    @Test
    public void testVisitScript() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node script = IR.empty();

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitScript(t, script);

        // verify addProvide is called
        Mockito.verify(t.getInput()).addProvide(Mockito.anyString());
        
        // verify reportCodeChange is called
        Mockito.verify(compiler).reportCodeChange();
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        Node script = IR.empty();
        String moduleName = "test";

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.emitOptionalModuleExportsOverride(script, moduleName);

        // verify addChildToBack is called
        Mockito.verify(script).addChildToBack(Mockito.any(Node.class));
    }

    @Test
    public void testVisitModuleExports() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        Node prop = IR.empty();

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visitModuleExports(prop);

        Node module = IR.name("test");
        Node exports = IR.empty();

        // verify putProp is called for module
        Mockito.verify(module).putProp(Mockito.anyString(), Mockito.anyString());

        // verify putProp is called for exports
        Mockito.verify(exports).putProp(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSuffixVarsCallback() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String filenamePrefix = ".\\";
        boolean reportDependencies = true;

        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix, reportDependencies);

        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);

        ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("test");
        callback.visit(t, n, parent);

        // verify setString is called
        Mockito.verify(n).setString(Mockito.anyString());

        // verify putProp is called
        Mockito.verify(n).putProp(Mockito.anyString(), Mockito.anyString());
    }
}