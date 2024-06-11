package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerMap;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootNameFromConfig() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("--");
        config.setRootName("rootElement");

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putPOJO("myObject", new MyObject());

        JsonSerializer<Object> serializer = provider.findValueSerializer(MyObject.class, null);
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);

        provider.serializeValue(gen, node, MyObject.class, serializer);

        String xml = gen.getOutputContent();
        assertTrue(xml.startsWith("<rootElement>"));
        assertTrue(xml.endsWith("</rootElement>"));
    }

    @Test
    public void testSerializationWithRootNameFromLookup() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("--");

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        rootNameLookup.addRootNameMapping(MyObject.class, "myObjectElement");

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putPOJO("myObject", new MyObject());

        JsonSerializer<Object> serializer = provider.findValueSerializer(MyObject.class, null);
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);

        provider.serializeValue(gen, node, MyObject.class, serializer);

        String xml = gen.getOutputContent();
        assertTrue(xml.startsWith("<myObjectElement>"));
        assertTrue(xml.endsWith("</myObjectElement>"));
    }

    @Test
    public void testSerializationWithNullRootName() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("--");

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putPOJO("myObject", new MyObject());

        JsonSerializer<Object> serializer = provider.findValueSerializer(MyObject.class, null);
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);

        provider.serializeValue(gen, node, null, serializer);

        String xml = gen.getOutputContent();
        assertEquals("<null>", xml);
    }

    @Test
    public void testSerializationAsArray() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("--");

        XmlSerializerProvider provider = new XmlSerializerProvider(null, config);

        ObjectNode rootNode = new ObjectMapper().createObjectNode();
        rootNode.set("items", new ObjectMapper().createValueFactory().asArray());

        ObjectNode itemNode1 = rootNode.set("items", new ObjectMapper().createObjectNode().set("name", "item1"));
        ObjectNode itemNode2 = rootNode.set("items", new ObjectMapper().createObjectNode().set("name", "item2"));

        JavaType javaType = new TypeFactory().constructType(List.class.getName() + "<MyObject>");

        JsonSerializer<Object> serializer = provider.findValueSerializer(MyObject.class, javaType);
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);

        provider.serializeValue(gen, rootNode, javaType, serializer);

        String xml = gen.getOutputContent();
        assertTrue(xml.startsWith("<items>"));
        assertTrue(xml.endsWith("</items>"));
        assertTrue(xml.contains("<item>item1</item>"));
        assertTrue(xml.contains("<item>item2</item>"));
    }

    @Test
    public void testSerializationWithRootNameFromConfigAndAsArray() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("--");
        config.setRootName("rootElement");

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectNode rootNode = new ObjectMapper().createObjectNode();
        rootNode.set("items", new ObjectMapper().createValueFactory().asArray());

        ObjectNode itemNode1 = rootNode.set("items", new ObjectMapper().createObjectNode().set("name", "item1"));
        ObjectNode itemNode2 = rootNode.set("items", new ObjectMapper().createObjectNode().set("name", "item2"));

        JavaType javaType = new TypeFactory().constructType(List.class.getName() + "<MyObject>");

        JsonSerializer<Object> serializer = provider.findValueSerializer(MyObject.class, javaType);
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);

        provider.serializeValue(gen, rootNode, javaType, serializer);

        String xml = gen.getOutputContent();
        assertTrue(xml.startsWith("<rootElement>"));
        assertTrue(xml.endsWith("</rootElement>"));
        assertTrue(xml.contains("<items>"));
        assertTrue(xml.contains("<item>item1</item>"));
        assertTrue(xml.contains("<item>item2</item>"));
    }

    @Test
    public void testSerializationWithNullConfig() throws IOException, JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        JsonGenerator gen = new JsonFactory(null).createGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, new Object(), null);

        String xml = gen.getOutputContent();
        assertNull(xml);
    }

    @Test
    public void testSerializationWithNullObject() throws IOException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();

        XmlSerializerProvider provider = new XmlSerializerProvider(null, config);

        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, null, null);

        String xml = gen.getOutputContent();
        assertEquals("<null>", xml);
    }
}

class MyObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}