import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter interpreter;
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;

    @Before
    public void setUp() {
        convention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = true;

        // mock condition node structure
        Node callee = mock(Node.class);
        Node param = mock(Node.class);
        Node left = mock(Node.class);
        Node right = mock(Node.class);
        when(condition.isCall()).thenReturn(true);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.isGetProp()).thenReturn(true);
        when(param.isQualifiedName()).thenReturn(true);
        when(callee.getFirstChild()).thenReturn(left);
        when(callee.getLastChild()).thenReturn(right);
        when(left.isName()).thenReturn(true);
        when(left.getString()).thenReturn("goog");
        when(right.isString()).thenReturn(true);
        when(right.getString()).thenReturn("isDef");

        // mock getTypeIfRefinable
        Node refNode = mock(Node.class);
        FlowScope scope = mock(FlowScope.class);
        JSType type = mock(JSType.class);
        when(typeRegistry.getTypeIfRefinable(refNode, scope)).thenReturn(type);

        // mock restrictParameter
        JSType restrictedType = mock(JSType.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        when(restriction.apply(mock(TypeRestriction.class))).thenReturn(restrictedType);
        when(interpreter.restricters.get("isDef")).thenReturn(restriction);

        interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // verify method calls
        verify(typeRegistry).getTypeIfRefinable(param, blindScope);
        verify(restriction).apply(mock(TypeRestriction.class));

        // verify changed scope
        verify(blindScope).createChildFlowScope();
        verify(interpreter).declareNameInScope(informed, parameter, restrictedType);
    }
}