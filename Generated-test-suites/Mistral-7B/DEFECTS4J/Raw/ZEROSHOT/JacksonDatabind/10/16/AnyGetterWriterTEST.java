package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.bean.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for AnyGetterWriter
 */
public class AnyGetterWriterTest {

    private ObjectMapper mapper;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer mapSerializer;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        BeanPropertyDefinition def = BeanPropertyDefinition.standardModel("testBean");
        property = new BeanProperty("anyMap", Map.class, def);
        accessor = BeanUtil.findProperty(TestBean.class, "anyMap");
        mapSerializer = (MapSerializer) Serializers.findSerializationConfig(Map.class, mapper);
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setAnyMap(new HashMap<>());
        testBean.getAnyMap().put("key1", "value1");

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        Map<String, Object> serializedMap = mapper.readValue(gen.getOutputBuffer(), Map.class);
        assertEquals("value1", serializedMap.get("key1"));
        assertTrue(gen.isClosed());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNullValue() throws Exception {
        TestBean testBean = new TestBean();

        anyGetterWriter.getAndSerialize(testBean, null, mapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNonMapValue() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setAnyMap(42);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        String errorMessage = "Value returned by 'any-getter' (anyMap()) not java.util.Map but class java.lang.Integer";
        assertEquals(errorMessage, gen.getLastError().getMessage());
        assertTrue(gen.isClosed());
    }

    @Test
    public void testGetAndFilter() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setAnyMap(new HashMap<>());
        testBean.getAnyMap().put("key1", "value1");

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean filterOut(String name, JsonGenerator gen, SerializerProvider provider) throws IOException {
                return !name.equals("key1");
            }
        };

        anyGetterWriter.getAndFilter(testBean, gen, provider, filter);

        Map<String, Object> serializedMap = mapper.readValue(gen.getOutputBuffer(), Map.class);
        assertFalse(serializedMap.containsKey("key1"));
        assertTrue(gen.isClosed());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterNullValue() throws Exception {
        TestBean testBean = new TestBean();

        anyGetterWriter.getAndFilter(testBean, null, mapper.getSerializerProvider(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterNonMapValue() throws Exception {
        TestBean testBean = new TestBean();
        testBean.setAnyMap(42);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndFilter(testBean, gen, provider, null);

        String errorMessage = "Value returned by 'any-getter' (anyMap()) not java.util.Map but class java.lang.Integer";
        assertEquals(errorMessage, gen.getLastError().getMessage());
        assertTrue(gen.isClosed());
    }

    @Test
    public void testResolve() throws Exception {
        TestBean testBean = new TestBean();
        TestBean resolvedBean = mapper.readValue(mapper.writeValueAsBytes(testBean), TestBean.class);

        SerializerProvider provider = mapper.getSerializerProvider();
        anyGetterWriter.resolve(provider);

        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }
}

class TestBean {
    @JsonAnyGetter
    public Map<String, Object> getAnyMap() {
        return anyMap;
    }

    private Map<String, Object> anyMap = new HashMap<>();
}