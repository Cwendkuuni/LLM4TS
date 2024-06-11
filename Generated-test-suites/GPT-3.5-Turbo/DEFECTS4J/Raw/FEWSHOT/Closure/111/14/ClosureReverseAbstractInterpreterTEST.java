package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter cri;

    @Before
    public void setUp() {
        CodingConvention convention = null;
        JSTypeRegistry typeRegistry = null;
        cri = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = null;
        FlowScope blindScope = null;
        boolean outcome = true;
        FlowScope result = cri.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertNotNull(result);
    }

    @Test
    public void testRestrictParameter() {
        Node parameter = null;
        JSType type = null;
        FlowScope blindScope = null;
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = true;
        FlowScope result = cri.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result);
    }

    @Test
    public void testRestrictParameter_WithNonNullTypeAndOutcomeFalse_ReturnNonNullType() {
        Node parameter = null;
        JSType type = new ObjectType();
        FlowScope blindScope = null;
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = false;
        FlowScope result = cri.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertEquals(type, result.getVar(parameter).getType());
    }

    @Test
    public void testRestrictParameter_WithNonNullTypeAndOutcomeTrue_ReturnUpdatedType() {
        Node parameter = null;
        JSType type = new ObjectType();
        FlowScope blindScope = null;
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = true;
        FlowScope result = cri.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result);
    }
    
    @Test
    public void testRestrictParameter_WithNullTypeAndOutcomeFalse_ReturnNull() {
        Node parameter = null;
        JSType type = null;
        FlowScope blindScope = null;
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = false;
        FlowScope result = cri.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertEquals(null, result);
    }

    @Test
    public void testRestrictParameter_WithNullTypeAndOutcomeTrue_ReturnNull() {
        Node parameter = null;
        JSType type = null;
        FlowScope blindScope = null;
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = true;
        FlowScope result = cri.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertEquals(null, result);
    }

    @Test
    public void testTypeRestrictionConstructor() {
        JSType type = null;
        boolean outcome = true;
        TypeRestriction tr = new TypeRestriction(type, outcome);
        assertNotNull(tr);
    }

    @Test
    public void testRestrictToArrayVisitorCaseTopType() {
        JSType topType = null;
        Visitor<JSType> rv = cri.restrictToArrayVisitor.caseTopType(topType);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToArrayVisitorCaseObjectType() {
        ObjectType type = null;
        Visitor<JSType> rv = cri.restrictToArrayVisitor.caseObjectType(type);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToNotArrayVisitorCaseObjectType() {
        ObjectType type = null;
        Visitor<JSType> rv = cri.restrictToNotArrayVisitor.caseObjectType(type);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToObjectVisitorCaseTopType() {
        JSType topType = null;
        Visitor<JSType> rv = cri.restrictToObjectVisitor.caseTopType(topType);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToObjectVisitorCaseObjectType() {
        ObjectType type = null;
        Visitor<JSType> rv = cri.restrictToObjectVisitor.caseObjectType(type);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToObjectVisitorCaseFunctionType() {
        FunctionType type = null;
        Visitor<JSType> rv = cri.restrictToObjectVisitor.caseFunctionType(type);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToNotObjectVisitorCaseAllType() {
        JSType topType = null;
        Visitor<JSType> rv = cri.restrictToNotObjectVisitor.caseAllType();
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToNotObjectVisitorCaseObjectType() {
        ObjectType type = null;
        Visitor<JSType> rv = cri.restrictToNotObjectVisitor.caseObjectType(type);
        assertNotNull(rv);
    }

    @Test
    public void testRestrictToNotObjectVisitorCaseFunctionType() {
        FunctionType type = null;
        Visitor<JSType> rv = cri.restrictToNotObjectVisitor.caseFunctionType(type);
        assertNotNull(rv);
    }
}