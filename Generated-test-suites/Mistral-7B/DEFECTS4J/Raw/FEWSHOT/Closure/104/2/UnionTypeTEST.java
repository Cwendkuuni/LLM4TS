package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JsTypeTestBase;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.UnionType.UnionTypeBuilder;

public class UnionTypeTest extends JsTypeTestBase {

  @Test
  public void testUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    JSType intType = registry.getIntType();
    JSType nullType = registry.getNullType();
    JSType voidType = registry.getVoidType();

    Set<JSType> alternates = new HashSet<>();
    alternates.add(stringType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(registry, alternates);

    // Basic tests
    assertEquals("(String|Boolean)", unionType.toString());
    assertTrue(unionType.isUnionType());
    assertFalse(unionType.isObject());
    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());
    assertFalse(unionType.isFunctionType());
    assertFalse(unionType.isArrayType());

    // Gets
    assertEquals(alternates, unionType.getAlternates());

    // Forgive unknown names
    unionType.forgiveUnknownNames();

    // Matches context
    assertFalse(unionType.matchesNumberContext());
    assertFalse(unionType.matchesStringContext());
    assertFalse(unionType.matchesObjectContext());

    // Finds property type
    assertEquals(null, unionType.findPropertyType("toString"));

    // Can assign to
    assertTrue(unionType.canAssignTo(booleanType));
    assertFalse(unionType.canAssignTo(intType));

    // Can be called
    assertTrue(unionType.canBeCalled());

    // Restricts by not null or undefined
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals(alternates, restricted.getAlternates());
    assertEquals(restricted, restricted.restrictByNotNullOrUndefined());

    // Test for equality
    UnionType equal = new UnionType(registry, alternates);
    assertEquals(unionType, equal);

    // Test for inequality
    JSType unequal = new UnionType(registry, ImmutableSet.of(intType));
    assertFalse(unionType.equals(unequal));

    // Test meet
    UnionType meetOther = new UnionType(registry, Arrays.asList(booleanType, intType));
    UnionType meetResult = unionType.meet(meetOther);
    assertEquals(alternates, meetResult.getAlternates());

    // Test isSubtype
    assertTrue(intType.isSubtype(unionType));
    assertFalse(unionType.isSubtype(intType));

    // Test toString
    assertEquals("(String|Boolean)", unionType.toString());

    // Test getLeastSupertype
    JSType leastSupertype = new ObjectType(registry);
    assertEquals(leastSupertype, unionType.getLeastSupertype(leastSupertype));

    // Test resticted union
    JSType restrictedUnion = unionType.getRestrictedUnion(stringType);
    assertTrue(alternates.contains(booleanType) && !alternates.contains(stringType));
    assertEquals(booleanType, restrictedUnion.getAlternates().iterator().next());

    // Test canAssignTo
    JSType numberType = registry.getNumberType();
    assertFalse(unionType.canAssignTo(numberType));
    JSType unionNumberType = new UnionType(registry, ImmutableSet.of(numberType));
    assertTrue(unionNumberType.canAssignTo(unionType));

