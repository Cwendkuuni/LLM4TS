
import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Test case for goog.isDef
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FlowScope blindScope = new FlowScope();
    Node condition = new Node(Token.CALL);
    Node callee = new Node(Token.GETPROP);
    Node param = new Node(Token.NAME, "param");
    Node goog = new Node(Token.NAME, "goog");
    Node isDef = new Node(Token.STRING, "isDef");
    goog.addChildToBack(isDef);
    callee.addChildrenToBack(goog, isDef);
    condition.addChildrenToBack(callee, param);
    
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType paramType = scope.getVar(param.getString()).getType();
    assertTrue("Param type should not contain undefined", paramType.getDisplayName().equals("VOID"));
    
    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    paramType = scope.getVar(param.getString()).getType();
    assertTrue("Param type should be void", paramType.getDisplayName().equals("UNDEFINED"));
    
    // Other test cases for different methods can be added in similar manner
  }
}
