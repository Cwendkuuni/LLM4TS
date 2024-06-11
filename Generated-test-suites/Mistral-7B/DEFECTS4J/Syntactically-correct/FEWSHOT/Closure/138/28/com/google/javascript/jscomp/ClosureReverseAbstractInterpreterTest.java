package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testRestrictIsDef() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isDef").apply(restriction);
        assertEquals(getNativeType(JSTypeNative.UNDEFINED_TYPE), restrictedType);

        restriction = new TypeRestriction(JSTypeNative.UNDEFINED_TYPE, false);
        restrictedType = interpreter.restricters.get("isDef").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.NUMBER_TYPE, true);
        restrictedType = interpreter.restricters.get("isDef").apply(restriction);
        assertEquals(JSTypeNative.NUMBER_TYPE, restrictedType);
    }

    @Test
    public void testRestrictIsNull() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isNull").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.NULL_TYPE, false);
        restrictedType = interpreter.restricters.get("isNull").apply(restriction);
        assertEquals(JSTypeNative.NULL_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, true);
        restrictedType = interpreter.restricters.get("isNull").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictIsDefAndNotNull() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, true);
        restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertEquals(JSTypeNative.STRING_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.NULL_TYPE, false);
        restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictIsString() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isString").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, false);
        restrictedType = interpreter.restricters.get("isString").apply(restriction);
        assertEquals(JSTypeNative.STRING_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.NUMBER_TYPE, true);
        restrictedType = interpreter.restricters.get("isString").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictIsBoolean() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isBoolean").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.BOOLEAN_TYPE, false);
        restrictedType = interpreter.restricters.get("isBoolean").apply(restriction);
        assertEquals(JSTypeNative.BOOLEAN_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, true);
        restrictedType = interpreter.restricters.get("isBoolean").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictIsNumber() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isNumber").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.NUMBER_TYPE, false);
        restrictedType = interpreter.restricters.get("isNumber").apply(restriction);
        assertEquals(JSTypeNative.NUMBER_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, true);
        restrictedType = interpreter.restricters.get("isNumber").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictIsFunction() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = JSTypeNative.ANY_TYPE;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isFunction").apply(restriction);
        assertNull(restrictedType);

        restriction = new TypeRestriction(JSTypeNative.FUNCTION_TYPE, false);
        restrictedType = interpreter.restricters.get("isFunction").apply(restriction);
        assertEquals(JSTypeNative.FUNCTION_TYPE, restrictedType);

        restriction = new TypeRestriction(JSTypeNative.STRING_TYPE, true);
        restrictedType = interpreter.restricters.get("isFunction").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictArray() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = null;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isArray").apply(restriction);
        assertEquals(JSTypeNative.ARRAY_TYPE, restrictedType);

        type = JSTypeNative.NUMBER_TYPE;
        restriction = new TypeRestriction(type, false);
        restrictedType = interpreter.restricters.get("isArray").apply(restriction);
        assertNull(restrictedType);

        type = JSTypeNative.ARRAY_TYPE;
        restriction = new TypeRestriction(type, true);
        restrictedType = interpreter.restricters.get("isArray").apply(restriction);
        assertEquals(JSTypeNative.ARRAY_TYPE, restrictedType);

        type = JSTypeNative.OBJECT_TYPE;
        restriction = new TypeRestriction(type, true);
        restrictedType = interpreter.restricters.get("isArray").apply(restriction);
        assertNull(restrictedType);
    }

    @Test
    public void testRestrictObject() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType type = null;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType restrictedType = interpreter.restricters.get("isObject").apply(restriction);
        assertEquals(JSTypeNative.OBJECT_TYPE, restrictedType);

        type = JSTypeNative.FUNCTION_TYPE;
        restriction = new TypeRestriction(type, false);
        restrictedType = interpreter.restricters.get("isObject").apply(restriction);
        assertNull(restrictedType);

        type = JSTypeNative.OBJECT_TYPE;
        restriction = new TypeRestriction(type, true);
        restrictedType = interpreter.restricters.get("isObject").apply(restriction);
        assertEquals(JSTypeNative.OBJECT_TYPE, restrictedType);

        type = JSTypeNative.ARRAY_TYPE;
        restriction = new TypeRestriction(type, true);
        restrictedType = interpreter.restricters.get("isObject").apply(restriction);
        assertNull(restrictedType);
    }

    private JSType getNativeType(JSTypeNative type) {
        return typeRegistry.getNativeType(type);
    }

    private JSType getRestrictedWithoutUndefined(JSType type) {
        return type.isSubtype(JSTypeNative.UNDEFINED_TYPE) ? getNativeType(JSTypeNative.VOID_TYPE) : type;
    }

    private JSType getRestrictedWithoutNull(JSType type) {
        return type.isSubtype(JSTypeNative.NULL_TYPE) ? getNativeType(JSTypeNative.VOID_TYPE) : type;
    }

    private JSType getRestrictedByTypeOfResult(JSType type, String expectedType, boolean outcome) {
        if (type == null) {
            return null;
        }

        ObjectType objectType = (ObjectType) type;
        JSType resultType = objectType.getMemberType("__esModule");
        if (resultType != null && resultType.getName().equals(expectedType)) {
            return type;
        }

        JSType restrictedType = nextRestrictedType(type, outcome);
        if (restrictedType != null) {
            return restrictedType;
        }

        return null;
    }

    private JSType nextRestrictedType(JSType type, boolean outcome) {
        for (Function<TypeRestriction, JSType> restricter : interpreter.restricters.values()) {
            TypeRestriction restriction = new TypeRestriction(type, outcome);
            JSType restrictedType = restricter.apply(restriction);
            if (restrictedType != null) {
                return restrictedType;
            }
        }

        return null;
    }
}