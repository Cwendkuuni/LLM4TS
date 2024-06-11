package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, getNativeType(Number.class), false);
    ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), true);
    ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, null, false);
    ArrowType arrowType5 = new ArrowType(new JSTypeRegistry(), null, null, true);

    // Arrow type with known types should be a subtype of itself.
    assertTrue(arrowType1.isSubtype(arrowType1));

    // Arrow type with inferred return type should be a subtype of itself.
    assertTrue(arrowType3.isSubtype(arrowType3));

    // Arrow type with unknown return type should be a subtype of itself.
    assertTrue(arrowType5.isSubtype(arrowType5));

    // Arrow type with known types should not be a subtype of another arrow type with different return types.
    assertFalse(arrowType1.isSubtype(arrowType2));

    // Arrow type with known types should not be a subtype of another arrow type with different parameter types.
    assertFalse(arrowType1.isSubtype(new ArrowType(new JSTypeRegistry(), null, getNativeType(String.class), false)));

    // Arrow type with inferred return type should be a subtype of another arrow type with the same inferred return type.
    assertTrue(arrowType3.isSubtype(arrowType3));

    // Arrow type with inferred return type should be a subtype of another arrow type with a known return type that is a supertype of the inferred return type.
    assertTrue(arrowType3.isSubtype(new ArrowType(new JSTypeRegistry(), null, getNativeType(Number.class), false)));

    // Arrow type with unknown return type should be a subtype of another arrow type with an unknown return type.
    assertTrue(arrowType5.isSubtype(arrowType5));

    // Arrow type with unknown return type should be a subtype of another arrow type with a known return type that is a supertype of UNKNOWN_TYPE.
    assertTrue(arrowType5.isSubtype(getNativeType(Object.class)));
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);
    ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), true);
    ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, getNativeType(String.class), false);
    ArrowType arrowType5 = new ArrowType(new JSTypeRegistry(), null, null, false);

    // Arrow types with the same known parameter types and return type should have equal parameters.
    assertTrue(arrowType1.hasEqualParameters(arrowType1, false));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));

    // Arrow types with different return types should not have equal parameters.
    assertFalse(arrowType1.hasEqualParameters(new ArrowType(new JSTypeRegistry(), null, getNativeType(String.class), false), false));

    // Arrow types with optional or varargs parameters should be considered equal even if the order of the parameters is different.
    ArrowType arrowType6 = new ArrowType(new JSTypeRegistry(), null, null, true);
    ArrowType arrowType7 = new ArrowType(new JSTypeRegistry(), null, null, true);
    arrowType6.parameters.setNext(getVariableArgNode(getNativeType(String[].class)));
    arrowType7.parameters.setNext(getVariableArgNode(getNativeType(int[].class)));
    assertTrue(arrowType5.hasEqualParameters(arrowType6, false));
    assertTrue(arrowType5.hasEqualParameters(arrowType7, false));

    // Arrow types with unknown return types should be considered equal if they have the same number and type of parameters.
    assertTrue(arrowType5.hasEqualParameters(arrowType5, false));
    assertTrue(arrowType5.hasEqualParameters(new ArrowType(new JSTypeRegistry(), null, null, true), false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);
    ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, getNativeType(Number.class), false);

    // Arrow types with the same known parameter types and return type should be equivalent.
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType1, false));
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));

    // Arrow types with different return types should not be equivalent.
    assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType3, false));
  }

  private Node getVariableArgNode(JSType type) {
    Node variableArgNode = new Node_FunctionCall();
    variableArgNode.setType(new JSTypeFunction(null, null, null, type, null, null, null, null));
    variableArgNode.setIsVarArgs(true);
    return variableArgNode;
  }
}