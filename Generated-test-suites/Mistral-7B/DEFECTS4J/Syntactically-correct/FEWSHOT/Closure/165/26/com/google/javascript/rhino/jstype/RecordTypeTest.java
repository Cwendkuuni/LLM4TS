package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with empty record types and Object type
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(
        new RecordType(registry, Maps.newHashMap())));

    // Test with RecordType A
    RecordType typeA = new RecordType(registry, Maps.newHashMap()
        .put("propA", new IntType()));

    // Test with RecordType B that is a subtype of A
    RecordType typeB = new RecordType(registry, Maps.newHashMap()
        .put("propA", new IntType())
        .put("propB", new IntType()));

    // Test that A is a subtype of B
    assertTrue(typeA.isSubtype(typeB));

    // Test that B is a subtype of A
    assertTrue(typeB.isSubtype(typeA));

    // Test that A is a subtype of A
    assertTrue(typeA.isSubtype(typeA));

    // Test with RecordType C that is not a subtype of A
    RecordType typeC = new RecordType(registry, Maps.newHashMap()
        .put("propC", new IntType()));

    // Test that A is not a subtype of C
    assertFalse(typeA.isSubtype(typeC));

    // Test with RecordType D that has a mismatched property type
    RecordType typeD = new RecordType(registry, Maps.newHashMap()
        .put("propA", new IntType())
        .put("propB", new StringType()));

    // Test that A is not a subtype of D
    assertFalse(typeA.isSubtype(typeD));

    // Test with RecordType E that has a missing property
    RecordType typeE = new RecordType(registry, Maps.newHashMap()
        .put("propC", new IntType()));

    // Test that A is not a subtype of E
    assertFalse(typeA.isSubtype(typeE));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with basic Object type
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtype = objectType.getGreatestSubtypeHelper(objectType);
    assertTrue(objectType.isEquivalentTo(greatestSubtype));

    // Test with RecordType A
    RecordType typeA = new RecordType(registry, Maps.newHashMap()
        .put("propA", new IntType()));

    // Test with RecordType B that is a subtype of A
    RecordType typeB = new RecordType(registry, Maps.newHashMap()
        .put("propA", new IntType())
        .put("propB", new IntType()));

    // Test that A's greatest subtype is equal to B
    greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);
    assertTrue(typeA.isEquivalentTo(greatestSubtype));

    // Test with RecordType C that is not a subtype of A
    RecordType typeC = new RecordType(registry, Maps.newHashMap()
        .put("propC", new IntType()));

    // Test that A's greatest subtype is equal to Object Type
    greatestSubtype = typeA.getGreatestSubtypeHelper(typeC);
    assertTrue(objectType.isEquivalentTo(greatestSubtype));
  }
}