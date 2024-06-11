package com.yourpackage.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        BeanProperty property = BeanUtil.findProperty(_TestBean.class, "testMap");
        AnnotatedMember accessor = BeanUtil.findGetter(property);
        MapSerializer mapSerializer = objectMapper.findValueSerializer(Map.class);
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws IOException {
        Object bean = new _TestBean();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        gen.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNullValue() throws IOException {
        Object bean = new _TestBean();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.getAndSerialize(null, gen, provider);
        gen.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws IOException {
        Object bean = new _TestBean();
        bean.testNonMapValue = "not a map";
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        gen.close();
    }

    @Test
    public void testGetAndFilter() throws IOException {
        Object bean = new _TestBean();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean filterOut(String name) {
                return name.equals("key2");
            }
        };
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        gen.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNullValue() throws IOException {
        Object bean = new _TestBean();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean filterOut(String name) {
                return false;
            }
        };
        anyGetterWriter.getAndFilter(null, gen, provider, filter);
        gen.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws IOException {
        Object bean = new _TestBean();
        bean.testNonMapValue = "not a map";
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean filterOut(String name) {
                return false;
            }
        };
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        gen.close();
    }

    @Test
    public void testResolve() throws IOException {
        Object bean = new _TestBean();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.resolve(provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveWithNullMapSerializer() throws IOException {
        Object bean = new _TestBean();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        provider.setContextualizationConfig(null);
        anyGetterWriter.resolve(provider);
    }

    static class _TestBean {
        Map<String, String> testMap = new HashMap<>();
        Object testNonMapValue;
    }

    static class PropertyFilter {
        public boolean filterOut(String name) {
            return false;
        }
    }
}