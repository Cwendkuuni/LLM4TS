import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  private static final ErrorReporter t = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.Test
  public void testArrowTypeWithSimpleParametersAndReturnType() {
    Node arrowNode = registry.parseExpression("function(x, y) {}");
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);

    JSType expected = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number")),
        registry.createNamedType("Number"));

    assertEquals(expected, arrowType);

    // Test subtyping
    JSType subType = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number"),
            registry.createNamedType("Number")),
        registry.createNamedType("Number"));
    assertTrue(arrowType.isSubtype(subType));

    // Test isSubtype with null types
    JSType nullType = UNKNOWN_TYPE;
    ArrowType nullArrowType = new ArrowType(registry, null, nullType, true);
    assertFalse(arrowType.isSubtype(nullArrowType));
    assertFalse(nullArrowType.isSubtype(arrowType));

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType1);
  }

  @org.junit.Test
  public void testArrowTypeWithVarArgsParameterAndNoReturnType() {
    Node arrowNode = registry.parseExpression("function(x, y...) {}");
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);

    JSType expected = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createParameterVarArgsType(registry.createNamedType("Number"))),
        null);

    assertEquals(expected, arrowType);

    // Test subtyping
    JSType subType = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number"),
            registry.createNamedType("String"),
            registry.createParameterVarArgsType(registry.createNamedType("Number"))),
        null);
    assertTrue(arrowType.isSubtype(subType));

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType1);
  }

  @org.junit.Test
  public void testArrowTypeWithOptionalParameterAndReturnType() {
    Node arrowNode = registry.parseExpression("function(x, y = 0) {}");
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);

    JSType expected = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number").setOptional(true)),
        registry.createNamedType("Number"));

    assertEquals(expected, arrowType);

    // Test subtyping
    JSType subType = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number"),
            registry.createNamedType("String"),
            registry.createNamedType("Number")),
        registry.createNamedType("Number"));
    assertTrue(arrowType.isSubtype(subType));

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertEquals(arrowType, arrowType1);
  }

  @org.junit.Test
  public void testArrowTypeWithMismatchedParameterTypes() {
    Node arrowNode = registry.parseExpression("function(x, y) {}");
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("String"), false);

    // Test subtyping
    JSType subType = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number")),
        registry.createNamedType("Number"));
    assertFalse(arrowType.isSubtype(subType));

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("String"), false);
    assertNotEquals(arrowType1, arrowType2);

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("Number"), true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        registry.createNamedType("String"), false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertNotEquals(arrowType, arrowType1);
  }

  @org.junit.Test
  public void testArrowTypeWithUnresolvedReturnType() {
    Node arrowNode = registry.parseExpression("function(x) {}", new ErrorReporter() {
      @Override
      public void error(String message) {
        // Intentionally left empty
      }
    });
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertNull(arrowType);
  }

  @org.junit.Test
  public void testArrowTypeWithUnresolvedParameters() {
    Node arrowNode = registry.parseExpression("function(x, y) {}", new ErrorReporter() {
      @Override
      public void error(String message) {
        // Intentionally left empty
      }
    });
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertNull(arrowType);
  }

  @org.junit.Test
  public void testArrowTypeWithUnresolvedParametersAndReturnType() {
    Node arrowNode = registry.parseExpression("function(x, y) {}", new ErrorReporter() {
      @Override
      public void error(String message) {
        // Intentionally left empty
      }
    });
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertNull(arrowType);
  }

  @org.junit.Test
  public void testArrowTypeWithMismatchedParameterCounts() {
    Node arrowNode = registry.parseExpression("function(x) {}",
        new ErrorReporter() {
          @Override
          public void error(String message) {
            // Intentionally left empty
          }
        });
    ArrowType arrowType = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);

    JSType expected = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number")),
        null);

    // Test subtyping
    JSType subType = registry.createFunctionType(
        registry.createParametersWithVarArgs(
            registry.createNamedType("Number"),
            registry.createNamedType("Number")),
        registry.createNamedType("Number"));
    assertFalse(arrowType.isSubtype(subType));

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    ArrowType arrowType2 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertTrue(arrowType1.equals(arrowType2));

    // Test hashCode
    ArrowType arrowType3 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, true);
    ArrowType arrowType4 = new ArrowType(registry, arrowNode.getFirstChild(),
        null, false);
    assertNotSame(arrowType3.hashCode(), arrowType4.hashCode());

    // Test resolve
    arrowType = arrowType.resolve(t, new StaticScope<JSType>(registry));
    assertEquals(arrowType, expected);
  }
}