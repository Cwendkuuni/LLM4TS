package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

 JSTypeRegistry registry = new JSTypeRegistry();
 RecordTypeBuilder recordTypeBuilder;

 @Test
 public void testEmptyRecordTypeBuilder() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType emptyRecordType = recordTypeBuilder.build();
 assertNotNull(emptyRecordType);
 }

 @Test
 public void testNonEmptyRecordTypeBuilder() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty("name", registry.getStringType(), null);
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 RecordType expectedRecordType = new RecordType(registry,
 Maps.newHashMap());
 expectedRecordType.addRecordProperty("name", registry.getStringType());
 assertEquals(expectedRecordType.getProperties(), recordType.getProperties());
 }

 @Test
 public void testAddingDuplicateProperty() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty("name", registry.getStringType(), null);
 recordTypeBuilder.addProperty("name", registry.getStringType(), null);
 JSType recordType = recordTypeBuilder.build();
 assertNull(recordType);
 }

 @Test
 public void testAddingNullRegistry() {
 assertNull(new RecordTypeBuilder(null).build());
 }

 @Test
 public void testAddingNullName() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 assertNull(recordTypeBuilder.addProperty(null, registry.getStringType(), null));
 }

 @Test
 public void testAddingNullType() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 Node node = new Node();
 assertNull(recordTypeBuilder.addProperty("name", null, node));
 }

 @Test
 public void testAddingNullNode() {
 recordTypeBuilder = new RecordTypeBuilder(registry);
 assertNull(recordTypeBuilder.addProperty("name", registry.getStringType(), null));
 }
}