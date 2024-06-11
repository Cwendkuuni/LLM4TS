import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType objectType = new ObjectType();
        JSType arrayType = new ArrayType(objectType);
        
        // Typical use case where goog.isArray returns true
        JSType result1 = interpreter.restrictToArrayVisitor.visit(objectType);
        assertEquals(arrayType, result1);
        
        // Edge case where goog.isArray returns false
        JSType result2 = interpreter.restrictToArrayVisitor.visit(arrayType);
        assertNull(result2);
        
        // Error scenario where the type is null
        JSType result3 = interpreter.restrictToArrayVisitor.visit(null);
        assertNull(result3);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType objectType = new ObjectType();
        JSType arrayType = new ArrayType(objectType);
        
        // Typical use case where goog.isArray returns false
        JSType result1 = interpreter.restrictToNotArrayVisitor.visit(objectType);
        assertEquals(objectType, result1);
        
        // Edge case where goog.isArray returns true
        JSType result2 = interpreter.restrictToNotArrayVisitor.visit(arrayType);
        assertNull(result2);
        
        // Error scenario where the type is null
        JSType result3 = interpreter.restrictToNotArrayVisitor.visit(null);
        assertNull(result3);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType objectType = new ObjectType();
        JSType functionType = new FunctionType();
        
        // Typical use case where goog.isObject returns true
        JSType result1 = interpreter.restrictToObjectVisitor.visit(objectType);
        assertEquals(objectType, result1);
        
        // Edge case where goog.isObject returns true for a function type
        JSType result2 = interpreter.restrictToObjectVisitor.visit(functionType);
        assertEquals(functionType, result2);
        
        // Error scenario where the type is null
        JSType result3 = interpreter.restrictToObjectVisitor.visit(null);
        assertEquals(NO_OBJECT_TYPE, result3);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        JSType objectType = new ObjectType();
        JSType functionType = new FunctionType();
        
        // Typical use case where goog.isObject returns false
        JSType result1 = interpreter.restrictToNotObjectVisitor.visit(objectType);
        assertNull(result1);
        
        // Edge case where goog.isObject returns false for a function type
        JSType result2 = interpreter.restrictToNotObjectVisitor.visit(functionType);
        assertNull(result2);
        
        // Error scenario where the type is null
        JSType result3 = interpreter.restrictToNotObjectVisitor.visit(null);
        assertEquals(NUMBER_STRING_BOOLEAN, result3);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node condition1 = new Node("goog.isArray(x)");
        Node condition2 = new Node("goog.isString(x)");
        Node condition3 = new Node("goog.isDefAndNotNull(x)");
        FlowScope blindScope = new FlowScope();
        
        // Typical use case where outcome is true
        FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, true);
        assertEquals(arrayType, result1);
        
        // Edge case where outcome is false
        FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, false);
        assertNull(result2);
        
        // Error scenario where the condition is not a valid call node
        FlowScope result3 = interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope, true);
        assertEquals(result3, blindScope);
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node("x");
        JSType type = new ObjectType();
        Function<TypeRestriction, JSType> restriction1 = interpreter.restricters.get("isDef");
        Function<TypeRestriction, JSType> restriction2 = interpreter.restricters.get("isArray");
        boolean outcome1 = true;
        boolean outcome2 = false;
        FlowScope blindScope = new FlowScope();
        
        // Typical use case where restricter is isDef and outcome is true
        FlowScope result1 = interpreter.restrictParameter(parameter, type, blindScope, restriction1, outcome1);
        assertEquals(getRestrictedWithoutUndefined(type), result1);
        
        // Edge case where restricter is isArray and outcome is false
        FlowScope result2 = interpreter.restrictParameter(parameter, type, blindScope, restriction2, outcome2);
        assertNull(result2);
        
        // Error scenario where the type is null
        FlowScope result3 = interpreter.restrictParameter(parameter, null, blindScope, restriction1, outcome1);
        assertEquals(getNativeType(VOID_TYPE).getGreatestSubtype(null), result3);
    }

}