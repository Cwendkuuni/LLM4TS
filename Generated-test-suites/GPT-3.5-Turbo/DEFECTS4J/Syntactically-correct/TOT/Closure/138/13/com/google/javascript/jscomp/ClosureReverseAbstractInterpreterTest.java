import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito.*;

public class ClosureReverseAbstractInterpreterTest {

    /**
     * Test for the restrictToArrayVisitor method when goog.isArray returns true.
     */
    @Test
    public void testRestrictToArrayVisitor() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        JSType topType = mock(JSType.class);
        ObjectType type = mock(ObjectType.class);
        JSType arrayType = mock(JSType.class);
        when(closureReverseAbstractInterpreter.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);
        when(arrayType.isSubtype(type)).thenReturn(true);
        
        // Test
        JSType result = closureReverseAbstractInterpreter.restrictToArrayVisitor.caseTopType(topType);
        
        // Verify
        assertEquals(arrayType, result);
    }
    
    /**
     * Test for the restrictToArrayVisitor method when goog.isArray returns false.
     */
    @Test
    public void testRestrictToNotArrayVisitor() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        ObjectType type = mock(ObjectType.class);
        JSType arrayType = mock(JSType.class);
        when(closureReverseAbstractInterpreter.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);
        when(type.isSubtype(arrayType)).thenReturn(false);
        
        // Test
        JSType result = closureReverseAbstractInterpreter.restrictToNotArrayVisitor.caseObjectType(type);
        
        // Verify
        assertEquals(type, result);
    }
    
    /**
     * Test for the restrictToObjectVisitor method when goog.isObject returns true.
     * This includes functions, but not null.
     */
    @Test
    public void testRestrictToObjectVisitor() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        JSType topType = mock(JSType.class);
        ObjectType type = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);

        // Test
        JSType result1 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseTopType(topType);
        JSType result2 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseObjectType(type);
        JSType result3 = closureReverseAbstractInterpreter.restrictToObjectVisitor.caseFunctionType(functionType);
        
        // Verify
        assertEquals(closureReverseAbstractInterpreter.getNativeType(NO_OBJECT_TYPE), result1);
        assertEquals(type, result2);
        assertEquals(functionType, result3);
    }

    /**
     * Test for the restrictToNotObjectVisitor method when goog.isObject returns false.
     */
    @Test
    public void testRestrictToNotObjectVisitor() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        ObjectType type = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);

        // Test
        JSType result1 = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseObjectType(type);
        JSType result2 = closureReverseAbstractInterpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
        
        // Verify
        assertNull(result1);
        assertNull(result2);
    }
    
    /**
     * Test for the getPreciserScopeKnowingConditionOutcome method.
     */
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        boolean outcome = true;
        
        // Test
        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        // Verify
        assertNotNull(result);
    }
    
    /**
     * Test for the restrictParameter method.
     */
    @Test
    public void testRestrictParameter() {
        // Setup
        ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = mock(Node.class);
        JSType type = mock(JSType.class);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        boolean outcome = true;
        
        // Test
        FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify
        assertNotNull(result);
    }
    
    /**
     * Test for the TypeRestriction method.
     */
    @Test
    public void testTypeRestriction() {
        // Setup
        JSType type = mock(JSType.class);
        boolean outcome = true;
        
        // Test
        ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
        
        // Verify
        assertEquals(type, typeRestriction.type);
        assertEquals(outcome, typeRestriction.outcome);
    }
}