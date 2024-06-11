import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindDeserializerForUUID() {
 UUID expectedUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 Object actualUuid;
 try {
 actualUuid = objectMapper.readValue(new ObjectMapper().writeValueAsString(expectedUuid), new TypeReference<Object>() {});
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to deserialize UUID", e);
 }
 assertEquals(expectedUuid, actualUuid);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() {
 AtomicBoolean expectedAtomicBoolean = new AtomicBoolean(true);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 Object actualAtomicBoolean;
 try {
 actualAtomicBoolean = objectMapper.readValue(new ObjectMapper().writeValueAsString(expectedAtomicBoolean), AtomicBoolean.class);
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to deserialize AtomicBoolean", e);
 }
 assertEquals(expectedAtomicBoolean, actualAtomicBoolean);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() {
 StackTraceElement expectedStackTraceElement = new StackTraceElement("TestClass", "testMethod", "/test/TestClass.java", 1);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 Object actualStackTraceElement;
 try {
 actualStackTraceElement = objectMapper.readValue(new ObjectMapper().writeValueAsString(expectedStackTraceElement), StackTraceElement.class);
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to deserialize StackTraceElement", e);
 }
 assertEquals(expectedStackTraceElement, actualStackTraceElement);
 }

 @Test
 public void testFindDeserializerForByteBuffer() {
 byte[] byteArray = new byte[] { 0x00, 0x01, 0x02, 0x03 };
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(byteArray);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 Object actualByteBuffer;
 try {
 actualByteBuffer = objectMapper.readValue(new ObjectMapper().writeValueAsString(expectedByteBuffer), ByteBuffer.class);
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to deserialize ByteBuffer", e);
 }
 assertEquals(expectedByteBuffer, actualByteBuffer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() {
 String unknownClassName = "com.nonexistent.UnknownClass";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, unknownClassName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForNullClass() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, UUID.class.getName());
 assertNull(deserializer);
 }
}