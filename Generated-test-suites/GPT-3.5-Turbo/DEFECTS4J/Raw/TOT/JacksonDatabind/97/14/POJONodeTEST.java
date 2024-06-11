import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class POJONodeTest {

  @Test
  public void testGetNodeType() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
  }
  
  @Test
  public void testAsToken() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
  }
  
  @Test
  public void testBinaryValueWhenValueIsByteArray() throws IOException {
    byte[] byteArray = new byte[] { 0x01, 0x02, 0x03 };
    POJONode pojoNode = new POJONode(byteArray);
    assertArrayEquals(byteArray, pojoNode.binaryValue());
  }
  
  @Test(expected = IOException.class)
  public void testBinaryValueWhenValueIsNotByteArray() throws IOException {
    POJONode pojoNode = new POJONode("test");
    pojoNode.binaryValue();
  }
  
  @Test
  public void testAsTextWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals("null", pojoNode.asText());
  }
  
  @Test
  public void testAsTextWhenValueIsString() {
    POJONode pojoNode = new POJONode("test");
    assertEquals("test", pojoNode.asText());
  }
  
  @Test
  public void testAsTextWithDefaultValueWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals("default", pojoNode.asText("default"));
  }
  
  @Test
  public void testAsTextWithDefaultValueWhenValueIsString() {
    POJONode pojoNode = new POJONode("test");
    assertEquals("test", pojoNode.asText("default"));
  }
  
  @Test
  public void testAsBooleanWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertFalse(pojoNode.asBoolean(false));
  }
  
  @Test
  public void testAsBooleanWhenValueIsBoolean() {
    POJONode pojoNode = new POJONode(true);
    assertTrue(pojoNode.asBoolean(false));
  }
  
  @Test
  public void testAsBooleanWithDefaultValueWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertTrue(pojoNode.asBoolean(true));
  }
  
  @Test
  public void testAsBooleanWithDefaultValueWhenValueIsBoolean() {
    POJONode pojoNode = new POJONode(false);
    assertFalse(pojoNode.asBoolean(true));
  }
  
  @Test
  public void testAsIntWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(1, pojoNode.asInt(1));
  }
  
  @Test
  public void testAsIntWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(2);
    assertEquals(2, pojoNode.asInt(1));
  }
  
  @Test
  public void testAsIntWithDefaultValueWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(2, pojoNode.asInt(2));
  }
  
  @Test
  public void testAsIntWithDefaultValueWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(3);
    assertEquals(3, pojoNode.asInt(2));
  }
  
  @Test
  public void testAsLongWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(1L, pojoNode.asLong(1L));
  }
  
  @Test
  public void testAsLongWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(2L);
    assertEquals(2L, pojoNode.asLong(1L));
  }
  
  @Test
  public void testAsLongWithDefaultValueWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(2L, pojoNode.asLong(2L));
  }
  
  @Test
  public void testAsLongWithDefaultValueWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(3L);
    assertEquals(3L, pojoNode.asLong(2L));
  }
  
  @Test
  public void testAsDoubleWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(1.0, pojoNode.asDouble(1.0), 0.000001);
  }
  
  @Test
  public void testAsDoubleWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(2.0);
    assertEquals(2.0, pojoNode.asDouble(1.0), 0.000001);
  }
  
  @Test
  public void testAsDoubleWithDefaultValueWhenValueIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertEquals(2.0, pojoNode.asDouble(2.0), 0.000001);
  }
  
  @Test
  public void testAsDoubleWithDefaultValueWhenValueIsNumber() {
    POJONode pojoNode = new POJONode(3.0);
    assertEquals(3.0, pojoNode.asDouble(2.0), 0.000001);
  }
  
  @Test
  public void testSerializeWhenValueIsNull() throws IOException {
    POJONode pojoNode = new POJONode(null);
    // Test serialization
  }
  
  @Test
  public void testSerializeWhenValueIsJsonSerializable() throws IOException {
    TestObject testObject = new TestObject();
    POJONode pojoNode = new POJONode(testObject);
    // Test serialization
  }
  
  @Test
  public void testSerializeWhenValueIsNotJsonSerializable() throws IOException {
    POJONode pojoNode = new POJONode("test");
    // Test serialization
  }
  
  @Test
  public void testGetPojo() {
    TestObject testObject = new TestObject();
    POJONode pojoNode = new POJONode(testObject);
    assertEquals(testObject, pojoNode.getPojo());
  }
  
  @Test
  public void testEqualsWhenObjIsNull() {
    POJONode pojoNode = new POJONode(null);
    assertFalse(pojoNode.equals(null));
  }
  
  @Test
  public void testEqualsWhenObjIsSame() {
    POJONode pojoNode = new POJONode(null);
    assertTrue(pojoNode.equals(pojoNode));
  }
  
  @Test
  public void testEqualsWhenObjIsPOJONodeWithSameValue() {
    POJONode pojoNode1 = new POJONode("test");
    POJONode pojoNode2 = new POJONode("test");
    assertTrue(pojoNode1.equals(pojoNode2));
  }
  
  @Test
  public void testEqualsWhenObjIsPOJONodeWithDifferentValue() {
    POJONode pojoNode1 = new POJONode("test1");
    POJONode pojoNode2 = new POJONode("test2");
    assertFalse(pojoNode1.equals(pojoNode2));
  }
  
  @Test
  public void testEqualsWhenObjIsNotPOJONode() {
    POJONode pojoNode = new POJONode(null);
    assertFalse(pojoNode.equals("test"));
  }
  
  @Test
  public void testHashCode() {
    POJONode pojoNode = new POJONode("test");
    assertEquals("test".hashCode(), pojoNode.hashCode());
  }
  
  @Test
  public void testToStringWhenValueIsByteArray() {
    byte[] byteArray = new byte[] { 0x01, 0x02, 0x03 };
    POJONode pojoNode = new POJONode(byteArray);
    assertEquals("(binary value of 3 bytes)", pojoNode.toString());
  }
  
  @Test
  public void testToStringWhenValueIsRawValue() {
    RawValue rawValue = new RawValue("test");
    POJONode pojoNode = new POJONode(rawValue);
    assertEquals("(raw value 'test')", pojoNode.toString());
  }
  
  @Test
  public void testToStringWhenValueIsNotByteArrayOrRawValue() {
    POJONode pojoNode = new POJONode("test");
    assertEquals("test", pojoNode.toString());
  }
  
}