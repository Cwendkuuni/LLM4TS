package org.apache.commons.cli; // This is not related to the given code, it's just to keep the imports consistent with the first example.

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final ObjectType objectType = mock(ObjectType.class);
  private static final FunctionType functionType = mock(FunctionType.class);
  private static final FunctionType functionType2 = mock(FunctionType.class);
  private static final JSTypeNative arrayType = mock(JSTypeNative.class);
  private static final JSTypeNative noObjectType = mock(JSTypeNative.class);
  private static final JSTypeNative nullType = mock(JSTypeNative.class);

  private ClosureReverseAbstractInterpreter interpreter;

  @Mock
  private Node condition;

  @Mock
  private FlowScope blindScope;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);
    when(typeRegistry.getNativeType(NO_OBJECT_TYPE)).thenReturn(noObjectType);
    when(typeRegistry.getNativeType(NULL_TYPE)).thenReturn(nullType);

    when(condition.getType()).thenReturn(CALL);
    when(condition.getChildCount()).thenReturn(2);
    when(condition.getFirstChild().getType()).thenReturn(GETPROP);
    when(condition.getFirstChild().getLastChild().isQualifiedName()).thenReturn(true);

    interpreter = new ClosureReverseAbstractInterpreter(null, typeRegistry);
  }

  @Test
  public void testRestrictToArray() {
    when(condition.getLastChild().getString()).thenReturn("goog.isArray");

    Node left = mock(Node.class);
    Node right = mock(Node.class);
    when(left.getType()).thenReturn(NAME);
    when(left.getString()).thenReturn("goog");

    JSType type = mock(JSType.class);
    when(type.isSubtype(objectType)).thenReturn(true);

    JSType result = interpreter.restrictParameter(condition, type, blindScope,
        restrictToArrayVisitor, true);

    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToNotArray() {
    when(condition.getLastChild().getString()).thenReturn("goog.isArray");

    JSType type = mock(JSType.class);
    when(type.isSubtype(arrayType)).thenReturn(true);

    JSType result = interpreter.restrictParameter(condition, type, blindScope,
        restrictToNotArrayVisitor, false);

    assertNull(result);
  }

  @Test
  public void testRestrictToObject() {
    when(condition.getLastChild().getString()).thenReturn("goog.isObject");

    JSType type = mock(JSType.class);
    when(type.isSubtype(objectType)).thenReturn(true);

    JSType result = interpreter.restrictParameter(condition, type, blindScope,
        restrictToObjectVisitor, true);

    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToNotObject() {
    when(condition.getLastChild().getString()).thenReturn("goog.isObject");

    JSType type = mock(JSType.class);
    when(type.isSubtype(objectType)).thenReturn(true);

    JSType result = interpreter.restrictParameter(condition, type, blindScope,
        restrictToNotObjectVisitor, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    when(condition.getLastChild().getString()).thenReturn("goog.isDef");

    JSType type = mock(JSType.class);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(blindScope, informedScope);

    type = mock(JSType.class);
    when(type.isSubtype(noObjectType)).thenReturn(true);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(blindScope, informedScope);

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(blindScope, informedScope);

    informedScope = interpreter.restrictParameter(condition, type, blindScope,
        new Function<TypeRestriction, JSType>() {
          public JSType apply(TypeRestriction p) {
            return getNativeType(ARRAY_TYPE);
          }
        }, true);
    assertEquals(blindScope, informedScope);

    informedScope = interpreter.restrictParameter(condition, type, blindScope,
        new Function<TypeRestriction, JSType>() {
          public JSType apply(TypeRestriction p) {
            return getNativeType(OBJECT_TYPE);
          }
        }, false);
    assertNotNull(informedScope);
  }
}