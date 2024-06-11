import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectBeanContext context;
 private Object beanInstance;

 @Before
 public void setup() throws JsonProcessingException {
 JsonFactory factory = new JsonFactory();
 context = new ObjectMapper().getDeserializationContext().injectableValues();
 ObjectReader reader = factory.createValueReader(getClass().getResourceAsStream("/testData.json"));
 ObjectNode node = (ObjectNode) reader.readValueAsTree();
 property = new CreatorProperty(
 new PropertyName("testProperty"),
 ObjectTypeFactory.getDefaultInstance().constructType(String.class),
 null,
 null,
 context,
 null,
 0,
 null,
 new PropertyMetadata()
 );
 beanInstance = new TestObject();
 }

 @Test
 public void testBasicDeserialization() throws IOException {
 String value = "testValue";
 JsonParser parser = factory.createParser(getClass().getResourceAsStream("/testData.json"));
 parser.nextValue(); // skip root element
 parser.nextValue(); // skip array element
 property.deserializeAndSet(parser, context, beanInstance);
 assertEquals(value, property.get(beanInstance));
 }

 @Test
 public void testNullValue() throws IOException {
 JsonParser parser = factory.createParser(getClass().getResourceAsStream("/testData.json"));
 parser.nextValue(); // skip root element
 parser.nextNull();
 property.deserializeAndSet(parser, context, beanInstance);
 assertNull(property.get(beanInstance));
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws IOException {
 property.setFallbackSetter(null);
 JsonParser parser = factory.createParser(getClass().getResourceAsStream("/testData.json"));
 parser.nextValue(); // skip root element
 parser.nextValue(); // skip array element
 property.deserializeAndSet(parser, context, beanInstance);
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingInjectableValueId() throws IOException {
 property.setInjectableValueId(null);
 JsonParser parser = factory.createParser(getClass().getResourceAsstream("/testData.json"));
 JsonToken token = parser.nextValue();
 parser.nextValue(); // skip array element
 property.inject(parser, context, beanInstance);
 }

 @Test
 public void testInjectValue() throws IOException {
 String injectedValue = "injectedValue";
 property.setInjectableValueId("testInjectValueId");
 JsonParser parser = factory.createParser(getClass().getResourceAsStream("/testData.json"));
 parser.nextValue(); // skip root element
 parser.nextString("injectedValue");
 property.inject(parser, context, beanInstance);
 assertEquals(injectedValue, property.get(beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValue() throws IOException {
 property.setInjectableValueId("nonExistentInjectValueId");
 JsonParser parser = factory.createParser(getClass().getResourceAsStream("/testData.json"));
 JsonToken token = parser.nextValue();
 parser.nextValue(); // skip array element
 property.inject(parser, context, beanInstance);
 }

 @Test
 public void testSetValue() throws IOException {
 String valueToSet = "valueToSet";
 property.set(beanInstance, valueToSet);
 assertEquals(valueToSet, property.get(beanInstance));
 }

 @Test
 public void testSetNullValue() {
 property.set(beanInstance, null);
 assertNull(property.get(beanInstance));
 }

 @Test
 public void testToString() {
 assertEquals("[creator property, name 'testProperty'; inject id 'null']", property.toString());
 }
}

class TestObject {
 // empty class for testing
}