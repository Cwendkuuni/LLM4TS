import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    
    ClosureReverseAbstractInterpreter interpreter;
    
    @Before
    public void setup() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry registry = new JSTypeRegistry();
        interpreter = new ClosureReverseAbstractInterpreter(convention, registry);
    }
    
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ConditionIsCallWithRestrictedType_ReturnsRestrictedScope() {
        Node condition = new Node();
        Node callee = new Node();
        Node param = new Node();
        Node left = new Node("name", "goog");
        Node right = new Node("string", "isDef");
        Node paramType = new Node();
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        callee.addChildToBack(left);
        callee.addChildToBack(right);
        param.addChildToBack(paramType);
        
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;
        
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        // Verify that parameter type is restricted
        assertEquals(restrictedType, scope.getType(paramType));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_ConditionIsCallWithNullType_ReturnsSameScope() {
        Node condition = new Node();
        Node callee = new Node();
        Node param = new Node();
        Node left = new Node("name", "goog");
        Node right = new Node("string", "isDefAndNotNull");
        Node paramType = new Node();
        condition.addChildToBack(callee);
        condition.addChildToBack(param);
        callee.addChildToBack(left);
        callee.addChildToBack(right);
        param.addChildToBack(paramType);
        
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;
        
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        // Verify that scope is not changed
        assertEquals(blindScope, scope);
    }
    
    @Test
    public void testRestrictParameter_TypeIsNull_ReturnsNullType() {
        Node parameter = new Node();
        JSType type = null;
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = ClosureReverseAbstractInterpreter.restricters.get("isNull");
        boolean outcome = true;
        
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify that parameter type is null
        assertEquals(null, scope.getType(parameter));
    }

    @Test
    public void testRestrictParameter_TypeIsNotNull_ReturnsRestrictedType() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = ClosureReverseAbstractInterpreter.restricters.get("isDefAndNotNull");
        boolean outcome = true;
        
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify that parameter type is restricted
        assertEquals(restrictedType, scope.getType(parameter));
    }

    @Test
    public void testRestrictParameter_TypeIsFunction_ReturnsRestrictedType() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = ClosureReverseAbstractInterpreter.restricters.get("isFunction");
        boolean outcome = false;
        
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify that parameter type is restricted
        assertEquals(restrictedType, scope.getType(parameter));
    }

    @Test
    public void testRestrictParameter_TypeIsObject_ReturnsRestrictedType() {
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = ClosureReverseAbstractInterpreter.restricters.get("isObject");
        boolean outcome = true;
        
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // Verify that parameter type is restricted
        assertEquals(restrictedType, scope.getType(parameter));
    }

}