    // Test equals
    UnionType equalUnionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    assertEquals(unionType, equalUnionType);
  }

  @Test
  public void testUnionTypeWithNullOrUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType nullType = registry.getNullType();
    JSType undefinedType = registry.getUndefinedType();

    Set<JSType> alternates = new HashSet<>();
    // Null type is included in this union type as it is not part of JavaScript language, but functions
    // returning 'void' type can't be used as operands of any operator or statement.
    alternates.add(nullType);
    alternates.add(undefinedType);

    UnionType unionType = new UnionType(registry, alternates);

    // Can be called
    assertTrue(unionType.canBeCalled());

    // Restricts by not null or undefined
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals(ImmutableSet.of(), restricted.getAlternates());
  }

  @Test
  public void testUnionTypeWithError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType errorType = registry.getErrorType();

    Set<JSType> alternates = new HashSet<>();
    alternates.add(errorType);
    alternates.add(registry.getNumberType());

    UnionType unionType = new UnionType(registry, alternates);

    // Test meet
    UnionType meetOther = new UnionType(registry, Arrays.asList(registry.getBooleanType(), registry.getIntType()));
    UnionType meetResult = unionType.meet(meetOther);
    assertEquals(ImmutableSet.of(errorType), meetResult.getAlternates());

    // Test equals
    UnionType equal = new UnionType(registry, alternates);
    assertEquals(unionType, equal);
  }

  @Test
  public void testUnionTypeWithEmptyAlternates() {
    JSTypeRegistry registry = new JSTypeRegistry();

    UnionType unionType = new UnionType(registry, ImmutableSet.of());

    // Basic tests
    assertEquals("()", unionType.toString());
    assertTrue(unionType.isUnionType());
    assertFalse(unionType.isObject());
    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());
    assertFalse(unionType.isFunctionType());
    assertFalse(unionType.isArrayType());

    // Gets
    assertEquals(ImmutableSet.of(), unionType.getAlternates());

    // Forgive unknown names
    unionType.forgiveUnknownNames();

    // Matches context
    assertFalse(unionType.matchesNumberContext());
    assertFalse(unionType.matchesStringContext());
    assertFalse(unionType.matchesObjectContext());

    // Finds property type
    assertEquals(null, unionType.findPropertyType("toString"));

    // Can assign to
    assertTrue(unionType.canAssignTo(unionType));

    // Can be called
    assertTrue(unionType.canBeCalled());

    // Restricts by not null or undefined
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals(ImmutableSet.of(), restricted.getAlternates());

    // Test equals
    UnionType equal = new UnionType(registry, ImmutableSet.of());
    assertEquals(unionType, equal);
  }

  @Test
  public void testUnionTypeWithObjects() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType objectType = registry.getObjectType();

    Set<JSType> alternates = new HashSet<>();
    alternates.add(objectType);
    alternates.add(registry.getStringType());

    UnionType unionType = new UnionType(registry, alternates);

    // Test toString
    assertEquals("(Object|String)", unionType.toString());

    // Test getLeastSupertype
    JSType leastSupertype = new ObjectType(registry);
    assertEquals(leastSupertype, unionType.getLeastSupertype(leastSupertype));

    // Test restricted union
    JSType restrictedUnion = unionType.getRestrictedUnion(objectType);
    assertEquals(ImmutableSet.of(objectType), restrictedUnion.getAlternates());
  }

  @Test
  public void testUnionTypeWithArrays() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType arrayType = registry.getArrayType(registry.getIntType());

    Set<JSType> alternates = new HashSet<>();
    alternates.add(arrayType);
    alternates.add(registry.getStringType());

    UnionType unionType = new UnionType(registry, alternates);

    // Test toString
    assertEquals("(Array<number>|String)", unionType.toString());

    // Test getLeastSupertype
    JSType leastSupertype = registry.getArrayType(registry.getObjectType());
    assertEquals(leastSupertype, unionType.getLeastSupertype(leastSupertype));

    // Test restricted union
    JSType restrictedUnion = unionType.getRestrictedUnion(arrayType);
    assertEquals(ImmutableSet.of(arrayType), restrictedUnion.getAlternates());
  }

  @Test
  public void testUnionTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getObjectType());
    alternates.add(registry.getStringType());
    alternates.add(registry.getBooleanType());

    UnionType unionType = new UnionType(registry, alternates);

    // Test toString
    assertEquals("(Object|String|Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeWithCustomType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType customType = new MyCustomType(registry);

    Set<JSType> alternates = new HashSet<>();
    alternates.add(customType);
    alternates.add(registry.getStringType());

    UnionType unionType = new UnionType(registry, alternates);

    // Test toString
    assertEquals("(MyCustomType|String)", unionType.toString());
  }
}

class MyCustomType extends JSType {
  MyCustomType(JSTypeRegistry registry) {
    super(registry);
  }

  @Override
  public String toString() {
    return "MyCustomType";
  }
}