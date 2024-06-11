import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Visitor<JSType> restrictToArrayVisitor = interpreter.getRestrictToArrayVisitor();

        // Typical use case: array type
        JSType arrayType = new ArrayType();
        JSType result = restrictToArrayVisitor.visit(arrayType);
        assertEquals(arrayType, result);

        // Edge case: top type
        JSType topType = new TopType();
        result = restrictToArrayVisitor.visit(topType);
        assertNull(result);

        // Error scenario: non-array type
        JSType objectType = new ObjectType();
        result = restrictToArrayVisitor.visit(objectType);
        assertNull(result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Visitor<JSType> restrictToNotArrayVisitor = interpreter.getRestrictToNotArrayVisitor();

        // Typical use case: object type
        JSType objectType = new ObjectType();
        JSType result = restrictToNotArrayVisitor.visit(objectType);
        assertEquals(objectType, result);

        // Edge case: top type
        JSType topType = new TopType();
        result = restrictToNotArrayVisitor.visit(topType);
        assertNull(result);

        // Error scenario: array type
        JSType arrayType = new ArrayType();
        result = restrictToNotArrayVisitor.visit(arrayType);
        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Visitor<JSType> restrictToObjectVisitor = interpreter.getRestrictToObjectVisitor();

        // Typical use case: object type
        JSType objectType = new ObjectType();
        JSType result = restrictToObjectVisitor.visit(objectType);
        assertEquals(objectType, result);

        // Edge case: top type
        JSType topType = new TopType();
        result = restrictToObjectVisitor.visit(topType);
        JSType expectedType = new NoObjectType();
        assertEquals(expectedType, result);

        // Error scenario: function type
        JSType functionType = new FunctionType();
        result = restrictToObjectVisitor.visit(functionType);
        assertNull(result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Visitor<JSType> restrictToNotObjectVisitor = interpreter.getRestrictToNotObjectVisitor();

        // Typical use case: function type
        JSType functionType = new FunctionType();
        JSType result = restrictToNotObjectVisitor.visit(functionType);
        assertNull(result);

        // Edge case: top type
        JSType topType = new TopType();
        result = restrictToNotObjectVisitor.visit(topType);
        assertNull(result);

        // Error scenario: object type
        JSType objectType = new ObjectType();
        result = restrictToNotObjectVisitor.visit(objectType);
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope blindScope = new FlowScope();

        // Typical use case: goog.isDef
        Node condition = createConditionNode("goog.isDef", "var1");
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNotNull(result);

        // Edge case: goog.isNull
        condition = createConditionNode("goog.isNull", "var2");
        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNotNull(result);

        // Error scenario: unknown condition
        condition = createConditionNode("unknownCondition", "var3");
        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNull(result);
    }

    private Node createConditionNode(String functionName, String variableName) {
        Node functionNode = new Node(functionName);
        Node variableNode = new Node(variableName);
        Node conditionNode = new Node(Token.CALL, functionNode, variableNode);
        return conditionNode;
    }
}