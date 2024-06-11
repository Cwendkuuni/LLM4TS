package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testEmptyRecord() {
 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 JSType type = registry.getBooleanType();
 Node propertyNode = Node.newIdentifierNode("name");
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, type, propertyNode);
 JSType recordType = builder.build();
 assertNotNull(recordType);
 RecordType record = (RecordType) recordType;
 RecordProperty property = record.getProperty(name);
 assertNotNull(property);
 assertEquals(type, property.getType());
 assertEquals(propertyNode, property.getPropertyNode());
 }

 @Test
 public void testDuplicateProperty() {
 String name = "name";
 JSType type = registry.getBooleanType();
 Node propertyNode = Node.newIdentifierNode("name");
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, type, propertyNode);
 builder.addProperty(name, type, propertyNode);
 JSType recordType = builder.build();
 assertNull(recordType);
 }

 @Test
 public void testBuildRecordWithProperties() {
 String name1 = "name1";
 JSType type1 = registry.getStringType();
 Node propertyNode1 = Node.newIdentifierNode("name1");
 String name2 = "name2";
 JSType type2 = registry.getNumberType();
 Node propertyNode2 = Node.newIdentifierNode("name2");
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name1, type1, propertyNode1);
 builder.addProperty(name2, type2, propertyNode2);
 JSType recordType = builder.build();
 assertNotNull(recordType);
 RecordType record = (RecordType) recordType;
 RecordProperty property1 = record.getProperty(name1);
 RecordProperty property2 = record.getProperty(name2);
 assertNotNull(property1);
 assertNotNull(property2);
 assertEquals(type1, property1.getType());
 assertEquals(type2, property2.getType());
 assertEquals(propertyNode1, property1.getPropertyNode());
 assertEquals(propertyNode2, property2.getPropertyNode());
 }

 @Test
 public void testBuildRecordWithEmptyProperties() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }
}