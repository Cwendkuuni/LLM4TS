package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import static com.google.javascript.jscomp.type.JSType.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.visitor.RestrictByFalseTypeOfResultVisitor;
import com.google.javascript.rhino.jstype.visitor.RestrictByTrueTypeOfResultVisitor;

import java.util.Map;

/**
 * Test cases for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private static final CodingConvention CONVENTION = new CodingConvention();
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();

  @Test
  public void testRestrictToArray() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType intType = getNativeType(INT_TYPE);

    // true case
    JSType intArray = interpreter.restrict(intType, "isArray", true).get();
    assertEquals(arrayType, intArray);

    // false case
    JSType intNotArray = interpreter.restrict(intType, "isArray", false).get();
    assertNull(intNotArray);
  }

  @Test
  public void testRestrictToObject() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType objectType = getNativeType(OBJECT_TYPE);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType obj = interpreter.restrict(numberType, "isObject", true).get();
    assertEquals(objectType, obj);

    // false case
    JSType notObj = interpreter.restrict(numberType, "isObject", false).get();
    assertNull(notObj);
  }

  @Test
  public void testRestrictToFunction() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType functionType = getNativeType(FUNCTION_TYPE);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType func = interpreter.restrict(numberType, "isFunction", true).get();
    assertEquals(functionType, func);

    // false case
    JSType notFunc = interpreter.restrict(numberType, "isFunction", false).get();
    assertNull(notFunc);
  }

  @Test
  public void testRestrictToBoolean() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType booleanType = getNativeType(BOOLEAN_TYPE);

    // true case
    JSType trueType = interpreter.restrict(booleanType, "isBoolean", true).get();
    assertEquals(booleanType, trueType);

    // false case
    JSType falseType = interpreter.restrict(booleanType, "isBoolean", false).get();
    assertNull(falseType);
  }

  @Test
  public void testRestrictToNumber() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType num = interpreter.restrict(numberType, "isNumber", true).get();
    assertEquals(numberType, num);

    // false case
    JSType notNum = interpreter.restrict(numberType, "isNumber", false).get();
    assertNull(notNum);
  }

  @Test
  public void testRestrictToString() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType stringType = getNativeType(STRING_TYPE);

    // true case
    JSType str = interpreter.restrict(stringType, "isString", true).get();
    assertEquals(stringType, str);

    // false case
    JSType notStr = interpreter.restrict(stringType, "isString", false).get();
    assertNull(notStr);
  }

  @Test
  public void testRestrictToNull() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType nullType = getNativeType(NULL_TYPE);

    // true case
    JSType nullObj = interpreter.restrict(nullType, "isNull", true).get();
    assertEquals(nullType, nullObj);

    // false case
    JSType notNull = interpreter.restrict(nullType, "isNull", false).get();
    assertNull(notNull);
  }

  @Test
  public void testRestrictToDef() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType def = interpreter.restrict(numberType, "isDef", true).get();
    assertNotNull(def);

    // false case
    JSType notDef = interpreter.restrict(numberType, "isDef", false).get();
    assertNull(notDef);
  }

  @Test
  public void testRestrictToDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType defAndNotNull = interpreter.restrict(numberType, "isDefAndNotNull", true).get();
    assertNotNull(defAndNotNull);

    // false case
    JSType notDefAndNotNull = interpreter.restrict(numberType, "isDefAndNotNull", false).get();
    assertNull(notDefAndNotNull);
  }

  @Test
  public void testRestrictToDefOrNull() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true case
    JSType defOrNull = interpreter.restrict(numberType, "isDefOrNull", true).get();
    assertNotNull(defOrNull);

    // false case
    JSType notDefOrNull = interpreter.restrict(numberType, "isDefOrNull", false).get();
    assertNull(notDefOrNull);
  }

  @Test
  public void testRestrictToUndefined() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    JSType undefinedType = getNativeType(UNDEFINED_TYPE);

    // true case
    JSType undefined = interpreter.restrict(undefinedType, "isDef", false).get();
    assertEquals(undefinedType, undefined);

    // false case
    JSType notUndefined = interpreter.restrict(undefinedType, "isDef", true).get();
    assertNull(notUndefined);
  }

  private JSType getNativeType(String typeName) {
    return TYPE_REGISTRY.getType(typeName);
  }

  private void declareNameInScope(FlowScope scope, Node node, JSType type) {
    ObjectType objectType = getNativeType(OBJECT_TYPE);
    ObjectType declaredType = objectType.createProperty(node.getName(), type);
    scope.declare(node, declaredType);
  }
}