import static org.junit.Assert.*;
import static com.google.javascript.rhino.token.TokenType.*;
import static com.google.javascript.jscomp.NodeFactory.createNode;
import static com.google.javascript.jscomp.NodeFactory.createString;
import static com.google.javascript.jscomp.NodeFactory.createName;
import static com.google.javascript.jscomp.NodeFactory.createGetProperty;
import static com.google.javascript.jscomp.NodeFactory.createCall;
import static com.google.javascript.jscomp.NodeFactory.createBooleanLiteral;
import static com.google.javascript.jscomp.CodingConvention.Standard;
import static com.google.javascript.rhino.NodeType.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.RestrictByTrueTypeOfResultVisitor;
import com.google.javascript.rhino.jstype.RestrictByFalseTypeOfResultVisitor;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.TypeRestriction;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(Standard, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = createCall(CALL, createName("goog", NAME), "isDef",
        createGetProperty(GETPROP, createName("foo", NAME), NULL_TYPE),
        createBooleanLiteral(true));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = getNativeType(JSTypeNative.ANY_TYPE);
    assertEquals(expectedType, informedScope.getVariable("foo").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_null() {
    Node condition = createCall(CALL, createName("goog", NAME), "isDef",
        createGetProperty(GETPROP, createName("foo", NAME), NULL_TYPE),
        createBooleanLiteral(false));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = createCall(CALL, createName("goog", NAME), "isArray", NULL_TYPE, createBooleanLiteral(true));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = getNativeType(JSTypeNative.ARRAY_TYPE);
    assertEquals(expectedType, informedScope.getVariable("arr").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createCall(CALL, createName("goog", NAME), "isArray", NULL_TYPE, createBooleanLiteral(false));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    Node condition = createCall(CALL, createName("goog", NAME), "isObject", createNode(OBJECT_TYPE, new Object[] {}), createBooleanLiteral(true));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = getNativeType(NO_OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getVariable("obj").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    Node condition = createCall(CALL, createName("goog", NAME), "isObject", createNode(OBJECT_TYPE, new Object[] {}), createBooleanLiteral(false));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_Unknown() {
    Node condition = createCall(CALL, createName("goog", NAME), "isUnknown", createNode(NUMBER_TYPE, new Object[] {}), createBooleanLiteral(true));

    Node blindScope = createNode(BLOCK, new Object[] {});
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNull(informedScope);
  }

  private JSType getNativeType(int type) {
    return typeRegistry.getNativeType(type);
  }

  private JSType getNativeType(JSTypeNative type) {
    return typeRegistry.getNativeType(type.getTypeCode());
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.get(typeName);
  }
}