import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSType.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.Node;

/**
 * Test class for ClosureReverseAbstractInterpreter class.
 */
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = INT_TYPE;
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = getNativeType(BOOLEAN_TYPE);
    JSType actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = NULL_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = NULL_VOID;
    actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = STRING_TYPE;
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = inputType;
    JSType actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "string", true);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = BOOLEAN_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "string", false);
    assertNull(actualOutput);

    inputType = STRING_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "string", false);
    assertNull(actualOutput);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = FUNCTION_TYPE(INT_TYPE, VOID_TYPE);
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = inputType;
    JSType actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "function", true);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = BOOLEAN_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "function", false);
    assertNull(actualOutput);

    inputType = FUNCTION_TYPE(INT_TYPE, VOID_TYPE);
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "function", false);
    assertNull(actualOutput);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = ARRAY_TYPE(INT_TYPE);
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = inputType;
    JSType actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "array", true);
    assertEquals(expectedOutput, actualOutput);

    inputType = STRING_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = NULL_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "array", false);
    assertNull(actualOutput);

    inputType = NULL_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = ARRAY_TYPE(INT_TYPE);
    actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertNull(actualOutput);

    inputType = ARRAY_TYPE(INT_TYPE);
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertNull(actualOutput);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = OBJECT_TYPE;
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = inputType;
    JSType actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "object", true);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedByTypeOfResult(inputType, "object", false);
    assertNull(actualOutput);

    inputType = OBJECT_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = VOID_TYPE;
    actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertNull(actualOutput);
  }

  @Test
  public void testIsNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = INT_TYPE;
    TypeRestriction restriction = new TypeRestriction(inputType, false);
    JSType expectedOutput = NULL_TYPE;
    JSType actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = NULL_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = NULL_VOID;
    actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, true);
    expectedOutput = BOOLEAN_TYPE;
    actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertNull(actualOutput);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention codingConvention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        codingConvention, typeRegistry);

    JSType inputType = INT_TYPE;
    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType expectedOutput = INT_TYPE;
    JSType actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = NULL_VOID;
    actualOutput = interpreter.getRestrictedWithoutUndefined(inputType);
    assertEquals(expectedOutput, actualOutput);

    inputType = INT_TYPE;
    restriction = new TypeRestriction(inputType, false);
    expectedOutput = NULL_VOID;
    actualOutput = interpreter.getRestrictedWithoutNull(inputType);
    assertEquals(expectedOutput, actualOutput);
  }
}