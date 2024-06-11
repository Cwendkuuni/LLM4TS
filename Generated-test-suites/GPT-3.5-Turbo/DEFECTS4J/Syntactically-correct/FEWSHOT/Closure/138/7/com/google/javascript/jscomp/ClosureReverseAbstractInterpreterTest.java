
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(),
                new JSTypeRegistry());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node conditionNode = new Node(
                new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.STRING, "goog")), new Node(Token.STRING, "isArray")),
                new Node(Token.NAME, "param"));

        // outcome true
        FlowScope blindScopeTrue = new FlowScope(null);
        FlowScope resultTrue = interpreter.getPreciserScopeKnowingConditionOutcome(
                conditionNode, blindScopeTrue, true);
        JSType expectedTrue = new JSTypeRegistry().getNativeType(
                JSTypeRegistry.getArrayType());
        assertEquals(expectedTrue, resultTrue.getVar("param"));

        // outcome false
        FlowScope blindScopeFalse = new FlowScope(null);
        FlowScope resultFalse = interpreter.getPreciserScopeKnowingConditionOutcome(
                conditionNode, blindScopeFalse, false);
        assertNull(resultFalse.getVar("param"));
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter.TypeRestriction restrictionTrue =
                interpreter.new TypeRestriction(new JSTypeRegistry().getNativeType(
                        JSTypeRegistry.getObjectType()), true);
        ClosureReverseAbstractInterpreter.TypeRestriction restrictionFalse =
                interpreter.new TypeRestriction(new JSTypeRegistry().getNativeType(
                        JSTypeRegistry.getObjectType()), false);

        // outcome true
        Node paramNodeTrue = new Node(Token.NAME, "param");
        FlowScope blindScopeTrue = new FlowScope(null);
        FlowScope resultTrue = interpreter.restrictParameter(paramNodeTrue, new JSTypeRegistry().getNativeType(
                JSTypeRegistry.getObjectType()), blindScopeTrue,
                new Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType>() {
                    public JSType apply(ClosureReverseAbstractInterpreter.TypeRestriction p) {
                        if (p.outcome) {
                            return new JSTypeRegistry().getNativeType(
                                    JSTypeRegistry.getNumberType());
                        } else {
                            return p.type;
                        }
                    }
                }, true);
        JSType expectedTrue = new JSTypeRegistry().getNativeType(
                JSTypeRegistry.getNumberType());
        assertEquals(expectedTrue, resultTrue.getVar("param"));

        // outcome false
        Node paramNodeFalse = new Node(Token.NAME, "param");
        FlowScope blindScopeFalse = new FlowScope(null);
        FlowScope resultFalse = interpreter.restrictParameter(paramNodeFalse, new JSTypeRegistry().getNativeType(
                JSTypeRegistry.getObjectType()), blindScopeFalse,
                new Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType>() {
                    public JSType apply(ClosureReverseAbstractInterpreter.TypeRestriction p) {
                        if (p.outcome) {
                            return new JSTypeRegistry().getNativeType(
                                    JSTypeRegistry.getNumberType());
                        } else {
                            return p.type;
                        }
                    }
                }, false);
        assertNull(resultFalse.getVar("param"));
    }
}
