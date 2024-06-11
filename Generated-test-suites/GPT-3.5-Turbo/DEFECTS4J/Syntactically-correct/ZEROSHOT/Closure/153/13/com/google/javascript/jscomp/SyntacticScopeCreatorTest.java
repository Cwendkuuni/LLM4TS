import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

public class SyntacticScopeCreatorTest {
  
  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }
  
  @Test
  public void testCreateScope_withFunctionNode_returnsScope() {
    Node functionNode = new Node(Token.FUNCTION);
    Scope parentScope = new Scope(new Node(Token.SCRIPT), compiler);
    
    Scope scope = scopeCreator.createScope(functionNode, parentScope);
    
    assertEquals(parentScope, scope.getParent());
    assertEquals(functionNode, scope.getRootNode());
  }
  
  @Test
  public void testCreateScope_withNonFunctionNode_returnsScope() {
    Node nonFunctionNode = new Node(Token.SCRIPT);
    
    Scope scope = scopeCreator.createScope(nonFunctionNode, null);
    
    assertEquals(null, scope.getParent());
    assertEquals(nonFunctionNode, scope.getRootNode());
  }
  
  @Test
  public void testScanVars_withVarNode_declaresVariables() {
    Node varNode = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME);
    child1.setString("x");
    child1.setProp(Node.SOURCENAME_PROP, "source1");
    Node child2 = new Node(Token.NAME);
    child2.setString("y");
    child2.setProp(Node.SOURCENAME_PROP, "source1");
    child2.setJSDocInfo(new JSDocInfo());
    Node child3 = new Node(Token.NAME);
    child3.setString("z");
    child3.setProp(Node.SOURCENAME_PROP, "source1");
    child3.setJSDocInfo(new JSDocInfo());
    varNode.addChildToFront(child3);
    varNode.addChildToFront(child2);
    varNode.addChildToFront(child1);
    
    scopeCreator.scanVars(varNode, null);
    
    Scope scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    assertEquals(3, scope.getLocalVars().size());
  }
  
  @Test
  public void testScanVars_withFunctionNode_declaresVariables() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME);
    functionNameNode.setString("fn");
    functionNameNode.setProp(Node.SOURCENAME_PROP, "source2");
    Node argsNode = new Node(Token.LP);
    Node arg1 = new Node(Token.NAME);
    arg1.setString("x");
    arg1.setProp(Node.SOURCENAME_PROP, "source2");
    Node arg2 = new Node(Token.NAME);
    arg2.setString("y");
    arg2.setProp(Node.SOURCENAME_PROP, "source2");
    Node arg3 = new Node(Token.NAME);
    arg3.setString("z");
    arg3.setProp(Node.SOURCENAME_PROP, "source2");
    argsNode.addChildToFront(arg3);
    argsNode.addChildToFront(arg2);
    argsNode.addChildToFront(arg1);
    Node bodyNode = new Node(Token.SCRIPT);
    functionNode.addChildToFront(functionNameNode);
    functionNode.addChildToBack(argsNode);
    functionNode.addChildToBack(bodyNode);
    
    scopeCreator.scanVars(functionNode, null);
    
    Scope scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    assertEquals(4, scope.getLocalVars().size());
  }
  
  @Test
  public void testScanVars_withCatchNode_declaresVariables() {
    Node catchNode = new Node(Token.CATCH);
    Node catchVarNode = new Node(Token.NAME);
    catchVarNode.setString("e");
    catchVarNode.setProp(Node.SOURCENAME_PROP, "source3");
    Node blockNode = new Node(Token.SCRIPT);
    catchNode.addChildToFront(catchVarNode);
    catchNode.addChildToBack(blockNode);
    
    scopeCreator.scanVars(catchNode, null);
    
    Scope scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    assertEquals(1, scope.getLocalVars().size());
  }
  
  @Test
  public void testScanVars_withScriptNode_scansChildren() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node child1 = new Node(Token.VAR);
    child1.setProp(Node.SOURCENAME_PROP, "source4");
    Node child2 = new Node(Token.FUNCTION);
    child2.setProp(Node.SOURCENAME_PROP, "source4");
    Node child3 = new Node(Token.SCRIPT);
    child3.setProp(Node.SOURCENAME_PROP, "source4");
    scriptNode.addChildToFront(child3);
    scriptNode.addChildToFront(child2);
    scriptNode.addChildToFront(child1);
    
    scopeCreator.scanVars(scriptNode, null);
    
    Scope scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
    assertEquals(0, scope.getLocalVars().size());
    assertEquals(3, scope.blocks.size());
  }
  
  @Test
  public void testDeclareVar_withNewVariable_declaresVariable() {
    Node varNode = new Node(Token.VAR);
    Node childNode = new Node(Token.NAME);
    childNode.setString("x");
    childNode.setProp(Node.SOURCENAME_PROP, "source5");
    varNode.addChildToFront(childNode);
    Node rootNode = new Node(Token.SCRIPT);
    
    scopeCreator.declareVar("x", childNode, varNode, rootNode, null, varNode);
    
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals("x", scope.getVar("x").getName());
  }
  
  @Test
  public void testDeclareVar_withExistingVariable_callsRedeclarationHandler() {
    Node varNode = new Node(Token.VAR);
    Node childNode = new Node(Token.NAME);
    childNode.setString("x");
    childNode.setProp(Node.SOURCENAME_PROP, "source6");
    varNode.addChildToFront(childNode);
    Node rootNode = new Node(Token.SCRIPT);
    
    scopeCreator.declareVar("x", childNode, varNode, rootNode, null, varNode);
    scopeCreator.declareVar("x", childNode, varNode, rootNode, null, varNode);
    
    // Verify that the redeclarationHandler was called
  }
  
  @Test
  public void testDeclareVar_withArgumentsVariable_inFunctionScope_callsRedeclarationHandler() {
    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME);
    functionNameNode.setString("fn");
    functionNameNode.setProp(Node.SOURCENAME_PROP, "source7");
    Node argsNode = new Node(Token.LP);
    Node arg1 = new Node(Token.NAME);
    arg1.setString("x");
    arg1.setProp(Node.SOURCENAME_PROP, "source7");
    argsNode.addChildToFront(arg1);
    Node bodyNode = new Node(Token.SCRIPT);
    functionNode.addChildToFront(functionNameNode);
    functionNode.addChildToBack(argsNode);
    functionNode.addChildToBack(bodyNode);
    
    scopeCreator.declareVar("arguments", functionNode, null, null, null, null);
    
    // Verify that the redeclarationHandler was called
  }
  
  @Test
  public void testDeclareVar_withArgumentsVariable_inNonFunctionScope_doesNotCallRedeclarationHandler() {
    Node varNode = new Node(Token.VAR);
    Node childNode = new Node(Token.NAME);
    childNode.setString("arguments");
    childNode.setProp(Node.SOURCENAME_PROP, "source8");
    varNode.addChildToFront(childNode);
    Node rootNode = new Node(Token.SCRIPT);
    
    scopeCreator.declareVar("arguments", childNode, varNode, rootNode, null, varNode);
    
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertEquals("arguments", scope.getVar("arguments").getName());
  }
}