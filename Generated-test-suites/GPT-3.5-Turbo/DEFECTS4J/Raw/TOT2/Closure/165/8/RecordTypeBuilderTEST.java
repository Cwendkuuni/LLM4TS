package com.google.javascript.rhino.jstype;

import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType dummyType = new JSType();
    Node dummyNode = new Node();

    // Typical case
    RecordTypeBuilder result = builder.addProperty("testProp", dummyType, dummyNode);
    assertNotNull(result);

    // Edge case - adding a duplicate property
    result = builder.addProperty("testProp", dummyType, dummyNode);
    assertNull(result);
  }

  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Typical case - build a non-empty record
    JSType dummyType = new JSType();
    Node dummyNode = new Node();
    builder.addProperty("testProp1", dummyType, dummyNode);
    builder.addProperty("testProp2", dummyType, dummyNode);
    JSType recordType = builder.build();
    assertNotEquals(JSTypeNative.OBJECT_TYPE, recordType);

    // Edge case - build an empty record
    builder = new RecordTypeBuilder(registry);
    JSType emptyRecordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecordType);
  }

}