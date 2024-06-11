package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypePair;
import com.google.javascript.rhino. UnionTypeBuilder;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testEquals() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));

    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertEquals(type1, type2);

    UnionType type3 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new BooleanType()));
    assertNotSame(type1, type3);
  }

  @Test
  public void testGetAlternates() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertEquals(ImmutableSet.of(new StringType(), new NumberType()),
        type.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    type.forgiveUnknownNames();
    assertTrue(type.isForgiven());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    assertTrue(type.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertTrue(type.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertTrue(type.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new ObjectType()));
    assertEquals(new StringType(), type.findPropertyType("toString"));
  }

  @Test
  public void testCanAssignTo() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertTrue(type.canAssignTo(new StringType()));
    assertTrue(type.canAssignTo(new NumberType()));
  }

  @Test
  public void testCanBeCalled() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new FunctionType(ImmutableSet.of(new NumberType()), new VoidType()),
        new NumberType()));
    assertTrue(type.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NullType(), new StringType()));
    assertEquals(new NullType(), type.restrictByNotNullOrUndefined());
  }

  @Test
  public void testTestForEquality() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertEquals(type1, type2);
  }

  @Test
  public void testIsNullable() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NullType(), new StringType()));
    assertFalse(type.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NullType(), new StringType()));
    assertFalse(type.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType(), new ObjectType()));
    assertEquals(type2, type1.getLeastSupertype(type2));
  }

  @Test
  public void testMeet() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new ObjectType()));
    UnionType result = type1.meet(type2);
    assertEquals(new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new ObjectType(), new NumberType())), result);
  }

  @Test
  public void testHashCode() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertEquals(type1.hashCode(), type2.hashCode());
  }

  @Test
  public void testToString() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertEquals("(String|Number)", type.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    assertTrue(type1.isSubtype(type2));
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testGetTypesUnderEquality() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    TypePair pair = type1.getTypesUnderEquality(type2);
    assertEquals(type1, pair.typeA);
    assertEquals(type2, pair.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    TypePair pair = type1.getTypesUnderInequality(type2);
    assertEquals(type2, pair.typeA);
    assertEquals(type1, pair.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType type1 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new NumberType(), new StringType()));
    UnionType type2 = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    TypePair pair = type1.getTypesUnderShallowInequality(type2);
    assertEquals(type1, pair.typeA);
    assertEquals(type2, pair.typeB);
  }

  @Test
  public void testVisit() {
    UnionType type = new UnionType(new UnionTypeRegistry(), ImmutableSet.of(
        new StringType(), new NumberType()));
    TypeVisitor visitor = new TypeVisitor() {
      private int counter = 0;

      @Override
      public StringType visitStringType(StringType type) {
        counter++;
        return type;
      }

      @Override
      public NumberType visitNumberType(NumberType type) {
        counter++;
        return type;
      }

      @Override
      public UnionType visitUnionType(UnionType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public ObjectType visitObjectType(ObjectType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public FunctionType visitFunctionType(FunctionType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public VoidType visitVoidType(VoidType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public NullType visitNullType(NullType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public BooleanType visitBooleanType(BooleanType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public ArrayType visitArrayType(ArrayType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public RegExpType visitRegExpType(RegExpType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public ThisType visitThisType(ThisType type) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Type visitOtherType(JSType type) {
        throw new UnsupportedOperationException();
      }

      public int getCounter() {
        return counter;
      }
    };
    type.visit(visitor);
    assertEquals(2, visitor.getCounter());
  }

  @Test
  public void testResolveInternal() {
    UnionTypeRegistry registry = new UnionTypeRegistry();
    registry.registerType("String", new StringType());
    registry.registerType("Number", new NumberType());
    registry.registerType("Null", new NullType());
    registry.registerType("Object", new ObjectType());
    registry.registerType("Function", new FunctionType(
        ImmutableSet.of(new NumberType()), new VoidType()));
    registry.registerType("Boolean", new BooleanType());
    registry.registerType("Array", new ArrayType(new ArrayTypeRegistry(), 
        new IntType(), new NumberType()));
    registry.registerType("RegExp", new RegExpType(new ArrayType(
        new ArrayTypeRegistry(), new StringType(), new ArrayType(
        new ArrayTypeRegistry(), new StringType(), new IntType()))));
    registry.registerType("ThisType", new ThisType());

    UnionType type1 = new UnionType(registry, ImmutableSet.of(
        new NullType(), new NumberType()));

    StaticScope scope = new StaticScope<>();
    ErrorReporter reporter = new ErrorReporter();
    JSType resolved = type1.resolveInternal(reporter, scope);
    assertNotNull(resolved);
  }
}