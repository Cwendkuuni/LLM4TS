import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializerWithStringKey() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);

 String key = "testKey";
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);
 serializer.serialize(key, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 generator.flush();

 String serializedKey = outputStream.toString();
 assertEquals("\"testKey\"", serializedKey);
 }

 @Test
 public void testGetStdKeySerializerWithDateKey() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);
 serializer.serialize(date, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 generator.flush();

 String serializedKey = outputStream.toString();
 assertEquals("\"\\/Date( " + date.getTime() + " )+\"", serializedKey);
 }

 @Test
 public void testGetStdKeySerializerWithEnumKey() throws JsonProcessingException {
 Enum testEnum = Enum.valueOf(MyEnum.class, "TEST");
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyEnum.class, false);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator generator = mapper.getFactory().createGenerator(outputStream);
 serializer.serialize(testEnum, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 generator.flush();

 String serializedKey = outputStream.toString();
 assertEquals("\"TEST\"", serializedKey);
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerWithNullKeyType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, false);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetStdKeySerializerWithUnknownKeyType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyUnknownClass.class, false);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetStdKeySerializerWithUnsupportedSerializationFeature() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 Object writeValueAsString = mapper.valueToTree("testValue").withoutRoot().value1();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyEnum.class, false);
 }
}