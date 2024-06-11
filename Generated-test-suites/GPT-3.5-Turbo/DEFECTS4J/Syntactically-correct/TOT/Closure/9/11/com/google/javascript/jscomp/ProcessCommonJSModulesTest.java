import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.*;

public class ProcessCommonJSModulesTest {

    @Mock
    private AbstractCompiler mockCompiler;
    
    @Mock
    private CompilerInput mockInput;
    
    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processCommonJSModules = new ProcessCommonJSModules(mockCompiler, "test/");
    }

    @Test
    public void testProcess_shouldTraverseRootNode() {
        Node externs = IR.root();
        Node root = IR.root();
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);

        when(mockCompiler.getNodeTraversal()).thenReturn(mockNodeTraversal);

        processCommonJSModules.process(externs, root);

        verify(mockNodeTraversal).traverse(mockCompiler, root, processCommonJSModules.new ProcessCommonJsModulesCallback());
    }

    @Test
    public void testGuessCJSModuleName_shouldReturnExpectedModuleName() {
        String filename = "test/module.js";
        String expectedModuleName = "module$module";

        String actualModuleName = processCommonJSModules.guessCJSModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_shouldReturnExpectedModuleName() {
        String filename = "test/module.js";
        String expectedModuleName = "module$module";

        String actualModuleName = ProcessCommonJSModules.toModuleName(filename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleName_withRelativeAddressing_shouldReturnExpectedModuleName() {
        String requiredFilename = "./test/module.js";
        String currentFilename = "src/main.js";
        String expectedModuleName = "module$module";

        String actualModuleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testProcessCommonJSModulesCallback_visit_shouldConvertRequireCallAndAddToScriptNode() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node requireCall = IR.call(IR.name("require"), IR.string("module$test"));

        when(mockNodeTraversal.getSourceName()).thenReturn("src/module.js");
        when(mockCompiler.getPathForMessageFile(anyString())).thenReturn("src/module.js");
        when(mockNodeTraversal.getInput()).thenReturn(mockInput);
        when(mockCompiler.getNodeTraversal()).thenReturn(mockNodeTraversal);
        when(mockInput.addRequire(anyString())).thenReturn(true);
        when(mockCompiler.reportCodeChange()).thenReturn(true);

        processCommonJSModules.new ProcessCommonJsModulesCallback().visit(mockNodeTraversal, requireCall, parent);

        assertEquals(IR.name("module$test"), parent.getFirstChild());
        assertTrue(parent.getLastChild().isCall());
        assertEquals(IR.getprop(IR.name("goog"), IR.string("require")), parent.getLastChild().getFirstChild());
        assertEquals(IR.string("module$test"), parent.getLastChild().getLastChild());
    }

    @Test
    public void testProcessCommonJSModulesCallback_visit_shouldAddScriptNodeWithVarAndGoogProvide() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node testNode = IR.var(IR.name("module$test"), IR.objectlit());
        Node googProvideNode = IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string("module$test")));

        when(mockNodeTraversal.getSourceName()).thenReturn("src/module.js");
        when(mockCompiler.getPathForMessageFile(anyString())).thenReturn("src/module.js");
        when(mockNodeTraversal.getInput()).thenReturn(mockInput);
        when(mockCompiler.getNodeTraversal()).thenReturn(mockNodeTraversal);
        when(mockInput.addProvide(anyString())).thenReturn(true);
        when(mockCompiler.reportCodeChange()).thenReturn(true);

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.scriptNodeCount = 0;
        callback.visit(mockNodeTraversal, testNode, parent);

        assertEquals(testNode, parent.getFirstChild());
        assertEquals(googProvideNode, parent.getLastChild());
    }

    @Test
    public void testProcessCommonJSModulesCallback_visit_shouldEmitOptionalModuleExportsOverride() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node moduleExportsNode = IR.getprop(IR.name("module$test"), IR.string("module$exports"));
        String moduleName = "module$test";
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
        callback.modulesWithExports.add(moduleName);

        callback.emitOptionalModuleExportsOverride(parent, moduleName);

        assertTrue(parent.getLastChild().isIf());
        assertEquals(moduleExportsNode, parent.getLastChild().getFirstChild());
        assertEquals(IR.block(IR.exprResult(IR.assign(IR.name(moduleName), moduleExportsNode.cloneTree()))), parent.getLastChild().getLastChild());
    }

    @Test
    public void testProcessCommonJSModulesCallback_visit_shouldRewriteModuleExports() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node moduleExportsNode = IR.getprop(IR.name("module$test"), IR.string("module$exports"));

        processCommonJSModules.new ProcessCommonJsModulesCallback().visit(mockNodeTraversal, moduleExportsNode, parent);

        assertEquals("module$test", moduleExportsNode.getChildAtIndex(0).getString());
        assertEquals("module$exports", moduleExportsNode.getChildAtIndex(1).getString());
    }

    @Test
    public void testSuffixVarsCallback_visit_shouldSuffixExportName() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node exportsNode = IR.name("exports");

        processCommonJSModules.new SuffixVarsCallback("module").visit(mockNodeTraversal, exportsNode, parent);

        assertEquals("module", exportsNode.getString());
        assertEquals("exports", exportsNode.getProp(Node.ORIGINALNAME_PROP));
    }

    @Test
    public void testSuffixVarsCallback_visit_shouldSuffixGlobalVars() {
        NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
        Node parent = IR.root();
        Node varNode = IR.name("variable");
        Scope.Var mockVar = mock(Scope.Var.class);

        when(mockNodeTraversal.getScope()).thenReturn(mock(Scope.class));
        when(mockNodeTraversal.getScope().getVar(anyString())).thenReturn(mockVar);
        when(mockVar.isGlobal()).thenReturn(true);

        processCommonJSModules.new SuffixVarsCallback("module").visit(mockNodeTraversal, varNode, parent);

        assertEquals("variable$$module", varNode.getString());
        assertEquals("variable", varNode.getProp(Node.ORIGINALNAME_PROP));
    }
}