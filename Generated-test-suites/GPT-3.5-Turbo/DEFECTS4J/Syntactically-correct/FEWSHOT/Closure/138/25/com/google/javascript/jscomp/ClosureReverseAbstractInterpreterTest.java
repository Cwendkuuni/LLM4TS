import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());

        // Test first if condition.getType() == CALL and condition.getChildCount() == 2
        Node condition = new Node(CALL);
        Node callee = new Node(GETPROP);
        Node param = new Node(NAME, "param");
        Node left = new Node(NAME, "goog");
        Node right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here

        // Test if callee.getType() == GETPROP and param.isQualifiedName()
        condition = new Node(CALL);
        callee = new Node(GETPROP);
        param = new Node(NAME, "param");
        right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here

        // Test if left.getType() == NAME and "goog".equals(left.getString()) and right.getType() == STRING
        condition = new Node(CALL);
        callee = new Node(GETPROP);
        param = new Node(NAME, "param");
        left = new Node(NAME, "goog");
        right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here

        // Test if restricter != null
        condition = new Node(CALL);
        callee = new Node(GETPROP);
        param = new Node(NAME, "param");
        left = new Node(NAME, "goog");
        right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here

        // Test if type != null
        condition = new Node(CALL);
        callee = new Node(GETPROP);
        param = new Node(NAME, "param");
        left = new Node(NAME, "goog");
        right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here

        // Test if type is null
        condition = new Node(CALL);
        callee = new Node(GETPROP);
        param = new Node(NAME, "param");
        left = new Node(NAME, "goog");
        right = new Node(STRING, "isArray");
        left.addChildToBack(right);
        callee.addChildToBack(left);
        condition.addChildToBack(callee);
        condition.addChildToBack(param);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert statements here
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \\\"bar\\\"", Util.stripLeadingAndTrailingQuotes("foo \\\"bar\\\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}