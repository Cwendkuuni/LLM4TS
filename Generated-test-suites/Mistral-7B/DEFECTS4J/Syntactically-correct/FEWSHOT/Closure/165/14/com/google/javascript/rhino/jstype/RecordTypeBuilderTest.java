package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry() {};
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddProperty() {
 String name = "prop1";
 JSType type = registry.getNumberType();
 Node propertyNode = new Node();
 RecordTypeBuilder recordTypeBuilder = builder.addProperty(name, type, propertyNode);

 assertNotNull(recordTypeBuilder);
 Map<String, RecordProperty> properties = recordTypeBuilder.build().getProperties();
 RecordProperty recordProperty = properties.get(name);

 assertNotNull(recordProperty);
 assertEquals(type, recordProperty.getType());
 assertEquals(propertyNode, recordProperty.getPropertyNode());
 }

 @Test
 public void testAddDuplicateProperty() {
 String name = "prop1";
 JSType type = registry.getNumberType();
 Node propertyNode = new Node();
 RecordTypeBuilder recordTypeBuilder = builder.addProperty(name, type, propertyNode);

 assertNull(recordTypeBuilder);
 }

 @Test
 public void testBuildEmptyRecordType() {
 JSType recordType = builder.build();
 JSTypeNative objectType = JSTypeNative.OBJECT_TYPE;

 assertEquals(objectType, recordType);
 }

 @Test
 public void testBuildRecordTypeWithProperties() {
 String prop1 = "prop1";
 String prop2 = "prop2";
 JSType numberType = registry.getNumberType();
 JSType stringType = registry.getStringType();
 Node propertyNode1 = new Node();
 Node propertyNode2 = new Node();
 RecordTypeBuilder recordTypeBuilder = builder
 .addProperty(prop1, numberType, propertyNode1)
 .addProperty(prop2, stringType, propertyNode2);
 JSType recordType = recordTypeBuilder.build();

 Map<String, RecordProperty> properties = recordType.getProperties();
 RecordProperty recordProperty1 = properties.get(prop1);
 RecordProperty recordProperty2 = properties.get(prop2);

 assertNotNull(recordProperty1);
 assertEquals(numberType, recordProperty1.getType());
 assertEquals(propertyNode1, recordProperty1.getPropertyNode());

 assertNotNull(recordProperty2);
 assertEquals(stringType, recordProperty2.getType());
 assertEquals(propertyNode2, recordProperty2.getPropertyNode());
 }
}