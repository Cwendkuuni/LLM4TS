import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFind_UUID() {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 Object result = mapper.readValue("\"123e4567-e89b-12d3-a456-426614174000\"", new TypeReference<UUID>() {});
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "class com.fasterxml.jackson.databind.deser.std.UUIDDeserializer");
 assertEquals(expected, deserializer.deserialize(mapper.getFactory().getValueCodec().fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.class, mapper));
 }

 @Test
 public void testFind_AtomicBoolean() {
 AtomicBoolean expected = new AtomicBoolean(true);
 Object result = mapper.readValue("true", AtomicBoolean.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "class com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer");
 assertEquals(expected, deserializer.deserialize(mapper.getFactory().getValueCodec().fromString("true"), AtomicBoolean.class, mapper));
 }

 @Test
 public void testFind_StackTraceElement() {
 StackTraceElement expected = new StackTraceElement("TestClass", "testMethod", "TestFile.java", 1);
 Object result = mapper.readValue("{\"className\":\"TestClass\",\"methodName\":\"testMethod\",\"file\":\"TestFile.java\",\"line\":1}", new TypeReference<StackTraceElement>() {});
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "class com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer");
 assertEquals(expected, deserializer.deserialize(mapper.getFactory().getValueCodec().fromString("{\"className\":\"TestClass\",\"methodName\":\"testMethod\",\"file\":\"TestFile.java\",\"line\":1}"), StackTraceElement.class, mapper));
 }

 @Test
 public void testFind_ByteBuffer() {
 ByteBuffer expected = ByteBuffer.wrap("Test".getBytes());
 Object result = mapper.readValue("\"Test\"", ByteBuffer.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "class com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer");
 assertEquals(expected, deserializer.deserialize(mapper.getFactory().getValueCodec().fromString("\"Test\""), ByteBuffer.class, mapper));
 }

 @Test
 public void testFind_UnknownClass() {
 Class<?> unknownClass = Object.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(unknownClass, "UnknownClass");
 assertNull(deserializer);
 }

 @Test
 public void testFind_NullRawType() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, "UUID");
 assertNull(deserializer);
 }

 @Test
 public void testFind_NullClsName() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, null);
 assertNull(deserializer);
 }
}