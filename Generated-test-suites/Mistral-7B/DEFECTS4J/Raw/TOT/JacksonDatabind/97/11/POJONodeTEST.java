package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test for getNodeType() method.
 */
 @Test
 public void testGetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 /**
 * Test for asText() method with null value.
 */
 @Test
 public void testAsTextNullValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText());
 }

 /**
 * Test for asText() method with non-null value.
 */
 @Test
 public void testAsTextNonNullValue() {
 Object obj = new Object();
 POJONode pojoNode = new POJONode(obj);
 assertEquals(obj.toString(), pojoNode.asText());
 }

 /**
 * Test for asBoolean() method with default value.
 */
 @Test
 public void testAsBooleanDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertFalse(pojoNode.asBoolean(false));
 }

 /**
 * Test for asBoolean() method with Boolean value.
 */
 @Test
 public void testAsBooleanBooleanValue() {
 POJONode pojoNode = new POJONode(Boolean.TRUE);
 assertTrue(pojoNode.asBoolean(false));
 }

 /**
 * Test for asInt() method with default value.
 */
 @Test
 public void testAsIntDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0, pojoNode.asInt(0));
 }

 /**
 * Test for asInt() method with Number value.
 */
 @Test
 public void testAsIntNumberValue() {
 POJONode pojoNode = new POJONode(Integer.valueOf(42));
 assertEquals(42, pojoNode.asInt(0));
 }

 /**
 * Test for asLong() method with default value.
 */
 @Test
 public void testAsLongDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0L, pojoNode.asLong(0L));
 }

 /**
 * Test for asLong() method with Number value.
 */
 @Test
 public void testAsLongNumberValue() {
 POJONode pojoNode = new POJONode(Long.valueOf(42L));
 assertEquals(42L, pojoNode.asLong(0L));
 }

 /**
 * Test for asDouble() method with default value.
 */
 @Test
 public void testAsDoubleDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
 }

 /**
 * Test for asDouble() method with Number value.
 */
 @Test
 public void testAsDoubleNumberValue() {
 POJONode pojoNode = new POJONode(Double.valueOf(3.14));
 assertEquals(3.14, pojoNode.asDouble(0.0), 0.001);
 }

 /**
 * Test for serialize() method with null value.
 */
 @Test
 public void testSerializeNullValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(null);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoNode);
 assertNull(json);
 }

 /**
 * Test for serialize() method with non-null JsonSerializable value.
 */
 @Test
 public void testSerializeJsonSerializableValue() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 POJONode pojoNode = new POJONode(myClass);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoNode);
 assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", json);
 }

 /**
 * Test for serialize() method with non-JsonSerializable value.
 */
 @Test
 public void testSerializeNonJsonSerializableValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(new byte[0]);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoNode);
 assertTrue(json.startsWith("{"));
 assertTrue(json.endsWith("}"));
 }

 /**
 * Test for equals() method with null value.
 */
 @Test
 public void testEqualsNullValue() {
 POJONode pojoNode1 = new POJONode(null);
 POJONode pojoNode2 = new POJONode(null);
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 /**
 * Test for equals() method with same value.
 */
 @Test
 public void testEqualsSameValue() {
 Object obj = new Object();
 POJONode pojoNode1 = new POJONode(obj);
 POJONode pojoNode2 = new POJONode(obj);
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 /**
 * Test for equals() method with different values.
 */
 @Test
 public void testEqualsDifferentValues() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 POJONode pojoNode1 = new POJONode(obj1);
 POJONode pojoNode2 = new POJONode(obj2);
 assertFalse(pojoNode1.equals(pojoNode2));
 }

 /**
 * Test for hashCode() method.
 */
 @Test
 public void testHashCode() {
 Object obj = new Object();
 POJONode pojoNode1 = new POJONode(obj);
 Object obj2 = new Object();
 POJONode pojoNode2 = new POJONode(obj2);
 int hashCode1 = pojoNode1.hashCode();
 int hashCode2 = pojoNode2.hashCode();
 assertFalse(hashCode1 == hashCode2);
 }
}

class MyClass implements JsonSerializable {
 String field1 = "value1";
 String field2 = "value2";

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("field1");
 gen.writeString(field1);
 gen.writeFieldName("field2");
 gen.writeString(field2);
 }
}