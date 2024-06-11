import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with valid arguments.
 */
 @Test
 public void testFindWithValidArguments() {
 Class<?>[] types = new Class<?>[] { UUID.class, AtomicBoolean.class, StackTraceElement.class, ByteBuffer.class };
 String[] clsNames = new String[] { "java.util.UUID", "java.util.concurrent.atomic.AtomicBoolean", "java.lang.StackTraceElement", "java.nio.ByteBuffer" };

 for (int i = 0; i < types.length; i++) {
 JsonDeserializer<?> expectedDeserializer = null;
 Class<?> rawType = types[i];
 String clsName = clsNames[i];
 if (rawType == UUID.class) {
 expectedDeserializer = new UUIDDeserializer();
 } else if (rawType == AtomicBoolean.class) {
 expectedDeserializer = new AtomicBooleanDeserializer();
 } else if (rawType == StackTraceElement.class) {
 expectedDeserializer = new StackTraceElementDeserializer();
 } else if (rawType == ByteBuffer.class) {
 expectedDeserializer = new ByteBufferDeserializer();
 }

 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(rawType, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }
}

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with null arguments.
 */
 @Test(expected = NullPointerException.class)
 public void testFindWithNullArguments() {
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(null, "java.util.UUID");
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with invalid className.
 */
 @Test
 public void testFindWithInvalidClassName() {
 Class<?> rawType = UUID.class;
 String invalidClassName = "java.util.InvalidClassName";
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(rawType, invalidClassName);
 assertNull(actualDeserializer);
}

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with classNames not in HashSet.
 */
 @Test
 public void testFindWithClassNamesNotInHashSet() {
 Class<?> rawType = Integer.class;
 String clsName = "java.util.UUID";
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(actualDeserializer);
}

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with null rawType.
 */
 @Test(expected = NullPointerException.class)
 public void testFindWithNullRawType() {
 String clsName = "java.util.UUID";
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(null, clsName);
 }

 /**
 * Test method for {@link JdkDeserializers#findDeserializer(Class)} with invalid type.
 */
 @Test
 public void testFindDeserializerWithInvalidType() {
 Class<?> invalidType = new Object() {}.getClass();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.findDeserializer(invalidType);
 assertNull(actualDeserializer);
}

 /**
 * Test method for {@link JdkDeserializers#UUIDDeserializer()} constructor.
 */
 @Test
 public void testUUIDDeserializer() {
 UUIDDeserializer uuidDeserializer = new UUIDDeserializer();
 assertNotNull(uuidDeserializer);
 }

 /**
 * Test method for {@link JdkDeserializers#UUIDDeserializer()} deserialize method.
 */
 @Test
 public void testUUIDDeserializerDeserialize() throws IOException {
 String json = "\"rftp://192.168.0.2:10000/myfile.txt\"";
 UUID expectedUuid = new UUID("rftp", 192, 168, 0, 2, 10000, 0, 123);
 Object actualObject = mapper.readValue(json, new TypeReference<UUID>() {});
 UUID actualUuid = (UUID) actualObject;
 assertEquals(expectedUuid, actualUuid);
 }

 /**
 * Test method for {@link JdkDeserializers#AtomicBooleanDeserializer()} constructor.
 */
 @Test
 public void testAtomicBooleanDeserializer() {
 AtomicBooleanDeserializer atomicBooleanDeserializer = new AtomicBooleanDeserializer();
 assertNotNull(atomicBooleanDeserializer);
 }

 /**
 * Test method for {@link JdkDeserializers#AtomicBooleanDeserializer()} deserialize method.
 */
 @Test
 public void testAtomicBooleanDeserializerDeserialize() throws IOException {
 String json = "true";
 AtomicBoolean expectedAtomicBoolean = new AtomicBoolean(true);
 Object actualObject = mapper.readValue(json,new TypeReference<AtomicBoolean>() {});
 AtomicBoolean actualAtomicBoolean = (AtomicBoolean) actualObject;
 assertEquals(expectedAtomicBoolean, actualAtomicBoolean);
 }

 /**
 * Test method for {@link JdkDeserializers#StackTraceElementDeserializer()} constructor.
 */
 @Test
 public void testStackTraceElementDeserializer() {
 StackTraceElementDeserializer stackTraceElementDeserializer = new StackTraceElementDeserializer();
 assertNotNull(stackTraceElementDeserializer);
 }

 /**
 * Test method for {@link JdkDeserializers#StackTraceElementDeserializer()} deserialize method.
 */
 @Test
 public void testStackTraceElementDeserializerDeserialize() throws IOException {
 String json = "testClass[testMethod(int arg1=1, int arg2=2), /file:lineNumber]";
 StackTraceElement expectedStackTraceElement = new StackTraceElement("testClass", "testMethod", "/file", 1);
 Object actualObject = mapper.readValue(json, new TypeReference<StackTraceElement>() {});
 StackTraceElement actualStackTraceElement = (StackTraceElement) actualObject;
 assertEquals(expectedStackTraceElement, actualStackTraceElement);
 }

 /**
 * Test method for {@link JdkDeserializers#ByteBufferDeserializer()} constructor.
 */
 @Test
 public void testByteBufferDeserializer() {
 ByteBufferDeserializer byteBufferDeserializer = new ByteBufferDeserializer();
 assertNotNull(byteBufferDeserializer);
 }

 /**
 * Test method for {@link JdkDeserializers#ByteBufferDeserializer()} deserialize method.
 */
 @Test
 public void testByteBufferDeserializerDeserialize() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4, 5 });
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(byteBuffer);
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4, 5 });
 Object actualObject = mapper.readValue(json, new TypeReference<ByteBuffer>() {});
 ByteBuffer actualByteBuffer = (ByteBuffer) actualObject;
 assertEquals(expectedByteBuffer, actualByteBuffer);
 }
}