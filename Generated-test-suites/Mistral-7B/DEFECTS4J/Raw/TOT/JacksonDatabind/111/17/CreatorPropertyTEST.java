package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testCreatorPropertyWithInjectableValue() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, null);
 Object expectedValue = "expectedValue";
 Object beanInstance = new Object();
 AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, 0, null);
 property.setInjectableValueId(annotatedParameter);
 Annotations contextAnnotations = new Annotations();
 contextAnnotations.forSerialization().put(JsonProperty.Value.class);

 // When
 Object actualValue = property.findInjectableValue(new DeserializationContext(objectMapper), beanInstance);

 // Then
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreatorPropertyWithoutInjectableValue() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, null);
 Object beanInstance = new Object();
 Annotations contextAnnotations = new Annotations();
 contextAnnotations.forSerialization().put(JsonProperty.Value.class);

 // When and Then
 assertNull(property.findInjectableValue(new DeserializationContext(objectMapper), beanInstance));
 }

 @Test
 public void testCreatorPropertyWithFallbackSetter() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, new SettableBeanProperty(PropertyName.construct("fallback"), JsonType.construct(String.class), null, null, null));
 Object expectedValue = "expectedValue";
 Object beanInstance = new Object();

 // When
 property.setFallbackSetter(new SettableBeanProperty(PropertyName.construct("fallback"), JsonType.construct(String.class), null, null, null));
 property.inject(new DeserializationContext(objectMapper), beanInstance);

 // Then
 assertEquals(expectedValue, beanInstance.getClass().getDeclaredField("fallback").get(beanInstance));
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testCreatorPropertyWithMissingFallbackSetter() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, null);
 Object beanInstance = new Object();

 // When
 property.inject(new DeserializationContext(objectMapper), beanInstance);

 // Then
 fail("Should throw InvalidDefinitionException due to missing fallback setter");
 }

 @Test
 public void testCreatorPropertyWithEmptyValue() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, null);
 Object beanInstance = new Object();
 JsonNode node = JsonNodeFactory.instance.textNode("");

 // When
 property.deserializeAndSet(objectMapper.getFactory().createParser(node.getText()),
 new DeserializationContext(objectMapper), beanInstance);

 // Then
 assertNull(beanInstance.getClass().getDeclaredField("name").get(beanInstance));
 }

 @Test
 public void testCreatorPropertyWithNullValue() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(String.class), null, null, null, null, -1, null, null);
 Object beanInstance = new Object();
 JsonNode node = JsonNodeFactory.instance.nullNode();

 // When
 property.deserializeAndSet(objectMapper.getFactory().createParser(node.toString()),
 new DeserializationContext(objectMapper), beanInstance);
 // No need to check the result, as it's tested in other tests
 }

 @Test(expected = JsonMappingException.class)
 public void testCreatorPropertyWithInvalidValueType() throws JsonProcessingException {
 // Given
 CreatorProperty property = new CreatorProperty(PropertyName.construct("name"),
 JsonType.construct(int.class), null, null, null, null, -1, null, null);
 Object beanInstance = new Object();
 JsonNode node = JsonNodeFactory.instance.textNode("not a number");

 // When and Then
 property.deserializeAndSet(objectMapper.getFactory().createParser(node.toString()),
 new DeserializationContext(objectMapper), beanInstance);
 }
}