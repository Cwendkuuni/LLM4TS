package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ArrowTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new ArrowTypeRegistry();
    errorReporter = new ErrorReporter() {};
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testIsSubtype_SameArrowType() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType1 = getNativeType(JSTypeNative.NUMBER);
    JSType returnType2 = getNativeType(JSTypeNative.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameters() {
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.STRING));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameParameters_Covariant() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType1 = getNativeType(JSTypeNative.NUMBER);
    JSType returnType2 = getNativeType(JSTypeNative.NUMBER).subtype(getNativeType(JSTypeNative.STRING));

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameParameters_Contravariant() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType1 = getNativeType(JSTypeNative.NUMBER);
    JSType returnType2 = getNativeType(JSTypeNative.STRING).subtype(getNativeType(JSTypeNative.NUMBER));

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, false);

    assertTrue(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    Node parameters3 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.STRING));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, getNativeType(JSTypeNative.NUMBER), false);
    ArrowType arrowType4 = new ArrowType(registry, parameters1, getNativeType(JSTypeNative.NUMBER), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType4));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testIsEquivalentTo() {
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.STRING));
    JSType returnType1 = getNativeType(JSTypeNative.NUMBER);
    JSType returnType2 = getNativeType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, false);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1, false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testGetLeastSupertype_UnsupportedOperationException() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    try {
      arrowType.getLeastSupertype(getNativeType(JSTypeNative.STRING));
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testGetGreatestSubtype_UnsupportedOperationException() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    try {
      arrowType.getGreatestSubtype(getNativeType(JSTypeNative.STRING));
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testToStringHelper() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertEquals("(function(...args) { return arguments[args.length]; })", arrowType.toStringHelper(false));
  }

  @Test
  public void testResolveInternal() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    JSType resolvedArrowType = arrowType.resolveInternal(errorReporter, scope);

    assertEquals(returnType, resolvedArrowType.returnType);

    Node param = parameters.getFirstChild();
    assertEquals(param.getJSType(), resolvedArrowType.parameters.getFirstChild().getJSType());

    for (int i = 1; i < parameters.getChildCount(); i++) {
      param = parameters.getChildByIndex(i);
      assertNull(param.getJSType());
      assertEquals(JSTypeNative.UNDEFINED, resolvedArrowType.parameters.getChildByIndex(i).getJSType().getType());
    }
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.NUMBER));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    JSType unknownType = getNativeType(JSTypeNative.UNKNOWN_TYPE);

    ArrowType arrowTypeWithUnknownType = new ArrowType(registry, parameters, returnType, false);
    arrowTypeWithUnknownType.parameters.getFirstChild().setJSType(null);

    assertTrue(arrowTypeWithUnknownType.hasUnknownParamsOrReturn());

    ArrowType arrowTypeWithUnknownReturn = new ArrowType(registry, parameters, null, false);

    assertTrue(arrowTypeWithUnknownReturn.hasUnknownParamsOrReturn());
  }
}