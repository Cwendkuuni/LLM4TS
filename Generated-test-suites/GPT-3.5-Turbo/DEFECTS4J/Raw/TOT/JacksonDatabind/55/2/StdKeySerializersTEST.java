import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

  @Test
  public void testGetStdKeySerializer() {
    // Test case 1: rawKeyType is null
    assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));

    // Test case 2: rawKeyType is Object.class
    assertTrue(StdKeySerializers.getStdKeySerializer(null, Object.class, false)
        instanceof StdKeySerializers.Dynamic);

    // Test case 3: rawKeyType is String.class
    assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER,
        StdKeySerializers.getStdKeySerializer(null, String.class, false));

    // Test case 4: rawKeyType is primitive
    assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
        StdKeySerializers.getStdKeySerializer(null, int.class, false));

    // Test case 5: rawKeyType is assignable from Number
    assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
        StdKeySerializers.getStdKeySerializer(null, Double.class, false));

    // Test case 6: rawKeyType is Class.class
    assertTrue(StdKeySerializers.getStdKeySerializer(null, Class.class, false)
        instanceof StdKeySerializers.Default);

    // Test case 7: rawKeyType is assignable from Date
    assertTrue(StdKeySerializers.getStdKeySerializer(null, java.sql.Date.class, false)
        instanceof StdKeySerializers.Default);

    // Test case 8: rawKeyType is assignable from Calendar
    assertTrue(
        StdKeySerializers.getStdKeySerializer(null, java.util.GregorianCalendar.class, false)
            instanceof StdKeySerializers.Default);

    // Test case 9: rawKeyType is java.util.UUID.class
    assertTrue(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false)
        instanceof StdKeySerializers.Default);

    // Test case 10: useDefault is true
    assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));

    // Test case 11: useDefault is false
    assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
  }

  @Test
  public void testGetFallbackKeySerializer() {
    // Test case 1: rawKeyType is null
    assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
        StdKeySerializers.getFallbackKeySerializer(null, null));

    // Test case 2: rawKeyType is Enum.class
    assertTrue(StdKeySerializers.getFallbackKeySerializer(null, Enum.class)
        instanceof StdKeySerializers.Dynamic);

    // Test case 3: rawKeyType is Enum
    assertTrue(StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class)
        instanceof StdKeySerializers.Default);
  }

  @Test
  public void testGetDefault() {
    // Test case 1
    assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
  }

  @Test
  public void testDefaultSerialize() throws IOException {
    // Test case 1: _typeId = TYPE_DATE
    StdKeySerializers.Default defaultSerializer =
        new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, java.util.Date.class);
    Date date = new Date();
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
    defaultSerializer.serialize(date, jsonGenerator, serializerProvider);

    // Test case 2: _typeId = TYPE_CALENDAR
    defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR,
        java.util.Calendar.class);
    Calendar calendar = Calendar.getInstance();
    defaultSerializer.serialize(calendar, jsonGenerator, serializerProvider);

    // Test case 3: _typeId = TYPE_CLASS
    defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS,
        java.lang.Class.class);
    Class<?> clazz = Integer.class;
    defaultSerializer.serialize(clazz, jsonGenerator, serializerProvider);

    // Test case 4: _typeId = TYPE_ENUM
    defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM,
        TestEnum.class);
    TestEnum testEnum = TestEnum.A;
    defaultSerializer.serialize(testEnum, jsonGenerator, serializerProvider);

    // Test case 5: _typeId = TYPE_TO_STRING
    defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING,
        java.util.UUID.class);
    java.util.UUID uuid = java.util.UUID.randomUUID();
    defaultSerializer.serialize(uuid, jsonGenerator, serializerProvider);
  }

  @Test
  public void testDynamicSerialize() throws IOException {
    // Test case 1
    StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
    String value = "test";
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
    dynamicSerializer.serialize(value, jsonGenerator, serializerProvider);
  }

  @Test
  public void testStringKeySerialize() throws IOException {
    // Test case 1
    StdKeySerializers.StringKeySerializer stringKeySerializer =
        new StdKeySerializers.StringKeySerializer();
    String value = "test";
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
    stringKeySerializer.serialize(value, jsonGenerator, serializerProvider);
  }
  
  public enum TestEnum {
    A, B, C;
  }
}