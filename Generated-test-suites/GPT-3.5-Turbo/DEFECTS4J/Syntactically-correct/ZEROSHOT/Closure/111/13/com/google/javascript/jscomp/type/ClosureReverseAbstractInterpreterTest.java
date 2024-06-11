import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

public class ClosureReverseAbstractInterpreterTest {
    
    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;
    private CodingConvention codingConvention;
    private JSTypeRegistry typeRegistry;
    
    @Before
    public void setUp() {
        codingConvention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = true;
        
        when(condition.isCall()).thenReturn(true);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(mock(Node.class));
        when(condition.getLastChild()).thenReturn(mock(Node.class));
        when(condition.getFirstChild().isGetProp()).thenReturn(true);
        when(condition.getLastChild().isQualifiedName()).thenReturn(true);
        when(condition.getFirstChild().getFirstChild()).thenReturn(mock(Node.class));
        when(condition.getFirstChild().getLastChild()).thenReturn(mock(Node.class));
        when(condition.getFirstChild().getFirstChild().isName()).thenReturn(true);
        when(condition.getFirstChild().getFirstChild().getString()).thenReturn("goog");
        when(condition.getFirstChild().getLastChild().isString()).thenReturn(true);
        when(condition.getFirstChild().getLastChild().getString()).thenReturn("isDef");
        when(typeRegistry.createUnionType(any(), any())).thenReturn(mock(JSType.class));
        
        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        assertNotNull(result);
        verify(blindScope).createChildFlowScope();
    }
    
    @Test
    public void testRestrictParameter() {
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        boolean outcome = true;
        
        when(restriction.apply(any())).thenReturn(mock(JSType.class));
        when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));
        
        FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        assertNotNull(result);
        verify(blindScope).createChildFlowScope();
    }

}