package com.example; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Test;

public class AnyGetterWriterTest {

 private AnyGetterWriter anyGetterWriter;
 private ObjectNode testObject;
 private MapSerializer mapSerializer;
 private JsonFactory jsonFactory = new JsonFactory();
 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter = new AnyGetterWriter(new BeanProperty(new BeanProperty.StdGetter(ReflectionUtil.getDeclaredField(TestBean.class, "anyMap")), null),
 ReflectionUtil.getDeclaredMethod(TestBean.class, "getAnyMap"), mapSerializer);
 testObject = (ObjectNode) jsonFactory.createObjectNode();
 mapSerializer = objectMapper.SerializerProvider.findValueSerializer(ObjectNode.class, null);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter.getAndSerialize(testBean, createJsonGenerator(), createSerializerProvider());
 Eq("{\"anyMap\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", testObject.toString());
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter.getAndSerialize(testBean, createJsonGenerator(), createSerializerProvider());
 assertNull(testObject);
 }

 @Test
 public void testGetAndSerializeNonMapType() throws Exception {
 TestBean testBean = new TestBean();
 testBean.setAnyMap(new String[]{"test"});
 anyGetterWriter.getAndSerialize(testBean, createJsonGenerator(), createSerializerProvider());
 throw new AssertionError("Expected JsonMappingException, but didn't receive one.");
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object testBean = new TestBean();
 testBean.setAnyMap(new TestMap());
 anyGetterWriter.getAndFilter(testBean, createJsonGenerator(), createSerializerProvider(), new FilterProvider().emptyFilterProvider());
 Eq("{\"anyMap\":{\"key2\":\"value2\"}}", testObject.toString());
 }

 @Test
 public void testGetAndFilterNullValue() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter.getAndFilter(testBean, createJsonGenerator(), createSerializerProvider(), new FilterProvider().emptyFilterProvider());
 assertNull(testObject);
 }

 @Test
 public void testGetAndFilterNonMapType() throws Exception {
 TestBean testBean = new TestBean();
 testBean.setAnyMap(new String[]{"test"});
 anyGetterWriter.getAndFilter(testBean, createJsonGenerator(), createSerializerProvider(), new FilterProvider().emptyFilterProvider());
 throw new AssertionError("Expected JsonMappingException, but didn't receive one.");
 }

 @Test
 public void testResolve() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter.resolve(createSerializerProvider());
 assertNotNull(anyGetterWriter.getMapSerializer());
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveNullMapSerializer() throws Exception {
 Object testBean = new TestBean();
 anyGetterWriter = new AnyGetterWriter(null, null, null);
 anyGetterWriter.resolve(createSerializerProvider());
 }

 private JsonGenerator createJsonGenerator() throws Exception {
 JsonGenerator gen = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 gen.setPrettyPrinting();
 return gen;
 }

 private SerializerProvider createSerializerProvider() throws Exception {
 return objectMapper.getSerializerProvider();
 }

 private static ObjectNode Eq(String expected, Object toCompare) {
 if (!expected.equals(toCompare.toString())) {
 fail("Expected:\n" + expected + "\nGot:\n" + toCompare.toString());
 }
 return (ObjectNode) toCompare;
 }
}