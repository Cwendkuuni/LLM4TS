package com.example.anygetterwriter;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.AnnotatedClassTestBase;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest extends AnnotatedClassTestBase {

    private static class TestBean {
        @JsonAnyGetter
        Map<String, String> getAnyMap() {
            return map;
        }

        Map<String, String> map = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new AnnotationIntrospector(true, true, null));
        AnyGetterWriter writer = mapper.findValueSerializer(TestBean.class, TestBean.class);
        TestBeanTest.writer = writer;
    }

    // Test case for constructor
    @Test
    public void testAnyGetterWriterConstruction() {
        assertNotNull(TestBeanTest.writer);
        BeanProperty property = TestBeanTest.writer._property;
        AnnotatedMember accessor = TestBeanTest.writer._accessor;
        MapSerializer mapSerializer = TestBeanTest.writer._mapSerializer;
        assertEquals(TestBean.class, property.getMember().getDeclaringClass());
        assertEquals("getAnyMap", accessor.getName());
        assertNotNull(mapSerializer);
    }

    // Test case for getAndSerialize method
    @Test
    public void testGetAndSerialize() throws Exception {
        TestBean bean = new TestBean();
        bean.map.put("key1", "value1");
        bean.map.put("key2", "value2");

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();

        TestBeanTest.writer.getAndSerialize(bean, gen, provider);

        String json = gen.getOutputContentAsString();
        Map<String, Object> actual = mapper.readValue(json, Map.class);

        Map<String, Object> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        assertEquals(expected, actual);
    }

    // Test case for getAndSerialize method with null value
    @Test
    public void testGetAndSerializeWithNullValue() throws Exception {
        TestBean bean = new TestBean();

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();

        TestBeanTest.writer.getAndSerialize(bean, gen, provider);

        String json = gen.getOutputContentAsString();
        assertTrue(json.isEmpty());
    }

    // Test case for getAndSerialize method with non-Map value
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        TestBean bean = new TestBean();
        bean.map = null;
        bean.map = new Integer(0);

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();

        TestBeanTest.writer.getAndSerialize(bean, gen, provider);
    }

    // Test case for getAndFilter method
    @Test
    public void testGetAndFilter() throws Exception {
        TestBean bean = new TestBean();
        bean.map.put("key1", "value1");
        bean.map.put("key2", "value2");

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();

        Object filter = new FilterFinder();
        TestBeanTest.writer.getAndFilter(bean, gen, provider, filter);

        String json = gen.getOutputContentAsString();
        Map<String, Object> actual = mapper.readValue(json, Map.class);

        Map<String, Object> expected = Collections.emptyMap();
        assertEquals(expected, actual);
    }

    // Test case for resolve method
    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = getProvider();
        TestBeanTest.writer.resolve(provider);
        assertNotNull(TestBeanTest.writer._mapSerializer);
    }

    // Test case for getAndSerialize method with MapSerializer is null
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithMapSerializerNull() throws Exception {
        TestBean bean = new TestBean();
        bean.map.put("key1", "value1");
        bean.map.put("key2", "value2");

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();
        TestBeanTest.writer._mapSerializer = null;

        TestBeanTest.writer.getAndSerialize(bean, gen, provider);
    }

    // Test case for getAndSerialize method with empty Map
    @Test
    public void testGetAndSerializeWithEmptyMap() throws Exception {
        TestBean bean = new TestBean();

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = getProvider();

        TestBeanTest.writer.getAndSerialize(bean, gen, provider);

        String json = gen.getOutputContentAsString();
        assertTrue(json.isEmpty());
    }

    // Nested class to test Filtering
    public static class FilterFinder implements PropertyFilter {
        @Override
        public void filterObject(SerializerProvider provider, MapSerializer serializer, Object bean, Map<String, Object> properties, String name, int serializerIndex, Object parent, JsonGenerator gen) throws IOException {
            gen.writeString(name);
            gen.writeString(": ");
            gen.writeString(properties.get(name).toString());
            gen.writeEndObject();
        }
    }

    // Static field to hold the writer instance for test cases
    private static AnyGetterWriter writer;

    // Helper methods to create JsonGenerator and SerializerProvider
    private JsonGenerator createJsonGenerator() throws IOException {
        StringWriter writer = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createGenerator(writer);
    }

    private SerializerProvider getProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getSerializerProvider();
    }
}