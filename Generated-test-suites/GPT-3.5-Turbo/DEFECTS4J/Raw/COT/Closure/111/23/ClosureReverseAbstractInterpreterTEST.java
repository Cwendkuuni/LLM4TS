import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;
    
    // Test when input type is ARRAY_TYPE
    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType result = arrayType.visit(visitor);
    assertEquals(arrayType, result);
    
    // Test when input type is not ARRAY_TYPE
    JSType objectType = getNativeType(OBJECT_TYPE);
    result = objectType.visit(visitor);
    assertNull(result);
  }
  
  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;
    
    // Test when input type is ARRAY_TYPE
    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType result = arrayType.visit(visitor);
    assertNull(result);
    
    // Test when input type is not ARRAY_TYPE
    JSType objectType = getNativeType(OBJECT_TYPE);
    result = objectType.visit(visitor);
    assertEquals(objectType, result);
  }
  
  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;
    
    // Test when input type is OBJECT_TYPE
    JSType objectType = getNativeType(OBJECT_TYPE);
    JSType result = objectType.visit(visitor);
    assertEquals(objectType, result);
    
    // Test when input type is FUNCTION_TYPE
    JSType functionType = getNativeType(FUNCTION_TYPE);
    result = functionType.visit(visitor);
    assertEquals(functionType, result);
    
    // Test when input type is not OBJECT_TYPE or FUNCTION_TYPE
    JSType stringType = getNativeType(STRING_TYPE);
    result = stringType.visit(visitor);
    assertEquals(getNativeType(NO_OBJECT_TYPE), result);
  }
  
  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;
    
    // Test when input type is allType
    JSType allType = getAllType();
    JSType result = allType.visit(visitor);
    assertEquals(typeRegistry.createUnionType(
              getNativeType(NUMBER_STRING_BOOLEAN), getNativeType(NULL_VOID)), result);
    
    // Test when input type is OBJECT_TYPE
    JSType objectType = getNativeType(OBJECT_TYPE);
    result = objectType.visit(visitor);
    assertNull(result);
    
    // Test when input type is FUNCTION_TYPE
    JSType functionType = getNativeType(FUNCTION_TYPE);
    result = functionType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP,
            new Node(Token.NAME, new Node("goog")),
            new Node("isDef")),
        new Node("param"));
    FlowScope blindScope = new FlowScope();
    
    // Test outcome is true
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    // check the scope for the parameter
    assertTrue(scope.isTyped("param"));
    JSType restrictedType = getRestrictedWithoutUndefined(getNativeType(ANY_TYPE));
    assertEquals(restrictedType, scope.getVar("param").getType());
    
    // Test outcome is false
    scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    // check the scope for the parameter
    assertTrue(scope.isTyped("param"));
    JSType voidType = getNativeType(VOID_TYPE);
    assertEquals(voidType.getGreatestSubtype(getNativeType(ANY_TYPE)),
        scope.getVar("param").getType());
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Node parameter = new Node("param");
    JSType type = getNativeType(ANY_TYPE);
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction =
        interpreter.restricters.get("isDef");
    
    // Test outcome is true
    FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope,
        restriction, true);
    // check the scope for the parameter
    assertTrue(scope.isTyped("param"));
    JSType restrictedType = getRestrictedWithoutUndefined(getNativeType(ANY_TYPE));
    assertEquals(restrictedType, scope.getVar("param").getType());
    
    // Test outcome is false
    scope = interpreter.restrictParameter(parameter, type, blindScope,
        restriction, false);
    // check the scope for the parameter
    assertTrue(scope.isTyped("param"));
    JSType voidType = getNativeType(VOID_TYPE);
    assertEquals(voidType.getGreatestSubtype(getNativeType(ANY_TYPE)),
        scope.getVar("param").getType());
  }
}