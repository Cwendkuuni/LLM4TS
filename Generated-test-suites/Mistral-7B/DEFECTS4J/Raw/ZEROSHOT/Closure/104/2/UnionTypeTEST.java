import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeAssert.assertUnionType;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.testing.TestUtilities;
import com.google.javascript.rhino.testing.TypeAssert;
import com.google.javascript.rhino.types.BooleanType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.NullType;
import com.google.javascript.rhino.types.NumberType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.StringType;
import com.google.javascript.rhino.types.UnionType;
import com.google.javascript.rhino.testing.AnnTypes;

import java.util.Set;

public class UnionTypeTest {

  @org.junit.Test
  public void testUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    UnionType stringBoolean = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new BooleanType(registry)
    ));

    UnionType numberString = new UnionType(registry, ImmutableSet.of(
        new NumberType(registry),
        new StringType(registry)
    ));

    UnionType numberBoolean = new UnionType(registry, ImmutableSet.of(
        new NumberType(registry),
        new BooleanType(registry)
    ));

    UnionType objectNull = new UnionType(registry, ImmutableSet.of(
        new ObjectType(registry),
        new NullType(registry)
    ));

    UnionType functionAny = new UnionType(registry, ImmutableSet.of(
        new FunctionType(registry, new Type[]{}, new Type[]{Registry.ANY_TYPE}),
        new VoidType(registry)
    ));

    UnionType nullAny = new UnionType(registry, ImmutableSet.of(
        new NullType(registry),
        Registry.ANY_TYPE
    ));

    // Basic tests
    assertUnionType(stringBoolean, "(String, Boolean)");
    assertEquals(2, stringBoolean.getAlternates().size());

    // Test matchesNumberContext
    assertTrue(stringBoolean.matchesNumberContext());
    assertFalse(new StringType(registry).matchesNumberContext());
    assertFalse(new BooleanType(registry).matchesNumberContext());

    // Test matchesStringContext
    assertTrue(stringBoolean.matchesStringContext());
    assertTrue(new StringType(registry).matchesStringContext());
    assertFalse(new BooleanType(registry).matchesStringContext());

    // Test matchesObjectContext
    assertTrue(stringBoolean.matchesObjectContext());
    assertTrue(new StringType(registry).matchesObjectContext());
    assertTrue(new ObjectType(registry).matchesObjectContext());
    assertFalse(new BooleanType(registry).matchesObjectContext());
    assertFalse(new NullType(registry).matchesObjectContext());

    // Test getLeastSupertype
    UnionType stringOrNumber = stringBoolean.getLeastSupertype(numberString);
    assertUnionType(stringOrNumber, "(String, Number)");

    // Test findPropertyType
    UnionType stringNumberBoolean = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    assertEquals(StringType.getInstance(registry), stringNumberBoolean.findPropertyType("toString"));

    // Test canAssignTo
    assertFalse(numberString.canAssignTo(new StringType(registry)));
    assertTrue(numberString.canAssignTo(numberString));
    assertTrue(stringBoolean.canAssignTo(numberString));

    // Test canBeCalled
    assertTrue(stringBoolean.canBeCalled());
    assertFalse(new VoidType(registry).canBeCalled());

    // Test restrictByNotNullOrUndefined
    UnionType stringOrNull = stringBoolean.restrictByNotNullOrUndefined();
    assertUnionType(stringOrNull, "(String|null)");

    // Test testForEquality
    UnionType stringOrNumberOrBoolean = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    UnionType numberOrBoolean = new UnionType(registry, ImmutableSet.of(
        new NumberType(registry),
        new BooleanType(registry)
    ));
    assertEquals(UNKNOWN, stringBoolean.testForEquality(numberOrBoolean));
    assertEquals(UNKNOWN, numberOrBoolean.testForEquality(stringBoolean));
    assertEquals(BooleanType.getInstance(registry), stringBoolean.testForEquality(new BooleanType(registry)));

    // Test isNullable
    assertFalse(stringBoolean.isNullable());
    assertTrue(objectNull.isNullable());

    // Test isUnknownType
    assertFalse(stringBoolean.isUnknownType());

    // Test getLeastSupertype
    assertEquals(stringOrNumber, stringBoolean.getLeastSupertype(numberString));
    assertNull(stringBoolean.getLeastSupertype(new ObjectType(registry)));

    // Test meet
    UnionType stringOrNumberOrBooleanMeet = stringBoolean.meet(numberString);
    assertEquals(stringOrNumberOrBoolean, stringOrNumberOrBooleanMeet);

    // Test equals
    UnionType stringOrNumberOrBooleanEqual = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    assertTrue(stringBoolean.equals(stringOrNumberOrBooleanEqual));

    // Test hashCode
    UnionType stringOrNumberOrBooleanHashCode = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    assertEquals(stringBoolean.hashCode(), stringOrNumberOrBooleanHashCode.hashCode());

    // Test isUnionType
    assertTrue(stringBoolean.isUnionType());

    // Test isObject
    assertTrue(stringBoolean.isObject());

    // Test contains
    assertTrue(stringBoolean.contains(new StringType(registry)));
    assertTrue(stringBoolean.contains(new BooleanType(registry)));
    assertFalse(stringBoolean.contains(new NumberType(registry)));

    // Test getRestrictedUnion
    UnionType stringOrNumberRestricted = stringOrNumber.getRestrictedUnion(NumberType.getInstance(registry));
    assertUnionType(stringOrNumberRestricted, "(String)");

    // Test toString
    assertEquals("(String, Boolean)", stringBoolean.toString());

    // Test isSubtype
    assertTrue(stringBoolean.isSubtype(stringOrNumberOrBoolean));
    assertFalse(stringBoolean.isSubtype(new ObjectType(registry)));

    // Test getRestrictedTypeGivenToBooleanOutcome
    assertEquals(BooleanType.getInstance(registry), stringBoolean.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(StringType.getInstance(registry), stringBoolean.getRestrictedTypeGivenToBooleanOutcome(false));

    // Test getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.BOTH, stringBoolean.getPossibleToBooleanOutcomes());

    // Test getTypesUnderEquality
    UnionType stringOrNumberOrBooleanUnderEquality = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    TypePair stringOrNumberOrBooleanUnderEqualityResult = stringBoolean.getTypesUnderEquality(stringOrNumberOrBoolean);
    assertUnionType(stringOrNumberOrBooleanUnderEqualityResult.typeA, stringOrNumberOrBoolean);
    assertNull(stringOrNumberOrBooleanUnderEqualityResult.typeB);

    // Test getTypesUnderInequality
    UnionType stringOrNumberOrBooleanUnderInequality = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    TypePair stringOrNumberOrBooleanUnderInequalityResult = stringBoolean.getTypesUnderInequality(stringOrNumberOrBoolean);
    assertNull(stringOrNumberOrBooleanUnderInequalityResult.typeA);
    assertUnionType(new ObjectType(registry), stringOrNumberOrBooleanUnderInequalityResult.typeB);

    // Test getTypesUnderShallowInequality
    UnionType stringOrNumberOrBooleanUnderShallowInequality = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    ));
    TypePair stringOrNumberOrBooleanUnderShallowInequalityResult = stringBoolean.getTypesUnderShallowInequality(stringOrNumberOrBoolean);
    assertNull(stringOrNumberOrBooleanUnderShallowInequalityResult.typeA);
    assertNull(stringOrNumberOrBooleanUnderShallowInequalityResult.typeB);

    // Test visitor
    UnionType result = new UnionType(registry, ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry)
    ));
    AnnTypes annTypesVisitor = new AnnTypes();
    result.visit(annTypesVisitor);
    assertEquals("[String, Number]", annTypesVisitor.toString());

    // Test resolveInternal
    UnionType resolvedUnion = stringBoolean.resolveInternal(TestUtilities.createErrorReporter(), null);
    assertEquals(stringBoolean, resolvedUnion);
  }
}