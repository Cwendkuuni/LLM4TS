package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Type;
import com.google.javascript.rhino.jstype.Visitor.TypeRestriction;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ClosureReverseAbstractInterpreterTest {
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter ra;

  @Before
  public void setup() {
    JSType getNativeType = mock(JSTypeNative.class);
    when(getNativeType.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN,
        JSTypeNative.NULL_VOID)).thenReturn(mock(JSType.class));

    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    ra = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node condition = Node.newCall(null, "goog.isDef", Node.newThis(),
        Node.newString("foo"));
    Type paramType = mock(Type.class);
    FlowScope blindScope = mock(FlowScope.class);
    JSType expected = mock(JSType.class);

    when(getTypeIfRefinable(condition.getLastChild(), blindScope)).thenReturn(paramType);

    ArgumentCaptor<TypeRestriction> typeRestrictionCaptor = ArgumentCaptor.forClass(TypeRestriction.class);
    ra.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ra.restricters.get("isDef").apply(new TypeRestriction(paramType, true));

    FlowScope actual = capture(ra.restricterCalls).getValue();
    assertNotNull(actual);
    assertEquals(blindScope, actual.getParent());
    assertEquals(expected, actual.getTypes().get(0));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = Node.newCall(null, "goog.isDef", Node.newThis(),
        Node.newString("foo"));
    Type paramType = mock(Type.class);
    FlowScope blindScope = mock(FlowScope.class);

    when(getTypeIfRefinable(condition.getLastChild(), blindScope)).thenReturn(paramType);

    ra.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(capture(ra.restricterCalls).getValue());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_otherFunction() {
    Node condition = Node.newCall(null, "goog.isDef", Node.newThis(), null);
    Type paramType = mock(Type.class);
    FlowScope blindScope = mock(FlowScope.class);

    when(getTypeIfRefinable(condition.getLastChild(), blindScope)).thenReturn(paramType);

    ra.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNull(capture(ra.restricterCalls).getValue());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_notCall() {
    Node condition = Node.newAssignment(Node.newName("x"), Node.newNumber(42));
    FlowScope blindScope = mock(FlowScope.class);

    ra.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNull(capture(ra.restricterCalls).getValue());
  }

  private ArgumentCaptor<Visitor<JSType>> raRestricterCalls = ArgumentCaptor.forClass(Visitor.class);
  private ArgumentCaptor<TypeRestriction> typeRestrictionCaptor = ArgumentCaptor.forClass(TypeRestriction.class);

  private FlowScope capture(ArgumentCaptor<Visitor<JSType>> captor) {
    return (FlowScope) captor.getValue().getVisitor().accept(null);
  }
}