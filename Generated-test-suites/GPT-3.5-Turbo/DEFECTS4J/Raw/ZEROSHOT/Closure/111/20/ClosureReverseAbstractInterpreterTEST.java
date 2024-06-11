import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ObjectType objectType = typeRegistry.getObjectType();
    JSType arrayType = typeRegistry.getArrayType();
    ObjectType restrictedArrayType = typeRegistry.createRestrictedType(arrayType);
    
    ClosureReverseAbstractInterpreter.Visitor<JSType> restrictToArrayVisitor = 
      closure.getRestrictToArrayVisitor();
    JSType result1 = restrictToArrayVisitor.caseTopType(objectType);
    assertNull(result1);
    
    JSType result2 = restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(arrayType, result2);
    
    JSType result3 = restrictToArrayVisitor.caseFunctionType(objectType);
    assertNull(result3);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ObjectType objectType = typeRegistry.getObjectType();
    JSType arrayType = typeRegistry.getArrayType();
    
    ClosureReverseAbstractInterpreter.Visitor<JSType> restrictToNotArrayVisitor = 
      closure.getRestrictToNotArrayVisitor();
    JSType result1 = restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(result1);
    
    JSType result2 = restrictToNotArrayVisitor.caseObjectType(arrayType);
    assertEquals(objectType, result2);
    
    JSType result3 = restrictToNotArrayVisitor.caseFunctionType(objectType);
    assertNull(result3);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ObjectType objectType = typeRegistry.getObjectType();
    FunctionType functionType = typeRegistry.getFunctionType();
    
    ClosureReverseAbstractInterpreter.Visitor<JSType> restrictToObjectVisitor =
      closure.getRestrictToObjectVisitor();
    JSType result1 = restrictToObjectVisitor.caseTopType(objectType);
    assertEquals(typeRegistry.getNoObjectType(), result1);
    
    JSType result2 = restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(objectType, result2);
    
    JSType result3 = restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(functionType, result3);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ObjectType objectType = typeRegistry.getObjectType();
    FunctionType functionType = typeRegistry.getFunctionType();
    
    ClosureReverseAbstractInterpreter.Visitor<JSType> restrictToNotObjectVisitor =
      closure.getRestrictToNotObjectVisitor();
    JSType result1 = restrictToNotObjectVisitor.caseAllType();
    assertNotNull(result1);
    
    JSType result2 = restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(result2);
    
    JSType result3 = restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(result3);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = closure.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter closure = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>();
    boolean outcome = true;
    
    FlowScope result = closure.restrictParameter(parameter, type, blindScope, 
      restriction, outcome);
    assertNotNull(result);
  }
  
  @Test
  public void testTypeRestrictionConstructor() {
    JSType type = new JSType();
    boolean outcome = true;
    
    TypeRestriction restriction = new TypeRestriction(type, outcome);
    assertEquals(type, restriction.type);
    assertEquals(outcome, restriction.outcome);
  }
  
}