import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.Assert;
import com.google.javascript.rhino.testing.StaticScope;
import com.google.javascript.rhino.testing.TypeCheckers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnionTypeTest {

  private static UnionType numberStringUnion() {
    return new UnionType(TypeCheckers.registry, ImmutableSet.of(
        JSTypeNative.INT_TYPE,
        JSTypeNative.STRING_TYPE
    ));
  }

  private static UnionType nullErrorUnion() {
    return new UnionType(TypeCheckers.registry, ImmutableSet.of(
        JSTypeNative.NULL_TYPE,
        JSTypeNative.ERROR_TYPE
    ));
  }

  @org.junit.Test
  public void testBasicUnionType() {
    UnionType unionType = numberStringUnion();

    // Test getting alternates
    Set<JSType> alternates = unionType.getAlternates();
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.INT_TYPE, JSTypeNative.STRING_TYPE), alternates);

    // Test matchesNumberContext
    Assert.assertTrue(unionType.matchesNumberContext());

    // Test matchesStringContext
    Assert.assertTrue(unionType.matchesStringContext());

    // Test matchesObjectContext
    Assert.assertTrue(unionType.matchesObjectContext());

    // Test finding property type
    String propertyName = "length";
    JSType propertyType = unionType.findPropertyType(propertyName);
    Assert.assertNull(propertyType);

    // Test canAssignTo
    JSType intType = JSTypeNative.INT_TYPE;
    Assert.assertTrue(unionType.canAssignTo(intType));

    // Test canBeCalled
    Assert.assertTrue(unionType.canBeCalled());

    // Test restrictByNotNullOrUndefined
    UnionType restrictedUnion = unionType.restrictByNotNullOrUndefined();
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.STRING_TYPE), restrictedUnion.getAlternates());

    // Test testForEquality
    JSType stringType = JSTypeNative.STRING_TYPE;
    TernaryValue result = unionType.testForEquality(stringType);
    Assert.assertEquals(TernaryValue.MAYBE, result);

    // Test isNullType
    Assert.assertFalse(unionType.isNullable());

    // Test isUnknownType
    Assert.assertFalse(unionType.isUnknownType());

    // Test getLeastSupertype
    JSType leastSupertype = unionType.getLeastSupertype(JSTypeNative.NUMBER_TYPE);
    Assert.assertEquals(unionType, leastSupertype);

    // Test meet
    UnionType meetType = new UnionType(TypeCheckers.registry, ImmutableSet.of(
        JSTypeNative.STRING_TYPE
    ));
    UnionType meetingType = unionType.meet(meetType);
    Assert.assertEquals(unionType, meetingType);

    // Test equals and hashCode
    UnionType equalUnion = numberStringUnion();
    UnionType anotherEqualUnion = numberStringUnion();
    Assert.assertEquals(equalUnion, anotherEqualUnion);
    Assert.assertEquals(equalUnion.hashCode(), anotherEqualUnion.hashCode());

    // Test isUnionType, isObject and isSubtype
    Assert.assertTrue(unionType.isUnionType());
    Assert.assertTrue(unionType.isObject());
    Assert.assertTrue(JSTypeNative.INT_TYPE.isSubtype(unionType));
    Assert.assertFalse(JSTypeNative.BOOLEAN_TYPE.isSubtype(unionType));

    // Test getRestrictedUnion
    UnionType restrictedNumberString = unionType.getRestrictedUnion(JSTypeNative.NUMBER_TYPE);
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.STRING_TYPE), restrictedNumberString.getAlternates());

    // Test toString
    Assert.assertEquals("(number|string)", unionType.toString());

    // Test equals, hashCode and isSubtype for different unions
    UnionType anotherNumberStringUnion = numberStringUnion();
    UnionType numberOnlyUnion = new UnionType(TypeCheckers.registry, ImmutableSet.of(JSTypeNative.INT_TYPE));
    UnionType stringOnlyUnion = new UnionType(TypeCheckers.registry, ImmutableSet.of(JSTypeNative.STRING_TYPE));

    Assert.assertFalse(numberOnlyUnion.equals(unionType));
    Assert.assertFalse(unionType.equals(stringOnlyUnion));
    Assert.assertFalse(unionType.equals(anotherNumberStringUnion));
    Assert.assertFalse(unionType.isSubtype(numberOnlyUnion));
    Assert.assertFalse(numberOnlyUnion.isSubtype(unionType));
    Assert.assertFalse(unionType.isSubtype(stringOnlyUnion));
    Assert.assertFalse(stringOnlyUnion.isSubtype(unionType));

    // Test getTypesUnderEquality, getTypesUnderInequality and getTypesUnderShallowInequality
    UnionType that = anotherNumberStringUnion;
    TypePair typesUnderEquality = unionType.getTypesUnderEquality(that);
    Assert.assertEquals(unionType, typesUnderEquality.typeA);
    Assert.assertEquals(that, typesUnderEquality.typeB);

    typesUnderInequality = unionType.getTypesUnderInequality(that);
    Assert.assertEquals(that, typesUnderInequality.typeA);
    Assert.assertEquals(unionType, typesUnderInequality.typeB);

    typesUnderShallowInequality = unionType.getTypesUnderShallowInequality(that);
    Assert.assertEquals(unionType, typesUnderShallowInequality.typeA);
    Assert.assertEquals(that, typesUnderShallowInequality.typeB);

    // Test visit
    Assert.assertEquals(unionType.toString(), unionType.visit(new TestVisitor()));
  }

  @org.junit.Test
  public void testNullErrorUnion() {
    UnionType nullErrorUnion = nullErrorUnion();

    // Test getting alternates
    Set<JSType> alternates = nullErrorUnion.getAlternates();
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.ERROR_TYPE), alternates);

    // Test matchesNumberContext
    Assert.assertFalse(nullErrorUnion.matchesNumberContext());

    // Test matchesStringContext
    Assert.assertTrue(nullErrorUnion.matchesStringContext());

    // Test matchesObjectContext
    Assert.assertTrue(nullErrorUnion.matchesObjectContext());

    // Test findPropertyType
    String propertyName = "message";
    JSType propertyType = nullErrorUnion.findPropertyType(propertyName);
    Assert.assertEquals(JSTypeNative.STRING_TYPE, propertyType);

    // Test canAssignTo
    Assert.assertTrue(nullErrorUnion.canAssignTo(JSTypeNative.STRING_TYPE));

    // Test canBeCalled
    Assert.assertFalse(nullErrorUnion.canBeCalled());

    // Test restrictByNotNullOrUndefined
    UnionType restrictedNullError = nullErrorUnion.restrictByNotNullOrUndefined();
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.NULL_TYPE), restrictedNullError.getAlternates());

    // Test testForEquality
    JSType errorType = JSTypeNative.ERROR_TYPE;
    TernaryValue result = nullErrorUnion.testForEquality(errorType);
    Assert.assertEquals(TernaryValue.MAYBE, result);

    // Test isNullType
    Assert.assertTrue(nullErrorUnion.isNullable());

    // Test isUnknownType
    Assert.assertFalse(nullErrorUnion.isUnknownType());

    // Test getLeastSupertype
    JSType leastSupertype = nullErrorUnion.getLeastSupertype(JSTypeNative.NULL_TYPE);
    Assert.assertEquals(nullErrorUnion, leastSupertype);

    // Test meet
    UnionType meetingType = new UnionType(TypeCheckers.registry, ImmutableSet.of(JSTypeNative.ERROR_TYPE));
    UnionType meetingNullError = nullErrorUnion.meet(meetingType);
    Assert.assertEquals(nullErrorUnion, meetingNullError);

    // Test equals and hashCode
    UnionType anotherNullErrorUnion = nullErrorUnion();
    Assert.assertEquals(nullErrorUnion, anotherNullErrorUnion);
    Assert.assertEquals(nullErrorUnion.hashCode(), anotherNullErrorUnion.hashCode());

    // Test isUnionType, isObject and isSubtype
    Assert.assertTrue(nullErrorUnion.isUnionType());
    Assert.assertTrue(nullErrorUnion.isObject());
    Assert.assertTrue(JSTypeNative.NULL_TYPE.isSubtype(nullErrorUnion));
    Assert.assertFalse(JSTypeNative.BOOLEAN_TYPE.isSubtype(nullErrorUnion));

    // Test getRestrictedUnion
    UnionType restrictedNullError = nullErrorUnion.getRestrictedUnion(JSTypeNative.NULL_TYPE);
    Assert.assertEquals(ImmutableSet.of(JSTypeNative.ERROR_TYPE), restrictedNullError.getAlternates());

    // Test toString
    Assert.assertEquals("(null|Error)", nullErrorUnion.toString());

    // Test equals, hashCode and isSubtype for different unions
    UnionType anotherNullErrorUnion2 = nullErrorUnion();
    UnionType numberOnlyUnion = new UnionType(TypeCheckers.registry, ImmutableSet.of(JSTypeNative.INT_TYPE));
    UnionType stringOnlyUnion = new UnionType(TypeCheckers.registry, ImmutableSet.of(JSTypeNative.STRING_TYPE));

    Assert.assertFalse(numberOnlyUnion.equals(nullErrorUnion));
    Assert.assertFalse(nullErrorUnion.equals(stringOnlyUnion));
    Assert.assertFalse(nullErrorUnion.equals(anotherNullErrorUnion2));
    Assert.assertFalse(numberOnlyUnion.isSubtype(nullErrorUnion));
    Assert.assertFalse(nullErrorUnion.isSubtype(numberOnlyUnion));
    Assert.assertFalse(nullErrorUnion.isSubtype(stringOnlyUnion));
    Assert.assertFalse(stringOnlyUnion.isSubtype(nullErrorUnion));

    // Test getTypesUnderEquality, getTypesUnderInequality and getTypesUnderShallowInequality
    UnionType that2 = anotherNullErrorUnion2;
    TypePair typesUnderEquality = nullErrorUnion.getTypesUnderEquality(that2);
    Assert.assertEquals(nullErrorUnion, typesUnderEquality.typeA);
    Assert.assertEquals(that2, typesUnderEquality.typeB);

    typesUnderInequality = nullErrorUnion.getTypesUnderInequality(that2);
    Assert.assertEquals(that2, typesUnderInequality.typeA);
    Assert.assertEquals(nullErrorUnion, typesUnderInequality.typeB);

    typesUnderShallowInequality = nullErrorUnion.getTypesUnderShallowInequality(that2);
    Assert.assertEquals(nullErrorUnion, typesUnderShallowInequality.typeA);
    Assert.assertEquals(that2, typesUnderShallowInequality.typeB);
  }
}

class TestVisitor implements Visitor<String> {
  @Override
  public String caseUnionType(UnionType unionType) {
    return unionType.toString();
  }
}