import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

   @Test
   public void testGetStdKeySerializer() {
      // Test when rawKeyType is null
      assertNull(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), null, false));

      // Test when rawKeyType is Object class
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Object.class, true).getClass());

      // Test when rawKeyType is String class
      assertEquals(StdKeySerializers.StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), String.class, true).getClass());

      // Test when rawKeyType is primitive
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), int.class, true).getClass());

      // Test when rawKeyType is Number
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Double.class, true).getClass());

      // Test when rawKeyType is Class
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Class.class, true).getClass());

      // Test when rawKeyType is Date
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Date.class, true).getClass());

      // Test when rawKeyType is Calendar
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Calendar.class, true).getClass());

      // Test when rawKeyType is UUID
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(new SerializationConfig(), java.util.UUID.class, true).getClass());

      // Test when useDefault is false
      assertNull(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), String.class, false));
   }

   @Test
   public void testGetFallbackKeySerializer() {
      // Test when rawKeyType is null
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), null).getClass());

      // Test when rawKeyType is Enum
      assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), Enum.class).getClass());

      // Test when rawKeyType is Enum type
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), MyEnum.class).getClass());

      // Test when rawKeyType is not Enum
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), String.class).getClass());
   }

   @Test
   public void testGetDefault() {
      assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getDefault().getClass());
   }

   @Test
   public void testDefaultSerializeDateKey() {
      // Test when _typeId is TYPE_DATE
      StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
      JsonGeneratorStub jsonGenerator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      try {
         defaultSerializer.serialize(new Date(), jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("Date serialized successfully", jsonGenerator.getFieldName(), "Date");

      // Test when _typeId is TYPE_CALENDAR
      defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
      jsonGenerator = new JsonGeneratorStub();
      provider = new SerializerProviderStub();
      try {
         defaultSerializer.serialize(Calendar.getInstance(), jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("Calendar serialized successfully", jsonGenerator.getFieldName(), "Calendar");

      // Test when _typeId is TYPE_CLASS
      defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
      jsonGenerator = new JsonGeneratorStub();
      provider = new SerializerProviderStub();
      try {
         defaultSerializer.serialize(StdKeySerializersTest.class, jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("Class serialized successfully", jsonGenerator.getFieldName(), "StdKeySerializersTest");

      // Test when _typeId is TYPE_ENUM
      defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
      jsonGenerator = new JsonGeneratorStub();
      provider = new SerializerProviderStub();
      try {
         defaultSerializer.serialize(MyEnum.VALUE1, jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("Enum serialized successfully", jsonGenerator.getFieldName(), "VALUE1");

      // Test when _typeId is TYPE_TO_STRING
      defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
      jsonGenerator = new JsonGeneratorStub();
      provider = new SerializerProviderStub();
      try {
         defaultSerializer.serialize("test", jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("String serialized successfully", jsonGenerator.getFieldName(), "test");
   }

   @Test
   public void testDynamicSerialize() {
      StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
      JsonGeneratorStub jsonGenerator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      try {
         dynamicSerializer.serialize("test", jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("Dynamic serialize success", jsonGenerator.getFieldName(), "test");
   }

   @Test
   public void testStdStringKeySerializerSerialize() {
      StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
      JsonGeneratorStub jsonGenerator = new JsonGeneratorStub();
      SerializerProviderStub provider = new SerializerProviderStub();
      try {
         stringKeySerializer.serialize("test", jsonGenerator, provider);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
      assertEquals("StringKeySerializer serialize success", jsonGenerator.getFieldName(), "test");
   }

   public enum MyEnum {
      VALUE1, VALUE2;
   }
}