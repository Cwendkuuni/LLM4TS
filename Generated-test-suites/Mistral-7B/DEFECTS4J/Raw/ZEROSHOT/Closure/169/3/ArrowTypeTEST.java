import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.UnknownType;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeCreation() {
    JSType parameters = registry.createParametersWithVarArgs(registry.getNativeType(UnknownType.class));
    JSType returnType = registry.getNativeType(Integer.class);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
    assertEquals(false, arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSType parameters = registry.createParametersWithVarArgs(registry.getNativeType(UnknownType.class));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertEquals(parameters, arrowType.getParameters());
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testIsSubtype() {
    JSType parameters1 = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType parameters2 = registry.createParametersWithVarArgs(registry.getNativeType(String.class));
    JSType returnType1 = registry.getNativeType(Integer.class);
    JSType returnType2 = registry.getNativeType(Float.class);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    // arrowType1 is a subtype of arrowType2 (covariant return type)
    assertTrue(arrowType1.isSubtype(arrowType2));

    // arrowType2 is not a subtype of arrowType1 (contravariant parameter types)
    assertFalse(arrowType2.isSubtype(arrowType1));

    // arrowType1 is not a subtype of itself (identity)
    assertFalse(arrowType1.isSubtype(arrowType1));

    // arrowType1 is not a subtype of a FunctionType (incompatible types)
    FunctionType functionType = new FunctionType(registry, null, registry.getNativeType(Integer.class));
    assertFalse(arrowType1.isSubtype(functionType));

    // FunctionType is not a subtype of arrowType1 (incompatible types)
    assertFalse(functionType.isSubtype(arrowType1));

    // ArrowType with unknown return type is a subtype of ArrowType with unknown return type (covariant)
    ArrowType arrowTypeUnknownReturn1 = new ArrowType(registry, parameters1, null);
    ArrowType arrowTypeUnknownReturn2 = new ArrowType(registry, parameters2, null);
    assertTrue(arrowTypeUnknownReturn1.isSubtype(arrowTypeUnknownReturn2));

    // ArrowType with inferred return type is a subtype of ArrowType with known return type (covariant)
    ArrowType arrowTypeInferredReturn = new ArrowType(registry, parameters1, null, true);
    ArrowType arrowTypeKnownReturn = new ArrowType(registry, parameters1, returnType1);
    assertTrue(arrowTypeInferredReturn.isSubtype(arrowTypeKnownReturn));
  }

  @Test
  public void testHasEqualParameters() {
    JSType parameters1 = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType parameters2 = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType(Integer.class));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, registry.getNativeType(Integer.class));
    ArrowType arrowType3 = new ArrowType(registry, parameters1, registry.getNativeType(String.class));

    assertTrue(arrowType1.hasEqualParameters(arrowType1, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));

    // arrowType1 and arrowType2 have equal parameters, so they are equal
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));

    // arrowType1 and arrowType3 do not have equal parameters, they are not equal
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
  }

  @Test
  public void testCheckEquivalenceHelper() {
    JSType parameters1 = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType parameters2 = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType returnType1 = registry.getNativeType(Integer.class);
    JSType returnType2 = registry.getNativeType(Float.class);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

    // arrowType1 and arrowType1 are equal, so their checkEquivalenceHelper should return true
    assertTrue(arrowType1.checkEquivalenceHelper(arrowType1, false));

    // arrowType1 and arrowType2 are not equal, so their checkEquivalenceHelper should return false
    assertFalse(arrowType1.checkEquivalenceHelper(arrowType2, false));

    // arrowType1 and arrowType3 are equal, so their checkEquivalenceHelper should return true
    assertTrue(arrowType1.checkEquivalenceHelper(arrowType3, false));
  }

  @Test
  public void testHashCode() {
    JSType parameters = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType returnType = registry.getNativeType(Integer.class);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    ArrowType arrowType3 = new ArrowType(registry, parameters, null);
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("Method not supported.");
  }

  @Test
  public void testToStringHelper() {
    JSType parameters = registry.createParametersWithVarArgs(registry.getNativeType(Number.class));
    JSType returnType = registry.getNativeType(Integer.class);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    String expected = "[ArrowType]";
    assertEquals(expected, arrowType.toStringHelper(false));
  }
}