import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;

import java.util.Map;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention codingConvention = new CodingConvention();

  @org.junit.BeforeClass
  public static void setup() {
    typeRegistry.init(codingConvention);
  }

  private static JSType getNativeType(int typeCode) {
    return typeRegistry.getNativeType(typeCode);
  }

  private static void declareType(String name, JSType type, JSTypeRegistry typeRegistry) {
    ObjectType objType = new ObjectType(name + "Type", typeRegistry);
    typeRegistry.addType(objType);
  }

  private static JSType getType(String name, boolean isArray, JSType elementType) {
    JSType type;
    if (isArray) {
      type = ARRAY_TYPE;
      type = type.makeArrayType(elementType);
    } else {
      type = new ObjectType(name, typeRegistry);
    }
    return type;
  }

  private static Node createCallNode(String callee, Node... args) {
    Node callNode = NodeOperations.createCall(callee, args);
    Node[] children = new Node[2];
    children[0] = NodeOperations.createString("isDef");
    children[1] = NodeOperations.createQualifiedName("goog", "isDef");
    callNode.setChildren(children);
    return callNode;
  }

  private static JSType getRestrictedType(String name, boolean isArray, boolean isDef, JSTypeRegistry typeRegistry) {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);
    Type type = new Type(name, null);
    JSType actual = interpreter.getType(createCallNode("isDef", NodeOperations.createQualifiedName("goog", name)), null, null);
    return actual;
  }

  /**
   * Test {@code isDef(undefined)} returns false.
   */
  @org.junit.Test
  public void testIsDefUndefined() {
    JSType actual = getRestrictedType("undefinedType", false, false, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(null)} returns false.
   */
  @org.junit.Test
  public void testIsDefNull() {
    JSType actual = getRestrictedType("nullType", false, false, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(string)} returns true.
   */
  @org.junit.Test
  public void testIsDefString() {
    JSType actual = getRestrictedType("stringType", false, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(number)} returns true.
   */
  @org.junit.Test
  public void testIsDefNumber() {
    JSType actual = getRestrictedType("numberType", false, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(boolean)} returns true.
   */
  @org.junit.Test
  public void testIsDefBoolean() {
    JSType actual = getRestrictedType("booleanType", false, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(nullFunction)} returns false.
   */
  @org.junit.Test
  public void testIsDefNullFunction() {
    FunctionType functionType = new FunctionType(new ObjectType("Function", typeRegistry), VOID_TYPE);
    JSType actual = getRestrictedType("nullFunctionType", false, false, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(function)} returns true.
   */
  @org.junit.Test
  public void testIsDefFunction() {
    FunctionType functionType = new FunctionType(new ObjectType("Function", typeRegistry), VOID_TYPE);
    JSType actual = getRestrictedType("functionType", false, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(array)} returns true.
   */
  @org.junit.Test
  public void testIsDefArray() {
    JSType elementType = getNativeType(NUMBER_TYPE);
    JSType arrayType = getType("arrayType", true, elementType);
    JSType actual = getRestrictedType("arrayType", true, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(object)} returns true.
   */
  @org.junit.Test
  public void testIsDefObject() {
    JSType objectType = getType("objectType", false, null);
    JSType actual = getRestrictedType("objectType", false, true, typeRegistry);
    assertTrue(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(undefined)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefUndefinedIsNull() {
    JSType actual = getRestrictedType("undefinedType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(null)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefNullIsNull() {
    JSType actual = getRestrictedType("nullType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(string)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefStringIsNull() {
    JSType actual = getRestrictedType("stringType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(number)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefNumberIsNull() {
    JSType actual = getRestrictedType("numberType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(boolean)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefBooleanIsNull() {
    JSType actual = getRestrictedType("booleanType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(nullFunction)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefNullFunctionIsNull() {
    FunctionType functionType = new FunctionType(new ObjectType("Function", typeRegistry), VOID_TYPE);
    JSType actual = getRestrictedType("nullFunctionType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(function)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefFunctionIsNull() {
    FunctionType functionType = new FunctionType(new ObjectType("Function", typeRegistry), VOID_TYPE);
    JSType actual = getRestrictedType("functionType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(array)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefArrayIsNull() {
    JSType elementType = getNativeType(NUMBER_TYPE);
    JSType arrayType = getType("arrayType", true, elementType);
    JSType actual = getRestrictedType("arrayType", true, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }

  /**
   * Test {@code isDef(object)} returns false with {@code isNull: true}.
   */
  @org.junit.Test
  public void testIsDefObjectIsNull() {
    JSType objectType = getType("objectType", false, null);
    JSType actual = getRestrictedType("objectType", false, true, true, typeRegistry);
    assertFalse(actual.isSubtype(getNativeType(BOOLEAN_TYPE).getTrue()));
  }
}