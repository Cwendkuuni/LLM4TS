import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention codingConvention = new CodingConvention();

  @Test
  public void testIsDefMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    // Test when the outcome is true
    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = interpreter.getRestrictedWithoutUndefined(type.type);
    assertEquals(expected, interpreter.isDef(type));

    // Test when the outcome is false
    type.outcome = false;
    JSType actual = interpreter.isDef(type);
    assertNull(actual);
  }

  @Test
  public void testIsNullMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    // Test when the outcome is true
    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = interpreter.getRestrictedWithoutNull(type.type);
    assertEquals(expected, interpreter.isNull(type));

    // Test when the outcome is false
    type.type = getNativeType(NULL_TYPE);
    JSType actual = interpreter.isNull(type);
    assertNull(actual);
  }

  @Test
  public void testIsDefAndNotNullMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    // Test when the outcome is true
    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(type.type));
    assertEquals(expected, interpreter.isDefAndNotNull(type));

    // Test when the outcome is false
    type.outcome = false;
    JSType actual = interpreter.isDefAndNotNull(type);
    assertNull(actual);
  }

  @Test
  public void testIsStringMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = getNativeType("string");
    assertEquals(expected, interpreter.isString(type));

    type.outcome = false;
    JSType actual = interpreter.isString(type);
    assertNull(actual);
  }

  @Test
  public void testIsBooleanMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = getNativeType("boolean");
    assertEquals(expected, interpreter.isBoolean(type));

    type.outcome = false;
    JSType actual = interpreter.isBoolean(type);
    assertNull(actual);
  }

  @Test
  public void testIsNumberMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    JSType expected = getNativeType("number");
    assertEquals(expected, interpreter.isNumber(type));

    type.outcome = false;
    JSType actual = interpreter.isNumber(type);
    assertNull(actual);
  }

  @Test
  public void testIsFunctionMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    TypeRestriction type = new TypeRestriction(getNativeType(NUMBER_STRING_BOOLEAN), true);
    FunctionType functionType = getFunctionType(new JSType[0], VOID_TYPE);
    JSType expected = functionType;
    assertEquals(expected, interpreter.isFunction(type));

    type.outcome = false;
    JSType actual = interpreter.isFunction(type);
    assertNull(actual);
  }

  @Test
  public void testIsArrayMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    // Test when the outcome is true
    JSType arrayType = getNativeType(ARRAY_TYPE);
    TypeRestriction type = new TypeRestriction(arrayType, true);
    JSType expected = arrayType;
    assertEquals(expected, interpreter.isArray(type));

    // Test when the outcome is false
    type.type = getNativeType(OBJECT_TYPE);
    JSType actual = interpreter.isArray(type);
    assertNull(actual);
  }

  @Test
  public void testIsObjectMethod() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    // Test when the outcome is true
    ObjectType objectType = getObjectType();
    TypeRestriction type = new TypeRestriction(objectType, true);
    JSType expected = objectType;
    assertEquals(expected, interpreter.isObject(type));

    // Test when the outcome is false
    type.type = getNativeType(ARRAY_TYPE);
    JSType actual = interpreter.isObject(type);
    assertNull(actual);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeWithNullCondition() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    interpreter.getPreciserScopeKnowingConditionOutcome(null, null, true);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidCallee() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node condition = Node.newCall(Node.newName("goog"), "isDef", null);
    Node callee = Node.newName("invalid");
    Node param = Node.newQualifiedName("goog", "isDef");
    condition.setFirstChild(condition.getFirstChild().cloneTree());
    condition.setLastChild(condition.getLastChild().cloneTree());
    condition.getFirstChild().setFirstChild(callee);
    condition.getLastChild().setLastChild(param);

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidCalleeName() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node condition = Node.newCall(Node.newName("goog"), "isDef", null);
    Node callee = Node.newName("invalidGoog");
    Node param = Node.newQualifiedName("goog", "isDef");
    condition.setFirstChild(condition.getFirstChild().cloneTree());
    condition.setLastChild(condition.getLastChild().cloneTree());
    condition.getFirstChild().setFirstChild(callee);
    condition.getLastChild().setLastChild(param);

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidParameter() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node condition = Node.newCall(Node.newName("goog"), "isDef", null);
    Node callee = Node.newName("goog");
    Node param = Node.newName("invalid");
    condition.setFirstChild(condition.getFirstChild().cloneTree());
    condition.setLastChild(condition.getLastChild().cloneTree());
    condition.getFirstChild().setFirstChild(callee);
    condition.getLastChild().setLastChild(param);

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidParameterName() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    Node condition = Node.newCall(Node.newName("goog"), "isDef", null);
    Node callee = Node.newName("goog");
    Node param = Node.newName("invalidGoog");
    condition.setFirstChild(condition.getFirstChild().cloneTree());
    condition.setLastChild(condition.getLastChild().cloneTree());
    condition.getFirstChild().setFirstChild(callee);
    condition.getLastChild().setLastChild(param);

    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  

  private FunctionType getFunctionType(JSType[] parameterTypes, JSType returnType) {
    ObjectType functionTypeObject = typeRegistry.createObjectType("Function");
    FunctionType functionType = new FunctionType(functionTypeObject, parameterTypes, returnType);
    return functionType;
  }

  private ObjectType getObjectType() {
    return typeRegistry.createObjectType("Object");
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.getNativeType(typeName);
  }

  private JSType getTypeIfRefinable(Node node, FlowScope flowScope) {
    return flowScope.getVariableType(node.getIdentifier(), Node.NO_SOURCE_POSITION);
  }

  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }
}