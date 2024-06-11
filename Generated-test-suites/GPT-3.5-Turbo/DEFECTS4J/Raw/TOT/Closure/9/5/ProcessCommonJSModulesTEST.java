import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

    @Mock
    private AbstractCompiler mockCompiler;

    @Mock
    private CompilerInput mockCompilerInput;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process_shouldTraverseTree() {
        // Arrange
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(mockCompiler, "test");

        // Act
        processCommonJSModules.process(null, null);

        // Assert
        // Verify that the traversal method of the compiler is called
        verify(mockCompiler, times(1))
            .traverse(eq(null), eq(null), any(ProcessCommonJSModules.ProcessCommonJsModulesCallback.class));
    }

    @Test
    public void guessCJSModuleName_validFilename_shouldReturnCorrectModuleName() {
        // Arrange
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "test");

        // Act
        String moduleName = processCommonJSModules.guessCJSModuleName("test/module.js");

        // Assert
        Assert.assertEquals("module$test$module", moduleName);
    }

    @Test
    public void toModuleName_validFilename_shouldReturnCorrectModuleName() {
        // Arrange
        String filename = "test/module.js";

        // Act
        String moduleName = ProcessCommonJSModules.toModuleName(filename);

        // Assert
        Assert.assertEquals("module$test$module", moduleName);
    }

    @Test
    public void toModuleName_relativeFilename_shouldResolveCorrectModuleName() {
        // Arrange
        String requiredFilename = "./module.js";
        String currentFilename = "test/index.js";

        // Act
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

        // Assert
        Assert.assertEquals("module$test$module", moduleName);
    }

    @Test
    public void normalizeSourceName_validFilename_shouldReturnNormalizedFilename() {
        // Arrange
        String filename = "test/module.js";
        ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(null, "test/");

        // Act
        String normalizedFilename = processCommonJSModules.normalizeSourceName(filename);

        // Assert
        Assert.assertEquals("module.js", normalizedFilename);
    }

    @Test
    public void visitRequireCall_shouldReplaceRequireNode() {
        // Arrange
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node require = NodeUtil.newQName(mockCompiler, "require");
        Node call = new Node(Token.CALL, require);
        call.addChildToFront(NodeUtil.newString(mockCompiler, "test"));

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        Node script = NodeUtil.newQName(mockCompiler, "script");
        callback.visitScript(nodeTraversal, script);

        // Act
        callback.visitRequireCall(nodeTraversal, call, script);

        // Assert
        // Verify that the require node is replaced with the module reference node
        Assert.assertEquals(1, script.getChildCount());
        Node moduleRef = script.getFirstChild();
        Assert.assertEquals("module$test$test", moduleRef.getQualifiedName());
        Assert.assertEquals(Token.NAME, moduleRef.getToken());
    }

    @Test
    public void visitScript_shouldAddProvideAndSuffixGlobalVars() {
        // Arrange
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node script = NodeUtil.newQName(mockCompiler, "script");

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

        // Act
        callback.visitScript(nodeTraversal, script);

        // Assert
        // Verify that the provide and suffix variables are added to the script node
        Assert.assertEquals("module$test$script", script.getFirstChild().getQualifiedName());
        Assert.assertEquals("goog.provide('module$test$script');", script.getFirstChild().toSource());

        Assert.assertEquals(2, script.getChildCount());
        Assert.assertEquals("goog.provide('module$test$script');", script.getFirstChild().toSource());
        Assert.assertEquals("var module$test$script = {};", script.getLastChild().toSource());
    }

    @Test
    public void emitOptionalModuleExportsOverride_withModuleExports_shouldAddModuleExportsOverrideAtEndOfFile() {
        // Arrange
        Node script = NodeUtil.newQName(mockCompiler, "script");
        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
        callback.visitScript(mock(NodeTraversal.class), script);

        // Act
        callback.emitOptionalModuleExportsOverride(script, "module$test$script");

        // Assert
        // Verify that the optional module exports override is added at the end of the script
        Assert.assertEquals(2, script.getChildCount());
        Node ifNode = script.getLastChild();
        Assert.assertEquals(Token.IF, ifNode.getToken());
        Assert.assertEquals("if (module$test$script.module$exports) { module$test$script = module$test$script.module$exports; }", ifNode.toSource());
    }

    @Test
    public void visitModuleExports_shouldRewriteModuleExports() {
        // Arrange
        Node moduleExports = NodeUtil.newQName(mockCompiler, "module.exports");
        Node module = NodeUtil.newQName(mockCompiler, "module");
        moduleExports.addChildToFront(module);
        moduleExports.addChildToBack(NodeUtil.newString(mockCompiler, "test"));

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

        // Act
        callback.visitModuleExports(moduleExports);

        // Assert
        // Verify that the module.exports node is rewritten
        Assert.assertEquals("module$test$module", module.getQualifiedName());
        Assert.assertEquals("module$test$module.module$exports", moduleExports.getQualifiedName());
    }

    @Test
    public void visit_shouldCallCorrectCallbackMethod() {
        // Arrange
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node require = NodeUtil.newQName(mockCompiler, "require");
        Node moduleExports = NodeUtil.newQName(mockCompiler, "module.exports");
        Node script = NodeUtil.newQName(mockCompiler, "script");

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = spy(new ProcessCommonJSModules.ProcessCommonJsModulesCallback());

        // Act
        callback.visit(nodeTraversal, require, null);
        callback.visit(nodeTraversal, script, null);
        callback.visit(nodeTraversal, moduleExports, null);

        // Assert
        // Verify that the correct callback methods are called for each node type
        verify(callback, times(1)).visitRequireCall(eq(nodeTraversal), eq(require), eq(null));
        verify(callback, times(1)).visitScript(eq(nodeTraversal), eq(script));
        verify(callback, times(1)).visitModuleExports(eq(moduleExports));
    }

    @Test
    public void getCurrentScriptNode_inNestedScript_shouldReturnOuterScriptNode() {
        // Arrange
        Node outerScript = NodeUtil.newQName(mockCompiler, "script");
        Node nestedScript = NodeUtil.newQName(mockCompiler, "nestedScript");
        outerScript.addChildToFront(nestedScript);

        ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

        // Act
        Node currentScriptNode = callback.getCurrentScriptNode(nestedScript);

        // Assert
        // Verify that the correct script node is returned
        Assert.assertEquals(outerScript, currentScriptNode);
    }

    @Test
    public void suffixVarsCallback_visit_shouldSuffixGlobalVars() {
        // Arrange
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node name = NodeUtil.newName(mockCompiler, "test");
        Node varNode = IR.var(name);
        varNode.addChildToBack(NodeUtil.newObject(mockCompiler));

        ProcessCommonJSModules.SuffixVarsCallback callback = new ProcessCommonJSModules.SuffixVarsCallback("suffix");

        // Act
        callback.visit(nodeTraversal, name, null);

        // Assert
        // Verify that the global variable name is suffixed
        Assert.assertEquals("test$$suffix", name.getString());
        Assert.assertEquals(Token.NAME, name.getToken());
        Assert.assertEquals("originalName", name.getProp(Node.ORIGINALNAME_PROP));
    }

}