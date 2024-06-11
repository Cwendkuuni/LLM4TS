import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType inputType = JSTypeNative.createUndefinedType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = interpreter.getRestrictedWithoutUndefined(inputType);

    assertEquals(expectedOutput, interpreter.restricters.get("isDef").apply(typeRestriction));
  }

  @Test
  public void testIsDefFalse() {
    JSType inputType = JSTypeNative.createNumberType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isDef").apply(typeRestriction));
  }

  @Test
  public void testIsNotNull() {
    JSType inputType = JSTypeNative.createNullType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = interpreter.getRestrictedWithoutNull(inputType);

    assertEquals(expectedOutput, interpreter.restricters.get("isNull").apply(typeRestriction));
  }

  @Test
  public void testIsNotNullFalse() {
    JSType inputType = JSTypeNative.createStringType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.OBJECT_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isNull").apply(typeRestriction));
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType inputType = JSTypeNative.createBooleanType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = interpreter.getRestrictedWithoutUndefined(
        inputType.getGreatestSubtype(JSTypeNative.createNullType()));

    assertEquals(expectedOutput, interpreter.restricters.get("isDefAndNotNull").apply(typeRestriction));
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    JSType inputType = JSTypeNative.createFunctionType(new FunctionType(null, null));
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.NULL_VOID;

    assertEquals(expectedOutput, interpreter.restricters.get("isDefAndNotNull").apply(typeRestriction));
  }

  @Test
  public void testIsString() {
    JSType inputType = JSTypeNative.createNumberType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.createStringType();

    assertEquals(expectedOutput, interpreter.restricters.get("isString").apply(typeRestriction));
  }

  @Test
  public void testIsStringFalse() {
    JSType inputType = JSTypeNative.createStringType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isString").apply(typeRestriction));
  }

  @Test
  public void testIsBoolean() {
    JSType inputType = JSTypeNative.createStringType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.createBooleanType();

    assertEquals(expectedOutput, interpreter.restricters.get("isBoolean").apply(typeRestriction));
  }

  @Test
  public void testIsBooleanFalse() {
    JSType inputType = JSTypeNative.createBooleanType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isBoolean").apply(typeRestriction));
  }

  @Test
  public void testIsNumber() {
    JSType inputType = JSTypeNative.createStringType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.createNumberType();

    assertEquals(expectedOutput, interpreter.restricters.get("isNumber").apply(typeRestriction));
  }

  @Test
  public void testIsNumberFalse() {
    JSType inputType = JSTypeNative.createNumberType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isNumber").apply(typeRestriction));
  }

  @Test
  public void testIsFunction() {
    JSType inputType = JSTypeNative.createStringType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.createFunctionType(new FunctionType(null, null));

    assertEquals(expectedOutput, interpreter.restricters.get("isFunction").apply(typeRestriction));
  }

  @Test
  public void testIsFunctionFalse() {
    JSType inputType = JSTypeNative.createFunctionType(new FunctionType(null, null));
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isFunction").apply(typeRestriction));
  }

  @Test
  public void testIsArray() {
    JSType inputType = JSTypeNative.createObjectType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.ARRAY_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isArray").apply(typeRestriction));
  }

  @Test
  public void testIsArrayFalse() {
    JSType inputType = JSTypeNative.createArrayType(new JSType[] {});
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isArray").apply(typeRestriction));
  }

  @Test
  public void testIsObject() {
    JSType inputType = JSTypeNative.createArrayType(new JSType[] {});
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = JSTypeNative.OBJECT_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isObject").apply(typeRestriction));
  }

  @Test
  public void testIsObjectFalse() {
    JSType inputType = JSTypeNative.createObjectType();
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = JSTypeNative.VOID_TYPE;

    assertEquals(expectedOutput, interpreter.restricters.get("isObject").apply(typeRestriction));
  }
}