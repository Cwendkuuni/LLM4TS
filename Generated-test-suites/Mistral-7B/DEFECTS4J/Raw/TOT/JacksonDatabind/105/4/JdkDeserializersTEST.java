import org.junit.jupiter.api.*;
import org.junit.platform.runner.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitJupiter.class)
public class JdkDeserializersTest {

 @BeforeAll
 static void setup() {
 // Initialize ObjectMapper for testing
 ObjectMapper mapper = new ObjectMapper();
 }

 @Test
 void testFind_UUID_ClassName() {
 // Given
 String clsName = UUID.class.getName();
 Class<?> rawType = UUID.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNotNull(deserializer, "Expected UUID deserializer to be found");
 assertEquals(UUIDDeserializer.class, deserializer.getClass(), "Unexpected deserializer class");
 }

 @Test
 void testFind_UUID_NullClassName() {
 // Given
 String clsName = null;
 Class<?> rawType = UUID.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNull(deserializer, "Expected null for null class name");
 }

 @Test
 void testFind_AtomicBoolean_ClassName() {
 // Given
 String clsName = AtomicBoolean.class.getName();
 Class<?> rawType = AtomicBoolean.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNotNull(deserializer, "Expected AtomicBoolean deserializer to be found");
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass(), "Unexpected deserializer class");
 }

 @Test
 void testFind_AtomicBoolean_NullClassName() {
 // Given
 String clsName = null;
 Class<?> rawType = AtomicBoolean.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNull(deserializer, "Expected null for null class name");
 }

 @Test
 void testFind_StackTraceElement_ClassName() {
 // Given
 String clsName = StackTraceElement.class.getName();
 Class<?> rawType = StackTraceElement.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNotNull(deserializer, "Expected StackTraceElement deserializer to be found");
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass(), "Unexpected deserializer class");
 }

 @Test
 void testFind_StackTraceElement_NullClassName() {
 // Given
 String clsName = null;
 Class<?> rawType = StackTraceElement.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNull(deserializer, "Expected null for null class name");
 }

 @Test
 void testFind_ByteBuffer_ClassName() {
 // Given
 String clsName = ByteBuffer.class.getName();
 Class<?> rawType = ByteBuffer.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNotNull(deserializer, "Expected ByteBuffer deserializer to be found");
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass(), "Unexpected deserializer class");
 }

 @Test
 void testFind_ByteBuffer_NullClassName() {
 // Given
 String clsName = null;
 Class<?> rawType = ByteBuffer.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNull(deserializer, "Expected null for null class name");
 }

 @Test
 void testFind_NonExistentClass() {
 // Given
 String clsName = "NonExistentClass";
 Class<?> rawType = Object.class;

 // When
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

 // Then
 assertNull(deserializer, "Expected null for non-existent class");
 }
}