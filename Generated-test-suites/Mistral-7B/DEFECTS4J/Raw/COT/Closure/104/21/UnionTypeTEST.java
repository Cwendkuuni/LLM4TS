import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private JSTypeRegistry registry;
  private UnionType intStringUnion;
  private UnionType nullUndefinedUnion;
  private UnionType nullBooleanUnion;
  private UnionType nullErrorUnion;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    intStringUnion = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    nullUndefinedUnion = new UnionType(registry, ImmutableSet.of(JSType.NULL, JSType.UNDEFINED));
    nullBooleanUnion = new UnionType(registry, ImmutableSet.of(JSType.NULL, JSType.BOOLEAN));
    nullErrorUnion = new UnionType(registry, ImmutableSet.of(JSType.NULL, JSType.ERROR));
  }

  @Test
  public void testGetAlternates() {
    assertEquals(ImmutableSet.of(JSType.INT, JSType.STRING), intStringUnion.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    intStringUnion.forgiveUnknownNames();
    nullUndefinedUnion.forgiveUnknownNames();
    nullBooleanUnion.forgiveUnknownNames();
    nullErrorUnion.forgiveUnknownNames();

    // No exceptions should be thrown.
  }

  @Test
  public void testMatchesNumberContext() {
    assertTrue(intStringUnion.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    assertTrue(intStringUnion.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    assertTrue(intStringUnion.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    // Test with known properties.
    String propertyName = "length";
    JSType expected = JSType.NUMBER;
    assertEquals(expected, intStringUnion.findPropertyType(propertyName));

    // Test with unknown properties.
    propertyName = "toString";
    assertNull(intStringUnion.findPropertyType(propertyName));
  }

  @Test
  public void testCanAssignTo() {
    // Tests with null and undefined.
    assertFalse(intStringUnion.canAssignTo(JSType.NULL));
    assertFalse(intStringUnion.canAssignTo(JSType.UNDEFINED));

    // Tests with alternates.
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;
    assertTrue(intStringUnion.canAssignTo(intType));
    assertTrue(intStringUnion.canAssignTo(stringType));

    // Test with a subtype of an alternate.
    JSType numberType = JSTypeNative.getBuiltinType(JSTypeNative.Number);
    assertTrue(intStringUnion.canAssignTo(numberType));

    // Test with a supertype of an alternate.
    JSType objectType = JSType.OBJECT;
    assertTrue(intStringUnion.canAssignTo(objectType));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue(intStringUnion.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType restrictedIntString = intStringUnion.restrictByNotNullOrUndefined();
    assertTrue(restrictedIntString.isSubtype(intStringUnion));
    assertFalse(restrictedIntString.isSubtype(JSType.NULL));
    assertFalse(restrictedIntString.isSubtype(JSType.UNDEFINED));
  }

  @Test
  public void testTestForEquality() {
    JSType otherIntString = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;
    JSType numberType = JSTypeNative.getBuiltinType(JSTypeNative.Number);

    TernaryValue result = intStringUnion.testForEquality(intType);
    assertEquals(TernaryValue.TRUE, result);

    result = intStringUnion.testForEquality(stringType);
    assertEquals(TernaryValue.TRUE, result);

    result = intStringUnion.testForEquality(numberType);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = intStringUnion.testForEquality(otherIntString);
    assertEquals(TernaryValue.TRUE, result);

    result = intStringUnion.testForEquality(JSType.NULL);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = intStringUnion.testForEquality(JSType.UNDEFINED);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = intStringUnion.testForEquality(JSType.ERROR);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = intStringUnion.testForEquality(JSType.BOOLEAN);
    assertEquals(TernaryValue.UNKNOWN, result);
  }

  @Test
  public void testIsNullable() {
    assertFalse(intStringUnion.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    assertFalse(intStringUnion.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;
    JSType numberType = JSTypeNative.getBuiltinType(JSTypeNative.Number);
    JSType objectType = JSType.OBJECT;

    JSType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));
    JSType intOrNumber = new UnionType(registry, ImmutableSet.of(intType, numberType));
    JSType intOrObject = new UnionType(registry, ImmutableSet.of(intType, objectType));

    assertEquals(intOrNumber, intOrString.getLeastSupertype(numberType));
    assertEquals(intOrObject, intOrString.getLeastSupertype(objectType));
    assertEquals(intStringUnion, intOrString.getLeastSupertype(intStringUnion));
  }

  @Test
  public void testMeet() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;
    JSType numberType = JSTypeNative.getBuiltinType(JSTypeNative.Number);
    JSType errorType = JSType.ERROR;

    JSType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));
    JSType intOrNumber = new UnionType(registry, ImmutableSet.of(intType, numberType));
    JSType intOrError = new UnionType(registry, ImmutableSet.of(intType, errorType));

    JSType intOrStringOrNumber = intOrString.meet(intOrNumber);
    assertEquals(intOrNumber, intOrStringOrNumber);

    JSType intOrStringOrError = intOrString.meet(intOrError);
    assertNull(intOrStringOrError);
  }

  @Test
  public void testEquals() {
    UnionType intString1 = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intString2 = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intString3 = new UnionType(registry, ImmutableSet.of(JSType.STRING, JSType.INT));
    UnionType intOrError = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.ERROR));

    assertTrue(intString1.equals(intString2));
    assertFalse(intString1.equals(intOrError));
  }

  @Test
  public void testHashCode() {
    UnionType intString1 = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intString2 = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intString3 = new UnionType(registry, ImmutableSet.of(JSType.STRING, JSType.INT));
    UnionType intOrError = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.ERROR));

    assertEquals(intString1.hashCode(), intString2.hashCode());
    assertNotEquals(intString1.hashCode(), intOrError.hashCode());
  }

  @Test
  public void testIsUnionType() {
    assertTrue(intStringUnion.isUnionType());
  }

  @Test
  public void testIsObject() {
    assertTrue(intStringUnion.isObject());
  }

  @Test
  public void testToString() {
    assertEquals("(INT|STRING)", intStringUnion.toString());
  }

  @Test
  public void testIsSubtype() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;
    JSType numberType = JSTypeNative.getBuiltinType(JSTypeNative.Number);
    JSType errorType = JSType.ERROR;

    assertTrue(intOrString.isSubtype(intType));
    assertTrue(intOrString.isSubtype(stringType));
    assertFalse(intOrString.isSubtype(numberType));
    assertFalse(intOrString.isSubtype(errorType));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;

    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));

    UnionType intOnly = intOrString.getRestrictedUnion(intType);

    assertEquals(intType, intOnly);
  }

  @Test
  public void testGetTypesUnderEquality() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;

    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType intTypeA = new UnionType(registry, ImmutableSet.of(intType));
    JSType stringTypeB = new UnionType(registry, ImmutableSet.of(stringType));

    TypePair result = intOrString.getTypesUnderEquality(intType);

    assertSame(intTypeA, result.typeA);
    assertSame(stringTypeB, result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;

    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType intTypeA = new UnionType(registry, ImmutableSet.of(intType));
    JSType stringTypeB = new UnionType(registry, ImmutableSet.of(stringType));

    TypePair result = intOrString.getTypesUnderInequality(stringType);

    assertNull(result.typeA);
    assertSame(intTypeA, result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    JSType intType = JSType.INT;
    JSType stringType = JSType.STRING;

    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType intTypeA = new UnionType(registry, ImmutableSet.of(intType));
    JSType stringTypeB = new UnionType(registry, ImmutableSet.of(stringType));

    TypePair result = intOrString.getTypesUnderShallowInequality(stringType);

    assertNull(result.typeA);
    assertSame(intTypeA, result.typeB);
  }

  @Test(expected = AssertionError.class)
  public void testVisit_UnsupportedType() {
    intStringUnion.visit(new Visitor<Object>() {
      @Override
      public Object caseUnionType(UnionType unionType) {
        fail("Should not reach this point.");
      }

      @Override
      public Object caseNullType(JSType nullType) {
        return null;
      }

      @Override
      public Object caseObjectType(JSType objectType) {
        return null;
      }

      @Override
      public Object caseBuiltinType(JSTypeNative builtinType) {
        return null;
      }

      @Override
      public Object caseVoidType(JSTypeVoid voidType) {
        return null;
      }

      @Override
      public Object caseArrayType(JSTypeArray arrayType) {
        return null;
      }

      @Override
      public Object caseFunctionType(JSTypeFunction functionType) {
        return null;
      }

      @Override
      public Object caseTypeVariable(JSTypeVariable variable) {
        return null;
      }

      @Override
      public Object caseRecordType(JSTypeRecord recordType) {
        return null;
      }

      @Override
      public Object caseAnyType(JSTypeAny anyType) {
        return null;
      }
    });
  }

  @Test
  public void testResolveInternal() {
    // Tests with no circular dependencies.
    UnionType intString = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intOrNull = new UnionType(registry, ImmutableSet.of(JSType.INT, JSType.NULL));
    UnionType stringOrNull = new UnionType(registry, ImmutableSet.of(JSType.STRING, JSType.NULL));

    intString.resolveInternal(registry.getErrorReporter(), registry.getEmptyScope());
    intOrNull.resolveInternal(registry.getErrorReporter(), registry.getEmptyScope());
    stringOrNull.resolveInternal(registry.getErrorReporter(), registry.getEmptyScope());

    // Tests with circular dependencies.
    UnionType circleA = new UnionType(registry, ImmutableSet.of(circleB));
    UnionType circleB = new UnionType(registry, ImmutableSet.of(circleA));

    try {
      circleA.resolveInternal(registry.getErrorReporter(), registry.getEmptyScope());
      fail("Expected an exception here.");
    } catch (AssertionError e) {
      // Expected.
    }
  }
}