import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

  @Test
  public void testGetStdKeySerializer() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = String.class;
    boolean useDefault = true;
    JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
    assertEquals("The result should be DEFAULT_STRING_SERIALIZER", StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
  }

  @Test
  public void testGetStdKeySerializer_nullRawKeyType() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = null;
    boolean useDefault = true;
    JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
    assertEquals("The result should be an instance of Dynamic", StdKeySerializers.Dynamic.class, result.getClass());
  }

  @Test
  public void testGetStdKeySerializer_notUseDefault() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = String.class;
    boolean useDefault = false;
    JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
    assertNull("The result should be null", result);
  }

  @Test
  public void testGetFallbackKeySerializer_notEnum() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = Integer.class;
    JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
    assertEquals("The result should be DEFAULT_KEY_SERIALIZER", StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
  }

  @Test
  public void testGetFallbackKeySerializer_enum() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = MyEnum.class;
    JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
    assertEquals("The result should be an instance of Default", StdKeySerializers.Default.class, result.getClass());
  }

  @Test
  public void testGetFallbackKeySerializer_nullRawKeyType() {
    SerializationConfig config = new SerializationConfig();
    Class<?> rawKeyType = null;
    JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
    assertEquals("The result should be DEFAULT_KEY_SERIALIZER", StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
  }

  @Test
  public void testGetDefault() {
    JsonSerializer<Object> result = StdKeySerializers.getDefault();
    assertEquals("The result should be DEFAULT_KEY_SERIALIZER", StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
  }

  // Additional tests can be added for Default, Dynamic, and StringKeySerializer classes
}