package com.google.javascript.rhino.jstype.test;

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
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeVisitor;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testIsSubtype_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    JSType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType));
  }

  @Test
  public void testIsSubtype_DifferentArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType1 = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(JSTypeNative.STRING));
    JSType returnType2 = getNativeType(JSTypeNative.BOOLEAN);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowFunctionWithVarArgsAndFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowParams = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, arrowParams, returnType);

    Node functionParams = registry.createParameters(getNativeType(JSTypeNative.NUMBER));
    Node functionBody = registry.createBlock(getNativeType(JSTypeNative.VOID));
    FunctionType functionType = new FunctionType(registry, functionParams, getNativeType(JSTypeNative.VOID), null);

    assertFalse(arrowType.isSubtype(functionType));
  }

  @Test
  public void testIsSubtype_ArrowFunctionWithoutVarArgsAndFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node arrowParams = registry.createParameters(getNativeType(UNKNOWN_TYPE), getNativeType(JSTypeNative.STRING));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, arrowParams, returnType);

    Node functionParams = registry.createParameters(getNativeType(JSTypeNative.NUMBER));
    Node functionBody = registry.createBlock(getNativeType(JSTypeNative.VOID));
    FunctionType functionType = new FunctionType(registry, functionParams, getNativeType(JSTypeNative.VOID), null);

    // The arrow function should be a subtype of the function, because
    // it can accept any arguments (including the function's arguments).
    assertTrue(arrowType.isSubtype(functionType));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, parameters1, getNativeType(JSTypeNative.NUMBER));

    assertTrue(arrowType.hasEqualParameters(new ArrowType(registry, parameters1, getNativeType(JSTypeNative.NUMBER))));
    assertTrue(arrowType.hasEqualParameters(new ArrowType(registry, parameters2, getNativeType(JSTypeNative.NUMBER))));
    assertTrue(arrowType.hasEqualParameters(new ArrowType(registry, parameters2, getNativeType(JSTypeNative.NUMBER), true)));
    assertFalse(arrowType.hasEqualParameters(new ArrowType(registry, parameters1, getNativeType(JSTypeNative.BOOLEAN))));
  }

  @Test
  public void testEqualsAndHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

    // Test equals
    assertTrue(arrowType1.equals(arrowType2));
    assertTrue(arrowType2.equals(arrowType1));

    // Test hashcode
    int hashcode1 = arrowType1.hashCode();
    int hashcode2 = arrowType2.hashCode();
    assertEquals(hashcode1, hashcode2);

    // Test equals with different return types
    ArrowType arrowType3 = new ArrowType(registry, parameters, getNativeType(JSTypeNative.BOOLEAN));
    assertFalse(arrowType1.equals(arrowType3));
    assertFalse(arrowType3.equals(arrowType1));

    // Test hashcode with different return types
    int hashcode3 = arrowType3.hashCode();
    assertNotEquals(hashcode1, hashcode3);
  }

  @Test
  public void testResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry);

    JSType resolvedArrowType = arrowType.resolve(errorReporter, scope);

    assertNotNull(resolvedArrowType);
    assertEquals(arrowType, resolvedArrowType);
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSTypeVisitor visitor = new JSTypeVisitor() {
      @Override
      public JSType visitArrowType(ArrowType arrowType) {
        return getNativeType(JSTypeNative.BOOLEAN);
      }
    };

    JSType result = arrowType.visit(visitor);

    assertNotNull(result);
    assertFalse(result.isEquivalentTo(arrowType));
    assertTrue(result instanceof JSTypeNative);
    assertEquals(JSTypeNative.BOOLEAN, result);
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType returnType = getNativeType(JSTypeNative.NUMBER);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    String toString = arrowType.toStringHelper(false);

    assertTrue(toString.contains("ArrowType"));
    assertTrue(toString.contains("parameters: " + parameters.toString()));
    assertTrue(toString.contains("returnType: " + returnType.toString()));
  }

  private static JSType getNativeType(int typeCode) {
    return JSTypeNative.getType(typeCode);
  }
}