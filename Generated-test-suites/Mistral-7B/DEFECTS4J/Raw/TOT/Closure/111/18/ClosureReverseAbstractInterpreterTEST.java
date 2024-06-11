import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSType.*;
import static com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final CodingConvention convention = new CodingConvention();
  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isDef",
        Node.newQualifiedName("myObject", "myProperty"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(VOID_TYPE, type);
    assertTrue(scope.canAssume(TYPES, VOID_TYPE, "myObject.myProperty"));

    // edge case: undefined property
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
    assertFalse(scope.canAssume(TYPES, ANY_TYPE, "myObject.myProperty"));

    // error case: non-existent property
    scope.reset();
    condition = Node.newCall( Node.newName("goog"), "isDef",
        Node.newQualifiedName("myObject", "nonexistentProperty"));
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
    assertFalse(scope.canAssume(TYPES, ANY_TYPE, "myObject.nonexistentProperty"));
  }

  @Test
  public void testIsNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isNull",
        Node.newName("myVariable"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE, "goog.isNull(myVariable)"));

    // edge case: null value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertTrue(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE,
        "goog.isNull(null)"));

    // error case: non-null value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertFalse(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
  }

  @Test
  public void testIsDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isDefAndNotNull",
        Node.newName("myVariable"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(VOID_TYPE, type);
    assertTrue(scope.canAssume(TYPES, VOID_TYPE,
        "goog.isDefAndNotNull(myVariable)"));

    // edge case: null value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
    assertFalse(scope.canAssume(TYPES, ANY_TYPE,
        "goog.isDefAndNotNull(null)"));

    // error case: undefined value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
    assertFalse(scope.canAssume(TYPES, ANY_TYPE,
        "goog.isDefAndNotNull(undefined)"));
  }

  @Test
  public void testIsString() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isString",
        Node.newString("myString"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE,
        "goog.isString('myString')"));

    // edge case: non-string value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertFalse(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
  }

  @Test
  public void testIsBoolean() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isBoolean",
        Node.newName("myBooleanVariable"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE,
        "goog.isBoolean(myBooleanVariable)"));

    // edge case: non-boolean value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertFalse(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
  }

  @Test
  public void testIsNumber() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isNumber",
        Node.newName("myNumberVariable"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE,
        "goog.isNumber(myNumberVariable)"));

    // edge case: non-number value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertFalse(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
  }

  @Test
  public void testIsFunction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isFunction",
        Node.newName("myFunction"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(scope.canAssume(TYPES, BOOLEAN_TYPE,
        "goog.isFunction(myFunction)"));

    // edge case: non-function value
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertFalse(type.isSubtype(getNativeType(BOOLEAN_TYPE).getTrueValue()));
  }

  @Test
  public void testIsArray() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isArray",
        Node.newName("myArray"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case: array
    scope.reset();
    assertEquals(ARRAY_TYPE, type);
    assertTrue(scope.canAssume(TYPES, ARRAY_TYPE, "goog.isArray(myArray)"));

    // edge case: non-array
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
  }

  @Test
  public void testIsObject() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall( Node.newName("goog"), "isObject",
        Node.newName("myObject"));

    FlowScope scope = new FlowScope(typeRegistry.createTopType());
    Type type = interpreter.getType(condition, null, scope);

    // typical use case: object
    scope.reset();
    assertEquals(OBJECT_TYPE, type);
    assertTrue(scope.canAssume(TYPES, OBJECT_TYPE, "goog.isObject(myObject)"));

    // edge case: non-object
    scope.reset();
    type = interpreter.getType(condition, null, scope);
    assertNull(type);
  }
}