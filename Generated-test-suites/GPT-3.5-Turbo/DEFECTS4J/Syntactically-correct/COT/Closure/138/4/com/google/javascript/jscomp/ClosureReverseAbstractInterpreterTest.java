import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;

    @Before
    public void setUp() {
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(
                mock(CodingConvention.class),
                mock(JSTypeRegistry.class)
        );
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Test case for condition.getType() != CALL
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = true;

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that nextPreciserScopeKnowingConditionOutcome is called
        verify(closureReverseAbstractInterpreter).nextPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Test case for condition.getType() == CALL and condition.getChildCount() != 2
        condition = mock(Node.class);
        Node callee = mock(Node.class);
        Node param = mock(Node.class);
        when(condition.getType()).thenReturn(CALL);
        when(condition.getChildCount()).thenReturn(1);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that nextPreciserScopeKnowingConditionOutcome is called
        verify(closureReverseAbstractInterpreter).nextPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Test case for condition.getType() == CALL, condition.getChildCount() == 2,
        // and callee.getType() != GETPROP
        condition = mock(Node.class);
        callee = mock(Node.class);
        param = mock(Node.class);
        when(condition.getType()).thenReturn(CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(NAME);

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that nextPreciserScopeKnowingConditionOutcome is called
        verify(closureReverseAbstractInterpreter).nextPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );


        // Test case for condition.getType() == CALL, condition.getChildCount() == 2,
        //callee.getType() == GETPROP, param.isQualifiedName() == false
        condition = mock(Node.class);
        callee = mock(Node.class);
        param = mock(Node.class);
        when(condition.getType()).thenReturn(CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(GETPROP);
        when(param.isQualifiedName()).thenReturn(false);

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that nextPreciserScopeKnowingConditionOutcome is called
        verify(closureReverseAbstractInterpreter).nextPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );


        // Test case for condition.getType() == CALL, condition.getChildCount() == 2,
        //callee.getType() == GETPROP, param.isQualifiedName() == true,
        //left.getType() != NAME, and "goog".equals(left.getString()) == false
        condition = mock(Node.class);
        callee = mock(Node.class);
        param = mock(Node.class);
        Node left = mock(Node.class);
        Node right = mock(Node.class);
        when(condition.getType()).thenReturn(CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(GETPROP);
        when(param.isQualifiedName()).thenReturn(true);
        when(callee.getFirstChild()).thenReturn(left);
        when(callee.getLastChild()).thenReturn(right);
        when(left.getType()).thenReturn(NAME);
        when(left.getString()).thenReturn("notgoog");

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that nextPreciserScopeKnowingConditionOutcome is called
        verify(closureReverseAbstractInterpreter).nextPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Test case for condition.getType() == CALL, condition.getChildCount() == 2,
        //callee.getType() == GETPROP, param.isQualifiedName() == true,
        //left.getType() == NAME, and "goog".equals(left.getString()) == true
        condition = mock(Node.class);
        callee = mock(Node.class);
        param = mock(Node.class);
        left = mock(Node.class);
        right = mock(Node.class);
        when(condition.getType()).thenReturn(CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild()).thenReturn(callee);
        when(condition.getLastChild()).thenReturn(param);
        when(callee.getType()).thenReturn(GETPROP);
        when(param.isQualifiedName()).thenReturn(true);
        when(callee.getFirstChild()).thenReturn(left);
        when(callee.getLastChild()).thenReturn(right);
        when(left.getType()).thenReturn(NAME);
        when(left.getString()).thenReturn("goog");

        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
                condition, blindScope, outcome
        );

        // Verify that restrictParameter is called
        verify(closureReverseAbstractInterpreter).restrictParameter(
                param, null, blindScope, closureReverseAbstractInterpreter.restricters.get(right.getString()), outcome
        );
    }
}