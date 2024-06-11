import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class ProcessCommonJSModulesTest {
  
  @Test
  public void testProcessCommonJSModules_guessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    String moduleName = pcm.guessCJSModuleName(".\\test\\test.js");
    assertEquals("module$test$test", moduleName);
  }
  
  @Test
  public void testProcessCommonJSModules_toModuleName() {
    String moduleName = ProcessCommonJSModules.toModuleName(".\\test\\test.js");
    assertEquals("module$test$test", moduleName);
  }
  
  @Test
  public void testProcessCommonJSModules_toModuleName_withRelativeAddressing() {
    String moduleName = ProcessCommonJSModules.toModuleName("../module.js", ".\\test\\test.js");
    assertEquals("module$test$module", moduleName);
  }
  
  @Test
  public void testProcessCommonJSModules_visitRequireCall() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    NodeTraversal t = new NodeTraversal(new CompilerInput("", false), null);
    Node requireNode = new Node(Token.NAME).addChildToBack(new Node(Token.STRING, "module.js"));
    Node callNode = new Node(Token.CALL, requireNode);
    
    pcm.visitRequireCall(t, callNode, null);
    
    Node expectedModuleRef = new Node(Token.NAME, "module$module$js").srcref(callNode);
    assertEquals(expectedModuleRef, callNode);
    
    assertEquals(3, t.getInput().getRequireCount());
    assertEquals("module$module$js", t.getInput().getRequiredModule(2));
    
    Node scriptNode = new Node(Token.SCRIPT);
    Node googRequireNode = new Node(Token.CALL, new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "require")));
    googRequireNode.addChildToBack(new Node(Token.STRING, "module$module$js"));
    Node exprResultNode = new Node(Token.EXPR_RESULT, googRequireNode).copyInformationFromForTree(callNode);
    
    assertEquals(expectedModuleRef, scriptNode.getFirstChild());
    assertEquals(exprResultNode, scriptNode.getSecondChild());
  }
  
  @Test
  public void testProcessCommonJSModules_visitScript() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    NodeTraversal t = new NodeTraversal(new CompilerInput("", false), null);
    Node scriptNode = new Node(Token.SCRIPT);
    
    pcm.visitScript(t, scriptNode);
    
    Node varNode = new Node(Token.VAR);
    varNode.addChildToBack(new Node(Token.NAME, "module$module$js"));
    assertEquals(varNode, scriptNode.getFirstChild());
    
    Node googProvideNode = new Node(Token.CALL, new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "provide")));
    Node exprResultNode = new Node(Token.EXPR_RESULT, googProvideNode).copyInformationFromForTree(scriptNode);
    assertEquals(exprResultNode, scriptNode.getSecondChild());
  }
  
  @Test
  public void testProcessCommonJSModules_emitOptionalModuleExportsOverride_false() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    Node scriptNode = new Node(Token.SCRIPT);
    String moduleName = "module$module$js";
    
    pcm.emitOptionalModuleExportsOverride(scriptNode, moduleName);
    
    assertEquals(1, scriptNode.getChildCount());
  }
  
  @Test
  public void testProcessCommonJSModules_emitOptionalModuleExportsOverride_true() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    Node scriptNode = new Node(Token.SCRIPT);
    Node moduleExportsProp = new Node(Token.GETPROP, new Node(Token.NAME, "module$module$js"), new Node(Token.STRING, "module$exports"));
    Node ifBlockNode = new Node(Token.BLOCK, new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN, new Node(Token.NAME, "module$module$js"), moduleExportsProp.cloneTree()))).copyInformationFromForTree(scriptNode);
    Node ifNode = new Node(Token.IF, moduleExportsProp, ifBlockNode).copyInformationFromForTree(scriptNode);
    String moduleName = "module$module$js";
    
    pcm.emitOptionalModuleExportsOverride(scriptNode, moduleName);
    
    assertEquals(ifNode, scriptNode.getLastChild());
  }
  
  @Test
  public void testProcessCommonJSModules_visitModuleExports() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    Node moduleExportsNode = new Node(Token.GETPROP, new Node(Token.NAME, "module"), new Node(Token.STRING, "exports"));
    
    pcm.visitModuleExports(moduleExportsNode);
    
    Node moduleNode = new Node(Token.NAME, "module$module$js");
    moduleNode.putProp(Node.ORIGINALNAME_PROP, "module");
    assertEquals(moduleNode, moduleExportsNode.getChildAtIndex(0));
    
    Node exportsNode = new Node(Token.STRING, "module$exports");
    exportsNode.putProp(Node.ORIGINALNAME_PROP, "exports");
    assertEquals(exportsNode, moduleExportsNode.getChildAtIndex(1));
  }
  
  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules.SuffixVarsCallback svc = new ProcessCommonJSModules.SuffixVarsCallback("module$js");
    NodeTraversal t = new NodeTraversal(new CompilerInput("", false), null);
    Node varNode = new Node(Token.VAR);
    Node varNameNode = new Node(Token.NAME, "var");
    varNode.addChildToBack(varNameNode);
    Node letNode = new Node(Token.LET);
    Node letNameNode = new Node(Token.NAME, "let");
    letNode.addChildToBack(letNameNode);
    Node constNode = new Node(Token.CONST);
    Node constNameNode = new Node(Token.NAME, "const");
    constNode.addChildToBack(constNameNode);
    
    svc.visit(t, varNameNode, varNode);
    svc.visit(t, letNameNode, letNode);
    svc.visit(t, constNameNode, constNode);
    
    Node expectedVarNameNode = new Node(Token.NAME, "var$$module$js");
    expectedVarNameNode.putProp(Node.ORIGINALNAME_PROP, "var");
    assertEquals(expectedVarNameNode, varNameNode);
    
    Node expectedLetNameNode = new Node(Token.NAME, "let$$module$js");
    expectedLetNameNode.putProp(Node.ORIGINALNAME_PROP, "let");
    assertEquals(expectedLetNameNode, letNameNode);
    
    assertEquals(constNameNode, constNameNode);
  }
}