import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 BeanProperty property = new BeanProperty.Std("myProperty", String.class);
 TypeDeserializer result = testee.forProperty(property);
 assertNotNull(result);
 assertInstanceOf(MyTypeDeserializer.class, result);
 }

 @Test
 public void testGetTypeInclusion() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, testee.getTypeInclusion());
 }

 @Test
 public void testDefaultImpl() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, String.class);
 Object defaultImpl = testee.getDefaultImpl();
 assertNotNull(defaultImpl);
 assertInstanceOf(String.class, defaultImpl);
 }

 @Test
 public void testFindDeserializer() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 String typeId = "someTypeId";
 JsonDeserializer<Object> result = testee.findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNull(result);
 }

 @Test
 public void testFindDeserializerWithValidTypeId() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, String.class);
 String typeId = "StringDeserializerId";
 JsonDeserializer<Object> result = testee.findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertInstanceOf(StringDeserializer.class, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerWithUnknownTypeId() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 String typeId = "UnknownTypeId";
 testee.findDeserializer(objectMapper.getDeserializationContext(), typeId);
 fail("Expected JsonMappingException to be thrown but it wasn't");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<Object> result = testee.findDefaultImplDeserializer(context);
 assertNull(result);
 }

 @Test
 public void testFindDefaultImplDeserializerWithDefaultImplSet() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, String.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<Object> result = testee.findDefaultImplDeserializer(context);
 assertInstanceOf(StringDeserializer.class, result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 String json = "{\"typeId\":\"StringDeserializerId\",\"value\":\"test\"}";
 Object result = testee.deserializeWithNativeTypeId(objectMapper.readTree(json), objectMapper.getDeserializationContext());
 assertEquals("test", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnknownNativeTypeId() throws JsonProcessingException {
 TypeDeserializerBase testee = new MyTypeDeserializerBase(Object.class, null, "typeProperty", true, null);
 String json = "{\"typeId\":\"UnknownTypeId\"}";
 testee.deserializeWithNativeTypeId(objectMapper.readTree(json), objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException to be thrown but it wasn't");
 }
}