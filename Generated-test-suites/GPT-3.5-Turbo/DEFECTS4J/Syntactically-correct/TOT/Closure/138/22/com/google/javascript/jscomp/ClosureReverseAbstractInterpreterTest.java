import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    JSType result1 = c.restrictToArrayVisitor.caseTopType(JSTypeRegistry.TOP);
    assertEquals(JSTypeRegistry.TOP, result1);

    ObjectType objType = new ObjectType();
    JSType result2 = c.restrictToArrayVisitor.caseObjectType(objType);
    assertEquals(JSTypeRegistry.ARRAY_TYPE, result2);

    JSType result3 = c.restrictToArrayVisitor.caseObjectType(JSTypeRegistry.UNKNOWN_TYPE);
    assertNull(result3);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    ObjectType objType1 = new ObjectType();
    JSType result1 = c.restrictToNotArrayVisitor.caseObjectType(objType1);
    assertNull(result1);

    ObjectType objType2 = new ObjectType();
    objType2.addSuperType(JSTypeRegistry.ARRAY_TYPE);
    JSType result2 = c.restrictToNotArrayVisitor.caseObjectType(objType2);
    assertNull(result2);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    ObjectType objType1 = new ObjectType();
    JSType result1 = c.restrictToObjectVisitor.caseObjectType(objType1);
    assertEquals(objType1, result1);

    FunctionType funcType = new FunctionType(null);
    JSType result2 = c.restrictToObjectVisitor.caseFunctionType(funcType);
    assertEquals(funcType, result2);

    JSType result3 = c.restrictToObjectVisitor.caseTopType(JSTypeRegistry.TOP);
    assertEquals(JSTypeRegistry.NO_OBJECT_TYPE, result3);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    ObjectType objType = new ObjectType();
    JSType result1 = c.restrictToNotObjectVisitor.caseObjectType(objType);
    assertNull(result1);

    FunctionType funcType = new FunctionType(null);
    JSType result2 = c.restrictToNotObjectVisitor.caseFunctionType(funcType);
    assertNull(result2);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    Node condition1 = new Node(Token.CALL,
                              new Node(Token.GETPROP),
                              new Node(Token.NAME, "goog", null),
                              new Node(Token.STRING, "isDef"),
                              new Node(Token.STRING, "param"));

    FlowScope blindScope1 = new FlowScope();
    FlowScope result1 = c.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, true);
    assertNotNull(result1);

    FlowScope blindScope2 = new FlowScope();
    FlowScope result2 = c.getPreciserScopeKnowingConditionOutcome(condition1, blindScope2, false);
    assertEquals(blindScope2, result2);
  }

  @Test
  public void testApply() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    TypeRestriction tr1 = new TypeRestriction(JSTypeRegistry.STRING_TYPE, true);
    JSType result1 = c.restricters.get("isString").apply(tr1);
    assertEquals(JSTypeRegistry.STRING_TYPE, result1);

    TypeRestriction tr2 = new TypeRestriction(JSTypeRegistry.STRING_TYPE, false);
    JSType result2 = c.restricters.get("isString").apply(tr2);
    assertNull(result2);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter c = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node(Token.NAME, "param", null);
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();

    TypeRestriction tr1 = new TypeRestriction(type, true);
    FlowScope result1 = c.restrictParameter(parameter, type, blindScope, tr1, true);
    assertNotNull(result1);

    TypeRestriction tr2 = new TypeRestriction(type, false);
    FlowScope result2 = c.restrictParameter(parameter, type, blindScope, tr2, false);
    assertEquals(blindScope, result2);
  }
}