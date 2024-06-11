import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

  @Test
  public void testSerialize_BigDecimal() throws IOException {
    NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    BigDecimal value = new BigDecimal("12345.67890");
    
    // Test serialization of a positive BigDecimal value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
    
    // Test serialization of a negative BigDecimal value
    value = new BigDecimal("-12345.67890");
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
    
    // Test serialization of a zero BigDecimal value
    value = BigDecimal.ZERO;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
  }

  @Test
  public void testSerialize_BigInteger() throws IOException {
    NumberSerializer serializer = new NumberSerializer(BigInteger.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    BigInteger value = new BigInteger("1234567890");
    
    // Test serialization of a positive BigInteger value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
    
    // Test serialization of a negative BigInteger value
    value = new BigInteger("-1234567890");
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
    
    // Test serialization of a zero BigInteger value
    value = BigInteger.ZERO;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value, g.getValue());
  }

  @Test
  public void testSerialize_Integer() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Integer.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Integer value = 123456;
    
    // Test serialization of a positive Integer value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of a negative Integer value
    value = -123456;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of zero
    value = 0;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
  }

  @Test
  public void testSerialize_Long() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Long.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Long value = 1234567890L;
    
    // Test serialization of a positive Long value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.longValue(), g.getValue());
    
    // Test serialization of a negative Long value
    value = -1234567890L;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.longValue(), g.getValue());
    
    // Test serialization of zero
    value = 0L;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.longValue(), g.getValue());
  }

  @Test
  public void testSerialize_Double() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Double.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Double value = 12345.67890;
    
    // Test serialization of a positive Double value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.doubleValue(), g.getValue());
    
    // Test serialization of a negative Double value
    value = -12345.67890;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.doubleValue(), g.getValue());
    
    // Test serialization of zero
    value = 0.0;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.doubleValue(), g.getValue());
  }

  @Test
  public void testSerialize_Float() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Float.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Float value = 12345.67890f;
    
    // Test serialization of a positive Float value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.floatValue(), g.getValue());
    
    // Test serialization of a negative Float value
    value = -12345.67890f;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.floatValue(), g.getValue());
    
    // Test serialization of zero
    value = 0.0f;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.floatValue(), g.getValue());
  }

  @Test
  public void testSerialize_Byte() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Byte.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Byte value = 123;
    
    // Test serialization of a positive Byte value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of a negative Byte value
    value = -123;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of zero
    value = 0;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
  }

  @Test
  public void testSerialize_Short() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Short.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Short value = 1234;
    
    // Test serialization of a positive Short value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of a negative Short value
    value = -1234;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
    
    // Test serialization of zero
    value = 0;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.intValue(), g.getValue());
  }

  @Test
  public void testSerialize_OtherNumber() throws IOException {
    NumberSerializer serializer = new NumberSerializer(Number.class);
    JsonGenerator g = null;  // Set up a dummy JsonGenerator
    SerializerProvider provider = null;  // Set up a dummy SerializerProvider
    Number value = 12345.67890;
    
    // Test serialization of a positive Number value
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.toString(), g.getValue());
    
    // Test serialization of a negative Number value
    value = -12345.67890;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.toString(), g.getValue());
    
    // Test serialization of zero
    value = 0;
    g = new DummyJsonGenerator();
    serializer.serialize(value, g, provider);
    assertEquals(value.toString(), g.getValue());
  }

  // Dummy JsonGenerator implementation for testing
  private class DummyJsonGenerator extends JsonGenerator {
    private Object value;
    
    public Object getValue() {
        return value;
    }
    
    // Other method implementations are not relevant for this test
    // and can be left empty or thrown as UnsupportedOperationException
  }

}