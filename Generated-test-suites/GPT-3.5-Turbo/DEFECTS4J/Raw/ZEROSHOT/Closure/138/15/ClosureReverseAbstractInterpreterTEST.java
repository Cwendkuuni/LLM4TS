import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Test case 1
    Node condition1 = new Node(Token.CALL);
    Node callee1 = new Node(Token.GETPROP);
    Node left1 = new Node(Token.NAME, "goog");
    Node right1 = new Node(Token.STRING, "isDef");
    Node param1 = new Node(Token.NAME, "param");
    callee1.addChildToFront(left1);
    callee1.addChildToBack(right1);
    condition1.addChildToFront(callee1);
    condition1.addChildToBack(param1);
    FlowScope blindScope1 = new FlowScope();
    boolean outcome1 = true;
    FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, outcome1);
    assertNotNull(result1);

    // Test case 2
    Node condition2 = new Node(Token.CALL);
    Node callee2 = new Node(Token.GETPROP);
    Node left2 = new Node(Token.NAME, "goog");
    Node right2 = new Node(Token.STRING, "isString");
    Node param2 = new Node(Token.NAME, "param");
    callee2.addChildToFront(left2);
    callee2.addChildToBack(right2);
    condition2.addChildToFront(callee2);
    condition2.addChildToBack(param2);
    FlowScope blindScope2 = new FlowScope();
    boolean outcome2 = false;
    FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2);
    assertNotNull(result2);
  }
}