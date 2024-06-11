
import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

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

public class SerializationUtilsTest {

  // Test clone method
  @Test
  public void testClone() {
    String str = "Hello World";
    String cloneStr = SerializationUtils.clone(str);
    assertEquals(str, cloneStr);
  }

  // Test serialize method with valid object and output stream
  @Test
  public void testSerializeWithValidObjectAndOutputStream() {
    String str = "Hello World";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    SerializationUtils.serialize(str, baos);
    byte[] serializedData = baos.toByteArray();
    assertNotNull(serializedData);
  }

  // Test serialize method with null object
  @Test(expected = IllegalArgumentException.class)
  public void testSerializeWithNullObject() {
    OutputStream outputStream = new ByteArrayOutputStream();
    SerializationUtils.serialize(null, outputStream);
  }

  // Test serialize method with null output stream
  @Test(expected = IllegalArgumentException.class)
  public void testSerializeWithNullOutputStream() {
    String str = "Hello World";
    SerializationUtils.serialize(str, null);
  }

  // Test serialize method with invalid output stream
  @Test(expected = SerializationException.class)
  public void testSerializeWithInvalidOutputStream() {
    String str = "Hello World";
    OutputStream outputStream = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        throw new IOException("Error writing to output stream");
      }
    };
    SerializationUtils.serialize(str, outputStream);
  }

  // Test deserialize method with valid input stream
  @Test
  public void testDeserializeWithValidInputStream() {
    String str = "Hello World";
    byte[] serializedData = SerializationUtils.serialize(str);
    ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
    Object deserializedObject = SerializationUtils.deserialize(bais);
    assertTrue(deserializedObject instanceof String);
    assertEquals(str, deserializedObject);
  }

  // Test deserialize method with null input stream
  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeWithNullInputStream() {
    SerializationUtils.deserialize(null);
  }

  // Test deserialize method with invalid input stream
  @Test(expected = SerializationException.class)
  public void testDeserializeWithInvalidInputStream() {
    InputStream inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("Error reading from input stream");
      }
    };
    SerializationUtils.deserialize(inputStream);
  }
}

