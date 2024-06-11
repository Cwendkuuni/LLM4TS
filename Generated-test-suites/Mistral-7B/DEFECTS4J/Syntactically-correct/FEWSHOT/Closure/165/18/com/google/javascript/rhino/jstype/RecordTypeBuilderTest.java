package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setup() {
 registry = mock(JSTypeRegistry.class);
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType result = builder.build();
 assertNotNull(result);
 assertNotNull(result.getTypeName());
 assertEquals(JSTypeNative.OBJECT_TYPE, result.getTypeClass());
 }

 @Test
 public void testAddProperty() {
 String propertyName = "foo";
 JSType propertyType = mock(JSType.class);
 Node propertyNode = mock(Node.class);

 builder.addProperty(propertyName, propertyType, propertyNode);
 assertFalse(builder.isEmpty());

 Map<String, RecordTypeBuilder.RecordProperty> properties = builder.properties;
 RecordTypeBuilder.RecordProperty recordedProperty = properties.get(propertyName);
 assertEquals(propertyType, recordedProperty.getType());
 assertEquals(propertyNode, recordedProperty.getPropertyNode());
 }

 @Test
 public void testAddDuplicateProperty() {
 String propertyName = "foo";
 JSType propertyType = mock(JSType.class);
 Node propertyNode = mock(Node.class);
 RecordTypeBuilder.RecordProperty recordedProperty = new RecordTypeBuilder.RecordProperty(propertyType, propertyNode);
 builder.properties.put(propertyName, recordedProperty);

 RecordTypeBuilder.RecordProperty newProperty = builder.addProperty(propertyName, propertyType, propertyNode);
 assertNull(newProperty);
 }

 @Test
 public void testBuildWithProperties() {
 String propertyName1 = "foo";
 JSType propertyType1 = mock(JSType.class);
 Node propertyNode1 = mock(Node.class);
 builder.addProperty(propertyName1, propertyType1, propertyNode1);

 String propertyName2 = "bar";
 JSType propertyType2 = mock(JSType.class);
 Node propertyNode2 = mock(Node.class);
 builder.addProperty(propertyName2, propertyType2, propertyNode2);

 JSType result = builder.build();
 assertNotNull(result);
 assertNotNull(result.getTypeName());
 assertFalse(result.getTypeName().isEmpty());
 }
}