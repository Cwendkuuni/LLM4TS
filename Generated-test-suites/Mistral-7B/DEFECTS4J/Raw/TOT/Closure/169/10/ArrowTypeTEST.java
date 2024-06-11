import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithKnownTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode("function foo(int x, int y) { }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(),
        registry.getType("int"));

    ArrowType expectedArrowType = new ArrowType(registry, functionNode.getParameters(),
        registry.getType("int"));
    assertTrue(arrowType.isSubtype(expectedArrowType));
    assertEquals(arrowType, expectedArrowType);
  }

  @Test
  public void testArrowTypeWithUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode("function foo(int x) { }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), null);

    ArrowType expectedArrowType = new ArrowType(registry, functionNode.getParameters(), null);
    assertTrue(arrowType.isSubtype(expectedArrowType));
    assertEquals(arrowType, expectedArrowType);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode("function foo(int... x) { }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.getType("int"));

    ArrowType expectedArrowType = new ArrowType(registry, functionNode.getParameters(), registry.getType("int"));
    assertTrue(arrowType.isSubtype(expectedArrowType));
    assertEquals(arrowType, expectedArrowType);
  }

  @Test
  public void testArrowTypeWithOptionalParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode("function foo(int x, int y = 0) { }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.getType("int"));

    ArrowType expectedArrowType = new ArrowType(registry, functionNode.getParameters(), registry.getType("int"), false);
    assertTrue(arrowType.isSubtype(expectedArrowType));
    assertEquals(arrowType, expectedArrowType);
  }

  @Test
  public void testArrowTypeWithEmptyParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, registry.getType("int"));

    ArrowType expectedArrowType = new ArrowType(registry, null, registry.getType("int"));
    assertTrue(arrowType.isSubtype(expectedArrowType));
    assertEquals(arrowType, expectedArrowType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode("function foo() { }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.getType("int"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithMismatchedParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode1 = Node.newFunctionNode("function foo(int x) { }");
    Node functionNode2 = Node.newFunctionNode("function foo(double x) { }");
    ArrowType arrowType = new ArrowType(registry, functionNode1.getParameters(), registry.getType("int"));
    ArrowType arrowType2 = new ArrowType(registry, functionNode2.getParameters(), registry.getType("int"));

    ArrowType expectedArrowType = null;
    try {
      expectedArrowType = new ArrowType(registry, functionNode1.getParameters(), registry.getType("double"));
    } catch (IllegalArgumentException e) {
      // expected
    }

    assertNull(expectedArrowType);
  }
}