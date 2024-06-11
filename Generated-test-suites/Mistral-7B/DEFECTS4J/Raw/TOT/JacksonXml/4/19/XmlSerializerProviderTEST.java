package com.fasterxml.jackson.dataformat.xml.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

 private static final ObjectMapper objectMapper = new XmlMapper();

 @SuppressWarnings("unchecked")
 @org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @org.junit.Test
 public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("root")));
 ObjectNode rootNode = objectMapper.writeValueAsTree(provider, null);
 String xml = rootNode.toString();
 assertEquals("<root><null></null></root>", xml);
 }

 @org.junit.Test
 public void testSerializeValue() throws JsonGenerationException, JsonMappingException, IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("root")));
 ObjectNode rootNode = objectMapper.writeValueAsTree(provider, new String[]{"foo", "bar"});
 String xml = rootNode.toString();
 assertEquals("<root><item><string>foo</string><string>bar</string></item></root>", xml);
 }

 @org.junit.Test
 public void testSerializeValueWithCustomRoot() throws JsonGenerationException, JsonMappingException, IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("myRoot")));
 ObjectSerializer myRootSerializer = new ObjectSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
 throws IOException {
 provider.defaultSerializeValue(gen, value);
 gen.writeEndObject();
 gen.writeString("customRoot");
 }
 };
 ObjectNode rootNode = objectMapper.writeValueAsTree(provider, new Object(), new SimpleObjectSerializerModifier() {{
 setSerializer(Object.class, myRootSerializer);
 }});
 String xml = rootNode.toString();
 assertEquals("<myRoot><item>null</item><customRoot/></myRoot>", xml);
 }

 @org.junit.Test
 public void testSerializeValueWithRootType() throws JsonGenerationException, JsonMappingException, IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("root")), objectMapper.getSerializationConfig(), null);
 JavaType javaType = objectMapper.getTypeFactory().constructType(Object[].class);
 ObjectNode rootNode = objectMapper.writeValueAsTree(provider, new Object[]{"foo", "bar"}, javaType);
 String xml = rootNode.toString();
 assertEquals("<root><item><string>foo</string><string>bar</string></item></root>", xml);
 }

 @org.junit.Test
 public void testSerializeValueWithRootTypeAndCustomSerializer()
 throws JsonGenerationException, JsonMappingException, IOException {
 ObjectMapper mapper = new XmlMapper();
 mapper.registerSerializer(String[].class, new ToStringSerializer());
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("root")), mapper.getSerializationConfig(), null);
 JavaType javaType = objectMapper.getTypeFactory().constructType(Object[].class);
 ObjectNode rootNode = mapper.writeValueAsTree(provider, new Object[]{"foo", "bar"}, javaType);
 String xml = rootNode.toString();
 assertEquals("<root><item><toString>[foo, bar]</toString></item></root>", xml);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testSerializeValueWithRootTypeAndNullValue() throws JsonGenerationException, JsonMappingException, IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(Arrays.asList("root")), objectMapper.getSerializationConfig(), null);
 JavaType javaType = objectMapper.getTypeFactory().constructType(Object[].class);
 ObjectNode rootNode = objectMapper.writeValueAsTree(provider, null, javaType);
 // Should throw JsonMappingException: no Serializer found for class java.lang.Object and no properties/getters
 fail("Expected JsonMappingException");
 }
}

class ObjectSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
 throws IOException {
 provider.defaultSerializeValue(gen, value);
 gen.writeEndObject();
 gen.writeString("customRoot");
 }
}

class SimpleObjectSerializerModifier extends SimpleBeanPropertyFilter {
 @Override
 protected void filterOutAllProperties(SerializerProvider p, PropertyName pn, Object c)
 throws IOException {
 // Do not filter out any properties in this test
 }

 @SuppressWarnings("unchecked")
 @Override
 public SerializerProvider serializeWithFilter(SerializerProvider p, TypeSerializer tss)
 throws IOException {
 SerializerProvider provider = p.getUnderlyingProvider();
 ObjectSerializer objectSerializer = new ObjectSerializer();
 return new XmlSerializerProvider(provider, new XmlRootNameLookup(Arrays.asList("root")), objectMapper.getSerializationConfig(), null) {
 @Override
 public JsonSerializer<Object> findContentSerializer(Class<?> paramType, boolean paramBoolean) throws JsonMappingException {
 return objectSerializer;
 }
 };
}
}