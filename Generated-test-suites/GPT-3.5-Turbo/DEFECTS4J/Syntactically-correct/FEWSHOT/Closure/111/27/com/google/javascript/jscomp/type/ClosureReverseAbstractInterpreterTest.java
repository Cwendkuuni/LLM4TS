import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter interpreter;
    private CodingConvention convention;
    private JSTypeRegistry typeRegistry;

    @Before
    public void setUp() {
        convention = new CodingConvention();
        typeRegistry = new JSTypeRegistry();
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsDef() {
        Node condition = getNodeForCall("goog.isDef(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsNull() {
        Node condition = getNodeForCall("goog.isNull(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsDefAndNotNull() {
        Node condition = getNodeForCall("goog.isDefAndNotNull(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsString() {
        Node condition = getNodeForCall("goog.isString(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsBoolean() {
        Node condition = getNodeForCall("goog.isBoolean(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsNumber() {
        Node condition = getNodeForCall("goog.isNumber(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsFunction() {
        Node condition = getNodeForCall("goog.isFunction(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsArray() {
        Node condition = getNodeForCall("goog.isArray(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsObject() {
        Node condition = getNodeForCall("goog.isObject(param)");
        FlowScope blindScope = createDummyFlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    private Node getNodeForCall(String call) {
        Node root = new Node(Node.SCRIPT);
        Node exprResult = new Node(Node.EXPR_RESULT);
        Node callNode = new Node(Node.CALL);
        callNode.addChildToFront(new Node(Node.NAME, call.substring(0, call.indexOf("("))));
        callNode.addChildToBack(new Node(Node.NAME, call.substring(call.indexOf("(")+1, call.indexOf(")"))));
        exprResult.addChildToBack(callNode);
        root.addChildToBack(exprResult);
        return callNode;
    }

    private FlowScope createDummyFlowScope() {
        return new FlowScope(null);
    }
}