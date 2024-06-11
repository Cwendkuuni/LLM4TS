import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private static final Object MockedInstance = new Object();
 private static final String PROPERTY_NAME = "testProperty";
 private static final String VALID_JSON = "{\"testProperty\": \"validValue\"}";
 private static final String VALID_JSON_WITH_KEY = "{\"key\": \"testProperty\", \"value\": \"validValue\"}";
 private static final String INVALID_JSON = "{\"testProperty\": 123}";
 private static final String NULL_JSON = "{}";
 private static final String MISSING_JSON = "{}";

 @Test
 public void testDeserializeAndSet() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), Object.class, null, null, null);

 Object value = settableAnyProperty.deserialize(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class));
 assertEquals("validValue", value.toString());

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, PROPERTY_NAME);
 assertEquals("validValue", MockedInstance.getClass().getDeclaredField(PROPERTY_NAME).get(MockedInstance).toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullJson() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), Object.class, null, null, null);

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, PROPERTY_NAME);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithMissingJson() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), Object.class, null, null, null);

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, PROPERTY_NAME);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithInvalidJson() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), Object.class, null, null, null);

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, PROPERTY_NAME);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithUnknownType() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), new ClassType(int.class), null, null, null);

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, PROPERTY_NAME);
 }

 @Test
 public void testDeserializeAndSetWithKeyDeserializer() throws IOException {
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedField(Mockito.mock(Class.class), PROPERTY_NAME)),
 Mockito.mock(AnnotatedMember.class), Object.class, new SimpleKeyDeserializer(), null, null);

 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), MockedInstance, "key");
 assertEquals("testProperty", MockedInstance.getClass().getDeclaredField(PROPERTY_NAME).get(MockedInstance).toString());
 }
}