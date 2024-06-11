import org.junit.Assert;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        RestrictByTrueTypeOfResultVisitor visitor = new RestrictByTrueTypeOfResultVisitor();
        ObjectType objectType = new ObjectType();
        JSType arrayType = new JSType(ARRAY_TYPE);
        JSType result = visitor.caseObjectType(objectType);
        Assert.assertEquals(arrayType, result);

        // Edge case
        JSType topType = new JSType(TOP_TYPE);
        result = visitor.caseTopType(topType);
        Assert.assertEquals(topType, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        RestrictByFalseTypeOfResultVisitor visitor = new RestrictByFalseTypeOfResultVisitor();
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        Assert.assertNull(result);

        // Edge case
        JSType topType = new JSType(TOP_TYPE);
        result = visitor.caseTopType(topType);
        Assert.assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        RestrictByTrueTypeOfResultVisitor visitor = new RestrictByTrueTypeOfResultVisitor();
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        Assert.assertEquals(objectType, result);

        // Edge case
        JSType topType = new JSType(TOP_TYPE);
        result = visitor.caseTopType(topType);
        Assert.assertEquals(NO_OBJECT_TYPE, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        RestrictByFalseTypeOfResultVisitor visitor = new RestrictByFalseTypeOfResultVisitor();
        ObjectType objectType = new ObjectType();
        JSType result = visitor.caseObjectType(objectType);
        Assert.assertNull(result);

        // Edge case
        JSType topType = new JSType(TOP_TYPE);
        result = visitor.caseTopType(topType);
        Assert.assertEquals(NULL_TYPE, result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        Assert.assertNotNull(result);

        // Edge case
        outcome = false;
        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        Assert.assertEquals(blindScope, result);
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

        // Typical use case
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new RestrictionFunction();
        boolean outcome = true;
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        Assert.assertNotNull(result);

        // Edge case
        outcome = false;
        result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        Assert.assertEquals(blindScope, result);
    }

    private class RestrictionFunction implements Function<TypeRestriction, JSType> {
        @Override
        public JSType apply(TypeRestriction p) {
            if (p.outcome) {
                return p.type != null ? new JSType(VOID_TYPE).getGreatestSubtype(p.type) : null;
            } else {
                return null;
            }
        }
    }
}