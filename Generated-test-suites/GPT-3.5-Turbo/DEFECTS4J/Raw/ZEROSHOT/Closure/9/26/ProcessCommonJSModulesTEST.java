import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {
    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
        String filename = "test.js";
        String expected = "module$test";
        String result = pcm.guessCJSModuleName(filename);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToModuleName() {
        String filename = "./test.js";
        String expected = "module$test";
        String result = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "../foo.js";
        String currentFilename = "test.js";
        String expected = "module$foo";
        String result = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, result);
    }
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        String filenamePrefix = ".\\";
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, filenamePrefix);
        pcm.process(new Node(), new Node());
        // Make assertions
    }
    
    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal();
        Node require = new Node();
        Node parent = new Node();
        
        String moduleName = "module$test";
        Node moduleRef = new Node();
        parent.replaceChild(require, moduleRef);
        
        Node script = new Node();
        script.addChildToFront(IR.var(IR.name(moduleName), IR.objectlit()));
        script.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string(moduleName))));
        String expectedScript = script.toString();
        
        ProcessCommonJSModulesCallback pcmcb = new ProcessCommonJSModulesCallback();
        pcmcb.visitRequireCall(t, require, parent);
        
        assertEquals(expectedScript, script.toString());
        // Make assertions
    }
    
    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal();
        Node script = new Node();
        ProcessCommonJSModulesCallback pcmcb = new ProcessCommonJSModulesCallback();
        pcmcb.visitScript(t, script);
        // Make assertions
    }
    
    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node();
        String moduleName = "module$test";
        ProcessCommonJSModulesCallback pcmcb = new ProcessCommonJSModulesCallback();
        pcmcb.emitOptionalModuleExportsOverride(script, moduleName);
        // Make assertions
    }
    
    @Test
    public void testVisitModuleExports() {
        Node prop = new Node();
        String moduleName = "module$test";
        Node module = new Node();
        module.setString("module");
        Node exports = new Node();
        exports.setString("exports");
        prop.addChildToBack(module);
        prop.addChildToBack(exports);
        ProcessCommonJSModulesCallback pcmcb = new ProcessCommonJSModulesCallback();
        pcmcb.visitModuleExports(prop);
        // Make assertions
    }
    
    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        String suffix = "test";
        String expected = "name$$test";
        
        NodeTraversal.Callback sc = new SuffixVarsCallback(suffix);
        sc.visit(t, n, parent);
        
        assertEquals(expected, n.getString());
    }
}