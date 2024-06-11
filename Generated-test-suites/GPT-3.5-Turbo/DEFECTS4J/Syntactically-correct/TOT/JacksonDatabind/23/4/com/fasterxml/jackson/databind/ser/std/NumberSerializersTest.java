import org.junit.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

  @Test
  public void testAddAll() {
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    NumberSerializers.addAll(allDeserializers);

    assertNotNull(allDeserializers.get(Integer.class.getName()));
    assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
    assertNotNull(allDeserializers.get(Long.class.getName()));
    assertNotNull(allDeserializers.get(Long.TYPE.getName()));
    assertNotNull(allDeserializers.get(Byte.class.getName()));
    assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
    assertNotNull(allDeserializers.get(Short.class.getName()));
    assertNotNull(allDeserializers.get(Short.TYPE.getName()));
    assertNotNull(allDeserializers.get(Float.class.getName()));
    assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    assertNotNull(allDeserializers.get(Double.class.getName()));
    assertNotNull(allDeserializers.get(Double.TYPE.getName()));
  }

  @Test
  public void testShortSerializer() {
    NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Short value = 10;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

  @Test
  public void testIntegerSerializer() {
    NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Integer value = 100;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

  @Test
  public void testIntLikeSerializer() {
    NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Number value = 500;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

  @Test
  public void testLongSerializer() {
    NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Long value = 1000L;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

  @Test
  public void testFloatSerializer() {
    NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Float value = 3.14f;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

  @Test
  public void testDoubleSerializer() {
    NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
    SerializerProvider provider = null;
    JsonGenerator gen = null;

    Double value = 2.71828;
    try {
      serializer.serialize(value, gen, provider);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Add more test cases for edge cases and error scenarios

  }

}