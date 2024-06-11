import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeNative;

import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static com.google.javascript.rhino.Token.*;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter closureRai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        
        // Test with a CALL node and a qualified name parameter
        Node condition = new Node(Token.CALL);
        Node callee = new Node(Token.GETPROP);
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isDef"));
        condition.addChildToBack(callee);
        Node param = new Node(Token.NAME, "param");
        param.setQualifiedName(true);
        condition.addChildToBack(param);
        
        FlowScope blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        
        boolean outcome = true;
        FlowScope result = closureRai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert the result
        
        outcome = false;
        result = closureRai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert the result
        
        // Test with a CALL node and a non-qualified name parameter
        condition = new Node(Token.CALL);
        callee = new Node(Token.GETPROP);
        callee.addChildToBack(new Node(Token.NAME, "goog"));
        callee.addChildToBack(new Node(Token.STRING, "isDef"));
        condition.addChildToBack(callee);
        param = new Node(Token.NAME, "param");
        param.setQualifiedName(false);
        condition.addChildToBack(param);
        
        blindScope = new FlowScope();
        blindScope.createChildFlowScope();
        
        outcome = true;
        result = closureRai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert the result
        
        outcome = false;
        result = closureRai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        // Assert the result
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter closureRai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        
        Node parameter = new Node(Token.NAME, "param");
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            public JSType apply(TypeRestriction p) {
                return null;
            }
        };
        boolean outcome = true;
        
        FlowScope result = closureRai.restrictParameter(parameter, type, blindScope, restriction, outcome);
        // Assert the result
        
        outcome = false;
        result = closureRai.restrictParameter(parameter, type, blindScope, restriction, outcome);
        // Assert the result
    }
}