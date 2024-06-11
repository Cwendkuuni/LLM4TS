package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.SerializerType;
import com.fasterxml.jackson.databind.util.BeanProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link AnyGetterWriter} Class
 */
public class AnyGetterWriterTest {

 private ObjectMapper mapper;
 private Object testBean;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 testBean = new TestBean();
 }

 /**
 * Test constructor
 */
 @Test
 public void testAnyGetterWriterConstruction() {
 BeanProperty property = new BeanProperty("testProperty", null, TestBean.class, null);
 AnnotatedMember accessor = mapper.constructorInfo().findProperty("testProperty");
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 assertNotNull(anyGetterWriter);
 }

 /**
 * Test getAndSerialize method with null value
 */
 @Test
 public void testGetAndSerializeWithNullValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(testBean, gen, provider);
 assertTrue(gen.isClosed());
 }

 /**
 * Test getAndSerialize method with empty map value
 */
 @Test
 public void testGetAndSerializeWithEmptyMapValue() throws Exception {
 Object value = new HashMap<>();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(new TestBean(), gen, provider);
 assertFalse(gen.hasMoreEvents());
 }

 /**
 * Test getAndSerialize method with valid map value
 */
 @Test
 public void testGetAndSerializeWithValidMapValue() throws Exception {
 Object value = new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(new TestBean(), gen, provider);
 assertFalse(gen.hasMoreEvents());
 }

 /**
 * Test getAndSerialize method with null MapSerializer
 */
 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullMapSerializer() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), null);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.getAndSerialize(testBean, gen, provider);
 fail("JsonMappingException expected but not thrown");
 }

 /**
 * Test getAndFilter method with null value
 */
 @Test
 public void testGetAndFilterWithNullValue() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = mapper.getSerializationConfig().getDefaultPropertyFilter();
 anyGetterWriter.getAndFilter(testBean, gen, provider, filter);
 assertTrue(gen.isClosed());
 }

 /**
 * Test getAndFilter method with empty map value
 */
 @Test
 public void testGetAndFilterWithEmptyMapValue() throws Exception {
 Object value = new HashMap<>();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = mapper.getSerializationConfig().getDefaultPropertyFilter();
 anyGetterWriter.getAndFilter(new TestBean(), gen, provider, filter);
 assertFalse(gen.hasMoreEvents());
 }

 /**
 * Test getAndFilter method with valid map value
 */
 @Test
 public void testGetAndFilterWithValidMapValue() throws Exception {
 Object value = new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = mapper.getSerializationConfig().getDefaultPropertyFilter();
 anyGetterWriter.getAndFilter(new TestBean(), gen, provider, filter);
 assertFalse(gen.hasMoreEvents());
 }

 /**
 * Test getAndFilter method with null MapSerializer
 */
 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullMapSerializer() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), null);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = mapper.getSerializerProvider();
 PropertyFilter filter = mapper.getSerializationConfig().getDefaultPropertyFilter();
 anyGetterWriter.getAndFilter(testBean, gen, provider, filter);
 fail("JsonMappingException expected but not thrown");
 }

 /**
 * Test getAndFilter method with JsonInclude.Include.NON_NULL filter
 */
 @Test
 public void testGetAndFilterWithJsonIncludeNonNullFilter() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper newMapper = objectMapper.copy();
 newMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 JsonGenerator gen = newMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = newMapper.getSerializerProvider();
 PropertyFilter filter = newMapper.getSerializationConfig().getDefaultPropertyFilter();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 TestBean testBean = new TestBean();
 testBean.setTestProperty(null);
 anyGetterWriter.getAndFilter(testBean, gen, provider, filter);
 assertFalse(gen.hasMoreEvents());
 }

 /**
 * Test getAndFilter method with JsonInclude.Include.ALWAYS filter
 */
 @Test
 public void testGetAndFilterWithJsonIncludeAlwaysFilter() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper newMapper = objectMapper.copy();
 newMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
 JsonGenerator gen = newMapper.getFactory().createGenerator(System.out);
 SerializerProvider provider = newMapper.getSerializerProvider();
 PropertyFilter filter = newMapper.getSerializationConfig().getDefaultPropertyFilter();
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 TestBean testBean = new TestBean();
 testBean.setTestProperty(null);
 anyGetterWriter.getAndFilter(testBean, gen, provider, filter);
 assertFalse(gen.hasMoreEvents());

 Object value = new HashMap<String, Object>() {{
 put("key1", "value1");
 put("key2", null);
 }};
 TestBean testBeanWithValue = new TestBean();
 testBeanWithValue.setTestProperty(value);
 anyGetterWriter.getAndFilter(testBeanWithValue, gen, provider, filter);
 assertTrue(gen.hasMoreEvents());
 }

 /**
 * Test resolve method
 */
 @Test
 public void testResolve() throws Exception {
 AnyGetterWriter anyGetterWriter = new AnyGetterWriter(_property("testProperty", TestBean.class), _accessor("testProperty", TestBean.class), new MapSerializer(null, new HashMapSerializer(), SerializerType.FIELD_VALUE));
 SerializerProvider provider = mapper.getSerializerProvider();
 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 }
}

 class TestBean {
 @JsonAnyGetter
 Map<String, Object> testProperty;
 }

 class TestPropertyAccessor {
 @JsonProperty("testProperty")
 public Map<String, Object> getTestProperty(TestBean testBean) {
 return testBean.testProperty;
 }
 }

 class TestProperty {
 @JsonAnyGetter
 Map<String, Object> testProperty;

 public static BeanProperty _property(String name, Class<?> clazz) {
 return new BeanProperty(name, null, clazz, null);
 }

 public static AnnotatedMember _accessor(String name, Class<?> clazz) {
 return mapper.getBeanDescription(clazz).findProperty(name);
 }
}