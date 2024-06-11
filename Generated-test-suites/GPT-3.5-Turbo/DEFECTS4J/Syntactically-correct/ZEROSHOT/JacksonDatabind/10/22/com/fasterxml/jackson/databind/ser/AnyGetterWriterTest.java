package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Object bean = new Object();
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Object bean = new Object();
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerialize_NonMapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Object bean = new Object();
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        PropertyFilter filter = null;
        Object bean = new Object();
        writer.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        PropertyFilter filter = null;
        Object bean = new Object();
        writer.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testGetAndFilter_NonMapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        PropertyFilter filter = null;
        Object bean = new Object();
        writer.getAndFilter(bean, gen, provider, filter);
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        SerializerProvider provider = null;
        writer.resolve(provider);
    }
}