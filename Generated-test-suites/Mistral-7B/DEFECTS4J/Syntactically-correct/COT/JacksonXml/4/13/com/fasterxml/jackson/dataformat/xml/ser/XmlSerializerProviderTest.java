package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XmlSerializerProviderTest {

    private XmlMapper mapper;
    private XmlRootNameLookup rootNameLookup;

    @Before
    public void setup() {
        mapper = new XmlMapper();
        rootNameLookup = new XmlRootNameLookup(mapper.getConfiguration());
    }

    @Test
    public void testCreateInstance() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider newProvider = provider.createInstance(mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory());
        assertEquals(provider, newProvider);
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, null);
        gen.close();
        assertEquals("<null/>", TestUtil.writeValueAsString(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes())));
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        String expectedRootName = "root";
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            protected QName _rootNameFromConfig() {
                return new QName(null, expectedRootName);
            }
        };
        Object value = new Object();
        provider.serializeValue(gen, value);
        gen.close();
        String actualValue = TestUtil.writeValueAsString(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("<root>" + TestUtil.writeValueAsString(value) + "</root>", actualValue);
    }

    @Test
    public void testSerializeValueWithArray() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        String expectedRootName = "items";
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            protected QName _rootNameFromConfig() {
                return new QName(null, expectedRootName);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void _startRootArray(ToXmlGenerator xgen, QName rootName) throws IOException {
                super._startRootArray(xgen, rootName);
                xgen.writeStartArray();
            }
        };
        Object[] values = new Object[]{"a", "b"};
        provider.serializeValue(gen, values);
        gen.close();
        String actualValue = TestUtil.writeValueAsString(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("<items><item>" + TestUtil.writeValueAsString(values[0]) + "</item><item>" + TestUtil.writeValueAsString(values[1]) + "</item></items>", actualValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithException() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            protected QName _rootNameFromConfig() {
                throw new IOException("Exception while finding root name");
            }
        };
        Object value = new Object();
        provider.serializeValue(gen, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullRootType() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser) throws IOException {
                if (rootType == null) {
                    throw new JsonMappingException("Root type is null");
                }
                super.serializeValue(gen, value, rootType, ser);
            }
        };
        Object value = new Object();
        provider.serializeValue(gen, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullSerializer() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            public JsonSerializer<Object> findTypedValueSerializer(JavaType type, boolean isEmpty, SerializerProvider serializers) {
                return null;
            }

            @Override
            public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser) throws IOException {
                super.serializeValue(gen, value, rootType, ser);
            }
        };
        Object value = new Object();
        provider.serializeValue(gen, value, null);
    }

    @Test
    public void testSerializeValueWithFilterProvider() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        SimpleBeanPropertyFilter filter = SimpleFilterProvider.filterEmptyBeans();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, mapper.getDefaultSerializerFactory()) {
            @Override
            public SerializerProvider createSerializerProvider() {
                return new DefaultSerializerProvider() {
                    @Override
                    public SerializerProvider getSerializerProvider() {
                        return this;
                    }

                    @Override
                    public BeanPropertyWriter findBeanPropertyWriter(BeanPropertyWriter.BeanPropertyWriterImpl bpw, BeanDescription beanDesc, JsonSerializer serializer) {
                        return new FilteringBeanPropertyWriter(bpw, filter);
                    }
                };
            }
        };
        ObjectNode node = ObjectMapper.getDefaultInstance().readValue(new ByteArrayInputStream(TestUtil.writeValueAsBytes("{\"foo\": \"bar\"}")), ObjectNode.class);
        provider.serializeValue(gen, node, null, null);
        gen.close();
        String actualValue = TestUtil.writeValueAsString(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("<root><foo/></root>", actualValue);
    }

    @Test
    public void testSerializeValueWithNonRootObject() throws IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        String expectedRootName = "root";
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup) {
            @Override
            protected QName _rootNameFromConfig() {
                return new QName(null, expectedRootName);
            }

            @Override
            protected void _startRootArray(ToXmlGenerator xgen, QName rootName) throws IOException {
                throw new IOException("Cannot start root array when not root");
            }
        };
        Object value = new Object();
        provider.serializeValue(gen, value);
        gen.close();
        assertNull(gen.getOutputBuffer().toString());
    }
}