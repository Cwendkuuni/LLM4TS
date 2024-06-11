import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Typical use case: The input type is an object type
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToArrayVisitor.apply(objectType);
    assertNull(result);

    // Edge case: The input type is a subtype of Array
    JSType arrayType = new ArrayType();
    result = interpreter.restrictToArrayVisitor.apply(arrayType);
    assertNotNull(result);

    // Error scenario: The input type is a top type
    JSType topType = new TopType();
    result = interpreter.restrictToArrayVisitor.apply(topType);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Typical use case: The input type is an object type
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToNotArrayVisitor.apply(objectType);
    assertNotNull(result);

    // Edge case: The input type is a subtype of Array
    JSType arrayType = new ArrayType();
    result = interpreter.restrictToNotArrayVisitor.apply(arrayType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Typical use case: The input type is an object type
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToObjectVisitor.apply(objectType);
    assertNotNull(result);

    // Edge case: The input type is a function type
    JSType functionType = new FunctionType();
    result = interpreter.restrictToObjectVisitor.apply(functionType);
    assertNotNull(result);

    // Error scenario: The input type is a top type
    JSType topType = new TopType();
    result = interpreter.restrictToObjectVisitor.apply(topType);
    assertNotNull(result);

    // Error scenario: The input type is null
    JSType nullType = new NullType();
    result = interpreter.restrictToObjectVisitor.apply(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Typical use case: The input type is an object type
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToNotObjectVisitor.apply(objectType);
    assertNull(result);

    // Edge case: The input type is a function type
    JSType functionType = new FunctionType();
    result = interpreter.restrictToNotObjectVisitor.apply(functionType);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Typical use case: The condition is a valid goog method call
    Node condition = createGoogMethodCall("isDef", "param");
    FlowScope blindScope = createBlindScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);

    // Edge case: The condition is a valid goog method call with unknown outcome
    condition = createGoogMethodCall("isDef", "param");
    blindScope = createBlindScope();
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);

    // Error scenario: The condition is not a valid goog method call
    condition = createInvalidMethodCall("param");
    blindScope = createBlindScope();
    outcome = true;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);

  }

  private Node createGoogMethodCall(String method, String param) {
    Node root = new Node();
    Node callNode = new Node();
    Node callee = new Node();
    Node paramNode = new Node();
    callee.setType(Node.GETPROP);
    callee.setString("goog");
    paramNode.setType(Node.NAME);
    paramNode.setString(param);
    callNode.setType(Node.CALL);
    callNode.addChildToBack(callee);
    callNode.addChildToBack(paramNode);
    root.addChildToBack(callNode);
    return root;
  }

  private Node createInvalidMethodCall(String param) {
    Node root = new Node();
    Node callNode = new Node();
    Node callee = new Node();
    Node paramNode = new Node();
    callee.setType(Node.GETPROP);
    callee.setString("invalid");
    paramNode.setType(Node.NAME);
    paramNode.setString(param);
    callNode.setType(Node.CALL);
    callNode.addChildToBack(callee);
    callNode.addChildToBack(paramNode);
    root.addChildToBack(callNode);
    return root;
  }

  private FlowScope createBlindScope() {
    return new FlowScope();
  }
}