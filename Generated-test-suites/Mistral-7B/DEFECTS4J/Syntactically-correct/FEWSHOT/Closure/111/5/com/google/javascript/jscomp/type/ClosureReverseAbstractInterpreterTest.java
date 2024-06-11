package com.google.javascript.jscomp.type;

import static com.google.javascript.jscomp.util.CodeCoverage.cover;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Map;

/**
 * Testing all methods in ClosureReverseAbstractInterpreter class.
 */
public class ClosureReverseAbstractInterpreterTest {
    
    @Mock
    private CodingConvention codingConvention;
    
    @Mock
    private JSTypeRegistry typeRegistry;
    
    @InjectMocks
    private ClosureReverseAbstractInterpreter interpreter;
    
    @Before
    public void setup() {
        when(typeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID))
                .thenReturn(mock(JSType.class));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node node = mock(Node.class);
        Scope blindScope = new Scope();
        FlowScope flowScope = mock(FlowScope.class);
        JSType type = mock(JSType.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);

        when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);
        when(interpreter.getRestrictedByTypeOfResult(type, "string", true)).thenReturn(type);
        when(interpreter.getTypeIfRefinable(node, blindScope)).thenReturn(type);
        when(node.isCall()).thenReturn(true);
        when(node.getChildCount()).thenReturn(2);
        when(node.getFirstChild().isName()).thenReturn(true);
        when(node.getFirstChild().getString()).thenReturn("goog");
        when(node.getLastChild().isQualifiedName()).thenReturn(true);
        when(node.getLastChild().getQualifiedNameElements().get(0).isName())
                .thenReturn(true);
        when(node.getLastChild().getQualifiedNameElements().get(0).getString())
                .thenReturn("isString");

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(node, blindScope, true);
        assertNotNull(result);
        cover(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_false() {
        Node node = mock(Node.class);
        Scope blindScope = new Scope();
        FlowScope flowScope = mock(FlowScope.class);

        when(interpreter.nextPreciserScopeKnowingConditionOutcome(node, blindScope, false))
                .thenReturn(flowScope);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(node, blindScope, false);
        assertNotNull(result);
        cover(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_null() {
        Node node = mock(Node.class);
        Scope blindScope = new Scope();

        when(interpreter.nextPreciserScopeKnowingConditionOutcome(node, blindScope, true))
                .thenReturn(null);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(node, blindScope, true);
        assertNull(result);
    }

    @Test
    public void testRestrictParameter() {
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        FlowScope blindScope = new FlowScope();
        FlowScope informedScope = mock(FlowScope.class);

        when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);
        when(interpreter.getRestrictedByTypeOfResult(type, "string", true)).thenReturn(type);
        when(interpreter.restrictParameter(parameter, type, blindScope, restriction, true))
                .thenReturn(informedScope);

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
        assertNotNull(result);
        cover(result);
    }

    @Test
    public void testRestrictParameter_null() {
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        FlowScope blindScope = new FlowScope();

        when(interpreter.restrictParameter(parameter, type, blindScope, restriction, false))
                .thenReturn(blindScope);

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, false);
        assertNotNull(result);
        cover(result);
    }
}