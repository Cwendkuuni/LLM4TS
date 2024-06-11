import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Type;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

public class ClosureReverseAbstractInterpreterTest {

 private JSTypeRegistry typeRegistry;
 private ClosureReverseAbstractInterpreter interpreter;

 @Before
 public void setUp() {
 typeRegistry = new JSTypeRegistry();
 interpreter = new ClosureReverseAbstractInterpreter(null, typeRegistry);
 }

 @Test
 public void testIsDef() {
 // Given a boolean type
 JSType booleanType = typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(booleanType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isDef"), booleanType, null, interpreter.restricters.get("isDef"), true).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the same as the original type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(booleanType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isDef"), booleanType, null, interpreter.restricters.get("isDef"), false).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsDefAndNotNull() {
 // Given a Object type
 JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(objectType, true);
 JSType expectedType = interpreter.getNativeType(JSTypeNative.NO_OBJECT_TYPE)
 .getSubType(interpreter.getNativeType(JSTypeNative.OBJECT_TYPE));
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isDefAndNotNull"), objectType, null, interpreter.restricters.get("isDefAndNotNull"), true)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the subtype of Object but not equal to Object
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertFalse(expectedType.equals(actualType));
 assertSame(expectedType.getClass(), actualType.getClass());

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(objectType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isDefAndNotNull"), objectType, null, interpreter.restricters.get("isDefAndNotNull"), false)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsString() {
 // Given a Object type
 JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(objectType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.STRING_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isString"), objectType, null, interpreter.restricters.get("isString"), true)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the expected type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(objectType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isString"), objectType, null, interpreter.restricters.get("isString"), false)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsBoolean() {
 // Given a Object type
 JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(objectType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isBoolean"), objectType, null, interpreter.restricters.get("isBoolean"), true)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the expected type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(objectType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isBoolean"), objectType, null, interpreter.restricters.get("isBoolean"), false)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsNumber() {
 // Given a Object type
 JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(objectType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isNumber"), objectType, null, interpreter.restricters.get("isNumber"), true)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the expected type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(objectType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isNumber"), objectType, null, interpreter.restricters.get("isNumber"), false)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsFunction() {
 // Given a FunctionType type
 FunctionType functionType = new FunctionType(new ObjectType(null), new ObjectType(null), null);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(functionType, true);
 JSType expectedType = functionType;
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isFunction"), functionType, null, interpreter.restricters.get("isFunction"), true)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the same as the original type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(functionType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isFunction"), functionType, null, interpreter.restricters.get("isFunction"), false)
 .getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsArray() {
 // Given a null type
 JSType nullType = typeRegistry.getNativeType(JSTypeNative.NULL_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(nullType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isArray"), nullType, null, interpreter.restricters.get("isArray"), true).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the expected type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(nullType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isArray"), nullType, null, interpreter.restricters.get("isArray"), false).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }

 @Test
 public void testIsObject() {
 // Given a null type
 JSType nullType = typeRegistry.getNativeType(JSTypeNative.NULL_TYPE);

 // When the outcome is true
 TypeRestriction trueTypeRestriction = new TypeRestriction(nullType, true);
 JSType expectedType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
 JSType actualType = interpreter.restrictParameter(
 new Node(NAME, "isObject"), nullType, null, interpreter.restricters.get("isObject"), true).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be the expected type
 assertNotNull(expectedType);
 assertNotNull(actualType);
 assertSame(expectedType, actualType);

 // When the outcome is false
 TypeRestriction falseTypeRestriction = new TypeRestriction(nullType, false);
 actualType = interpreter.restrictParameter(
 new Node(NAME, "isObject"), nullType, null, interpreter.restricters.get("isObject"), false).getScope().getVariable("result")
 .getType();

 // Then the restricted type should be null
 assertNull(actualType);
 }
}