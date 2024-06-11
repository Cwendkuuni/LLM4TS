package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testIsSubtype_EmptyArrowTypes() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    ArrowType arrowType1 = new ArrowType(registry, null,
        registry.createStringType(), false);
    ArrowType arrowType2 = new ArrowType(registry, null,
        registry.createNumberType(), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameReturnType() {
    ArrowType arrowType1 = new ArrowType(registry, null,
        registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry, null,
        registry.createNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentParameterTypes() {
    ArrowType arrowType1 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createStringType()),
        registry.createNumberType(), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_SameParameterTypes() {
    ArrowType arrowType1 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ParameterTypeInferred() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, true);
    ArrowType arrowType2 = new ArrowType(registry, null,
        registry.createNumberType(), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_ParameterTypeUnknown() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    ArrowType arrowType2 = new ArrowType(registry, null,
        JSTypeNative.UNKNOWN_TYPE, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_EmptyArrowTypes() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHasEqualParameters_DifferentReturnTypes() {
    ArrowType arrowType1 = new ArrowType(registry, null,
        registry.createStringType(), false);
    ArrowType arrowType2 = new ArrowType(registry, null,
        registry.createNumberType(), false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHasEqualParameters_SameReturnType_DifferentParameters() {
    ArrowType arrowType1 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createStringType()),
        registry.createNumberType(), false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHasEqualParameters_SameReturnType_SameParameters() {
    ArrowType arrowType1 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testToString() {
    ArrowType arrowType = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createNumberType()),
        registry.createNumberType(), false);

    String expected = "function({})->Number";
    String actual = arrowType.toString();

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testResolveInternal_NullParameters() {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNumberType(), false);

    ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, null);

    assertNotNull(resolvedArrowType.getParameters());
  }

  @Test
  public void testResolveInternal_UnknownParameters() {
    Node parameters = Node.newList();
    for (int i = 0; i < 10; i++) {
      parameters.addChild(Node.newIdent("p" + i));
      parameters.setJSType(registry.getParameterType("p" + i, i));
    }

    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNumberType(), false);

    ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, null);

    assertNotNull(resolvedArrowType.getParameters());
    Node paramNode = resolvedArrowType.getParameters().getFirstChild();
    assertNotNull(paramNode);
    assertTrue(paramNode.getJSType().isUnknownType());
  }

  @Test
  public void testResolveInternal_UnknownReturnType() {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, null, null, false);

    ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, null);

    assertNull(resolvedArrowType.getReturnType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(registry, null, null, false);
    ArrowType arrowType2 = new ArrowType(registry, null, null, true);

    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertTrue(arrowType2.hasUnknownParamsOrReturn());

    Node parameters = Node.newList();
    ArrowType arrowType3 = new ArrowType(registry, parameters, null, false);

    assertTrue(arrowType3.hasUnknownParamsOrReturn());

    ArrowType arrowType4 = new ArrowType(registry, null,
        registry.createNumberType(), true);

    assertTrue(arrowType4.hasUnknownParamsOrReturn());
  }
}