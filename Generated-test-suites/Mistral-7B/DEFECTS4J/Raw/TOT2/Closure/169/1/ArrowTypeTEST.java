import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {
  @org.junit.Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParameters(JSTypeNative.getArrayType(JSType.INT));
    ArrowType arrowType = new ArrowType(registry, parameters, JSType.INT);

    // Basic functionality test
    assertTrue(arrowType.isSubtype(new ArrowType(registry, parameters, JSType.INT)));

    // Edge case 1: Test with null parameters
    ArrowType arrowTypeWithNullParameters = new ArrowType(registry, null, JSType.INT);
    assertFalse(arrowType.isSubtype(arrowTypeWithNullParameters));

    // Edge case 2: Test with different return types
    ArrowType arrowTypeWithStringReturn = new ArrowType(registry, parameters, JSType.STRING);
    assertFalse(arrowType.isSubtype(arrowTypeWithStringReturn));

    // Edge case 3: Test with different parameter types
    ArrowType arrowTypeWithDifferentParams = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.getNativeType(JSTypeNative.STRING)),
        JSType.INT);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentParams));

    // Edge case 4: Test with missing required parameters
    ArrowType arrowTypeWithMissingParams = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)),
        JSType.INT);
    assertFalse(arrowType.isSubtype(arrowTypeWithMissingParams));

    // Exception handling: Test null return type
    try {
      new ArrowType(registry, parameters, null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Exception handling: Test null parameters
    try {
      new ArrowType(registry, null, JSType.INT);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}