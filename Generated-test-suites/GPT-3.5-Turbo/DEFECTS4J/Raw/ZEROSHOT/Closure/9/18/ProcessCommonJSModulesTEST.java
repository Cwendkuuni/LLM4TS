import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules processCommonJSModules;
  private AbstractCompiler compiler;
  private String filenamePrefix;
  
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    filenamePrefix = ".\\";
    processCommonJSModules = new ProcessCommonJSModules(compiler, filenamePrefix);
  }
  
  @Test
  public void testProcess() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);
    
    processCommonJSModules.process(externs, root);
    
    verify(NodeTraversal, times(1)).traverse(compiler, root, processCommonJSModules.new ProcessCommonJsModulesCallback());
  }
  
  @Test
  public void testGuessCJSModuleName() {
    String filename = "test.js";
    assertEquals("module$test",
                 processCommonJSModules.guessCJSModuleName(filename));
  }
  
  @Test
  public void testToModuleName() {
    String filename = "test.js";
    assertEquals("module$test",
                 processCommonJSModules.toModuleName(filename));
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = ".\\module.js";
    String currentFilename = ".\\test.js";
    assertEquals("module$module",
                 processCommonJSModules.toModuleName(requiredFilename, currentFilename));
  }
  
  @Test
  public void testNormalizeSourceName() {
    String filename = ".\\test.js";
    assertEquals("test.js",
                 processCommonJSModules.normalizeSourceName(filename));
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node require = mock(Node.class);
    Node parent = mock(Node.class);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(t, require, parent);
    
    verify(t, times(1)).getSourceName();
    verify(require.getChildAtIndex(1)).getString();
    verify(parent, times(1)).replaceChild(require, IR.name("module$"));
    verify(parent, times(1)).addChildToFront(any(Node.class));
    verify(compiler, times(1)).reportCodeChange();
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node script = mock(Node.class);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(t, script, null);
    
    verify(t, times(1)).getInput();
    verify(t.getInput(), times(1)).addRequire(anyString());
    verify(t.getInput(), times(1)).addProvide(anyString());
    verify(script, times(2)).addChildToFront(any(Node.class));
    verify(compiler, times(1)).reportCodeChange();
  }
  
  @Test
  public void testProcessCommonJsModulesCallbackVisitModuleExports() {
    Node prop = mock(Node.class);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(null, prop, null);
    
    verify(prop, times(1)).getChildAtIndex(0);
    verify(prop, times(1)).getChildAtIndex(1);
  }
  
  @Test
  public void testSuffixVarsCallbackVisit() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);
    Scope.Var var = mock(Scope.Var.class);
    
    when(var.isGlobal()).thenReturn(true);
    when(t.getScope().getVar(anyString())).thenReturn(var);
    
    processCommonJSModules.new SuffixVarsCallback("test").visit(t, n, parent);
    
    verify(n, times(1)).getString();
    verify(t.getScope(), times(1)).getVar(anyString());
    verify(n, times(1)).setString(anyString());
  }
}