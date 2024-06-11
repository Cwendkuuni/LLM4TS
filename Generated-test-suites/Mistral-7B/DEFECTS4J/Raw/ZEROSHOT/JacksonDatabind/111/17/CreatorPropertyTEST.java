package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

 private ObjectMapper mapper;
 private CreatorProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 property = new CreatorProperty(
 PropertyName.construct("name"),
 javaType,
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.empty());
 }

 @Test
 public void testGetName() {
 assertEquals("name", property.getName().asString());
 }

 @Test
 public void testGetType() {
 assertEquals(MyClass.class, property.getType());
 }

 @Test
 public void testSetFallbackSetter() {
 SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
 property.setFallbackSetter(fallbackSetter);
 assertEquals(fallbackSetter, property._fallbackSetter);
 }

 @Test(expected = IOException.class)
 public void testSetFallbackSetterNull() throws IOException {
 property.setFallbackSetter(null);
 }

 @Test
 public void testIsIgnorable() {
 assertFalse(property.isIgnorable());
 }

 @Test
 public void testMarkAsIgnorable() {
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());
 }

 @Test
 public void testFindInjectableValue() throws IOException {
 Object injectedValue = new Object();
 Object beanInstance = new MyClass();
 when(mapper.findInjectableValue(eq(injectedValue), eq(property), eq(beanInstance)))
 .thenReturn(injectedValue);
 assertEquals(injectedValue, property.findInjectableValue(mapper, beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindInjectableValueNullInjectableValueId() {
 property._injectableValueId = null;
 assertNull(property.findInjectableValue(mapper, new MyClass()));
 }

 @Test
 public void testInject() throws IOException {
 Object injectedValue = new Object();
 when(mapper.findInjectableValue(eq(injectedValue), eq(property), eq(new MyClass())))
 .thenReturn(injectedValue);
 property.inject(mapper, new MyClass());
 assertEquals(injectedValue, property.get(new MyClass()));
 }

 @Test
 public void testWithName() {
 String newName = "newName";
 CreatorProperty newProperty = property.withName(PropertyName.construct(newName));
 assertEquals(newName, newProperty.getName().asString());
 assertEquals(property.getClass(), newProperty.getClass());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
 CreatorProperty newProperty = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, newProperty._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nva = mock(NullValueProvider.class);
 CreatorProperty newProperty = property.withNullProvider(nva);
 assertEquals(nva, newProperty._nullProvider);
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 property.fixAccess(config);
 verify(config).findAccessModifier(eq(MyClass.class), eq("name"), anyInt());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String json = "{\"name\":\"value\"}";
 ObjectReader reader = mapper.readerForType(MyClass.class);
 Object readValue = reader.readValue(new StringReader(json));
 property.deserializeAndSet(mapper.getFactory().createParser(json), mapper, readValue);
 assertEquals("value", property.get(readValue));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String json = "{\"name\":\"value\"}";
 ObjectReader reader = mapper.readerForType(MyClass.class);
 Object readValue = reader.readValue(new StringReader(json));
 assertEquals("value", property.deserializeSetAndReturn(mapper.getFactory().createParser(json), mapper, readValue));
 assertEquals("value", property.get(readValue));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new MyClass();
 String value = "value";
 property.set(instance, value);
 assertEquals(value, property.get(instance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new MyClass();
 String value = "value";
 assertEquals(instance, property.setAndReturn(instance, value).get(instance));
 assertEquals(value, property.get(instance));
 }

 @Test
 public void testGetInjectableValueId() {
 assertNull(property.getInjectableValueId());
 }

 @Test
 public void testToString() {
 assertEquals("[creator property, name 'name'; inject id null]", property.toString());
 }
}

 class MyClass {
 @JsonCreator
 public MyClass(@JsonProperty("name") String name) {
 this.name = name;
 }

 private String name;
 }