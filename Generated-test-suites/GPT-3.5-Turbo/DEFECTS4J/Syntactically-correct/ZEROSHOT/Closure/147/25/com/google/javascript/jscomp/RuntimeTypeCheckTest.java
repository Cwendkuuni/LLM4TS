import static org.junit.Assert.*;
import org.junit.Test;

public class RuntimeTypeCheckTest {
  
  @Test
  public void testAddMarkers() {
    AbstractCompiler compiler = new AbstractCompiler();
    RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
    
    // Create mock nodes and types
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    FunctionType funType = new FunctionType();
    funType.setConstructor(true);
    funType.setSource(new Node());
    funType.setAllImplementedInterfaces(new TreeSet<ObjectType>());
    ObjectType interfaceType = new ObjectType();
    funType.getAllImplementedInterfaces().add(interfaceType);
    
    // Test visitFunction method
    addMarkers.visitFunction(t, n);
    addMarkers.visit(t, n, parent);
    
    // Assert code changes
    assertTrue(compiler.getCodeChange());
  }
  
  @Test
  public void testAddChecks() {
    AbstractCompiler compiler = new AbstractCompiler();
    RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
    
    // Create mock nodes and types
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    FunctionType funType = new FunctionType();
    funType.getParameters().add(new Node());
    funType.getParameters().add(new Node());
    funType.getReturnType().setString();
    Node block = new Node(Token.BLOCK);
    n.addChildToBack(block);
    
    // Test visitFunction method
    addChecks.visitFunction(t, n);
    addChecks.visit(t, n, parent);
    
    // Assert code changes
    assertTrue(compiler.getCodeChange());
    
    // Test visitReturn method
    n = new Node(Token.RETURN, new Node(Token.STRING, "test"));
    funType.setReturnType(new JSType());
    addChecks.visitReturn(t, n);
    
    // Assert code changes
    assertTrue(compiler.getCodeChange());
  }
  
  @Test
  public void testCreateCheckTypeCallNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
    
    // Create mock nodes and types
    JSType type1 = new JSType();
    type1.setString();
    JSType type2 = new JSType();
    type2.setBoolean();
    Node expr = new Node(Token.NAME, "test");
    Node expected = new Node(Token.CALL, addChecks.jsCode("checkType"), expr, new Node(Token.ARRAYLIT));
    
    // Test createCheckTypeCallNode method
    Node result1 = addChecks.createCheckTypeCallNode(type1, expr);
    Node result2 = addChecks.createCheckTypeCallNode(type2, expr);
    
    // Assert correct function call nodes
    assertEquals(expected, result1);
    assertNull(result2);
  }
  
  @Test
  public void testCreateCheckerNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
    
    // Create mock types
    ObjectType objType = new ObjectType();
    objType.getConstructor().setInterface(true);
    objType.getConstructor().setReferenceName("MyInterface");
    JSType type1 = new JSType();
    type1.setNullType();
    JSType type2 = new JSType();
    type2.setString();
    JSType type3 = new JSType();
    type3.setBoolean();
    JSType type4 = new JSType();
    type4.setInstanceType(objType);
    JSType type5 = new JSType();
    
    // Test createCheckerNode method
    Node result1 = addChecks.createCheckerNode(type1);
    Node result2 = addChecks.createCheckerNode(type2);
    Node result3 = addChecks.createCheckerNode(type3);
    Node result4 = addChecks.createCheckerNode(type4);
    Node result5 = addChecks.createCheckerNode(type5);
    
    // Assert correct type checker nodes
    assertEquals(new Node(Token.CALL, addChecks.jsCode("nullChecker")), result1);
    assertEquals(new Node(Token.CALL, addChecks.jsCode("valueChecker"), new Node(Token.STRING, "string")), result2);
    assertEquals(new Node(Token.CALL, addChecks.jsCode("valueChecker"), new Node(Token.STRING, "boolean")), result3);
    assertEquals(new Node(Token.CALL, addChecks.jsCode("interfaceChecker"), new Node(Token.STRING, "MyInterface")), result4);
    assertNull(result5);
  }
}