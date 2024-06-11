package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

@RunWith(MockitoJUnitRunner.class)
public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = Mockito.mock(JSTypeRegistry.class);
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertySuccess() {
 String propertyName = "testProperty";
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);

 RecordProperty recordProperty = builder.addProperty(propertyName, propertyType, propertyNode);

 assertTrue(recordProperty != null);
 assertEquals(propertyName, recordProperty.getPropertyNode().getIdentifier().getName());
 assertEquals(propertyType, recordProperty.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyDuplicateFailure() {
 String propertyName = "testProperty";
 JSType propertyType = Mockito.mock(JSType.class);
 Node propertyNode = Mockito.mock(Node.class);

 RecordProperty recordProperty = builder.addProperty(propertyName, propertyType, propertyNode);
 RecordProperty recordProperty2 = builder.addProperty(propertyName, propertyType, propertyNode);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType result = builder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, result.getClass());
 }

 @Test
 public void testBuildRecordWithProperties() {
 String propertyName1 = "property1";
 String propertyName2 = "property2";
 JSType propertyType1 = Mockito.mock(JSType.class);
 JSType propertyType2 = Mockito.mock(JSType.class);
 Node propertyNode1 = Mockito.mock(Node.class);
 Node propertyNode2 = Mockito.mock(Node.class);

 RecordProperty property1 = builder.addProperty(propertyName1, propertyType1, propertyNode1);
 RecordProperty property2 = builder.addProperty(propertyName2, propertyType2, propertyNode2);

 JSType result = builder.build();

 assertTrue(result instanceof RecordType);
 Map<String, JSType> expectedProperties = Maps.newHashMap();
 expectedProperties.put(propertyName1, propertyType1);
 expectedProperties.put(propertyName2, propertyType2);

 RecordType recordType = (RecordType) result;
 assertEquals(expectedProperties, recordType.getProperties());
 }
}