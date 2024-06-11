import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testGetStdKeySerializer() throws Exception {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 Object value = "test";
 keySerializer.serialize(value, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("test", value);
 }

 @Test
 public void testGetStdKeySerializer_nullRawKeyType() throws Exception {
 Class<?> rawKeyType = null;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, keySerializer);
 }

 @Test
 public void testGetStdKeySerializer_Object() throws Exception {
 Class<?> rawKeyType = Object.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 Object value = new Object();
 keySerializer.serialize(value, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("[]", mapper.writeValueAsString(value));
 }

 @Test
 public void testGetStdKeySerializer_Date() throws Exception {
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 Date date = new Date();
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(date, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("\"2023-03-14T15:30:23.000Z\"", mapper.writeValueAsString(date));
 }

 @Test
 public void testGetStdKeySerializer_Calendar() throws Exception {
 Class<?> rawKeyType = Calendar.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 Calendar calendar = Calendar.getInstance();
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(calendar, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("\"2023-03-14T15:30:23.000Z\"" , mapper.writeValueAsString(calendar.getTimeInMillis()));
 }

 @Test
 public void testGetStdKeySerializer_Enum() throws Exception {
 Class<MyEnum> rawKeyType = MyEnum.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 MyEnum value = MyEnum.VALUE1;
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(value, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("VALUE1", mapper.writeValueAsString(value));
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializer_Null() throws Exception {
 Class<?> rawKeyType = null;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, false);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 String value = "test";
 keySerializer.serialize(value, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("test", value);
 }

 @Test
 public void testGetFallbackKeySerializer_Enum() throws Exception {
 Class<MyEnum> rawKeyType = MyEnum.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 MyEnum value = MyEnum.VALUE1;
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(value, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("VALUE1", mapper.writeValueAsString(value));
 }

 @Test
 public void testGetFallbackKeySerializer_Class() throws Exception {
 Class<?> rawKeyType = Class.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(MyClass.class, gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("com.mypackage.MyClass", mapper.writeValueAsString(MyClass.class));
 }

 @Test
 public void testGetFallbackKeySerializer_NullRawKeyType() throws Exception {
 Class<?> rawKeyType = null;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, keySerializer);
 }

 @Test
 public void testGetFallbackKeySerializer_NonEnum() throws Exception {
 Class<?> rawKeyType = MyClass.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 keySerializer.serialize(new MyClass(), gen, mapper.getSerializerProvider());
 gen.close();
 assertEquals("{\"name\":\"MyClassName\",\"fields\":{}}", mapper.writeValueAsString(new MyClass()));
 }
}

enum MyEnum {
 VALUE1,
 VALUE2
}

class MyClass {
 String name;
}