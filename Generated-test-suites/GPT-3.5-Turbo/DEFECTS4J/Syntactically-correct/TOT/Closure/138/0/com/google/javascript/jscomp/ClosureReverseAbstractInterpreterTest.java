import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {
  
  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(new ObjectType());
    
    assertNotNull(result);
    assertTrue(result.isSubtype(interpreter.getNativeType(ARRAY_TYPE)));
  }
  
  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(new ObjectType());
    
    assertNull(result);
  }
  
  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    JSType result = interpreter.restrictToObjectVisitor.caseObjectType(new ObjectType());
    
    assertNotNull(result);
    assertTrue(result.isSubtype(interpreter.getNativeType(NO_OBJECT_TYPE)));
  }
  
  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(new ObjectType());
    
    assertNull(result);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    
    assertNotNull(result);
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    parameter.setType(NAME);
    JSType type = new ObjectType();
    FlowScope blindScope = new FlowScope();
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return p.type;
        } else {
          return null;
        }
      }
    }, true);
    
    assertNotNull(result);
    assertTrue(result.hasNameInScope(parameter));
  }
  
  @Test
  public void testTypeRestriction() {
    JSType type = new ObjectType();
    boolean outcome = true;
    
    TypeRestriction restriction = new TypeRestriction(type, outcome);
    
    assertNotNull(restriction);
    assertEquals(restriction.type, type);
    assertEquals(restriction.outcome, outcome);
  }
}