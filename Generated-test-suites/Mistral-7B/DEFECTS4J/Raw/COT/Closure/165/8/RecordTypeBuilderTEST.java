package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyWithEmptyRecord() {
 RecordType type = builder.build();
 assertNotNull(type);
 }

 @Test
 public void testAddSamePropertyTwice() {
 String name = "name";
 JSType type = registry.getSimpleType("String");
 Node propertyNode = new Node(); // Create a dummy node
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 RecordProperty property = builder.addProperty(name, type, propertyNode);
 RecordProperty property2 = builder.addProperty(name, type, propertyNode);
 assertNull(property2);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPropertyWithDuplicateName() {
 String name = "name";
 JSType type = registry.getSimpleType("String");
 Node propertyNode = new Node(); // Create a dummy node
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, type, propertyNode);
 builder.addProperty(name, type, propertyNode);
 }

 @Test
 public void testBuildWithEmptyRecord() {
 RecordType type = builder.build();
 assertNotNull(type);
 }

 @Test
 public void testBuildWithRecord() {
 String name1 = "name1";
 String name2 = "name2";
 JSType stringType = registry.getSimpleType("String");
 Node propertyNode1 = new Node(); // Create a dummy node
 Node propertyNode2 = new Node(); // Create a dummy node

 builder.addProperty(name1, stringType, propertyNode1);
 builder.addProperty(name2, stringType, propertyNode2);
 RecordType type = builder.build();
 assertNotNull(type);

 RecordProperty prop1 = ((RecordType) type).getProperty(name1);
 RecordProperty prop2 = ((RecordType) type).getProperty(name2);
 assertNotNull(prop1);
 assertNotNull(prop2);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullType() {
 String name = "name";
 Node propertyNode = new Node(); // Create a dummy node
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, null, propertyNode);
 builder.build(); // Should throw an exception
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullPropertyNode() {
 String name = "name";
 JSType stringType = registry.getSimpleType("String");
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(name, stringType, null);
 builder.build(); // Should throw an exception
 }
}