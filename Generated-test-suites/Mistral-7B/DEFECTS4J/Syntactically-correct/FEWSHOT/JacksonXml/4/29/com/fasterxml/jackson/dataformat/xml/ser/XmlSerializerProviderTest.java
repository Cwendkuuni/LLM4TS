package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import static com.fasterxml.jackson.databind.SerializationFeature.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithDefaultRootName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(USE_DEFAULT_PRETTIES, false);
        mapper.configure(RELAXED_XML_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_XML_NAMESPACE, false);

        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getRootValueSerializerFactory());
        JsonSerializer<Object> serializer = mapper.getSerializerProvider().findValueSerializer(Object.class, null);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "John Doe");
        rootNode.putPOJO("person", new Person());

        JsonGenerator gen = provider.getFactory().createJsonGenerator(new TokenBuffer(), null);
        provider.serializeValue(gen, rootNode);
        gen.flush();

        String serializedXml = gen.getOutputSource().getCharacterStream().readAllBytes();
        assertNotNull(serializedXml);
        System.out.println(serializedXml);
    }

    @Test
    public void testSerializationWithSpecifiedRootName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(USE_DEFAULT_PRETTIES, false);
        mapper.configure(RELAXED_XML_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_XML_NAMESPACE, "http://example.com/ns");

        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getRootValueSerializerFactory());
        JsonSerializer<Object> serializer = mapper.getSerializerProvider().findValueSerializer(Object.class, null);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "John Doe");
        rootNode.putPOJO("person", new Person());

        String rootName = "rootElement";
        QName qName = new QName("http://example.com/ns", rootName);

        JsonGenerator gen = provider.getFactory().createJsonGenerator(new TokenBuffer(), null);
        provider.setRootName(qName);
        provider.serializeValue(gen, rootNode);
        gen.flush();

        String serializedXml = gen.getOutputSource().getCharacterStream().readAllBytes();
        assertNotNull(serializedXml);
        System.out.println(serializedXml);
    }

    @Test
    public void testSerializationWithArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(USE_DEFAULT_PRETTIES, false);
        mapper.configure(RELAXED_XML_OUTPUT, true);

        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getRootValueSerializerFactory());
        JsonSerializer<Object> serializer = mapper.getSerializerProvider().findValueSerializer(Object[].class, null);

        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode arrayNode = mapper.createObjectNode();

        arrayNode.put(0, mapper.valueToTree("Person1"));
        arrayNode.put(1, mapper.valueToTree("Person2"));

        rootNode.set("persons", arrayNode);

        String rootName = "rootElement";
        QName qName = new QName("http://example.com/ns", rootName);

        JsonGenerator gen = provider.getFactory().createJsonGenerator(new TokenBuffer(), null);
        provider.setRootName(qName);

        provider.serializeValue(gen, rootNode);
        gen.writeStartArray();
        provider.serializeValue(gen, rootNode.get("persons"));
        gen.writeEndArray();

        gen.flush();

        String serializedXml = gen.getOutputSource().getCharacterStream().readAllBytes();
        assertNotNull(serializedXml);
        System.out.println(serializedXml);
    }

    private static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}