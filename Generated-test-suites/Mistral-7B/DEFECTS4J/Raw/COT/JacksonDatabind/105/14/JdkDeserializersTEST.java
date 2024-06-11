import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFind_ValidClassNameAndType_ReturnsDeserializer() {
 Class<UUID> uuidClass = UUID.class;
 String uuidClassName = uuidClass.getName();
 JsonDeserializer<UUID> expectedDeserializer = new UUIDDeserializer();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(uuidClass, uuidClassName);
 assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
 }

 @Test
 public void testFind_ValidType_ReturnsDeserializer() {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<UUID> expectedDeserializer = new UUIDDeserializer();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(uuidClass, null);
 assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
 }

 @Test
 public void testFind_InvalidClassNameAndType_ReturnsNull() {
 Class<Integer> integerClass = Integer.class;
 String uuidClassName = UUID.class.getName();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(integerClass, uuidClassName);
 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_NullType_ThrowsIllegalArgumentException() {
 assertThrows(IllegalArgumentException.class, () -> {
 JdkDeserializers.find(null, "java.util.UUID");
 });
 }

 @Test
 public void testFind_NullClassName_ThrowsIllegalArgumentException() {
 assertThrows(IllegalArgumentException.class, () -> {
 JdkDeserializers.find(UUID.class, null);
 });
 }

 @Test
 public void testFind_ClassNamesNotInHashSet_ReturnsNull() {
 Class<String> stringClass = String.class;
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stringClass, "com.example.MyClass");
 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_FromStringDeserializer_ReturnsDeserializer() {
 Class<String> stringClass = String.class;
 JsonDeserializer<String> expectedDeserializer = ObjectMapper.getDefaultInstance().findValueSerializer(String.class);
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stringClass, null);
 assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
 }

 @Test
 public void testFind_Arrays_ReturnsNull() {
 Class<int[]> intArrayClass = int[].class;
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(intArrayClass, null);
 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_List_ReturnsNull() {
 Class<List<Integer>> listClass = new TypeReference<List<Integer>>(){}.getType();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(listClass, null);
 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_Map_ReturnsNull() {
 Class<Map<String, String>> mapClass = new TypeReference<Map<String, String>>(){}.getType();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(mapClass, null);
 assertNull(actualDeserializer);
 }

 @Test
 public void testFind_Set_ReturnsNull() {
 Class<Set<String>> setClass = new TypeReference<Set<String>>(){}.getType();
 JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(setClass, null);
 assertNull(actualDeserializer);
 }

 @Test
 public void testUuidDeserializer_ValidUuidString_ReturnsDeserialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 String uuidString = "123e4567-e89b-12d3-a456-426614174000";
 UUID expectedUuid = new UUID(123, 4567897654321L);
 UUID actualUuid = objectMapper.readValue(new StringReader(uuidString), UUID.class);
 assertEquals(expectedUuid, actualUuid);
 }

 @Test
 public void testUuidDeserializer_InvalidUuidString_ThrowsJsonProcessingException() {
 assertThrows(JsonProcessingException.class, () -> {
 ObjectMapper objectMapper = new ObjectMapper();
 String invalidUuidString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 objectMapper.readValue(new StringReader(invalidUuidString), UUID.class);
 });
 }

 @Test
 public void testAtomicBooleanDeserializer_ValidAtomicBooleanString_ReturnsDeserialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 String atomicBooleanString = "true";
 AtomicBoolean expectedAtomicBoolean = new AtomicBoolean(true);
 AtomicBoolean actualAtomicBoolean = objectMapper.readValue(new StringReader(atomicBooleanString), AtomicBoolean.class);
 assertEquals(expectedAtomicBoolean, actualAtomicBoolean);
 }

 @Test
 public void testAtomicBooleanDeserializer_InvalidAtomicBooleanString_ThrowsJsonProcessingException() {
 assertThrows(JsonProcessingException.class, () -> {
 ObjectMapper objectMapper = new ObjectMapper();
 String invalidAtomicBooleanString = "123";
 objectMapper.readValue(new StringReader(invalidAtomicBooleanString), AtomicBoolean.class);
 });
 }

 @Test
 public void testStackTraceElementDeserializer_ValidStackTraceElementArray_ReturnsDeserialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 String stackTraceElementsJson = "[Ljava.lang.StackTraceElement;@7852e922";
 StackTraceElement[] expectedStackTraceElements = new StackTraceElement[]{
 new StackTraceElement("JdkDeserializersTest", "testStackTraceElementDeserializer", "JdkDeserializersTest.java", 10),
 new StackTraceElement("JdkDeserializersTest", "testFind", "JdkDeserializersTest.java", 11),
 new StackTraceElement("JdkDeserializersTest", "<init>", "JdkDeserializersTest.java", 1),
 new StackTraceElement("junit.framework.Assert.assertArrayEquals", "assertArrayEquals", "Assert.java", 68),
 new StackTraceElement("org.junit.internal.runners.JUnit38ClassRunner.runMapperWithParameters", "<init>", "JUnit38ClassRunner.java", 101),
 new StackTraceElement("org.junit.internal.runners.JUnit38ClassRunner.<init>", "<init>", "JUnit38ClassRunner.java", 37),
 new StackTraceElement("org.junit.runner.RunnerImpl.startCase", "runCase", "RunnerImpl.java", 234),
 new StackTraceElement("org.junit.runner.RunnerImpl.runChildren", "runChildren", "RunnerImpl.java", 221),
 new StackTraceElement("org.junit.runner.RunnerImpl.runChild", "runChild", "RunnerImpl.java", 197),
 new StackTraceElement("org.junit.runner.RunnerImpl.runChildren", "runChildren", "RunnerImpl.java", 191),
 new StackTraceElement("org.junit.runner.RunnerImpl.runChild", "runChild", "RunnerImpl.java", 197),
 new StackTraceElement("org.junit.runner.JUnitCore.runChild", "runChild", "JUnitCore.java", 139),
 new StackTraceElement("org.junit.runner.JUnitCore.runChildren", "runChildren", "JUnitCore.java", 136),
 new StackTraceElement("org.junit.runner.JUnitCore.runSuite", "run", "JUnitCore.java", 128)
 };
 StackTraceElement[] actualStackTraceElements = objectMapper.readValue(new StringReader(stackTraceElementsJson), new TypeReference<StackTraceElement[]>(){}.getType());
 assertArrayEquals(expectedStackTraceElements, actualStackTraceElements);
 }

 @Test
 public void testStackTraceElementDeserializer_InvalidStackTraceElementArray_ThrowsJsonProcessingException() {
 assertThrows(JsonProcessingException.class, () -> {
 ObjectMapper objectMapper = new ObjectMapper();
 String invalidStackTraceElementsJson = "[]";
 objectMapper.readValue(new StringReader(invalidStackTraceElementsJson), StackTraceElement[].class);
 });
 }

 @Test
 public void testByteBufferDeserializer_ValidByteBuffer_ReturnsDeserialization() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ByteBuffer byteBuffer = ByteBuffer.allocate(10);
 byteBuffer.put("Hello World!".getBytes());
 byteBuffer.flip();
 String expectedString = "Hello World!";
 String actualString = objectMapper.readValue(new ByteBufferReader(byteBuffer), String.class);
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testByteBufferDeserializer_InvalidByteBuffer_ThrowsJsonProcessingException() {
 assertThrows(JsonProcessingException.class, () -> {
 ObjectMapper objectMapper = new ObjectMapper();
 ByteBuffer invalidByteBuffer = ByteBuffer.allocate(1);
 objectMapper.readValue(new ByteBufferReader(invalidByteBuffer), String.class);
 });
 }
}