package com.example.pojonode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

 private ObjectMapper objectMapper;
 private POJONode pojoNode;
 private SerializerProvider serializerProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 serializerProvider = mock(SerializerProvider.class);
 pojoNode = new POJONode(new Object());
 }

 @Test
 public void testGetNodeType() {
 JsonNodeType expectedNodeType = JsonNodeType.POJO;
 JsonNodeType actualNodeType = pojoNode.getNodeType();
 assertEquals(expectedNodeType, actualNodeType);
 }

 @Test
 public void testAsToken() {
 JsonToken expectedToken = JsonToken.VALUE_EMBEDDED_OBJECT;
 JsonToken actualToken = pojoNode.asToken();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testBinaryValue() throws IOException {
 Object inputObject = new Object();
 byte[] expectedBinaryData = new byte[0];
 byte[] actualBinaryData = pojoNode.binaryValue();
 assertNull(actualBinaryData);

 pojoNode = new POJONode(expectedBinaryData);
 actualBinaryData = pojoNode.binaryValue();
 assertArrayEquals(expectedBinaryData, actualBinaryData);

 pojoNode = new POJONode(inputObject);
 actualBinaryData = pojoNode.binaryValue();
 assertNull(actualBinaryData);
 }

 @Test
 public void testAsText() {
 String expectedText = "null";
 String actualText = pojoNode.asText();
 assertEquals(expectedText, actualText);

 Object inputObject = new Object() {
 @Override
 public String toString() {
 return "Test Object";
 }
 };
 pojoNode = new POJONode(inputObject);
 actualText = pojoNode.asText();
 assertEquals("Test Object", actualText);
 }

 @Test
 public void testAsTextWithDefaultValue() {
 String expectedDefaultValue = "default value";
 String actualText = pojoNode.asText("default value");
 assertEquals(expectedDefaultValue, actualText);
 }

 @Test
 public void testAsBoolean() {
 Boolean expectedBoolean = Boolean.FALSE;
 boolean actualBoolean = pojoNode.asBoolean(Boolean.FALSE);
 assertEquals(expectedBoolean, actualBoolean);

 Object inputObject = new Object() {
 @Override
 public boolean booleanValue() {
 return true;
 }
 };
 pojoNode = new POJONode(inputObject);
 actualBoolean = pojoNode.asBoolean(Boolean.FALSE);
 assertTrue(actualBoolean);
 }

 @Test
 public void testAsInt() {
 Integer expectedInt = Integer.MIN_VALUE;
 int actualInt = pojoNode.asInt(Integer.MIN_VALUE);
 assertEquals(expectedInt, actualInt);

 Object inputObject = new Object() {
 @Override
 public int intValue() {
 return 42;
 }
 };
 pojoNode = new POJONode(inputObject);
 actualInt = pojoNode.asInt(Integer.MIN_VALUE);
 assertEquals(Integer.MIN_VALUE, actualInt);
 assertSame(inputObject, pojoNode.getPojo());
 }

 @Test
 public void testAsLong() {
 Long expectedLong = Long.MIN_VALUE;
 long actualLong = pojoNode.asLong(Long.MIN_VALUE);
 assertEquals(expectedLong, actualLong);

 Object inputObject = new Object() {
 @Override
 public long longValue() {
 return 42L;
 }
 };
 pojoNode = new POJONode(inputObject);
 actualLong = pojoNode.asLong(Long.MIN_VALUE);
 assertEquals(Long.MIN_VALUE, actualLong);
 assertSame(inputObject, pojoNode.getPojo());
 }

 @Test
 public void testAsDouble() {
 Double expectedDouble = Double.MIN_VALUE;
 double actualDouble = pojoNode.asDouble(Double.MIN_VALUE);
 assertEquals(expectedDouble, actualDouble);

 Object inputObject = new Object() {
 @Override
 public double doubleValue() {
 return 42.0;
 }
 };
 pojoNode = new POJONode(inputObject);
 actualDouble = pojoNode.asDouble(Double.MIN_VALUE);
 assertEquals(Double.MIN_VALUE, actualDouble);
 assertSame(inputObject, pojoNode.getPojo());
 }

 @Test(expected = IOException.class)
 public void testSerializeThrowsIOExceptionWhenValueIsNull() throws IOException {
 when(serializerProvider.defaultSerializeNull(any(JsonGenerator.class))).thenThrow(IOException.class);
 pojoNode = new POJONode(null);
 pojoNode.serialize(mock(JsonGenerator.class), serializerProvider);
 }

 @Test
 public void testSerializeWhenValueIsJsonSerializable() {
 Object inputObject = mock(JsonSerializable.class);
 when(inputObject.serialize(any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);
 pojoNode = new POJONode(inputObject);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 serializerProvider.defaultSerializeNull(jsonGenerator);
 pojoNode.serialize(jsonGenerator, serializerProvider);
 verify(jsonGenerator).writeObject(inputObject);
 }

 @Test
 public void testSerializeWhenValueIsNotJsonSerializable() {
 Object inputObject = new Object();
 pojoNode = new POJONode(inputObject);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 pojoNode.serialize(jsonGenerator, serializerProvider);
 verify(jsonGenerator).writeObject(inputObject);
 }

 @Test
 public void testGetPojo() {
 Object expectedObject = new Object();
 Object actualObject = pojoNode.getPojo();
 assertSame(expectedObject, actualObject);
 }

 @Test
 public void testEquals() {
 Object inputObject1 = new Object();
 Object inputObject2 = new Object();
 POJONode pojoNode1 = new POJONode(inputObject1);
 POJONode pojoNode2 = new POJONode(inputObject2);

 assertTrue(pojoNode1.equals(pojoNode1));
 assertTrue(pojoNode1.equals(pojoNode1.getPojo()));
 assertFalse(pojoNode1.equals(pojoNode2));
 assertFalse(pojoNode1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 Object inputObject = new Object();
 POJONode pojoNode = new POJONode(inputObject);
 int expectedHashcode = inputObject.hashCode();
 int actualHashcode = pojoNode.hashCode();
 assertEquals(expectedHashcode, actualHashcode);
 }

 @Test
 public void testToString() {
 String expectedString = "Test Object";
 String actualString = pojoNode.toString();
 assertEquals(expectedString, actualString);
 }
}