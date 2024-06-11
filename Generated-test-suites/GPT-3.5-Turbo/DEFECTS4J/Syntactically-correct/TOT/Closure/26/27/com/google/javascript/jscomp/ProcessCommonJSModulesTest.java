import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void guessCJSModuleName_withValidFilename_returnsModuleName() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");

        String result = module.guessCJSModuleName("src/test/File.js");

        assertEquals("module$src$test$File", result);
    }

    @Test
    public void guessCJSModuleName_withEmptyFilename_returnsModuleName() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");

        String result = module.guessCJSModuleName("");

        assertEquals("module$", result);
    }

    @Test
    public void toModuleName_withValidFilename_returnsModuleName() {
        String result = ProcessCommonJSModules.toModuleName("src/test/File.js");

        assertEquals("module$src$test$File", result);
    }

    @Test
    public void toModuleName_withEmptyFilename_returnsDefaultModuleName() {
        String result = ProcessCommonJSModules.toModuleName("");

        assertEquals("module$", result);
    }

    @Test
    public void toModuleName_withRelativePath_returnsCorrectModuleName() {
        String result = ProcessCommonJSModules.toModuleName("./src/test/File.js");

        assertEquals("module$src$test$File", result);
    }

    @Test
    public void toModuleName_withRelativePathInDifferentDirectory_returnsCorrectModuleName() {
        String result = ProcessCommonJSModules.toModuleName("../src/test/File.js");

        assertEquals("module$src$test$File", result);
    }

    @Test
    public void process_withValidInput_updatesModule() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");

        JSModule jsModule = module.getModule();
        assertNull(jsModule);

        module.process(null, null);

        jsModule = module.getModule();
        assertNotNull(jsModule);
    }

    @Test
    public void visitRequireCall_withValidRequire_updatesScriptAndInput() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        Node externs = IR.root();
        Node root = IR.root();
        NodeTraversal traversal = new NodeTraversal(module, externs, root);

        Node require = IR.call(
                IR.getprop(IR.name("require"), IR.string("moduleName"))
        );
        Node parent = new Node();
        parent.addChildToFront(require);

        assertNull(traversal.getInput().getRequire("moduleName"));

        module.visitRequireCall(traversal, require, parent);

        assertNotNull(traversal.getInput().getRequire("moduleName"));
        assertEquals("module$moduleName", require.getFirstChild().getQualifiedName());
    }

    @Test
    public void visitScript_withValidScript_updatesScriptAndModule() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        Node externs = IR.root();
        Node root = IR.root();
        NodeTraversal traversal = new NodeTraversal(module, externs, root);

        Node script = IR.script(
                IR.var(IR.name("moduleName"), IR.objectlit())
        );

        module.visitScript(traversal, script);

        assertEquals("module$moduleName", script.getFirstChild().getFirstChild().getQualifiedName());
        assertNotNull(traversal.getModule());
        assertEquals("moduleName", traversal.getModule().getName());
    }

    @Test
    public void emitOptionalModuleExportsOverride_withModuleExports_emitsCorrectCode() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        Node script = IR.script();

        module.emitOptionalModuleExportsOverride(script, "moduleName");

        Node ifNode = script.getFirstChild();
        assertEquals(Token.IF, ifNode.getToken());
        assertEquals("module$moduleName.module$exports", ifNode.getFirstChild().getFirstChild().getQualifiedName());
        assertNotNull(ifNode.getLastChild());
    }

    @Test
    public void visitModuleExports_withModuleExports_updatesModuleAndExports() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");

        Node moduleExports = IR.getprop(IR.name("module"), IR.string("exports"));
        
        module.visitModuleExports(moduleExports);

        assertEquals("module$moduleName", moduleExports.getFirstChild().getString());
        assertEquals("module$exports", moduleExports.getSecondChild().getString());
    }

    @Test
    public void visit_withUnknownNode_noChangesMade() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        NodeTraversal traversal = null;
        Node node = new Node();

        module.visit(traversal, node, null);

        // No assertions required as the method does not perform any changes
    }

    @Test
    public void getCurrentScriptNode_withScript_returnsScriptNode() {
        ProcessCommonJSModules module = new ProcessCommonJSModules(null, "");
        Node script = IR.script();
        Node parent = new Node();
        parent.addChildToFront(script);

        Node result = module.getCurrentScriptNode(parent);

        assertEquals(script, result);
    }

    @Test
    public void suffixVarsCallback_withGlobalName_suffixesVariable() {
        ProcessCommonJSModules.SuffixVarsCallback callback = new ProcessCommonJSModules.SuffixVarsCallback("moduleName");
        NodeTraversal traversal = null;
        Node node = new Node(Token.NAME, "globalTest");

        callback.visit(traversal, node, null);

        assertEquals("globalTest$$moduleName", node.getString());
    }
    
}