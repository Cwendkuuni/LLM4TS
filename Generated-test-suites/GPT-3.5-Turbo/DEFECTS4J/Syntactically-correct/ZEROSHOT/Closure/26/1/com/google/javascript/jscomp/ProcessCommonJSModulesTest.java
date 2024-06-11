import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private AbstractCompiler compiler;
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        processCommonJSModules = new ProcessCommonJSModules(compiler, "src/main/resources/");
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "src/main/resources/module1.js";
        String expectedResult = "module$module1";
        String actualResult = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToModuleName() {
        String filename = "src/main/resources/module1.js";
        String expectedResult = "module$module1";
        String actualResult = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToModuleNameWithRelativePath() {
        String requiredFilename = "./module2.js";
        String currentFilename = "src/main/resources/module1.js";
        String expectedResult = "module$module2";
        String actualResult = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        ProcessCommonJSModulesCallback callback = mock(ProcessCommonJSModulesCallback.class);
        processCommonJSModules.process(externs, root);
        verify(NodeTraversal.traverse(compiler, root, callback)).once();
    }

    @Test
    public void testProcessCommonJsModulesCallback() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.visit(traversal, node, parent);
        verify(traversal, times(1)).getSourceName();
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node require = mock(Node.class);
        Node parent = mock(Node.class);
        Node script = mock(Node.class);
        when(parent.replaceChild(eq(require), any(Node.class))).thenReturn(require);
        when(processCommonJSModules.getCurrentScriptNode(parent)).thenReturn(script);
        traversal.getInput().addRequire("module$module2");
        processCommonJSModules.reportDependencies = true;

        callback.visitRequireCall(traversal, require, parent);

        verify(parent, times(1)).replaceChild(eq(require), any(Node.class));
        verify(traversal.getInput(), times(1)).addRequire("module$module2");
        verify(script, times(1)).addChildToFront(any(Node.class));
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitRequireCallWithoutReportDependencies() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node require = mock(Node.class);
        Node parent = mock(Node.class);
        Node script = mock(Node.class);
        when(parent.replaceChild(eq(require), any(Node.class))).thenReturn(require);
        when(processCommonJSModules.getCurrentScriptNode(parent)).thenReturn(script);
        processCommonJSModules.reportDependencies = false;

        callback.visitRequireCall(traversal, require, parent);

        verify(parent, times(1)).replaceChild(eq(require), any(Node.class));
        verify(traversal.getInput(), never()).addRequire(anyString());
        verify(script, times(1)).addChildToFront(any(Node.class));
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisitScript() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node script = mock(Node.class);
        CompilerInput input = mock(CompilerInput.class);
        JSModule m = mock(JSModule.class);
        when(traversal.getInput()).thenReturn(input);
        when(traversal.getInput().addProvide(anyString())).thenReturn(true);
        when(traversal.getInput().addProvide("module$module1")).thenReturn(true);
        when(traversal.getInput().addModule(any(JSModule.class))).thenReturn(true);
        when(traversal.getInput().addRequire(anyString())).thenReturn(true);
        when(traversal.getInput().addRequire("module$module1")).thenReturn(true);
        when(compiler.getModule("module$module1")).thenReturn(m);

        callback.visitScript(traversal, script);

        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = mock(Node.class);
        String moduleName = "module$module1";

        callback.emitOptionalModuleExportsOverride(script, moduleName);

        verify(script, times(1)).addChildToBack(any(Node.class));
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = mock(Node.class);
        String moduleName = "module$module1";

        callback.visitModuleExports(prop);

        verify(prop.getChildAtIndex(0), times(1)).setString(moduleName);
        verify(prop.getChildAtIndex(1), times(1)).setString("module$exports");
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        Node name = mock(Node.class);
        when(node.isName()).thenReturn(true);
        when(node.getString()).thenReturn("var1");
        when(name.getString()).thenReturn("var1$$module$module1");
        when(traversal.getScope()).thenReturn(mock(Scope.class));
        when(traversal.getScope().getVar(anyString())).thenReturn(mock(Scope.Var.class));

        callback.visit(traversal, node, parent);

        verify(node, times(1)).setString("var1$$module$module1");
        verify(compiler, times(1)).reportCodeChange();
    }
}