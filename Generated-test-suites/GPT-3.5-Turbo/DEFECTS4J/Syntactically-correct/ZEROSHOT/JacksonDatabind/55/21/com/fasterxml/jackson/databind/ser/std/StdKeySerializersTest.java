import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {

  private final SerializationConfig config = null;

  @Test
  public void testGetStdKeySerializer() {
    Class<?> rawKeyType = null;
    boolean useDefault = false;
    JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
    assertNotNull(result);
  }

  @Test
  public void testGetFallbackKeySerializer() {
    Class<?> rawKeyType = null;
    JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
    assertNotNull(result);
  }

  @Test
  public void testGetDefault() {
    JsonSerializer<Object> result = StdKeySerializers.getDefault();
    assertNotNull(result);
  }

  @Test
  public void testDefaultSerializeMethodShouldSerializeDateKey() throws Exception {
    Default defaultSerializer = new Default(Default.TYPE_DATE, Date.class);
    JsonGenerator g = null;
    SerializerProvider provider = null;
    defaultSerializer.serialize(new Date(), g, provider);
    //assertion code here
  }

  @Test
  public void testDefaultSerializeMethodShouldSerializeCalendarKey() throws Exception {
    Calendar calendar = Calendar.getInstance();
    Default defaultSerializer = new Default(Default.TYPE_CALENDAR, Calendar.class);
    JsonGenerator g = null;
    SerializerProvider provider = null;
    defaultSerializer.serialize(calendar, g, provider);
    //assertion code here
  }

  @Test
  public void testDefaultSerializeMethodShouldSerializeClassKey() throws Exception {
    Default defaultSerializer = new Default(Default.TYPE_CLASS, Class.class);
    JsonGenerator g = null;
    SerializerProvider provider = null;
    defaultSerializer.serialize(String.class, g, provider);
    //assertion code here
  }

  @Test
  public void testDefaultSerializeMethodShouldSerializeEnumKey() throws Exception {
    Default defaultSerializer = new Default(Default.TYPE_ENUM, Enum.class);
    JsonGenerator g = null;
    SerializerProvider provider = null;
    defaultSerializer.serialize(Enum.ONE, g, provider);
    //assertion code here
  }

  @Test
  public void testDefaultSerializeMethodShouldSerializeToStringKey() throws Exception {
    Default defaultSerializer = new Default(Default.TYPE_TO_STRING, java.util.UUID.class);
    JsonGenerator g = null;
    SerializerProvider provider = null;
    defaultSerializer.serialize(java.util.UUID.randomUUID(), g, provider);
    //assertion code here
  }

  @Test
  public void testDynamicSerializeMethodShouldSerialize() throws Exception {
    Dynamic dynamicSerializer = new Dynamic();
    JsonGenerator g = null;
    SerializerProvider provider = null;
    dynamicSerializer.serialize("key", g, provider);
    //assertion code here
  }

  @Test
  public void testStringKeySerializeMethodShouldWriteFieldName() throws Exception {
    StringKeySerializer stringKeySerializer = new StringKeySerializer();
    JsonGenerator g = null;
    SerializerProvider provider = null;
    stringKeySerializer.serialize("key", g, provider);
    //assertion code here
  }

}