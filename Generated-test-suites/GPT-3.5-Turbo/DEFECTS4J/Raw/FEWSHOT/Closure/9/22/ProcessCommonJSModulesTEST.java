package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processCommonJSModules;
  private NodeTraversal nodeTraversal;
  private Node externs;
  private Node root;
  
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    processCommonJSModules = new ProcessCommonJSModules(compiler, DEFAULT_FILENAME_PREFIX);
    nodeTraversal = mock(NodeTraversal.class);
    externs = new Node(Token.SCRIPT);
    root = new Node(Token.SCRIPT);
  }
  
  @Test
  public void testProcess_commonJSModuleProcessed() {
    Node requireNode = new Node(Token.CALL, new Node(Token.NAME, "require"),
        new Node(Token.STRING, "moduleName"));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(requireNode);
    root.addChildToBack(scriptNode);
    
    Node expected = new Node(Token.NAME, "module$moduleName");
    processCommonJSModules.process(externs, root);
    
    assertEquals(expected, root.getLastChild().getLastChild().getFirstChild().getFirstChild());
  }
  
  @Test
  public void testVisit_requireCallRewritten() {
    Node requireNode = new Node(Token.CALL, new Node(Token.NAME, "require"),
        new Node(Token.STRING, "moduleName"));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(requireNode);
    root.addChildToBack(scriptNode);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(nodeTraversal, requireNode, scriptNode);
    
    Node expected = new Node(Token.NAME, "module$moduleName");
    expected.putBooleanProp(Node.IGNORE_REQUIRE, true);
    expected.copyInformationFrom(requireNode, false);
    assertEquals(expected, scriptNode.getLastChild());
  }
  
  @Test
  public void testVisit_scriptNodeProcessed() {
    Node scriptNode = new Node(Token.SCRIPT);
    root.addChildToBack(scriptNode);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(nodeTraversal, scriptNode, root);
    
    Node expected = new Node(Token.VAR, new Node(Token.NAME, "module$filename"),
        new Node(Token.OBJECTLIT));
    expected.putBooleanProp(Node.IS_CLOSURE_MODULE, true);
    expected.copyInformationFrom(scriptNode.getFirstChild(), false);
    assertEquals(expected, scriptNode.getFirstChild());
  }
  
  @Test
  public void testVisit_moduleExportsProcessed() {
    Node moduleExportsNode = new Node(Token.GETPROP, new Node(Token.NAME, "module"),
        new Node(Token.STRING, "exports"));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(moduleExportsNode);
    root.addChildToBack(scriptNode);
    
    processCommonJSModules.new ProcessCommonJsModulesCallback().visit(nodeTraversal, moduleExportsNode, scriptNode);
    
    Node expectedModule = new Node(Token.NAME, "module$filename");
    expectedModule.setString("module$moduleName");
    expectedModule.putProp(Node.ORIGINALNAME_PROP, "module");
    
    Node expectedExports = new Node(Token.STRING, "module$exports");
    expectedExports.putProp(Node.ORIGINALNAME_PROP, "exports");
    
    Node expected = new Node(Token.GETPROP, expectedModule, expectedExports);
    expected.copyInformationFrom(moduleExportsNode, false);
    assertEquals(expected, moduleExportsNode);
  }
  
  @Test
  public void testSuffixVarsCallback_suffixAdded() {
    String suffix = "moduleName";
    
    Node nameNode = new Node(Token.NAME, "name");
    Node exportsNode = new Node(Token.NAME, "exports");
    
    NodeTraversal t = mock(NodeTraversal.class);
    Scope.Var var = mock(Scope.Var.class);
    
    Node expectedName = new Node(Token.NAME, "name$moduleName");
    expectedName.putProp(Node.ORIGINALNAME_PROP, "name");
    Node expectedExports = new Node(Token.NAME, "moduleName");
    
    processCommonJSModules.new SuffixVarsCallback(suffix).visit(t, nameNode, null);
    processCommonJSModules.new SuffixVarsCallback(suffix).visit(t, exportsNode, null);
    
    assertEquals(expectedName, nameNode);
    assertEquals(expectedExports, exportsNode);
  }
}