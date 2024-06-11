import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonDeserializer.EmptyContextType;
import com.fasterxml.jackson.databind.deser.std.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFind() {
 // Test finding deserializer for known classes
 Class<?> uuidClass = UUID.class;
 String uuidClassName = uuidClass.getName();
 JsonDeserializer<?> uuidDeserializer = JdkDeserializers.find(uuidClass, uuidClassName);
 assertTrue(uuidDeserializer instanceof UUIDDeserializer);

 Class<?> atomicBooleanClass = AtomicBoolean.class;
 String atomicBooleanClassName = atomicBooleanClass.getName();
 JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(atomicBooleanClass, atomicBooleanClassName);
 assertTrue(atomicBooleanDeserializer instanceof AtomicBooleanDeserializer);

 Class<?> stackTraceElementClass = StackTraceElement.class;
 String stackTraceElementClassName = stackTraceElementClass.getName();
 JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(stackTraceElementClass, stackTraceElementClassName);
 assertTrue(stackTraceElementDeserializer instanceof StackTraceElementDeserializer);

 Class<?> byteBufferClass = ByteBuffer.class;
 String byteBufferClassName = byteBufferClass.getName();
 JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(byteBufferClass, byteBufferClassName);
 assertTrue(byteBufferDeserializer instanceof ByteBufferDeserializer);

 // Test finding deserializer for unsupported class
 Class<?> unsupportedClass = Integer.class;
 String unsupportedClassName = unsupportedClass.getName();
 JsonDeserializer<?> unsupportedDeserializer = JdkDeserializers.find(unsupportedClass, unsupportedClassName);
 assertNull(unsupportedDeserializer);
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullClassName() {
 JdkDeserializers.find(UUID.class, null);
 }

 @Test
 public void testFindUnregisteredClass() {
 Class<?> unregisteredClass = MyCustomClass.class;
 String unregisteredClassName = unregisteredClass.getName();
 JsonDeserializer<?> unregisteredDeserializer = JdkDeserializers.find(unregisteredClass, unregisteredClassName);
 assertNull(unregisteredDeserializer);
 }

 @Test
 public void testFindUnknownRawType() {
 Class<?> unknownRawType = String[].class;
 String unknownRawTypeName = unknownRawType.getName();
 JsonDeserializer<?> unknownDeserializer = JdkDeserializers.find(unknownRawType, unknownRawTypeName);
 assertNull(unknownDeserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWrongClassName() {
 Class<?> wrongClassName = String.class;
 String wrongClassNameName = UUID.class.getName();
 JsonDeserializer<?> wrongDeserializer = JdkDeserializers.find(wrongClassName, wrongClassNameName);
 }

 @Test
 public void testFromStringDeserializer() {
 // Assuming FromStringDeserializer is properly tested in its own test suite
 // We just need to make sure JdkDeserializers.find does not return it for JDK types
 Class<?> stringClass = String.class;
 String stringClassName = stringClass.getName();
 JsonDeserializer<?> stringDeserializer = JdkDeserializers.find(stringClass, stringClassName);
 assertNotNull(stringDeserializer);
 assertFalse(stringDeserializer instanceof FromStringDeserializer);
 }

 @Test
 public void testByteBufferDeserializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 StoneStomper stoneStomper = new StoneStomper();
 objectMapper.registerModule(new SimpleModule().addSerializer(ByteBuffer.class, new ByteBufferSerializer(stoneStomper)));
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { 0x01, 0x02, 0x03, 0x04 });
 String json = objectMapper.writerWithView(new StandardSerializer(ByteBuffer.class)).writeValueAsString(byteBuffer);
 JsonNode node = objectMapper.readTree(json);
 ByteBuffer deserializedByteBuffer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName())
 .deserialize(emptyContext(), new Object(), node, new TypeReference<ByteBuffer>() {});
 assertEquals(byteBuffer, deserializedByteBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testByteBufferDeserializerInvalidJson() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 String json = "{\"invalid\": \"json\"}";
 JsonNode node = objectMapper.readTree(json);
 JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName())
 .deserialize(EmptyContextType.Instance, null, node, new TypeReference<ByteBuffer>() {});
 }

 // Add tests for other methods if needed

}