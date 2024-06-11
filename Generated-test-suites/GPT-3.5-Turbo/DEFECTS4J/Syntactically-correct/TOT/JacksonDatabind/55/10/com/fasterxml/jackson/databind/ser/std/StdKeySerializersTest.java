import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeySerializersTest {

   @Test
   public void testGetStdKeySerializer_NullRawKeyType_ReturnsDynamicSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = null;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertTrue(result instanceof StdKeySerializers.Dynamic);
   }

   @Test
   public void testGetStdKeySerializer_StringRawKeyType_ReturnsDefaultStringSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = String.class;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertTrue(result instanceof StdKeySerializers.Default);
       StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
       assertEquals(StdKeySerializers.Default.TYPE_STRING, defaultSerializer.getTypeId());
   }

   @Test
   public void testGetStdKeySerializer_PrimitiveRawKeyType_ReturnsDefaultKeySerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = int.class;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
   }

   @Test
   public void testGetStdKeySerializer_DateRawKeyType_ReturnsDefaultDateSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = java.util.Date.class;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertTrue(result instanceof StdKeySerializers.Default);
       StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
       assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultSerializer.getTypeId());
   }

   @Test
   public void testGetStdKeySerializer_CalendarRawKeyType_ReturnsDefaultCalendarSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = java.util.Calendar.class;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertTrue(result instanceof StdKeySerializers.Default);
       StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
       assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, defaultSerializer.getTypeId());
   }

   @Test
   public void testGetStdKeySerializer_ClassRawKeyType_ReturnsDefaultClassSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = java.lang.Class.class;
       boolean useDefault = true;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertTrue(result instanceof StdKeySerializers.Default);
       StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
       assertEquals(StdKeySerializers.Default.TYPE_CLASS, defaultSerializer.getTypeId());
   }

   @Test
   public void testGetStdKeySerializer_UuidRawKeyTypeAndUseDefaultFalse_ReturnsNull() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = java.util.UUID.class;
       boolean useDefault = false;

       JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

       assertNull(result);
   }

   @Test
   public void testGetFallbackKeySerializer_EnumRawKeyType_ReturnsDynamicSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = Enum.class;

       JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

       assertTrue(result instanceof StdKeySerializers.Dynamic);
   }

   @Test
   public void testGetFallbackKeySerializer_EnumRawKeyType_ReturnsDefaultEnumSerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = MyEnum.class;

       JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

       assertTrue(result instanceof StdKeySerializers.Default);
       StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
       assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer.getTypeId());
   }

   @Test
   public void testGetFallbackKeySerializer_RawKeyTypeNull_ReturnsDefaultKeySerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = null;

       JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

       assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
   }

   @Test
   public void testGetFallbackKeySerializer_RawKeyTypeNotEnum_ReturnsDefaultKeySerializer() {
       SerializationConfig config = new SerializationConfig();
       Class<?> rawKeyType = String.class;

       JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

       assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
   }

   @Test
   public void testGetDefault_ReturnsDefaultKeySerializer() {
       JsonSerializer<Object> result = StdKeySerializers.getDefault();

       assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
   }

   @Test
   public void testDefaultSerializeDateKey_ShouldSerializeDateAsKey() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Date date = new Date();

       StdKeySerializers.Default.DateProvider.INSTANCE.defaultSerializeDateKey(date, g);

       // Verify that the date is serialized correctly
       assertEquals(g.getCurrentToken(), JsonToken.VALUE_STRING);
       assertEquals(g.getText(), date.toString());
   }

   @Test
   public void testSerialize_CalendarType_ShouldSerializeCalendarAsKey() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Calendar calendar = Calendar.getInstance();

       StdKeySerializers.Default.CalendarProvider.INSTANCE.serialize(calendar, g, provider);

       // Verify that the calendar is serialized correctly
       assertEquals(g.getCurrentToken(), JsonToken.VALUE_NUMBER_INT);
       assertEquals(g.getLongValue(), calendar.getTimeInMillis());
   }

   @Test
   public void testSerialize_ClassType_ShouldSerializeClassAsKey() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Class<?> c = String.class;

       StdKeySerializers.Default.ClassProvider.INSTANCE.serialize(c, g, provider);

       // Verify that the class is serialized correctly
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), c.getName());
   }

   @Test
   public void testSerialize_EnumTypeWithWriteEnumsUsingToStringEnabled_ShouldSerializeEnumUsingToString() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       MyEnum myEnum = MyEnum.VALUE;

       provider.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
       StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
       defaultSerializer.serialize(myEnum, g, provider);

       // Verify that the enum is serialized using toString
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), myEnum.toString());
   }

   @Test
   public void testSerialize_EnumTypeWithWriteEnumsUsingToStringDisabled_ShouldSerializeEnumUsingName() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       MyEnum myEnum = MyEnum.VALUE;

       provider.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
       StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
       defaultSerializer.serialize(myEnum, g, provider);

       // Verify that the enum is serialized using name
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), myEnum.name());
   }

   @Test
   public void testSerialize_ObjectType_ShouldSerializeObjectUsingToString() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Object obj = new Object();

       StdKeySerializers.Default.ObjectProvider.INSTANCE.serialize(obj, g, provider);

       // Verify that the object is serialized using toString
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), obj.toString());
   }

   @Test
   public void testDynamicSerialize_ShouldSerializeKeyUsingDynamicSerializer() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Object key = "key";

       StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
       dynamicSerializer.serialize(key, g, provider);

       // Verify that the key is serialized correctly
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), (String) key);
   }

   @Test
   public void testDynamicSerialize_ShouldSerializeKeyUsingDynamicSerializerAndCaching() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       Object key1 = "key1";
       Object key2 = "key2";

       StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
       dynamicSerializer.serialize(key1, g, provider);

       // Verify that the key is serialized correctly the first time
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), (String) key1);
       g.clearCurrentToken();

       dynamicSerializer.serialize(key2, g, provider);

       // Verify that the key is serialized correctly the second time by the cached serializer
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), (String) key2);
   }

   @Test
   public void testStringKeySerialize_ShouldSerializeKeyAsString() throws IOException {
       JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
       SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
       String key = "key";

       StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
       stringKeySerializer.serialize(key, g, provider);

       // Verify that the key is serialized correctly
       assertEquals(g.getCurrentToken(), JsonToken.FIELD_NAME);
       assertEquals(g.getCurrentName(), key);
   }

   public enum MyEnum {
       VALUE
   }

}