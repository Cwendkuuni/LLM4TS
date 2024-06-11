import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
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
import org.mockito.Mockito;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;

    private CodingConvention mockCodingConvention;
    private JSTypeRegistry mockJSTypeRegistry;
    private Visitor<JSType> mockVisitor;
    private Function<TypeRestriction, JSType> mockFunction;

    private class TypeRestriction {
        private final JSType type;
        private final boolean outcome;

        private TypeRestriction(JSType type, boolean outcome) {
            this.type = type;
            this.outcome = outcome;
        }
    }

    @Before
    public void setUp() {
        mockCodingConvention = mock(CodingConvention.class);
        mockJSTypeRegistry = mock(JSTypeRegistry.class);
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(mockCodingConvention, mockJSTypeRegistry);

        mockVisitor = mock(Visitor.class);
        mockFunction = mock(Function.class);
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
        when(condition.getFirstChild().getLastChild().getString()).thenReturn("isFunction");

        when(mockJSTypeRegistry.createUnionType(JSTypeNative.NUMBER_STRING_BOOLEAN, JSTypeNative.NULL_VOID)).thenReturn(null);

        // Test with restricters map not containing the function name
        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        Mockito.verify(blindScope, Mockito.times(1)).createChildFlowScope();
    }
}