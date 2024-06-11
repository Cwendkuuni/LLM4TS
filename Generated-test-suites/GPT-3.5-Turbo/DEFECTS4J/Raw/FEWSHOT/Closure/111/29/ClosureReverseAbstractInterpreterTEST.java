import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import java.util.Map;
import static com.google.javascript.jscomp.type.JSTypeTestUtil.getTypeIfRefinable;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_VOID;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_STRING_BOOLEAN;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;

public class ClosureReverseAbstractInterpreterTest {
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_callFunction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Node condition = createCallFunctionNode();
        FlowScope blindScope = createBlindScope();
        boolean outcome = true;
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // perform assertions on result
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_notCallFunction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Node condition = createNotCallFunctionNode();
        FlowScope blindScope = createBlindScope();
        boolean outcome = true;
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // perform assertions on result
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Node condition = createInvalidConditionNode();
        FlowScope blindScope = createBlindScope();
        boolean outcome = true;
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // perform assertions on result
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        Node parameter = createParameterNode();
        JSType type = createJSType();
        FlowScope blindScope = createBlindScope();
        Function<TypeRestriction, JSType> restriction = createRestrictionFunction();
        boolean outcome = true;
        FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        // perform assertions on result
    }
    
    private Node createCallFunctionNode() {
        // create and return the Node
    }
    
    private Node createNotCallFunctionNode() {
        // create and return the Node
    }
    
    private Node createInvalidConditionNode() {
        // create and return the Node
    }
    
    private Node createParameterNode() {
        // create and return the Node
    }
    
    private JSType createJSType() {
        // create and return the JSType
    }
    
    private FlowScope createBlindScope() {
        // create and return the FlowScope
    }
    
    private Function<TypeRestriction, JSType> createRestrictionFunction() {
        // create and return the Function
    }
    
}