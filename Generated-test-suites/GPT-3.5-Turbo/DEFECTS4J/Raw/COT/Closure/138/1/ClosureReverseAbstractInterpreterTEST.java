import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interp;

    @Before
    public void setUp() {
        interp = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_arrayType() {
        // given
        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        Node param = new Node(Token.NAME, "arr");
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isArray"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        // when
        FlowScope result = interp.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // then
        JSType expected = new JSType(ARRAY_TYPE);
        assertTrue(result.getDeclaredType(param).isSubtype(expected));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_notArrayType() {
        // given
        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        Node param = new Node(Token.NAME, "arr");
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isArray"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;

        // when
        FlowScope result = interp.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // then
        assertFalse(result.getDeclaredType(param).isSubtype(new JSType(ARRAY_TYPE)));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_objectType() {
        // given
        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        Node param = new Node(Token.NAME, "obj");
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isObject"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        // when
        FlowScope result = interp.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // then
        assertNull(result.getDeclaredType(param));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_notObjectType() {
        // given
        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        Node param = new Node(Token.NAME, "obj");
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isObject"));
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;

        // when
        FlowScope result = interp.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // then
        assertNotNull(result.getDeclaredType(param));
    }

}