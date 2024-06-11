import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {
    
    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        JSType type = interpreter.getNativeType(ARRAY_TYPE);
        JSType result = restrictToArrayVisitor.caseObjectType(type);
        assertEquals(type, result);
        
        // Edge case
        type = interpreter.getNativeType(OBJECT_TYPE);
        result = restrictToArrayVisitor.caseObjectType(type);
        assertNull(result);
    }
    
    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        JSType type = interpreter.getNativeType(OBJECT_TYPE);
        JSType result = restrictToNotArrayVisitor.caseObjectType(type);
        assertEquals(type, result);
        
        // Edge case
        type = interpreter.getNativeType(ARRAY_TYPE);
        result = restrictToNotArrayVisitor.caseObjectType(type);
        assertNull(result);
    }
    
    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        JSType type = interpreter.getNativeType(OBJECT_TYPE);
        JSType result = restrictToObjectVisitor.caseObjectType(type);
        assertEquals(type, result);
        
        // Edge case
        type = interpreter.getNativeType(NULL_TYPE);
        result = restrictToObjectVisitor.caseObjectType(type);
        assertNull(result);
    }
    
    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        JSType type = interpreter.getNativeType(NUMBER_STRING_BOOLEAN);
        JSType result = restrictToNotObjectVisitor.caseObjectType(type);
        assertNull(result);
        
        // Edge case
        type = interpreter.getNativeType(OBJECT_TYPE);
        result = restrictToNotObjectVisitor.caseObjectType(type);
        assertNull(result);
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertNotNull(result);
        
        // Edge case
        condition = null;
        blindScope = null;
        outcome = false;
        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertNotNull(result);
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        // Typical use case
        Node parameter = new Node();
        JSType type = interpreter.getNativeType(OBJECT_TYPE);
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = restricters.get("isDef");
        boolean outcome = true;
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result);
        
        // Edge case
        parameter = null;
        type = interpreter.getNativeType(VOID_TYPE);
        blindScope = null;
        restriction = null;
        outcome = false;
        result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNull(result);
    }
}