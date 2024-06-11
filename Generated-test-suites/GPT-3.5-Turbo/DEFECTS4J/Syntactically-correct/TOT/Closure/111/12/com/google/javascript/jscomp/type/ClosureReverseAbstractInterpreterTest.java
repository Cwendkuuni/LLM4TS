import org.junit.Test;

import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void getPreciserScopeKnowingConditionOutcome_ArrayTypeIsNull_ReturnsNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node condition = new Node(Node.CALL);
        Node callee = new Node(Node.GETPROP);
        Node param = new Node(Node.NAME);
        param.setString("arr");
        callee.addChildToBack(new Node(Node.NAME, "goog"));
        callee.addChildToBack(new Node(Node.STRING, "isArray"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope(); // Create child scope to simulate nesting
        boolean outcome = true;
        
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        assertNull(result);
    }

    @Test
    public void getPreciserScopeKnowingConditionOutcome_ValidTypeRestriction_ReturnsInformedScope() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node condition = new Node(Node.CALL);
        Node callee = new Node(Node.GETPROP);
        Node param = new Node(Node.NAME);
        param.setString("obj");
        callee.addChildToBack(new Node(Node.NAME, "goog"));
        callee.addChildToBack(new Node(Node.STRING, "isObject"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        JSType objectType = new JSType(JSTypeNative.OBJECT_TYPE);
        blindScope.declareNameInScope(param.getString(), objectType);
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
        assertEquals(objectType, result.getType(param.getString()));
    }

    @Test
    public void restrictParameter_OutcomeIsTrue_RestrictsParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node(Node.NAME);
        parameter.setString("param");
        JSType type = new JSType(JSTypeNative.NUMBER_STRING_BOOLEAN);
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope(); // Create child scope to simulate nesting
        boolean outcome = true;

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, outcome);

        assertNotNull(result);
        assertEquals(type, result.getType(parameter.getString()));
    }

    @Test
    public void restrictParameter_OutcomeIsFalse_ReturnsBlindScope() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node(Node.NAME);
        parameter.setString("param");
        JSType type = new JSType(JSTypeNative.NUMBER_STRING_BOOLEAN);
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope(); // Create child scope to simulate nesting
        boolean outcome = false;
        
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, outcome);
        
        assertEquals(blindScope, result);
    }

    @Test
    public void restrictParameter_TypeIsNull_OutcomeIsTrue_ReturnsArrayType() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node(Node.NAME);
        parameter.setString("arr");
        JSType type = null;
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, outcome);
        
        assertNotNull(result);
        assertEquals(JSTypeNative.ARRAY_TYPE, result.getType(parameter.getString()));
    }

    @Test
    public void restrictParameter_TypeIsNull_OutcomeIsFalse_ReturnsNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node(Node.NAME);
        parameter.setString("arr");
        JSType type = null;
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;
        
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, outcome);
        
        assertEquals(blindScope, result);
    }

}