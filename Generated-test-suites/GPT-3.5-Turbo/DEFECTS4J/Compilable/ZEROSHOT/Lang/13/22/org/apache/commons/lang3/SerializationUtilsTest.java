package org.apache.commons.lang3;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationException;
import org.junit.Before;
import org.junit.Test;

public class SerializationUtilsTest {
  
  @Test
  public void testClone() {
    // Given
    String object = "Hello World";
    
    // When
    String clonedObject = SerializationUtils.clone(object);
    
    // Then
    assertEquals(object, clonedObject);
  }
  
  @Test
  public void testSerialize() {
    // Given
    String object = "Hello World";
    
    // When
    byte[] serializedObject = SerializationUtils.serialize(object);
    
    // Then
    assertNotNull(serializedObject);
  }
  
  @Test
  public void testDeserialize() {
    // Given
    byte[] serializedObject = "Hello World".getBytes();
    
    // When
    Object deserializedObject = SerializationUtils.deserialize(serializedObject);
    
    // Then
    assertEquals("Hello World", deserializedObject);
  }
}