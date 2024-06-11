import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test for getStdKeySerializer method with given class type
 */
 @Test
 public void testGetStdKeySerializer_String() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method with null class type
 */
 @Test
 public void testGetStdKeySerializer_Null() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, false);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method with Object class type
 */
 @Test
 public void testGetStdKeySerializer_Object() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Object.class, false);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 /**
 * Test for getStdKeySerializer method with Date class type
 */
 @Test
 public void testGetStdKeySerializer_Date() throws Exception {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 /**
 * Test for getStdKeySerializer method with Calendar class type
 */
 @Test
 public void testGetStdKeySerializer_Calendar() throws Exception {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 /**
 * Test for getStdKeySerializer method with Class class type
 */
 @Test
 public void testGetStdKeySerializer_Class() throws Exception {
 Class<?> clazz = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), clazz, false);
 assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 /**
 * Test for getStdKeySerializer method with Enum class type
 */
 @Test
 public void testGetStdKeySerializer_Enum() throws Exception {
 Enum testEnum = Enum.valueOf(String.class, "TestEnumValue");
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), testEnum.getClass(), false);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 /**
 * Test for getFallbackKeySerializer method with given class type
 */
 @Test
 public void testGetFallbackKeySerializer_String() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 /**
 * Test for getFallbackKeySerializer method with null class type
 */
 @Test
 public void testGetFallbackKeySerializer_Null() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 /**
 * Test for getFallbackKeySerializer method with Object class type
 */
 @Test
 public void testGetFallbackKeySerializer_Object() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Object.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 /**
 * Test for getFallbackKeySerializer method with custom class type
 */
 @Test
 public void testGetFallbackKeySerializer_CustomClass() throws Exception {
 class CustomClass {
 }
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), CustomClass.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 /**
 * Test for Dynamic serializer
 */
 @Test
 public void testDynamicSerializer() throws Exception {
 Object value = new Object() {
 };
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), value.getClass(), true);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 /**
 * Test for Default serializer with String key
 */
 @Test
 public void testDefaultSerializer_StringKey() throws Exception {
 String key = "test";
 JsonGenerator generator = mapper.getFactory().createGenerator();
 StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
 ObjectMapper testMapper = new ObjectMapper();
 testMapper.setSerializerProvider(new DefaultSerializerProvider());
 testMapper.addSerializer(String.class, defaultSerializer);
 defaultSerializer.serialize(key, generator, testMapper.getDefaultSerializerProvider());
 assertEquals("test", generator.getCurrentName());
 }

 /**
 * Test for Default serializer with Enum key
 */
 @Test
 public void testDefaultSerializer_EnumKey() throws Exception {
 Enum testEnum = Enum.valueOf(String.class, "TestEnumValue");
 JsonGenerator generator = mapper.getFactory().createGenerator();
 StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, testEnum.getClass());
 ObjectMapper testMapper = new ObjectMapper();
 testMapper.setSerializerProvider(new DefaultSerializerProvider());
 testMapper.addSerializer(testEnum.getClass(), defaultSerializer);
 defaultSerializer.serialize(testEnum, generator, testMapper.getDefaultSerializerProvider());
 assertEquals(testEnum.name(), generator.getCurrentName());
 }

}