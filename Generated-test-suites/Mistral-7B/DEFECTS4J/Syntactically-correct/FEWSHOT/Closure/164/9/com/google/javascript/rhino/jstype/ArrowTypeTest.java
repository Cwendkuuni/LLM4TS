package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.type.JSType;
import com.google.javascript.rhino.type.JSTypeRegistry;
import com.google.javascript.rhino.type.TernaryValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private JSTypeRegistry mockedRegistry;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);
    staticScope = mock(StaticScope.class);
    mockedRegistry = mock(JSTypeRegistry.class);

    when(registry.createParametersWithVarArgs(ArgumentMatchers.eq(UNKNOWN_TYPE)))
        .thenReturn(createParametersWithVarArgs(UNKNOWN_TYPE));
  }

  @Test
  public void testIsSubtype_IdenticalArrowTypes() {
    Node parameters1 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameters2 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    JSType returnType1 = new ArrowType(registry, parameters1, getNativeType(UNKNOWN_TYPE));
    JSType returnType2 = new ArrowType(registry, parameters2, getNativeType(UNKNOWN_TYPE));

    assertTrue(returnType1.isSubtype(returnType2));
    assertTrue(returnType2.isSubtype(returnType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypes() {
    Node parameters1 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameters2 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    JSType returnType1 = new ArrowType(registry, parameters1, getNativeType(INT_TYPE));
    JSType returnType2 = new ArrowType(registry, parameters2, getNativeType(STRING_TYPE));

    assertFalse(returnType1.isSubtype(returnType2));
    assertFalse(returnType2.isSubtype(returnType1));
  }

  @Test
  public void testIsSubtype_DifferentParameterLists() {
    Node parameters1 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameters2 = new Node(createParametersWithVarArgs(new Node(getParameterType(INT_TYPE))));
    JSType returnType = getNativeType(UNKNOWN_TYPE);

    JSType arrowType1 = new ArrowType(registry, parameters1, returnType);
    JSType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypesWithVarArgs() {
    Node parameters = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    JSType returnType1 = new ArrowType(registry, parameters, getNativeType(INT_TYPE));
    JSType returnType2 = new ArrowType(registry, parameters, getNativeType(VAR_ARGS_TYPE));

    assertFalse(returnType1.isSubtype(returnType2));
    assertFalse(returnType2.isSubtype(returnType1));
  }

  @Test
  public void testIsSubtype_DifferentReturnTypesWithMixedVarArgs() {
    Node parameters1 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameters2 = new Node(createParametersWithVarArgs(new Node(getParameterType(INT_TYPE)), getParameterType(VAR_ARGS_TYPE)));
    JSType returnType1 = new ArrowType(registry, parameters1, getNativeType(INT_TYPE));
    JSType returnType2 = new ArrowType(registry, parameters2, getNativeType(VAR_ARGS_TYPE));

    assertFalse(returnType1.isSubtype(returnType2));
    assertFalse(returnType2.isSubtype(returnType1));
  }

  @Test
  public void testHasEqualParameters() {
    Node parameters1 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameters2 = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    Node parameter1 = new Node(getParameterType(INT_TYPE));
    Node parameter2 = new Node(getParameterType(INT_TYPE));
    Node parameter3 = new Node(getParameterType(VAR_ARGS_TYPE));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType3 = new ArrowType(registry, new Node(parameter1, parameter2, parameter3), getNativeType(UNKNOWN_TYPE));

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(new ArrowType(registry, parameters1, getNativeType(STRING_TYPE), false)));
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testResolve() {
    when(registry.createParametersWithVarArgs(ArgumentMatchers.eq(UNKNOWN_TYPE)))
        .thenReturn(createParametersWithVarArgs(UNKNOWN_TYPE));

    ArrowType arrowType = new ArrowType(registry, null, getNativeType(INT_TYPE));
    arrowType.resolveInternal(errorReporter, staticScope);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    Node parameters = new Node(createParametersWithVarArgs(UNKNOWN_TYPE));
    arrowType = new ArrowType(registry, parameters, getNativeType(INT_TYPE));
    arrowType.resolveInternal(errorReporter, staticScope);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertypeUnsupported() {
    JSType arrowType = new ArrowType(registry, null, getNativeType(INT_TYPE));
    arrowType.getLeastSupertype(getNativeType(STRING_TYPE));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtypeUnsupported() {
    JSType arrowType = new ArrowType(registry, null, getNativeType(INT_TYPE));
    arrowType.getGreatestSubtype(getNativeType(STRING_TYPE));
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType = new ArrowType(registry, null, getNativeType(INT_TYPE));

    assertEquals("function(...)", arrowType.toStringHelper(false));
  }

  private Node createParametersWithVarArgs(JSType type) {
    Node node = new Node(new Node(new Node("..."), getParameterType(type)));
    node.setVarArgs(true);
    return node;
  }

  private Node getParameterType(JSType type) {
    Node node = new Node();
    node.setJSType(type);
    return node;
  }

  private Node getNativeType(JSType type) {
    Node node = new Node();
    node.setJSType(type);
    return node;
  }

  private static JSType INT_TYPE = getNativeType(JSTypeNative.INT);
  private static JSType STRING_TYPE = getNativeType(JSTypeNative.STRING);
  private static JSType VAR_ARGS_TYPE = getNativeType(JSTypeNative.VAR_ARGS);
}