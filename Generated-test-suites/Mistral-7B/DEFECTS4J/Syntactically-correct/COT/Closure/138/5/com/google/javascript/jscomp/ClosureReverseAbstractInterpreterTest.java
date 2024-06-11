import static org.junit.Assert.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeNative.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    JSType type = getNativeType(OBJECT_TYPE);
    Node condition = createNode(CALL, new Node[] {
        createNode(GETPROP, new String[] {"goog", "isDef"}),
        createNode(STRING, "myObject")
    });
    FlowScope blindScope = createFlowScope();

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getVariableValue("myObject");
    assertEquals(getRestrictedWithoutUndefined(type), restrictedType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = createNode(CALL, new Node[] {
        createNode(GETPROP, new String[] {"goog", "isDef"}),
        createNode(STRING, "undefined")
    });
    FlowScope blindScope = createFlowScope();

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getVariableValue("myObject"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    JSType arrayType = getNativeType(ARRAY_TYPE);
    Node condition = createNode(CALL, new Node[] {
        createNode(GETPROP, new String[] {"goog", "isArray"}),
        createNode(NAME, "myArray")
    });
    FlowScope blindScope = createFlowScope();

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getVariableValue("myArray");
    assertEquals(arrayType, restrictedType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createNode(CALL, new Node[] {
        createNode(GETPROP, new String[] {"goog", "isArray"}),
        createNode(NULL_TYPE)
    });
    FlowScope blindScope = createFlowScope();

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getVariableValue("myArray"));
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcome_unknown_condition() {
    Node condition = createNode(CALL, new Node[] {
        createNode(CALL, new Node[] {NAME, "unknownFunction"}),
        createNode(STRING, "unknownArgument")
    });
    FlowScope blindScope = createFlowScope();

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }

  private Node createNode(int type, Node... children) {
    RhinoInternals internals = new RhinoInternals();
    return internals.newNode(type, children);
  }

  private FlowScope createFlowScope() {
    return new FlowScope(null, null, null);
  }

}