import com.google.javascript.jscomp.*;
import com.google.common.collect.Sets;
import org.junit.*;
import org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private NodeTraversal traversal;
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        traversal = mock(NodeTraversal.class);
        processCommonJSModules = new ProcessCommonJSModules(compiler, "DEFAULT_FILENAME_PREFIX");
    }

    @Test
    public void testProcess() {
        // Test code here
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        Assert.assertEquals(expected, moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        Assert.assertEquals(expected, moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./required.js";
        String currentFilename = "test.js";
        String expected = "module$required";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        Assert.assertEquals(expected, moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitRequireCall() {
        Node parent = new Node();
        Node require = new Node();
        require.addChildToFront(new Node("require"));
        require.addChildToBack(new Node("requiredModule"));
        parent.addChildToFront(require);

        processCommonJSModules.process(null, parent);

        String expected = "module$requiredModule";
        String moduleName = parent.getFirstChild().getQualifiedName();
        Assert.assertEquals(expected, moduleName);
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitRequireCallWithReportDependencies() {
        processCommonJSModules = new ProcessCommonJSModules(compiler, "DEFAULT_FILENAME_PREFIX", true);
        Node parent = new Node();
        Node require = new Node();
        require.addChildToFront(new Node("require"));
        require.addChildToBack(new Node("requiredModule"));
        parent.addChildToFront(require);

        processCommonJSModules.process(null, parent);

        String expected = "module$requiredModule";
        String moduleName = parent.getFirstChild().getQualifiedName();
        Assert.assertEquals(expected, moduleName);

        // Verify that the module is added as a dependency
        verify(compiler.getInput()).addRequire(expected);
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitRequireCallWithoutReportDependencies() {
        processCommonJSModules = new ProcessCommonJSModules(compiler, "DEFAULT_FILENAME_PREFIX", false);
        Node parent = new Node();
        Node require = new Node();
        require.addChildToFront(new Node("require"));
        require.addChildToBack(new Node("requiredModule"));
        parent.addChildToFront(require);

        processCommonJSModules.process(null, parent);

        String expected = "module$requiredModule";
        String moduleName = parent.getFirstChild().getQualifiedName();
        Assert.assertEquals(expected, moduleName);

        // Verify that the module is not added as a dependency
        verify(compiler.getInput(), times(0)).addRequire(anyString());
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitScript() {
        Node script = new Node(Node.Type.SCRIPT);
        NodeTraversal t = new NodeTraversal(compiler, traversal);
        t.getInput().addProvide("test");
        t.getInput().addRequire("requiredModule");

        processCommonJSModules.process(null, script);

        String expected = "module$test";
        String moduleName = script.getFirstChild().getQualifiedName();
        Assert.assertEquals(expected, moduleName);

        // Verify that the module is added as a provide
        verify(compiler.getInput()).addProvide(expected);
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitScriptWithoutReportDependencies() {
        processCommonJSModules = new ProcessCommonJSModules(compiler, "DEFAULT_FILENAME_PREFIX", false);
        Node script = new Node(Node.Type.SCRIPT);
        NodeTraversal t = new NodeTraversal(compiler, traversal);
        t.getInput().addProvide("test");
        t.getInput().addRequire("requiredModule");

        processCommonJSModules.process(null, script);

        String expected = "module$test";
        String moduleName = script.getFirstChild().getQualifiedName();
        Assert.assertEquals(expected, moduleName);

        // Verify that the module is not added as a provide
        verify(compiler.getInput(), times(0)).addProvide(anyString());
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitModuleExports() {
        Node moduleExports = new Node(Node.Type.GETPROP);
        Node module = new Node(Node.Type.NAME);
        module.addChildToFront(new Node("module"));
        moduleExports.addChildToFront(module);
        moduleExports.addChildToBack(new Node("exports"));

        processCommonJSModules.process(null, moduleExports);

        String expectedModule = "module$module";
        String expectedExports = "module$exports";
        String moduleName = module.getQualifiedName();
        String exportsName = moduleExports.getQualifiedName();
        Assert.assertEquals(expectedModule, moduleName);
        Assert.assertEquals(expectedExports, exportsName);
    }

    @Test
    public void testSuffixVarsCallbackVisit() {
        Scope.Var var = new Scope.Var("test", null);
        var.setGlobal(true);
        NodeTraversal t = new NodeTraversal(compiler, traversal);
        t.getScope().declare(var);

        Node nameNode = new Node(Node.Type.NAME, "test");

        processCommonJSModules.process(null, nameNode);

        String expected = "test$$module$test";
        String name = nameNode.getString();
        Assert.assertEquals(expected, name);
    }
}