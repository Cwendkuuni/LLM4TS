package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import com.google.common.base.Function;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    
    private ClosureReverseAbstractInterpreter closureRAI;
    private JSTypeRegistry typeRegistry;

    @Before
    public void setup() {
        CodingConvention convention = new CodingConvention();
        typeRegistry = new JSTypeRegistry(null, null);
        closureRAI = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testRestrictToArrayVisitor() {
        ObjectType objType = typeRegistry.createObjectType("Object", null);
        Object result = closureRAI.restrictToArrayVisitor.caseObjectType(objType);
        assertNull(result);

        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        result = closureRAI.restrictToArrayVisitor.caseObjectType(arrayType);
        assertEquals(arrayType, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ObjectType objType = typeRegistry.createObjectType("Object", null);
        Object result = closureRAI.restrictToNotArrayVisitor.caseObjectType(objType);
        assertEquals(objType, result);

        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        result = closureRAI.restrictToNotArrayVisitor.caseObjectType(arrayType);
        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ObjectType objType = typeRegistry.createObjectType("Object", null);
        Object result = closureRAI.restrictToObjectVisitor.caseObjectType(objType);
        assertEquals(objType, result);

        FunctionType funcType = typeRegistry.createFunctionType("Function");
        result = closureRAI.restrictToObjectVisitor.caseFunctionType(funcType);
        assertEquals(funcType, result);

        JSType nullType = typeRegistry.getNativeType(NULL_TYPE);
        result = closureRAI.restrictToObjectVisitor.caseTopType(nullType);
        assertEquals(typeRegistry.getNativeType(NO_OBJECT_TYPE), result);

        JSType topType = typeRegistry.getNativeType(OBJECT_TYPE);
        result = closureRAI.restrictToObjectVisitor.caseTopType(topType);
        assertEquals(typeRegistry.getNativeType(NO_OBJECT_TYPE), result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ObjectType objType = typeRegistry.createObjectType("Object", null);
        Object result = closureRAI.restrictToNotObjectVisitor.caseObjectType(objType);
        assertNull(result);

        FunctionType funcType = typeRegistry.createFunctionType("Function");
        result = closureRAI.restrictToNotObjectVisitor.caseFunctionType(funcType);
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertEquals(blindScope, result);
    }

    @Test
    public void testRestrictParameter() {
        Node parameter = new Node();
        JSType type = typeRegistry.createObjectType("Object", null);
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            public JSType apply(TypeRestriction p) {
                return p.outcome ? p.type : null;
            }
        };

        FlowScope result = closureRAI.restrictParameter(parameter, type, blindScope, restriction, outcome);

        FlowScope informed = blindScope.createChildFlowScope();
        closureRAI.declareNameInScope(informed, parameter, type);

        assertEquals(informed, result);
    }
}