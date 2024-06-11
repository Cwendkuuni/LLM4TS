package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final Function<JSType, JSType> GET_NATIVE_TYPE =
      type -> JSTypeNative.getNativeType(type);

  private static final Function<JSType, JSType> GET_RESTRICTED_WITHOUT_NULL =
      type -> type != null && type.isInstanceOf(JSTypeNative.NULL_TYPE) ?
          type.getSubtypes()[0] : type;

  private static final Function<JSType, JSType> GET_RESTRICTED_WITHOUT_UNDEFINED =
      type -> type != null && type.isInstanceOf(JSTypeNative.UNDEFINED_TYPE) ?
          type.getSubtypes()[0] : type;

  private static final Function<JSType, JSType> GET_RESTRICTED_BY_TYPE_OF_RESULT =
      (type, expectedType, outcome) -> {
        if (!type.isInstanceOf(GET_NATIVE_TYPE.apply(JSTypeNative.OBJECT_TYPE))) {
          throw new IllegalArgumentException("Expected ObjectType but got " + type);
        }

        ObjectType objectType = (ObjectType) type;
        JSType subtype = null;
        for (JSType subtypeType : objectType.getSubtypes()) {
          if (subtypeType.getName().equals(expectedType)) {
            subtype = subtypeType;
            break;
          }
        }

        if (subtype == null) {
          throw new IllegalArgumentException(
              "Could not find expected type '" + expectedType + "' among " +
                  "subtypes of ObjectType.");
        }

        return outcome ? subtype : null;
      };

  @Test
  public void testRestrictToArray() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
    when(node.getType()).thenReturn(null);
    when(typeRegistry.getArrayType(NodeOperations.getQualifiedName(node))).thenReturn(arrayType);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(arrayType, result);
  }

  @Test
  public void testRestrictToNotArray() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE));
    when(typeRegistry.getArrayType(NodeOperations.getQualifiedName(node))).thenReturn(null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }

  @Test
  public void testRestrictToObject() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    ObjectType objectType = mock(ObjectType.class);
    when(node.getType()).thenReturn(null);
    when(typeRegistry.getObjectType(NodeOperations.getQualifiedName(node))).thenReturn(objectType);

    when(objectType.getName()).thenReturn("String");
    when(objectType.isSubtype(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE))).thenReturn(false);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testRestrictToNotObject() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    ObjectType objectType = mock(ObjectType.class);
    when(node.getType()).thenReturn(null);
    when(typeRegistry.getObjectType(NodeOperations.getQualifiedName(node))).thenReturn(objectType);

    when(objectType.getName()).thenReturn("String");
    when(objectType.isSubtype(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE))).thenReturn(true);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }

  @Test
  public void testRestrictToBoolean() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(null);
    when(typeRegistry.getBooleanType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.BOOLEAN_TYPE));

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(JSTypeNative.getNativeType(JSTypeNative.BOOLEAN_TYPE), result);
  }

  @Test
  public void testRestrictToFalseBoolean() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.BOOLEAN_TYPE));
    when(typeRegistry.getBooleanType()).thenReturn(null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }

  @Test
  public void testRestrictToNumber() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(null);
    when(typeRegistry.getNumberType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE));

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE), result);
  }

  @Test
  public void testRestrictToFalseNumber() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.NUMBER_TYPE));
    when(typeRegistry.getNumberType()).thenReturn(null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }

  @Test
  public void testRestrictToFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    FunctionType functionType = mock(FunctionType.class);
    when(node.getType()).thenReturn(null);
    when(typeRegistry.getFunctionType(NodeOperations.getQualifiedName(node))).thenReturn(functionType);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(functionType, result);
  }

  @Test
  public void testRestrictToFalseFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    FunctionType functionType = mock(FunctionType.class);
    when(node.getType()).thenReturn(functionType);
    when(typeRegistry.getFunctionType(NodeOperations.getQualifiedName(node))).thenReturn(null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }

  @Test
  public void testRestrictToString() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(null);
    when(typeRegistry.getStringType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.STRING_TYPE));

    String expectedType = "String";
    when(typeRegistry.getObjectType(NodeOperations.getQualifiedName(node))).thenReturn(
        new ObjectType(ImmutableMap.of("toString", new FunctionType(
            new FunctionType(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE),
                JSTypeNative.getNativeType(JSTypeNative.STRING_TYPE),
                null)))));

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, true).getScope().getVariable("p").getType();
    assertSame(JSTypeNative.getNativeType(JSTypeNative.STRING_TYPE), result);
  }

  @Test
  public void testRestrictToFalseString() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention codingConvention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(JSTypeNative.getNativeType(JSTypeNative.STRING_TYPE));
    when(typeRegistry.getStringType()).thenReturn(null);

    String expectedType = "String";
    when(typeRegistry.getObjectType(NodeOperations.getQualifiedName(node))).thenReturn(
        new ObjectType(ImmutableMap.of("toString", new FunctionType(
            JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE),
            JSTypeNative.getNativeType(JSTypeNative.STRING_TYPE),
            null))));

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, null, false).getScope().getVariable("p").getType();
    assertNull(result);
  }
}