import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testIsCall() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    UseSite useSite = new UseSite();
    useSite.node = new Node(Token.CALL);
    boolean result = dpm.isCall(useSite);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotCall() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    UseSite useSite = new UseSite();
    useSite.node = new Node(Token.NAME);
    boolean result = dpm.isCall(useSite);
    
    assertEquals(false, result);
  }
  
  @Test
  public void testIsPrototypeMethodDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_VOID);
    parent.addChildToFront(node);
    Node gramp = new Node(Token.ASSIGN);
    gramp.addChildToFront(new Node(Token.GETPROP));
    gramp.getLastChild().addChildToFront(new Node(Token.GETPROP));
    gramp.getLastChild().getLastChild().addChildToFront(new Node(Token.NAME, "prototype"));
    gramp.addChildToBack(node);
    parent.addChildToBack(gramp);
    
    boolean result = dpm.isPrototypeMethodDefinition(node);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotPrototypeMethodDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_VOID);
    parent.addChildToFront(node);
    Node gramp = new Node(Token.NAME);
    parent.addChildToBack(gramp);
    
    boolean result = dpm.isPrototypeMethodDefinition(node);
    
    assertEquals(false, result);
  }
  
  @Test
  public void testRewriteDefinitionIfEligible() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    DefinitionSite defSite = new DefinitionSite();
    Definition definition = new Definition();
    defSite.definition = definition;
    defSite.module = new JSModule();
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_VOID);
    parent.addChildToFront(node);
    Node gramp = new Node(Token.ASSIGN);
    gramp.addChildToFront(new Node(Token.GETPROP));
    gramp.getLastChild().addChildToFront(new Node(Token.GETPROP));
    gramp.getLastChild().getLastChild().addChildToFront(new Node(Token.NAME, "prototype"));
    gramp.addChildToBack(node);
    parent.addChildToBack(gramp);
    defSite.node = node;
    defFinder.addDefinitionSite(defSite);
    
    dpm.rewriteDefinitionIfEligible(defSite, defFinder);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testIsEligibleDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    DefinitionSite defSite = new DefinitionSite();
    Definition definition = new Definition();
    defSite.definition = definition;
    defSite.module = new JSModule();
    Node rValue = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "a");
    Node lValue = new Node(Token.GETPROP, nameNode);
    CodingConvention codingConvention = new CodingConvention();
    defSite.inGlobalScope = true;
    defSite.inExterns = false;
    
    boolean result = dpm.isEligibleDefinition(defFinder, defSite);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotEligibleDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    DefinitionSite defSite = new DefinitionSite();
    Definition definition = new Definition();
    defSite.definition = definition;
    defSite.module = new JSModule();
    codingConvention = new CodingConvention();
    defSite.inGlobalScope = false;
    defSite.inExterns = false;
    
    boolean result = dpm.isEligibleDefinition(defFinder, defSite);
    
    assertEquals(false, result);
  }
  
  @Test
  public void testRewriteCallSites() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    Definition definition = new Definition();
    String newMethodName = "JSCompiler_StaticMethods_1";
    
    dpm.rewriteCallSites(defFinder, definition, newMethodName);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testRewriteDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    String newMethodName = "JSCompiler_StaticMethods_1";
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_VOID);
    parent.addChildToFront(node);
    Node expr = new Node(Token.ASSIGN);
    expr.addChildToFront(new Node(Token.GETPROP));
    expr.getLastChild().addChildToFront(new Node(Token.NAME, "prototype"));
    node.getFirstChild().setNext(new Node(Token.NAME, "param1"));
    node.getFirstChild().getNext().addChildToBack(new Node(Token.NAME, "param2"));
    parent.addChildToBack(expr);
    
    dpm.rewriteDefinition(node, newMethodName);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testFixFunctionType() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSType();
    
    dpm.fixFunctionType(functionNode);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testReplaceReferencesToThis() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    String name = "param1";
    Node node = new Node(Token.ASSIGN);
    Node thisNode = new Node(Token.THIS);
    node.addChildToBack(thisNode);
    
    dpm.replaceReferencesToThis(node, name);
    
    // TODO: Add assertions
  }
}