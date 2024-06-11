import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import junit.framework.TestCase;

public class TypeDeserializerBaseTest extends TestCase {

 private static final ObjectMapper MAPPER = new ObjectMapper()
          .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

 @SuppressWarnings("unchecked")
 @Test
 public void testForProperty() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 BeanProperty property = new BeanProperty.Std("propertyName", String.class);

 // When
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);

 // Then
 assertTrue(forProperty instanceof MyTypeDeserializer);
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();

 // When & Then
 assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();

 // When
 String baseTypeName = typeDeserializerBase.baseTypeName();

 // Then
 assertEquals("com.example.MyClass", baseTypeName);
 }

 @Test
 public void testDefaultImplDeserializer() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();

 // When
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._defaultImplDeserializer;

 // Then
 assertNotNull(defaultImplDeserializer);
 }

 @Test
 public void testFindDeserializer() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 String typeId = "com.example.MyClass";

 // When
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);

 // Then
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithUnknownTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 String typeId = "UnknownTypeId";

 // When
 typeDeserializerBase._findDeserializer(ctxt, typeId);

 // Then
 fail("Exception expected");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

 // Then
 assertNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("[]");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 fail("Exception expected");
 }

 @Test
 public void testDeserializeWithUnknownTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("{\"typeId\":\"UnknownTypeId\"}");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 assertNull(result);
 }

 @Test
 public void testDeserializeWithValidTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("{\"typeId\":\"com.example.MyClass\"}");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
 // Then
 assertInstanceOf(MyClass.class, result);
 }

 // Edge cases and errors

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypeIdFormat() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("{\"typeId\":123}");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 fail("Exception expected");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEmptyTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("{\"typeId\":\"\"}");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 fail("Exception expected");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithMissingTypeId() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 JsonParser jp = MAPPER.getFactory().createParser("[]");
 DeserializationContext ctxt = MAPPER.getDeserializationContext();

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 fail("Exception expected");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullJsonParser() throws Exception {
 // Given
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonParser jp = null;

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

 // Then
 fail("Exception expected");
 }
}