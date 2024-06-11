package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(null, Maps.newHashMap(), true);
    recordType1.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType1.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    RecordType recordType2 = new RecordType(null, Maps.newHashMap(), true);
    recordType2.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType2.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    RecordType recordType3 = new RecordType(null, Maps.newHashMap(), true);
    recordType3.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType3.defineProperty("c", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(null, Maps.newHashMap(), true);
    recordType1.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType1.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    RecordType recordType2 = new RecordType(null, Maps.newHashMap(), true);
    recordType2.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType2.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    recordType2.defineProperty("c", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);

    RecordType recordType3 = new RecordType(null, Maps.newHashMap(), true);
    recordType3.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType3.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    assertTrue(recordType1.isSubtype(recordType2));

    RecordType recordType4 = new RecordType(null, Maps.newHashMap(), true);
    recordType4.defineProperty("c", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), false, null);
    recordType4.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType4.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    assertFalse(recordType1.isSubtype(recordType4));

    RecordType recordType5 = new RecordType(null, Maps.newHashMap(), true);
    recordType5.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

    assertTrue(recordType1.isSubtype(recordType5));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType(null, Maps.newHashMap(), true);
    recordType1.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType1.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

    RecordType recordType2 = new RecordType(null, Maps.newHashMap(), true);
    recordType2.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType2.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    recordType2.defineProperty("c", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

    RecordType recordType3 = new RecordType(null, Maps.newHashMap(), true);
    recordType3.defineProperty("a", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);
    recordType3.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);
    recordType3.defineProperty("d", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

    JSType greatestSubtype1 = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2.getClass(), greatestSubtype1.getClass());

    JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(recordType3);
    JSType unionType = new UnionTypeBuilder(registry).build();
    unionType.addAlternate(recordType2.toMaybeRecordType());
    unionType.addAlternate(recordType3.toMaybeRecordType());
    assertEquals(unionType, greatestSubtype2);
  }

  @Test
  public void testIsSynthetic() {
    RecordType recordType1 = new RecordType(null, Maps.newHashMap(), true);
    assertTrue(recordType1.isSynthetic() == false);

    RecordType recordType2 = new RecordType(null, Maps.newHashMap(), false);
    assertTrue(recordType2.isSynthetic() == true);
  }
}