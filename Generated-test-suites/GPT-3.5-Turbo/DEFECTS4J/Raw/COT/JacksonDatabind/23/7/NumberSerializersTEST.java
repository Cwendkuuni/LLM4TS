
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;


public class NumberSerializersTest {

  @Test
  public void testAddAll() {
    Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    NumberSerializers.addAll(allDeserializers);
    Assert.assertEquals("Integer Serializer", IntegerSerializer.class, allDeserializers.get(Integer.class.getName()));
    Assert.assertEquals("Integer Serializer for primitive", IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()));
    Assert.assertEquals("Long Serializer", LongSerializer.class, allDeserializers.get(Long.class.getName()));
    Assert.assertEquals("Long Serializer for primitive", LongSerializer.class, allDeserializers.get(Long.TYPE.getName()));
    Assert.assertEquals("Byte Serializer", IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()));
    Assert.assertEquals("Byte Serializer for primitive", IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()));
    Assert.assertEquals("Short Serializer", ShortSerializer.class, allDeserializers.get(Short.class.getName()));
    Assert.assertEquals("Short Serializer for primitive", ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()));
    Assert.assertEquals("Float Serializer", FloatSerializer.class, allDeserializers.get(Float.class.getName()));
    Assert.assertEquals("Float Serializer for primitive", FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()));
    Assert.assertEquals("Double Serializer", DoubleSerializer.class, allDeserializers.get(Double.class.getName()));
    Assert.assertEquals("Double Serializer for primitive", DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()));
  }

  @Test
  public void testShortSerializer() {
    ShortSerializer serializer = new ShortSerializer();
    Short value = 123;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.shortValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testIntegerSerializer() {
    IntegerSerializer serializer = new IntegerSerializer();
    Integer value = 123;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.intValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testIntLikeSerializer() {
    IntLikeSerializer serializer = new IntLikeSerializer();
    Number value = 123;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.intValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testLongSerializer() {
    LongSerializer serializer = new LongSerializer();
    Long value = 123L;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.longValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testFloatSerializer() {
    FloatSerializer serializer = new FloatSerializer();
    Float value = 123.45f;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.floatValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testDoubleSerializer() {
    DoubleSerializer serializer = new DoubleSerializer();
    Double value = 123.45;
    JsonGenerator gen = MockJsonGenerator.createGenerator();
    SerializerProvider provider = MockSerializerProvider.createProvider();
    try {
      serializer.serialize(value, gen, provider);
      Assert.assertEquals("Serialized value", String.valueOf(value.doubleValue()), gen.getOutput());
    } catch (IOException e) {
      Assert.fail("Unexpected exception: " + e);
    }
  }
}

class MockJsonGenerator extends JsonGenerator {
  private StringBuilder output = new StringBuilder();

  MockJsonGenerator() {}

  public static MockJsonGenerator createGenerator() {
    return new MockJsonGenerator();
  }

  public String getOutput() {
    return output.toString();
  }

  @Override
  public void writeStartObject() throws IOException {
    output.append("{");
  }

  @Override
  public void writeEndObject() throws IOException {
    output.append("}");
  }

  @Override
  public void writeFieldName(String name) throws IOException {
    output.append(name).append(":");
  }

  @Override
  public void writeString(String text) throws IOException {
    output.append(text);
  }

  @Override
  public void writeNumber(int v) throws IOException {
    output.append(v);
  }

  @Override
  public void close() throws IOException {}

  // Other methods omitted for brevity
}

class MockSerializerProvider extends SerializerProvider {

  MockSerializerProvider() {
    super(null, null, null);
  }

  public static MockSerializerProvider createProvider() {
    return new MockSerializerProvider();
  }

  // Other methods omitted for brevity
}
