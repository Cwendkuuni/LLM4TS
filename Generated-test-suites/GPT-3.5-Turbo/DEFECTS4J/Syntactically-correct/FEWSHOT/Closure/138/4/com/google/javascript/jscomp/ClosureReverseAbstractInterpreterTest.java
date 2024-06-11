import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Function;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        convention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withValidParameters_callsNextPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);

        interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

        verify(interpreter).nextPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_callsRestrictParameter() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        Node callee = mock(Node.class);
        Node param = mock(Node.class);
        Node left = mock(Node.class);
        Node right1 = mock(Node.class);
        Node right2 = mock(Node.class);
        Function<TypeRestriction, JSType> restricter = mock(Function.class);

        when(condition.getType()).thenReturn(6);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(8);
        when(param.isQualifiedName()).thenReturn(true);
        when(getTypeIfRefinable(param, blindScope)).thenReturn(mock(JSType.class));
        when(callee.getFirstChild()).thenReturn(left);
        when(callee.getLastChild()).thenReturn(right1);
        when(left.getType()).thenReturn(39);
        when(left.getString()).thenReturn("goog");
        when(right1.getType()).thenReturn(40);
        when(right1.getString()).thenReturn("isDef");
        when(restricters.get(right1.getString())).thenReturn(restricter);

        interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

        verify(interpreter).restrictParameter(param, mock(JSType.class), blindScope, restricter, true);
    }

    @Test
    public void testRestrictParameter_withNonNullType_callsCreateChildFlowScopeAndDeclareNameInScope() {
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        FlowScope blindScope = mock(FlowScope.class);
        FlowScope informed = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);

        when(restriction.apply(new TypeRestriction(type, true))).thenReturn(type);
        when(blindScope.createChildFlowScope()).thenReturn(informed);

        interpreter.restrictParameter(parameter, type, blindScope, restriction, true);

        verify(informed).declareNameInScope(parameter, type);
    }
}