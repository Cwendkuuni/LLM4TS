package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FlowScope;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.Node;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention convention = new CodingConvention();
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isDef"),
        NodeFactory.newString("someString"));

    FlowScope originalScope = NodeFactory.newRootScope();
    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, true);

    JSType expectedType = getNativeType(JSTypeNative.STRING_TYPE);
    assertEquals(expectedType, newScope.getVariableValue("someString"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isDef"),
        NodeFactory.newString("nonexistentString"));

    FlowScope originalScope = NodeFactory.newRootScope();
    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, false);

    assertNull(newScope.getVariableValue("nonexistentString"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_true() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isNull"),
        NodeFactory.newNull());

    FlowScope originalScope = NodeFactory.newRootScope();
    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, true);

    assertNull(newScope.getVariableValue("nonexistentVariable"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isNull"),
        NodeFactory.newName("someVariable"));

    FlowScope originalScope = NodeFactory.newRootScope();
    originalScope.declareVariable("someVariable", getNativeType(JSTypeNative.NUMBER_TYPE));

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, false);

    JSType expectedType = getNativeType(JSTypeNative.NUMBER_TYPE);
    assertEquals(expectedType, newScope.getVariableValue("someVariable"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_true() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isDefAndNotNull"),
        NodeFactory.newString("someString"));

    FlowScope originalScope = NodeFactory.newRootScope();
    originalScope.declareVariable("someString", getNativeType(JSTypeNative.STRING_TYPE));

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, true);

    JSType expectedType = getNativeType(JSTypeNative.STRING_TYPE);
    assertEquals(expectedType, newScope.getVariableValue("someString"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isDefAndNotNull"),
        NodeFactory.newString("nonexistentString"));

    FlowScope originalScope = NodeFactory.newRootScope();

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, false);

    assertNull(newScope.getVariableValue("nonexistentString"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isArray"),
        NodeFactory.newArray(NodeFactory.newNumber(1), NodeFactory.newNumber(2)));

    FlowScope originalScope = NodeFactory.newRootScope();

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, true);

    JSType expectedType = getNativeType(JSTypeNative.ARRAY_TYPE);
    assertEquals(expectedType, newScope.getVariableValue("array"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isArray"),
        NodeFactory.newFunctionType(new FunctionType(
            new ObjectType(typeRegistry.createUnionType(
                JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE)),
            new ObjectType(JSTypeNative.BOOLEAN_TYPE))));

    FlowScope originalScope = NodeFactory.newRootScope();

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, false);

    assertNull(newScope.getVariableValue("function"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_true() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isObject"),
        NodeFactory.newString("Object"));

    FlowScope originalScope = NodeFactory.newRootScope();
    originalScope.declareVariable("Object", NodeFactory.newObjectType(typeRegistry));

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, true);

    ObjectType expectedType = (ObjectType) NodeFactory.newObjectType(typeRegistry);
    assertEquals(expectedType, newScope.getVariableValue("Object"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isObject"),
        NodeFactory.newNull());

    FlowScope originalScope = NodeFactory.newRootScope();

    FlowScope newScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, originalScope, false);

    assertNull(newScope.getVariableValue("nonexistentVariable"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_nullCondition() {
    Node conditionNode = null;

    FlowScope originalScope = NodeFactory.newRootScope();

    interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, originalScope, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_nullBlindScope() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "isDef"),
        NodeFactory.newString("someString"));

    FlowScope originalScope = null;

    interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, originalScope, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
    Node conditionNode = NodeFactory.newCall(
        NodeFactory.newGetProp(NodeFactory.newName("goog"), "invalidFunction"),
        NodeFactory.newNull());

    FlowScope originalScope = NodeFactory.newRootScope();

    interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, originalScope, true);
  }

  private static JSType getNativeType(JSTypeNative type) {
    return typeRegistry.getNativeType(type);
  }

  private static class JSTypeNative {
    public static final JSType ARRAY_TYPE = JSTypeNative.ArrayType.INSTANCE;
    public static final JSType NO_OBJECT_TYPE = JSTypeNative.NoObjectType.INSTANCE;
    public static final JSType NULL_TYPE = JSTypeNative.NullType.INSTANCE;
    public static final JSType NULL_VOID = JSTypeNative.NullVoidType.INSTANCE;
    public static final JSType NUMBER_STRING_BOOLEAN =
        JSTypeNative.NumberStringBooleanType.INSTANCE;
    public static final JSType BOOLEAN_TYPE = JSTypeNative.BooleanType.INSTANCE;
    public static final JSType NUMBER_TYPE = JSTypeNative.NumberType.INSTANCE;

    private static abstract class Type {
      public abstract JSType getGreatestSubtype(JSType subtype);
    }

    public static final class ArrayType extends Type {
      public static final ArrayType INSTANCE = new ArrayType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.ARRAY_TYPE);
      }
    }

    public static final class NoObjectType extends Type {
      public static final NoObjectType INSTANCE = new NoObjectType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
      }
    }

    public static final class NullType extends Type {
      public static final NullType INSTANCE = new NullType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.NULL_TYPE);
      }
    }

    public static final class NullVoidType extends Type {
      public static final NullVoidType INSTANCE = new NullVoidType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.NULL_VOID);
      }
    }

    public static final class NumberType extends Type {
      public static final NumberType INSTANCE = new NumberType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.NUMBER_TYPE);
      }
    }

    public static final class NumberStringBooleanType extends Type {
      public static final NumberStringBooleanType INSTANCE = new NumberStringBooleanType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN);
      }
    }

    public static final class BooleanType extends Type {
      public static final BooleanType INSTANCE = new BooleanType();

      @Override
      public JSType getGreatestSubtype(JSType subtype) {
        return getNativeType(JSTypeNative.BOOLEAN_TYPE);
      }
    }
  }
}