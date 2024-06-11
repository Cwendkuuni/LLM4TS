
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.jscomp.CodingConvention;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsDef() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isDef")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.STRING_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedWithoutUndefined(JSTypeNative.STRING_TYPE);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsNull() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isNull")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.NULL_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getNativeType(JSTypeNative.NULL_TYPE);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsDefAndNotNull() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isDefAndNotNull")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.UNKNOWN_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedWithoutUndefined(interpreter.getRestrictedWithoutNull(JSTypeNative.UNKNOWN_TYPE));
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsString() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isString")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.STRING_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedByTypeOfResult(JSTypeNative.STRING_TYPE, "string", outcome);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsBoolean() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isBoolean")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.BOOLEAN_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedByTypeOfResult(JSTypeNative.BOOLEAN_TYPE, "boolean", outcome);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsNumber() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isNumber")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.NUMBER_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedByTypeOfResult(JSTypeNative.NUMBER_TYPE, "number", outcome);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsFunction() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isFunction")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.UNKNOWN_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getRestrictedByTypeOfResult(JSTypeNative.UNKNOWN_TYPE, "function", outcome);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsArray_whenOutcomeIsTrue() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isArray")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.UNKNOWN_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getNativeType(JSTypeNative.ARRAY_TYPE);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsArray_whenOutcomeIsFalse() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isArray")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.ARRAY_TYPE);
        boolean outcome = false;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNull(resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsObject_whenOutcomeIsTrue() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isObject")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.UNKNOWN_TYPE);
        boolean outcome = true;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        JSType expectedType = interpreter.getNativeType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expectedType, resultScope.getSlotType("param"));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withIsObject_whenOutcomeIsFalse() {
        Node condition = new Node(
                new Node(Token.GETPROP,
                        new Node(Token.NAME, new Node(Token.STRING, "goog")),
                        new Node(Token.STRING, "isObject")),
                new Node(Token.NAME, new Node(Token.STRING, "param"))
        );
        FlowScope blindScope = new FlowScope();
        blindScope.inferSlotType("param", JSTypeNative.OBJECT_TYPE);
        boolean outcome = false;

        FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNull(resultScope.getSlotType("param"));
    }
}
