package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry(null, null);
    recordBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = recordBuilder.addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();
    RecordType recordType2 = recordBuilder.addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();
    RecordType recordType3 = recordBuilder.addProperty("y", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();

    assertTrue(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testIsSubtype() {
    RecordType supertype = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
        .addProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE), null)
        .build();

    RecordType subtype1 = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
        .addProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE), null)
        .build();

    RecordType subtype2 = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
        .build();

    assertTrue(subtype1.isSubtype(supertype));
    assertFalse(subtype2.isSubtype(supertype));
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType recordType1 = recordBuilder.addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();
    RecordType recordType2 = recordBuilder.addProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE), null).build();
    RecordType recordType3 = recordBuilder.addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();

    JSType leastSupertype1 = recordType1.getLeastSupertype(recordType2);
    JSType leastSupertype2 = recordType2.getLeastSupertype(recordType3);

    assertTrue(leastSupertype1.isRecordType());
    assertTrue(leastSupertype2.isRecordType());
  }

  @Test
  public void testGetGreatestSubtype() {
    RecordType recordType1 = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
        .addProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE), null)
        .build();

    RecordType recordType2 = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null)
        .build();

    RecordType recordType3 = recordBuilder
        .addProperty("x", registry.getNativeType(JSTypeNative.STRING_TYPE), null)
        .build();

    JSType greatestSubtype1 = recordType1.getGreatestSubtype(recordType2);
    JSType greatestSubtype2 = recordType2.getGreatestSubtype(recordType3);

    assertTrue(greatestSubtype1.isRecordType());
    assertTrue(greatestSubtype2.isRecordType());
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = recordBuilder.build();

    assertFalse(recordType.defineProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = recordBuilder.addProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();

    JSType resolvedType = recordType.resolveInternal(null, null);

    assertTrue(resolvedType.isRecordType());
  }
}