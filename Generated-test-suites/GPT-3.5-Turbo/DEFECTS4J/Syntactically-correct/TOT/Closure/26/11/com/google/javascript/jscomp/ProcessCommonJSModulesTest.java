import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private ProcessCommonJSModules pass;

    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        pass = new ProcessCommonJSModules(compiler, ".");
    }

    @Test
    public void testProcess_moduleNameValid_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("./test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validModuleNameWithRelativeAddressing_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("./test.js", "index.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validModuleNameWithRelativeAddressingAndParentDirectory_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("../test.js", "js/module/index.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validModuleNameWithRelativeAddressingAndCurrentDirectory_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("./test.js", "module/index.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validModuleNameWithRelativeAddressingAndCurrentDirectoryAndParentDirectory_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("../test.js", "module2/index.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_moduleNameWithJSExtension_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("test.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_requiredFilenameWithJSExtension_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("test.js", "index.js");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validRequiredFilenameAndCurrentFilename_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("./test", "index");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_toModuleName_validRequiredFilenameAndParentDirectoryOfCurrentFilename_returnsCorrectModuleName() {
        String moduleName = pass.toModuleName("../test", "js/module/index");
        assertEquals("module$test", moduleName);
    }

    @Test
    public void testProcess_processRequireCall_validRequireCall_rewritesRequireModuleReference() {
        Node externs = new Node(Node.FILES);
        Node root = new Node(Node.SCRIPT);
        Node call = new Node(Node.CALL);
        Node requireName = new Node(Node.STRING);
        requireName.setString("require");
        Node requireArg = new Node(Node.STRING);
        requireArg.setString("./test.js");
        call.addChildToBack(requireName);
        call.addChildToBack(requireArg);
        Node script = new Node(Node.SCRIPT);
        script.addChildToBack(call);
        root.addChildToBack(script);

        pass.process(externs, root);

        Node moduleRef = call.getFirstChild().getNext();
        assertEquals("module$test", moduleRef.getQualifiedName());
    }

    @Test
    public void testProcess_processRequireCall_validRequireCall_addsGoogRequire() {
        Node externs = new Node(Node.FILES);
        Node root = new Node(Node.SCRIPT);
        Node call = new Node(Node.CALL);
        Node requireName = new Node(Node.STRING);
        requireName.setString("require");
        Node requireArg = new Node(Node.STRING);
        requireArg.setString("./test.js");
        call.addChildToBack(requireName);
        call.addChildToBack(requireArg);
        Node script = new Node(Node.SCRIPT);
        script.addChildToBack(call);
        root.addChildToBack(script);

        pass.process(externs, root);

        Node googRequire = root.getFirstChild().getFirstChild();
        assertEquals("goog.require", googRequire.getFirstChild().getQualifiedName());
        assertEquals("module$test", googRequire.getSecondChild().getString());
    }

    @Test
    public void testProcess_processScriptNode_validScriptNode_addsModuleVariable() {
        Node externs = new Node(Node.FILES);
        Node root = new Node(Node.SCRIPT);
        Node script = new Node(Node.SCRIPT);
        root.addChildToBack(script);

        pass.process(externs, root);

        Node var = script.getFirstChild();
        assertEquals("module$test", var.getFirstChild().getString());
    }

    @Test
    public void testProcess_processScriptNode_validScriptNode_addsGoogProvide() {
        Node externs = new Node(Node.FILES);
        Node root = new Node(Node.SCRIPT);
        Node script = new Node(Node.SCRIPT);
        root.addChildToBack(script);

        pass.process(externs, root);

        Node googProvide = root.getFirstChild();
        assertEquals("goog.provide", googProvide.getFirstChild().getQualifiedName());
        assertEquals("module$test", googProvide.getSecondChild().getString());
    }

    @Test
    public void testProcess_processScriptNode_validScriptNode_emitsOptionalModuleExportsOverride() {
        Node externs = new Node(Node.FILES);
        Node root = new Node(Node.SCRIPT);
        Node script = new Node(Node.SCRIPT);
        root.addChildToBack(script);

        pass.process(externs, root);

        Node ifNode = script.getLastChild();
        assertEquals(Node.IF, ifNode.getToken());
        assertEquals("module$test.module$exports", ifNode.getFirstChild().getLastChild().getString());
        assertEquals("module$test", ifNode.getFirstChild().getFirstChild().getString());
    }

    @Test
    public void testProcess_visitModuleExports_validModuleExport_rewritesModuleExports() {
        Node moduleExports = new Node(Node.GETPROP);
        Node module = new Node(Node.NAME);
        module.setString("module");
        Node exports = new Node(Node.NAME);
        exports.setString("exports");
        moduleExports.addChildToBack(module);
        moduleExports.addChildToBack(exports);

        pass.visitModuleExports(moduleExports);

        assertEquals("module$test.module$exports", moduleExports.getFirstChild().getLastChild().getString());
        assertEquals("module$test", moduleExports.getFirstChild().getFirstChild().getString());
    }

    @Test
    public void testProcess_suffixVarsCallback_validName_noSuffixAddedToExports() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node name = new Node(Node.NAME);
        name.setString("exports");
        Node parent = new Node(Node.SCRIPT);
        Scope.Var var = new Scope.Var("exports", parent);
        var.setGlobal();
        t.getScope().declare("exports", var, null, null);

        pass.new SuffixVarsCallback("test").visit(t, name, parent);

        assertEquals("exports", name.getString());
    }

    @Test
    public void testProcess_suffixVarsCallback_globalVar_renamesWithSuffix() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node name = new Node(Node.NAME);
        name.setString("varName");
        Node parent = new Node(Node.SCRIPT);
        Scope.Var var = new Scope.Var("varName", parent);
        var.setGlobal();
        t.getScope().declare("varName", var, null, null);

        pass.new SuffixVarsCallback("test").visit(t, name, parent);

        assertEquals("varName$$test", name.getString());
    }
}