package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private static final Object MAP = new Object() {};
 private static final Object INSTANCE = new Object() {};

 @Test
 public void testSettableAnyProperty() throws Exception {
 SettableAnyProperty settableAnyProperty = createSettableAnyProperty();
 // Test property
 BeanProperty property = settableAnyProperty.getProperty();
 assertEquals("getProperty", property.getName());
 // Test setter
 AnnotatedMember setter = settableAnyProperty.getSetter();
 assertTrue("isSetterField", settableAnyProperty.isSetterField());
 if (settableAnyProperty.isSetterField()) {
 AnnotatedField field = (AnnotatedField) setter;
 assertEquals("getName", "getName", field.getName());
 } else {
 AnnotatedMethod method = (AnnotatedMethod) setter;
 assertEquals("getName", "getName", method.getName());
 }
 // Test type
 JavaType type = settableAnyProperty.getType();
 assertEquals("getType", String.class, type.getRawClass());
 // Test valueDeserializer
 JsonDeserializer<Object> valueDeserializer = settableAnyProperty.getValueDeserializer();
 assertNotNull("hasValueDeserializer", valueDeserializer);
 // Test keyDeserializer
 KeyDeserializer keyDeserializer = settableAnyProperty.getKeyDeserializer();
 assertNull("isKeyDeserializerNull", keyDeserializer);
 // Test deserializeAndSet
 Object propName = "propName";
 String value = "value";
 JsonParser jsonParser = createTestParser();
 settableAnyProperty.deserializeAndSet(jsonParser, createContext(), INSTANCE, propName);
 Object result = getFieldValue(INSTANCE, "anyProperty");
 assertEquals("result", value, result);
 // Test deserialize
 Object deserializedValue = settableAnyProperty.deserialize(jsonParser, createContext());
 assertEquals("deserializedValue", value, deserializedValue);
 // Test set
 settableAnyProperty.set(INSTANCE, "propName", value);
 Object result2 = getFieldValue(INSTANCE, "anyProperty");
 assertEquals("result2", value, result2);
 }

 private SettableAnyProperty createSettableAnyProperty() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = mapper.findBeanProperty("anyProperty", INSTANCE.getClass());
 AnnotatedField setterField = mapper.getIntrospect().findAnnotatedMember(INSTANCE.getClass(), "setAnyProperty");
 JavaType type = mapper.getTypeFactory().constructType(Map.class);
 KeyDeserializer keyDeserializer = null;
 JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class, JsonToken.VALUE_STRING);
 TypeDeserializer valueTypeDeserializer = null;
 return new SettableAnyProperty(property, setterField, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 private Object getFieldValue(Object object, String fieldName) throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.readValue(mapper.writeValueAsString(object), Map.class).get(fieldName);
 }

 private JsonParser createTestParser() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getFactory().createParser("{\"anyProperty\":\"value\"}");
 }

 private DeserializationContext createContext() {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getDeserializationConfig().withoutFeature(MapperFeature.AUTO_DETECT_GETTERS);
 }
}