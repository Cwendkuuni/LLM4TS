import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /**
 * Test for constructor without property.
 */
 @Test
 public void testTypeDeserializerBaseConstructorWithoutProperty() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 assertNotNull(deserializer);
 }

 /**
 * Test for constructor with property.
 */
 @Test
 public void testTypeDeserializerBaseConstructorWithProperty() throws Exception {
 BeanProperty property = new BeanProperty.Std("myProperty", String.class);
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "myProperty",
 false,
 null,
 property
 );
 assertNotNull(deserializer);
 assertEquals("myProperty", deserializer.getPropertyName());
 }

 /**
 * Test for getTypeInclusion method.
 */
 @Test
 public void testGetTypeInclusion() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, deserializer.getTypeInclusion());
 }

 /**
 * Test for baseTypeName method.
 */
 @Test
 public void testBaseTypeName() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 assertEquals("com.example.MyBaseType", deserializer.baseTypeName());
 }

 /**
 * Test for toString method.
 */
 @Test
 public void testToString() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 assertEquals("[com.example.TypeDeserializerBase; base-type:com.example.MyBaseType; id-resolver:null]", deserializer.toString());
 }

 /**
 * Test for finding deserializer with a known type id.
 */
 @Test
 public void testFindDeserializerWithKnownTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 String typeId = "myTypeId";
 JsonDeserializer<Object> deserializerFound = deserializer._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertNotNull(deserializerFound);
 }

 /**
 * Test for finding deserializer with an unknown type id.
 */
 @Test(expected = IOException.class)
 public void testFindDeserializerWithUnknownTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 String typeId = "unknownTypeId";
 deserializer._findDeserializer(mapper.getDeserializationContext(), typeId);
 fail("Expected an IOException but no exception was thrown.");
 }

 /**
 * Test for finding default impl deserializer.
 */
 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 JsonDeserializer<Object> deserializerFound = deserializer._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNotNull(deserializerFound);
 }

 /**
 * Test deserialization with native type id.
 */
 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 TypeDeserializerBase deserializer = new TypeDeserializerBase(
 Class.forName("com.example.MyBaseType"),
 null,
 "",
 false,
 null
 );
 String json = "{\"@type\":\"com.example.MyType\"}";
 JsonParser parser = mapper.getFactory().createParser(json);
 Object result = deserializer._deserializeWithNativeTypeId(parser, mapper.getDeserializationContext());
 assertNotNull(result);
 }
}