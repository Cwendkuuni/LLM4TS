package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.flow.FlowScope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private JSTypeRegistry typeRegistry;
    private CodingConvention convention;
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typeRegistry = mock(JSTypeRegistry.class);
        convention = mock(CodingConvention.class);
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testIsDef() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.BOOLEAN_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedWithoutUndefined(type);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(result);
    }

    @Test
    public void testIsNull() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.NULL_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.NULL_TYPE));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedWithoutNull(type);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedWithoutNull(type);
        assertNull(result);
    }

    @Test
    public void testIsDefAndNotNull() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.NULL_VOID);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.NULL_VOID));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(result);

        restriction.outcome = true;
        expected = type;
        result = interpreter.getRestrictedWithoutNull(expected);
        assertSame(expected, result);

        TypeRestriction restriction2 = new TypeRestriction(expected, false);
        result = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(result);
    }

    @Test
    public void testIsString() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.STRING_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.STRING_TYPE));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedByTypeOfResult(type, "string",
                true);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "string", false);
        assertNull(result);
    }

    @Test
    public void testIsBoolean() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.BOOLEAN_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.BOOLEAN_TYPE));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedByTypeOfResult(type, "boolean",
                true);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "boolean", false);
        assertNull(result);
    }

    @Test
    public void testIsNumber() {
        JSType type = mock(JSType.class);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.NUMBER_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.NUMBER_TYPE));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedByTypeOfResult(type, "number",
                true);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "number", false);
        assertNull(result);
    }

    @Test
    public void testIsFunction() {
        FunctionType functionType = mock(FunctionType.class);
        JSType expected = functionType;
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(functionType);
        when(type.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .then(arg -> getNativeType(JSTypeNative.FUNCTION_TYPE));
        when(type.visit(restrictToArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToNotArrayVisitor)).thenReturn(null);
        when(type.visit(restrictToObjectVisitor)).thenReturn(null);
        when(type.visit(restrictToNotObjectVisitor)).thenReturn(null);

        JSType result = interpreter.getRestrictedByTypeOfResult(type, "function",
                true);
        assertSame(expected, result);

        TypeRestriction restriction = new TypeRestriction(type, false);
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "function", false);
        assertNull(result);
    }

    @Test
    public void testIsArray() {
        JSType type = null;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.ARRAY_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);

        JSType result = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(result);

        result = interpreter.getRestrictedByTypeOfResult(type, "array", true);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "array", false);
        assertNull(result);
    }

    @Test
    public void testIsObject() {
        JSType type = null;
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType expected = getNativeType(JSTypeNative.OBJECT_TYPE);
        when(typeRegistry.createUnionType(ArgumentMatchers.any(Type.class),
                ArgumentMatchers.any(Type.class))).thenReturn(type);

        JSType result = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(result);

        result = interpreter.getRestrictedByTypeOfResult(type, "object", true);
        assertSame(expected, result);

        restriction.outcome = false;
        expected = null;
        result = interpreter.getRestrictedByTypeOfResult(type, "object", false);
        assertNull(result);
    }

    private JSType getNativeType(int typeCode) {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        JSTypeNative jstypeNative = mock(JSTypeNative.class);
        when(typeRegistry.getNativeType(typeCode)).thenReturn(jstypeNative);
        when(jstypeNative.getGreatestSubtype(ArgumentMatchers.any(JSType.class)))
                .thenReturn(jstypeNative);
        return typeRegistry.getNativeType(typeCode);
    }
}