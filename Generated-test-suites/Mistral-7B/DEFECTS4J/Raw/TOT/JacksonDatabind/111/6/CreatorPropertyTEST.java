import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private ObjectNode testObject;
 private ObjectMapper mapper;
 private CreatorProperty property;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 testObject = mapper.createObjectNode();
 property = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct(String.class),
 null,
 null,
 null,
 null,
 0,
 null,
 PropertyMetadata.STANDARD);
 }

 @Test
 public void testNormalDeserialization() throws Exception {
 testObject.put("testProperty", "Test Value");
 JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
 @SuppressWarnings("unchecked")
 SettableBeanPropertyDeserializer deserializer = (SettableBeanPropertyDeserializer) introspector.findPropertyDeserializer(mapper, property);
 JsonDeserializer<String> valueDeserializer = deserializer.getValueDeserializer();
 property.setValueDeserializer(valueDeserializer);
 ObjectReader reader = mapper.readerFor(property.getType());
 Object result = reader.readValue(testObject);
 assertEquals("Test Value", ((Map<String, Object>) result).get("testProperty"));
 }

 @Test(expected = IOException.class)
 public void testMissingValueDeserializer() throws Exception {
 ObjectReader reader = mapper.readerFor(property.getType());
 reader.readValue(testObject);
 fail("should throw IOException");
 }

 @Test
 public void testInjectableValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"injectValue\": 123}");
 p.nextToken();
 property.setInjectableValueId("injectValue");
 Object result = property.inject(mapper, null);
 assertEquals(123, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValueId() throws Exception {
 property.setInjectableValueId(null);
 DeserializationContext context = mapper.getDeserializationContext();
 property.inject(context, null);
 fail("should throw JsonMappingException");
 }
}