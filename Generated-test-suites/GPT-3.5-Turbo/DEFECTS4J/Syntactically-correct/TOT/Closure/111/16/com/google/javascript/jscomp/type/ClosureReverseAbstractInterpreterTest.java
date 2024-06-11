import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node node = new Node();
    ObjectType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    node.setType(objectType);
    objectType.setSubtype(arrayType);
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(node.getType());
    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node node = new Node();
    ObjectType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    node.setType(objectType);
    objectType.setSubtype(arrayType);
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(node.getType());
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node node = new Node();
    ObjectType objectType = new ObjectType();
    JSType result = interpreter.restrictToObjectVisitor.caseObjectType(node.getType());
    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node node = new Node();
    ObjectType objectType = new ObjectType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(node.getType());
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return typeRegistry.createUnionType(
              typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN), typeRegistry.getNativeType(NULL_VOID));
        } else {
          return null;
        }
      }
    };
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);
  }

}