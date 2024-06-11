import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

    @Mock
    AbstractCompiler compiler;

    @Mock
    JSModule module;

    ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setup() {
        compiler = mock(AbstractCompiler.class);
        module =mock(JSModule.class);
        processCommonJSModules = new ProcessCommonJSModules(compiler, ".//");
    }

    @Test
    public void guessCJSModuleName_validFileName_returnValidModuleName() {
        String filename = "src/main/java/com/example/test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$com$example$test", moduleName);
    }
    
    @Test
    public void guessCJSModuleName_fileNameWithRelativePath_returnValidModuleName() {
        String filename = "./src/main/java/com/example/test.js";
        String moduleName = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals("module$com$example$test", moduleName);
    }

    @Test
    public void toModuleName_validFileName_returnValidModuleName() {
        String filename = "src/main/java/com/example/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals("module$com$example$test", moduleName);
    }

    @Test
    public void toModuleName_relativeFileName_returnValidModuleName() {
        String requiredFilename = "./util";
        String currentFilename = "src/main/java/com/example/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$com$example$util", moduleName);
    }

    @Test
    public void toModuleName_relativeFileNameWithParent_returnValidModuleName() {
        String requiredFilename = "../util";
        String currentFilename = "src/main/java/com/example/test.js";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals("module$com$util", moduleName);
    }

    @Test
    public void process_validExternsAndRoot_callTraverse() {
        Node externs = IR.script();
        Node root = IR.script();
        NodeTraversal traverser = mock(NodeTraversal.class);
        ProcessCommonJsModulesCallback callback = spy(ProcessCommonJSModulesCallback.class);
        
        when(callback.traverse(compiler, root, callback)).thenReturn(null);
        when(compiler.getErrorCount()).thenReturn(0);
        when(compiler.getWarningCount()).thenReturn(0);
        when(callback.getModule()).thenReturn(module);
        processCommonJSModules = spy(processCommonJSModules);
        doReturn(callback).when(processCommonJSModules).new ProcessCommonJsModulesCallback();
        doReturn(traverser).when(compiler).getCallback();
        
        processCommonJSModules.process(externs, root);
        
        verify(traverser, times(1)).traverse(compiler, root, callback);
        verify(callback, times(1)).getModule();
        
    }

    @Test
    public void visit_validRequireCall_correctlyRewriteRequireCall() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node require = IR.call(IR.name("require"), IR.string("./util"));
        Node parent = IR.exprResult(require);

        processCommonJSModules.visitRequireCall(t, require, parent);

        assertEquals("module$util", require.getChildAtIndex(1).getString());
        assertEquals("goog.require('module$util');", 
            processCommonJSModules.module.getStatements().get(0).toString());
        verify(compiler, times(1)).reportCodeChange();
    }
    @Test
    public void visit_validRequireCall_reportDependenciesTrue_addRequireCalled() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node require = IR.call(IR.name("require"), IR.string("./util"));
        Node parent = IR.exprResult(require);
        processCommonJSModules = new ProcessCommonJSModules(compiler, ".//", true);
        CompilerInput ci = mock(CompilerInput.class);
        when(t.getInput()).thenReturn(ci);

        processCommonJSModules.visitRequireCall(t, require, parent);

        verify(ci, times(1)).addRequire("module$util");
    }

    @Test
    public void visitScript_validScript_correctlyRewriteScript() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node script = IR.script();
        Node child1 = IR.var(IR.name("name"), IR.objectlit());
        Node child2 = IR.var(IR.name("anotherName"), IR.objectlit());
        script.addChildToBack(child1);
        script.addChildToBack(child2);
        CompilerInput ci = mock(CompilerInput.class);
        when(t.getInput()).thenReturn(ci);
        processCommonJSModules.reportDependencies = true;
        processCommonJSModules.visitScript(t, script);

        assertEquals("goog.provide('module$com$example$test');", 
            processCommonJSModules.module.getStatements().get(0).toString());
        assertEquals("name = {};", child1.toString());
        assertEquals("anotherName = {};", child2.toString());
        verify(ci, times(1)).addProvide("module$com$example$test");
    }

    @Test
    public void visitScript_validScript_reportDependenciesTrue_addProvideCalled() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node script = IR.script();
        Node child1 = IR.var(IR.name("name"), IR.objectlit());
        Node child2 = IR.var(IR.name("anotherName"), IR.objectlit());
        script.addChildToBack(child1);
        script.addChildToBack(child2);
        CompilerInput ci = mock(CompilerInput.class);
        when(t.getInput()).thenReturn(ci);
        processCommonJSModules.reportDependencies = true;
        processCommonJSModules.visitScript(t, script);
        verify(ci, times(1)).addProvide("module$com$example$test");
    }

    @Test
    public void visitScript_validScript_modulesWithExportsContainModuleName_emitOptionalModuleExportsOverrideCalled() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node script = IR.script();
        Node child1 = IR.getprop(IR.name("module.exports"));
        script.addChildToBack(child1);
        CompilerInput ci = mock(CompilerInput.class);
        when(t.getInput()).thenReturn(ci);
        processCommonJSModules.reportDependencies = true;
        processCommonJSModules.modulesWithExports.add("module$com$example$test");

        processCommonJSModules.visitScript(t, script);

        assertEquals("goog.provide('module$com$example$test');",
            processCommonJSModules.module.getStatements().get(0).toString());
        assertEquals("if(module$com$example$test.module$exports){module$com$example$test = module$com$example$test.module$exports;}\n",
            script.getLastChild().toString());
    }

    @Test
    public void visitModuleExports_validModuleExports_correctlyRewriteModuleExports() {
        Node prop = IR.getprop(IR.name("module.exports"), IR.objectlit());
        processCommonJSModules.visitModuleExports(prop);
        assertEquals("module$com$example$test.module$exports",
            prop.getFirstChild().getString());
        assertEquals("module$exports", 
            prop.getFirstChild().getNext().getString());
        assertEquals("module$com$example$test",
            prop.getFirstChild().getChildAtIndex(0).getString());
        assertEquals("module", 
            prop.getFirstChild().getChildAtIndex(1).getString());
        assertEquals("exports", 
            prop.getFirstChild().getChildAtIndex(1).getNext().getString());
        assertEquals(true, 
            processCommonJSModules.modulesWithExports.contains("module$com$example$test"));
    }

    @Test
    public void SuffixVarsCallback_validNodeWithGlobalVar_appendSuffixToGlobalVar() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node var = IR.name("name");
        processCommonJSModules.new SuffixVarsCallback("suffix")
            .visit(t, var, IR.block(var));

        assertEquals("name$$suffix", var.getString());
        assertEquals("name", var.getProp(Node.ORIGINALNAME_PROP));
    }
    
    @Test
    public void SuffixVarsCallback_validNodeWithExports_appendSuffixToExports() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node var = IR.name("exports");
        processCommonJSModules.new SuffixVarsCallback("suffix")
            .visit(t, var, IR.block(var));

        assertEquals("suffix", var.getString());
        assertEquals("exports", var.getProp(Node.ORIGINALNAME_PROP));
    }

    @Test
    public void SuffixVarsCallback_validNodeWithGlobalVarNotInScope_skipSuffixToGlobalVar() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node var = IR.name("name");
        Scope.Var scopeVar = mock(Scope.Var.class);
        when(t.getScope()).thenReturn(new Scope(null));
        when(t.getScope().getVar("name")).thenReturn(null);
        processCommonJSModules.new SuffixVarsCallback("suffix")
            .visit(t, var, IR.block(var));

        assertEquals("name", var.getString());
        verify(t, times(1)).getScope();
        verify(t.getScope(), times(1)).getVar("name");
    }
}