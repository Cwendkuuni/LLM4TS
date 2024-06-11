import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.rhino.Node;

public class DevirtualizePrototypeMethodsTest {
  @Test
  public void testAccumulate() {
    // Given a valid object and method
    Node rootNode = Node.objectLiteral();
    rootNode.setProperty("a", Node.objectLiteral()
        .setProperty("prototype", Node.objectLiteral()
            .setProperty("accumulate", Node.functionLiteral(
                "function(value) { this.total += value; return this.total; }"))));

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(null);
    Node processedNode = devirtualizer.process(null, rootNode);

    // When the method is called
    Node callSite = Node.newCall(processedNode.getFirstChild(),
        Node.newDouble(2.0));
    Node result = Node.newCall(callSite, Node.newThis());

    // Then the result should be the sum of the total property and the value passed
    double expectedResult = 2.0;
    assertEquals(expectedResult, result.toNumber().doubleValue(), 0.001);
  }

  @Test
  public void testInvalidDefinition() {
    // Given an invalid definition
    Node rootNode = Node.objectLiteral();
    rootNode.setProperty("a", Node.objectLiteral()
        .setProperty("prototype", Node.objectLiteral()
            .setProperty("accumulate", Node.functionLiteral(
                "function(value) { this.total += value; return value; }"))));

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(null);
    Node processedNode = devirtualizer.process(null, rootNode);

    // Then the method is not rewritten
    Node callSite = Node.newCall(processedNode.getFirstChild(),
        Node.newThis(), Node.newDouble(2.0));
    assertFalse(NodeUtil.isFunction(callSite.getFirstChild()));
  }

  @Test
  public void testInvalidCallSite() {
    // Given an invalid call site
    Node rootNode = Node.objectLiteral();
    rootNode.setProperty("a", Node.objectLiteral()
        .setProperty("prototype", Node.objectLiteral()
            .setProperty("accumulate", Node.functionLiteral(
                "function(value) { this.total += value; return this.total; }"))))
        .setProperty("invalid", Node.numberLiteral(42));

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(null);
    Node processedNode = devirtualizer.process(null, rootNode);

    // When calling the invalid property
    Node callSite = Node.newGetProp(processedNode.getFirstChild(),
        Node.newIdentifier("invalid"));

    // Then a TypeError is thrown
    try {
      Node result = Node.newCall(callSite, Node.newThis());
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().startsWith("TypeError: Cannot read property"));
    }
  }
}