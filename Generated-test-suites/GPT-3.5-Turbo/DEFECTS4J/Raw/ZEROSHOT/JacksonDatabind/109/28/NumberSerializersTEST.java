package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

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
    assertNotNull(allDeserializers.get(Double.class.getName()));
    assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    assertNotNull(allDeserializers.get(Float.class.getName()));
    assertNotNull(allDeserializers.get(Float.TYPE.getName()));
  }

  @Test
  public void testShortSerializer() {
    ShortSerializer serializer = new ShortSerializer();
    assertEquals(Short.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.INT, serializer._numberType);
    assertEquals("number", serializer._schemaType);

    Short value = 10;
    assertEquals("10", serializer.serialize(value));

    value = -10;
    assertEquals("-10", serializer.serialize(value));
  }

  @Test
  public void testIntegerSerializer() {
    IntegerSerializer serializer = new IntegerSerializer(Integer.class);
    assertEquals(Integer.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.INT, serializer._numberType);
    assertEquals("integer", serializer._schemaType);

    Integer value = 10;
    assertEquals("10", serializer.serialize(value));

    value = -10;
    assertEquals("-10", serializer.serialize(value));
  }

  @Test
  public void testIntLikeSerializer() {
    IntLikeSerializer serializer = new IntLikeSerializer();
    assertEquals(Number.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.INT, serializer._numberType);
    assertEquals("integer", serializer._schemaType);

    Number value = 10;
    assertEquals("10", serializer.serialize(value));

    value = -10;
    assertEquals("-10", serializer.serialize(value));
  }

  @Test
  public void testLongSerializer() {
    LongSerializer serializer = new LongSerializer(Long.class);
    assertEquals(Long.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
    assertEquals("number", serializer._schemaType);

    Long value = 10000000000L;
    assertEquals("10000000000", serializer.serialize(value));

    value = -10000000000L;
    assertEquals("-10000000000", serializer.serialize(value));
  }

  @Test
  public void testFloatSerializer() {
    FloatSerializer serializer = new FloatSerializer();
    assertEquals(Float.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
    assertEquals("number", serializer._schemaType);

    Float value = 1.234f;
    assertEquals("1.234", serializer.serialize(value));

    value = -1.234f;
    assertEquals("-1.234", serializer.serialize(value));
  }

  @Test
  public void testDoubleSerializer() {
    DoubleSerializer serializer = new DoubleSerializer(Double.class);
    assertEquals(Double.class, serializer.handledType());
    assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
    assertEquals("number", serializer._schemaType);

    Double value = 1.23456789;
    assertEquals("1.23456789", serializer.serialize(value));

    value = -1.23456789;
    assertEquals("-1.23456789", serializer.serialize(value));
  }
}