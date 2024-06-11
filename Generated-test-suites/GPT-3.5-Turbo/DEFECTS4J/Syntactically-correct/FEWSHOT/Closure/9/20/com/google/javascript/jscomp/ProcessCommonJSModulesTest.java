import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;

public class ProcessCommonJSModulesTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, "");
        Node externs = IR.root();
        Node root = IR.root();
        module.process(externs, root);
        // verify the process results
    }
    
    @Test
    public void testGuessCJSModuleName() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, "");
        String filename = "filename.js";
        String result = module.guessCJSModuleName(filename);
        assertEquals("module$filename", result);
    }
    
    @Test
    public void testToModuleName() {
        String filename = "filename.js";
        String result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$filename", result);
    }
    
    @Test
    public void testToModuleName_withRelativeAddressing() {
        String requiredFilename = "./required.js";
        String currentFilename = "current.js";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$required", result);
    }
    
    @Test
    public void testProcessCommonJsModulesCallback_visit() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = IR.call(IR.name("require"), IR.string("module"));
        Node parent = IR.root();
        callback.visit(t, n, parent);
        // verify the visit results
    }
    
    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node require = IR.call(IR.name("require"), IR.string("module"));
        Node parent = IR.root();
        callback.visitRequireCall(t, require, parent);
        // verify the visitRequireCall results
    }
    
    @Test
    public void testProcessCommonJsModulesCallback_visitScript() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node script = IR.script();
        callback.visitScript(t, script);
        // verify the visitScript results
    }
    
    @Test
    public void testProcessCommonJsModulesCallback_emitOptionalModuleExportsOverride() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        Node script = IR.script();
        String moduleName = "module";
        callback.emitOptionalModuleExportsOverride(script, moduleName);
        // verify the emitOptionalModuleExportsOverride results
    }
    
    @Test
    public void testProcessCommonJsModulesCallback_visitModuleExports() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        Node prop = IR.getprop(IR.name("module"), IR.string("exports"));
        callback.visitModuleExports(prop);
        // verify the visitModuleExports results
    }
    
    @Test
    public void testSuffixVarsCallback_visit() {
        AbstractCompiler compiler = new Compiler();
        ProcessCommonJSModules.SuffixVarsCallback callback = new ProcessCommonJSModules.SuffixVarsCallback("suffix");
        NodeTraversal t = new NodeTraversal(compiler, callback);
        Node n = IR.name("name");
        Node parent = IR.root();
        callback.visit(t, n, parent);
        // verify the visit results
    }
}