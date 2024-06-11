package org.apache.commons.cli;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private static final CodingConvention CONVENTION = new CodingConvention();
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private ClosureReverseAbstractInterpreter interpreter;
  private JSType nullType;
  private JSType arrayType;
  private JSType booleanType;
  private JSType numberType;
  private JSType stringType;
  private JSType functionType;
  private JSType objectType;
  private JSType voidType;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
    nullType = TYPE_REGISTRY.createNullType();
    arrayType = TYPE_REGISTRY.createArrayType(TYPE_REGISTRY.createStringType());
    booleanType = TYPE_REGISTRY.createBooleanType();
    numberType = TYPE_REGISTRY.createNumberType();
    stringType = TYPE_REGISTRY.createStringType();
    functionType = new FunctionType(new ObjectType(TYPE_REGISTRY.createStringType()),
        VOID_TYPE);
    objectType = new ObjectType(TYPE_REGISTRY.createStringType());
    voidType = TYPE_REGISTRY.createVoidType();
  }

  @Test
  public void testRestrictByTrueTypeOfResultVisitor_array() {
    JSType input = arrayType;
    JSType expectedOutput = arrayType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDef").apply(
        new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTrueTypeOfResultVisitor_nonArray() {
    JSType input = objectType;
    JSType expectedOutput = objectType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDef").apply(
        new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByFalseTypeOfResultVisitor_array() {
    JSType input = arrayType;
    JSType expectedOutput = null;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDef")
        .apply(new TypeRestriction(input, false));

    assertNull(actualOutput);
  }

  @Test
  public void testRestrictByFalseTypeOfResultVisitor_nonArray() {
    JSType input = objectType;
    JSType expectedOutput = voidType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDef")
        .apply(new TypeRestriction(input, false));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_string() {
    JSType input = stringType;
    JSType expectedOutput = stringType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isString")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_number() {
    JSType input = numberType;
    JSType expectedOutput = numberType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isNumber")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_boolean() {
    JSType input = booleanType;
    JSType expectedOutput = booleanType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isBoolean")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_function() {
    JSType input = functionType;
    JSType expectedOutput = functionType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isFunction")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_null() {
    JSType input = nullType;
    JSType expectedOutput = null;

    JSType actualOutput = interpreter.getVisitorForRestriction("isNull")
        .apply(new TypeRestriction(input, false));

    assertNull(actualOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_nonNull() {
    JSType input = objectType;
    JSType expectedOutput = objectType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isNull")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_isDefAndNotNull() {
    JSType input = objectType;
    JSType expectedOutput = null;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDefAndNotNull")
        .apply(new TypeRestriction(input, false));

    assertNull(actualOutput);
  }

  @Test
  public void testRestrictByTypeOfResultVisitor_isDefAndNotNull_true() {
    JSType input = objectType;
    JSType expectedOutput = objectType;

    JSType actualOutput = interpreter.getVisitorForRestriction("isDefAndNotNull")
        .apply(new TypeRestriction(input, true));

    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  private static class TypeRestriction {
    private final JSType actualType;
    private final boolean outcome;

    public TypeRestriction(JSType actualType, boolean outcome) {
      this.actualType = actualType;
      this.outcome = outcome;
    }
  }
}