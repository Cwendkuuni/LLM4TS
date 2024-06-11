package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 @Test
 public void testAnyGetterWriter() throws Exception {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 MyClass myClass = new MyClass();
 BeanProperty property = BeanUtil.findProperty(MyClass.class, "anyMap");
 AnnotatedMember accessor = BeanUtil.findGetter(MyClass.class, "getAnyMap");
 MapSerializer mapSerializer = objectMapper.getSerializerProvider().findValueSerializer(Map.class);
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

 // When
 Object bean = myClass;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getOutputFactory());
 SerializerProvider provider = objectMapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 gen.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputStatistics().getOutputValue();
 String jsonString = new String(outputStream.toByteArray());

 // Then
 assertTrue(jsonString.contains("{\"anyKey1\":\"anyValue1\"}"));
 assertTrue(jsonString.contains("{\"anyKey2\":\"anyValue2\"}"));

 // When filtering
 provider.setPropertyFilter(new PropertyFilter() {
 @Override
 public boolean shouldFilterMember(SerializerProvider serializers, BeanProperty property) {
 return property.getName().equals("anyKey2");
 }
 });
 anyGetterWriter.getAndFilter(bean, gen, provider, null);
 gen.flush();
 outputStream = (ByteArrayOutputStream) gen.getOutputStatistics().getOutputValue();
 jsonString = new String(outputStream.toByteArray());

 // Then
 assertFalse(jsonString.contains("\"anyKey2\":\"anyValue2\""));
 assertTrue(jsonString.contains("{\"anyKey1\":\"anyValue1\"}"));
 }

 // Test resolve method (not covered in this test file as it's not relevant to the test)

 class MyClass {
 @JsonAnyGetter
 Map<String, String> anyMap;

 public Map<String, String> getAnyMap() {
 return anyMap;
 }
 }
}