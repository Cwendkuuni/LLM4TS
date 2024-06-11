import static org.junit.Assert.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeNative.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.com.google.javascript.rhino.NodeOperations;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node condition = NodeOperations.createCall(CALL, "goog.isDef", NO_OBJECT_TYPE);
    FlowScope blindScope = new FlowScope(ObjectType.VOID);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getVariable("arg0").getType();
    assertTrue(restrictedType instanceof ObjectType);
    assertFalse(restrictedType.isSubtype(getNativeType(NULL_TYPE)));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = NodeOperations.createCall(CALL, "goog.isDef", NO_OBJECT_TYPE);
    FlowScope blindScope = new FlowScope(ObjectType.VOID);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope.getVariable("arg0"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node condition = NodeOperations.createCall(CALL, "goog.isArray", ARRAY_TYPE);
    FlowScope blindScope = new FlowScope(OBJECT_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getVariable("arg0").getType();
    assertTrue(restrictedType.isSubtype(ARRAY_TYPE));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = NodeOperations.createCall(CALL, "goog.isArray", NO_OBJECT_TYPE);
    FlowScope blindScope = new FlowScope(OBJECT_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope.getVariable("arg0"));
  }

  @Test(expected = NullPointerException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
    Node condition = NodeOperations.createCall(CALL, "goog.isDef", NULL_TYPE);
    FlowScope blindScope = new FlowScope(ObjectType.VOID);
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }
}