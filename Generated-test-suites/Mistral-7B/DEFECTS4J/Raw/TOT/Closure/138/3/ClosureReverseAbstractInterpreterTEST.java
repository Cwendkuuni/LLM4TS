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
  public void testIsDefTrue() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType outcome = interpreter.restricters.get("isDef").apply(restriction);

    assertTrue(outcome != null);
    assertTrue(outcome.isSubtype(type));
  }

  @Test
  public void testIsDefFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isDef").apply(restriction);

    assertNull(outcome);
  }

  @Test
  public void testIsDefNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(NULL_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isDef").apply(restriction);

    assertNull(outcome);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType outcome = interpreter.restricters.get("isDefAndNotNull").apply(restriction);

    assertNotNull(outcome);
    assertTrue(outcome.isSubtype(type));
    assertFalse(outcome.isSubtype(interpreter.getNativeType(NULL_TYPE)));
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isDefAndNotNull").apply(restriction);

    assertNull(outcome);
  }

  @Test
  public void testIsDefAndNotNullNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(NULL_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isDefAndNotNull").apply(restriction);

    assertNull(outcome);
  }

  @Test
  public void testIsArrayTrue() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = interpreter.getNativeType(ARRAY_TYPE);
    TypeRestriction restriction = new TypeRestriction(null, true);
    JSType outcome = interpreter.restricters.get("isArray").apply(restriction);

    assertTrue(outcome != null);
    assertTrue(outcome.isSubtype(arrayType));
  }

  @Test
  public void testIsArrayFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isArray").apply(restriction);

    assertNull(outcome);
  }

  @Test
  public void testIsStringTrue() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType stringType = interpreter.getNativeType("string");
    TypeRestriction restriction = new TypeRestriction(null, true);
    JSType outcome = interpreter.restricters.get("isString").apply(restriction);

    assertTrue(outcome != null);
    assertTrue(outcome.isSubtype(stringType));
  }

  @Test
  public void testIsStringFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(OBJECT_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType outcome = interpreter.restricters.get("isString").apply(restriction);

    assertNull(outcome);
  }

  @Test(expected = AssertionError.class)
  public void testUnexpectedType() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = interpreter.getNativeType(FUNCTION_TYPE);
    TypeRestriction restriction = new TypeRestriction(type, true);
    interpreter.restricters.get("isString").apply(restriction);
  }
}