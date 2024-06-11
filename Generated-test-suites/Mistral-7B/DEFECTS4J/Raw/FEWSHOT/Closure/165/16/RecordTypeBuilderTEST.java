package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = Mockito.spy(JSTypeRegistry.class);
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyWithEmptyBuilder() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 assertNull(builder.addProperty("name", Mockito.mock(JSType.class), Mockito.mock(Node.class)));
 }

 @Test
 public void testAddPropertyWithExistingName() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty("name", Mockito.mock(JSType.class), Mockito.mock(Node.class));
 assertNull(builder.addProperty("name", Mockito.mock(JSType.class), Mockito.mock(Node.class)));
 }

 @Test
 public void testBuildEmptyRecord() {
 Map<String, RecordTypeBuilder.RecordProperty> properties = Collections.emptyMap();
 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClassName());
 }

 @Test
 public void testBuildRecordWithProperties() {
 String propertyName1 = "name1";
 String propertyName2 = "name2";
 JSType propertyType1 = Mockito.mock(JSType.class);
 JSType propertyType2 = Mockito.mock(JSType.class);
 Node propertyNode1 = Mockito.mock(Node.class);
 Node propertyNode2 = Mockito.mock(Node.class);

 builder.addProperty(propertyName1, propertyType1, propertyNode1);
 builder.addProperty(propertyName2, propertyType2, propertyNode2);

 Map<String, RecordTypeBuilder.RecordProperty> properties = builder.properties;
 assertEquals(2, properties.size());
 assertEquals(propertyName1, properties.get(propertyName1).getPropertyNode().getString("id"));
 assertEquals(propertyName2, properties.get(propertyName2).getPropertyNode().getString("id"));

 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals("Record<name1: " + propertyType1.getClassName() + ", name2: " + propertyType2.getClassName() + ">", recordType.getClassName());
 }

 @Test
 public void testBuildRecordWithDuplicateProperties() {
 String propertyName = "name";
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(propertyName, propertyType, propertyNode);
 assertNull(builder.addProperty(propertyName, propertyType, propertyNode));
 }
}