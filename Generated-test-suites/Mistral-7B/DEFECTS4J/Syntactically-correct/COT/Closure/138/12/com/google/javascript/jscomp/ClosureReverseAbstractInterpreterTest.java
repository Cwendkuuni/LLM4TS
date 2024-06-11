import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                new CodingConvention(), typeRegistry);

        Node condition = mock(Node.class);
        Node parameter = mock(Node.class);
        Node blindScope = mock(Node.class);
        JSType paramType = ObjectType.VOID;
        JSType expectedType = ObjectType.VOID;
        boolean outcome = true;

        when(condition.getType()).thenReturn(Node.CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild().getType()).thenReturn(Node.GETPROP);
        when(condition.getLastChild().isQualifiedName()).thenReturn(true);
        when(condition.getLastChild().getString()).thenReturn("isDef");
        when(condition.getFirstChild().getFirstChild().getString())
                .thenReturn("goog");

        when(parameter.getType()).thenReturn(Node.STRING);

        when(blindScope.createChildFlowScope()).thenReturn(node -> node);

        when(typeRegistry.getTypeIfRefinable(parameter, blindScope)).thenReturn(paramType);

        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertEquals(expectedType, actual.getVariableType(parameter.getIdentifier()));
    }

    @Test
    public void testRestrictParameter_isDef_true() {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                new CodingConvention(), typeRegistry);

        Node parameter = mock(Node.class);
        Node blindScope = mock(Node.class);
        JSType inputType = new ObjectType("InputType");
        JSType expectedType = new ObjectType("OutputType");
        boolean outcome = true;

        when(parameter.getType()).thenReturn(null);
        when(blindScope.createChildFlowScope()).thenReturn(node -> node);

        Function<TypeRestriction, JSType> restricter = interpreter.restricters.get("isDef");

        FlowScope actual = interpreter.restrictParameter(parameter, inputType, blindScope, restricter, outcome);

        assertEquals(expectedType, actual.getVariableType(parameter.getIdentifier()));
    }

    @Test
    public void testRestrictParameter_isDef_false() {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                new CodingConvention(), typeRegistry);

        Node parameter = mock(Node.class);
        Node blindScope = mock(Node.class);
        JSType inputType = new ObjectType("InputType");

        when(parameter.getType()).thenReturn(null);
        when(blindScope.createChildFlowScope()).thenReturn(node -> node);

        Function<TypeRestriction, JSType> restricter = interpreter.restricters.get("isDef");

        assertNull(interpreter.restrictParameter(parameter, inputType, blindScope, restricter, false));
    }

    @Test(expected = NullPointerException.class)
    public void testGetPreciserScopeKnowingConditionOutcome_nullCondition() {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                new CodingConvention(), typeRegistry);

        Node condition = null;
        Node blindScope = mock(Node.class);
        boolean outcome = true;

        FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Test should throw a NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestrictParameter_nullType() {
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                new CodingConvention(), typeRegistry);

        Node parameter = mock(Node.class);
        Node blankScope = mock(Node.class);
        JSType inputType = null;
        Function<TypeRestriction, JSType> restricter = interpreter.restricters.get("isDef");
        boolean outcome = true;

        assertNull(interpreter.restrictParameter(parameter, inputType, blankScope, restricter, outcome));

        // Test should throw an IllegalArgumentException
    }

    
}