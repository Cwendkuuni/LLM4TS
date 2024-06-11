import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
   
   @Test
   public void testRestrictToArrayVisitor_caseTopType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      JSType topType = null;
      JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);
      assertNull(result);
   }
   
   @Test
   public void testRestrictToArrayVisitor_caseObjectType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      ObjectType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseObjectType(type);
      assertNull(result);
   }
   
   
   @Test
   public void testRestrictToNotArrayVisitor_caseObjectType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      ObjectType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(type);
      assertNull(result);
   }
   
   
   @Test
   public void testRestrictToObjectVisitor_caseTopType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      JSType topType = null;
      JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);
      assertNotNull(result);
   }
   
   
   @Test
   public void testRestrictToObjectVisitor_caseObjectType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      ObjectType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(type);
      assertNotNull(result);
   }
   
   @Test
   public void testRestrictToObjectVisitor_caseFunctionType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      FunctionType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(type);
      assertNotNull(result);
   }
   
   @Test
   public void testRestrictToNotObjectVisitor_caseObjectType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      ObjectType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(type);
      assertNull(result);
   }
   
   @Test
   public void testRestrictToNotObjectVisitor_caseFunctionType() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      FunctionType type = null;
      JSType result = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(type);
      assertNull(result);
   }
   
   @Test 
   public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome_call() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      Node condition = new Node();
      FlowScope blindScope = new FlowScope();
      boolean outcome = true;
      FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      assertNotNull(result);
   }
   
   @Test 
   public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome_false() {
      ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
      Node condition = new Node();
      FlowScope blindScope = new FlowScope();
      boolean outcome = false;
      FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      assertNotNull(result);
   }
   
   
}