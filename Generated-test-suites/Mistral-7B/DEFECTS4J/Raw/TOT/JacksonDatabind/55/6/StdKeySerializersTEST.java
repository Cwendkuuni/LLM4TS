import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetStringSerializer() throws JsonProcessingException {
 String key = "testKey";
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String json = writer.writeValueAsString(key);
 assertEquals("\"testKey\"", json);
 }

 @Test
 public void testGetDateSerializer() throws JsonProcessingException {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String json = writer.writeValueAsString(date);
 assertEquals("\"Fri Aug 06 10:22:16 CEST 2021\"", json);
 }

 @Test
 public void testGetCalendarSerializer() throws JsonProcessingException {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String json = writer.writeValueAsString(calendar);
 assertEquals("\"Fri Aug 06 10:22:16 CEST 2021\"", json);
 }

 @Test
 public void testGetClassSerializer() throws JsonProcessingException {
 Class<?> clazz = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), clazz, false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String json = writer.writeValueAsString(clazz.getName());
 assertEquals("\"java.lang.String\"", json);
 }

 @Test
 public void testGetEnumSerializer() throws JsonProcessingException {
 Enum testEnum = Enum.valueOf(MyEnum.class, "TestValue");
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), testEnum.getClass());
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String json = writer.writeValueAsString(testEnum);
 assertEquals("\"TestValue\"", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownTypeSerializer() throws JsonProcessingException {
 Object unknownType = new Object();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), unknownType.getClass(), false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 writer.writeValueAsString(unknownType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 writer.writeValueAsString(null);
 }
}