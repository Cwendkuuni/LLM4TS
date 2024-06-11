package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
  
  @Test
  public void testClone() {
    // Test cloning a null object
    assertNull(SerializationUtils.clone(null));
    
    // Test cloning an object
    TestObject original = new TestObject("test", 10);
    TestObject clone = SerializationUtils.clone(original);
    assertEquals(original.getName(), clone.getName());
    assertEquals(original.getValue(), clone.getValue());
  }
  
  @Test
  public void testSerialize() {
    // Test serializing a null object
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
      SerializationUtils.serialize(null, baos);
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    
    // Test serializing an object
    TestObject object = new TestObject("test", 10);
    ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
    SerializationUtils.serialize(object, baos);
    // Check if something has been written to the stream
    assertTrue(baos.size() > 0);
  }
  
  @Test
  public void testDeserialize() {
    // Test deserializing a null object
    try {
      SerializationUtils.deserialize(null);
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    
    // Test deserializing an object
    TestObject original = new TestObject("test", 10);
    byte[] serializedObj = SerializationUtils.serialize(original);
    TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(serializedObj);
    assertEquals(original.getName(), deserializedObj.getName());
    assertEquals(original.getValue(), deserializedObj.getValue());
  }
  
  // Helper class for testing serialization
  static class TestObject implements Serializable {
    private String name;
    private int value;
    
    public TestObject(String name, int value) {
      this.name = name;
      this.value = value;
    }
    
    public String getName() {
      return name;
    }
    
    public int getValue() {
      return value;
    }
  }
}