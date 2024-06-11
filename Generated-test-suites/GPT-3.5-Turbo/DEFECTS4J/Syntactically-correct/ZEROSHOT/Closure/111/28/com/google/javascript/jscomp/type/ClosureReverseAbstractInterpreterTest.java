import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FlowScope blindScope = new FlowScope();

    // Test case 1
    Node condition1 = new Node(Node.CALL);
    Node callee1 = new Node(Node.GETPROP);
    callee1.addChildToFront(new Node(Node.NAME, "goog"));
    callee1.addChildToBack(new Node(Node.STRING, "isDef"));
    condition1.addChildToFront(callee1);
    condition1.addChildToBack(new Node(Node.STRING, "param1"));
    FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, false);
    assertNotNull(result1);

    // Test case 2
    Node condition2 = new Node(Node.CALL);
    Node callee2 = new Node(Node.GETPROP);
    callee2.addChildToFront(new Node(Node.NAME, "goog"));
    callee2.addChildToBack(new Node(Node.STRING, "isObject"));
    condition2.addChildToFront(callee2);
    condition2.addChildToBack(new Node(Node.STRING, "param2"));
    FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, true);
    assertNotNull(result2);

    // Test case 3
    Node condition3 = new Node(Node.CALL);
    Node callee3 = new Node(Node.GETPROP);
    callee3.addChildToFront(new Node(Node.NAME, "goog"));
    callee3.addChildToBack(new Node(Node.STRING, "isNull"));
    condition3.addChildToFront(callee3);
    condition3.addChildToBack(new Node(Node.STRING, "param3"));
    FlowScope result3 = interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope, true);
    assertNotNull(result3);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FlowScope blindScope = new FlowScope();
    Node parameter = new Node(Node.STRING, "param");

    // Test case 1
    TypeRestriction restriction1 = new TypeRestriction(null, true);
    FlowScope result1 = interpreter.restrictParameter(parameter, restriction1, blindScope);
    assertNotNull(result1);

    // Test case 2
    TypeRestriction restriction2 = new TypeRestriction(null, false);
    FlowScope result2 = interpreter.restrictParameter(parameter, restriction2, blindScope);
    assertNull(result2);
  }

  public void testGetTypeIfRefinable() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FlowScope blindScope = new FlowScope();
    Node parameter = new Node(Node.STRING, "param");

    // Test case 1
    JSType type1 = interpreter.getTypeIfRefinable(parameter, blindScope);
    assertNotNull(type1);

    // Test case 2
    JSType type2 = interpreter.getTypeIfRefinable(parameter, blindScope);
    assertNotNull(type2);
  }

  @Test
  public void testGetRestrictedWithoutUndefined() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType type = new JSType();
    JSType result = interpreter.getRestrictedWithoutUndefined(type);
    assertNotNull(result);
  }

  @Test
  public void testGetRestrictedWithoutNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType type = new JSType();
    JSType result = interpreter.getRestrictedWithoutNull(type);
    assertNotNull(result);
  }

  @Test
  public void testGetRestrictedByTypeOfResult() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType type = new JSType();
    JSType result1 = interpreter.getRestrictedByTypeOfResult(type, "string", true);
    assertNotNull(result1);
    JSType result2 = interpreter.getRestrictedByTypeOfResult(type, "boolean", true);
    assertNotNull(result2);
    JSType result3 = interpreter.getRestrictedByTypeOfResult(type, "number", true);
    assertNotNull(result3);
    JSType result4 = interpreter.getRestrictedByTypeOfResult(type, "function", true);
    assertNotNull(result4);
    JSType result5 = interpreter.getRestrictedByTypeOfResult(type, "undefined", false);
    assertNull(result5);
  }
}