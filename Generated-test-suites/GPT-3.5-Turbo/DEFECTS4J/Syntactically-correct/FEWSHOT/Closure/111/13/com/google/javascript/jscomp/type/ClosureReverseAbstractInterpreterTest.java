import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        Node conditionTrue = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isDef")), new Node(Node.NAME, "param"));
        Node conditionFalse = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isArray")), new Node(Node.NAME, "param"));
        FlowScope blindScope = new FlowScope();
        FlowScope outcomeTrue = interpreter.getPreciserScopeKnowingConditionOutcome(conditionTrue, blindScope, true);
        FlowScope outcomeFalse = interpreter.getPreciserScopeKnowingConditionOutcome(conditionFalse, blindScope, false);
        assertEquals(outcomeTrue, outcomeFalse);
    }

    @Test
    public void testRestrictParameter() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        Node parameter = new Node(Node.NAME, "param");
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                    return new JSType();
                } else {
                    return null;
                }
            }
        };
        boolean outcomeTrue = true;
        boolean outcomeFalse = false;
        FlowScope outcomeTrueScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcomeTrue);
        FlowScope outcomeFalseScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcomeFalse);
        assertEquals(outcomeTrueScope, outcomeFalseScope);
    }
}