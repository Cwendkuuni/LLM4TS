package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeNative unknownType = JSTypeNative.getUnknownType();
  private JSTypeRegistry registry = new JSTypeRegistry();

  private ArrowType createArrowType(Node parameters, JSType returnType) {
    return new ArrowType(registry, parameters, returnType, false);
  }

  private ArrowType createArrowTypeWithInferredReturn(Node parameters) {
    return new ArrowType(registry, parameters, null, true);
  }

  @Test
  public void testIsSubtype() {
    // Test simple cases
    ArrowType arrowType1 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType2 = createArrowType(null, JSTypeNative.getNumberType());
    ArrowType arrowType3 = createArrowType(null, JSTypeNative.getStringType());

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));

    // Test with same return type but different parameter types
    ArrowType arrowType4 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType5 = createArrowType(new Node(), JSTypeNative.getNumberType());
    ArrowType arrowType6 = createArrowType(new Node(), JSTypeNative.getStringType());

    assertFalse(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType4.isSubtype(arrowType6));

    // Test with different return types but same parameter types
    JSType returnType1 = new JSTypeRegistry().getTypes().getMemberType("int");
    ArrowType arrowType7 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType8 = createArrowType(null, returnType1);

    assertFalse(arrowType7.isSubtype(arrowType8));

    // Test with arrow types that have the same parameters but different return types
    ArrowType arrowType9 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType10 = createArrowType(null, JSTypeNative.getNumberType());

    ArrowType arrowType11 = new ArrowType(registry, null, JSTypeNative.getBooleanType(), false);
    ArrowType arrowType12 = new ArrowType(registry, null, JSTypeNative.getNumberType(), false);

    assertTrue(arrowType11.isSubtype(arrowType9));
    assertTrue(arrowType12.isSubtype(arrowType10));
    assertFalse(arrowType9.isSubtype(arrowType12));
  }

  @Test
  public void testHasEqualParameters() {
    // Test simple cases
    ArrowType arrowType1 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType2 = createArrowType(null, JSTypeNative.getNumberType());
    ArrowType arrowType3 = createArrowType(null, JSTypeNative.getStringType());

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));

    // Test with same return type and same parameters
    ArrowType arrowType4 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType5 = createArrowType(new Node(), JSTypeNative.getBooleanType());

    assertTrue(arrowType4.hasEqualParameters(arrowType5));

    // Test with same return type but different parameters
    ArrowType arrowType6 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType7 = createArrowType(new Node(), JSTypeNative.getNumberType());

    assertFalse(arrowType6.hasEqualParameters(arrowType7));

    // Test with different return types but same parameters
    JSType returnType1 = new JSTypeRegistry().getTypes().getMemberType("int");
    ArrowType arrowType8 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType9 = createArrowType(null, returnType1);

    assertFalse(arrowType8.hasEqualParameters(arrowType9));
  }

  @Test
  public void testEqualsAndHashcode() {
    // Test with same return type and same parameters
    ArrowType arrowType1 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType2 = createArrowType(new Node(), JSTypeNative.getBooleanType());

    assertTrue(arrowType1.equals(arrowType2));
    assertTrue(arrowType1.hashCode() == arrowType2.hashCode());

    // Test with different return types but same parameters
    JSType returnType1 = new JSTypeRegistry().getTypes().getMemberType("int");
    ArrowType arrowType3 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType4 = createArrowType(new Node(), returnType1);

    assertFalse(arrowType3.equals(arrowType4));
    assertFalse(arrowType3.hashCode() == arrowType4.hashCode());

    // Test with same return type but different parameters
    ArrowType arrowType5 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType6 = createArrowType(new Node(new Node("int")), JSTypeNative.getBooleanType());

    assertFalse(arrowType5.equals(arrowType6));
    assertFalse(arrowType5.hashCode() == arrowType6.hashCode());

    // Test with different return types and different parameters
    ArrowType arrowType7 = createArrowType(new Node(), JSTypeNative.getBooleanType());
    ArrowType arrowType8 = createArrowType(new Node(new Node("int")), JSTypeNative.getBooleanType());

    assertFalse(arrowType7.equals(arrowType8));
    assertFalse(arrowType7.hashCode() == arrowType8.hashCode());

    // Test with null parameters
    ArrowType arrowType9 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType10 = createArrowType(null, JSTypeNative.getBooleanType());

    assertTrue(arrowType9.equals(arrowType10));
    assertTrue(arrowType9.hashCode() == arrowType10.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testVisit() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Node node, int line, int lineOffset) {
        // Do nothing
      }

      @Override
      public void warning(String message, Node node, int line, int lineOffset) {
        // Do nothing
      }
    };

    ArrowType arrowType = createArrowTypeWithInferredReturn(null);
    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(null));

    assertNotNull(resolvedArrowType);
    assertNull(resolvedArrowType.getJSType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = createArrowType(null, JSTypeNative.getBooleanType());
    ArrowType arrowType2 = createArrowType(null, null);
    ArrowType arrowType3 = createArrowTypeWithInferredReturn(null);

    assertFalse(arrowType1.hasUnknownParamsOrReturn());
    assertFalse(arrowType2.hasUnknownParamsOrReturn());
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
  }
}