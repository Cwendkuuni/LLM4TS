import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {
  
  @Test
  public void testIsCall() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    UseSite site = new UseSite(node, module);
    
    // Test when parent node is not a call
    assertFalse(dpm.isCall(site));
    
    // Test when parent node is a call and node is the first child
    Node parent1 = new Node(Token.CALL);
    parent1.addChildToFront(node);
    assertTrue(dpm.isCall(site));
    
    // Test when parent node is a call and node is not the first child
    Node parent2 = new Node(Token.CALL);
    parent2.addChildToBack(new Node(Token.NAME, "other"));
    parent2.addChildToFront(node);
    assertFalse(dpm.isCall(site));
  }
  
  @Test
  public void testIsPrototypeMethodDefinition() {
    Node prototypeMethodNode = new Node(Token.GETPROP);
    prototypeMethodNode.addChildToFront(new Node(Token.GETPROP));
    prototypeMethodNode.getFirstChild().addChildToFront(new Node(Token.GETPROP));
    prototypeMethodNode.getFirstChild().getFirstChild().addChildToFront(new Node(Token.NAME, "A"));
    assertEquals(false, isPrototypeMethodDefinition(prototypeMethodNode));
    
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(new Node(Token.GETPROP));
    assignNode.getFirstChild().addChildToFront(new Node(Token.NAME, "A"));
    assignNode.getFirstChild().addChildToBack(new Node(Token.NAME, "prototype"));
    assertEquals(false, isPrototypeMethodDefinition(assignNode));
    
    assignNode.getFirstChild().addChildToBack(new Node(Token.NAME, "accumulate"));
    assignNode.getLastChild().addChildToFront(new Node(Token.FUNCTION));
    assertEquals(true, isPrototypeMethodDefinition(assignNode));
  }
  
  @Test
  public void testGetRewrittenMethodName() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    String originalMethodName1 = "accumulate";
    assertEquals("JSCompiler_StaticMethods_accumulate", dpm.getRewrittenMethodName(originalMethodName1));
    
    String originalMethodName2 = "add";
    assertEquals("JSCompiler_StaticMethods_add", dpm.getRewrittenMethodName(originalMethodName2));
  }
  
  @Test
  public void testIsEligibleDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    DefinitionSite defSite = new DefinitionSite(node, module, false, true);
    Definition definition = new Definition(rValue, lValue);
    JSModule definitionModule = new JSModule(name);
    defFinder.process(externs, root);
    boolean result = dpm.isEligibleDefinition(defFinder, defSite, definition, definitionModule);
    
    // write test cases for different if conditions in method
  }
  
  @Test
  public void testRewriteCallSites() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    Definition definition = new Definition(rValue, lValue);
    String newMethodName = "JSCompiler_StaticMethods_newMethodName";
    
    // write test cases
  }
  
  @Test
  public void testRewriteDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    String newMethodName = "JSCompiler_StaticMethods_newMethodName";
    
    // write test cases
  }
  
  @Test
  public void testFixFunctionType() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    Node functionNode = new Node(Token.FUNCTION);
    
    // write test cases
  }
  
  @Test
  public void testReplaceReferencesToThis() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    Node node = new Node();
    String name = "name";
    
    // write test cases
  }
}