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

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {

  // Clone
  
  @Test
  public void testClone_withNullObject_returnsNull() {
    // Arrange
    Object object = null;
    
    // Act
    Object clonedObject = SerializationUtils.clone(object);
    
    // Assert
    assertNull(clonedObject);
  }
  
  @Test
  public void testClone_withSerializableObject_returnsDeepCopy() {
    // Arrange
    SerializableObject object = new SerializableObject("test");
    
    // Act
    SerializableObject clonedObject = SerializationUtils.clone(object);
    
    // Assert
    assertNotSame(object, clonedObject);
    assertEquals(object.getValue(), clonedObject.getValue());
  }
  
  // Serialize
  
  @Test(expected = IllegalArgumentException.class)
  public void testSerialize_withNullObjectAndNullOutputStream_throwsIllegalArgumentException() {
    // Arrange
    Serializable object = null;
    OutputStream outputStream = null;
    
    // Act
    SerializationUtils.serialize(object, outputStream);
  }
  
  @Test(expected = SerializationException.class)
  public void testSerialize_withObjectAndThrowingOutputStream_throwsSerializationException() {
    // Arrange
    Serializable object = new SerializableObject("test");
    OutputStream outputStream = new ThrowingOutputStream();
    
    // Act
    SerializationUtils.serialize(object, outputStream);
  }
  
  @Test
  public void testSerialize_withSerializableObject_returnsByteArray() {
    // Arrange
    Serializable object = new SerializableObject("test");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    // Act
    SerializationUtils.serialize(object, outputStream);
    byte[] result = outputStream.toByteArray();
    
    // Assert
    assertNotNull(result);
    assertTrue(result.length > 0);
  }
  
  // Deserialize
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeserialize_withNullInputStream_throwsIllegalArgumentException() {
    // Arrange
    InputStream inputStream = null;
    
    // Act
    SerializationUtils.deserialize(inputStream);
  }
  
  @Test(expected = SerializationException.class)
  public void testDeserialize_withThrowingInputStream_throwsSerializationException() {
    // Arrange
    InputStream inputStream = new ThrowingInputStream();
    
    // Act
    SerializationUtils.deserialize(inputStream);
  }
  
  @Test
  public void testDeserialize_withSerializedByteArray_returnsDeserializedObject() {
    // Arrange
    SerializableObject object = new SerializableObject("test");
    byte[] serializedObject = SerializationUtils.serialize(object);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
    
    // Act
    Object result = SerializationUtils.deserialize(inputStream);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof SerializableObject);
    assertEquals(object.getValue(), ((SerializableObject)result).getValue());
  }
  
  // Helper Classes
  
  private class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public SerializableObject(String value) {
      this.value = value;
    }
    
    public String getValue() {
      return value;
    }
  }
  
  private class ThrowingOutputStream extends OutputStream {
    @Override
    public void write(int b) throws IOException {
      throw new IOException("IOException while writing stream");
    }
  }
  
  private class ThrowingInputStream extends InputStream {
    @Override
    public int read() throws IOException {
      throw new IOException("IOException while reading stream");
    }
  }
}