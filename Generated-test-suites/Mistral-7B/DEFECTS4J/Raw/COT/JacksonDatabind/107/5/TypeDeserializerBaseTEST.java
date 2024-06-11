import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private DeserializationContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 ObjectMapper.Builder builder = JsonMapperBuilder.builder();
 objectMapper = builder.build();
 context = objectMapper.getDeserializationContext();
 testee = new TypeDeserializerBase(
 new TypeReference<Object>() {},
 new TypeIdResolver() {},
 null,
 false,
 null) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@41c2a38a]";
 assertEquals(expected, testee.toString());
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertNull(testee.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertEquals("class java.lang.Object", testee.baseTypeName());
 }

 @Test
 public void testPropertyName() throws Exception {
 assertNull(testee.getPropertyName());
 }

 @Test
 public void testTypeIdResolver() throws Exception {
 assertEquals(TypeIdResolver.class, testee.getTypeIdResolver().getClass());
 }

 @Test
 public void testDefaultImpl() throws Exception {
 assertNull(testee.getDefaultImpl());
 }

 @Test
 public void testBaseType() throws Exception {
 JavaType expected = new TypeReference<Class<Object>>() {}.getType();
 assertEquals(expected, testee.baseType());
 }

 @Test
 public void testFindDeserializerWithNullTypeId() throws Exception {
 assertNull(testee._findDeserializer(context, null));
 }

 @Test
 public void testFindDeserializerWithValidTypeId() throws Exception {
 JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
 testee._deserializers.put("testTypeId", deserializer);
 Object result = testee._findDeserializer(context, "testTypeId");
 assertEquals(deserializer, result);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 Class<?> raw = Void.TYPE.getClass();
 assertNull(testee._findDefaultImplDeserializer(context));
 }

 @Test
 public void testFindDefaultImplDeserializerWhenDefaultImplIsNotNull() throws Exception {
 testee._defaultImplDeserializer = new StubJsonDeserializer<>();
 Class<?> raw = String.class.getClass();
 synchronized (testee._defaultImpl) {
 assertEquals(testee._defaultImplDeserializer, testee._findDefaultImplDeserializer(context));
 }
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullJsonParser() throws Exception {
 testee._deserializeWithNativeTypeId(null, context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws Exception {
 testee._deserializeWithNativeTypeId(new JsonParser(new StringReader("")), context);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypeId() throws Exception {
 testee._deserializeWithNativeTypeId(new JsonParser(new StringReader("{\"typeId\":\"InvalidTypeId\"}")), context);
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws Exception {
 assertThrows(JsonMappingException.class, () -> testee._handleUnknownTypeId(context, "UnknownTypeId"));
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 assertNull(testee._handleMissingTypeId(context, ""));
 }
}

class StubJsonDeserializer<T> implements JsonDeserializer<T> {
 @Override
 public T deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
 throw new UnsupportedOperationException();
 }
}