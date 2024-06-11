import org.junit.Assert;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testRestrictToArrayVisitor() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        JSType resultType = closureInterpreter.restrictToArrayVisitor.caseObjectType(new ObjectType());
        Assert.assertNotNull(resultType);
        Assert.assertEquals(JSTypeNative.ARRAY_TYPE, resultType.getDisplayName());
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        JSType resultType = closureInterpreter.restrictToNotArrayVisitor.caseObjectType(new ObjectType());
        Assert.assertNull(resultType);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        JSType resultType = closureInterpreter.restrictToObjectVisitor.caseObjectType(new ObjectType());
        Assert.assertEquals(JSTypeNative.OBJECT_TYPE, resultType.getDisplayName());
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        JSType resultType = closureInterpreter.restrictToNotObjectVisitor.caseObjectType(new ObjectType());
        Assert.assertNull(resultType);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        FlowScope resultScope = closureInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        Assert.assertNotNull(resultScope);
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                return null;
            }
        };
        boolean outcome = false;
        FlowScope resultScope = closureInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        Assert.assertEquals(blindScope, resultScope);
    }

}