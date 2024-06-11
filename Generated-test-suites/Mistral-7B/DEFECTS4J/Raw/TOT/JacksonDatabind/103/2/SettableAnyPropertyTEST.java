import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper mapper;
 private SettableAnyProperty property;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 property = mapper.findSpecialValueDeserializer(null, SettableAnyProperty.class);
 }

 @Test
 public void testSettableAnyPropertyDeserialization() throws IOException {
 Object testInstance = new Object();
 String testPropertyName = "testProperty";
 JsonParser testParser = mapper.parserForObject("{\"testProperty\": 1}");

 property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testInstance, testPropertyName);

 Map<Object, Object> testMap = (Map<Object, Object>) ReflectionTestUtils.getFieldValue(ReflectionTestUtils.findField(testInstance, "_properties"));
 assertEquals(1, testMap.size());
 assertEquals(1, testMap.get(testPropertyName));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializationWithNullValue() throws IOException {
 Object testInstance = new Object();
 String testPropertyName = "testProperty";
 JsonParser testParser = mapper.parserForObject("{\"testProperty\": null}");

 property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testInstance, testPropertyName);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializationWithInvalidType() throws IOException {
 Object testInstance = new Object();
 String testPropertyName = "testProperty";
 JsonParser testParser = mapper.parserForObject("{\"testProperty\": [1, 2, 3]}");

 property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testInstance, testPropertyName);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSettableAnyPropertyDeserializationWithValueDeserializer() throws IOException {
 Object testInstance = new Object();
 String testPropertyName = "testProperty";
 JsonParser testParser = mapper.parserForObject("{\"testProperty\": 42.3}");
 JsonDeserializer<Double> valueDeserializer = new StandardDeserializer<Double>(Double.class);
 property = property.withValueDeserializer(valueDeserializer);

 assertEquals(42.3, property.deserialize(testParser, mapper.getDeserializationContext()));
 property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testInstance, testPropertyName);
 Map<Object, Object> testMap = (Map<Object, Object>) ReflectionTestUtils.getFieldValue(ReflectionTestUtils.findField(testInstance, "_properties"));
 assertEquals(1, testMap.size());
 assertEquals(42.3, testMap.get(testPropertyName));
 }
}