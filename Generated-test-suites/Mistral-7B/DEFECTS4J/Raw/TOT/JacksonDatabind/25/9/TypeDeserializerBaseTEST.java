import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper mapper;
 private TypeDeserializerBase typeDeserializerBase;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 typeDeserializerBase = mapper.getTypeFactory()
 .constructType(TypeDeserializerBase.class);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = new BeanProperty.Std("testProperty", String.class);
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
 assertEquals(String.class, forProperty.getType());
 }

 @Test
 public void testGetTypeInclusion() {
 TypeDeserializerBase testTypeDeserializerBase = new TestTypeDeserializerBase();
 assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, testTypeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws IOException {
 TypeDeserializerBase testTypeDeserializerBase = new TestTypeDeserializerBase();
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TestTypeDeserializerBase", testTypeDeserializerBase.baseTypeName());
 }

 @Test
 public void testFindDeserializer() throws IOException {
 String typeId = "com.example.MyClass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeId() throws IOException {
 TypeIdResolver idResolver = new TypeIdResolverBase() {
 @Override
 public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
 fail("Should not be called");
 return null;
 }
 };
 String typeId = "unknownTypeId";
 typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId, idResolver, typeDeserializerBase._baseType);
 }

 @Test
 public void testToString() {
 TypeDeserializerBase testTypeDeserializerBase = new TestTypeDeserializerBase();
 String result = testTypeDeserializerBase.toString();
 assertTrue(result.contains("[TypeDeserializerBase"));
 }
}

class TestTypeDeserializerBase extends TypeDeserializerBase {

 public TestTypeDeserializerBase() {
 super(null, null, null, false, Object.class);
 }

 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return null;
 }

 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.WRAP_ONLY;
 }
}