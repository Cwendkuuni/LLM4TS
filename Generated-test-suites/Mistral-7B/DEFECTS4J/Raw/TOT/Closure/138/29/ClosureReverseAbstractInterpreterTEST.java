import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType originalType = typeRegistry.getNativeType(Object.class);
    JSType expectedType = interpreter.getRestrictedWithoutUndefined(originalType);
    assertTrue(expectedType != null);

    // false case
    JSType nullType = typeRegistry.getNativeType(Null.class);
    JSType restrictedType = interpreter.getRestrictedWithoutUndefined(nullType);
    assertNull(restrictedType);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType originalType = typeRegistry.getNativeType(Object.class);
    JSType expectedType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(originalType));
    assertTrue(expectedType != null);

    // false case
    JSType nullType = typeRegistry.getNativeType(Null.class);
    JSType objectType = typeRegistry.getNativeType(Object.class);
    JSType restrictedType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(nullType));
    assertNull(restrictedType);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType arrayType = typeRegistry.getNativeType(int[].class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        arrayType, "array", true);
    assertTrue(expectedType != null);

    // false case
    JSType stringType = typeRegistry.getNativeType(String.class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        stringType, "array", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType objectType = typeRegistry.getNativeType(Object.class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        objectType, "object", true);
    assertTrue(expectedType != null);

    // false case
    JSType functionType = typeRegistry.getNativeType(Function.class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        functionType, "object", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType stringType = typeRegistry.getNativeType(String.class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        stringType, "string", true);
    assertTrue(expectedType != null);

    // false case
    JSType numberType = typeRegistry.getNativeType(Number.class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        numberType, "string", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType booleanType = typeRegistry.getNativeType(boolean.class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        booleanType, "boolean", true);
    assertTrue(expectedType != null);

    // false case
    JSType arrayType = typeRegistry.getNativeType(int[].class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        arrayType, "boolean", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType numberType = typeRegistry.getNativeType(Number.class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        numberType, "number", true);
    assertTrue(expectedType != null);

    // false case
    JSType stringType = typeRegistry.getNativeType(String.class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        stringType, "number", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType functionType = typeRegistry.getNativeType(Function.class);
    JSType expectedType = interpreter.getRestrictedByTypeOfResult(
        functionType, "function", true);
    assertTrue(expectedType != null);

    // false case
    JSType stringType = typeRegistry.getNativeType(String.class);
    JSType expectedType2 = interpreter.getRestrictedByTypeOfResult(
        stringType, "function", false);
    assertNull(expectedType2);
  }

  @Test
  public void testIsNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true case
    JSType nullType = typeRegistry.getNativeType(Null.class);
    JSType expectedType = interpreter.getRestrictedWithoutNull(nullType);
    assertNotNull(expectedType);

    // false case
    JSType objectType = typeRegistry.getNativeType(Object.class);
    JSType expectedType2 = interpreter.getRestrictedWithoutNull(objectType);
    assertNull(expectedType2);
  }
}