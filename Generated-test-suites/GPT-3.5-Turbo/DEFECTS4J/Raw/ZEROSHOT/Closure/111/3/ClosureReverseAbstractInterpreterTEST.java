import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);
    assertEquals(topType, result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType arrayType = new JSType();
    JSType type = new JSType();
    type.addSupertype(arrayType);
    JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseObjectType(type);
    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType arrayType = new JSType();
    JSType type = new JSType();
    type.addSupertype(arrayType);
    JSType result = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(type);
    assertEquals(null, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(NO_OBJECT_TYPE, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType type = new JSType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(type);
    assertEquals(type, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    FunctionType type = new FunctionType();
    JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(type);
    assertEquals(type, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseAllType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    JSType unionType = new JSType();
    unionType.addSupertype(type1);
    unionType.addSupertype(type2);
    JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseAllType();
    assertEquals(unionType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    ObjectType type = new ObjectType();
    JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(type);
    assertEquals(null, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    FunctionType type = new FunctionType();
    JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(type);
    assertEquals(null, result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome_withCallCondition() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node("call");
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter1 = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = closureReverseAbstractInterpreter1.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(null, result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_restrictParameter() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node("parameter");
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return p.type;
        } else {
          return null;
        }
      }
    };
    FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(null, result);
  }

  @Test
  public void testTypeRestriction_constructor() {
    JSType type = new JSType();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
    assertEquals(type, typeRestriction.type);
    assertEquals(outcome, typeRestriction.outcome);
  }
}