package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import static com.google.javascript.rhino.NodeType.*;

public class RecordTypeBuilderTest {

 JSTypeRegistry registry = new JSTypeRegistry();
 RecordTypeBuilder builder = null;

 @Test
 public void testEmptyRecordType() {
 builder = new RecordTypeBuilder(registry);
 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 @Test
 public void testRecordTypeWithOneProperty() {
 builder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 JSType propertyType = registry.getNullType();
 Node propertyNode = newNode(STRING, null);
 builder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = builder.build();

 assertNotNull(recordType);
 assertEquals(RecordType.class, recordType.getClass());
 HashMap<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
 RecordProperty property = properties.get(propertyName);
 assertNotNull(property);
 assertEquals(propertyType, property.getType());
 assertNull(property.getPropertyNode());
 }

 @Test
 public void testRecordTypeWithDuplicateProperty() {
 builder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 JSType propertyType = registry.getTypeByName("String");
 Node propertyNode = newNode(STRING, null);
 builder.addProperty(propertyName, propertyType, propertyNode);
 builder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = builder.build();
 assertNull(recordType);
 }

 @Test
 public void testRecordTypeWithPropertyNode() {
 builder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 JSType propertyType = registry.getTypeByName("String");
 Node propertyNode = newNode(STRING, "John Doe");
 builder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = builder.build();

 assertNotNull(recordType);
 assertEquals(RecordType.class, recordType.getClass());
 HashMap<String, RecordProperty> properties = ((RecordType) recordType).getProperties();
 RecordProperty property = properties.get(propertyName);
 assertNotNull(property);
 assertEquals(propertyType, property.getType());
 assertNotNull(property.getPropertyNode());
 assertNotNull(property.getPropertyNode().getName());
 assertEquals("John Doe", property.getPropertyNode().getValue());
 }

 @Test
 public void testBuildRecordTypeTwice() {
 builder = new RecordTypeBuilder(registry);
 String propertyName1 = "name";
 JSType propertyType1 = registry.getTypeByName("String");
 Node propertyNode1 = newNode(STRING, null);
 builder.addProperty(propertyName1, propertyType1, propertyNode1);
 String propertyName2 = "age";
 JSType propertyType2 = registry.getTypeByName("Number");
 Node propertyNode2 = newNode(NUMBER, null);
 builder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType recordType1 = builder.build();
 JSType recordType2 = builder.build();
 assertNotNull(recordType1);
 assertNotNull(recordType2);
 assertEquals(recordType1, recordType2);
 }
}