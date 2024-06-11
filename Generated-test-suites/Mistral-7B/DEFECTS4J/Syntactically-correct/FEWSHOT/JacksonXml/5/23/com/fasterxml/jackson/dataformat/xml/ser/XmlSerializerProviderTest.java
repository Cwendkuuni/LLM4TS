package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

import java.io.IOException;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":1}");
        ObjectNode result = objectNode.set("root", objectNode);
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><name>1</name></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithDefaultRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":1}");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<null><name>1</name></null>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithNullRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":null}");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<null></null>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithCustomRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        rootNameLookup.addRootNameMapping("newRoot", "root");
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":1}");
        ObjectNode result = objectNode.set("newRoot", objectNode);
        String json = writer.writeValueAsString(result);
        String expectedXml = "<newRoot><name>1</name></newRoot>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithArrayRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer[].class, new IntArraySerializer());
        serializerFactory.registerSerializer(Integer[].class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("[1, 2, 3]");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithEmptyArrayRootName() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer[].class, new IntArraySerializer());
        serializerFactory.registerSerializer(Integer[].class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("[]");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithNullValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(String.class, new StringSerializer());
        serializerFactory.registerSerializer(String.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":null}");
        ObjectNode result = objectNode.set("name", null);
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><name xsi:nil='true'/></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithEmptyValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(String.class, new StringSerializer());
        serializerFactory.registerSerializer(String.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":\"\"}");
        ObjectNode result = objectNode.set("name", "");
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><name/></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithEmptyObject() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Object.class, new EmptyObjectSerializer());
        serializerFactory.registerSerializer(Object.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{}");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root/>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithEmptyArray() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Object[].class, new EmptyArraySerializer());
        serializerFactory.registerSerializer(Object[].class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("[]");
        ObjectNode result = objectNode;
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithObjectWithNullValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(String.class, new StringSerializer());
        serializerFactory.registerSerializer(String.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":{\"value\":null}}");
        ObjectNode result = objectNode.set("name", ObjectNode.parse("{}"));
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><name><value xsi:nil='true'/></name></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithObjectWithEmptyValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(String.class, new StringSerializer());
        serializerFactory.registerSerializer(String.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"name\":{\"value\":\"\"}}");
        ObjectNode result = objectNode.set("name", ObjectNode.parse("{}"));
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><name/></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithArrayWithNullValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"items\":[null]}");
        ObjectNode result = objectNode.set("items", ObjectNode.parser().readValue(ObjectNode[].class));
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><items xsi:nil='true'/></root>";
        assertEquals(expectedXml, json);
    }

    @Test
    public void testSerializationWithArrayWithEmptyValue() throws Exception {
        ObjectNode rootNode = TestUtil.createRootNode("root");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        SimpleModule module = new SimpleModule();
        SerializerFactory serializerFactory = serializerProvider.getFactory();
        module.addSerializer(Integer.class, new IntSerializer());
        serializerFactory.registerSerializer(Integer.class, module.getDefaultSerializer());
        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), serializerProvider);
        ObjectWriter writer = mapper.writer();
        ObjectNode objectNode = mapper.readTree("{\"items\":[]}");
        ObjectNode result = objectNode.set("items", ObjectNode.parser().readValue(ObjectNode[].class));
        String json = writer.writeValueAsString(result);
        String expectedXml = "<root><items></items></root>";
        assertEquals(expectedXml, json);
    }

    private static class IntSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value);
        }
    }

    private static class IntArraySerializer extends JsonSerializer<Integer[]> {
        @Override
        public void serialize(Integer[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Integer intValue : value) {
                serializeInt(intValue, gen, serializers);
            }
            gen.writeEndArray();
        }

        private void serializeInt(Integer intValue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(intValue);
        }
    }

    private static class EmptyObjectSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

    private static class EmptyArraySerializer extends JsonSerializer<Object[]> {
        @Override
        public void serialize(Object[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            gen.writeEndArray();
        }
    }

    private static class StringSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value);
        }
    }

}