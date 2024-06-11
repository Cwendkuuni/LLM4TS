
package com.google.javascript.jscomp;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        
        // Test case 1
        when(condition.getType()).thenReturn(1);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(mock(Node.class));
        when(condition.getLastChild()).thenReturn(mock(Node.class));
        assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true));
        
        // Test case 2
        when(condition.getType()).thenReturn(1);
        when(condition.getChildCount()).thenReturn(2);
        Node callee = mock(Node.class);
        Node param = mock(Node.class);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(2);
        when(callee.getFirstChild()).thenReturn(mock(Node.class));
        when(callee.getLastChild()).thenReturn(mock(Node.class));
        when(callee.getString()).thenReturn("goog");
        when(param.isQualifiedName()).thenReturn(true);
        JSType paramType = mock(JSType.class);
        when(interpreter.getTypeIfRefinable(param, blindScope)).thenReturn(paramType);
        when(paramType.visit(any())).thenReturn(null);
        assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true));
        
        // Test case 3
        when(condition.getType()).thenReturn(1);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(2);
        when(callee.getFirstChild()).thenReturn(mock(Node.class));
        when(callee.getLastChild()).thenReturn(mock(Node.class));
        when(callee.getString()).thenReturn("goog");
        when(param.isQualifiedName()).thenReturn(true);
        when(interpreter.getTypeIfRefinable(param, blindScope)).thenReturn(paramType);
        when(paramType.visit(any())).thenReturn(paramType);
        when(paramType.toString()).thenReturn("type");
        assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true));
        
        // Test case 4
        when(condition.getType()).thenReturn(1);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(2);
        when(callee.getFirstChild()).thenReturn(mock(Node.class));
        when(callee.getLastChild()).thenReturn(mock(Node.class));
        when(callee.getString()).thenReturn("goog");
        when(param.isQualifiedName()).thenReturn(true);
        when(interpreter.getTypeIfRefinable(param, blindScope)).thenReturn(paramType);
        when(paramType.visit(any())).thenReturn(paramType);
        when(paramType.toString()).thenReturn("type");
        when(interpreter.restricters.get(any())).thenReturn(null);
        assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true));
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        
        // Test case 1
        TypeRestriction restrictionParam = new TypeRestriction(null, true);
        when(restriction.apply(restrictionParam)).thenReturn(type);
        when(interpreter.createChildFlowScope()).thenReturn(blindScope);
        interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
        verify(interpreter).declareNameInScope(blindScope, parameter, type);
        
        // Test case 2
        when(restriction.apply(restrictionParam)).thenReturn(null);
        interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
        verify(interpreter, never()).declareNameInScope(blindScope, parameter, type);
    }

    @Test
    public void testTypeRestriction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        JSType type = mock(JSType.class);
        
        // Test case 1
        TypeRestriction restriction = interpreter.new TypeRestriction(type, true);
        assertEquals(restriction.type, type);
        assertTrue(restriction.outcome);
        
        // Test case 2
        restriction = interpreter.new TypeRestriction(type, false);
        assertEquals(restriction.type, type);
        assertFalse(restriction.outcome);
    }
}
