import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;

  @Before
  public void setUp() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    // Test case 1
    JSType topType = new JSType();
    JSType result1 = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);
    assertNull(result1);

    // Test case 2
    ObjectType type = new ObjectType();
    JSType arrayType = new JSType();
    JSType result2 = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseObjectType(type);
    assertEquals(arrayType, result2);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    // Test case 1
    ObjectType type = new ObjectType();
    JSType arrayType = new JSType();
    JSType result1 = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(type);
    assertNull(result1);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    // Test case 1
    JSType topType = new JSType();
    JSType noObjectType = new JSType();
    JSType result1 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(noObjectType, result1);

    // Test case 2
    ObjectType type = new ObjectType();
    JSType result2 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(type);
    assertEquals(type, result2);

    // Test case 3
    FunctionType functionType = new FunctionType();
    JSType result3 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(functionType, result3);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    // Test case 1
    ObjectType type = new ObjectType();
    JSType result1 = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(type);
    assertNull(result1);

    // Test case 2
    FunctionType functionType = new FunctionType();
    JSType result2 = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(result2);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Test case 1
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result1 = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result1);

    // Test case 2
    Node param = new Node(NodeType.QUALIFIED_NAME);
    Node left = new Node(NodeType.NAME, "goog");
    Node right = new Node(NodeType.STRING, "isDef");
    Node callee = new Node(NodeType.GETPROP, left, right);
    Node call = new Node(NodeType.CALL, callee, param);
    condition = call;
    FlowScope result2 = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result2);
  }

  @Test
  public void testRestrictParameter() {
    // Test case 1
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return new JSType();
        } else {
          return null;
        }
      }
    };
    boolean outcome = true;
    FlowScope result1 = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result1);

    // Test case 2
    outcome = false;
    FlowScope result2 = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(blindScope, result2);
  }
}