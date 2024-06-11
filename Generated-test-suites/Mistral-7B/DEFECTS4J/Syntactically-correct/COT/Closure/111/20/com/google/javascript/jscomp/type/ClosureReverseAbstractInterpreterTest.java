package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

@RunWith(JUnit4.class)
public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    convention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node condition = createNode("goog.isDef(x)");
    JSType originalType = createObjectType("{x: number}");
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = createNode("goog.isDef(x)");
    FlowScope blindScope = createFlowScope(null);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_true() {
    Node condition = createNode("goog.isNull(x)");
    JSType originalType = createObjectType("{x: number}");
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(createType(JSTypeNative.NULL_TYPE), precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    Node condition = createNode("goog.isNull(x)");
    FlowScope blindScope = createFlowScope(null);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_true() {
    Node condition = createNode("goog.isDefAndNotNull(x)");
    JSType originalType = createObjectType("{x: number}");
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(createType(JSTypeNative.VOID_TYPE), precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    Node condition = createNode("goog.isDefAndNotNull(x)");
    FlowScope blindScope = createFlowScope(null);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node condition = createNode("goog.isArray(x)");
    JSType originalType = createArrayType();
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createNode("goog.isArray(x)");
    FlowScope blindScope = createFlowScope(createObjectType("{x: number}"));

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_true() {
    Node condition = createNode("goog.isObject(x)");
    JSType originalType = createObjectType("{x: number}");
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    Node condition = createNode("goog.isObject(x)");
    FlowScope blindScope = createFlowScope(createArrayType());

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction_true() {
    Node condition = createNode("goog.isFunction(x)");
    JSType originalType = createFunctionType();
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction_false() {
    Node condition = createNode("goog.isFunction(x)");
    FlowScope blindScope = createFlowScope(createObjectType("{x: number}"));

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_true() {
    Node condition = createNode("goog.isString(x)");
    JSType originalType = createStringType();
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_false() {
    Node condition = createNode("goog.isString(x)");
    FlowScope blindScope = createFlowScope(createObjectType("{x: number}"));

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_true() {
    Node condition = createNode("goog.isBoolean(x)");
    JSType originalType = createBooleanType();
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_false() {
    Node condition = createNode("goog.isBoolean(x)");
    FlowScope blindScope = createFlowScope(createObjectType("{x: number}"));

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber_true() {
    Node condition = createNode("goog.isNumber(x)");
    JSType originalType = createNumberType();
    FlowScope blindScope = createFlowScope(originalType);

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(originalType, precisionScope.getScopeType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber_false() {
    Node condition = createNode("goog.isNumber(x)");
    FlowScope blindScope = createFlowScope(createObjectType("{x: number}"));

    FlowScope precisionScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, precisionScope.getScopeType());
  }

  private Node createNode(String node) {
    return new Node() {
      @Override
      public NodeType getNodeType() {
        return NodeType.CALL;
      }

      @Override
      public String getString() {
        return node;
      }

      @Override
      public int getChildCount() {
        return 2;
      }

      @Override
      public Node getFirstChild() {
        return new Node() {
          @Override
          public NodeType getNodeType() {
            return NodeType.GET_PROP;
          }

          @Override
          public String getString() {
            return "goog";
          }
        };
      }

      @Override
      public Node getLastChild() {
        return new Node() {
          @Override
          public NodeType getNodeType() {
            return NodeType.STRING;
          }

          @Override
          public String getString() {
            return node.substring(5);
          }
        };
      }
    };
  }

  private FlowScope createFlowScope(JSType type) {
    return new FlowScope(interpreter, type, null, null, null, null);
  }

  private JSType createObjectType(String typeString) {
    return new ObjectType(typeRegistry, null, ImmutableMap.of(), null, typeString);
  }

  private JSType createArrayType() {
    return JSTypeNative.ARRAY_TYPE;
  }

  private JSType createFunctionType() {
    return new FunctionType(typeRegistry, null, ImmutableMap.of(), null, null, null);
  }

  private JSType createStringType() {
    return JSTypeNative.STRING;
  }

  private JSType createBooleanType() {
    return JSTypeNative.BOOLEAN;
  }

  private JSType createNumberType() {
    return JSTypeNative.NUMBER;
  }
}