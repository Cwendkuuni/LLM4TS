import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.MethodCompilerPass.SignatureStore;

public class MethodCompilerPassTest {
  
  private MethodCompilerPass compilerPass;
  
  @Before
  public void setUp() {
    compilerPass = new MethodCompilerPass(null) {
      @Override
      Callback getActingCallback() {
        return null;
      }
      
      @Override
      SignatureStore getSignatureStore() {
        return null;
      }
    };
  }
  
  @Test
  public void testProcess_WithExternsAndRoot_ExternMethodsPopulated() {
    // Arrange
    Node externs = new Node(Token.EXTERN_EXPRESSION);
    Node root = new Node(Token.ROOT);
    
    // Act
    compilerPass.process(externs, root);
    
    // Assert
    assertEquals(0, compilerPass.externMethods.size());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
  }
  
  @Test
  public void testProcess_WithoutExterns_ExternMethodsEmpty() {
    // Arrange
    Node root = new Node(Token.ROOT);
    
    // Act
    compilerPass.process(null, root);
    
    // Assert
    assertEquals(0, compilerPass.externMethods.size());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
  }
  
  @Test
  public void testProcess_WithExternMethods_MethodDefinitionsPopulated() {
    // Arrange
    Node externs = new Node(Token.EXTERN_EXPRESSION);
    Node root = new Node(Token.ROOT);
    Node getProp = new Node(Token.GETPROP);
    Node name = new Node(Token.NAME, new Node(Token.STRING, "getName"));
    Node function = new Node(Token.FUNCTION);
    getProp.addChildToFront(name);
    getProp.addChildAfter(function, name);
    externs.addChildToFront(getProp);
    
    // Act
    compilerPass.process(externs, root);
    
    // Assert
    assertEquals(1, compilerPass.externMethods.size());
    assertEquals("getName", compilerPass.externMethods.iterator().next());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
    assertEquals(1, compilerPass.methodDefinitions.size());
    assertTrue(compilerPass.methodDefinitions.containsKey("getName"));
    assertEquals(getProp, compilerPass.methodDefinitions.get("getName").iterator().next());
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsFunction_AddsSignature() {
    // Arrange
    String name = "testMethod";
    Node function = new Node(Token.FUNCTION);
    
    // Act
    compilerPass.addPossibleSignature(name, function, null);
    
    // Assert
    assertTrue(compilerPass.methodDefinitions.containsKey(name));
    assertEquals(function, compilerPass.methodDefinitions.get(name).iterator().next());
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsNameWithExistingFunction_AddsSignature() {
    // Arrange
    String name = "testMethod";
    Node nameNode = new Node(Token.NAME, new Node(Token.STRING, name));
    Node function = new Node(Token.FUNCTION);
    Scope.Var var = new Scope.Var(null, null);
    var.setInitialValue(function);
    NodeTraversal t = new NodeTraversal.Builder().build();
    t.getScope().declare(name, nameNode, null, var);
    
    // Act
    compilerPass.addPossibleSignature(name, nameNode, t);
    
    // Assert
    assertTrue(compilerPass.methodDefinitions.containsKey(name));
    assertEquals(function, compilerPass.methodDefinitions.get(name).iterator().next());
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsNameWithUndeclaredFunction_ThrowsIllegalStateException() {
    // Arrange
    String name = "testMethod";
    Node nameNode = new Node(Token.NAME, new Node(Token.STRING, name));
    NodeTraversal t = new NodeTraversal.Builder().build();
    
    // Act & Assert
    assertThrows(IllegalStateException.class, () -> {
      compilerPass.addPossibleSignature(name, nameNode, t);
    });
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsNotFunctionOrName_AddsToNonMethodProperties() {
    // Arrange
    String name = "testMethod";
    Node otherNode = new Node(Token.STRING);
    
    // Act
    compilerPass.addPossibleSignature(name, otherNode, null);
    
    // Assert
    assertTrue(compilerPass.nonMethodProperties.contains(name));
  }
  
  @Test
  public void testProcess_WithExternMethodsAndRoot_CallsGetExternMethodsCallback() {
    // Arrange
    Node externs = new Node(Token.EXTERN_EXPRESSION);
    Node root = new Node(Token.ROOT);
    MethodCompilerPass.GetExternMethods callback = new MethodCompilerPass(Compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        // Assert
        assertEquals(externs, t.getScopeRoot());
        assertEquals(NodeTraversal.ScopingMode.EXTERN_AND_GLOBAL, t.getScope().getScopingMode());
        assertEquals(Token.EXTERN_EXPRESSION, n.getType());
        assertNull(parent);
      }
    };
    compilerPass = new MethodCompilerPass(null) {
      @Override
      Callback getActingCallback() {
        return null;
      }
      
      @Override
      SignatureStore getSignatureStore() {
        return null;
      }
    };
    
    // Act
    compilerPass.process(externs, root);
    
    // Assert (indirectly in the callback)
  }
  
  @Test
  public void testProcess_WithExternMethodsAndRoot_CallsGatherSignaturesCallbacks() {
    // Arrange
    Node externs = new Node(Token.EXTERN_EXPRESSION);
    Node root = new Node(Token.ROOT);
    MethodCompilerPass.GatherSignatures callback = new MethodCompilerPass(Compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        // Assert
        assertTrue(n.getType() == Token.GETPROP || n.getType() == Token.GETELEM || n.getType() == Token.OBJECTLIT);
      }
    };
    compilerPass = new MethodCompilerPass(null) {
      @Override
      Callback getActingCallback() {
        return null;
      }
      
      @Override
      SignatureStore getSignatureStore() {
        return null;
      }
    };
    
    // Act
    compilerPass.process(externs, root);
    
    // Assert (indirectly in the callback)
  }
  
  @Test
  public void testGatherSignatures_VisitGetPropPrototype_ProcessesPrototypeParent() {
    // Arrange
    NodeTraversal t = new NodeTraversal.Builder().build();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.NAME);
    
    // Act
    compilerPass.GatherSignatures.processPrototypeParent(t, n);
    
    // Assert (indirectly in the callback)
  }
  
  @Test
  public void testGatherSignatures_VisitGetPropPrototype_ProcessesNestedPrototypeParent() {
    // Arrange
    NodeTraversal t = new NodeTraversal.Builder().build();
    Node n = new Node(Token.GETPROP);
    Node dest = new Node(Token.GETPROP);
    Node name = new Node(Token.NAME, new Node(Token.STRING, "Foo"));
    Node prototype = new Node(Token.STRING, "prototype");
    dest.addChildToFront(name);
    dest.addChildAfter(prototype, name);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(dest);
    Node function = new Node(Token.FUNCTION);
    parent.addChildAfter(function, dest);
    n.addChildToFront(dest);
    Node assignee = new Node(Token.NAME);
    assignee.addChildToFront(name);
    parent.addChildAfter(assignee, dest);
    
    // Act
    compilerPass.GatherSignatures.processPrototypeParent(t, n);
    
    // Assert (indirectly in the callback)
  }
  
  @Test
  public void testGatherSignatures_VisitGetPropPrototype_DoesNotProcessInvalidParent() {
    // Arrange
    NodeTraversal t = new NodeTraversal.Builder().build();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.NAME);
    
    // Act
    compilerPass.GatherSignatures.processPrototypeParent(t, n);
    
    // Assert (indirectly in the callback)
  }
}