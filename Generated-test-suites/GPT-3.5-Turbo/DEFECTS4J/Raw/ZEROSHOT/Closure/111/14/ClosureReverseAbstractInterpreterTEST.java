import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = rai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // Add assertions here
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                return null;
            }
        };
        boolean outcome = true;

        FlowScope result = rai.restrictParameter(parameter, type, blindScope, restriction, outcome);

        // Add assertions here
    }

}