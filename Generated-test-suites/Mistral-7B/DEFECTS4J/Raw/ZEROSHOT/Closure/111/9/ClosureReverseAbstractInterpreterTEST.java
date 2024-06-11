import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.FlowScope;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private CodingConvention codingConvention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    codingConvention = new CodingConvention();
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testRestrictIsDef() {
    Node condition = newCall("goog.isDef", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsDefFalse() {
    Node condition = newCall("goog.isDef", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    Node condition = newCall("goog.isDefAndNotNull", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN).getGreatestSubtype(getNativeType(OBJECT_TYPE));
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsDefAndNotNullFalse() {
    Node condition = newCall("goog.isDefAndNotNull", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(NULL_VOID);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsString() {
    Node condition = newCall("goog.isString", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(JSTypeNative.STRING_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsStringFalse() {
    Node condition = newCall("goog.isString", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsBoolean() {
    Node condition = newCall("goog.isBoolean", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsBooleanFalse() {
    Node condition = newCall("goog.isBoolean", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsNumber() {
    Node condition = newCall("goog.isNumber", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(JSTypeNative.NUMBER_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsNumberFalse() {
    Node condition = newCall("goog.isNumber", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsFunction() {
    Node condition = newCall("goog.isFunction", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(ObjectType.FUNCTION_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsFunctionFalse() {
    Node condition = newCall("goog.isFunction", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsArrayTrue() {
    Node condition = newCall("goog.isArray", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(ARRAY_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsArrayFalse() {
    Node condition = newCall("goog.isArray", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsObjectTrue() {
    Node condition = newCall("goog.isObject", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  @Test
  public void testRestrictIsObjectFalse() {
    Node condition = newCall("goog.isObject", newQualifiedName("foo"), null);
    FlowScope blindScope = new FlowScope("test", getNativeType(VOID_TYPE));
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals("Informed type should be correct", expectedType, informedScope.getTopType());
  }

  private JSType getNativeType(int typeCode) {
    return typeRegistry.getTypes().get(JSTypeNative.getTypeName(typeCode));
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.getTypes().get(typeName);
  }

  private JSType getTypeIfRefinable(Node node, FlowScope flowScope) {
    if (node.isGetProp() && node.getChildCount() > 1) {
      Node target = node.getFirstChild();
      Node property = node.getLastChild();

      JSType type = flowScope.getTopType();
      if (type.isSubtype(ObjectType.OBJECT_TYPE)) {
        Visitor<JSType> visitor = new Visitor<JSType>() {
          @Override
          public JSType visitObjectType(ObjectType type) {
            Map<String, JSType> properties = type.getProperties();
            return properties.get(property.getString());
          }

          @Override
          public JSType visitNullType() {
            return null;
          }

          @Override
          public JSType visitTopType() {
            return null;
          }

          @Override
          public JSType visitUndefinedType() {
            return null;
          }
        };

        return type.visit(visitor);
      }
    }

    return typeRegistry.createNullType();
  }
}