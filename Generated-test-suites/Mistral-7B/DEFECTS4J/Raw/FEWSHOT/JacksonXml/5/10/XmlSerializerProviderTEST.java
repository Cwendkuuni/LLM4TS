package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(xmlSerializerProvider);
    }

    @Test
    public void testCopy() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copy = src.copy();
        assertTrue(copy instanceof XmlSerializerProvider);
        assertTrue(copy._rootNameLookup == src._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider instance = XmlSerializerProvider.createInstance(src._config, src._factory);
        assertTrue(instance instanceof XmlSerializerProvider);
        assertTrue(instance._rootNameLookup == src._rootNameLookup);
    }

    @Test
    public void testSerializeValueWithRootName() throws JsonProcessingException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(xmlSerializerProvider);
        JsonNode rootNode = objectMapper.readTree("{\"name\":\"test\"}");
        String rootName = "root";
        QName qname = new QName(rootName);
        JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String.class, null);
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(new TokenBuffer(new StringWriter()));
        xmlSerializerProvider._initWithRootName(toXmlGenerator, qname);
        jsonSerializer.serialize(rootNode.get(0).asText(), toXmlGenerator, xmlSerializerProvider);
        String serializedXml = toXmlGenerator.getAsString();
        assertTrue(serializedXml.startsWith("<root>" + rootNode.get(0).asText() + "</root>"));
    }

    @Test
    public void testSerializeValueWithRootType() throws JsonProcessingException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(xmlSerializerProvider);
        JsonNode rootNode = objectMapper.readTree("{\"name\":\"test\"}");
        String rootName = "root";
        QName qname = new QName(rootName);
        JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String.class, null);
        JavaType rootType = objectMapper.getTypeFactory().constructType(Object[].class);
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(new TokenBuffer(new StringWriter()));
        xmlSerializerProvider._initWithRootName(toXmlGenerator, qname);
        jsonSerializer.serialize(rootNode.get(0).asText(), toXmlGenerator, xmlSerializerProvider, rootType);
        String serializedXml = toXmlGenerator.getAsString();
        assertTrue(serializedXml.startsWith("<root><item>" + rootNode.get(0).asText() + "</item></root>"));
    }

    @Test
    public void testSerializeNullValue() throws JsonProcessingException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(xmlSerializerProvider);
        JsonNode rootNode = objectMapper.readTree("{}");
        String rootName = "root";
        QName qname = new QName(rootName);
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(new TokenBuffer(new StringWriter()));
        xmlSerializerProvider._serializeXmlNull(toXmlGenerator);
        xmlSerializerProvider._initWithRootName(toXmlGenerator, qname);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", null);
        jsonSerializer.serialize(node, toXmlGenerator, xmlSerializerProvider);
        String serializedXml = toXmlGenerator.getAsString();
        assertTrue(serializedXml.startsWith("<root><null/></root>"));
    }

    @Test
    public void testStartRootArray() throws JsonProcessingException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper objectMapper = new ObjectMapper(xmlSerializerProvider);
        JsonNode rootNode = objectMapper.readTree("{\"name\":[1, 2, 3]}");
        String rootName = "root";
        QName qname = new QName(rootName);
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(new TokenBuffer(new StringWriter()));
        xmlSerializerProvider._startRootArray(toXmlGenerator, qname);
        jsonSerializer.serialize(rootNode.get(0).get(0).asText(), toXmlGenerator, xmlSerializerProvider);
        jsonSerializer.serialize(rootNode.get(0).get(1).asText(), toXmlGenerator, xmlSerializerProvider);
        jsonSerializer.serialize(rootNode.get(0).get(2).asText(), toXmlGenerator, xmlSerializerProvider);
        jsonSerializer.serialize(rootNode.get(1).get(0).asText(), toXmlGenerator, xmlSerializerProvider);
        String serializedXml = toXmlGenerator.getAsString();
        assertTrue(serializedXml.startsWith("<root><item><item>1</item><item>2</item><item>3</item></item><item><item>1</item></item></root>"));
    }
}