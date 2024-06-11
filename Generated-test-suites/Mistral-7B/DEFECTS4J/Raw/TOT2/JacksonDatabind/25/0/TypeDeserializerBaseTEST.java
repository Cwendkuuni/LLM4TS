import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 typeDeserializer = new TypeDeserializerBase(
 JsonTypeFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 String.class) {
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.EXISTING_PROPERTY;
 }
 };
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBasicFunctionality() throws Exception {
 String expected = "expectedValue";
 JsonNode node = objectMapper.createObjectNode();
 node.put("typeId", "StringTypeId");
 node.put("value", expected);

 String actual = typeDeserializer.deserialize(_context, node, _property.getType());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownTypeId() throws Exception {
 String nodeAsString = "{\"typeId\":\"UnknownTypeId\"}";
 JsonNode node = objectMapper.readTree(nodeAsString);
 typeDeserializer.deserialize(_context, node, _property.getType());
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testNullInput() throws Exception {
 JsonNode node = null;
 try {
 typeDeserializer.deserialize(_context, node, _property.getType());
 fail("Expected IOException to be thrown");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDefaultImplDeserializer() throws Exception {
 typeDeserializer = new TypeDeserializerBase(
 JsonTypeFactory.getDefaultInstance().constructType(String.class),
 new TypeIdResolverBase(),
 "typePropertyName",
 true,
 null) {
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.EXISTING_PROPERTY;
 }
 };

 Object actual = typeDeserializer.deserialize(_context, null, _property.getType());
 assertNull(actual);
 }
}