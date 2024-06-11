import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testProcess_WithNullExternsAndRoot() {
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    methodCompilerPass.process(null, null);
    // Verify that the methodCompilerPass has correctly cleared all the data structures
    assertEquals(0, methodCompilerPass.externMethods.size());
  }

  @Test
  public void testProcess_WithValidExternsAndRoot() {
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    Node externs = createExterns();
    Node root = createRoot();
    methodCompilerPass.process(externs, root);
    // Verify that the methodCompilerPass has correctly populated the data structures
    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals(2, methodCompilerPass.methodDefinitions.keySet().size());
  }

  // Helper methods to create mock Nodes for testing purposes
  private Node createExterns() {
    Node externs = new Node(Token.BLOCK);
    Node getProp1 = new Node(Token.GETPROP, createName("setTimeout"), createString("setTimeout"));
    Node getProp2 = new Node(Token.GETPROP, createName("setInterval"), createString("setInterval"));
    Node getProp3 = new Node(Token.GETPROP, createName("setImmediate"), createString("setImmediate"));
    externs.addChildToBack(getProp1);
    externs.addChildToBack(createFunction());
    externs.addChildToBack(getProp2);
    externs.addChildToBack(getProp3);
    externs.addChildToBack(createObjectLiteral());
    return externs;
  }

  private Node createRoot() {
    Node root = new Node(Token.BLOCK);
    Node assignment1 = new Node(Token.ASSIGN);
    Node getProp1 = new Node(Token.GETPROP, createName("Foo"), createString("prototype"));
    Node function1 = createFunction();
    assignment1.addChildToBack(getProp1);
    assignment1.addChildToBack(function1);
    root.addChildToBack(assignment1);
    Node assignment2 = new Node(Token.ASSIGN);
    Node name2 = createName("Foo.prototype.getBar");
    Node getProp2 = new Node(Token.GETPROP, createName("Foo"), createString("prototype"));
    Node string2 = createString("getBar");
    Node function2 = createFunction();
    assignment2.addChildToBack(name2);
    assignment2.addChildToBack(function2);
    getProp2.addChildToBack(string2);
    root.addChildToBack(assignment2);
    Node assignment3 = new Node(Token.ASSIGN);
    Node assignee3 = createName("Foo.bar");
    Node name3 = createName("Foo");
    Node string3 = createString("bar");
    Node function3 = createFunction();
    assignment3.addChildToBack(getProp3);
    assignment3.addChildToBack(function3);
    root.addChildToBack(assignment3);
    return root;
  }

  private Node createFunction() {
    Node function = new Node(Token.FUNCTION);
    Node name = createName("");
    function.addChildToBack(name);
    return function;
  }

  private Node createObjectLiteral() {
    Node objectLiteral = new Node(Token.OBJECTLIT);
    Node string1 = createString("baz");
    Node function1 = createFunction();
    objectLiteral.addChildToBack(string1);
    objectLiteral.addChildToBack(function1);
    return objectLiteral;
  }

  private Node createName(String name) {
    return new Node(Token.NAME, createString(name));
  }

  private Node createString(String value) {
    return new Node(Token.STRING, value);
  }

  // Test implementation of MethodCompilerPass that extends the abstract class
  private class TestMethodCompilerPass extends MethodCompilerPass {
    TestMethodCompilerPass() {
      super(null);
    }

    @Override
    Callback getActingCallback() {
      return null;
    }

    @Override
    SignatureStore getSignatureStore() {
      return null;
    }
  }

}