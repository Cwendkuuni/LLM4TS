import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JdkDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindDeserializerForUUID() {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 try {
 Object result = mapper.readValue(new ByteArrayInputStream(new byte[0]), new TypeReference<UUID>() {});
 fail("Should not be able to deserialize empty byte array to UUID without a deserializer");
 } catch (IOException e) {
 // expected
 }
 Object actual = mapper.readValue(new ByteArrayInputStream(("123e4567-e89b-12d3-a456-426614174000".getBytes()).clone()), UUID.class, deserializer);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() {
 AtomicBoolean expected = new AtomicBoolean(true);
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 Object actual = mapper.readValue(new ByteArrayInputStream(new byte[0]), AtomicBoolean.class, deserializer);
 assertTrue(actual instanceof AtomicBoolean);
 AtomicBoolean actualAtomicBoolean = (AtomicBoolean) actual;
 assertEquals(expected, actualAtomicBoolean);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() {
 StackTraceElement expected = new StackTraceElement("TestClass", "testMethod", "TestFile.java", 1);
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 Object actual = mapper.readValue(new ByteArrayInputStream(("TestClass\ttestMethod\tTestFile.java\t1".getBytes()).clone()), StackTraceElement.class, deserializer);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForByteBuffer() {
 ByteBuffer expected = ByteBuffer.allocate(10);
 expected.putLong(1L);
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 Object actual = mapper.readValue(new ByteArrayInputStream(expected.array()), ByteBuffer.class, deserializer);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForUnknownClass() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.lang.String.class");
 assertNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerForUnknownClassNameWithDifferentCase() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.lang.String.CLass");
 }
}