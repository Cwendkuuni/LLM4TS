import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectTypeRegistry;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ObjectTypeRegistry objectTypeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    objectTypeRegistry = mock(ObjectTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    when(typeRegistry.getObjectTypeRegistry()).thenReturn(objectTypeRegistry);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testIsDef_true() {
    JSType type = JSTypeNative.getTopType();
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isDef").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.UNDEFINED_TYPE), result);
  }

  @Test
  public void testIsDef_false() {
    JSType type = JSTypeNative.getTopType();
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isDef").apply(restriction);
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsDefAndNotNull_true() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.STRING_TYPE), result);
  }

  @Test
  public void testIsDefAndNotNull_false() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testIsString_true() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isString").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsString_false() {
    JSType type = JSTypeNative.getBooleanType();
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isString").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testIsBoolean_true() {
    JSType type = JSTypeNative.getBooleanType();
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsBoolean_false() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testIsNumber_true() {
    JSType type = JSTypeNative.getNumberType();
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isNumber").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsNumber_false() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isNumber").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testIsFunction_true() {
    FunctionType functionType = objectTypeRegistry.createFunctionType(
        null, null, VOID_TYPE, null);
    JSType type = functionType;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isFunction").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsFunction_false() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isFunction").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  @Test
  public void testIsArray_true() {
    JSType type = ARRAY_TYPE;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isArray").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsArray_false() {
    JSType type = JSTypeNative.getObjectType("String");
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isArray").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_TYPE), result);
  }

  @Test
  public void testIsObject_true() {
    ObjectType objectType = objectTypeRegistry.createObjectType("String", null);
    JSType type = objectType;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isObject").apply(restriction);
    assertEquals(type, result);
  }

  @Test
  public void testIsObject_false() {
    JSType type = JSTypeNative.getNumberType();
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType result = interpreter.restricters.get("isObject").apply(restriction);
    assertEquals(getNativeType(JSTypeNative.NULL_VOID), result);
  }

  private JSType getNativeType(int typeCode) {
    return interpreter.getTypeRegistry().getNativeType(typeCode);
  }

  private JSType getNativeType(JSTypeNative type) {
    return interpreter.getTypeRegistry().getNativeType(type.getTypeCode());
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