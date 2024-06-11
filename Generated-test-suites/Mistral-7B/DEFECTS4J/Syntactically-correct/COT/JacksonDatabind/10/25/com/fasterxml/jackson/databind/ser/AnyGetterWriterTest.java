package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest extends BaseTest {

 private static class TestBean {
 private Map<String, Integer> testMap;

 @JsonAnyGetter
 public Map<String, Integer> getTestMap() {
 return testMap;
 }
 }

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter ow = mapper.writerWithDefaultPrettyPrinter();
 TestBean testBean = new TestBean();
 testBean.testMap = new HashMap<>();
 testBean.testMap.put("test", 1);
 String json = ow.writeValueAsString(testBean);
 Object readValue = mapper.readValue(JsonFactory.instance.getText(json), TestBean.class);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter ow = mapper.writerWithDefaultPrettyPrinter();
 TestBean testBean = new TestBean();
 testBean.testMap = new HashMap<>();
 testBean.testMap.put("test", 1);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndSerialize(testBean, gen, mapper.getSerializerProvider());
 JsonParser p = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
 Map<String, Object> map = mapper.readValue(p, Map.class);
 assertEquals("test", map.keySet().iterator().next());
 assertEquals(1, map.get("test"));
 }

 @Test
 public void testGetAndSerializeWithNullValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = null;

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndSerialize(testBean, gen, mapper.getSerializerProvider());
 JsonParser p = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
 assertNull(mapper.readValue(p, Map.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = new Integer(1);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndSerialize(testBean, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testGetAndFilter() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = new HashMap<>();
 testBean.testMap.put("test", 1);
 testBean.testMap.put("filtered", 2);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 PropertyFilter filter = new PropertyFilter() {
 @Override public boolean shouldFilterMember(SerializerProvider p, BeanProperty property) {
 return property.getName().equals("filtered");
 }
 };

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 anyGetterWriter.getAndFilter(testBean, gen, mapper.getSerializerProvider(), filter);
 JsonParser p = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
 Map<String, Object> map = mapper.readValue(p, Map.class);
 assertEquals(1, map.size());
 assertFalse(map.containsKey("filtered"));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = null;

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 PropertyFilter filter = new PropertyFilter() {
 @Override public boolean shouldFilterMember(SerializerProvider p, BeanProperty property) {
 return true;
 }
 };

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 anyGetterWriter.getAndFilter(testBean, gen, mapper.getSerializerProvider(), filter);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = new Integer(1);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 PropertyFilter filter = new PropertyFilter() {
 @Override public boolean shouldFilterMember(SerializerProvider p, BeanProperty property) {
 return true;
 }
 };

 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 try {
 anyGetterWriter.getAndFilter(testBean, gen, mapper.getSerializerProvider(), filter);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testResolve() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 testBean.testMap = new HashMap<>();
 testBean.testMap.put("test", 1);

 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), null);
 anyGetterWriter.resolve(mapper.getSerializerProvider());

 AnyGetterWriter resolvedAnyGetterWriter = new AnyGetterWriter(BeanProperty.findProperty(_TestBean.class, "testMap"),
 AnnotatedMember.construct(TestBean.class, "_TestBean$0"), _mapSerializer);
 resolvedAnyGetterWriter.resolve(mapper.getSerializerProvider());
 assertEquals(resolvedAnyGetterWriter._mapSerializer, anyGetterWriter._mapSerializer);
 }
}