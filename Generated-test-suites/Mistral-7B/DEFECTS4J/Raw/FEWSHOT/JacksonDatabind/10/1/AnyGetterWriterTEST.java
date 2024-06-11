package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BeanPropertyWriterTestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest extends BeanPropertyWriterTestBase {

    @SuppressWarnings("unchecked")
    public AnyGetterWriterTest() {
        super(new ObjectMapper());
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = getMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        Object bean = new Object() {{
            _anyMap = map;
        }};
        BeanProperty property = mapper.findBeanProperty("_anyMap", Object.class);
        AnnotatedMember accessor = mapper.findMember("_anyMap", Object.class);
        MapSerializer mapSerializer = new MapSerializer(mapper.getFactory(), null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        writer.getAndSerialize(bean, gen, provider);
        assertEquals("{\"key1\":\"value1\"}", gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = getMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        Object bean = new Object() {{
            _anyMap = map;
        }};
        BeanProperty property = mapper.findBeanProperty("_anyMap", Object.class);
        AnnotatedMember accessor = mapper.findMember("_anyMap", Object.class);
        MapSerializer mapSerializer = new MapSerializer(mapper.getFactory(), null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();
        PropertyFilter filter = BeanUtil.filterEmptyProperties();

        writer.getAndFilter(bean, gen, provider, filter);
        assertEquals("{\"key1\":\"value1\"}", gen.getOutputBuffer().toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNullMap() throws Exception {
        ObjectMapper mapper = getMapper();
        Object bean = new Object() {{
            _anyMap = null;
        }};
        BeanProperty property = mapper.findBeanProperty("_anyMap", Object.class);
        AnnotatedMember accessor = mapper.findMember("_anyMap", Object.class);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        writer.getAndSerialize(bean, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNonMapType() throws Exception {
        ObjectMapper mapper = getMapper();
        Object bean = new Object() {{
            _nonMap = "non map";
        }};
        BeanProperty property = mapper.findBeanProperty("_nonMap", Object.class);
        AnnotatedMember accessor = mapper.findMember("_nonMap", Object.class);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = mapper.getSerializerProvider();

        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testResolve() throws Exception {
        ObjectMapper mapper = getMapper();
        MapSerializer mapSerializer = new MapSerializer(mapper.getFactory(), null);
        AnyGetterWriter writer = new AnyGetterWriter(null, null, mapSerializer);
        SerializerProvider provider = mapper.getSerializerProvider();

        writer.resolve(provider);
        assertNotNull(writer._mapSerializer);
    }

    private Object _anyMap;
    private Object _nonMap;
}