import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_typicalCase() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType topType = new JSType();
    JSType arrayType = new JSType("Array");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Array", arrayType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToArrayVisitor();
    topType.setSubtype(arrayType);
    JSType result = topType.visit(visitor);
    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToArrayVisitor_nullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType nullType = new JSType("Null");
    JSType nullArrayType = new JSType("Array");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Null", nullType);
    registry.registerNativeType("Array", nullArrayType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToArrayVisitor();
    nullType.setSubtype(nullArrayType);
    JSType result = nullType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_typicalCase() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType objectType = new JSType("Object");
    JSType arrayType = new JSType("Array");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Object", objectType);
    registry.registerNativeType("Array", arrayType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToNotArrayVisitor();
    objectType.setSubtype(arrayType);
    JSType result = objectType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_nullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType nullType = new JSType("Null");
    JSType nullArrayType = new JSType("Array");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Null", nullType);
    registry.registerNativeType("Array", nullArrayType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToNotArrayVisitor();
    nullType.setSubtype(nullArrayType);
    JSType result = nullType.visit(visitor);
    assertEquals(nullType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_typicalCase() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType objectType = new JSType("Object");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Object", objectType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToObjectVisitor();
    objectType.setSubtype(objectType);
    JSType result = objectType.visit(visitor);
    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_nullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType nullType = new JSType("Null");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Null", nullType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToObjectVisitor();
    JSType result = nullType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_typicalCase() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType objectType = new JSType("Object");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Object", objectType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToNotObjectVisitor();
    JSType result = objectType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_nullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType nullType = new JSType("Null");
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerNativeType("Null", nullType);
    InterpreterVisitor<JSType> visitor = interpreter.getRestrictToNotObjectVisitor();
    JSType result = nullType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_typicalCase() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = new Node(CALL);
    Node param = new Node(NAME, "param");
    condition.addChildToBack(new Node(GETPROP));
    condition.getChildAtIndex(0).addChildToBack(new Node(NAME, "goog"));
    condition.getChildAtIndex(0).addChildToBack(new Node(STRING, "isArray"));
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = new Node(CALL);
    Node param = new Node(NAME, "param");
    condition.addChildToBack(new Node(GETPROP));
    condition.getChildAtIndex(0).addChildToBack(new Node(NAME, "goog"));
    condition.getChildAtIndex(0).addChildToBack(new Node(STRING, "notExisting"));
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

}