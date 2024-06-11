package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReverseAbstractInterpreter;
import com.google.javascript.jscomp.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private static final CodingConvention CONVENTION = new CodingConvention() {};
    private static final JSTypeRegistry TYPE_REGISTRY = mock(JSTypeRegistry.class);
    private final ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    private static final Map<String, Function<TypeRestriction, JSType>> RESTRICTERS =
        ImmutableMap.of(
            "isDef", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                if (restriction.outcome) {
                  return interpreter.getRestrictedWithoutUndefined(restriction.type);
                } else {
                  return null;
                }
              }
            },
            "isNull", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                if (restriction.outcome) {
                  return interpreter.getNativeType(JSTypeRegistry.NULL_TYPE);
                } else {
                  return interpreter.getRestrictedWithoutNull(restriction.type);
                }
              }
            },
            "isDefAndNotNull", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                if (restriction.outcome) {
                  return interpreter.getRestrictedWithoutUndefined(
                      interpreter.getRestrictedWithoutNull(restriction.type));
                } else {
                  return null;
                }
              }
            },
            "isString", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                return interpreter.getRestrictedByTypeOfResult(restriction.type, "string", restriction.outcome);
              }
            },
            "isBoolean", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                return interpreter.getRestrictedByTypeOfResult(restriction.type, "boolean", restriction.outcome);
              }
            },
            "isNumber", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                return interpreter.getRestrictedByTypeOfResult(restriction.type, "number", restriction.outcome);
              }
            },
            "isFunction", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                return interpreter.getRestrictedByTypeOfResult(restriction.type, "function", restriction.outcome);
              }
            },
            "isArray", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                if (restriction.type == null) {
                  return restriction.outcome ? interpreter.getNativeType(JSTypeRegistry.ARRAY_TYPE) : null;
                }

                Visitor<JSType> visitor = restriction.outcome ? interpreter.restrictToArrayVisitor :
                    interpreter.restrictToNotArrayVisitor;
                return restriction.type.visit(visitor);
              }
            },
            "isObject", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction restriction) {
                if (restriction.type == null) {
                  return restriction.outcome ? interpreter.getNativeType(JSTypeRegistry.NO_OBJECT_TYPE) : null;
                }

                Visitor<JSType> visitor = restriction.outcome ? interpreter.restrictToObjectVisitor :
                    interpreter.restrictToNotObjectVisitor;
                return restriction.type.visit(visitor);
              }
            }
        );

    @org.junit.Test
    public void testRestrictingTypes() {
        JSType type = JSTypeRegistry.OBJECT_TYPE;
        when(TYPE_REGISTRY.getNativeType(JSTypeRegistry.ARRAY_TYPE)).thenReturn(JSTypeRegistry.ARRAY_TYPE);
        when(TYPE_REGISTRY.getNativeType(JSTypeRegistry.NO_OBJECT_TYPE)).thenReturn(JSTypeRegistry.NO_OBJECT_TYPE);
        when(TYPE_REGISTRY.getNativeType(JSTypeRegistry.NULL_TYPE)).thenReturn(JSTypeRegistry.NULL_TYPE);
        when(TYPE_REGISTRY.getNativeType(JSTypeRegistry.OBJECT_TYPE)).thenReturn(type);

        Function<TypeRestriction, JSType> isDef = RESTRICTERS.get("isDef");
        Function<TypeRestriction, JSType> isNull = RESTRICTERS.get("isNull");
        Function<TypeRestriction, JSType> isDefAndNotNull = RESTRICTERS.get("isDefAndNotNull");

        assertSame(JSTypeRegistry.OBJECT_TYPE, isDef.apply(new TypeRestriction(type, true)).getType());
        assertNull(isDef.apply(new TypeRestriction(type, false)).getType());

        assertSame(JSTypeRegistry.NULL_TYPE, isNull.apply(new TypeRestriction(null, true)).getType());
        assertSame(JSTypeRegistry.OBJECT_TYPE, isNull.apply(new TypeRestriction(type, false)).getType());

        assertSame(JSTypeRegistry.OBJECT_TYPE, isDefAndNotNull.apply(new TypeRestriction(type, true)).getType());
        assertNull(isDefAndNotNull.apply(new TypeRestriction(type, false)).getType());

        JSType arrayType = JSTypeRegistry.ARRAY_TYPE;
        JSType stringType = JSTypeRegistry.STRING;
        JSType booleanType = JSTypeRegistry.BOOLEAN;
        JSType numberType = JSTypeRegistry.NUMBER;

        assertSame(arrayType, isArray.apply(new TypeRestriction(null, true)).getType());
        assertNull(isArray.apply(new TypeRestriction(null, false)).getType());
        assertSame(JSTypeRegistry.NO_OBJECT_TYPE, isArray.apply(new TypeRestriction(type, false)).getType());
        assertSame(JSTypeRegistry.OBJECT_TYPE, isArray.apply(new TypeRestriction(null, true)).getType());

        assertSame(JSTypeRegistry.NO_OBJECT_TYPE, isObject.apply(new TypeRestriction(null, true)).getType());
        assertNull(isObject.apply(new TypeRestriction(null, false)).getType());
        assertSame(JSTypeRegistry.OBJECT_TYPE, isObject.apply(new TypeRestriction(type, false)).getType());
        assertSame(type, isObject.apply(new TypeRestriction(null, true)).getType());

        assertSame(stringType, isString.apply(new TypeRestriction(null, true)).getType());
        assertNull(isString.apply(new TypeRestriction(null, false)).getType());

        assertSame(booleanType, isBoolean.apply(new TypeRestriction(null, true)).getType());
        assertNull(isBoolean.apply(new TypeRestriction(null, false)).getType());

        assertSame(numberType, isNumber.apply(new TypeRestriction(null, true)).getType());
        assertNull(isNumber.apply(new TypeRestriction(null, false)).getType());

        assertSame(JSTypeRegistry.FUNCTION_TYPE, isFunction.apply(new TypeRestriction(null, true)).getType());
        assertNull(isFunction.apply(new TypeRestriction(null, false)).getType());
    }
}

class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
        this.type = type;
        this.outcome = outcome;
    }
}