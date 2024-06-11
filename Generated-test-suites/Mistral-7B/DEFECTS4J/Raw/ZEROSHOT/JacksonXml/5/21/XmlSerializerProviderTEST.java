package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdSerializerKey;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

public class XmlSerializerProviderTest {

    private static final XmlRootNameLookup ROOT_NAME_LOOKUP = new XmlRootNameLookup();

    @Test
    public void testSerializeNull() throws IOException {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, null);
        gen.flush();
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("null", reader.getLocalName());
        assertEquals(XMLConstants.XML_NS_URI, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
    }

    @Test
    public void testSerializeObject() throws IOException, JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeString(t);
            }
        });
        ObjectMapper mapper = new XmlMapper(new DefaultSerializerProvider.Base().withActiveCustomSerializers(module));
        XmlSerializerProvider provider = mapper.getXmlSerializerProvider();

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");

        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, node);
        gen.flush();
        ByteArrayOutputStream byteStream = (ByteArrayOutputStream) gen.getOutputBuffer();
        String xmlString = byteStream.toString();
        assertEquals("<root><name>John Doe</name></root>", xmlString);

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("root", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("name", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.CHARACTERS, reader.getEventType());
        assertEquals("John Doe", reader.getText().getData());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("name", reader.getLocalName());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("root", reader.getLocalName());
    }

    @Test
    public void testSerializeObjectWithCustomRootName() throws IOException, JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeString(t);
            }
        });
        ObjectMapper mapper = new XmlMapper(new DefaultSerializerProvider.Base().withActiveCustomSerializers(module)
                .withSerializerProviderConfig(config -> config.setRootValueSerializer(new StdSerializer<Object>(Object.class) {
                    @Override
                    public void serialize(Object t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                        jg.writeStartElement("customRoot");
                        jg.writeObject(t);
                        jg.writeEndElement();
                    }
                })));
        XmlSerializerProvider provider = mapper.getXmlSerializerProvider();

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");

        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, node);
        gen.flush();
        ByteArrayOutputStream byteStream = (ByteArrayOutputStream) gen.getOutputBuffer();
        String xmlString = byteStream.toString();
        assertEquals("<customRoot><name>John Doe</name></customRoot>", xmlString);

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("customRoot", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("name", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.CHARACTERS, reader.getEventType());
        assertEquals("John Doe", reader.getText().getData());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("name", reader.getLocalName());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("customRoot", reader.getLocalName());
    }

    @Test
    public void testSerializeArray() throws IOException, JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Integer.class, new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeNumber(t);
            }
        });
        ObjectMapper mapper = new XmlMapper(new DefaultSerializerProvider.Base().withActiveCustomSerializers(module));
        XmlSerializerProvider provider = mapper.getXmlSerializerProvider();

        ObjectNode node = mapper.createObjectNode();
        node.set("name", "numbers");
        node.set("value", mapper.createValueNode(new Integer[3]));

        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, node);
        gen.flush();
        ByteArrayOutputStream byteStream = (ByteArrayOutputStream) gen.getOutputBuffer();
        String xmlString = byteStream.toString();
        assertEquals("<numbers><item><_></_></item><item><_></_></item><item><_></_></item></numbers>", xmlString);

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("numbers", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("item", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("_", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.CHARACTERS, reader.getEventType());
        assertEquals("", reader.getText().getData());
        reader.next(); // move to next element
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("item", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("_", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.CHARACTERS, reader.getEventType());
        assertEquals("", reader.getText().getData());
        reader.next(); // move to next element
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("item", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.START_ELEMENT, reader.getEventType());
        assertEquals("_", reader.getLocalName());
        assertEquals(null, reader.getNamespaceURI());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.CHARACTERS, reader.getEventType());
        assertEquals("", reader.getText().getData());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("item", reader.getLocalName());
        reader.next(); // move to next element
        assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        assertEquals("numbers", reader.getLocalName());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeObjectWithInvalidRootName() throws IOException, JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                jg.writeString(t);
            }
        });
        ObjectMapper mapper = new XmlMapper(new DefaultSerializerProvider.Base().withActiveCustomSerializers(module)
                .withSerializerProviderConfig(config -> config.setRootValueSerializer(new StdSerializer<Object>(Object.class) {
                    @Override
                    public void serialize(Object t, JsonGenerator jg, SerializerProvider provider) throws IOException {
                        jg.writeStartElement("customRoot");
                        jg.writeEndElement();
                    }
                })));
        XmlSerializerProvider provider = mapper.getXmlSerializerProvider();

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");

        JsonGenerator gen = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);
        provider.serializeValue(gen, node, new StdSerializerKey(ObjectNode.class), node.getClass(), null, null, null, null, null, null);
        gen.flush();
    }
}