import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.Node;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Function;

public class ClosureReverseAbstractInterpreterTest {
  
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  
  @Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(null, typeRegistry);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node(Node.CALL);
    Node callee = new Node(Node.GETPROP);
    callee.addChildToBack(new Node(Node.NAME, "goog"));
    callee.addChildToBack(new Node(Node.STRING, "isDef"));
    condition.addChildToFront(callee);
    Node param = new Node(Node.NAME, "param");
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    // Assert statements
  }
  
  @Test
  public void testRestrictParameter() {
    Node parameter = new Node(Node.NAME, "param");
    JSType type = typeRegistry.getType("Object");
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return p.type;
        } else {
          return null;
        }
      }
    };
    boolean outcome = true;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    // Assert statements
  }
}