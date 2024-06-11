
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    // Sample test data
    private static final String FILENAME_PREFIX = "./";
    private static final String MODULE_NAME = "module$example$test";
    private static final String SOURCE_FILENAME = "example/test.js";

    @Test
    public void testToModuleName() {
        String filename = "./foo/bar.js";
        String expectedModuleName = "module$foo$bar";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        Assert.assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testToModuleNameWithRelativePath() {
        String requiredFilename = "./baz/qux.js";
        String currentFilename = "foo/bar.js";
        String expectedModuleName = "module$baz$qux";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        Assert.assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, FILENAME_PREFIX);
        String filename = FILENAME_PREFIX + SOURCE_FILENAME;
        String expectedModuleName = MODULE_NAME;
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        Assert.assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitRequireCall() {
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);
        Node require = IR.call(IR.name("require"), IR.string("./module"));
        Node parent = IR.root(IR.root(), require);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
        nodeTraversal.traverse(null, parent);
        Node expectedModuleRef = IR.name("module$module");
        Assert.assertEquals(expectedModuleRef, parent.getFirstChild());
        Node expectedScript = IR.var(IR.name("module$example$test"), IR.objectlit());
        Assert.assertEquals(expectedScript, parent.getNext());
    }

    @Test
    public void testProcessCommonJsModulesCallback_visitModuleExports() {
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);
        Node moduleExports = IR.getprop(IR.name("module"), IR.string("exports"));
        Node prop = IR.getprop(moduleExports.cloneTree(), IR.string("example"));
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
        nodeTraversal.traverse(null, prop);
        String expectedModuleName = MODULE_NAME;
        Assert.assertEquals(expectedModuleName, prop.getChildAtIndex(0).getString());
        String expectedExports = "module$exports";
        Assert.assertEquals(expectedExports, prop.getChildAtIndex(1).getString());
    }

    @Test
    public void testSuffixVarsCallback_visit() {
        AbstractCompiler compiler = new AbstractCompiler();
        ProcessCommonJSModules.SuffixVarsCallback suffixVarsCallback = new ProcessCommonJSModules.SuffixVarsCallback("suffix");
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, suffixVarsCallback);
        Node nameNode = IR.name("varName");
        nodeTraversal.traverse(null, nameNode);
        String expectedVarName = "varName$$suffix";
        Assert.assertEquals(expectedVarName, nameNode.getString());
    }

    @Test
    public void testProcess() {
        // Test data
        Node externs = IR.root();
        Node root = IR.root();
        AbstractCompiler compiler = new AbstractCompiler();
        CompilerInput ci = new CompilerInput(IR.root(), SOURCE_FILENAME, false);
        NodeTraversal.traverse(compiler, root, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());

        // Perform the process
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);
        processCommonJSModules.process(externs, root);

        // Validate the result
        JSModule jsModule = processCommonJSModules.getModule();
        Assert.assertNotNull(jsModule);
        Assert.assertEquals(MODULE_NAME, jsModule.getName());
        Assert.assertEquals(ci, jsModule.getInputs().get(0));
    }
}
