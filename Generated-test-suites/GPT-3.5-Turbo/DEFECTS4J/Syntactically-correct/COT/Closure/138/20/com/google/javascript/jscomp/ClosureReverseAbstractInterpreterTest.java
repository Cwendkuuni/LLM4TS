
package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    // TODO: Add other test cases

    @Test
    public void testRestrictToArrayVisitor_caseTopType_returnsTopType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType topType = new JSType();
        JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);

        assertEquals(topType, result);
    }

    @Test
    public void testRestrictToArrayVisitor_caseObjectType_returnsArrayType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType objectType = new ObjectType();
        JSType arrayType = new JSType(ARRAY_TYPE);
        objectType.setSubtype(arrayType);
        JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseObjectType(objectType);

        assertEquals(arrayType, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor_caseObjectType_returnsNull() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType objectType = new ObjectType();
        JSType arrayType = new JSType(ARRAY_TYPE);
        objectType.setSubtype(arrayType);
        JSType result = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(objectType);

        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseTopType_returnsNoObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType topType = new JSType();
        JSType noObjectType = new JSType(NO_OBJECT_TYPE);
        JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);

        assertEquals(noObjectType, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseObjectType_returnsObjectType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType objectType = new ObjectType();
        JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(objectType);

        assertEquals(objectType, result);
    }

    @Test
    public void testRestrictToObjectVisitor_caseFunctionType_returnsFunctionType() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType functionType = new FunctionType();
        JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(functionType);

        assertEquals(functionType, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseObjectType_returnsNull() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType objectType = new ObjectType();
        JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(objectType);

        assertNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor_caseFunctionType_returnsNull() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType functionType = new FunctionType();
        JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_conditionIsCall_returnsPreciserScope() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node condition = new Node();
        condition.setType(CALL);
        Node callee = new Node();
        callee.setType(GETPROP);
        Node param = new Node();
        param.setQualifiedName(true);
        callee.addChildToFront(new Node("goog"));
        callee.addChildToBack(new Node("isDef"));
        condition.addChildToFront(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // TODO: Add assertion
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_conditionIsNotCall_returnsNextPreciserScope() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // TODO: Add assertion
    }

    @Test
    public void testRestrictParameter_restrictionApplies_restrictedTypeReturned() {
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                // TODO: Add implementation
            }
        };
        boolean outcome = true;
        FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

        // TODO: Add assertion
    }
}
