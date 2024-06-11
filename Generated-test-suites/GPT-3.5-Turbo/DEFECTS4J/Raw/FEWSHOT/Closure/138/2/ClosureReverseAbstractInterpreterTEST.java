package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    @Mock
    private CodingConvention codingConvention;
    @Mock
    private JSTypeRegistry typeRegistry;
    
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Setup
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = true;
        
        // Test
        interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        // Verify
        verify(blindScope).createChildFlowScope();
    }

    @Test
    public void testRestrictParameter() {
        // Setup
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        boolean outcome = true;
        
        // Test
        interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify
        verify(blindScope).createChildFlowScope();
    }

    @Test
    public void testTypeRestriction() {
        // Setup
        JSType type = mock(JSType.class);
        boolean outcome = true;
        
        // Test
        ClosureReverseAbstractInterpreter.TypeRestriction restriction = 
            new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
        
        // Verify
        assertEquals(type, restriction.type);
        assertEquals(outcome, restriction.outcome);
    }
}