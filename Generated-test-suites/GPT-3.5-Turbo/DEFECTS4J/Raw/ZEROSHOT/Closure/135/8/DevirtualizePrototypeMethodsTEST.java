import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  
  @Test
  public void testIsCall() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    UseSite site1 = new UseSite(new Node());
    assertTrue(dpm.isCall(site1));
    
    UseSite site2 = new UseSite(new Node());
    assertFalse(dpm.isCall(site2));
  }
  
  @Test
  public void testIsPrototypeMethodDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    Node node1 = new Node();
    assertTrue(dpm.isPrototypeMethodDefinition(node1));
    
    Node node2 = new Node();
    assertFalse(dpm.isPrototypeMethodDefinition(node2));
  }
  
  @Test
  public void testGetRewrittenMethodName() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    String methodName = dpm.getRewrittenMethodName("testMethod");
    
    assertEquals("JSCompiler_StaticMethods_testMethod", methodName);
  }
  
  @Test
  public void testIsEligibleDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    DefinitionSite defSite = new DefinitionSite(new Node(), new Definition(), false, false);
    
    assertFalse(dpm.isEligibleDefinition(defFinder, defSite));
  }
  
  @Test
  public void testRewriteCallSites() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
    Definition definition = new Definition();
    String newMethodName = "newMethod";
    
    dpm.rewriteCallSites(defFinder, definition, newMethodName);
  }
  
  @Test
  public void testRewriteDefinition() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    Node node = new Node();
    String newMethodName = "newMethod";
    
    dpm.rewriteDefinition(node, newMethodName);
  }
  
  @Test
  public void testFixFunctionType() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    Node functionNode = new Node();
    
    dpm.fixFunctionType(functionNode);
  }
  
  @Test
  public void testReplaceReferencesToThis() {
    DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
    
    Node node = new Node();
    String name = "thisName";
    
    dpm.replaceReferencesToThis(node, name);
  }
}