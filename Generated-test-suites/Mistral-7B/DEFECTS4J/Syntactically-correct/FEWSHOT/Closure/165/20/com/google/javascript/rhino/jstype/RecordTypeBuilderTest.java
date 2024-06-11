package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

 JSTypeRegistry registry;
 RecordTypeBuilder builder;
 JSType recordType;

 @Test
 public void testEmptyRecordType() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 @Test
 public void testNonEmptyRecordType() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 String propertyName = "prop";
 JSType propertyType = JSType.STRING;
 Node propertyNode = new Node.ObjectLiteral();
 builder.addProperty(propertyName, propertyType, propertyNode);
 recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(RecordType.class, recordType.getClass());
 RecordType record = (RecordType) recordType;
 RecordProperty recordProperty = record.getProperties().get(propertyName);
 assertNotNull(recordProperty);
 assertEquals(propertyType, recordProperty.getType());
 assertEquals(propertyNode, recordProperty.getPropertyNode());
 }

 @Test
 public void testAddingDuplicateProperty() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 String propertyName = "prop";
 JSType propertyType = JSType.STRING;
 Node propertyNode = new Node.ObjectLiteral();
 builder.addProperty(propertyName, propertyType, propertyNode);
 builder.addProperty(propertyName, propertyType, propertyNode);
 recordType = builder.build();
 assertNull(recordType);
 }

 @Test
 public void testAddingNullBuilder() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 builder = null;
 recordType = builder.build();
 assertNull(recordType);
 }
}