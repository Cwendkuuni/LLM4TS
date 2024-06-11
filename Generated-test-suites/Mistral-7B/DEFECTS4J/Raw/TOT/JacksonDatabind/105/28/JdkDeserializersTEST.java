import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @BeforeAll
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFind_UUID() throws Exception {
 UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 Object result = objectMapper.readValue(new String("\"123e4567-e89b-12d3-a456-426614174000\""), new TypeReference<UUID>() {});

 assertEquals(deserializer.getClass(), JdkDeserializers.UUIDDeserializer.class);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_AtomicBoolean() throws Exception {
 AtomicBoolean expected = new AtomicBoolean(true);
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 Object result = objectMapper.readValue(new String("true"), new TypeReference<AtomicBoolean>() {});

 assertEquals(deserializer.getClass(), JdkDeserializers.AtomicBooleanDeserializer.class);
 assertEquals(expected, result);
 }

 @Test
 public void testFind_StackTraceElement() throws Exception {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String stackTraceElementString = "MyClass[methodName(Ljava/lang/String;)(Ljava/lang/String;)V, lineNumber]";
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, stackTraceElementClass.getName());
 Object result = objectMapper.readValue(new String(stackTraceElementString), stackTraceElementClass);

 assertEquals(deserializer.getClass(), JdkDeserializers.StackTraceElementDeserializer.class);
 assertEquals(result, new StackTraceElement("MyClass", "methodName", "lineNumber"));
 }

 @Test
 public void testFind_ByteBuffer() throws Exception {
 ByteBuffer byteBuffer = ByteBuffer.allocate(10);
 byteBuffer.put("Test".getBytes());
 byteBuffer.flip();
 String expectedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(byteBuffer);
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 Object result = objectMapper.readValue(new String(expectedJson), new TypeReference<ByteBuffer>() {});

 assertEquals(deserializer.getClass(), JdkDeserializers.ByteBufferDeserializer.class);
 assertTrue(result instanceof ByteBuffer);
 ByteBuffer byteBufferResult = (ByteBuffer) result;
 assertArrayEquals(byteBuffer.array(), byteBufferResult.array());
 }

 @Test
 public void testFind_UnknownClass() throws Exception {
 Class<String> stringClass = String.class;
 String unknownClassName = "com.example.UnknownClass";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, unknownClassName);

 assertNull(deserializer);
 }
}