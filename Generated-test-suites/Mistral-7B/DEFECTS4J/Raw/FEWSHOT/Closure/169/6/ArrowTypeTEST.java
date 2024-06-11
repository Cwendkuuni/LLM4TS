package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;
  private ArrowType arrowType;
  private ArrowType arrowType2;
  private ArrowType arrowTypeNullReturn;
  private ArrowType arrowTypeSameParams;
  private ArrowType arrowTypeDifferentParams;
  private ArrowType arrowTypeVarArgs;
  private ArrowType arrowTypeOptional;
  private ArrowType arrowTypeNoType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    scope = new StaticScope<>(registry);

    arrowType = new ArrowType(registry, null, JSTypeNative.VOID_TYPE, false);
    arrowType2 = new ArrowType(registry, null, JSTypeNative.INT_TYPE, false);
    arrowTypeNullReturn = new ArrowType(registry, null, null, true);
    arrowTypeSameParams = new ArrowType(registry, null, JSTypeNative.INT_TYPE, false);
    arrowTypeDifferentParams = new ArrowType(registry, null, JSTypeNative.FLOAT_TYPE, false);
    arrowTypeVarArgs = new ArrowType(registry, null, JSTypeNative.INT_TYPE, false);
    arrowTypeOptional = new ArrowType(registry, null, JSTypeNative.INT_TYPE, false);
    arrowTypeOptional.getFirstChild().setOptionalArg(true);
    arrowTypeNoType = new ArrowType(registry, null, null, false);
  }

  @Test
  public void testIsSubtype() {
    // Void -> Int is a subtype
    assertTrue(arrowType.isSubtype(arrowType2));

    // Void -> Void is a subtype
    assertTrue(arrowType.isSubtype(arrowType));

    // Int -> Void is not a subtype
    assertFalse(arrowType2.isSubtype(arrowType));

    // Int -> Int is a subtype
    assertTrue(arrowType2.isSubtype(arrowType2));

    // Int -> Float is not a subtype
    assertFalse(arrowType2.isSubtype(arrowTypeDifferentParams));

    // Int -> [Int, Int] is not a subtype
    assertFalse(arrowType2.isSubtype(arrowTypeVarArgs));

    // Int -> Int with optional argument is a subtype of Int -> Int
    assertTrue(arrowTypeOptional.isSubtype(arrowTypeSameParams));

    // Int -> Int is a subtype of Int -> Int with optional argument
    assertTrue(arrowTypeSameParams.isSubtype(arrowTypeOptional));

    // Int -> Int is not a subtype of Int -> NoType
    assertFalse(arrowTypeSameParams.isSubtype(arrowTypeNoType));

    // Int -> Int with optional argument is not a subtype of Int -> NoType
    assertFalse(arrowTypeOptional.isSubtype(arrowTypeNoType));

    // Int -> Int with varargs is not a subtype of Int -> Int
    assertFalse(arrowTypeVarArgs.isSubtype(arrowTypeSameParams));

    // Int -> Int is not a subtype of Int -> Int with varargs
    assertFalse(arrowTypeSameParams.isSubtype(arrowTypeVarArgs));

    // Int -> Int is not a subtype of Int -> Int with optional argument and varargs
    assertFalse(arrowTypeSameParams.isSubtype(arrowTypeOptional));

    // Int -> Int with optional argument and varargs is not a subtype of Int -> Int
    assertFalse(arrowTypeSameParams.isSubtype(arrowTypeVarArgs));

    // Int -> Int with optional argument and varargs is not a subtype of Int -> Int with optional argument
    assertFalse(arrowTypeSameParams.isSubtype(arrowTypeOptional));

    // Null return type is not a subtype of any non-null type
    assertFalse(arrowTypeNullReturn.isSubtype(arrowType2));

    // Null return type is not a subtype of itself
    assertFalse(arrowTypeNullReturn.isSubtype(arrowTypeNullReturn));

    // Unknown return type is a subtype of any type, including itself
    assertTrue(arrowType.isSubtype(arrowType));
    assertTrue(arrowType.isSubtype(arrowTypeNullReturn));
    assertTrue(arrowTypeNullReturn.isSubtype(arrowType));
    assertTrue(arrowTypeNullReturn.isSubtype(arrowTypeNullReturn));
  }

  @Test
  public void testHasEqualParameters() {
    // Int -> Int and Int -> Int have equal parameters
    assertTrue(arrowTypeSameParams.hasEqualParameters(arrowTypeSameParams, true));

    // Int -> Int and Int -> Int with optional argument do not have equal parameters
    assertFalse(arrowTypeSameParams.hasEqualParameters(arrowTypeOptional, true));

    // Int -> Int and Int -> Int with varargs do not have equal parameters
    assertFalse(arrowTypeSameParams.hasEqualParameters(arrowTypeVarArgs, true));

    // Int -> Int and Int -> Int with optional argument and varargs do not have equal parameters
    assertFalse(arrowTypeSameParams.hasEqualParameters(arrowTypeOptional, false));

    // Int -> Int with optional argument and Int -> Int do not have equal parameters
    assertFalse(arrowTypeOptional.hasEqualParameters(arrowTypeSameParams, true));

    // Int -> Int with optional argument and Int -> Int with optional argument have equal parameters
    assertTrue(arrowTypeOptional.hasEqualParameters(arrowTypeOptional, true));

    // Int -> Int with optional argument and Int -> Int with varargs do not have equal parameters
    assertFalse(arrowTypeOptional.hasEqualParameters(arrowTypeVarArgs, true));

    // Int -> Int with optional argument and Int -> Int with varargs and optional argument have equal parameters
    assertTrue(arrowTypeOptional.hasEqualParameters(arrowTypeVarArgs, false));

    // Int -> Int with optional argument and Int -> Int with optional argument and varargs do not have equal parameters
    assertFalse(arrowTypeOptional.hasEqualParameters(arrowTypeOptional, false));

    // Int -> Int with optional argument and Int -> Int with optional argument and varargs have equal parameters
    assertTrue(arrowTypeOptional.hasEqualParameters(arrowTypeOptional, true));
  }

  @Test
  public void testResolve() {
    // Unresolved return type resolves to UNKNOWN_TYPE
    arrowType.resolveInternal(errorReporter, scope);
    assertTrue(arrowType.returnType.isUnknownType());

    // Known return type resolves to itself
    arrowType2.resolveInternal(errorReporter, scope);
    assertTrue(arrowType2.returnType.equals(JSTypeNative.INT_TYPE));

    // Unknown parameters resolve to UNKNOWN_TYPE
    arrowType.parameters = null;
    arrowType.resolveInternal(errorReporter, scope);
    assertTrue(arrowType.parameters.getFirstChild().getJSType() == null);

    // Known parameters resolve to their corresponding types
    arrowTypeSameParams.parameters = null;
    JSType paramType = JSTypeNative.INT_TYPE;
    for (Node paramNode = arrowTypeSameParams.parameters.getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      paramNode.setJSType(paramType);
      paramType = paramType.getNext();
    }
    arrowTypeSameParams.resolveInternal(errorReporter, scope);
    for (Node paramNode = arrowTypeSameParams.parameters.getFirstChild();
         paramNode != null; paramNode = paramNode.getNext()) {
      assertTrue(paramNode.getJSType().equals(JSTypeNative.INT_TYPE));
    }
  }

  @Test
  public void testHashCode() {
    // Different arrow types have different hash codes
    assertFalse(arrowType.hashCode() == arrowType2.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeNullReturn.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeSameParams.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeDifferentParams.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeVarArgs.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeOptional.hashCode());
    assertFalse(arrowType.hashCode() == arrowTypeNoType.hashCode());

    // Arrow type with same parameters and return type has the same hash code
    arrowTypeDifferentParams.parameters = null;
    arrowTypeDifferentParams.returnType = JSTypeNative.INT_TYPE;
    arrowTypeSameParams.parameters = null;
    arrowTypeSameParams.returnType = JSTypeNative.INT_TYPE;
    assertTrue(arrowTypeDifferentParams.hashCode() == arrowTypeSameParams.hashCode());

    // Arrow type with same parameters but different return types have different hash codes
    arrowTypeDifferentParams.returnType = JSTypeNative.FLOAT_TYPE;
    assertFalse(arrowTypeDifferentParams.hashCode() == arrowTypeSameParams.hashCode());

    // Arrow type with different parameters but the same return type have different hash codes
    arrowTypeVarArgs.parameters = null;
    arrowTypeVarArgs.returnType = JSTypeNative.INT_TYPE;
    arrowTypeOptional.parameters = null;
    arrowTypeOptional.returnType = JSTypeNative.INT_TYPE;
    arrowTypeOptional.getFirstChild().setOptionalArg(true);
    assertFalse(arrowTypeVarArgs.hashCode() == arrowTypeOptional.hashCode());
  }
}