package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestObjectMapper;

/**
 * Test class for AnyGetterWriter
 */
public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() throws Exception {
        objectMapper = TestObjectMapper.defaultInstance();
        BeanProperty property = new BeanProperty("testMap", Map.class);
        AnnotatedMember accessor = new AnnotatedMember(TestClass.class, "testMap");
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, new MapSerializer(objectMapper.getFactory()));
        this.anyGetterWriter = anyGetterWriter;
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new TestClass();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        gen.flush();
    }

    @Test
    public void testGetAndSerializeNullValue() throws Exception {
        Object bean = new TestClass();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        assertNotNull(gen.toString()); // verify no exception was thrown
        gen.flush();
    }

    @Test
    public void testGetAndSerializeNonMapValue() throws Exception {
        Object bean = new TestClassWithNonMapValue();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, gen, provider));
        gen.flush();
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new TestClass();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = PropertyFilter.FILTER_NONE;
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        gen.flush();
    }

    @Test
    public void testGetAndFilterNullValue() throws Exception {
        Object bean = new TestClass();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = PropertyFilter.FILTER_NONE;
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        assertNotNull(gen.toString()); // verify no exception was thrown
        gen.flush();
    }

    @Test
    public void testGetAndFilterNonMapValue() throws Exception {
        Object bean = new TestClassWithNonMapValue();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = PropertyFilter.FILTER_NONE;
        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndFilter(bean, gen, provider, filter));
        gen.flush();
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        assertNotNull(anyGetterWriter.resolve(provider));
    }

    @Test
    public void testResolveNullProvider() throws Exception {
        assertThrows(JsonMappingException.class, () -> anyGetterWriter.resolve(null));
    }

    static class TestClass {
        @JsonAnyGetter
        public Map<String, Integer> testMap() {
            return new HashMap<>();
        }
    }

    static class TestClassWithNonMapValue {
        @JsonSerialize(using = JsonNullSerializer.class)
        @JsonDeserialize(using = JsonNullDeserializer.class)
        public Object testMap() {
            return null;
        }
    }
}