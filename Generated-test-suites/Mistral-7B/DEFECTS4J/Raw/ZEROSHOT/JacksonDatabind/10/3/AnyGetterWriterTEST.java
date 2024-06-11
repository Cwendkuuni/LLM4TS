package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Tests for {@link AnyGetterWriter} class
 */
public class AnyGetterWriterTest {

 private static class TestBean {
 private Map<String, String> mapProperty;

 @JsonAnyGetter
 public Map<String, String> getMapProperty() {
 return mapProperty;
 }

 public void setMapProperty(Map<String, String> mapProperty) {
 this.mapProperty = mapProperty;
 }
 }

 @ org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 ObjectCodec objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();
 }

 @ org.junit.Test
 public void testGetAndSerialize() throws JsonMappingException, JsonProcessingException {
 TestBean bean = new TestBean();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 bean.setMapProperty(map);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.stdPropertyFor(_javaClass(TestBean.class), "mapProperty"),
 AnnotatedMember.construct(TestBean.class, "mapProperty"), new MapSerializer(objectMapper.getFactory(), SimpleType.constructUnboundedType(Map.class), null));

 JsonGenerator gen = null;
 try {
 gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndSerialize(bean, gen, objectMapper.getSerializerProvider());
 String result = new String(gen.getOutputBuffer().toString());
 System.out.println(result);
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", result);
 } finally {
 if (gen != null) gen.close();
 }
 }

 @ org.junit.Test
 public void testGetAndFilter() throws JsonMappingException, JsonProcessingException {
 TestBean bean = new TestBean();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 bean.setMapProperty(map);

 PropertyFilter filter = new FilterMapper().filterDefinition("foo");
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.stdPropertyFor(_javaClass(TestBean.class), "mapProperty"),
 AnnotatedMember.construct(TestBean.class, "mapProperty"), new MapSerializer(objectMapper.getFactory(), SimpleType.constructUnboundedType(Map.class), null));

 JsonGenerator gen = null;
 try {
 gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndFilter(bean, gen, objectMapper.getSerializerProvider(), filter);
 String result = new String(gen.getOutputBuffer().toString());
 System.out.println(result);
 // since we are not filtering anything here, result should be same as in testGetAndSerialize
 assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", result);
 } finally {
 if (gen != null) gen.close();
 }
 }

 @ org.junit.Test
 public void testResolve() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter writer = objectMapper.writer();
 TestBean bean = new TestBean();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 bean.setMapProperty(map);
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.stdPropertyFor(_javaClass(TestBean.class), "mapProperty"),
 AnnotatedMember.construct(TestBean.class, "mapProperty"), new MapSerializer(objectMapper.getFactory(), SimpleType.constructUnboundedType(Map.class), null));
 ObjectIdReader idReader = objectMapper.getFactory().getSerializerProvider().getParser().readValueAs(ObjectIdReader.class, bean.getClass());
 ObjectReader objectReader = objectMapper.readerFor(idReader.getClass());
 ObjectReader mapReader = objectMapper.readerFor(Map.class);
 JsonTreeReader jsonTreeReader = objectMapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{}), null);
 jsonTreeReader.nextToken(); // consume root object id
 ObjectIdReader rootIdReader = jsonTreeReader.readValueAs(ObjectIdReader.class);
 ObjectParser objectParser = objectMapper.parserForId(rootIdReader);
 JsonNode rootNode = objectParser.readValueAsTree();
 JsonNode mapNode = rootNode.get("mapProperty");
 assertNotNull(mapNode);
 ObjectNode mapObjectNode = (ObjectNode) mapNode;
 AnyGetterWriter resolvedAnyGetterWriter = new AnyGetterWriter(BeanProperty.stdPropertyFor(_javaClass(TestBean.class), "mapProperty"),
 AnnotatedMember.construct(TestBean.class, "mapProperty"), new MapSerializer(objectMapper.getFactory(), SimpleType.constructUnboundedType(Map.class), null));
 ObjectIdReader childIdReader = objectMapper.getFactory().getSerializerProvider().getParser().readValueAs(ObjectIdReader.class, MapSerializer.class);
 ObjectReader childMapReader = objectMapper.readerFor(Map.class);
 ObjectParser childParser = objectMapper.parserForId(childIdReader);
 JsonNode childNode = childParser.readValueAsTree(mapObjectNode);
 Map<?,?> map = childNode.defaultObject();
 assertEquals(map, bean.getMapProperty());
 assertEquals(resolvedAnyGetterWriter._mapSerializer, anyGetterWriter._mapSerializer);
 }
}