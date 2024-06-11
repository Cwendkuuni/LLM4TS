import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Test case 1
        Node condition1 = new Node(Token.CALL);
        Node callee1 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isDef"));
        Node param1 = new Node(Token.NAME, "x");
        callee1.addChildToFront(param1);
        condition1.addChildToFront(callee1);
        FlowScope blindScope1 = new FlowScope();
        blindScope1.declareName("x", JSType.UNKNOWN);
        boolean outcome1 = true;
        FlowScope expectedScope1 = new FlowScope();
        expectedScope1.declareName("x", JSType.UNKNOWN); // No change in the scope
        FlowScope actualScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, outcome1);
        assertEquals(expectedScope1, actualScope1);
        
        // Test case 2
        Node condition2 = new Node(Token.CALL);
        Node callee2 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isArray"));
        Node param2 = new Node(Token.NAME, "x");
        callee2.addChildToFront(param2);
        condition2.addChildToFront(callee2);
        FlowScope blindScope2 = new FlowScope();
        blindScope2.declareName("x", JSType.UNKNOWN);
        boolean outcome2 = false;
        FlowScope expectedScope2 = new FlowScope();
        expectedScope2.declareName("x", JSType.UNKNOWN); // No change in the scope
        FlowScope actualScope2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2);
        assertEquals(expectedScope2, actualScope2);
    }
    
    @Test
    public void testRestrictParameter() {
        // Test case 1
        Node parameter1 = new Node(Token.NAME, "x");
        JSType type1 = JSType.UNKNOWN;
        FlowScope blindScope1 = new FlowScope();
        blindScope1.declareName("x", JSType.UNKNOWN);
        Function<TypeRestriction, JSType> restriction1 = restricters.get("isDef");
        boolean outcome1 = true;
        FlowScope expectedScope1 = new FlowScope();
        expectedScope1.declareName("x", JSType.UNKNOWN); // No change in the scope
        FlowScope actualScope1 = interpreter.restrictParameter(parameter1, type1, blindScope1, restriction1, outcome1);
        assertEquals(expectedScope1, actualScope1);
        
        // Test case 2
        Node parameter2 = new Node(Token.NAME, "x");
        JSType type2 = JSType.UNKNOWN;
        FlowScope blindScope2 = new FlowScope();
        blindScope2.declareName("x", JSType.UNKNOWN);
        Function<TypeRestriction, JSType> restriction2 = restricters.get("isFunction");
        boolean outcome2 = false;
        FlowScope expectedScope2 = new FlowScope();
        expectedScope2.declareName("x", JSType.UNKNOWN); // No change in the scope
        FlowScope actualScope2 = interpreter.restrictParameter(parameter2, type2, blindScope2, restriction2, outcome2);
        assertEquals(expectedScope2, actualScope2);
    }
}