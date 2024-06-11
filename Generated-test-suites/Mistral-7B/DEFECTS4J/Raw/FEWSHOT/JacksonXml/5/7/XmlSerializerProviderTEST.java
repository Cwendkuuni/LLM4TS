package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.QName;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Test cases for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProviderConstruction() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(xmlProvider);
        assertEquals(rootNameLookup, xmlProvider._rootNameLookup);

        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copy = xmlProvider.copy();
        assertEquals(src, copy);
        assertNotNull(copy._rootNameLookup);
        assertEquals(rootNameLookup, copy._rootNameLookup);

        XmlSerializerProvider src2 = new XmlSerializerProvider(src);
        XmlSerializerProvider copy2 = src.copy();
        assertEquals(src, src2);
        assertEquals(copy, copy2);
        assertNotNull(src2._rootNameLookup);
        assertEquals(rootNameLookup, src2._rootNameLookup);
    }

    @Test
    public void testRootNameFromConfig() {
        SerializationConfig config = new SerializationConfig();
        assertEquals(null, XmlSerializerProvider.ROOT_NAME_FOR_NULL);

        PropertyName rootName = new PropertyName("root");
        config.setRootName(rootName);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        QName rootNameFromConfig = xmlProvider._rootNameFromConfig();
        assertEquals(rootName.getSimpleName(), rootNameFromConfig.getLocalPart());
        assertEquals(rootName.getNamespace(), rootNameFromConfig.getNamespaceURI());
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, null);
        assertEquals("null", gen.getAsString());
    }

    @Test
    public void testSerializeValueWithoutRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object value = new Object();
        JsonSerializer<Object> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the value of 'value' and the
        // configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("root");
        config.setRootName(rootName);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object value = new Object();
        JsonSerializer<Object> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value, null, null);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the value of 'value' and the
        // configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithRootNameAndType() throws IOException {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("root");
        config.setRootName(rootName);
        JavaType rootType = null;
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object value = new Object();
        JsonSerializer<Object> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value, rootType, null);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the value of 'value' and the
        // configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithArray() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[0];
        JsonSerializer<Object[]> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, values);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithArrayAndRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("root");
        config.setRootName(rootName);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[0];
        JsonSerializer<Object[]> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, values, null, null);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithArrayAndRootType() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JavaType rootType = null;
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[0];
        JsonSerializer<Object[]> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, values, rootType, null);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the configuration of 'xmlProvider'.
    }

    @Test
    public void testSerializeValueWithArrayAndRootNameAndType() throws IOException {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("root");
        config.setRootName(rootName);
        JavaType rootType = null;
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config, null);
        Object[] values = new Object[0];
        JsonSerializer<Object[]> serializer = null;
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, values, rootType, null);
        assertNotNull(serializer);
        String xml = gen.getAsString();
        System.out.println(xml);
        // The actual XML output will depend on the configuration of 'xmlProvider'.
    }

    @Test
    public void test_initWithRootName() throws IOException, XMLStreamException {
        ToXmlGenerator xgen = new ToXmlGenerator() {
            @Override
            public void writeStartDocument(XMLStreamWriter w, String encoding) throws IOException, XMLStreamException {
                super.writeStartDocument(w, encoding);
                assertEquals("xml", w.getNamespaceURI());
            }

            @Override
            public void writeEndDocument(XMLStreamWriter w) throws IOException, XMLStreamException {
                super.writeEndDocument(w);
            }

            @Override
            public void writeStartElement(String uri, String localName, String qName, XMLStreamWriter w)
                    throws IOException, XMLStreamException {
                super.writeStartElement(uri, localName, qName, w);
                assertEquals(null, w.getNamespaceContext().getNamespaceURI(qName));
            }
        };

        String ns = "http://example.com";
        String rootName = "root";
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlSerializerProvider()) {
            @Override
            protected void _initWithRootName(ToXmlGenerator xgen, QName rootName) throws IOException {
                super._initWithRootName(xgen, rootName);
                assertEquals(ns, xgen.getStaxWriter().getDefaultNamespace());
            }
        };

        QName rootNameFromConfig = xmlProvider._rootNameFromConfig();
        assertNull(rootNameFromConfig);

        xmlProvider._initWithRootName(xgen, new QName(ns, rootName));
        assertEquals(ns, xgen.getStaxWriter().getDefaultNamespace());
    }

    @Test
    public void test_asXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new TokenBuffer();
        ToXmlGenerator xgen = XmlSerializerProvider.class.cast(new XmlSerializerProvider().copy())
                ._asXmlGenerator(gen);
        assertNull(xgen);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGeneratorWithInvalidGenerator() throws IOException {
        JsonGenerator gen = new Object();
        XmlSerializerProvider.class.cast(new XmlSerializerProvider().copy())
                ._asXmlGenerator(gen);
    }
}