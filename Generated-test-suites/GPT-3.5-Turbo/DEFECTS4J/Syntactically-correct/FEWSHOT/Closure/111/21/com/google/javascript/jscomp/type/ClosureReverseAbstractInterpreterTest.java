package com.google.javascript.jscomp.type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;
    @Before
    public void setUp() {
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(CodingConvention.EMPTY,
                JSTypeRegistry.EMPTY);
    }
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = new Node(Token.CALL);
        condition.addChildToFront(new Node(Token.GETPROP));
        condition.getFirstChild().addChildToFront(new Node(Token.NAME, "goog"));
        condition.getFirstChild().addChildToBack(new Node(Token.STRING, "isDef"));
        condition.addChildToBack(new Node(Token.NAME, "param"));
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        boolean outcome = true;
        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition,
                blindScope, outcome);
        // Add assertions here
        
        condition = new Node(Token.CALL);
        condition.addChildToFront(new Node(Token.GETPROP));
        condition.getFirstChild().addChildToFront(new Node(Token.NAME, "goog"));
        condition.getFirstChild().addChildToBack(new Node(Token.STRING, "isString"));
        condition.addChildToBack(new Node(Token.NAME, "param"));
        blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        outcome = true;
        result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition,
                blindScope, outcome);
        // Add assertions here
        
        condition = new Node(Token.CALL);
        condition.addChildToFront(new Node(Token.GETPROP));
        condition.getFirstChild().addChildToFront(new Node(Token.NAME, "goog"));
        condition.getFirstChild().addChildToBack(new Node(Token.STRING, "isArray"));
        condition.addChildToBack(new Node(Token.NAME, "param"));
        blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        outcome = false;
        result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition,
                blindScope, outcome);
        // Add assertions here
        
    }
}