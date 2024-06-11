package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

 private POJONode pojoNode;

 @Before
 public void setup() {
 pojoNode = new POJONode(new String("Hello World"));
 }

 /*
 * Testing getNodeType method
 */
 @Test
 public void testgetNodeType() {
 JsonNodeType expectedNodeType = JsonNodeType.POJO;
 JsonNodeType actualNodeType = pojoNode.getNodeType();
 assertEquals(expectedNodeType, actualNodeType);
 }

 /*
 * Testing getNodeType method when POJONode instance is null
 */
 @Test(expected = NullPointerException.class)
 public void testgetNodeTypeWithNullPOJONode() {
 POJONode nullNode = null;
 JsonNodeType actualNodeType = nullNode.getNodeType();
 }

 /*
 * Testing asText method
 */
 @Test
 public void testAsText() {
 String expectedText = "Hello World";
 String actualText = pojoNode.asText();
 assertEquals(expectedText, actualText);
 }

 /*
 * Testing asText method when POJONode instance is null
 */
 @Test(expected = NullPointerException.class)
 public void testAsTextWithNullPOJONode() {
 POJONode nullNode = null;
 String actualText = nullNode.asText();
 }

 /*
 * Testing asText method with defaultValue
 */
 @Test
 public void testAsTextWithDefaultValue() {
 String expectedDefaultText = "defaultValue";
 String actualText = pojoNode.asText("defaultValue");
 assertEquals(expectedDefaultText, actualText);
 }

 /*
 * Testing asBoolean method
 */
 @Test
 public void testAsBoolean() {
 boolean expectedBoolean = true;
 boolean actualBoolean = pojoNode.asBoolean(expectedBoolean);
 assertTrue(actualBoolean);
 }

 /*
 * Testing asBoolean method with defaultValue
 */
 @Test
 public void testAsBooleanWithDefaultValue() {
 boolean expectedBoolean = false;
 boolean actualBoolean = pojoNode.asBoolean(expectedBoolean);
 assertEquals(expectedBoolean, actualBoolean);
 }

 /*
 * Testing asInt method
 */
 @Test
 public void testAsInt() {
 int expectedInt = 42;
 int actualInt = pojoNode.asInt(expectedInt);
 assertEquals(expectedInt, actualInt);
 }

 /*
 * Testing asInt method with defaultValue
 */
 @Test
 public void testAsIntWithDefaultValue() {
 int expectedInt = 0;
 int actualInt = pojoNode.asInt(expectedInt);
 assertEquals(expectedInt, actualInt);
 }

 /*
 * Testing asLong method
 */
 @Test
 public void testAsLong() {
 long expectedLong = 42L;
 long actualLong = pojoNode.asLong(expectedLong);
 assertEquals(expectedLong, actualLong);
 }

 /*
 * Testing asLong method with defaultValue
 */
 @Test
 public void testAsLongWithDefaultValue() {
 long expectedLong = 0L;
 long actualLong = pojoNode.asLong(expectedLong);
 assertEquals(expectedLong, actualLong);
 }

 /*
 * Testing asDouble method
 */
 @Test
 public void testAsDouble() {
 double expectedDouble = 42.0;
 double actualDouble = pojoNode.asDouble(expectedDouble);
 assertEquals(expectedDouble, actualDouble, 0.001);
 }

 /*
 * Testing asDouble method with defaultValue
 */
 @Test
 public void testAsDoubleWithDefaultValue() {
 double expectedDouble = 0.0;
 double actualDouble = pojoNode.asDouble(expectedDouble);
 assertEquals(expectedDouble, actualDouble, 0.001);
 }

 /*
 * Testing serialize method
 */
 @Test
 public void testSerialize() throws IOException {
 String expectedJson = "\"Hello World\"";
 String actualJson = "";
 JsonGenerator gen = null;
 SerializerProvider ctxt = null;
 pojoNode.serialize(gen, ctxt);
 if (gen != null) {
 actualJson = gen.getOutputBuffer().toString();
 }
 assertEquals(expectedJson, actualJson);
 }

 /*
 * Testing serialize method when POJONode instance is null
 */
 @Test(expected = NullPointerException.class)
 public void testSerializeWithNullPOJONode() throws IOException {
 POJONode nullNode = null;
 String actualJson = "";
 JsonGenerator gen = null;
 SerializerProvider ctxt = null;
 nullNode.serialize(gen, ctxt);
 }

 /*
 * Testing getPojo method
 */
 @Test
 public void testGetPojo() {
 Object expectedPojo = "Hello World";
 Object actualPojo = pojoNode.getPojo();
 assertEquals(expectedPojo, actualPojo);
 }

 /*
 * Testing getPojo method when POJONode instance is null
 */
 @Test(expected = NullPointerException.class)
 public void testGetPojoWithNullPOJONode() {
 POJONode nullNode = null;
 Object actualPojo = nullNode.getPojo();
 }

 /*
 * Testing equals method
 */
 @Test
 public void testEquals() {
 POJONode otherPOJONode = new POJONode("Hello World");
 assertTrue(pojoNode.equals(otherPOJONode));
 assertTrue(otherPOJONode.equals(pojoNode));
 }

 /*
 * Testing equals method with different POJONode instances
 */
 @Test
 public void testEqualsWithDifferentPOJONodes() {
 POJONode otherPOJONode = new POJONode(new Integer(42));
 assertFalse(pojoNode.equals(otherPOJONode));
 assertFalse(otherPOJONode.equals(pojoNode));
 }

 /*
 * Testing equals method with null object
 */
 @Test
 public void testEqualsWithNullObject() {
 assertFalse(pojoNode.equals(null));
 }

 /*
 * Testing hashCode method
 */
 @Test
 public void testHashCode() {
 int expectedHashCode = "Hello World".hashCode();
 int actualHashCode = pojoNode.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 /*
 * Testing toString method
 */
 @Test
 public void testToString() {
 String expectedString = "(value of Hello World)";
 String actualString = pojoNode.toString();
 assertEquals(expectedString, actualString);
 }

 /*
 * Testing toString method when POJONode instance is null
 */
 @Test(expected = NullPointerException.class)
 public void testToStringWithNullPOJONode() {
 POJONode nullNode = null;
 String actualString = nullNode.toString();
 }
}