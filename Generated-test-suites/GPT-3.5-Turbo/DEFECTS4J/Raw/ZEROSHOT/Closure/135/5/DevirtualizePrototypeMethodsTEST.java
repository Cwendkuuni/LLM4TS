import org.junit.*;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testIsCall() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    boolean result = devirtualizePrototypeMethods.isCall(null);
    assertEquals(false, result);
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    boolean result = devirtualizePrototypeMethods.isPrototypeMethodDefinition(null);
    assertEquals(false, result);
  }

  @Test
  public void testGetRewrittenMethodName() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    String result = devirtualizePrototypeMethods.getRewrittenMethodName(null);
    assertEquals("JSCompiler_StaticMethods_", result);
  }

  @Test
  public void testRewriteDefinitionIfEligible() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    devirtualizePrototypeMethods.rewriteDefinitionIfEligible(null, null);
  }

  @Test
  public void testIsEligibleDefinition() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    boolean result = devirtualizePrototypeMethods.isEligibleDefinition(null, null);
    assertEquals(false, result);
  }

  @Test
  public void testRewriteCallSites() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    devirtualizePrototypeMethods.rewriteCallSites(null, null, null);
  }

  @Test
  public void testRewriteDefinition() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    devirtualizePrototypeMethods.rewriteDefinition(null, null);
  }

  @Test
  public void testFixFunctionType() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    devirtualizePrototypeMethods.fixFunctionType(null);
  }

  @Test
  public void testReplaceReferencesToThis() {
    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(null);
    devirtualizePrototypeMethods.replaceReferencesToThis(null, null);
  }

}