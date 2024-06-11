import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 /**
 * Test method for {@link JdkDeserializers#find(Class, String)}.
 */
 @Test
 public void testFind() {
 Class<?> rawTypeUuid = UUID.class;
 String clsNameUuid = UUID.class.getName();
 JsonDeserializer<?> expectedUuidDeserializer = new UUIDDeserializer();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawTypeUuid, clsNameUuid);
 assertSame(expectedUuidDeserializer, deserializer);

 Class<?> rawTypeAtomicBoolean = AtomicBoolean.class;
 String clsNameAtomicBoolean = AtomicBoolean.class.getName();
 JsonDeserializer<?> expectedAtomicBooleanDeserializer = new AtomicBooleanDeserializer();
 JsonDeserializer<?> deserializerAtomicBoolean = JdkDeserializers.find(rawTypeAtomicBoolean, clsNameAtomicBoolean);
 assertSame(expectedAtomicBooleanDeserializer, deserializerAtomicBoolean);

 Class<?> rawTypeByteBuffer = ByteBuffer.class;
 String clsNameByteBuffer = ByteBuffer.class.getName();
 JsonDeserializer<?> expectedByteBufferDeserializer = new ByteBufferDeserializer();
 JsonDeserializer<?> deserializerByteBuffer = JdkDeserializers.find(rawTypeByteBuffer, clsNameByteBuffer);
 assertSame(expectedByteBufferDeserializer, deserializerByteBuffer);

 Class<?> rawTypeUnsupported = new Object() {}.getClass();
 String clsNameUnsupported = Object.class.getName();
 JsonDeserializer<?> expectedUnsupportedDeserializer = null;
 JsonDeserializer<?> deserializerUnsupported = JdkDeserializers.find(rawTypeUnsupported, clsNameUnsupported);
 assertNull(deserializerUnsupported);
 }

 /**
 * Test method for edge case {@link JdkDeserializers#find(Class, String)} when the class name is null.
 */
 @Test(expected = NullPointerException.class)
 public void testFindWithNullClassName() {
 Class<?> rawType = UUID.class;
 String clsName = null;
 JdkDeserializers.find(rawType, clsName);
 }

 /**
 * Test method for edge case {@link JdkDeserializers#find(Class, String)} when the class name is empty.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFindWithEmptyClassName() {
 Class<?> rawType = UUID.class;
 String clsName = "";
 JdkDeserializers.find(rawType, clsName);
 }

 /**
 * Test method for edge case {@link JdkDeserializers#find(Class, String)} when the class is not in the whitelist.
 */
 @Test
 public void testFindWithUnsupportedClass() {
 Class<?> rawTypeUnsupported = new Object() {}.getClass();
 String clsNameUnsupported = Object.class.getName();
 JsonDeserializer<?> deserializerUnsupported = JdkDeserializers.find(rawTypeUnsupported, clsNameUnsupported);
 assertNull(deserializerUnsupported);
 }

 /**
 * Test method for edge case {@link JdkDeserializers#find(Class, String)} when the class is a generic type.
 */
 @Test
 public void testFindWithGenericType() throws JsonProcessingException {
 TypeReference<List<UUID>> typeReference = new TypeReference<List<UUID>>() {};
 Object listUuid = mapper.readValue( "[1,2,3,4,5]", typeReference );
 Class<?> rawTypeListUuid = listUuid.getClass().getComponentType();
 String clsNameListUuid = UUID.class.getName();
 JsonDeserializer<?> deserializerListUuid = JdkDeserializers.find(rawTypeListUuid, clsNameListUuid);
 assertNotNull(deserializerListUuid);
 }
}