package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    errorReporter = mock(ErrorReporter.class);
    scope = mock(StaticScope.class);
  }

  @Test
  public void testIsSubtype_sameType() {
    Node parameters = Node.newList();
    JSType returnType = JSTypeNative.STRING;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    JSType sameType = new ArrowType(registry, parameters, returnType, false);

    assertTrue(arrowType.isSubtype(sameType));
  }

  @Test
  public void testIsSubtype_differentReturnTypes() {
    Node parameters = Node.newList();
    JSType returnType1 = JSTypeNative.NUMBER;
    JSType returnType2 = JSTypeNative.STRING;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_differentParameters() {
    Node parameters1 = Node.newList(Node.newName("string"), Node.newName("number"));
    Node parameters2 = Node.newList(Node.newName("number"), Node.newName("string"));
    JSType returnType = JSTypeNative.UNKNOWN_TYPE;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_sameParameters() {
    Node parameters = Node.newList(Node.newName("string"), Node.newName("number"));
    JSType returnType1 = JSTypeNative.NUMBER;
    JSType returnType2 = JSTypeNative.STRING;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, false);

    // Both ArrowType objects have the same parameters.
    // The return types are different, but ArrowType is covariant with respect to the return type.
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_differentReturnTypeInferred() {
    Node parameters = Node.newList();
    JSType returnType1 = JSTypeNative.NUMBER;
    JSType returnType2 = JSTypeNative.STRING;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, true);

    // arrowType1 has a known return type, so it is not a subtype of arrowType2
    // which has an inferred return type.
    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_sameReturnTypeInferred() {
    Node parameters = Node.newList();
    JSType returnType1 = JSTypeNative.UNKNOWN_TYPE;
    JSType returnType2 = JSTypeNative.UNKNOWN_TYPE;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1, true);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2, true);

    // Both ArrowType objects have inferred return types, so they are considered equal.
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters() {
    Node parameters1 = Node.newList(Node.newName("string"), Node.newName("number"));
    Node parameters2 = Node.newList(Node.newName("number"), Node.newName("string"));
    ArrowType arrowType1 = new ArrowType(registry, parameters1, null, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, null, false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));

    parameters1 = Node.newList(Node.newName("number"), Node.newName("string"));
    arrowType1 = new ArrowType(registry, parameters1, null, false);
    ArrowType arrowType3 = new ArrowType(registry, parameters2, null, false);

    // Both ArrowType objects have the same number of parameters, but the types
    // are different.
    assertFalse(arrowType1.hasEqualParameters(arrowType3));

    Node parameters3 = Node.newList(Node.newList(Node.newName("string"), Node.newName("number")), Node.newName("boolean"));
    ArrowType arrowType4 = new ArrowType(registry, parameters3, null, false);

    // parameters1 and parameters3 have the same types in the same positions,
    // but they have a different number of parameters.
    assertFalse(arrowType1.hasEqualParameters(arrowType4));

    parameters1 = Node.newList(Node.newList(Node.newName("string"), Node.newName("number")), Node.newName("boolean"));
    arrowType1 = new ArrowType(registry, parameters1, null, false);

    // parameters1 and parameters3 are equal.
    assertTrue(arrowType1.hasEqualParameters(arrowType4));
  }

  @Test
  public void testIsEquivalentTo() {
    Node parameters1 = Node.newList(Node.newName("string"), Node.newName("number"));
    Node parameters2 = Node.newList(Node.newName("number"), Node.newName("string"));
    JSType returnType = JSTypeNative.UNKNOWN_TYPE;
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType, false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));

    parameters1 = Node.newList(Node.newName("number"), Node.newName("string"));
    arrowType1 = new ArrowType(registry, parameters1, returnType, false);
    ArrowType arrowType3 = new ArrowType(registry, parameters2, returnType, false);

    // The ArrowType objects have different parameter lists, so they are not equal.
    assertFalse(arrowType1.isEquivalentTo(arrowType3));

    Node parameters3 = Node.newList(Node.newList(Node.newName("string"), Node.newName("number")), Node.newName("boolean"));
    ArrowType arrowType4 = new ArrowType(registry, parameters3, null, false);

    // parameters1 and parameters3 have the same types in the same positions,
    // but they have a different number of parameters.
    assertFalse(arrowType1.isEquivalentTo(arrowType4));

    parameters1 = Node.newList(Node.newList(Node.newName("string"), Node.newName("number")), Node.newName("boolean"));
    arrowType1 = new ArrowType(registry, parameters1, returnType, false);

    // parameters1 and parameters3 are equal, but they have different return types.
    assertFalse(arrowType1.isEquivalentTo(arrowType4));

    // Both ArrowType objects have the same parameter lists and return types.
    Node parameters4 = Node.newList(Node.newList(Node.newName("number"), Node.newName("string")), Node.newName("boolean"));
    ArrowType arrowType5 = new ArrowType(registry, parameters4, returnType, false);

    assertTrue(arrowType1.isEquivalentTo(arrowType5));
  }

  @Test
  public void testHashCode() {
    Node parameters = Node.newList(Node.newName("string"), Node.newName("number"));
    JSType returnType = JSTypeNative.STRING;
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertTrue(hashCode1 == hashCode2);

    returnType = JSTypeNative.NUMBER;
    arrowType1 = new ArrowType(registry, parameters, returnType, false);

    assertFalse(hashCode1 == arrowType1.hashCode());

    ArrowType arrowType3 = new ArrowType(registry, parameters, null, false);

    assertFalse(hashCode1 == arrowType3.hashCode());

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(), returnType, false);

    assertFalse(hashCode1 == arrowType4.hashCode());

    ArrowType arrowType5 = new ArrowType(registry, parameters, returnType, true);

    assertFalse(hashCode1 == arrowType5.hashCode());
  }

  @Test
  public void testResolveInternal() {
    Node parameters = Node.newList(Node.newName("string"), Node.newName("number"));
    JSType returnType = JSTypeNative.STRING;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    // Call resolveInternal with no arguments.
    arrowType.resolveInternal(errorReporter, scope);

    // The return type and parameters should have been resolved.
    JSType resolvedReturnType = arrowType.returnType;
    Node resolvedParameters = arrowType.parameters;

    assertTrue(resolvedReturnType != null);
    assertTrue(resolvedParameters != null);

    // The resolved types should be the same as the original types.
    assertEquals(returnType, resolvedReturnType);
    assertEquals(parameters, resolvedParameters);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node parameters = Node.newList(Node.newName("string"), Node.newName("number"));
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

    // The arrow type has no unknown types.
    assertFalse(arrowType.hasUnknownParamsOrReturn());

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), null, true);

    // The arrow type has an inferred return type.
    assertTrue(arrowType2.hasUnknownParamsOrReturn());

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(), null, false);
    ArrowType arrowType4 = new ArrowType(registry, Node.newList(Node.newName("boolean")), null, false);

    // The arrow types have an unknown parameter.
    assertTrue(arrowType3.hasUnknownParamsOrReturn());
    assertTrue(arrowType4.hasUnknownParamsOrReturn());
  }
}