import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RuntimeTypeCheckTest {
  
  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers();
    assertNotNull(addMarkers);
  }
  
  @Test
  public void testAddChecks() {
    RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks();
    assertNotNull(addChecks);
  }
  
  @Test
  public void testCreateCheckTypeCallNode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
    JSType type = new JSType();
    Node expr = new Node();
    Node checkTypeCallNode = runtimeTypeCheck.createCheckTypeCallNode(type, expr);
    assertNotNull(checkTypeCallNode);
  }
  
  @Test
  public void testCreateCheckerNode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
    JSType type = new JSType();
    Node checkerNode = runtimeTypeCheck.createCheckerNode(type);
    assertNull(checkerNode);
  }
  
  @Test
  public void testGetBoilerplateCode() {
    AbstractCompiler compiler = new AbstractCompiler();
    String logFunction = "";
    Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);
    assertNotNull(boilerplateCode);
  }
  
}