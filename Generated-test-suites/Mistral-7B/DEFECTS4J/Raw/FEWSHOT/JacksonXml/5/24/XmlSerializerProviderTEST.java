package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootName() throws Exception {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> clazz, SerializationConfig config) {
                return new QName("test", "MyRoot");
            }
        });

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
        JsonGenerator gen = StaxUtil.createGenerator();

        serializer.serialize(rootNode, gen, provider);

        gen.flush();
        String serialized = gen.getBuffer().asString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MyRoot><value>test</value></MyRoot>";
        assertTrue(serialized.equals(expected));
    }

    @Test
    public void testSerializationWithNullRootName() throws Exception {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> clazz, SerializationConfig config) {
                return null;
            }
        });

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
        JsonGenerator gen = StaxUtil.createGenerator();

        serializer.serialize(rootNode, gen, provider);

        gen.flush();
        String serialized = gen.getBuffer().asString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null><value>test</value></null>";
        assertTrue(serialized.equals(expected));
    }

    @Test
    public void testSerializationWithArray() throws Exception {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("items", JsonProcessing.mapper().valueToTree(new Integer[3]));

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
        JsonGenerator gen = StaxUtil.createGenerator();

        serializer.serialize(rootNode, gen, provider);

        gen.flush();
        String serialized = gen.getBuffer().asString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<items>\n<item></item>\n<item></item>\n<item></item>\n</items>";
        assertTrue(serialized.equals(expected));
    }

    @Test
    public void testSerializationWithNull() throws Exception {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});

        JsonSerializer<Void> serializer = provider.findValueSerializer(Void.class, null);

        JsonGenerator gen = StaxUtil.createGenerator();
        serializer.serialize(null, gen, provider);

        gen.flush();
        String serialized = gen.getBuffer().asString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";
        assertTrue(serialized.equals(expected));
    }

    @Test
    public void testSerializationWithInvalidXml() throws Exception {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("xml", "&lt;invalid-xml&gt;");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
        JsonGenerator gen = StaxUtil.createGenerator();

        try {
            serializer.serialize(rootNode, gen, provider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Invalid xml"));
        }

        gen.close();
    }

    @Test
    public void testSerializationWithNullGenerator() {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);

        try {
            serializer.serialize(rootNode, null, provider);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("JsonGenerator cannot be null"));
        }
    }

    @Test
    public void testSerializationWithNullRootNameLookup() {
        SerializerProvider provider = new XmlSerializerProvider(null);

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);

        try {
            serializer.serialize(rootNode, StaxUtil.createGenerator(), provider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Root name lookup cannot be null"));
        }
    }

    @Test
    public void testSerializationWithNullSerializationConfig() {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});
        SerializationConfig config = null;

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);

        try {
            serializer.serialize(rootNode, StaxUtil.createGenerator(), config);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Serialization config cannot be null"));
        }
    }

    @Test
    public void testSerializationWithNullSerializerFactory() {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});
        SerializerFactory factory = null;

        ObjectNode rootNode = JsonProcessing.mapper().createObjectNode();
        rootNode.put("value", "test");

        JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);

        try {
            serializer.serialize(rootNode, StaxUtil.createGenerator(), provider, factory);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Serializer factory cannot be null"));
        }
    }

    @Test
    public void testSerializationWithNullRootName() throws IOException, XMLStreamException {
        SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {});

        JsonSerializer<Void> serializer = provider.findValueSerializer(Void.class, null);

        ToXmlGenerator generator = StaxUtil.createGenerator();

        try {
            serializer.serialize(null, generator, provider);
            fail("Expected IOException");
        } catch (IOException e) {
            assertNull(generator.getDocumentRootElement());
        }
    }
}