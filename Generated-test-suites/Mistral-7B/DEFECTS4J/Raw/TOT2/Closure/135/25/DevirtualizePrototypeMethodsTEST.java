import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DevirtualizePrototypeMethodsTest {
  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = Node.objectLiteral();
  }

  @Test
  public void testSimpleMethodRewriting() {
    // Given: A class with a simple prototype method
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);

    // When: The devirtualizer processes the root node
    devirtualizer.process(null, rootNode);

    // Then: The method is rewritten as a global function
    Node methodDefinition = rootNode.getFirstChild().getLastChild();
    Node functionNode = methodDefinition.getLastChild();
    Node functionName = functionNode.getFirstChild();
    String expectedFunctionName = "JSCompiler_StaticMethods_accumulate";
    assertEquals(expectedFunctionName, functionName.getString());

    // And: The method call is updated to use the global function
    Node methodCall = Node.newCall(methodDefinition, Lists.newArrayList(
        Node.newThis(), Node.newNumber(2)));
    Node globalFunctionCall = Node.newCall(
        Node.newGetProp(rootNode, expectedFunctionName),
        Lists.newArrayList(rootNode, Node.newNumber(2)));
    assertEquals(globalFunctionCall, methodCall);
  }

  @Test
  public void testComplexMethodRewriting() {
    // Given: A class with a complex prototype method with multiple arguments
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);

    // When: The devirtualizer processes the root node
    devirtualizer.process(null, rootNode);

    // Then: The method is rewritten as a global function
    Node methodDefinition = rootNode.getFirstChild().getLastChild();
    Node functionNode = methodDefinition.getLastChild();
    Node functionName = functionNode.getFirstChild();
    String expectedFunctionName = "JSCompiler_StaticMethods_complexMethod";
    assertEquals(expectedFunctionName, functionName.getString());

    // And: The method call is updated to use the global function
    Node methodCall = Node.newCall(methodDefinition, Lists.newArrayList(
        Node.newThis(), Node.newString("arg1"), Node.newNumber(2), Node.newNumber(3)));
    Node globalFunctionCall = Node.newCall(
        Node.newGetProp(rootNode, expectedFunctionName),
        Lists.newArrayList(rootNode, "arg1", Node.newNumber(2), Node.newNumber(3)));
    assertEquals(globalFunctionCall, methodCall);
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateMethodDefinition() {
    // Given: A class with a prototype method that is defined multiple times
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);

    // When: The devirtualizer processes the root node
    devirtualizer.process(null, rootNode);

    // Then: An AssertionError is thrown since the multiple definitions are not allowed
  }

  @Test
  public void testMethodNotRewritten() {
    // Given: A class with a method that is not eligible for rewriting
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);

    // When: The devirtualizer processes the root node
    devirtualizer.process(null, rootNode);

    // Then: The method is not rewritten
    Node methodDefinition = rootNode.getFirstChild().getLastChild();
    assertTrue(methodDefinition != null);
  }

  @Test
  public void testCallSiteOutsideFunction() {
    // Given: A call site to a method outside of a function
    DevirtualizePrototypeMethods devirtualizer = new DevitutionalizePrototypeMethods(compiler);

    // When: The devirtualizer processes the root node
    devirtualizer.process(null, rootNode);

    // Then: The call site is left unchanged
    Node methodCall = Node.newCall(
        Node.newString("a.prototype.method"),
        Lists.newArrayList(Node.newThis(), Node.newNumber(2)));
    assertEquals(methodCall, Node.newCall(
        Node.newGetProp(Node.newThis(), "prototype"), "method",
        Lists.newArrayList(Node.newThis(), Node.newNumber(2))));
  }

  @Test(expected = AssertionError.class)
  public void testCallSiteBeforeDefinition() {
    // Given: A call site to a method before it is defined
    DevirtualizePrototypeMethods devitutionalizePrototypeMethods = new DevitutionalizePrototypeMethods(compiler);

    // When: The devitationalizePrototypeMethods processes the root node
    devitionalizePrototypeMethods.process(null, rootNode);

    // Then: An AssertionError is thrown since the call site is before the definition
  }
}