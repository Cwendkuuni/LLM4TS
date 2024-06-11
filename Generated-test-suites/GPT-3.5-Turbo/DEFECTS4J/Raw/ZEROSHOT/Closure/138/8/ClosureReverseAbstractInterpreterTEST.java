import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = interpreter.restrictToArrayVisitor.caseTopType(topType);
    assertNull(result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType type = new ObjectType();
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType type = new ObjectType();
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = interpreter.restrictToObjectVisitor.caseTopType(topType);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType type = new ObjectType();
    JSType result = interpreter.restrictToObjectVisitor.caseObjectType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FunctionType type = new FunctionType();
    JSType result = interpreter.restrictToObjectVisitor.caseFunctionType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType type = new ObjectType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FunctionType type = new FunctionType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseFunctionType(type);
    assertNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_restrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    TypeRestriction restriction = new TypeRestriction(type, true);
    boolean outcome = true;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);
  }

  @Test
  public void testTypeRestriction_constructor() {
    JSType type = new JSType();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter.TypeRestriction restriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
    assertNotNull(restriction);
  }
}