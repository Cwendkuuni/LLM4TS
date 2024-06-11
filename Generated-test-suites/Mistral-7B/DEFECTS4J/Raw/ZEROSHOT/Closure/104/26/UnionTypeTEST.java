import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeFactory.booleanType;
import static com.google.javascript.rhino.testing.TypeFactory.numberType;
import static com.google.javascript.rhino.testing.TypeFactory.stringType;
import static com.google.javascript.rhino.testing.TypeFactory.voidType;
import static com.google.javascript.rhino.testing.TypeFactory.unionType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.testing.TypeFactory;
import com.google.javascript.rhino.testing.UnionTypeBuilder;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionType() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), numberType()));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType(), stringType()));

    assertEquals(typeA, typeB);
    assertTrue(typeA.equals(typeB));
    assertEquals(typeA.hashCode(), typeB.hashCode());

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), voidType()));
    assertFalse(typeA.equals(typeC));
    assertFalse(typeA.hashCode() == typeC.hashCode());

    UnionType typeD = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType(), numberType()));
    assertFalse(typeA.equals(typeD));
    assertFalse(typeA.hashCode() == typeD.hashCode());

    UnionType typeE = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType()));
    assertFalse(typeA.equals(typeE));
    assertFalse(typeA.hashCode() == typeE.hashCode());

    UnionType typeF = new UnionType(TypeFactory.registry(), ImmutableSet.of(booleanType()));
    assertFalse(typeA.equals(typeF));
    assertFalse(typeA.hashCode() == typeF.hashCode());
  }

  @Test
  public void testFindPropertyType() {
    UnionType type = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), numberType()));
    assertEquals(stringType(), type.findPropertyType("toString"));
    assertEquals(numberType(), type.findPropertyType("length"));
  }

  @Test
  public void testCanAssignTo() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), numberType()));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType(), stringType()));

    UnionType intUnionType = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType()));
    UnionType stringUnionType = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType()));

    assertTrue(typeA.canAssignTo(intUnionType));
    assertTrue(typeB.canAssignTo(intUnionType));
    assertTrue(typeA.canAssignTo(stringUnionType));
    assertTrue(typeB.canAssignTo(stringUnionType));

    assertFalse(intUnionType.canAssignTo(typeA));
    assertFalse(intUnionType.canAssignTo(typeB));
    assertFalse(stringUnionType.canAssignTo(typeA));
    assertFalse(stringUnionType.canAssignTo(typeB));
  }

  @Test
  public void testCanBeCalled() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(FunctionType.VOID, FunctionType.VOID));
    assertTrue(typeA.canBeCalled());

    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(FunctionType.VOID, StringType.INSTANCE));
    assertFalse(typeB.canBeCalled());

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(StringType.INSTANCE, FunctionType.VOID));
    assertFalse(typeC.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType type = new UnionType(TypeFactory.registry(), ImmutableSet.of(NullType.INSTANCE, NumberType.INSTANCE));
    UnionType restricted = type.restrictByNotNullOrUndefined();
    assertTrue(resticted.isNullType());
  }

  @Test
  public void testTestForEquality() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), numberType()));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType(), stringType()));

    TernaryValue result = typeA.testForEquality(typeB);
    assertEquals(UNKNOWN, result);

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType()));
    result = typeA.testForEquality(typeC);
    assertEquals(UNKNOWN, result);

    UnionType typeD = new UnionType(TypeFactory.registry(), ImmutableSet.of(numberType()));
    result = typeA.testForEquality(typeD);
    assertEquals(UNKNOWN, result);

    result = typeD.testForEquality(typeA);
    assertEquals(UNKNOWN, result);

    UnionType typeE = new UnionType(TypeFactory.registry(), ImmutableSet.of(booleanType()));
    result = typeA.testForEquality(typeE);
    assertEquals(UNKNOWN, result);

    UnionType typeF = new UnionType(TypeFactory.registry(), ImmutableSet.of(nullType()));
    result = typeA.testForEquality(typeF);
    assertEquals(UNKNOWN, result);
  }

  @Test
  public void testIsNullable() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(NullType.INSTANCE, NumberType.INSTANCE));
    assertTrue(typeA.isNullable());

    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE));
    assertFalse(typeB.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(NullType.INSTANCE, NumberType.INSTANCE));
    assertFalse(typeA.isUnknownType());

    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(UnknownType.INSTANCE));
    assertTrue(typeB.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));

    UnionType leastSupertype = typeA.getLeastSupertype(typeB);
    assertEquals(typeA, leastSupertype);

    leastSupertype = typeB.getLeastSupertype(typeA);
    assertEquals(typeA, leastSupertype);

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(BooleanType.INSTANCE));
    UnionType typeD = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE));

    leastSupertype = typeC.getLeastSupertype(typeD);
    assertEquals(NumberType.INSTANCE, leastSupertype);
  }

  @Test
  public void testMeet() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));

    UnionType meet = typeA.meet(typeB);
    assertEquals(typeA, meet);

    meet = typeB.meet(typeA);
    assertEquals(typeB, meet);

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(BooleanType.INSTANCE));
    UnionType typeD = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE));

    meet = typeC.meet(typeD);
    assertEquals(NumberType.INSTANCE, meet);
  }

  @Test
  public void testEquals() {
    UnionType typeA = new UnionType(TypeFactory.registry(), ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType typeB = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));

    UnionType typeC = new UnionType(TypeFactory.registry(), ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType typeD = new UnionType(TypeFactory.registry(), ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));

    UnionType typeE = new UnionType(TypeFactory.registry(), ImmutableSet.of(BooleanType.INSTANCE));
    UnionType typeF = new UnionType(TypeFactory.registry(), ImmutableSet.of(BooleanType.INSTANCE));

    assertEquals(typeA, typeA);
    assertEquals(typeB, typeB);
    assertEquals(typeA, typeC);
    assertEquals(typeB, typeD);
    assertNotEquals(typeA, typeE);
    assertNotEquals(typeB, typeE);
    assertNotEquals(typeA, typeF);
    assertNotEquals(typeB, typeF);
  }

  @Test
  public void testToString() {
    UnionType type = new UnionType(TypeFactory.registry(), ImmutableSet.of(stringType(), numberType()));
    assertEquals("(string, number)", type.toString());
  }
}