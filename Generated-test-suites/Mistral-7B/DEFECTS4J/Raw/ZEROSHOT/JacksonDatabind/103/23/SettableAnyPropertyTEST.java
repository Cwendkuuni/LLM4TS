import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SettableAnyPropertyTest {

 private static BeanProperty property;
 private static AnnotatedMember setter;
 private static JavaType type;
 private static KeyDeserializer keyDeserializer;
 private static JsonDeserializer<Object> valueDeserializer;
 private static TypeDeserializer valueTypeDeserializer;

 @BeforeClass
 public static void setup() throws JsonMappingException, JsonProcessingException {
 // Initialize the properties used in SettableAnyProperty constructor
 property = new BeanProperty(new AnnotatedClass(SettableAnyProperty.class), "testProperty", String.class);
 setter = new AnnotatedMethod(SettableAnyProperty.class.getDeclaredMethod("set", Object.class, Object.class, Object.class));
 type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
 keyDeserializer = null;
 valueDeserializer = new StringDeserializer();
 valueTypeDeserializer = null;
 }

 @Test
 public void testSettableAnyPropertyCreation() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertNotNull(settableAnyProperty);
 }

 @Test
 public void testSetterIsField() {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertTrue(settableAnyProperty._setterIsField);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = new HashMap<>();
 String propName = "test";
 String value = "testValue";
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

 JsonParser jsonParser = ObjectMapper.getDefaultInstance().getFactory().createParser("{\"test\":\"testValue\"}");
 settableAnyProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance, propName);
 assertEquals(value, instance.get(propName));
 }

 @Test
 public void testDeserialize() throws IOException {
 JsonParser jsonParser = ObjectMapper.getDefaultInstance().getFactory().createParser("{\"test\":\"testValue\"}");
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 Object value = settableAnyProperty.deserialize(jsonParser, new DeserializationContext());
 assertEquals("testValue", value);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new HashMap<>();
 String propName = "test";
 String value = "testValue";
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

 settableAnyProperty.set(instance, propName, value);
 assertEquals(value, instance.get(propName));
 }
}