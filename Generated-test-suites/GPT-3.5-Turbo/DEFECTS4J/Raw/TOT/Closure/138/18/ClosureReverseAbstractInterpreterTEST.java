import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Test case 1: When the input type is Array
        JSType inputType1 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.ARRAY_TYPE)
                .build();
        JSType expected1 = inputType1;
        JSType result1 = interpreter.restrictToArrayVisitor.visit(inputType1);
        assertEquals(expected1, result1);

        // Test case 2: When the input type is not Array
        JSType inputType2 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.OBJECT_TYPE)
                .build();
        JSType expected2 = null;
        JSType result2 = interpreter.restrictToArrayVisitor.visit(inputType2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Test case 1: When the input type is Array
        JSType inputType1 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.ARRAY_TYPE)
                .build();
        JSType expected1 = null;
        JSType result1 = interpreter.restrictToNotArrayVisitor.visit(inputType1);
        assertEquals(expected1, result1);

        // Test case 2: When the input type is not Array
        JSType inputType2 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.OBJECT_TYPE)
                .build();
        JSType expected2 = inputType2;
        JSType result2 = interpreter.restrictToNotArrayVisitor.visit(inputType2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Test case 1: When the input type is an Object
        JSType inputType1 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.OBJECT_TYPE)
                .build();
        JSType expected1 = inputType1;
        JSType result1 = interpreter.restrictToObjectVisitor.visit(inputType1);
        assertEquals(expected1, result1);

        // Test case 2: When the input type is a Function
        JSType inputType2 = new JSType.JSTypeBuilder()
                .withFunctionType()
                .build();
        JSType expected2 = inputType2;
        JSType result2 = interpreter.restrictToObjectVisitor.visit(inputType2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Test case 1: When the input type is an Object
        JSType inputType1 = new JSType.JSTypeBuilder()
                .withNativeType(JSTypeNative.OBJECT_TYPE)
                .build();
        JSType expected1 = null;
        JSType result1 = interpreter.restrictToNotObjectVisitor.visit(inputType1);
        assertEquals(expected1, result1);

        // Test case 2: When the input type is a Function
        JSType inputType2 = new JSType.JSTypeBuilder()
                .withFunctionType()
                .build();
        JSType expected2 = null;
        JSType result2 = interpreter.restrictToNotObjectVisitor.visit(inputType2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Test case 1: When the condition is not a CALL node
        Node condition1 = new Node(NodeType.NAME);
        boolean outcome1 = true;
        FlowScope blindScope1 = new FlowScope();
        FlowScope expected1 = interpreter.nextPreciserScopeKnowingConditionOutcome(
                condition1, blindScope1, outcome1);
        FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition1, blindScope1, outcome1);
        assertEquals(expected1, result1);

        // Test case 2: When the callee is not GETPROP
        Node condition2 = new Node(NodeType.CALL);
        condition2.addChildToFront(new Node(NodeType.NAME));
        condition2.addChildToBack(new Node(NodeType.NAME));
        boolean outcome2 = true;
        FlowScope blindScope2 = new FlowScope();
        FlowScope expected2 = interpreter.nextPreciserScopeKnowingConditionOutcome(
                condition2, blindScope2, outcome2);
        FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition2, blindScope2, outcome2);
        assertEquals(expected2, result2);

        // Test case 3: When the callee is GETPROP and the parameter is not qualified name
        Node condition3 = new Node(NodeType.CALL);
        Node callee3 = new Node(NodeType.GETPROP);
        callee3.addChildToFront(new Node(NodeType.NAME));
        callee3.addChildToBack(new Node(NodeType.STRING));
        condition3.addChildToFront(callee3);
        condition3.addChildToBack(new Node(NodeType.NAME));
        boolean outcome3 = true;
        FlowScope blindScope3 = new FlowScope();
        FlowScope expected3 = interpreter.nextPreciserScopeKnowingConditionOutcome(
                condition3, blindScope3, outcome3);
        FlowScope result3 = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition3, blindScope3, outcome3);
        assertEquals(expected3, result3);

        // Test case 4: When the callee is GETPROP and the parameter is qualified name
        Node condition4 = new Node(NodeType.CALL);
        Node callee4 = new Node(NodeType.GETPROP);
        callee4.addChildToFront(new Node(NodeType.NAME));
        callee4.addChildToBack(new Node(NodeType.STRING));
        condition4.addChildToFront(callee4);
        condition4.addChildToBack(new Node(NodeType.QUALIFIED_NAME));
        boolean outcome4 = true;
        FlowScope blindScope4 = new FlowScope();
        FlowScope expected4 = interpreter.restrictParameter(
                condition4.getLastChild(), condition4.getLastChild().getType(),
                blindScope4, interpreter.restricters.get(condition4.getLastChild().getType()),
                outcome4);
        FlowScope result4 = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition4, blindScope4, outcome4);
        assertEquals(expected4, result4);
    }
}