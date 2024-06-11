package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamConstants;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonVersion;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.RootNameIT;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootName() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(RootNameIT.class, new RootNameSerializer());
        mapper.registerModule(module);
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(new DefaultXmlRootNameLookup()).getRootValueSerializer());

        RootNameIT obj = new RootNameIT();
        String xml = mapper.writeValueAsString(obj);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, obj);
        gen.close();
        assertEquals(xml, baos.toString());
    }

    @Test
    public void testSerializationWithNullRootName() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Object.class, new NullRootNameSerializer());
        mapper.registerModule(module);
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(null).getRootValueSerializer());

        String xml = mapper.writeValueAsString(null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, null);
        gen.close();
        assertEquals("<null/>", baos.toString());
    }

    @Test
    public void testSerializationWithArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(null).getRootValueSerializer());

        int[] ints = {1, 2, 3};
        String xml = mapper.writeValueAsString(ints);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, ints);
        gen.close();
        assertEquals("<int name=\"item\">1</int><int name=\"item\">2</int><int name=\"item\">3</int>", baos.toString());
    }

    @Test
    public void testSerializationWithEmptyArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(null).getRootValueSerializer());

        int[] ints = {};
        String xml = mapper.writeValueAsString(ints);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, ints);
        gen.close();
        assertEquals("<int name=\"item\"/>", baos.toString());
    }

    @Test
    public void testSerializationWithCustomRootName() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(new DefaultXmlRootNameLookup("root")).getRootValueSerializer());

        String xml = mapper.writeValueAsString("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, "test");
        gen.close();
        assertEquals("<root>test</root>", baos.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(null).getRootValueSerializer());

        String xml = mapper.writeValueAsString(null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, null);
        gen.close();
        // JSON mapping exception should be thrown, as null value should not be serialized
        mapper.writeValue(gen, null);
    }

    @Test
    public void testSerializationWithCustomRootValueSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new MyCustomRootValueSerializer());

        String xml = mapper.writeValueAsString("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, "test");
        gen.close();
        assertEquals(xml, baos.toString());
    }

    @Test
    public void testSerializationWithRootNameFromConfig() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new XmlSerializerProvider(new DefaultXmlRootNameLookup()).getRootValueSerializer());
        config.setRootValueSerializer(new XmlSerializerProvider(new DefaultXmlRootNameLookup("custom")).getRootValueSerializer());

        String xml = mapper.writeValueAsString("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(baos);
        mapper.writeValue(gen, "test");
        gen.close();
        assertEquals("<custom>test</custom>", baos.toString());
    }

    public static class RootNameSerializer extends StdSerializer<RootNameIT> {
        public RootNameSerializer() {
            this(null);
        }

        public RootNameSerializer(Class<RootNameIT> t, JsonSerializer.ValueInspector valueInspector) {
            super(t, valueInspector);
        }

        @Override
        public void serialize(RootNameIT value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("name");
            gen.writeString("RootNameIT");
            gen.writeEndObject();
        }
    }

    public static class NullRootNameSerializer extends StdSerializer<Object> {
        public NullRootNameSerializer() {
            this(null);
        }

        public NullRootNameSerializer(Class<Object> t, JsonSerializer.ValueInspector valueInspector) {
            super(t, valueInspector);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("name");
            gen.writeNull();
            gen.writeEndObject();
        }
    }

    public static class MyCustomRootValueSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("customRoot");
            gen.writeValueAsString("customValue");
            gen.writeEndObject();
        }
    }
}