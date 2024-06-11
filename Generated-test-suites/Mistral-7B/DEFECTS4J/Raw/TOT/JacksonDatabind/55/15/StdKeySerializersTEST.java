import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private SerializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 config = new SerializationConfig();
 mapper = new ObjectMapper(config);
 }

 @Test
 public void testGetStdKeySerializer_String() throws Exception {
 String str = "test";
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(str, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the string "test" was correctly serialized
 assertEquals("\"test\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Date() throws Exception {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the date was correctly serialized
 assertEquals("\"Thu Aug 04 14:32:26 CDT 2023\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Calendar() throws Exception {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(calendar, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the calendar was correctly serialized
 assertEquals("\"Thu Aug 04 14:32:26 CDT 2023\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Class() throws Exception {
 Class<String> cls = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, cls, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(cls, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the class name was correctly serialized
 assertEquals("\"java.lang.String\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Enum() throws Exception {
 enum TestEnum {
 TEST
 }
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, TestEnum.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(TestEnum.TEST, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the enum name was correctly serialized
 assertEquals("\"TEST\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Object() throws Exception {
 Object obj = new Object();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(obj, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the object was correctly serialized using the default key serializer
 assertEquals("\"[B@6f82c2f7\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetStdKeySerializer_Null() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that null was correctly serialized as an empty string
 assertEquals("\"\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_String() throws Exception {
 String str = "test";JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(str, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the string "test" was correctly serialized
 assertEquals("\"test\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_Date() throws Exception {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the date was correctly serialized
 assertEquals("\"Thu Aug 04 14:32:26 CDT 2023\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_Class() throws Exception {
 Class<String> cls = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, cls);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(cls, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the class name was correctly serialized
 assertEquals("\"java.lang.String\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_Enum() throws Exception {
 enum TestEnum {
 TEST
 }
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, TestEnum.class);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(TestEnum.TEST, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the enum name was correctly serialized
 assertEquals("\"TEST\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_Object() throws Exception {
 Object obj = new Object();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, obj.getClass());
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(obj, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that the object was correctly serialized using the default key serializer
 assertEquals("\"[B@6f82c2f7\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetFallbackKeySerializer_Null() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, generator, mapper.getDefaultSerializerProvider());
 generator.flush();
 // Verify that null was correctly serialized as an empty string
 assertEquals("\"\"", generator.getOutputBuffer().toString());
 }
}