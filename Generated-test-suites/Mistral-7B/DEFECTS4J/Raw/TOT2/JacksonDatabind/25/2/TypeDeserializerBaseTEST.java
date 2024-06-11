import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase.TypeDeserializerForTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;
 private TypeDeserializerBase typeDeserializerBase;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 typeDeserializerBase = new TypeDeserializerForTest();
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 BeanProperty property = new BeanProperty(null, "typePropertyName");
 typeDeserializerBase = typeDeserializerBase.forProperty(property);
 assertNotNull(typeDeserializerBase.getDefaultImpl());
 }

 @Test
 public void testFindDeserializerNullTypeId() throws JsonProcessingException {
 String json = "{}";
 JsonParser jsonParser = new JsonFactory().createParser(json);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object deserializedObject = typeDeserializerBase.deserialize(jsonParser, context);
 assertEquals(null, deserializedObject);
 }

 @Test
 public void testFindDeserializerValidTypeId() throws JsonProcessingException {
 String json = "{\"typeId\":\"com.example.TestClass\"}";
 JsonParser jsonParser = new JsonFactory().createParser(json);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object deserializedObject = typeDeserializerBase.deserialize(jsonParser, context);
 assertNotNull(deserializedObject);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerUnknownTypeId() throws JsonProcessingException {
 String json = "{\"typeId\":\"com.unknown.TestClass\"}";
 JsonParser jsonParser = new JsonFactory().createParser(json);
 DeserializationContext context = objectMapper.getDeserializationContext();
 typeDeserializerBase.deserialize(jsonParser, context);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 String json = "{}";
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object deserializedObject = typeDeserializerBase._findDefaultImplDeserializer(context).deserialize(jsonParser, context);
 assertEquals(null, deserializedObject);
 }

 @Test
 public void testDeserializeWithNativeTypeIdNull() throws JsonProcessingException {
 String json = "{}";
 JsonParser jsonParser = new JsonFactory().createParser(json);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
 assertEquals(null, deserializedObject);
 }

 @Test
 public void testDeserializeWithNativeTypeIdString() throws JsonProcessingException {
 String json = "{\"typeId\":\"com.example.TestClass\"}";
 JsonParser jsonParser = new JsonFactory().createParser(json);
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "com.example.TestClass");
 assertNotNull(deserializedObject);
 }
}