import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ArrowTypeTest {
  private static final ErrorReporter t = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeCreation() {
    // Basic test case for the constructor that takes a Node for parameters and return type
    Node parametersNode = arrayLiteral(numberLiteral(1), stringLiteral("str"));
    Node returnTypeNode = numberLiteral(2);
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnTypeNode);

    assertEquals("ArrowType with 1 parameter of number type and 1 return type of number", arrowType.toString(),
        "ArrowType([" + numberType + "], " + numberType + ")");
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    // Basic test case for the constructor that takes a Node for parameters and null for return type, indicating the return type is inferred
    Node parametersNode = arrayLiteral(numberLiteral(1), stringLiteral("str"));
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("ArrowType with 1 parameter of number type and inferred return type", arrowType.toString(),
        "ArrowType([" + numberType + "], " + UNKNOWN_TYPE + ")");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeCreationWithNullParameters() {
    // Test edge case for constructor with null parameters
    new ArrowType(registry, null, numberLiteral(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeCreationWithNullReturnType() {
    // Test edge case for constructor with null return type
    new ArrowType(registry, arrayLiteral(numberLiteral(1)), null);
  }

  @Test
  public void testIsSubtypeWithSameType() {
    // Test standard case, two ArrowTypes with the same parameters and return types are subtypes of each other
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertTrue("ArrowType1 is subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
    assertTrue("ArrowType2 is subtype of ArrowType1", arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentReturnType() {
    // Test edge case, two ArrowTypes with the same parameters but different return types are not subtypes of each other
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1)), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1)), stringLiteral("str"));

    assertFalse("ArrowType1 is not subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
    assertFalse("ArrowType2 is not subtype of ArrowType1", arrowType2.isSubtype(arrowType1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsSubtypeWithDifferentParameterTypes() {
    // Test edge case, ArrowType with a parameter of one type is not a subtype of ArrowType with a parameter of a different type
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1)), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(booleanLiteral(true), numberLiteral(1)), numberLiteral(1));

    assertFalse("ArrowType1 is not subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    // Test standard case, ArrowType1 and ArrowType2 have equal parameters
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertTrue("ArrowType1 has equal parameters as ArrowType2", arrowType1.hasEqualParameters((ArrowType) arrowType2));
  }

  @Test
  public void testHasUnequalParameters() {
    // Test edge case, ArrowType1 and ArrowType2 have unequal parameters
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), null), numberLiteral(1));

    assertFalse("ArrowType1 does not have equal parameters as ArrowType2", arrowType1.hasEqualParameters((ArrowType) arrowType2));
  }

  @Test
  public void testIsEquivalentToWithSameType() {
    // Test standard case, two ArrowTypes with the same parameters and return types are equivalent
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertTrue("ArrowType1 is equivalent to ArrowType2", arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testIsEquivalentToWithDifferentReturnType() {
    // Test edge case, two ArrowTypes with the same parameters but different return types are not equivalent
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), stringLiteral("str"));

    assertFalse("ArrowType1 is not equivalent to ArrowType2", arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testHashCodeWithSameType() {
    // Test standard case, two ArrowTypes with the same parameters and return types have the same hash codes
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertEquals("Hashcodes of ArrowType1 and ArrowType2 are equal", arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testHashCodeWithDifferentReturnType() {
    // Test edge case, two ArrowTypes with the same parameters but different return types have different hash codes
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), stringLiteral("str"));

    assertNotEquals("Hashcodes of ArrowType1 and ArrowType2 are not equal", arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test
  public void testGetLeastSupertypeWithSameType() {
    // Test standard case, getLeastSupertype returns the same ArrowType
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertEquals("Least supertype of ArrowType1 and ArrowType2 is the same ArrowType", arrowType1, arrowType2.getLeastSupertype(arrowType1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertypeWithDifferentReturnType() {
    // Test edge case, getLeastSupertype throws UnsupportedOperationException
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), stringLiteral("str"));

    assertFalse("Least supertype of ArrowType1 and ArrowType2 is not the same ArrowType", arrowType1.equals(arrowType2.getLeastSupertype(arrowType1)));
  }

  @Test
  public void testGetGreatestSubtypeWithSameType() {
    // Test standard case, getGreatestSubtype returns the same ArrowType
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));

    assertEquals("Greatest subtype of ArrowType1 and ArrowType2 is the same ArrowType", arrowType1, arrowType2.getGreatestSubtype(arrowType1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtypeWithDifferentReturnType() {
    // Test edge case, getGreatestSubtype throws UnsupportedOperationException
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), stringLiteral("str"));

    assertFalse("Greatest subtype of ArrowType1 and ArrowType2 is not the same ArrowType", arrowType1.equals(arrowType2.getGreatestSubtype(arrowType1)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEqualityWithDifferentParameters() {
    // Test edge case, testForEquality throws UnsupportedOperationException
    JSType arrowType1 = new ArrowType(registry, arrayLiteral(numberLiteral(1), stringLiteral("str")), numberLiteral(1));
    JSType arrowType2 = new ArrowType(registry, arrayLiteral(numberLiteral(1), booleanLiteral(true), stringLiteral("str")), numberLiteral(1));

    assertFalse("TestForEquality of ArrowType1 and ArrowType2 is not TernaryValue.TRUE", arrowType1.testForEquality(arrowType2) instanceof TernaryValue);
  }

  @Test
  public void testToString() {
    // Test standard case, toString returns the expected String representation
    JSType arrowType = new ArrowType(registry, arrayLiteral(numberLiteral(1)), numberLiteral(1));

    assertEquals("ArrowType([" + numberType + "], " + numberType + ")", arrowType.toString());
  }

  @Test
  public void testResolveInternal() {
    // Test standard case, ArrowType is resolved successfully
    JSType arrowType = new ArrowType(registry, arrayLiteral(numberLiteral(1)), numberLiteral(1));

    arrowType.resolveInternal(t, new StaticScope<JSType>(registry));

    assertTrue("ArrowType has been resolved", arrowType.isResolved());
  }

  @Test
  public void testResolveInternalWithUnknownTypes() {
    // Test edge case, ArrowType with unknown parameters or return type is not resolved
    JSType arrowType = new ArrowType(registry, null, numberLiteral(1));

    assertFalse("ArrowType with unknown parameters or return type is not resolved", arrowType.isResolved());
  }
}