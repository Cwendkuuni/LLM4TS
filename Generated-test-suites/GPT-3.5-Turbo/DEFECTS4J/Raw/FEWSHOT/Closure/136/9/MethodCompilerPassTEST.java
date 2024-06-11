import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testProcess() {
    MethodCompilerPass pass = new MethodCompilerPassImpl();
    
    // Test case 1: No externs and root nodes
    pass.process(null, null);
    assertEquals(0, pass.externMethods.size());
    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(0, pass.nonMethodProperties.size());
    assertEquals(0, pass.methodDefinitions.size());

    // Test case 2: Only externs
    pass.process(createExterns(), null);
    assertEquals(2, pass.externMethods.size());
    assertEquals(1, pass.externMethodsWithoutSignatures.size());
    assertFalse(pass.externMethods.contains("externMethod"));
    assertFalse(pass.methodDefinitions.containsKey("externMethod"));
    assertTrue(pass.externMethods.contains("methodWithSignature"));
    assertTrue(pass.methodDefinitions.containsKey("methodWithSignature"));

    // Test case 3: Only root nodes
    pass.process(null, createRoot());
    assertEquals(0, pass.externMethods.size());
    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(2, pass.nonMethodProperties.size());
    assertTrue(pass.nonMethodProperties.contains("nonMethodProperty1"));
    assertTrue(pass.nonMethodProperties.contains("nonMethodProperty2"));
    assertEquals(2, pass.methodDefinitions.size());
    assertNotNull(pass.methodDefinitions.get("method1"));
    assertNotNull(pass.methodDefinitions.get("method2"));

    // Test case 4: Externs and root nodes
    pass.process(createExterns(), createRoot());
    assertEquals(2, pass.externMethods.size());
    assertEquals(1, pass.externMethodsWithoutSignatures.size());
    assertFalse(pass.externMethods.contains("externMethod"));
    assertFalse(pass.methodDefinitions.containsKey("externMethod"));
    assertTrue(pass.externMethods.contains("methodWithSignature"));
    assertTrue(pass.methodDefinitions.containsKey("methodWithSignature"));
    assertEquals(2, pass.nonMethodProperties.size());
    assertTrue(pass.nonMethodProperties.contains("nonMethodProperty1"));
    assertTrue(pass.nonMethodProperties.contains("nonMethodProperty2"));
    assertEquals(4, pass.methodDefinitions.size());
    assertNotNull(pass.methodDefinitions.get("method1"));
    assertNotNull(pass.methodDefinitions.get("method2"));
    assertNotNull(pass.methodDefinitions.get("methodWithSignature"));
    assertNotNull(pass.methodDefinitions.get("methodWithSignature2"));
  }

  private Node createExterns() {
    Node externs = new Node(Token.ROOT);

    // Add extern methods with signatures
    Node externMethodWithSignature = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING, "methodWithSignature")));
    externs.addChildToBack(externMethodWithSignature);

    // Add extern methods without signatures
    Node externMethodWithoutSignature = new Node(Token.STRING, "externMethod");
    externs.addChildToBack(externMethodWithoutSignature);

    return externs;
  }

  private Node createRoot() {
    Node root = new Node(Token.ROOT);

    // Add method declarations
    Node method1 = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING, "method1")));
    root.addChildToBack(method1);

    Node method2 = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING, "method2")));
    root.addChildToBack(method2);

    // Add non-method properties
    Node nonMethodProperty1 = new Node(Token.GETPROP, new Node(Token.STRING, "nonMethodProperty1"));
    root.addChildToBack(nonMethodProperty1);

    Node nonMethodProperty2 = new Node(Token.GETELEM, new Node(Token.STRING, "nonMethodProperty2"));
    root.addChildToBack(nonMethodProperty2);

    // Add method declarations with signatures
    Node methodWithSignature = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING, "methodWithSignature")));
    root.addChildToBack(methodWithSignature);

    Node methodWithSignature2 = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING, "methodWithSignature2")));
    root.addChildToBack(methodWithSignature2);

    return root;
  }

  private class MethodCompilerPassImpl extends MethodCompilerPass {
    public MethodCompilerPassImpl() {
      super(null);
    }

    @Override
    public Callback getActingCallback() {
      return null;
    }

    @Override
    public SignatureStore getSignatureStore() {
      return null;
    }
  }
}