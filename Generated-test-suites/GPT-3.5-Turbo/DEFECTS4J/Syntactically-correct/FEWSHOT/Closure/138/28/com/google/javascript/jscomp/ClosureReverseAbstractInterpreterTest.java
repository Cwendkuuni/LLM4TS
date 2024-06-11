package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testRestrictToArrayVisitor_caseTopType() {
        ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor();
        JSType topType = new JSType();
        JSType result = visitor.caseTopType(topType);
        assertEquals(topType, result);
    }

    @Test
    public void testRestrictToArrayVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor();
        ObjectType type = new ObjectType();
        JSType arrayType = new JSType();
        JSTypeRegistry.addNativeType(arrayType, ClosureReverseAbstractInterpreter.ARRAY_TYPE);
        JSType result = visitor.caseObjectType(type);
        assertEquals(type, result);
        type.addSubtype(arrayType);
        result = visitor.caseObjectType(type);
        assertEquals(arrayType, result);
        type.addSubtype(new JSType());
        result = visitor.caseObjectType(type);
        assertNull(result);
    }

    @Test
    public void testRestrictToNotArrayVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor();
        ObjectType type = new ObjectType();
        JSType arrayType = new JSType();
        JSTypeRegistry.addNativeType(arrayType, ClosureReverseAbstractInterpreter.ARRAY_TYPE);
        JSType result = visitor.caseObjectType(type);
        assertEquals(type, result);
        type.addSubtype(arrayType);
        result = visitor.caseObjectType(type);
        assertNull(result);
        type.addSubtype(new JSType());
        result = visitor.caseObjectType(type);
        assertEquals(type, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseTopType() {
        ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor();
        JSType topType = new JSType();
        JSType result = visitor.caseTopType(topType);
        assertEquals(new JSTypeRegistry().getNativeType(NO_OBJECT_TYPE), result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor();
        ObjectType type = new ObjectType();
        JSType result = visitor.caseObjectType(type);
        assertEquals(type, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseFunctionType() {
        ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor();
        FunctionType type = new FunctionType();
        JSType result = visitor.caseFunctionType(type);
        assertEquals(type, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseObjectType() {
        ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor();
        ObjectType type = new ObjectType();
        JSType result = visitor.caseObjectType(type);
        assertNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseFunctionType() {
        ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor visitor = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor();
        FunctionType type = new FunctionType();
        JSType result = visitor.caseFunctionType(type);
        assertNull(result);
    }

    @Test
    public void testConstructor() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        assertNotNull(interpreter.restricters);
        assertEquals(8, interpreter.restricters.size());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertEquals(blindScope, result);
    }

    @Test
    public void testRestrictParameter() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType> restriction = new Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType>() {
            public JSType apply(ClosureReverseAbstractInterpreter.TypeRestriction p) {
                if (p.outcome) {
                    return new JSType();
                } else {
                    return null;
                }
            }
        };
        boolean outcome = true;

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result);
        assertTrue(result != blindScope && result.parent == blindScope);
        assertNotNull(result.getDeclaredType(parameter));
    }

    @Test
    public void testTypeRestriction() {
        JSType type = new JSType();
        boolean outcome = true;
        ClosureReverseAbstractInterpreter.TypeRestriction restriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
        assertEquals(type, restriction.type);
        assertEquals(outcome, restriction.outcome);
    }
}