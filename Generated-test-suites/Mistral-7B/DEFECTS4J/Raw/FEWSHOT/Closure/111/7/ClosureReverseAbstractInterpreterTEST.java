package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;
  private Function<Type, JSType> restrictToArrayVisitorMock;
  private Function<Type, JSType> restrictToNotArrayVisitorMock;
  private Function<Type, JSType> restrictToObjectVisitorMock;
  private Function<Type, JSType> restrictToNotObjectVisitorMock;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);

    restrictToArrayVisitorMock = mock(Function.class);
    restrictToNotArrayVisitorMock = mock(Function.class);
    restrictToObjectVisitorMock = mock(Function.class);
    restrictToNotObjectVisitorMock = mock(Function.class);

    when(typeRegistry.createUnionType(ArgumentMatchers.any(JSType.class),
        ArgumentMatchers.any(JSType.class))).thenReturn(mock(JSType.class));

    interpreter = new ClosureReverseAbstractInterpreter(codingConvention,
        typeRegistry);

    interpreter.restricters = new ImmutableMap.Builder<String, Function<TypeRestriction, JSType>>()
        .put("isDef", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            if (p.outcome) {
              return interpreter.getRestrictedWithoutUndefined(p.type);
            } else {
              return p.type != null ?
                  interpreter.getNativeType(JSTypeNative.VOID_TYPE).getGreatestSubtype(p.type) : null;
            }
          }
        })
        .put("isNull", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            if (p.outcome) {
              return p.type != null ?
                  interpreter.getNativeType(JSTypeNative.NULL_TYPE).getGreatestSubtype(p.type) : null;
            } else {
              return interpreter.getRestrictedWithoutNull(p.type);
            }
          }
        })
        .put("isDefAndNotNull", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            if (p.outcome) {
              return interpreter.getRestrictedWithoutUndefined(
                  interpreter.getRestrictedWithoutNull(p.type));
            } else {
              return p.type != null ?
                  interpreter.getNativeType(JSTypeNative.NULL_VOID).getGreatestSubtype(p.type) : null;
            }
          }
        })
        .put("isString", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return interpreter.getRestrictedByTypeOfResult(p.type, "string", p.outcome);
          }
        })
        .put("isBoolean", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return interpreter.getRestrictedByTypeOfResult(p.type, "boolean", p.outcome);
          }
        })
        .put("isNumber", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return interpreter.getRestrictedByTypeOfResult(p.type, "number", p.outcome);
          }
        })
        .put("isFunction", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return interpreter.getRestrictedByTypeOfResult(p.type, "function", p.outcome);
          }
        })
        .put("isArray", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            if (p.type == null) {
              return p.outcome ? interpreter.getNativeType(JSTypeNative.ARRAY_TYPE) : null;
            }

            Visitor<JSType> visitor = p.outcome ? interpreter.restrictToArrayVisitor :
                interpreter.restrictToNotArrayVisitor;
            return p.type.visit(visitor);
          }
        })
        .put("isObject", new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            if (p.type == null) {
              return p.outcome ? interpreter.getNativeType(JSTypeNative.OBJECT_TYPE) : null;
            }

            Visitor<JSType> visitor = p.outcome ? interpreter.restrictToObjectVisitor :
                interpreter.restrictToNotObjectVisitor;
            return p.type.visit(visitor);
          }
        })
        .build();

    when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(
        mock(JSType.class));
    when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        mock(JSType.class));
    when(typeRegistry.getNativeType(JSTypeNative.NULL_TYPE)).thenReturn(
        mock(JSType.class));
    when(typeRegistry.getNativeType(JSTypeNative.NULL_VOID)).thenReturn(
        mock(JSType.class));
    when(typeRegistry.getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN)).thenReturn(
        mock(JSType.class));
    when(typeRegistry.getNativeType(JSTypeNative.VOID_TYPE)).thenReturn(
        mock(JSType.class));

    when(restrictToArrayVisitorMock.apply(ArgumentMatchers.any(Type.class)))
        .thenCallRealMethod();
    when(restrictToNotArrayVisitorMock.apply(ArgumentMatchers.any(Type.class)))
        .thenCallRealMethod();
    when(restrictToObjectVisitorMock.apply(ArgumentMatchers.any(Type.class)))
        .thenCallRealMethod();
    when(restrictToNotObjectVisitorMock.apply(ArgumentMatchers.any(Type.class)))
        .thenCallRealMethod();

    when(interpreter.getRestrictedWithoutUndefined(ArgumentMatchers.any(JSType.class)))
        .thenCallRealMethod();
    when(interpreter.getRestrictedWithoutNull(ArgumentMatchers.any(JSType.class)))
        .thenCallRealMethod();
    when(interpreter.getRestrictedByTypeOfResult(ArgumentMatchers.any(JSType.class),
        ArgumentMatchers.eq("string"), ArgumentMatchers.anyBoolean()))
        .thenCallRealMethod();
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Test with a valid condition
    Node condition = Node.newCall(null, "goog.isDef", new Node[] {
      Node.newName("myVariable"), Node.newString("myString")
    });

    Type type = mock(Type.class);
    when(type.isRefinable()).thenReturn(true);
    when(type.getType()).thenReturn(mock(JSType.class));

    JSType expectedType = mock(JSType.class);
    when(restrictToArrayVisitorMock.apply(type)).thenReturn(expectedType);

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);

    assertSame(expectedType, scope.getVariable("myVariable").getType());

    // Test with a null condition
    condition = null;

    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);

    assertNull(scope);

    // Test with a false condition
    when(type.isRefinable()).thenReturn(true);
    when(type.getType()).thenReturn(mock(JSType.class));

    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);

    assertNull(scope);

    // Test with a null type
    when(type.isRefinable()).thenReturn(false);
    when(type.getType()).thenReturn(null);

    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);

    assertNull(scope);

    // Test with a non-valid condition
    condition = Node.newCall(null, "goog.isDef", new Node[] {
      Node.newName("myVariable"), Node.newString("invalidString")
    });

    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);

    assertNull(scope);
  }
}