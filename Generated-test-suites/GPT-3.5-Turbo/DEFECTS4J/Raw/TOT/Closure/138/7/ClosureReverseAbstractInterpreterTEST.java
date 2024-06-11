import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        // Typical use case: Array type passed
        JSType arrayType = new ObjectType("Array");
        RestrictByTrueTypeOfResultVisitor visitor = new RestrictByTrueTypeOfResultVisitor();
        JSType result = visitor.caseObjectType(arrayType);
        
        assertNotNull(result);
        assertTrue(result.isSubtype(arrayType));
        
        // Edge case: Top type passed
        JSType topType = new TopType();
        result = visitor.caseTopType(topType);
        
        assertNotNull(result);
        assertEquals(topType, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        // Typical use case: Non-array type passed
        JSType nonArrayType = new ObjectType("Object");
        RestrictByFalseTypeOfResultVisitor visitor = new RestrictByFalseTypeOfResultVisitor();
        JSType result = visitor.caseObjectType(nonArrayType);
        
        assertNotNull(result);
        assertFalse(result.isSubtype(new ObjectType("Array")));
        
        // Edge case: Top type passed
        JSType topType = new TopType();
        result = visitor.caseTopType(topType);
        
        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        // Typical use case: Object type passed
        JSType objectType = new ObjectType("Object");
        RestrictByTrueTypeOfResultVisitor visitor = new RestrictByTrueTypeOfResultVisitor();
        JSType result = visitor.caseObjectType(objectType);
        
        assertNotNull(result);
        assertEquals(objectType, result);
        
        // Edge case: Top type passed
        JSType topType = new TopType();
        result = visitor.caseTopType(topType);
        
        assertNotNull(result);
        assertEquals(new ObjectType("NoObject"), result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        // Typical use case: Non-object type passed
        JSType nonObjectType = new FunctionType();
        RestrictByFalseTypeOfResultVisitor visitor = new RestrictByFalseTypeOfResultVisitor();
        JSType result = visitor.caseObjectType(nonObjectType);
        
        assertNull(result);
        
        // Edge case: Function type passed
        JSType functionType = new FunctionType();
        result = visitor.caseFunctionType(functionType);
        
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Typical use case: Function call with known restricters
        Node condition = new Node(CALL);
        Node callee = new Node(GETPROP, new Node(NAME, "goog"), new Node(STRING, "isArray"));
        Node param = new Node(NAME, "param");
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        blindScope.createChildFlowScope();
        
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        
        assertNotNull(result);
        assertEquals(blindScope, result);
    }

    @Test
    public void testRestrictParameter() {
        // Typical use case: Restriction applies and parameter type is restricted
        Node parameter = new Node(NAME, "param");
        JSType type = new ObjectType("Object");
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return new ObjectType("Object");
                } else {
                    return null;
                }
            }
        };
        
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
        
        assertNotNull(result);
        assertEquals(1, result.getBindingNames().size());
        assertTrue(result.getBindingNames().contains("param"));
        assertEquals(type, result.getTypeOfBinding(parameter.getString()));
        
        // Error scenario: Restriction does not apply
        type = new FunctionType();
        result = interpreter.restrictParameter(parameter, type, blindScope, restriction, true);
        
        assertNotNull(result);
        assertEquals(blindScope, result);
    }

}