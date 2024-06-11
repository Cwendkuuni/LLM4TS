package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        Node param = new Node(Token.NAME);
        param.setQualifiedName(true);
        callee.addChildToBack(new Node(Token.NAME));
        callee.addChildToBack(new Node(Token.STRING));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        FlowScope blindScope = new FlowScope(null);
        blindScope.createChildFlowScope();

        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Assert statements
    }
}