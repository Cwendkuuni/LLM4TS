import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

	@Test
	public void testGetPreciserScopeKnowingConditionOutcome() {
		// Test case 1
		ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
		Node condition = new Node(Token.CALL);
		Node callee = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isDef"));
		Node param = new Node(Token.NAME, "param1");
		condition.addChildToBack(callee);
		condition.addChildToBack(param);
		FlowScope blindScope = new FlowScope();
		boolean outcome = true;
		FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
		assertNotNull(result);

		// Test case 2
		condition = new Node(Token.CALL);
		callee = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isArray"));
		param = new Node(Token.NAME, "param2");
		condition.addChildToBack(callee);
		condition.addChildToBack(param);
		blindScope = new FlowScope();
		outcome = false;
		result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
		assertNotNull(result);
	}

	@Test
	public void testRestrictParameter() {
		ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
		Node parameter = new Node(Token.NAME, "param");
		JSType type = null;
		FlowScope blindScope = new FlowScope();
		boolean outcome = true;
		Function<ClosureReverseAbstractInterpreter.TypeRestriction, JSType> restriction = interpreter.restricters
				.get("isObject");
		FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
		assertNotNull(result);
	}

}