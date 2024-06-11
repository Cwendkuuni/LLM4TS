package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test file for RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType recordType = recordTypeBuilder
        .addProperty("prop1", JSType.STRING, null)
        .addProperty("prop2", JSType.NUMBER, null)
        .build();

    assertNotNull(recordType);
    assertTrue(recordType instanceof RecordType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    recordTypeBuilder.addProperty("prop1", JSType.STRING, null);
    recordTypeBuilder.addProperty("prop1", JSType.NUMBER, null);
  }

  @Test
  public void testEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType recordType = recordTypeBuilder.build();

    assertNotNull(recordType);
    assertTrue(recordType instanceof JSTypeNative);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }

  @Test
  public void testRecordTypeBuilderWithNullRegistry() {
    assertNull(new RecordTypeBuilder(null));
  }
}