import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  
  private MethodCompilerPass methodCompilerPass;
  
  @Before
  public void setUp() {
    methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());
  }
  
  @Test
  public void testProcessWithNullExternsRoot() {
    Node externs = null;
    Node root = null;
    
    methodCompilerPass.process(externs, root);
    
    // Assert methodCompilerPass properties
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testProcessWithNonNullExternsRoot() {
    Node externs = new Node(Token.OBJECTLIT);
    externs.addChildToBack(new Node(Token.STRING, "obj1"));
    externs.addChildToBack(new Node(Token.FUNCTION));
    externs.addChildToBack(new Node(Token.STRING, "obj2"));
    externs.addChildToBack(new Node(Token.FUNCTION));
    Node root = new Node(Token.OBJECTLIT);
    root.addChildToBack(new Node(Token.STRING, "obj3"));
    root.addChildToBack(new Node(Token.FUNCTION));
    root.addChildToBack(new Node(Token.STRING, "obj4"));
    root.addChildToBack(new Node(Token.FUNCTION));
    
    methodCompilerPass.process(externs, root);
    
    // Assert methodCompilerPass properties
    assertEquals(4, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(4, methodCompilerPass.methodDefinitions.size());

    // Assert methodCompilerPass externMethods
    assertTrue(methodCompilerPass.externMethods.contains("obj1"));
    assertTrue(methodCompilerPass.externMethods.contains("obj2"));
    assertTrue(methodCompilerPass.externMethods.contains("obj3"));
    assertTrue(methodCompilerPass.externMethods.contains("obj4"));
    
    // Assert methodCompilerPass methodDefinitions
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("obj1"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("obj2"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("obj3"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("obj4"));
    assertEquals(1, methodCompilerPass.methodDefinitions.get("obj1").size());
    assertEquals(1, methodCompilerPass.methodDefinitions.get("obj2").size());
    assertEquals(1, methodCompilerPass.methodDefinitions.get("obj3").size());
    assertEquals(1, methodCompilerPass.methodDefinitions.get("obj4").size());
  }
  
  @Test
  public void testGetActingCallback() {
    Callback callback = methodCompilerPass.getActingCallback();
    
    // Assert callback is not null
    assertNotNull(callback);
  }
  
  @Test
  public void testGetSignatureStore() {
    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    
    // Assert signatureStore is not null
    assertNotNull(signatureStore);
  }
  
}