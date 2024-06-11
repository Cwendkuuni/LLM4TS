import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;

  @Before
  public void setUp() {
    closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
  }

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    JSType topType = new JSType();
    JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);
    assertNotNull(result);
  }
  
  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    ObjectType type = new ObjectType();
    JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseObjectType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    ObjectType type = new ObjectType();
    JSType result = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(type);
    assertNull(result);
  }
  
  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    JSType topType = new JSType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    ObjectType type = new ObjectType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    FunctionType type = new FunctionType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(type);
    assertNotNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    ObjectType type = new ObjectType();
    JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    FunctionType type = new FunctionType();
    JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(type);
    assertNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return null;
        } else {
          return p.type;
        }
      }
    };
    boolean outcome = false;
    FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);
  }

  private class TypeRestriction {
    private JSType type;
    private boolean outcome;

    public TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }
}