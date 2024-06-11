package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Before
 public void setup() {
 MAPPER.setAnnotationIntrospector(new AnnotationIntrospector() {
 @Override
 public AnnotatedMember findPropertyAnnotation(Class<?> cls, java.lang.reflect.AnnotatedElement annotatedElement, String propertyName) {
 return mock(AnnotatedMember.class);
 }

 @Override
 public AnnotatedParameter findParameterAnnotation(Class<?> controllerClass, java.lang.reflect.Method method, int paramNum) {
 return mock(AnnotatedParameter.class);
 }
 });
 }

 @Test
 public void testCreatorProperty() throws IOException, JsonMappingException {
 // Test construction with valid arguments
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("test_property"),
 MAPPER.getTypeFactory().constructType(int.class),
 PropertyName.construct("wrapperName"),
 mock(TypeDeserializer.class),
 Annotations.empty(),
 mock(AnnotatedParameter.class),
 1,
 null,
 PropertyMetadata.builder().build());

 assertEquals("test_property", property.getName().asString());
 assertEquals(int.class, property.getType());
 assertNull(property._annotated);
 assertEquals(1, property._creatorIndex);
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);

 // Test construction with null arguments
 property = new CreatorProperty(
 null,
 null,
 null,
 null,
 null,
 null,
 -1,
 null,
 null);

 assertNull(property);

 // Test setter method
 property = new CreatorProperty(
 PropertyName.construct("test_property"),
 MAPPER.getTypeFactory().constructType(int.class),
 PropertyName.construct("wrapperName"),
 mock(TypeDeserializer.class),
 Annotations.empty(),
 mock(AnnotatedParameter.class),
 1,
 null,
 PropertyMetadata.builder().build());
 property.setFallbackSetter(mock(SettableBeanProperty.class));
 SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
 when(fallbackSetter.getName()).thenReturn(PropertyName.construct("test_fallback_setter"));
 property.setFallbackSetter(fallbackSetter);
 assertEquals(fallbackSetter, property._fallbackSetter);

 // Test isIgnorable method
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());

 // Test findInjectableValue method
 DeserializationContext context = mock(DeserializationContext.class);
 Object beanInstance = new Object();
 Object injectableValue = new Object();
 when(context.findInjectableValue(eq(property._injectableValueId), eq(property), any())).thenReturn(injectableValue);
 assertEquals(injectableValue, property.findInjectableValue(context, beanInstance));

 // Test inject method
 property.inject(context, beanInstance);
 assertEquals(injectableValue, MAPper.readValue(new JsonParser(MAPPER.createParser()), property.getType()));

 // Test deserializeAndSet method
 JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
 when(deserializer.deserialize(any(), any(), any())).thenReturn(1);
 property.setValueDeserializer(deserializer);
 property.deserializeAndSet(MAPPER.getFactory().createParser("{}"), context, beanInstance);
 assertEquals(1, MAPper.readValue(new JsonParser(MAPPER.createParser("{}")), JavaType.construct(int.class)));

 // Test deserializeSetAndReturn method
 property.deserializeSetAndReturn(MAPPER.getFactory().createParser("{}"), context, beanInstance);
 assertEquals(1, MAPper.readValue(new JsonParser(MAPPER.createParser("{}")), JavaType.construct(int.class)));

 // Test set method
 property.deserializeAndSet(MAPPER.getFactory().createParser("{}"), context, beanInstance);
 assertEquals(1, MAPper.readValue(new JsonParser(MAPPER.createParser("{}")), JavaType.construct(int.class)));
 assertEquals(1, ((Number)BeanUtil.findPropertyValue(beanInstance, property.getName())).intValue());

 // Test get method
 assertEquals(1, property.get(beanInstance));

 // Test getSetter method
 assertEquals(property._fallbackSetter, property.getSetter(beanInstance));

 // Test toString method
 assertEquals("[creator property, name 'test_property'; inject id null]", property.toString());
 }

 @Test
 public void testMissingSetter() throws IOException, JsonMappingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("test_property"),
 MAPPER.getTypeFactory().constructType(int.class),
 PropertyName.construct("wrapperName"),
 mock(TypeDeserializer.class),
 Annotations.empty(),
 mock(AnnotatedParameter.class),
 1,
 null,
 PropertyMetadata.builder().build());
 property.deserializeAndSet(MAPPER.getFactory().createParser("{}"), null, null);
 assertThrows(InvalidDefinitionException.class, () -> property.setAndReturn(null, null));
 }
}