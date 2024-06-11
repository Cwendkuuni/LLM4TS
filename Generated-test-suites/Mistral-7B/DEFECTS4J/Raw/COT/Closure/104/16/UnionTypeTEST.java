package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Set;

public class UnionTypeTest {

  private UnionTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setup() {
    registry = Mockito.mock(UnionTypeRegistry.class);
    errorReporter = Mockito.mock(ErrorReporter.class);
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN);
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getBuiltins().getStringType();
    JSType booleanType = registry.getBuiltins().getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    String propertyName = "length";
    JSType expectedType = stringType;
    JSType actualType = unionType.findPropertyType(propertyName);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testCanAssignToNull() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertFalse(unionType.canAssignTo(JSTypeNative.NULL));
  }

  @Test
  public void testCanAssignToString() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertTrue(unionType.canAssignTo(JSTypeNative.STRING));
  }

  @Test
  public void testCanAssignToNumber() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertTrue(unionType.canAssignTo(JSTypeNative.NUMBER));
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    JSType expectedType = JSTypeNative.OBJECT;
    JSType actualType = unionType.getLeastSupertype(JSTypeNative.OBJECT);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = TypeError.class)
  public void testGetLeastSupertypeInvalidType() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    JSType invalidType = new Object() {}.getClass();
    unionType.getLeastSupertype(invalidType);
  }

  @Test
  public void testEquals() {
    UnionType unionType1 = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    UnionType unionType2 = getUnionType(JSTypeNative.STRING, JSTypeNative.NUMBER);
    assertFalse(unionType1.equals(unionType2));

    UnionType unionType3 = getUnionType(JSTypeNative.STRING, JSTypeNative.STRING);
    assertTrue(unionType1.equals(unionType3));
  }

  @Test
  public void testHashCode() {
    UnionType unionType1 = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    UnionType unionType2 = getUnionType(JSTypeNative.STRING, JSTypeNative.NUMBER);
    int hashCode1 = unionType1.hashCode();
    int hashCode2 = unionType2.hashCode();
    assertNotEquals(hashCode1, hashCode2);

    UnionType unionType3 = getUnionType(JSTypeNative.STRING, JSTypeNative.STRING);
    int hashCode3 = unionType3.hashCode();
    assertEquals(hashCode1, hashCode3);
  }

  @Test
  public void testIsUnionType() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertTrue(unionType.isObject());
  }

  @Test
  public void testIsSubtype() {
    UnionType unionType1 = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    UnionType unionType2 = getUnionType(JSTypeNative.STRING, JSTypeNative.NUMBER);
    UnionType unionType3 = getUnionType(JSTypeNative.STRING, JSTypeNative.STRING);

    assertTrue(unionType1.isSubtype(JSTypeNative.NUMBER));
    assertTrue(unionType1.isSubtype(JSTypeNative.STRING));
    assertTrue(unionType1.isSubtype(unionType3));

    assertFalse(unionType1.isSubtype(JSTypeNative.NULL));
    assertFalse(unionType1.isSubtype(JSTypeNative.BOOLEAN));

    assertTrue(unionType2.isSubtype(JSTypeNative.STRING));
    assertTrue(unionType2.isSubtype(JSTypeNative.NUMBER));
    assertTrue(unionType2.isSubtype(unionType3));

    assertFalse(unionType2.isSubtype(JSTypeNative.NULL));
    assertFalse(unionType2.isSubtype(JSTypeNative.BOOLEAN));

    assertTrue(unionType3.isSubtype(JSTypeNative.STRING));
    assertTrue(unionType3.isSubtype(JSTypeNative.STRING));
    assertTrue(unionType3.isSubtype(unionType1));
    assertTrue(unionType3.isSubtype(unionType2));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    JSType numberType = JSTypeNative.NUMBER;
    JSType stringType = JSTypeNative.STRING;

    assertEquals(unionType, unionType.getRestrictedUnion(numberType));
    assertEquals(stringType, unionType.getRestrictedUnion(stringType));
    assertEquals(JSTypeNative.VOID, unionType.getRestrictedUnion(JSTypeNative.VOID));
  }

  @Test
  public void testToString() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    String expectedString = "(number|string)";
    String actualString = unionType.toString();
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testMeet() {
    UnionType unionType1 = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    UnionType unionType2 = getUnionType(JSTypeNative.STRING, JSTypeNative.NUMBER);

    UnionType expectedType = getUnionType(JSTypeNative.STRING, JSTypeNative.NUMBER);
    UnionType actualType = unionType1.meet(unionType2);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictedByUnknownType() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertTrue(unionType.isUnknownType());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertFalse(restrictedType.isUnknownType());
  }

  @Test
  public void testRestrictedByNullType() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertFalse(unionType.isNullable());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertFalse(restrictedType.isNullable());

    UnionType nullableType = getUnionType(JSTypeNative.NULL, JSTypeNative.STRING);
    assertTrue(nullableType.isNullable());

    UnionType restrictedNullableType = nullableType.restrictByNotNullOrUndefined();
    assertFalse(restrictedNullableType.isNullable());
  }

  @Test
  public void testRestrictedByBooleanType() {
    UnionType unionType = getUnionType(JSTypeNative.NUMBER, JSTypeNative.STRING);
    assertFalse(unionType.isUnknownType());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertFalse(restrictedType.isUnknownType());

    UnionType booleanType = JSTypeNative.BOOLEAN;
    UnionType restrictedBooleanType = unionType.restrictByNotNullOrUndefined();
    UnionType restrictedBooleanTypeWithBoolean = restrictedBooleanType.getRestrictedUnion(booleanType);
    assertTrue(restrictedBooleanTypeWithBoolean.isUnknownType());

    UnionType restrictedBooleanTypeWithoutBoolean = restrictedBooleanType.getRestrictedUnion(JSTypeNative.NUMBER);
    assertFalse(restrictedBooleanTypeWithoutBoolean.isUnknownType());
  }

  private UnionType getUnionType(JSType... types) {
    UnionTypeRegistry registry = new UnionTypeRegistry() {
      @Override
      public JSType getBuiltins() {
        return new JSTypeRegistry.Builtins() {
          @Override
          public JSType getNullType() {
            return JSTypeNative.NULL;
          }

          @Override
          public JSType getBooleanType() {
            return JSTypeNative.BOOLEAN;
          }

          @Override
          public JSType getNumberType() {
            return JSTypeNative.NUMBER;
          }

          @Override
          public JSType getStringType() {
            return JSTypeNative.STRING;
          }

          @Override
          public JSType getVoidType() {
            return JSTypeNative.VOID;
          }
        };
      }
    };

    Set<JSType> alternates = ImmutableSet.copyOf(Arrays.asList(types));
    return new UnionType(registry, alternates);
  }
}