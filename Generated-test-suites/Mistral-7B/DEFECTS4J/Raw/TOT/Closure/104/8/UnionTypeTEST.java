import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType numberStringUnion = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        registry.getNativeType(String.class)
    ));

    assertEquals("(number, string)", numberStringUnion.toString());

    Set<JSType> numberSubtypes = Arrays.asList(
        registry.getIntType(),
        registry.getNumberType(),
        registry.getFloatType()
    ).stream().collect(ImmutableSet.toImmutableSet());

    Set<JSType> stringSubtypes = Arrays.asList(
        registry.getStringType(),
        registry.getBooleanType(),
        registry.getNullType()
    ).stream().collect(ImmutableSet.toImmutableSet());

    // Test getAlternates()
    assertEquals(numberSubtypes, numberStringUnion.getAlternates());

    // Test matchesNumberContext()
    assertTrue(numberStringUnion.matchesNumberContext());

    // Test matchesStringContext()
    assertTrue(numberStringUnion.matchesStringContext());

    // Test matchesObjectContext()
    assertTrue(numberStringUnion.matchesObjectContext());

    // Test canAssignTo()
    assertTrue(numberStringUnion.canAssignTo(registry.getIntType()));
    assertFalse(numberStringUnion.canAssignTo(registry.getBooleanType()));

    // Test findPropertyType()
    assertEquals(registry.getStringType(), numberStringUnion.findPropertyType("toString"));

    // Test restrictByNotNullOrUndefined()
    UnionType restrictedNumberStringUnion = numberStringUnion.restrictByNotNullOrUndefined();
    assertNotNull(restrictedNumberStringUnion);
    assertFalse(restrictedNumberStringUnion.contains(registry.getNullType()));

    // Test testForEquality()
    UnionType anotherNumberStringUnion = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        registry.getNativeType(String.class)
    ));
    assertEquals(UNKNOWN, numberStringUnion.testForEquality(anotherNumberStringUnion));

    UnionType sameNumberStringUnion = numberStringUnion;
    assertEquals(UNKNOWN, sameNumberStringUnion.testForEquality(sameNumberStringUnion));

    // Test isNullable()
    assertFalse(numberStringUnion.isNullable());

    // Test isUnknownType()
    assertFalse(numberStringUnion.isUnknownType());

    // Test getLeastSupertype()
    JSType leastSupertype = numberStringUnion.getLeastSupertype(registry.getObjectType());
    assertEquals(registry.getObjectType(), leastSupertype);

    // Test meet()
    UnionType meetResult = numberStringUnion.meet(registry.getObjectType());
    assertEquals(registry.getObjectType(), meetResult);

    // Test equals()
    UnionType sameUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        registry.getNativeType(String.class)
    ));
    assertTrue(numberStringUnion.equals(sameUnionType));

    // Test hashCode()
    int hashCode1 = numberStringUnion.hashCode();
    UnionType sameUnionType2 = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        registry.getNativeType(String.class)
    ));
    int hashCode2 = sameUnionType2.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test isUnionType()
    assertTrue(numberStringUnion.isUnionType());

    // Test isObject()
    assertTrue(numberStringUnion.isObject());

    // Test contains()
    assertTrue(numberStringUnion.contains(registry.getNumberType()));

    // Test getRestrictedUnion()
    UnionType numberOnlyUnion = numberStringUnion.getRestrictedUnion(registry.getNumberType());
    assertTrue(numberOnlyUnion.contains(registry.getNumberType()));
    assertFalse(numberOnlyUnion.contains(registry.getStringType()));
  }

  @Test
  public void testNestedUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType numberOrString = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        new UnionType(registry, ImmutableSet.of(
            registry.getNativeType(String.class),
            registry.getNativeType(Boolean.class)
        ))
    ));

    // Test getAlternates()
    Set<JSType> numberSubtypes = Arrays.asList(registry.getIntType(), registry.getNumberType(), registry.getFloatType())
        .stream()
        .collect(ImmutableSet.toImmutableSet());
    Set<JSType> stringOrBooleanSubtypes = Arrays.asList(
        registry.getStringType(),
        registry.getBooleanType()
    ).stream().collect(ImmutableSet.toImmutableSet());

    Set<JSType> numberOrStringAlternates = numberOrString.getAlternates();
    assertEquals(numberSubtypes, numberOrStringAlternates);
    assertEquals(stringOrBooleanSubtypes, numberOrStringAlternates.stream().filter(t -> t != registry.getNumberType())
        .collect(ImmutableSet.toImmutableSet()));

    // Test matchesNumberContext()
    assertTrue(numberOrString.matchesNumberContext());

    // Test matchesStringContext()
    assertTrue(numberOrString.matchesStringContext());

    // Test matchesObjectContext()
    assertTrue(numberOrString.matchesObjectContext());

    // Test canAssignTo()
    assertTrue(numberOrString.canAssignTo(registry.getIntType()));

    // Test findPropertyType()
    assertEquals(registry.getStringType(), numberOrString.findPropertyType("toString"));

    // Test restrictByNotNullOrUndefined()
    UnionType restrictedNumberOrString = numberOrString.restrictByNotNullOrUndefined();
    assertNotNull(restrictedNumberOrString);
    assertFalse(restrictedNumberOrString.contains(registry.getNullType()));

    // Test testForEquality()
    UnionType anotherNumberOrString = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        new UnionType(registry, ImmutableSet.of(
            registry.getNativeType(String.class),
            registry.getNativeType(Boolean.class)
        ))
    ));
    assertEquals(UNKNOWN, numberOrString.testForEquality(anotherNumberOrString));

    UnionType sameNumberOrString = numberOrString;
    assertEquals(UNKNOWN, sameNumberOrString.testForEquality(sameNumberOrString));

    // Test isNullable()
    assertFalse(numberOrString.isNullable());

    // Test isUnknownType()
    assertFalse(numberOrString.isUnknownType());

    // Test getLeastSupertype()
    JSType leastSupertype = numberOrString.getLeastSupertype(registry.getObjectType());
    assertEquals(registry.getObjectType(), leastSupertype);

    // Test meet()
    UnionType meetResult = numberOrString.meet(registry.getObjectType());
    assertEquals(registry.getObjectType(), meetResult);

    // Test equals()
    UnionType sameNumberOrString = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        new UnionType(registry, ImmutableSet.of(
            registry.getNativeType(String.class),
            registry.getNativeType(Boolean.class)
        ))
    ));
    assertTrue(numberOrString.equals(sameNumberOrString));

    // Test hashCode()
    int hashCode1 = numberOrString.hashCode();
    UnionType sameNumberOrString2 = new UnionType(registry, ImmutableSet.of(
        registry.getNativeType(Number.class),
        new UnionType(registry, ImmutableSet.of(
            registry.getNativeType(String.class),
            registry.getNativeType(Boolean.class)
        ))
    ));
    int hashCode2 = sameNumberOrString2.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test isUnionType()
    assertTrue(numberOrString.isUnionType());

    // Test isObject()
    assertTrue(numberOrString.isObject());

    // Test contains()
    assertTrue(numberOrString.contains(registry.getNumberType()));
  }

  @Test
  public void testEmptyUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    UnionType emptyUnionType = new UnionType(registry, ImmutableSet.of());

    // Test getAlternates()
    Set<JSType> emptyAlternates = emptyUnionType.getAlternates();
    assertTrue(emptyAlternates.isEmpty());

    // Test matchesNumberContext()
    assertFalse(emptyUnionType.matchesNumberContext());

    // Test matchesStringContext()
    assertFalse(emptyUnionType.matchesStringContext());

    // Test matchesObjectContext()
    assertFalse(emptyUnionType.matchesObjectContext());

    // Test canAssignTo()
    assertFalse(emptyUnionType.canAssignTo(registry.getIntType()));

    // Test findPropertyType()
    assertNull(emptyUnionType.findPropertyType("toString"));

    // Test restrictByNotNullOrUndefined()
    assertNull(emptyUnionType.restrictByNotNullOrUndefined());

    // Test testForEquality()
    UnionType anotherEmptyUnionType = new UnionType(registry, ImmutableSet.of());
    assertEquals(UNKNOWN, emptyUnionType.testForEquality(anotherEmptyUnionType));

    UnionType sameEmptyUnionType = emptyUnionType;
    assertEquals(UNKNOWN, sameEmptyUnionType.testForEquality(sameEmptyUnionType));

    // Test isNullable()
    assertFalse(emptyUnionType.isNullable());

    // Test isUnknownType()
    assertTrue(emptyUnionType.isUnknownType());

    // Test getLeastSupertype()
    JSType leastSupertype = emptyUnionType.getLeastSupertype(registry.getObjectType());
    assertEquals(registry.getObjectType(), leastSupertype);

    // Test meet()
    UnionType meetResult = emptyUnionType.meet(registry.getObjectType());
    assertEquals(registry.getObjectType(), meetResult);

    // Test equals()
    UnionType sameEmptyUnionType2 = emptyUnionType;
    assertTrue(emptyUnionType.equals(sameEmptyUnionType2));

    // Test hashCode()
    int hashCode1 = emptyUnionType.hashCode();
    UnionType sameEmptyUnionType3 = emptyUnionType;
    int hashCode2 = sameEmptyUnionType3.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test isUnionType()
    assertTrue(emptyUnionType.isUnionType());

    // Test isObject()
    assertTrue(emptyUnionType.isObject());

    // Test contains()
    assertFalse(emptyUnionType.contains(registry.getNumberType()));
  }
}