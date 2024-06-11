import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 property = new CreatorProperty(
 new PropertyName("testProperty"),
 JavaType.constructUntyped(),
 new PropertyName("wrapperName"),
 null,
 null,
 null,
 0,
 null,
 new PropertyMetadata()
 );
 mapper = new ObjectMapper();
 }

 @Test
 public void testBasicFunctionality() throws JsonProcessingException {
 Object value = "testValue";
 property.setValueDeserializer(mapper.findValueDeserializer(property.getType(), null));
 property.set(new Object(), value);
 assertEquals(value, mapper.readerForUpdating(new Object()).findValueAs(property.getType()));
 }

 @Test
 public void testInjectValue() throws JsonProcessingException {
 Object injectValue = new Object();
 property.setInjectableValueId("testId");
 property.markAsIgnorable(); // ignore it during deserialization
 Object beanInstance = new Object();
 property.inject(mapper.getDeserializationContext().injectables(), beanInstance);
 assertEquals(injectValue, mapper.readerForUpdating(beanInstance).findValueAs(property.getType()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingSetter() throws JsonProcessingException {
 property.markAsIgnorable(); // ignore it during deserialization
 property.setFallbackSetter(null);
 property.inject(mapper.getDeserializationContext().injectables(), new Object());
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 property.setValueDeserializer(mapper.findValueDeserializer(property.getType(), null));
 property.set(new Object(), null);
 assertNull(mapper.readerForUpdating(new Object()).findValueAs(property.getType()));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidInjectValueId() throws JsonProcessingException {
 property.setInjectableValueId("invalidId");
 property.markAsIgnorable(); // ignore it during deserialization
 Object beanInstance = new Object();
 property.inject(mapper.getDeserializationContext().injectables(), beanInstance);
 }

 @Test
 public void testTypeDeserializer() throws JsonProcessingException {
 TypeDeserializer typeDeserializer = new StubTypeDeserializer();
 property.setValueDeserializer(typeDeserializer);
 property.set(new Object(), "testValue");
 assertEquals("testValue", typeDeserializer.deserialize(mapper.getFactory(), null, null, null, null, null, null));
 }

 @Test
 public void testSetFallbackSetter() throws JsonProcessingException {
 SettableBeanProperty fallbackSetter = new SettableBeanProperty(
 new PropertyName("testFallbackSetter"),
 JavaType.constructUntyped(),
 new PropertyName("wrapperNameFallbackSetter"),
 null,
 null,
 null,
 0,
 null,
 new PropertyMetadata()
 );
 property.setFallbackSetter(fallbackSetter);
 Object beanInstance = new Object();
 property.set(beanInstance, "testValue");
 assertEquals("testValue", mapper.readerForUpdating(beanInstance).findValueAs(fallbackSetter.getType()));
 }
}