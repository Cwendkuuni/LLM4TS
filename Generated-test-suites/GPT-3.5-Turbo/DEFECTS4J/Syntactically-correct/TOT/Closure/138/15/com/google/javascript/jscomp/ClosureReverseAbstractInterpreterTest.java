import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ObjectType objectType = typeRegistry.getObjectType();
        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        
        JSType result1 = interpreter.restrictToArrayVisitor.caseTopType(objectType);
        assertNull(result1);
        
        JSType result2 = interpreter.restrictToArrayVisitor.caseObjectType(objectType);
        assertEquals(arrayType, result2);
    }
    
    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ObjectType objectType = typeRegistry.getObjectType();
        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        
        JSType result1 = interpreter.restrictToNotArrayVisitor.caseObjectType(objectType);
        assertNull(result1);
        
        JSType result2 = interpreter.restrictToNotArrayVisitor.caseObjectType(arrayType);
        assertEquals(arrayType, result2);
    }
    
    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ObjectType objectType = typeRegistry.getObjectType();
        JSType noObjectType = typeRegistry.getNativeType(NO_OBJECT_TYPE);
        FunctionType functionType = new FunctionType();
        
        JSType result1 = interpreter.restrictToObjectVisitor.caseTopType(objectType);
        assertEquals(noObjectType, result1);
        
        JSType result2 = interpreter.restrictToObjectVisitor.caseObjectType(objectType);
        assertEquals(objectType, result2);
        
        JSType result3 = interpreter.restrictToObjectVisitor.caseFunctionType(functionType);
        assertEquals(functionType, result3);
    }
    
    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ObjectType objectType = typeRegistry.getObjectType();
        FunctionType functionType = new FunctionType();
        
        JSType result1 = interpreter.restrictToNotObjectVisitor.caseObjectType(objectType);
        assertNull(result1);
        
        JSType result2 = interpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
        assertNull(result2);
    }
    
    @Test
    public void testApplyIsDef() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, true);
        
        JSType result = interpreter.restricters.get("isDef").apply(restriction);
        assertEquals(interpreter.getRestrictedWithoutUndefined(getObjectType), result);
    }
    
    @Test
    public void testApplyIsNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, false);
        
        JSType result = interpreter.restricters.get("isNull").apply(restriction);
        assertEquals(interpreter.getNativeType(NULL_TYPE), result);
    }
    
    @Test
    public void testApplyIsDefAndNotNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, true);
        
        JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertEquals(interpreter.getRestrictedWithoutUndefined(interpreter.getRestrictedWithoutNull(getObjectType)), result);
    }
    
    @Test
    public void testApplyIsString() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, true);
        
        JSType result = interpreter.restricters.get("isString").apply(restriction);
        assertEquals(interpreter.getRestrictedByTypeOfResult(getObjectType, "string", true), result);
    }
    
    @Test
    public void testApplyIsBoolean() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, false);
        
        JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
        assertEquals(interpreter.getRestrictedByTypeOfResult(getObjectType, "boolean", false), result);
    }
    
    @Test
    public void testApplyIsNumber() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, true);
        
        JSType result = interpreter.restricters.get("isNumber").apply(restriction);
        assertEquals(interpreter.getRestrictedByTypeOfResult(getObjectType, "number", true), result);
    }
    
    @Test
    public void testApplyIsFunction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction = new TypeRestriction(getObjectType, false);
        
        JSType result = interpreter.restricters.get("isFunction").apply(restriction);
        assertEquals(interpreter.getRestrictedByTypeOfResult(getObjectType, "function", false), result);
    }
    
    @Test
    public void testApplyIsArray() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction1 = new TypeRestriction(null, true);
        TypeRestriction restriction2 = new TypeRestriction(null, false);
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        JSType arrayType = typeRegistry.getNativeType(ARRAY_TYPE);
        
        JSType result1 = interpreter.restricters.get("isArray").apply(restriction1);
        assertEquals(arrayType, result1);
        
        JSType result2 = interpreter.restricters.get("isArray").apply(restriction2);
        assertNull(result2);
    }
    
    @Test
    public void testApplyIsObject() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        TypeRestriction restriction1 = new TypeRestriction(null, true);
        TypeRestriction restriction2 = new TypeRestriction(null, false);
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        JSType objectType = typeRegistry.getNativeType(OBJECT_TYPE);
        
        JSType result1 = interpreter.restricters.get("isObject").apply(restriction1);
        assertEquals(objectType, result1);
        
        JSType result2 = interpreter.restricters.get("isObject").apply(restriction2);
        assertNull(result2);
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        FlowScope blindScope = new FlowScope(typeRegistry);
        Node condition = new Node();
        condition.setType(CALL);
        Node callee = new Node();
        callee.setType(GETPROP);
        condition.addChildToBack(callee);
        Node left = new Node();
        left.setType(NAME);
        left.setString("goog");
        Node right = new Node();
        right.setType(STRING);
        right.setString("isDef");
        callee.addChildToFront(left);
        callee.addChildToBack(right);
        Node param = new Node();
        param.setQualifiedName(true);
        condition.addChildToBack(param);
        
        FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNotNull(result1);
        
        FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNotNull(result2);
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node();
        JSType type = null;
        FlowScope blindScope = new FlowScope(typeRegistry);
        Function<TypeRestriction, JSType> restriction = interpreter.restricters.get("isArray");
        boolean outcome = true;
        
        FlowScope result1 = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result1);
        
        FlowScope result2 = interpreter.restrictParameter(parameter, type, blindScope, restriction, !outcome);
        assertNotNull(result2);
    }

}