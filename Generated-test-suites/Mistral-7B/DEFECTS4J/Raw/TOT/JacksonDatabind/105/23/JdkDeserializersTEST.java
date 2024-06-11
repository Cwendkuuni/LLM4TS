import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for finding the deserializer for UUID class
 */
 @Test
 public void testUuidDeserializer() {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 Object result = null;
 try {
 result = objectMapper.readValue("\"123e4567-e89b-12d3-a456-426614174000\"", new TypeReference<UUID>() {});
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 assertEquals(new UUID(0x123e456712d3a456L, 0x426614174000L), result);
 }

 /**
 * Test for finding the deserializer for StackTraceElement class
 */
 @Test
 public void testStackTraceElementDeserializer() {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 Object result = null;
 try {
 result = objectMapper.readValue("{\"className\":\"MyClass\",\"methodName\":\"myMethod\",\"lineNumber\":12,\"fileName\":\"MyClass.java\",\"nativeMethod\":false}", new TypeReference<StackTraceElement>() {});
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 StackTraceElement expected = new StackTraceElement("MyClass", "myMethod", "MyClass.java", 12);
 assertEquals(expected, result);
 }

 /**
 * Test for finding the deserializer for AtomicBoolean class
 */
 @Test
 public void testAtomicBooleanDeserializer() {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 Object result = null;
 try {
 result = objectMapper.readValue("true", Boolean.TYPE);
 } catch (JsonProcessingException e) {
 result = objectMapper.readValue("{\"value\":true}", new TypeReference<AtomicBoolean>() {});
 }
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 AtomicBoolean expected = new AtomicBoolean(true);
 assertTrue(expected.get());
 assertEquals(expected, result);
 }

 /**
 * Test for finding the deserializer for ByteBuffer class with valid data
 */
 @Test
 public void testByteBufferDeserializerValidData() {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 Object result = null;
 try {
 result = objectMapper.readValue("{\"limit\":10,\"position\":0,\"isReadOnly\":false,\"arrayBase\":[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]}", new TypeReference<ByteBuffer>() {});
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 ByteBuffer expected = ByteBuffer.allocate(10);
 expected.put(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
 expected.flip();
 byte[] actualBytes = ((ByteBuffer) result).array();
 assertArrayEquals(expected.array(), actualBytes);
 }

 /**
 * Test for finding the deserializer for ByteBuffer class with invalid data
 */
 @Test(expected = JsonMappingException.class)
 public void testByteBufferDeserializerInvalidData() {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 Object result = null;
 try {
 result = objectMapper.readValue("{\"limit\":-1,\"position\":0,\"isReadOnly\":false,\"arrayBase\":[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]}", new TypeReference<ByteBuffer>() {});
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }
 }
}