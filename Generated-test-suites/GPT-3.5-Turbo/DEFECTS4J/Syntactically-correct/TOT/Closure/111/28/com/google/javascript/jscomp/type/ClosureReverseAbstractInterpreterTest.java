import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType topType = new JSType();
        ObjectType arrayType = new ObjectType("Array");
        RestrictToArrayVisitor visitor = interpreter.new RestrictToArrayVisitor();
      
        JSType result = visitor.caseTopType(topType);
        assertNull(result);
      
        result = visitor.caseObjectType(arrayType);
        assertEquals(arrayType, result);
    }
  
    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        ObjectType arrayType = new ObjectType("Array");
        RestrictToNotArrayVisitor visitor = interpreter.new RestrictToNotArrayVisitor();
      
        JSType result = visitor.caseObjectType(arrayType);
        assertNull(result);
    }
  
    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType topType = new JSType();
        ObjectType objectType = new ObjectType("Object");
        FunctionType functionType = new FunctionType();
        RestrictToObjectVisitor visitor = interpreter.new RestrictToObjectVisitor();
      
        JSType result = visitor.caseTopType(topType);
        assertEquals(interpreter.getNativeType(NO_OBJECT_TYPE), result);
      
        result = visitor.caseObjectType(objectType);
        assertEquals(objectType, result);
      
        result = visitor.caseFunctionType(functionType);
        assertEquals(functionType, result);
    }
  
    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        ObjectType objectType = new ObjectType("Object");
        FunctionType functionType = new FunctionType();
        RestrictToNotObjectVisitor visitor = interpreter.new RestrictToNotObjectVisitor();
      
        JSType result = visitor.caseAllType();
        assertNotNull(result);
      
        result = visitor.caseObjectType(objectType);
        assertNull(result);
      
        result = visitor.caseFunctionType(functionType);
        assertNull(result);
    }
  
    @Test
    public void testApplyForIsDef() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedWithoutUndefined(restriction.type);
        JSType result = interpreter.restricters.get("isDef").apply(restriction);
        assertEquals(expected, result);
      
        restriction = new TypeRestriction(new JSType(), false);
        expected = restriction.type != null ? interpreter.getNativeType(VOID_TYPE).getGreatestSubtype(restriction.type) : null;
        result = interpreter.restricters.get("isDef").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = restriction.type != null ? interpreter.getNativeType(NULL_TYPE).getGreatestSubtype(restriction.type) : null;
        JSType result = interpreter.restricters.get("isNull").apply(restriction);
        assertEquals(expected, result);
      
        restriction = new TypeRestriction(new JSType(), false);
        expected = interpreter.getRestrictedWithoutNull(restriction.type);
        result = interpreter.restricters.get("isNull").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsDefAndNotNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedWithoutUndefined(interpreter.getRestrictedWithoutNull(restriction.type));
        JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertEquals(expected, result);
      
        restriction = new TypeRestriction(new JSType(), false);
        expected = restriction.type != null ? interpreter.getNativeType(NULL_VOID).getGreatestSubtype(restriction.type) : null;
        result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsString() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedByTypeOfResult(restriction.type, "string", restriction.outcome);
        JSType result = interpreter.restricters.get("isString").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsBoolean() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedByTypeOfResult(restriction.type, "boolean", restriction.outcome);
        JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsNumber() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedByTypeOfResult(restriction.type, "number", restriction.outcome);
        JSType result = interpreter.restricters.get("isNumber").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsFunction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(new JSType(), true);
        JSType expected = interpreter.getRestrictedByTypeOfResult(restriction.type, "function", restriction.outcome);
        JSType result = interpreter.restricters.get("isFunction").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsArray() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType expected = interpreter.getNativeType(ARRAY_TYPE);
        JSType result = interpreter.restricters.get("isArray").apply(restriction);
        assertEquals(expected, result);
      
        restriction = new TypeRestriction(new JSType(), true);
        RestrictToArrayVisitor visitor = interpreter.new RestrictToArrayVisitor();
        expected = visitor.caseObjectType(restriction.type) ? interpreter.getNativeType(ARRAY_TYPE) : null;
        result = interpreter.restricters.get("isArray").apply(restriction);
        assertEquals(expected, result);
      
        RestrictToNotArrayVisitor visitorNotArray = interpreter.new RestrictToNotArrayVisitor();
        expected = visitorNotArray.caseObjectType(restriction.type) ? null : restriction.type;
        result = interpreter.restricters.get("isArray").apply(restriction);
        assertEquals(expected, result);
    }
  
    @Test
    public void testApplyForIsObject() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType expected = interpreter.getNativeType(OBJECT_TYPE);
        JSType result = interpreter.restricters.get("isObject").apply(restriction);
        assertEquals(expected, result);
      
        restriction = new TypeRestriction(new JSType(), true);
        RestrictToObjectVisitor visitor = interpreter.new RestrictToObjectVisitor();
        expected = visitor.caseObjectType(restriction.type) ? restriction.type : interpreter.getNativeType(NO_OBJECT_TYPE);
        JSType result = interpreter.restricters.get("isObject").apply(restriction);
        assertEquals(expected, result);
      
        RestrictToNotObjectVisitor visitorNotObject = interpreter.new RestrictToNotObjectVisitor();
        expected = visitorNotObject.caseObjectType(restriction.type) || visitorNotObject.caseFunctionType(restriction.type) ? null : restriction.type;
        JSType result = interpreter.restricters.get("isObject").apply(restriction);
        assertEquals(expected, result);
    }

}