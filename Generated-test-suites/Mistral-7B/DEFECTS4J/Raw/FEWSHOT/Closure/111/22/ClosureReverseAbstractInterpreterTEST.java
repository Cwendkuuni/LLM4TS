package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.TypeRestriction;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(BOOLEAN_TYPE);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isDef"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(VOID_TYPE).getGreatestSubtype(inputType);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isDef"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsNonNull() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(NULL_TYPE).getGreatestSubtype(inputType);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isNonNull"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isNonNull"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NULL_VOID).getGreatestSubtype(inputType);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isDefAndNotNull"), false);
    assertNotNull(result);
    assertNull(result);
  }

  @Test
  public void testIsString() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(STRING_TYPE);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isString"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isString"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsBoolean() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(BOOLEAN_TYPE);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isBoolean"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isBoolean"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsNumber() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(NUMBER_TYPE);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isNumber"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isNumber"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsFunction() {
    JSType inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(FUNCTION_TYPE);

    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isFunction"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = getNativeType(NUMBER_STRING_BOOLEAN);

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isFunction"), false);
    assertNotNull(result);
    assertEquals(expectedType, result);
  }

  @Test
  public void testIsArray() {
    JSType inputType = null;
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(ARRAY_TYPE);

    when(typeRegistry.getNativeType("Boolean")).thenReturn(getNativeType(BOOLEAN_TYPE));
    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isArray"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = null;

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isArray"), false);
    assertNotNull(result);
    assertNull(result);
  }

  @Test
  public void testIsObject() {
    JSType inputType = null;
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedType = getNativeType(OBJECT_TYPE);

    when(typeRegistry.getNativeType("Function")).thenReturn(getNativeType(FUNCTION_TYPE));
    JSType result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isObject"), true);
    assertNotNull(result);
    assertEquals(expectedType, result);

    inputType = getNativeType(NUMBER_STRING_BOOLEAN);
    typeRestriction = new TypeRestriction(inputType, false);
    expectedType = null;

    result = interpreter.restrictParameter(null, typeRestriction.type, null, interpreter.restricters.get("isObject"), false);
    assertNotNull(result);
    assertNull(result);
  }

  private JSType getNativeType(String name) {
    return (JSType) typeRegistry.getNativeType(name);
  }

  private static final Map<String, Class<? extends Node>> NODE_TYPES =
      ImmutableMap.<String, Class<? extends Node>>builder()
          .put("Name", Node.class)
          .put("StringLiteral", StringLiteral.class)
          .put("GetProp", GetProp.class)
          .put("Call", Call.class)
          .put("FunctionDeclaration", FunctionDeclaration.class)
          .build();

  private JSType getTypeIfRefinable(Node node, FlowScope scope) {
    if (node == null || node.getType() != null) {
      return null;
    }

    Class<? extends Node> nodeType = NODE_TYPES.get(node.getNodeType().getName());
    if (nodeType == null) {
      throw new RuntimeException("Unsupported node type: " + node.getNodeType());
    }

    JSType type;
    if (nodeType.isAssignableFrom(Call.class)) {
      Call call = (Call) node;
      JSType calleeType = scope.lookupVariable(call.getFirstChild().getName(), true);
      type = calleeType;
    } else {
      type = scope.lookupVariable(node.getName(), true);
    }

    if (type == null) {
      type = JSType.UNDEFINED;
    }

    return type;
  }
}