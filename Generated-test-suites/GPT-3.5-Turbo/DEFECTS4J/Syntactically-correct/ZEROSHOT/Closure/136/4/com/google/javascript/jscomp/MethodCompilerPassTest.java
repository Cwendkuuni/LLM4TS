import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testProcess() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // create the expected sets
    Set<String> externMethodsExpected = new HashSet<String>();
    Set<String> externMethodsWithoutSignaturesExpected = new HashSet<String>();
    Set<String> nonMethodPropertiesExpected = new HashSet<String>();

    // call the process method
    methodCompilerPass.process(null, null);

    // assert that the sets are equal
    assertEquals(externMethodsExpected, methodCompilerPass.externMethods);
    assertEquals(externMethodsWithoutSignaturesExpected, methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(nonMethodPropertiesExpected, methodCompilerPass.nonMethodProperties);
  }

  @Test
  public void testGetActingCallback() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // call the getActingCallback method
    Callback callback = methodCompilerPass.getActingCallback();

    // assert that the callback is not null
    assertNotNull(callback);
  }

  @Test
  public void testGetSignatureStore() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // call the getSignatureStore method
    SignatureStore store = methodCompilerPass.getSignatureStore();

    // assert that the store is not null
    assertNotNull(store);
  }

  @Test
  public void testAddPossibleSignature() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // create a NodeTraversal object
    NodeTraversal traversal = new NodeTraversal();

    // create some test nodes and names
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.NAME);
    node2.setString("testFunction");

    String name1 = "function1";
    String name2 = "function2";

    // call the addPossibleSignature method
    methodCompilerPass.addPossibleSignature(name1, node1, traversal);
    methodCompilerPass.addPossibleSignature(name2, node2, traversal);

    // assert that the methodDefinitions set contains the correct nodes
    assertTrue(methodCompilerPass.methodDefinitions.get(name1).contains(node1));
    assertTrue(methodCompilerPass.methodDefinitions.get(name2).contains(node2));

    // assert that the nonMethodProperties set is empty
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
  }

  @Test
  public void testAddSignature() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // create some test nodes and names
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.NAME);
    node2.setString("testFunction");

    String name1 = "function1";
    String name2 = "function2";

    // call the addSignature method
    methodCompilerPass.addSignature(name1, node1, "source1");
    methodCompilerPass.addSignature(name2, node2, "source2");

    // assert that the externMethodsWithoutSignatures set is empty
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());

    // assert that the methodDefinitions set contains the correct nodes
    assertTrue(methodCompilerPass.methodDefinitions.get(name1).contains(node1));
    assertTrue(methodCompilerPass.methodDefinitions.get(name2).contains(node2));

    // assert that the SignatureStore contains the correct signatures
    assertTrue(methodCompilerPass.getSignatureStore().contains(name1, "source1"));
    assertTrue(methodCompilerPass.getSignatureStore().contains(name2, "source2"));
  }

  @Test
  public void testGetExternMethods() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // create a NodeTraversal object
    NodeTraversal traversal = new NodeTraversal();

    // create some test nodes and names
    Node node1 = new Node(Token.GETPROP);
    Node node2 = new Node(Token.GETELEM);
    Node node3 = new Node(Token.STRING);
    node3.setString("testMethod");
    Node node4 = new Node(Token.NAME);
    node4.setString("testFunction");
    Node node5 = new Node(Token.FUNCTION);

    // create the expected sets
    Set<String> externMethodsExpected = new HashSet<String>();
    Set<String> externMethodsWithoutSignaturesExpected = new HashSet<String>();
    Set<String> nonMethodPropertiesExpected = new HashSet<String>();

    // add the expected values to the sets
    externMethodsExpected.add("testMethod");

    // call the visit method of GetExternMethods
    methodCompilerPass.new GetExternMethods().visit(traversal, node1, null);
    methodCompilerPass.new GetExternMethods().visit(traversal, node2, null);

    // assert that the sets are equal to the expected sets
    assertEquals(externMethodsExpected, methodCompilerPass.externMethods);
    assertEquals(externMethodsWithoutSignaturesExpected, methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(nonMethodPropertiesExpected, methodCompilerPass.nonMethodProperties);
  }

  @Test
  public void testGatherSignatures() {
    // create a MethodCompilerPass object
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());

    // create a NodeTraversal object
    NodeTraversal traversal = new NodeTraversal();

    // create some test nodes and names
    Node node1 = new Node(Token.GETPROP);
    Node node2 = new Node(Token.GETELEM);
    Node node3 = new Node(Token.STRING);
    node3.setString("testMethod");
    Node node4 = new Node(Token.NAME);
    node4.setString("testFunction");
    Node node5 = new Node(Token.FUNCTION);

    // call the visit method of GatherSignatures
    methodCompilerPass.new GatherSignatures().visit(traversal, node1, null);
    methodCompilerPass.new GatherSignatures().visit(traversal, node2, null);

    // assert that the nonMethodProperties set contains the correct names
    assertTrue(methodCompilerPass.nonMethodProperties.contains("testMethod"));

    // assert that the methodDefinitions set contains the correct nodes
    assertTrue(methodCompilerPass.methodDefinitions.get("testMethod").contains(node1));
    assertTrue(methodCompilerPass.methodDefinitions.get("testMethod").contains(node2));

    // assert that the SignatureStore contains the correct signatures
    assertTrue(methodCompilerPass.getSignatureStore().contains("testMethod", "<unknown>"));
  }
}