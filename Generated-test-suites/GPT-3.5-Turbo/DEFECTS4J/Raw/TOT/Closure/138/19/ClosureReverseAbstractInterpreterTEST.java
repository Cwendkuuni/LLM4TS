import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = new ClosureReverseAbstractInterpreter();
    }

    // Test cases for restrictToArrayVisitor

    @Test
    public void whenTopTypeIsPassed_ReturnTopType() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;
        JSType result = visitor.caseTopType(new JSType());
        assertNotNull(result);
        assertEquals(result, new JSType());
    }

    @Test
    public void whenObjectTypeIsPassed_ReturnArrayTypeIfSubtype() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;
        ObjectType objectType = new ObjectType();
        JSType arrayType = new JSType(Array.TYPE);
        objectType.addSubtype(arrayType);
        JSType result = visitor.caseObjectType(objectType);
        assertNotNull(result);
        assertEquals(result, arrayType);
    }

    @Test
    public void whenObjectTypeIsPassed_ReturnNullIfNotSubtype() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;
        ObjectType objectType = new ObjectType();
        JSType numberType = new JSType(Number.TYPE);
        JSType result = visitor.caseObjectType(objectType);
        assertNull(result);
    }

    // Test cases for restrictToNotArrayVisitor

    @Test
    public void whenObjectTypeIsPassed_ReturnNull() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        assertNull(result);
    }

    @Test
    public void whenFunctionTypeIsPassed_ReturnNull() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;
        FunctionType functionType = new FunctionType();
        JSType result = visitor.caseFunctionType(functionType);
        assertNull(result);
    }

    // Test cases for restrictToObjectVisitor

    @Test
    public void whenTopTypeIsPassed_ReturnNoObjectType() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;
        JSType topType = new JSType();
        JSType noObjectType = new JSType(NoObject.TYPE);
        topType.addSubtype(noObjectType);
        JSType result = visitor.caseTopType(topType);
        assertNotNull(result);
        assertEquals(result, noObjectType);
    }

    @Test
    public void whenObjectTypeIsPassed_ReturnObjectType() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        assertNotNull(result);
        assertEquals(result, objectType);
    }

    @Test
    public void whenFunctionTypeIsPassed_ReturnFunctionType() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;
        FunctionType functionType = new FunctionType();
        JSType result = visitor.caseFunctionType(functionType);
        assertNotNull(result);
        assertEquals(result, functionType);
    }

    // Test cases for restrictToNotObjectVisitor

    @Test
    public void whenObjectTypeIsPassed_ReturnNull() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        assertNull(result);
    }

    @Test
    public void whenFunctionTypeIsPassed_ReturnNull() {
        ClosureReverseAbstractInterpreter.Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;
        FunctionType functionType = new FunctionType();
        JSType result = visitor.caseFunctionType(functionType);
        assertNull(result);
    }

    // Test cases for getPreciserScopeKnowingConditionOutcome

    // TODO: Add test cases for getPreciserScopeKnowingConditionOutcome method

}