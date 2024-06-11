package com.google.javascript.jscomp.type;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final CodingConvention convention = mock(CodingConvention.class);
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node condition = Node.newCall("goog.isDef", Node.newThis(), Node.newString("value"));
    JSType type = JSTypeNative.BOOLEAN_TYPE;

    when(typeRegistry.createBooleanType("value")).thenReturn(type);
    when(convention.isRefinable(condition)).thenReturn(true);

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    FlowScope expected = mock(FlowScope.class);
    declareNameInScope(expected, condition.getLastChild(), type);

    ArgumentCaptor<FlowScope> captor = ArgumentCaptor.forClass(FlowScope.class);
    interpreter.declareNameInScope(expected, condition.getLastChild(), type);
    interpreter.assertSameScopeForAllTests(expected);

    assertThat(actual).isSameInstanceAs(expected);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = Node.newCall("goog.isDef", Node.newThis(), Node.newString("value"));
    JSType type = JSTypeNative.VOID_TYPE;

    when(typeRegistry.createBooleanType("value")).thenReturn(null);
    when(convention.isRefinable(condition)).thenReturn(true);

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);

    assertThat(actual).isNull();
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node condition = Node.newCall("goog.isArray", Node.newThis(), Node.newString("value"));
    JSType type = JSTypeNative.BOOLEAN_TYPE;

    when(typeRegistry.createBooleanType("value")).thenReturn(type);

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);

    assertThat(actual).isNotNull();
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = Node.newCall("goog.isArray", Node.newThis(), Node.newString("value"));

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);

    assertThat(actual).isNull();
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcome_unknown_condition() {
    Node condition = Node.newCall("goog.unknownMethod", Node.newThis(), Node.newString("value"));

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  private void declareNameInScope(FlowScope scope, Node node, JSType type) {
    FunctionType functionType = mock(FunctionType.class);
    ObjectType objectType = mock(ObjectType.class);
    when(functionType.getDeclaredNames()).thenReturn(new String[]{"value"});
    when(functionType.getReturnType()).thenReturn(type);
    when(objectType.getDeclaredNames()).thenReturn(new String[]{"value"});
    when(objectType.getMembers()).thenReturn(new Object[0]);
    when(typeRegistry.createFunctionType(new String[]{"value"}, new String[]{}, type)).thenReturn(functionType);
    when(typeRegistry.createObjectType("ValueType", new String[]{"value"}, new Object[0])).thenReturn(objectType);

    if (type instanceof FunctionType) {
      scope.declareFunction("value", (Function<?>) interpreter);
    } else {
      scope.declareVariable("value", type);
    }
  }
}