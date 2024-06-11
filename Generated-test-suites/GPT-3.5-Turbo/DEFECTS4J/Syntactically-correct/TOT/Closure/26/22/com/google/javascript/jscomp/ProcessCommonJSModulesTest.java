import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  private ProcessCommonJSModules processor;
  
  @Before
  public void setUp() {
    processor = new ProcessCommonJSModules(new AbstractCompiler(), ".");
  }
  
  @Test
  public void process_shouldReplaceRequireCallWithModuleRef() {
    // Arrange
    Node requireCall = createRequireCallNode("require", "\"moduleA\"");
    Node parent = new Node();
    parent.addChildToBack(requireCall);
    
    // Act
    processor.visitRequireCall(new NodeTraversal(), requireCall, parent);
    
    // Assert
    assertEquals("module$moduleA", requireCall.getLastChild().getString());
    assertEquals(IR.name("module$moduleA"), parent.getFirstChild());
  }
  
  @Test
  public void process_shouldAddVarWithModuleName() {
    // Arrange
    Node scriptNode = createScriptNode("test.js", "test.js");
    
    // Act
    processor.visitScript(new NodeTraversal(), scriptNode);
    
    // Assert
    assertEquals(IR.var(IR.name("module$test"), IR.objectlit()), scriptNode.getFirstChild());
  }
  
  @Test
  public void process_shouldAddProvideToScriptNode() {
    // Arrange
    Node scriptNode = createScriptNode("test.js", "test.js");
    
    // Act
    processor.visitScript(new NodeTraversal(), scriptNode);
    
    // Assert
    Node exprResult = scriptNode.getLastChild().getFirstChild();
    assertEquals(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
                         IR.string("module$test")), exprResult.getFirstChild());
  }
  
  @Test
  public void process_shouldEmitModuleExportsOverride() {
    // Arrange
    Node scriptNode = createScriptNode("test.js", "test.js");
    
    // Act
    processor.emitOptionalModuleExportsOverride(scriptNode, "module$test");
    
    // Assert
    Node ifNode = scriptNode.getLastChild();
    assertEquals(IR.ifNode(IR.getprop(IR.name("module$test"), IR.string("module$exports")),
                           IR.block(IR.exprResult(IR.assign(IR.name("module$test"),
                                                              IR.getprop(IR.name("module$test"),
                                                                         IR.string("module$exports")))))),
                 ifNode);
  }
  
  @Test
  public void process_shouldRewriteModuleExports() {
    // Arrange
    Node moduleExports = createModuleExportsNode("module.exports");
    
    // Act
    processor.visitModuleExports(moduleExports);
    
    // Assert
    assertEquals("module$moduleA", moduleExports.getFirstChild().getString());
    assertEquals("module$exports", moduleExports.getLastChild().getString());
  }
  
  @Test
  public void suffixVarsCallback_shouldSuffixGlobalVariableName() {
    // Arrange
    Scope.Var globalVar = new Scope.Var("globalVar", null, false);
    globalVar.setScopeRoot(true);
    Node nameNode = IR.name("globalVar").copyInformationFromForTree(globalVar);    
    NodeTraversal t = new NodeTraversal();
    t.getScope().declare("globalVar", globalVar);
    
    // Act
    processor.new SuffixVarsCallback("test").visit(t, nameNode, null);
    
    // Assert
    assertEquals("globalVar$$test", nameNode.getString());
  }
  
  private Node createRequireCallNode(String functionName, String moduleName) {
    Node requireCall = IR.call(IR.name(functionName), IR.string(moduleName));
    requireCall.getSecondChild().putProp(Node.ORIGINALNAME_PROP, "moduleName");
    return requireCall;
  }
  
  private Node createScriptNode(String sourceName, String sourceFileName) {
    Node scriptNode = IR.script();
    scriptNode.setInputId(sourceName);
    scriptNode.setSourceFile(new SourceFile(sourceName));
    scriptNode.putProp(Node.SOURCENAME_PROP, sourceFileName);
    return scriptNode;
  }
  
  private Node createModuleExportsNode(String qualifiedName) {
    Node propNode = IR.getprop(IR.name("module"), IR.string("exports"));
    propNode.putProp(Node.ORIGINALNAME_PROP, qualifiedName);
    return propNode;
  }
}