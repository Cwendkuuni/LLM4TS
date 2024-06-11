package com.example.junit.tests.anygetterwriter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class AnyGetterWriterTest {

 private static final ObjectNode EMPTY_MAP = ObjectMapper.getDefaultInstance().createObjectNode();

 @Test
 public void testAnyGetterWriterConstruction() {
 BeanProperty property = new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty");
 AnnotatedMember accessor = new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null);
 AnyGetterWriter writer = new AnyGetterWriter(property, accessor, new MapSerializer(Serializers.instance, null));
 assertNotNull(writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnyGetterWriterWithNullProperty() {
 AnyGetterWriter writer = new AnyGetterWriter(null, new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null));
 fail("Should not be able to create a new instance of AnyGetterWriter with a null BeanProperty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnyGetterWriterWithNullAccessor() {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), null, new MapSerializer(Serializers.instance, null));
 fail("Should not be able to create a new instance of AnyGetterWriter with a null AnnotatedMember");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnyGetterWriterWithNullSerializer() {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), null);
 fail("Should not be able to create a new instance of AnyGetterWriter with a null MapSerializer");
 }

 @Test
 public void testGetAndSerializeNullBean() throws IOException {
 Object bean = null;
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null));
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndSerialize(bean, gen, provider);
 assertTrue(gen.isClosed());
 assertEquals(EMPTY_MAP, gen.getOutputContent());
 }

 @Test
 public void testGetAndSerialize() throws IOException {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null));
 Object bean = new AnyGetterWriterTest();
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndSerialize(bean, gen, provider);
 assertTrue(gen.isClosed());
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 expectedMap.put("key2", "value2");
 ObjectNode actualMap = ((ObjectMapper) ObjectMapper.getDefaultInstance()).readTree(gen.getOutputContent());
 assertEquals(expectedMap.toString(), actualMap.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapType() throws IOException {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null));
 Object bean = new NonMapType();
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndSerialize(bean, gen, provider);
 fail("Should throw JsonMappingException when the value returned by 'any-getter' is not a Map");
 }

 @Test
 public void testGetAndFilterNullBean() throws IOException {
 Object bean = null;
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null), new TestPropertyFilter());
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndFilter(bean, gen, provider, new TestPropertyFilter());
 assertTrue(gen.isClosed());
 assertEquals(EMPTY_MAP, gen.getOutputContent());
 }

 @Test
 public void testGetAndFilter() throws IOException {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), new MapSerializer(Serializers.instance, null), new TestPropertyFilter());
 Object bean = new AnyGetterWriterTest();
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndFilter(bean, gen, provider, new TestPropertyFilter());
 assertTrue(gen.isClosed());
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("key1", "value1");
 ObjectNode actualMap = ((ObjectMapper) ObjectMapper.getDefaultInstance()).readTree(gen.getOutputContent());
 assertEquals(expectedMap.toString(), actualMap.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterNullSerializer() throws IOException {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), null, new TestPropertyFilter());
 Object bean = new AnyGetterWriterTest();
 JsonGenerator gen = new TestJsonGenerator();
 SerializerProvider provider = new TestSerializerProvider();
 writer.getAndFilter(bean, gen, provider, new TestPropertyFilter());
 fail("Should throw JsonMappingException when the MapSerializer is null");
 }

 @Test
 public void testResolve() throws JsonMappingException {
 AnyGetterWriter writer = new AnyGetterWriter(new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "mapProperty"), new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), null);
 SerializerProvider provider = new TestSerializerProvider();
 writer.resolve(provider);
 assertNotNull(writer._mapSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveWithNullProperty() throws JsonMappingException {
 AnyGetterWriter writer = new AnyGetterWriter(null, new AnnotatedMethod(AnyGetterWriterTest.class, "getMapProperty", null), null);
 SerializerProvider provider = new TestSerializerProvider();
 writer.resolve(provider);
 fail("Should throw JsonMappingException when the BeanProperty is null");
 }

 private static class NonMapType {
 }

 private static class TestJsonGenerator extends JsonGenerator {
 @Override
 public void close() throws IOException {
 // Empty implementation
 }
 }

 private static class TestSerializerProvider extends SerializerProvider {
 @Override
 public void findAndRegisterModules() {
 // Empty implementation
 }

 @Override
 public void findAndRegisterSerializers(SerializerFactory serializers) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterKeyDeserializers(DeserializationConfig config, DeserializerProvider deserializers) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterValueDeserializers(DeserializationConfig config, DeserializerProvider deserializers) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterBeanDeserializer(BeanDescription beanDesc) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterPropertyMetadata(BeanDescription beanDesc, PropertyName name, MapperConfig config) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterFactory(Factory registry) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterValueInstantiator(ValueInstantiator vinst) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterValueCoercions(Map<Class<?>, Converter<?,?>> converters) {
 // Empty implementation
 }

 @Override
 public void findAndRegisterObjectCodec(ObjectCodec objectCodec) {
 // Empty implementation
 }
 }

 private static class TestPropertyFilter implements PropertyFilter {
 @Override
 public void serializeFilteredFields(Map<String, Object> map, JsonGenerator gen, SerializerProvider provider, PropertyFilter pf, Object serContext) throws IOException {
 // Empty implementation
 }

 @Override
 public void filterNull(Map<String, Object> map) {
 // Empty implementation
 }
 }
}