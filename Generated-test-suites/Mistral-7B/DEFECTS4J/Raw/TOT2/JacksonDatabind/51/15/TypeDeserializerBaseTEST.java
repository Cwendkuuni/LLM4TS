import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase(Map.class, mapper.getTypeIdResolver(), "", false, Map.class);
 testee = new TypeDeserializerBase(typeDeserializerBase, null);
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty.Std("testProperty", String.class);
 TypeDeserializer typeDeserializer = testee.forProperty(property);
 assertNotNull(typeDeserializer);
 assertEquals(String.class, typeDeserializer.getType());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertEquals("java.util.Map", testee.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 assertEquals("", testee.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 assertEquals(testee._idResolver, testee.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImpl() throws Exception {
 assertEquals(Map.class, testee.getDefaultImpl());
 }

 @Test
 public void testToString() throws Exception {
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.util.Map; id-resolver:";
 assertEquals(expected, testee.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testForPropertyNullProperty() throws Exception {
 BeanProperty property = null;
 TypeDeserializer typeDeserializer = testee.forProperty(property);
 fail("Test should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullType() throws Exception {
 TypeDeserializer typeDeserializer = testee.forProperty(null);
 fail("Test should throw IllegalArgumentException");
 }

 @Test
 public void testFindDeserializerNullContext() throws Exception {
 assertNull(testee._findDeserializer(null, "TypeId"));
 }

 @Test
 public void testFindDeserializerNullTypeId() throws Exception {
 assertNull(testee._findDeserializer(new TestContext(), null));
 }

 @Test
 public void testFindDeserializerValidTypeId() throws Exception {
 String typeId = "MapType";
 JsonDeserializer<Object> deserializer = new TestMapDeserializer();
 testee._deserializers.put(typeId, deserializer);
 JsonDeserializer<Object> foundDeserializer = testee._findDeserializer(new TestContext(), typeId);
 assertEquals(deserializer, foundDeserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNotFoundTypeId() throws Exception {
 testee._deserializers.clear();
 assertNull(testee._findDeserializer(new TestContext(), "MapType"));
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 assertNotNull(testee._findDefaultImplDeserializer(new TestContext()));
 }

 @Test
 public void testFindDefaultImplDeserializerNullContext() throws Exception {
 assertNull(testee._findDefaultImplDeserializer(null));
 }

}

class TestContext extends DeserializationContext {
 @Override
 public Object readValue(Class<?> type, JsonParser p) throws IOException {
 return null;
 }

 @Override
 public String findRootValue(JsonParser p) throws IOException {
 return null;
 }

 @Override
 public JavaType findTypeDeserializer(Class<?> type) {
 return null;
 }

 @Override
 public JavaType findValueType(Class<?> type) {
 return null;
 }

 @Override
 public BeanDescription findBeanDescription(Class<?> beanType) {
 return null;
 }

 @Override
 public Object findRootValue(JsonNode node) throws IOException {
 return null;
 }

 @Override
 public boolean isEnabled(DeserializationFeature feature) {
 return true;
 }

 @Override
 public void enable(DeserializationFeature feature) {
}

 @Override
 public void disable(DeserializationFeature feature) {
}

 @Override
 public DeserializationFeature findFeature(DeserializationFeature feature) {
 return null;
 }

 @Override
 public void findValueIdEvent(JsonParser p) throws IOException {
}

 @Override
 public void handleUnexpectedValue(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleMissingValue(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleEmptyString(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleIncompleteNumber(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleNumberFormatException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, NumberFormatException e) throws IOException {
}

 @Override
 public void handleUnexpectedToken(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleMissingValueException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, MissingValueException e) throws IOException {
}

 @Override
 public void handleInvalidDefinitionException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, JsonMappingException e) throws IOException {
}

 @Override
 public void handleInvalidTypeIdException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, JsonMappingException e) throws IOException {
}

 @Override
 public void handleUnexpectedTokenException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, IOException e) throws IOException {
}

 @Override
 public void handleUnexpectedValueException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, JsonMappingException e) throws IOException {
}

 @Override
 public void handleParsingException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, ParsingException e) throws IOException {
}

 @Override
 public void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, String name, int sr) throws IOException {
}

 @Override
 public void handleMissingProperty(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, String name) throws IOException {
}

 @Override
 public void handleNoClassDefinition(Class<?> type, MapperFeature.Standard deserializerProviders) throws IOException {
}

 @Override
 public void handleUnknownKeyException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, String name) throws IOException {
}

 @Override
 public void handleValueReused(Object value, JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleEmptyArray(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type) throws IOException {
}

 @Override
 public void handleUnresolvableValue(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, Object value) throws IOException {
}

 @Override
 public void handleValueNotFound(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, String name) throws IOException {
}

 @Override
 public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
 return null;
 }

 @Override
 public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type) throws IOException {
 return null;
 }

 @Override
 public void handleWithAssertions(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, Object value) throws IOException {
}

 @Override
 public Object handleSuppressedException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, Object value, Class<?> valueType, IOException e) throws IOException {
 return null;
 }

 @Override
 public Object handleSuppressedException(JsonParser p, DeserializationContext ctxt, java.lang.Class<?> type, Object value, Class<?> valueType, Throwable t) throws IOException {
 return null;
 }
}

class TestMapDeserializer extends JsonDeserializer<Map<Object, Object>> {
 @Override
 public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new HashMap<>();
 }
}

class TestTypeDeserializerBase extends TypeDeserializer {
 public TestTypeDeserializerBase(Class<?> baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Override
 protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>();
}