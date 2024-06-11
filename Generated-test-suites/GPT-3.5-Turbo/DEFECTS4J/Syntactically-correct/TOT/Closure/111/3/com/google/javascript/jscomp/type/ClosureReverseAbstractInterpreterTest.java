import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.CodingConvention;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Test case for when the condition is not a call
    Node condition = new Node(Node.Type.VAR);
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), new JSTypeRegistry());
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
    
    assertEquals(blindScope, result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome2() {
    // Test case for when the condition is a call, but not a proper goog call
    Node condition = new Node(Node.Type.CALL, new Node(Node.Type.IDENT, new Node(Node.Type.NAME, "test")),
        new Node(Node.Type.STRING, "isString"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), new JSTypeRegistry());
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
    
    assertEquals(blindScope, result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome3() {
    // Test case for when the condition is a proper goog call
    Node condition = new Node(Node.Type.CALL, new Node(Node.Type.GETPROP,
        new Node(Node.Type.NAME, "goog"), new Node(Node.Type.STRING, "isString")),
        new Node(Node.Type.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), new JSTypeRegistry());
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
    
    // Check if the parameter is declared in the new scope with the correct type
    JSType expectedType = new JSTypeRegistry().getRestrictedByTypeOfResult(null, "string", true);
    assertTrue(result.isDeclared("param"));
    assertEquals(expectedType, result.getTypeOfName("param"));
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = new Node(Node.Type.NAME, "param");
    JSType type = null;
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = (p) -> {
      if (p.outcome) {
        return new JSTypeRegistry().getRestrictedByTypeOfResult(p.type, "string", p.outcome);
      } else {
        return new JSTypeRegistry().getRestrictedByTypeOfResult(p.type, "number", p.outcome);
      }
    };
    boolean outcome = true;
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), new JSTypeRegistry());
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction,
        outcome);
    
    // Check if the parameter is declared in the new scope with the correct type
    JSType expectedType = new JSTypeRegistry().getRestrictedByTypeOfResult(null, "string", outcome);
    assertTrue(result.isDeclared("param"));
    assertEquals(expectedType, result.getTypeOfName("param"));
  }

  @Test
  public void testRestrictParameter2() {
    Node parameter = new Node(Node.Type.NAME, "param");
    JSType type = null;
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = (p) -> {
      if (p.outcome) {
        return new JSTypeRegistry().getRestrictedByTypeOfResult(p.type, "string", p.outcome);
      } else {
        return new JSTypeRegistry().getRestrictedByTypeOfResult(p.type, "number", p.outcome);
      }
    };
    boolean outcome = false;
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), new JSTypeRegistry());
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction,
        outcome);
    
    // Check if the parameter is declared in the new scope with the correct type
    JSType expectedType = new JSTypeRegistry().getRestrictedByTypeOfResult(null, "number", outcome);
    assertTrue(result.isDeclared("param"));
    assertEquals(expectedType, result.getTypeOfName("param"));
  }
}