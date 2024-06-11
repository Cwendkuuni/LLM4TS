import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper objectMapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("newProperty", "value1");
 settableAnyProperty = objectMapper.readValue(node.toString(), SettableAnyProperty.class);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new Object();
 String propName = "newProperty";
 String expectedValue = "value1";

 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(node.toString()), objectMapper.getDeserializationContext(), instance, propName);
 assertEquals(expectedValue, settableAnyProperty.getProperty().get(instance));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullInstance() throws IOException {
 Object instance = null;
 String propName = "newProperty";
 String expectedValue = "value1";

 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(node.toString()), objectMapper.getDeserializationContext(), instance, propName);
 fail("Expected JsonMappingException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithInvalidType() throws IOException {
 Object instance = new int[1];
 String propName = "newProperty";
 String expectedValue = "value1";

 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(node.toString()), objectMapper.getDeserializationContext(), instance, propName);
 fail("Expected JsonMappingException but got none");
 }

 @Test
 public void testDeserializeAndSetWithValueDeserializer() throws IOException {
 Object instance = new Object();
 String propName = "newProperty";
 JsonNode nodeWithValue = JsonNodeFactory.instance.objectNode();
 nodeWithValue.put("value", "value2");
 settableAnyProperty = new SettableAnyProperty(settableAnyProperty._property, settableAnyProperty._setter, settableAnyProperty._type, settableAnyProperty._keyDeserializer, null, new ValueDeserializer());
 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(nodeWithValue.toString()), objectMapper.getDeserializationContext(), instance, propName);
 assertEquals("value2", settableAnyProperty.getProperty().get(instance));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 String propName = "newProperty";
 String expectedValue = "value2";
 settableAnyProperty.set(instance, propName, "value2");
 assertEquals(expectedValue, settableAnyProperty.getProperty().get(instance));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithNullInstance() throws IOException {
 Object instance = null;
 String propName = "newProperty";
 String expectedValue = "value2";

 settableAnyProperty.set(instance, propName, "value2");
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithNullValue() throws IOException {
 Object instance = new Object();
 String propName = "newProperty";

 settableAnyProperty.set(instance, propName, null);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithInvalidType() throws IOException {
 Object instance = new int[1];
 String propName = "newProperty";
 String expectedValue = "value3";

 settableAnyProperty.set(instance, propName, expectedValue);
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testSetWithMissingKeyDeserializer() throws IOException {
 Object instance = new Object();
 String propName = "missingProperty";

 settableAnyProperty = new SettableAnyProperty(settableAnyProperty._property, settableAnyProperty._setter, settableAnyProperty._type, null, settableAnyProperty._valueDeserializer, settableAnyProperty._valueTypeDeserializer);
 settableAnyProperty.set(instance, propName, "value4");
 fail("Expected JsonMappingException but got none");
 }

 @Test
 public void testToString() {
 String expectedValue = "settable any property on [class Object]";
 assertEquals(expectedValue, settableAnyProperty.toString());
 }
}