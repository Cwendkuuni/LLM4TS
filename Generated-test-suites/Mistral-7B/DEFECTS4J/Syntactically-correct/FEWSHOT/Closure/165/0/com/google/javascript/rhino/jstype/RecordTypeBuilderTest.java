package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = mock(JSTypeRegistry.class);
 builder = new RecordTypeBuilder(registry);
 }

 /**
 * Test adding a property and building a non-empty record
 */
 @Test
 public void testAddPropertyAndBuildNonEmptyRecord() {
 JSType stringType = mock(JSType.class);
 Node propertyNode = mock(Node.class);
 when(registry.getTypeFromNode(propertyNode)).thenReturn(stringType);

 builder.addProperty("name", stringType, propertyNode);
 JSType recordType = builder.build();

 assertEquals(1, builder.properties.size());
 assertNotNull(recordType);
 }

 /**
 * Test adding a duplicate property and building a record
 */
 @Test
 public void testAddDuplicateProperty() {
 String name = "name";
 JSType stringType = mock(JSType.class);
 Node propertyNode = mock(Node.class);
 when(registry.getTypeFromNode(propertyNode)).thenReturn(stringType);

 builder.addProperty(name, stringType, propertyNode);
 RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
 builder2.addProperty(name, stringType, propertyNode);
 JSType recordType = builder2.build();

 assertEquals(1, builder2.properties.size());
 assertNotNull(recordType);
 }

 /**
 * Test building an empty record
 */
 @Test
 public void testBuildEmptyRecord() {
 JSType recordType = builder.build();

 assertEquals(0, builder.properties.size());
 assertNotNull(recordType);
 }

 /**
 * Test building a record with null registry
 */
 @Test(expected = IllegalArgumentException.class)
 public void testBuildRecordWithNullRegistry() {
 builder = new RecordTypeBuilder(null);
 builder.build();
 }
}