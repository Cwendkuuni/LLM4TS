package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Visitor;
import com.google.javascript.rhino.jstype.UnionType.UnionTypeBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

public class UnionTypeTest {
  private ErrorReporter t;
  private JSTypeRegistry registry;
  private Set<JSType> alternates;
  private UnionType unionType;
  private UnionType alternateType;

  @Before
  public void setUp() {
    t = new ErrorReporter() {
      @Override
      public void error(String message, Object... args) {}

      @Override
      public void warning(String message, Object... args) {}
    };
    registry = new JSTypeRegistry();

    alternates = ImmutableSet.of(new IntType(registry), new StringType(registry));
    alternativeType = new UnionType(registry, alternates);
    unionType = new UnionType(registry, alternates);
  }

  @Test
  public void testGetAlternates() {
    assertEquals("expected and actual alternates to be equal", alternates, unionType.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    unionType.forgiveUnknownNames();
    for (JSType type : unionType.getAlternates()) {
      type.forgiveUnknownNames();
    }
  }

  @Test
  public void testMatchesNumberContext() {
    boolean result = unionType.matchesNumberContext();
    assertTrue("expected union type to match number context", result);
  }

  @Test
  public void testMatchesStringContext() {
    boolean result = unionType.matchesStringContext();
    assertTrue("expected union type to match string context", result);
  }

  @Test
  public void testMatchesObjectContext() {
    boolean result = unionType.matchesObjectContext();
    assertTrue("expected union type to match object context", result);
  }

  @Test
  public void testFindPropertyType() {
    String propertyName = "length";
    JSType propertyType = unionType.findPropertyType(propertyName);
    assertEquals("expected property type to be null for union type", null, propertyType);

    propertyType = alternativeType.findPropertyType(propertyName);
    // IntType has a length property, StringType does not.
    assertTrue("expected IntType to have length property", propertyType != null);
    assertEquals("expected length property type to be IntType", IntType.class, propertyType.getClass());
  }

  @Test
  public void testCanAssignTo() {
    JSType that = new IntType(registry);
    boolean result = unionType.canAssignTo(that);
    assertTrue("expected union type to be able to assign to IntType", result);

    result = that.canAssignTo(unionType);
    assertTrue("expected IntType to be able to assign to union type", result);

    JSType otherType = new BooleanType(registry);
    assertFalse("expected union type to not be able to assign to BooleanType", unionType.canAssignTo(otherType));
    assertFalse("expected BooleanType to not be able to assign to union type", otherType.canAssignTo(unionType));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue("expected union type to be callable", unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType result = unionType.restrictByNotNullOrUndefined();
    // check that the result is not null
    assertNotNull("expected non-null result", result);
    // check that all alternatives have been removed
    assertEquals("expected empty set of alternates", ImmutableSet.of(), result.getAlternates());
  }

  @Test
  public void testTestForEquality() {
    // test self equality
    TernaryValue result = unionType.testForEquality(unionType);
    assertEquals("expected self equality to be UNKNOWN", UNKNOWN, result);

    // test equality between two union types with different alternates
    UnionType otherUnionType = new UnionType(registry, ImmutableSet.of(new StringType(registry), new IntType(registry)));
    result = unionType.testForEquality(otherUnionType);
    assertEquals("expected union type to be unequal to other union type", UNKNOWN, result);

    // test equality between union type and a non-union type
    JSType nonUnionType = new NumberType(registry);
    result = unionType.testForEquality(nonUnionType);
    assertEquals("expected union type to be unequal to non-union type", UNKNOWN, result);
  }

  @Test
  public void testIsNullable() {
    assertFalse("expected union type to not be nullable", unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    for (JSType type : unionType.getAlternates()) {
      assertFalse("expected no unknown types in union", type.isUnknownType());
    }
    assertFalse("expected union type to not be unknown", unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType that = new ObjectType(registry);
    JSType result = unionType.getLeastSupertype(that);
    assertEquals("expected least supertype to be ObjectType", that, result);

    result = unionType.getLeastSupertype(alternativeType);
    UnionType leastSupertype = (UnionType) result;
    assertEquals("expected least supertype to contain both alternates", alternates, leastSupertype.getAlternates());
  }

  @Test
  public void meet() {
    UnionType that = new UnionType(registry, ImmutableSet.of(new NullType(registry), new NumberType(registry)));
    UnionType result = unionType.meet(that);
    assertTrue("expected result to be non-null", result != null);
    // check that all alternates have been added
    assertEquals("expected result to contain all alternates", ImmutableSet.ofAll(alternates, NullType.class, NumberType.class), result.getAlternates());
  }

  @Test
  public void equals() {
    UnionType that = new UnionType(registry, alternates);
    assertTrue("expected union types to be equal", unionType.equals(that));
    assertFalse("expected union types to not be equal to non-union type", unionType.equals(new IntType(registry)));
  }

  @Test
  public void hashCode() {
    UnionType that = new UnionType(registry, alternates);
    assertEquals("expected hash codes to be equal", unionType.hashCode(), that.hashCode());
  }

  @Test
  public void isUnionType() {
    assertTrue("expected union type to be union type", unionType.isUnionType());
  }

  @Test
  public void isObject() {
    assertTrue("expected union type to be object", unionType.isObject());
  }

  @Test
  public void contains() {
    assertTrue("expected union type to contain alternate type", unionType.contains(alternativeType));
    assertFalse("expected union type not to contain non-alternate type", unionType.contains(new IntType(registry)));
  }

  @Test
  public void getRestrictedUnion() {
    JSType restrictBy = new IntType(registry);
    UnionType result = unionType.getRestrictedUnion(restrictBy);
    assertTrue("expected result to be non-null", result != null);
    // check that all subtypes of IntType have been removed
    Set<JSType> restrictedAlternates = new TreeSet<JSType>(ALPHA);
    for (JSType alternate : unionType.getAlternates()) {
      if (alternate.isSubtype(restrictBy)) {
        restrictedAlternates.add(alternate);
      }
    }
    assertEquals("expected restricted alternates to be subset of union alternates", restrictedAlternates, result.getAlternates());
  }

  @Test
  public void toString() {
    String expected = "((Int, String))";
    String result = unionType.toString();
    assertEquals("expected union type to have correct string representation", expected, result);
  }

  @Test
  public void isSubtype() {
    UnionType that = new UnionType(registry, ImmutableSet.of(new IntType(registry), new StringType(registry)));
    assertTrue("expected union type to be subtype of itself", unionType.isSubtype(unionType));
    assertTrue("expected union type to be subtype of alternate type", unionType.isSubtype(alternativeType));
    assertTrue("expected alternate type to be subtype of union type", alternativeType.isSubtype(unionType));
    assertTrue("expected alternate type to be subtype of itself", alternativeType.isSubtype(alternativeType));
    assertFalse("expected non-union type to not be subtype of union type", new BooleanType(registry).isSubtype(unionType));
  }

  @Test
  public void getRestrictedTypeGivenToBooleanOutcome() {
    BooleanLiteralSet expected = BooleanLiteralSet.create(BooleanLiteral.TRUE);
    BooleanLiteralSet result = unionType.getRestrictedTypeGivenToBooleanOutcome(true);
    assertEquals("expected restricted type to have correct BooleanLiteralSet", expected, result);
  }

  @Test
  public void getPossibleToBooleanOutcomes() {
    BooleanLiteralSet expected = BooleanLiteralSet.create(BooleanLiteral.TRUE, BooleanLiteral.FALSE);
    BooleanLiteralSet result = unionType.getPossibleToBooleanOutcomes();
    assertEquals("expected possible toBooleanOutcomes to have correct BooleanLiteralSet", expected, result);
  }

  @Test
  public void getTypesUnderEquality() {
    UnionType that = new UnionType(registry, ImmutableSet.of(new StringType(registry), new IntType(registry)));
    TypePair expected = new TypePair(unionType, that);
    TypePair result = unionType.getTypesUnderEquality(that);
    assertEquals("expected typesUnderEquality to have correct TypePair", expected, result);
  }

  @Test
  public void getTypesUnderInequality() {
    UnionType that = new UnionType(registry, ImmutableSet.of(new IntType(registry), new StringType(registry)));
    TypePair expected = new TypePair(alternativeType, unionType);
    TypePair result = unionType.getTypesUnderInequality(that);
    assertEquals("expected typesUnderInequality to have correct TypePair", expected, result);
  }

  @Test
  public void getTypesUnderShallowInequality() {
    UnionType that = new UnionType(registry, ImmutableSet.of(new IntType(registry), new StringType(registry)));
    TypePair expected = new TypePair(alternativeType, unionType);
    TypePair result = unionType.getTypesUnderShallowInequality(that);
    assertEquals("expected typesUnderShallowInequality to have correct TypePair", expected, result);
  }

  @Test
  public void visit() {
    Visitor<Boolean> visitor = new Visitor<Boolean>() {
      @Override
      public Boolean visitBooleanType(BooleanType type) {
        return Boolean.TRUE;
      }

      @Override
      public Boolean visitIntType(IntType type) {
        return Boolean.FALSE;
      }

      @Override
      public Boolean visitStringType(StringType type) {
        return Boolean.TRUE;
      }

      @Override
      public Boolean visitUnionType(UnionType type) {
        return Boolean.FALSE;
      }

      @Override
      public Boolean visitNullType(NullType type) {
        return Boolean.FALSE;
      }

      @Override
      public Boolean visitVoidType(VoidType type) {
        return Boolean.FALSE;
      }

      @Override
      public Boolean visitNativeType(JSTypeNative type) {
        return Boolean.FALSE;
      }
    };

    Boolean result = unionType.visit(visitor);
    assertTrue("expected visit method to return false for UnionType", result == Boolean.FALSE);
  }

  @Test
  public void resolveInternal() {
    unionType.resolveInternal(t, new StaticScope<JSType>(registry));
    for (JSType type : unionType.getAlternates()) {
      type.resolveInternal(t, new StaticScope<JSType>(registry));
    }
  }
}