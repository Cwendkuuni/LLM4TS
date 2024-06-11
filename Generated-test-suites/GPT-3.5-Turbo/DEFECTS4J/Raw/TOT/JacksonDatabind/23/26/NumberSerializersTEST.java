import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {
  
  @Test
  public void testShortSerializer_SerializeShort_Success() {
    ShortSerializer serializer = new ShortSerializer();
    Short value = 10;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.shortValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testIntegerSerializer_SerializeInteger_Success() {
    IntegerSerializer serializer = new IntegerSerializer();
    Integer value = 100;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.intValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testIntLikeSerializer_SerializeNumber_Success() {
    IntLikeSerializer serializer = new IntLikeSerializer();
    Number value = 100.5;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.intValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testLongSerializer_SerializeLong_Success() {
    LongSerializer serializer = new LongSerializer();
    Long value = 100000L;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.longValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testFloatSerializer_SerializeFloat_Success() {
    FloatSerializer serializer = new FloatSerializer();
    Float value = 10.5f;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.floatValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testDoubleSerializer_SerializeDouble_Success() {
    DoubleSerializer serializer = new DoubleSerializer();
    Double value = 10.55;
    JsonGenerator gen = mock(JsonGenerator.class);
    SerializerProvider provider = mock(SerializerProvider.class);
    
    try {
      serializer.serialize(value, gen, provider);
      
      // Verify the generated JSON matches the expected value
      verify(gen).writeNumber(value.doubleValue());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
}