import org.junit.Test;

import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ArrayIsDef_ReturnsArrayType() {
        // Arrange
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope blindScope = new FlowScope();
        Node condition = createConditionNode("goog.isArray", "arr");

        // Act
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

        // Assert
        JSType expectedType = interpreter.getNativeType(ARRAY_TYPE);
        JSType actualType = scope.getDeclaredType("arr");
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ArrayNotDef_ReturnsNull() {
        // Arrange
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope blindScope = new FlowScope();
        Node condition = createConditionNode("goog.isArray", "arr");

        // Act
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

        // Assert
        JSType actualType = scope.getDeclaredType("arr");
        assertNull(actualType);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ObjectIsDef_ReturnsObjectType() {
        // Arrange
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope blindScope = new FlowScope();
        Node condition = createConditionNode("goog.isObject", "obj");

        // Act
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

        // Assert
        JSType expectedType = interpreter.getNativeType(OBJECT_TYPE);
        JSType actualType = scope.getDeclaredType("obj");
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ObjectNotDef_ReturnsNull() {
        // Arrange
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope blindScope = new FlowScope();
        Node condition = createConditionNode("goog.isObject", "obj");

        // Act
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

        // Assert
        JSType actualType = scope.getDeclaredType("obj");
        assertNull(actualType);
    }

    // Helper method to create condition node
    private Node createConditionNode(String methodName, String param) {
        Node callee = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, methodName));
        Node paramNode = new Node(Token.NAME, param);
        Node condition = new Node(Token.CALL, callee, paramNode);
        return condition;
    }
}