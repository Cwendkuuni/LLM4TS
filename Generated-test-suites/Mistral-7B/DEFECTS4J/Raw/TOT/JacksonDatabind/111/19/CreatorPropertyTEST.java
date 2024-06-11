import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreatorPropertyTest {

 private ObjectMapper objectMapper;
 private CreatorProperty creatorProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 creatorProperty = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.constructRawType(String.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.noAnnotation(String.class),
 0,
 null,
 PropertyMetadata.empty());
 }

 @Test
 public void testNormalDeserialization() throws JsonProcessingException {
 String testValue = "Test Value";
 ObjectNode input = objectMapper.createObjectNode();
 input.put("testProperty", testValue);
 JsonNode tree = objectMapper.readTree(input);
 Object beanInstance = new Object();
 creatorProperty.deserializeAndSet(objectMapper.findRootValueAsIterativeParser(tree),
 new DeserializationContext(objectMapper), beanInstance);
 assertEquals(testValue, creatorProperty.get(beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValueId() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("testProperty", "Test Value");
 JsonNode tree = objectMapper.readTree(input);
 Object beanInstance = new Object();
 creatorProperty.deserializeAndSet(objectMapper.findRootValueAsIterativeParser(tree),
 new DeserializationContext(objectMapper), beanInstance);
 fail("Should have thrown JsonMappingException with no injectable value id");
 }

 @Test
 public void testInjectValue() throws JsonProcessingException {
 ObjectNode input = objectMapper.createObjectNode();
 JsonNode injectNode = objectMapper.createObjectNode();
 injectNode.put("value", "Injected Value");
 input.set("testProperty", injectNode);
 Object beanInstance = new Object();
 creatorProperty.inject(new DeserializationContext(objectMapper), beanInstance);
 assertEquals("Injected Value", creatorProperty.get(beanInstance));
 }

 @Test
 public void testMissingFallbackSetter() throws JsonProcessingException {
 JsonParser p = objectMapper.getFactory().createParser("{}");
 DeserializationContext ctxt = new DeserializationContext(objectMapper);
 Object beanInstance = new Object();
 creatorProperty._verifySetter(); // will throw exception
 creatorProperty.deserializeAndSet(p, ctxt, beanInstance);
 fail("Should have thrown InvalidDefinitionException with missing setter");
 }

 @Test
 public void testIgnoreProperty() throws JsonProcessingException {
 creatorProperty.markAsIgnorable();
 ObjectNode input = objectMapper.createObjectNode();
 Object beanInstance = new Object();
 creatorProperty.deserializeAndSet(objectMapper.findRootValueAsIterativeParser(input),
 new DeserializationContext(objectMapper), beanInstance);
 assertNull(creatorProperty.get(beanInstance));
 }
}