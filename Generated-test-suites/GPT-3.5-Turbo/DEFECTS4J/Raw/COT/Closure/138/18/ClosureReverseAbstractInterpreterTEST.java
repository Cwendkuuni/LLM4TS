import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void getPreciserScopeKnowingConditionOutcome_validInput_returnFlowScope() {
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void getPreciserScopeKnowingConditionOutcome_invalidInput_returnNextPreciserScope() {
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        assertNotNull(result);
    }

    @Test
    public void restrictParameter_validInput_returnFlowScope() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = (p) -> {
            if (p.outcome) {
                return type;
            } else {
                return null;
            }
         };
        boolean outcome = true;

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

        assertNotNull(result);
    }

    @Test
    public void restrictParameter_invalidInput_returnFlowScope() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = (p) -> {
            if (p.outcome) {
                return type;
            } else {
                return null;
            }
         };
        boolean outcome = false;

        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

        assertNotNull(result);
    }

}