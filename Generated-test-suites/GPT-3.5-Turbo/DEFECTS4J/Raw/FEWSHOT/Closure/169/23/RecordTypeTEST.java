package com.google.javascript.rhino.jstype;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class RecordTypeTest {
  
  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(null, null, false);
    assertTrue(recordType.isSynthetic());
  }
  
  @Test
  public void testHasProperty() {
    RecordType recordType = new RecordType(null, null, false);
    assertFalse(recordType.hasProperty("property"));
  }
  
  @Test
  public void testGetPropertyType() {
    RecordType recordType = new RecordType(null, null, false);
    assertNull(recordType.getPropertyType("property"));
  }
  
  @Test
  public void testIsSubtype() {
    RecordType recordTypeA = new RecordType(null, null, false);
    RecordType recordTypeB = new RecordType(null, null, false);
    assertTrue(RecordType.isSubtype(recordTypeA, recordTypeB));
  }
  
  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType(null, null, false);
    assertNull(recordType.resolveInternal(null, null));
  }
  
}