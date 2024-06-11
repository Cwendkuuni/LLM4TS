import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

  @Test
  public void testSerializeBigDecimal() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    BigDecimal value = new BigDecimal("123.45");
    NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123.45", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeBigInteger() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    BigInteger value = new BigInteger("1234567890");
    NumberSerializer serializer = new NumberSerializer(BigInteger.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("1234567890", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeInteger() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Integer value = 123;
    NumberSerializer serializer = new NumberSerializer(Integer.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeLong() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Long value = 1234567890L;
    NumberSerializer serializer = new NumberSerializer(Long.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("1234567890", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeDouble() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Double value = 123.45;
    NumberSerializer serializer = new NumberSerializer(Double.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123.45", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeFloat() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Float value = 123.45f;
    NumberSerializer serializer = new NumberSerializer(Float.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123.45", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeByte() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Byte value = 123;
    NumberSerializer serializer = new NumberSerializer(Byte.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123", mockGenerator.getOutput());
  }

  @Test
  public void testSerializeShort() throws IOException {
    JsonGenerator mockGenerator = createMockGenerator();
    SerializerProvider mockProvider = createMockProvider();
    Short value = 123;
    NumberSerializer serializer = new NumberSerializer(Short.class);
    serializer.serialize(value, mockGenerator, mockProvider);
    assertEquals("123", mockGenerator.getOutput());
  }

  private JsonGenerator createMockGenerator() {
    return new JsonGenerator() {
      private StringBuilder output = new StringBuilder();

      @Override
      public void writeNumber(BigDecimal value) throws IOException {
        output.append(value.toString());
      }

      @Override
      public void writeNumber(BigInteger value) throws IOException {
        output.append(value.toString());
      }

      @Override
      public void writeNumber(int value) throws IOException {
        output.append(value);
      }

      @Override
      public void writeNumber(long value) throws IOException {
        output.append(value);
      }

      @Override
      public void writeNumber(double value) throws IOException {
        output.append(value);
      }

      @Override
      public void writeNumber(float value) throws IOException {
        output.append(value);
      }

      @Override
      public void writeString(String value) throws IOException {
        output.append(value);
      }

      public String getOutput() {
        return output.toString();
      }
    };
  }

  private SerializerProvider createMockProvider() {
    return new SerializerProvider() {
      // Implement necessary methods for testing
    };
  }
}