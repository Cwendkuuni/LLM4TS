import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
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
 * Test method for {@link JdkDeserializers#find(Class, String)} with a valid class name.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithValidClassName() throws JsonProcessingException {
 Class<UUID> uuidClass = UUID.class;
 String uuidClassName = uuidClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, uuidClassName);
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(new UUID(1, 2)), deserializer.getType());
 assertEquals(new UUID(1, 2), deserializedObject);
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a non-existent class name.
 * @throws JsonProcessingException
 */
 @Test(expected = JsonMappingException.ClassIdMismatch.class)
 public void testFindWithNonExistentClassName() throws JsonProcessingException {
 Class<String> stringClass = String.class;
 String nonExistentClassName = "com.non.existent.class.name";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, nonExistentClassName);
 objectMapper.readValue(new ObjectMapper().writeValueAsString(""), deserializer.getType());
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a null class name.
 * @throws JsonProcessingException
 */
 @Test(expected = NullPointerException.class)
 public void testFindWithNullClassName() throws JsonProcessingException {
 Class<Map<String, Integer>> mapClass = new TypeReference<Map<String, Integer>>() {}.getType();
 String nullClassName = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(mapClass, nullClassName);
 objectMapper.readValue(new ObjectMapper().writeValueAsString(""), deserializer.getType());
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a class name that matches a deserializer in FromStringDeserializer.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithFromStringDeserializerClassName() throws JsonProcessingException {
 Class<Map<String, Integer>> mapClass = new TypeReference<Map<String, Integer>>() {}.getType();
 String fromStringDeserializerClassName = FromStringDeserializer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(mapClass, fromStringDeserializerClassName);
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString("{\"key\":\"value\"}"), deserializer.getType());
 assertTrue(deserializedObject instanceof Map);
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a primitive class.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithPrimitiveClass() throws JsonProcessingException {
 Class<Boolean> booleanClass = Boolean.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(booleanClass, booleanClass.getName());
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(true), deserializer.getType());
 assertTrue((Boolean) deserializedObject);
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a class that has a single-arg constructor.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithAtomicIntegerClass() throws JsonProcessingException {
 Class<AtomicInteger> atomicIntegerClass = AtomicInteger.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicIntegerClass, atomicIntegerClass.getName());
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(1), deserializer.getType());
 assertEquals(1, deserializedObject);
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with a null input argument.
 * @throws JsonProcessingException
 */
 @Test(expected = NullPointerException.class)
 public void testFindWithNullInputArgument() throws JsonProcessingException {
 Class<?> nullClass = null;
 String className = "java.lang.String";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, className);
 objectMapper.readValue(new ObjectMapper().writeValueAsString(""), deserializer.getType());
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with an empty input argument list.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithEmptyInputArgumentList() throws JsonProcessingException {
 Class<?> emptyClassList[] = {};
 String className = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(emptyClassList, className);
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(new UUID(1, 2)), deserializer.getType());
 assertEquals(new UUID(1, 2), deserializedObject);
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with an input argument list containing a non-existent class name.
 * @throws JsonProcessingException
 */
 @Test(expected = JsonMappingException.ClassIdMismatch.class)
 public void testFindWithInputArgumentListContainingNonExistentClassName() throws JsonProcessingException {
 Class<?> classList[] = {String.class};
 String nonExistentClassName = "com.non.existent.class.name";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(classList, nonExistentClassName);
 objectMapper.readValue(new ObjectMapper().writeValueAsString(""), deserializer.getType());
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)} with an input argument list containing a class name that matches a deserializer in FromStringDeserializer.
 * @throws JsonProcessingException
 */
 @Test
 public void testFindWithInputArgumentListContainingFromStringDeserializerClassName() throws JsonProcessingException {
 Class<?> classList[] = {String.class};
 String fromStringDeserializerClassName = FromStringDeserializer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(classList, fromStringDeserializerClassName);
 Object deserializedObject = objectMapper.readValue(new ObjectMapper().writeValueAsString("{\"key\":\"value\"}"), deserializer.getType());
 assertTrue(deserializedObject instanceof Map);
 }
}