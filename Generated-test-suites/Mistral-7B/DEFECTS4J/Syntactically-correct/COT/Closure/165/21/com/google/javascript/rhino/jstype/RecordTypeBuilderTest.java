package com.google.javascript.rhino.jstype.test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.HashMap;

/**
 * Tests for the RecordTypeBuilder class.
 */
public class RecordTypeBuilderTest {

 @Test
 public void testRecordTypeBuilderWithEmptyList() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 }

 @Test
 public void testAddPropertyWithExistingName() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 recordTypeBuilder.addProperty("name", JSType.STRING, null);
 assertNull(recordTypeBuilder.addProperty("name", JSType.NUMBER, null));
 }

 @Test
 public void testBuildWithExistingProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 recordTypeBuilder.addProperty("name", JSType.STRING, null);
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);

 RecordType expectedRecordType = new RecordType(
 new JSTypeRegistry(), Collections.singletonMap("name", JSType.STRING));
 assert recordType.equals(expectedRecordType);
 }

 @Test(expected = NullPointerException.class)
 public void testAddPropertyWithNullRegistry() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
 }

 @Test
 public void testAddPropertyWithNullName() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 assertNull(recordTypeBuilder.addProperty(null, JSType.STRING, null));
 }

 @Test
 public void testAddPropertyWithNullType() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 assertNull(recordTypeBuilder.addProperty("name", null, null));
 }

 @Test
 public void testAddPropertyWithNullPropertyNode() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 assertNull(recordTypeBuilder.addProperty("name", JSType.STRING, null));
 }

 @Test
 public void testAddPropertyMultipleTimesWithSameNameDifferentTypes() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 recordTypeBuilder.addProperty("name", JSType.STRING, null);
 recordTypeBuilder.addProperty("name", JSType.NUMBER, null);

 JSType recordType = recordTypeBuilder.build();
 RecordType expectedRecordType = new RecordType(
 new JSTypeRegistry(), Maps.newHashMap(
 Collections.singletonMap("name", new RecordProperty(JSType.NUMBER, null))));
 assert recordType.equals(expectedRecordType);
 }
}