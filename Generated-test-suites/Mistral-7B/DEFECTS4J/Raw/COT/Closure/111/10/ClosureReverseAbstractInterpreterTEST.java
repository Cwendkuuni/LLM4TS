package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

/**
 * Test class for ClosureReverseAbstractInterpreter.
 */
public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testIsDefTrue() {
    JSType inputType = getNativeType(ObjectType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(ObjectType.class);

    JSType outputType = interpreter.getRestrictedWithoutUndefined(typeRestriction.type);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsDefFalse() {
    JSType inputType = getNativeType(VoidType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedWithoutUndefined(typeRestriction.type);
    assertNull(outputType);
  }

  @Test
  public void testIsNullTrue() {
    JSType inputType = getNativeType(ObjectType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(NullType.class);

    JSType outputType = interpreter.getRestrictedWithoutNull(typeRestriction.type);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsNullFalse() {
    JSType inputType = getNativeType(NullType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedWithoutNull(typeRestriction.type);
    assertNull(outputType);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    JSType inputType = getNativeType(ObjectType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(ObjectType.class);

    JSType outputType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(typeRestriction.type));
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    JSType inputType = getNativeType(NullVoidType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(typeRestriction.type));
    assertNull(outputType);
  }

  @Test
  public void testIsStringTrue() {
    JSType inputType = getNativeType(StringType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(StringType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "string", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsStringFalse() {
    JSType inputType = getNativeType(NumberType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "string", false);
    assertNull(outputType);
  }

  @Test
  public void testIsBooleanTrue() {
    JSType inputType = getNativeType(BooleanType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(BooleanType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "boolean", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsBooleanFalse() {
    JSType inputType = getNativeType(NumberType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "boolean", false);
    assertNull(outputType);
  }

  @Test
  public void testIsNumberTrue() {
    JSType inputType = getNativeType(NumberType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(NumberType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "number", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsNumberFalse() {
    JSType inputType = getNativeType(StringType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "number", false);
    assertNull(outputType);
  }

  @Test
  public void testIsFunctionTrue() {
    JSType inputType = getNativeType(FunctionType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(FunctionType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "function", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsFunctionFalse() {
    JSType inputType = getNativeType(ArrayType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "function", false);
    assertNull(outputType);
  }

  @Test
  public void testIsArrayTrue() {
    JSType inputType = getNativeType(ArrayType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(ArrayType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "array", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsArrayFalse() {
    JSType inputType = getNativeType(ObjectType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "array", false);
    assertNull(outputType);
  }

  @Test
  public void testIsObjectTrue() {
    JSType inputType = getNativeType(ObjectType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType expectedOutputType = getNativeType(ObjectType.class);

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "object", true);
    assertEquals(expectedOutputType, outputType);
  }

  @Test
  public void testIsObjectFalse() {
    JSType inputType = getNativeType(FunctionType.class);
    TypeRestriction typeRestriction = new TypeRestriction(inputType, false);
    JSType expectedOutputType = null;

    JSType outputType = interpreter.getRestrictedByTypeOfResult(typeRestriction.type, "object", false);
    assertNull(outputType);
  }

  private static JSType getNativeType(Class<?> clazz) {
    return typeRegistry.getNativeType(clazz);
  }

  private static JSType getTypeIfRefinable(Node node, FlowScope scope) {
    return interpreter.getTypeIfRefinable(node, scope);
  }
}

class TypeRestriction {
  private final JSType type;
  private final boolean outcome;

  private TypeRestriction(JSType type, boolean outcome) {
    this.type = type;
    this.outcome = outcome;
  }
}

class TestData {
  static Map<String, Function<TypeRestriction, JSType>> restricters =
      ImmutableMap.<String, Function<TypeRestriction, JSType>>builder()
          .put("isDef", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                  return p.type != null ?
                      interpreter.getRestrictedWithoutUndefined(p.type) : null;
                } else {
                  return p.type != null ?
                      getNativeType(VoidType.class).getGreatestSubtype(p.type) : null;
                }
              }
          })
          .put("isNull", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                  return p.type != null ?
                      getNativeType(NullType.class).getGreatestSubtype(p.type) : null;
                } else {
                  return interpreter.getRestrictedWithoutNull(p.type);
                }
              }
          })
          .put("isDefAndNotNull", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                if (p.outcome) {
                  return interpreter.getRestrictedWithoutUndefined(
                      interpreter.getRestrictedWithoutNull(p.type));
                } else {
                  return getNativeType(NullVoidType.class).getGreatestSubtype(p.type);
                }
              }
          })
          .put("isString", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                return interpreter.getRestrictedByTypeOfResult(p.type, "string", p.outcome);
              }
          })
          .put("isBoolean", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                return interpreter.getRestrictedByTypeOfResult(p.type, "boolean", p.outcome);
              }
          })
          .put("isNumber", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                return interpreter.getRestrictedByTypeOfResult(p.type, "number", p.outcome);
              }
          })
          .put("isFunction", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                return interpreter.getRestrictedByTypeOfResult(p.type, "function", p.outcome);
              }
          })
          .put("isArray", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                if (p.type == null) {
                  return p.outcome ? getNativeType(ArrayType.class) : null;
                }

                Visitor<JSType> visitor = p.outcome ? interpreter.restrictToArrayVisitor :
                    interpreter.restrictToNotArrayVisitor;
                return p.type.visit(visitor);
              }
          })
          .put("isObject", new Function<TypeRestriction, JSType>() {
              @Override
              public JSType apply(TypeRestriction p) {
                if (p.type == null) {
                  return p.outcome ? getNativeType(ObjectType.class) : null;
                }

                Visitor<JSType> visitor = p.outcome ? interpreter.restrictToObjectVisitor :
                    interpreter.restrictToNotObjectVisitor;
                return p.type.visit(visitor);
              }
          })
          .build();
}