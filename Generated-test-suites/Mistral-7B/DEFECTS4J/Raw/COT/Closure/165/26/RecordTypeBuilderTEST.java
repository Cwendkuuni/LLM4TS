package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

/**
 * Test class for RecordTypeBuilder.
 *
 */
public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 // Test for addProperty method
 @Test
 public void testAddProperty() {
 String name = "name";
 JSType type = registry.getPrimitiveType(JSType.STRING);
 Node propertyNode = new Node(); // A dummy node
 RecordProperty expectedResult = new RecordProperty(type, propertyNode);

 RecordProperty result = builder.addProperty(name, type, propertyNode);

 assertEquals(expectedResult.getType(), result.getType());
 assertSame(expectedResult.getPropertyNode(), result.getPropertyNode());
 assertNull(builder.build()); // The builder should not build until build() is called
 }

 // Test for addProperty with duplicate name
 @Test(expected = IllegalStateException.class)
 public void testAddPropertyDuplicateName() {
 String name = "name";
 JSType type = registry.getPrimitiveType(JSType.STRING);
 Node propertyNode = new Node(); // A dummy node
 RecordProperty property = new RecordProperty(type, propertyNode);
 builder.addProperty(name, type, propertyNode);
 builder.addProperty(name, type, propertyNode);
 }

 // Test for build method with empty record
 @Test
 public void testBuildEmptyRecord() {
 JSType result = builder.build();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
 }

 // Test for build method with non-empty record
 @Test
 public void testBuildNonEmptyRecord() {
 String name1 = "name1";
 String name2 = "name2";
 JSType type1 = registry.getPrimitiveType(JSType.STRING);
 JSType type2 = registry.getPrimitiveType(JSType.NUMBER);
 Node propertyNode1 = new Node(); // A dummy node for name1
 Node propertyNode2 = new Node(); // A dummy node for name2

 builder.addProperty(name1, type1, propertyNode1);
 builder.addProperty(name2, type2, propertyNode2);

 RecordType result = (RecordType) builder.build();
 Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
 expectedProperties.put(name1, new RecordProperty(type1, propertyNode1));
 expectedProperties.put(name2, new RecordProperty(type2, propertyNode2));

 assertEquals(Collections.unmodifiableMap(expectedProperties), result.getProperties());
 }

 // Test for build method with null registry
 @Test(expected = NullPointerException.class)
 public void testBuildWithNullRegistry() {
 JSTypeRegistry nullRegistry = null;
 RecordTypeBuilder builder = new RecordTypeBuilder(nullRegistry);
 builder.build();
 }
}