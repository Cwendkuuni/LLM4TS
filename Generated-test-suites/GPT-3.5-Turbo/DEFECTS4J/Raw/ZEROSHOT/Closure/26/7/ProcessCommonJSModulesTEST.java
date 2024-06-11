import junit.framework.TestCase;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest extends TestCase {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename1", "filename2"));
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        processCommonJSModules.process(null, null);
    }

    @Test
    public void testVisitRequireCall() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        Node require = new Node(NodeBuilder.createCall().withFirstChild(IR.name("require")).withSecondChild(IR.string("module")).node());
        Node parent = new Node(NodeBuilder.createScript().node());
        processCommonJSModules.visitRequireCall(null, require, parent);
    }

    @Test
    public void testVisitScript() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        Node script = new Node(NodeBuilder.createScript().node());
        processCommonJSModules.visitScript(null, script);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        Node script = new Node(NodeBuilder.createScript().node());
        processCommonJSModules.emitOptionalModuleExportsOverride(script, "moduleName");
    }

    @Test
    public void testVisitModuleExports() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        Node prop = new Node(NodeBuilder.createGetProp().withFirstChild(IR.name("module")).withSecondChild(IR.string("exports")).node());
        processCommonJSModules.visitModuleExports(prop);
    }

    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "");
        ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback = new ProcessCommonJSModules.SuffixVarsCallback("suffix");
        NodeTraversal t = null;
        Node n = new Node(NodeBuilder.createName("name").node());
        Node parent = new Node(NodeBuilder.createScript().node());
        suffixVarsCallback.visit(t, n, parent);
    }
}