import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private CreatorProperty creatorProperty;
 private ObjectNode objectNode;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws JsonMappingException, JsonProcessingException {
 objectMapper = new ObjectMapper();
 objectNode = objectMapper.createObjectNode();
 creatorProperty = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct(String.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.empty());
 }

 @Test
 public void testConstructor() {
 CreatorProperty testCreatorProperty = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct(String.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.empty());
 assertEquals(creatorProperty.getName(), testCreatorProperty.getName());
 assertEquals(creatorProperty.getType(), testCreatorProperty.getType());
 }

 @Test
 public void testSetFallbackSetter() {
 SettableBeanProperty fallbackSetterMock = new SettableBeanProperty(
 PropertyName.construct("testFallbackSetter"),
 JavaType.construct(Integer.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.empty());
 creatorProperty.setFallbackSetter(fallbackSetterMock);
 assertNotNull(creatorProperty.getFallbackSetter());
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetThrowsIOException() throws JsonMappingException, JsonProcessingException {
 creatorProperty.deserializeAndSet(objectMapper.getFactory().createParser("{ \"value\": \"testValue\" }"),
 objectMapper,
 new Object());
 }

 @Test
 public void testDeserializeAndSetSuccess() throws JsonMappingException, JsonProcessingException {
 creatorProperty.deserializeAndSet(objectMapper.getFactory().createParser("{ \"value\": \"testValue\" }"),
 objectMapper,
 new Object());
 assertEquals("testValue", creatorProperty.get(new Object()));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindInjectableValueThrowsJsonMappingException() throws JsonMappingException, JsonProcessingException {
 creatorProperty.findInjectableValue(objectMapper, new Object());
 }

 @Test
 public void testFindInjectableValueSuccess() throws JsonMappingException, JsonProcessingException {
 Object injectableValue = new Object();
 creatorProperty._injectableValueId = injectableValue;
 Object injectedValue = creatorProperty.findInjectableValue(objectMapper, new Object());
 assertEquals(injectableValue, injectedValue);
 }

 @Test(expected = IOException.class)
 public void testInjectThrowsIOException() throws JsonMappingException, JsonProcessingException {
 creatorProperty.inject(objectMapper, new Object());
 }

 @Test
 public void testInjectSuccess() throws JsonMappingException, JsonProcessingException {
 Object injectableValue = new Object();
 creatorProperty._injectableValueId = injectableValue;
 creatorProperty.inject(objectMapper, new Object());
 assertEquals(injectableValue, creatorProperty.get(new Object()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMarkAsIgnorableThrowsIllegalArgumentException() {
 creatorProperty.markAsIgnorable();
 }

 @Test
 public void testMarkAsIgnorableSuccess() {
 creatorProperty.markAsIgnorable();
 assertTrue(creatorProperty.isIgnorable());
 }
}