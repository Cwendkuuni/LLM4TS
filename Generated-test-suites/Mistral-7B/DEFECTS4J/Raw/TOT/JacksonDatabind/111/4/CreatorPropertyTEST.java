package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private ObjectMapper mapper;
 private CreatorProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 property = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct("java.lang.String"),
 PropertyName.construct("testWrapper"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(null, null, "testParameter"),
 0,
 null,
 PropertyMetadata.empty());
 }

 @Test
 public void testBasicDeserialization() throws JsonProcessingException {
 Object beanInstance = new Object() {};
 property.inject(mapper.deserializationContext(), beanInstance);
 String value = (String) property.get(beanInstance);
 assertEquals("expected value", value);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValueId() throws JsonProcessingException {
 property.markAsIgnorable(); // for test purpose, ignore the property
 Object beanInstance = new Object() {};
 property.inject(mapper.deserializationContext(), beanInstance);
 assertNull(property.get(beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidInjectableValueType() throws JsonProcessingException {
 property._injectableValueId = new Object() {};
 Object beanInstance = new Object() {};
 property.inject(mapper.deserializationContext(), beanInstance);
 }

 @Test
 public void testFallbackSetter() throws JsonProcessingException {
 property.setFallbackSetter(new SettableBeanProperty(PropertyName.construct("testFallbackSetter"),
 JavaType.construct("java.lang.Integer"), null, null, null, null, null, null, PropertyMetadata.empty()) {
 @Override
 public void set(Object instance, Object value) throws IOException {
 ((Object) instance).hashCode = (Integer) value;
 }

 @Override
 public Object get(Object instance) {
 return ((Object) instance).hashCode;
 }
 });
 Object beanInstance = new Object() { int hashCode = 0; };
 property.inject(mapper.deserializationContext(), beanInstance);
 assertEquals(42, property.get(beanInstance).hashCode);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingFallbackSetter() throws JsonProcessingException {
 property.markAsIgnorable(); // for test purpose, ignore the property
 Object beanInstance = new Object() {};
 property.inject(mapper.deserializationContext(), beanInstance);
 assertNull(property.get(beanInstance));
 }

 @Test(expected = IOException.class)
 public void testInvalidJsonInput() throws JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("[\"invalid json\"]");
 Object beanInstance = new Object() {};
 property.deserializeAndSet(p, mapper.deserializationContext(), beanInstance);
 fail("Expected IOException");
 }
}