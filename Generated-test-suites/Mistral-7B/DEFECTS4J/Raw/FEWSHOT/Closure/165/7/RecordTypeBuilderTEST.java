package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecordTypeBuilderTest {

 JSTypeRegistry mockRegistry;
 RecordTypeBuilder builder;

 @Test
 public void testEmptyRecordTypeBuilder() {
 builder = new RecordTypeBuilder(mockRegistry);
 assertNotNull(builder.build());
 }

 @Test
 public void testRecordTypeBuilderWithSingleProperty() {
 String propertyName = "testProperty";
 JSType propertyType = new IntType();
 Node propertyNode = new Node();

 mockRegistry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(mockRegistry);
 RecordProperty recordedProperty = builder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = builder.build();
 RecordType actualRecordType = (RecordType) recordType;
 RecordProperty actualProperty = actualRecordType.getProperty(propertyName);

 assertNull(recordedProperty); // Check that no duplicate property was added
 assertNotNull(actualProperty);
 assertNotNull(actualProperty.getPropertyNode());
 assertEquals(propertyType, actualProperty.getType());
 }

 @Test(expected = NullPointerException.class)
 public void testRecordTypeBuilderWithNullPropertyType() {
 String propertyName = "testProperty";
 Node propertyNode = new Node();
 mockRegistry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(mockRegistry);
 builder.addProperty(propertyName, null, propertyNode);
 }

 @Test
 public void testRecordTypeBuilderWithDuplicateProperty() {
 String propertyName = "testProperty";
 JSType propertyType = new IntType();
 Node propertyNode = new Node();

 mockRegistry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(mockRegistry);
 builder.addProperty(propertyName, propertyType, propertyNode);
 builder.addProperty(propertyName, propertyType, propertyNode);
 assertNull(builder.build());
 }

 @Test
 public void testRecordTypeBuilderWithMultipleProperties() {
 String propertyName1 = "testProperty1";
 String propertyName2 = "testProperty2";
 JSType propertyType1 = new IntType();
 JSType propertyType2 = new StringType();
 Node propertyNode1 = new Node();
 Node propertyNode2 = new Node();

 mockRegistry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(mockRegistry);
 RecordProperty property1 = builder.addProperty(propertyName1, propertyType1, propertyNode1);
 RecordProperty property2 = builder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType recordType = builder.build();
 RecordType actualRecordType = (RecordType) recordType;
 RecordProperty actualProperty1 = actualRecordType.getProperty(propertyName1);
 RecordProperty actualProperty2 = actualRecordType.getProperty(propertyName2);

 assertNotNull(property1);
 assertNotNull(property2);
 assertNull(property1.getNextProperty());
 assertNull(property2.getNextProperty());
 assertEquals(propertyType1, actualProperty1.getType());
 assertEquals(propertyType2, actualProperty2.getType());
 }
}