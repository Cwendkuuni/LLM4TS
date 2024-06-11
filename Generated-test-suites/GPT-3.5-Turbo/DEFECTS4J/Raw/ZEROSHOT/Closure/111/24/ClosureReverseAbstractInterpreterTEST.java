import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_VOID;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_STRING_BOOLEAN;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;

    @Before
    public void setUp() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

        // add assertions here
    }

    @Test
    public void testRestrictParameter() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = ImmutableMap.of().get("isDef");
        boolean outcome = true;

        FlowScope result = closureReverseAbstractInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToArrayVisitorCaseTopType() {
        JSType topType = new JSType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor().caseTopType(topType);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToArrayVisitorCaseObjectType() {
        ObjectType type = new ObjectType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor().caseObjectType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotArrayVisitorCaseObjectType() {
        ObjectType type = new ObjectType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseObjectType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotArrayVisitorCaseAllType() {
        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseAllType();

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotArrayVisitorCaseFunctionType() {
        FunctionType type = new FunctionType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseFunctionType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToObjectVisitorCaseTopType() {
        JSType topType = new JSType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor().caseTopType(topType);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToObjectVisitorCaseObjectType() {
        ObjectType type = new ObjectType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor().caseObjectType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToObjectVisitorCaseFunctionType() {
        FunctionType type = new FunctionType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor().caseFunctionType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotObjectVisitorCaseAllType() {
        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseAllType();

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotObjectVisitorCaseObjectType() {
        ObjectType type = new ObjectType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseObjectType(type);

        // add assertions here
    }

    @Test
    public void testVisitorRestrictToNotObjectVisitorCaseFunctionType() {
        FunctionType type = new FunctionType();

        JSType result = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor().caseFunctionType(type);

        // add assertions here
    }
}