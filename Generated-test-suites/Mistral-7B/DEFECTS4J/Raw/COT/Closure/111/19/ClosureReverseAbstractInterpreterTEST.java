package com.google.javascript.jscomp.type.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final CodingConvention convention = mock(CodingConvention.class);
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    // Given
    Node condition = createNode("goog.isDef(x, y)");
    Node x = new Node("x");
    Node y = new Node("y");

    JSType xType = createType("number");
    JSType yType = createType("boolean");
    when(typeRegistry.getType(x)).thenReturn(xType);
    when(typeRegistry.getType(y)).thenReturn(yType);

    // When
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true);

    // Then
    JSType expectedResult = interpreter.getNativeType(NUMBER_STRING_BOOLEAN)
        .getSubtype(xType);
    assertThat(flowScope.getVariable(x.toString()).getType()).isEqualTo(expectedResult);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    // Given
    Node condition = createNode("goog.isDef(x, y)");
    Node x = new Node("x");
    Node y = new Node("y");

    JSType xType = null;
    JSType yType = createType("boolean");
    when(typeRegistry.getType(x)).thenReturn(xType);
    when(typeRegistry.getType(y)).thenReturn(yType);

    // When
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), false);

    // Then
    JSType expectedResult = interpreter.getNativeType(NULL_VOID);
    assertThat(flowScope.getVariable(x.toString()).getType()).isNull();
  }

  // Add test cases for the remaining methods: isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, and isObject.

  private Node createNode(String nodeString) {
    return new Node(nodeString);
  }

  private JSType createType(String jstype) {
    if (jstype.equals("number")) {
      return interpreter.getNativeType(NUMBER_STRING_BOOLEAN);
    } else if (jstype.equals("boolean")) {
      return interpreter.getNativeType(NUMBER_STRING_BOOLEAN);
    } else if (jstype.equals("string")) {
      return interpreter.getNativeType(STRING_TYPE);
    } else if (jstype.equals("function")) {
      FunctionType functionType = mock(FunctionType.class);
      when(functionType.getReturnType()).thenReturn(interpreter.getNativeType(VOID_TYPE));
      ObjectType objectType = mock(ObjectType.class);
      when(objectType.isSubtype(functionType)).thenReturn(true);
      when(typeRegistry.getType(functionType)).thenReturn(objectType);
      return functionType;
    } else {
      return createArrayType(jstype);
    }
  }

  private JSType createArrayType(String jstype) {
    return interpreter.getNativeType(ARRAY_TYPE).makeArrayType(
        createType(jstype));
  }
}