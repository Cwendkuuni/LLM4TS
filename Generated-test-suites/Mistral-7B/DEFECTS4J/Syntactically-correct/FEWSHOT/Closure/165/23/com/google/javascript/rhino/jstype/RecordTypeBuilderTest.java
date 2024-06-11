package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeBuilderTest {

 JSTypeRegistry registry;
 RecordTypeBuilder builder;

 @org.junit.Before
 public void setUp() {
 registry = Mockito.mock(JSTypeRegistry.class);
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 JSType type = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
 assertNotNull(recordProperty);
 assertEquals(name, recordProperty.getPropertyName());
 assertEquals(type, recordProperty.getType());
 assertEquals(propertyNode, recordProperty.getPropertyNode());
 }

 @Test
 public void testAddDuplicateProperty() {
 String name = "name";
 JSType type = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);
 RecordProperty recordProperty = builder.addProperty(name, type, propertyNode);
 assertNull(builder.addProperty(name, type, propertyNode));
 }

 @Test
 public void testEmptyRecordType() {
 JSType recordType = builder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClassName());
 }

 @Test
 public void testNonEmptyRecordType() {
 String name1 = "name1";
 String name2 = "name2";
 JSType type1 = Mockito.mock(JSType.class);
 JSType type2 = Mockito.mock(JSType.class);
 Node propertyNode1 = Mockito.mock(Node.class);
 Node propertyNode2 = Mockito.mock(Node.class);
 builder.addProperty(name1, type1, propertyNode1);
 builder.addProperty(name2, type2, propertyNode2);
 JSType recordType = builder.build();
 assertEquals(JSTYPE_RECORD, recordType.getClassName());
 HashMap<String, RecordProperty> properties = recordType.getProperties();
 assertEquals(2, properties.size());
 RecordProperty recordProperty1 = properties.get(name1);
 assertEquals(name1, recordProperty1.getPropertyName());
 assertEquals(type1, recordProperty1.getType());
 assertEquals(propertyNode1, recordProperty1.getPropertyNode());
 RecordProperty recordProperty2 = properties.get(name2);
 assertEquals(name2, recordProperty2.getPropertyName());
 assertEquals(type2, recordProperty2.getType());
 assertEquals(propertyNode2, recordProperty2.getPropertyNode());
 }
}