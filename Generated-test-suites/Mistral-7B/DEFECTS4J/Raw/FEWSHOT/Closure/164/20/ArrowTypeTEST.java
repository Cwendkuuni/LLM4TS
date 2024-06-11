package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    // Test the base case
    ArrowType type1 = new ArrowType(registry, null, UNKNOWN_TYPE, false);
    ArrowType type2 = new ArrowType(registry, null, UNKNOWN_TYPE, false);
    assertTrue(type1.isSubtype(type2));
    assertTrue(type2.isSubtype(type1));

    // Test with different return types
    JSType returnType1 = registry.createPrimitiveType(JSType.NUMBER);
    JSType returnType2 = registry.createPrimitiveType(JSType.BOOLEAN);

    ArrowType type3 = new ArrowType(registry, null, returnType1, false);
    ArrowType type4 = new ArrowType(registry, null, returnType2, false);
    assertFalse(type3.isSubtype(type4));
    assertFalse(type4.isSubtype(type3));

    // Test with different parameter types
    Node paramNode1 = registry.createName("a");
    Node paramNode2 = registry.createName("b");

    ArrowType type5 = new ArrowType(registry, paramNode1, returnType1, false);
    ArrowType type6 = new ArrowType(registry, paramNode2, returnType1, false);
    assertFalse(type5.isSubtype(type6));
    assertFalse(type6.isSubtype(type5));

    // Test with a parameter and a varargs parameter
    JSType varargsType = registry.createParametersWithVarArgs(returnType1);
    ArrowType type7 = new ArrowType(registry, paramNode1, returnType1, false);
    ArrowType type8 = new ArrowType(registry, paramNode1, varargsType, false);
    assertTrue(type7.isSubtype(type8));
    assertFalse(type8.isSubtype(type7));

    // Test with a varargs parameter and no parameters
    ArrowType type9 = new ArrowType(registry, varargsType, returnType1, false);
    ArrowType type10 = new ArrowType(registry, null, returnType1, false);
    assertTrue(type10.isSubtype(type9));
    assertFalse(type9.isSubtype(type10));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramNode1 = registry.createName("a");
    Node paramNode2 = registry.createName("b");
    Node paramNode3 = registry.createName("c");

    ArrowType type1 = new ArrowType(registry, paramNode1, null, false);
    ArrowType type2 = new ArrowType(registry, paramNode1, null, false);
    ArrowType type3 = new ArrowType(registry, paramNode2, null, false);
    ArrowType type4 = new ArrowType(registry, paramNode3, null, false);
    ArrowType type5 = new ArrowType(registry, paramNode1, paramNode2, false);
    ArrowType type6 = new ArrowType(registry, paramNode1, paramNode2, true);

    assertTrue(type1.hasEqualParameters(type1));
    assertTrue(type1.hasEqualParameters(type2));
    assertFalse(type1.hasEqualParameters(type3));
    assertFalse(type1.hasEqualParameters(type4));
    assertFalse(type1.hasEqualParameters(type5));
    assertFalse(type1.hasEqualParameters(type6));
  }

  @Test
  public void testResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();

    Node paramNode1 = registry.createName("a");
    Node paramNode2 = registry.createName("b");
    JSType returnType1 = registry.createPrimitiveType(JSType.NUMBER);
    JSType returnType2 = registry.createPrimitiveType(JSType.BOOLEAN);

    ArrowType arrowType1 = new ArrowType(registry, paramNode1, returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, paramNode2, returnType2, false);

    // Test resolving a known type
    JSType resolvedType1 = arrowType1.resolve(errorReporter, scope);
    assertTrue(resolvedType1.equals(arrowType1));

    // Test resolving an undefined type
    JSType undefinedType = registry.createPrimitiveType(JSType.UNDEFINED);
    ArrowType arrowType3 = new ArrowType(registry, paramNode1, undefinedType, false);
    JSType resolvedType2 = arrowType3.resolve(errorReporter, scope);
    assertTrue(resolvedType2.isUnknownType());

    // Test resolving a circular dependency
    JSType cycleType1 = new CycleType(registry);
    ArrowType arrowType4 = new ArrowType(registry, paramNode1, cycleType1, false);
    JSType resolvedType3 = arrowType4.resolve(errorReporter, scope);
    assertTrue(resolvedType3.isUnknownType());
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node paramNode1 = registry.createName("a");
    Node paramNode2 = registry.createName("b");

    ArrowType type1 = new ArrowType(registry, paramNode1, null, false);
    ArrowType type2 = new ArrowType(registry, paramNode1, null, true);
    ArrowType type3 = new ArrowType(registry, paramNode1, paramNode2, false);
    ArrowType type4 = new ArrowType(registry, paramNode1, paramNode2, true);

    assertFalse(type1.hasUnknownParamsOrReturn());
    assertTrue(type2.hasUnknownParamsOrReturn());
    assertFalse(type3.hasUnknownParamsOrReturn());
    assertTrue(type4.hasUnknownParamsOrReturn());
  }
}

class CycleType extends JSType {
  private static final long serialVersionUID = 1L;

  CycleType(JSTypeRegistry registry) {
    super(registry);
  }

  @Override
  public boolean isSubtype(JSType other) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEquivalentTo(JSType other) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JSType getLeastSupertype(JSType that) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JSType getGreatestSubtype(JSType that) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TernaryValue testForEquality(JSType that) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T visit(Visitor<T> visitor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BooleanLiteralSet getPossibleToBooleanOutcomes() {
    return BooleanLiteralSet.TRUE;
  }

  @Override
  public JSType resolveInternal(ErrorReporter t, StaticScope<JSType> scope) {
    return this;
  }
}