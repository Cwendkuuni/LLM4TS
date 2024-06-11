package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.databind.util.TestNameResolver;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.util.Arrays;

public class XmlSerializerProviderTest extends TestBase {

    @Test
    public void testSerializationWithRootName() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());
        ObjectMapper mapper = new ObjectMapper(provider);

        QName rootName = new QName("test", "root");

        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        String[] values = {"value1", "value2"};
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter(SimpleBeanPropertyFilter.serializeAll());
        JsonSerializer<Object> serializer = mapper.getValueSerializerFactory()
                .findValueSerializer(javaType, filterProvider);

        String xml = mapper.writeValueAsString(values, rootName);
        assertNotNull(xml);
        assertNotNull(xml.substring(0, xml.indexOf(">") + 1));
        assertEquals("<root><item>value1</item><item>value2</item></root>", xml);
    }

    @Test
    public void testSerializationWithNullRootName() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        String xml = mapper.writeValueAsString(null);
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializationWithDefaultRootName() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        QName rootName = null;

        String xml = mapper.writeValueAsString(new Object());

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, rootName);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNotNull(rootNameUri);
        assertEquals("", rootNameLocal);

        provider._serializeXmlNull(gen);

        String expectedRootName = "<null>";
        assertEquals(expectedRootName, gen.getText().toString());
    }

    @Test
    public void testSerializationWithRootNameFromConfig() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator(new PropertyName("root"));

        String xml = mapper.writeValueAsString(new Object());

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, null);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNull(rootNameUri);
        assertEquals("root", rootNameLocal);

        provider._serializeXmlNull(gen);

        String expectedRootName = "<root><null/></root>";
        assertEquals(expectedRootName, gen.getText().toString());
    }

    @Test
    public void testSerializationWithRootNameFromClass() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        class Root {
            String name;
        }

        QName rootName = new QName("test", "Root");

        String xml = mapper.writeValueAsString(new Root());

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, rootName);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNotNull(rootNameUri);
        assertEquals("test", rootNameUri);
        assertEquals("Root", rootNameLocal);

        provider._serializeXmlNull(gen);

        String expectedRootName = "<Root><null/></Root>";
        assertEquals(expectedRootName, gen.getText().toString());
    }

    @Test
    public void testSerializationWithCustomRootNameLookup() throws IOException, XMLStreamException {
        class CustomRootNameLookup extends XmlRootNameLookup {
            @Override
            public QName findRootName(JavaType rootType, SerializationConfig config) {
                return new QName("test", "CustomRoot");
            }
        }

        XmlSerializerProvider provider = new XmlSerializerProvider(new CustomRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        String xml = mapper.writeValueAsString(new Object());

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, null);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNotNull(rootNameUri);
        assertEquals("test", rootNameUri);
        assertEquals("CustomRoot", rootNameLocal);

        provider._serializeXmlNull(gen);

        String expectedRootName = "<CustomRoot><null/></CustomRoot>";
        assertEquals(expectedRootName, gen.getText().toString());
    }

    @Test
    public void testSerializationWithArray() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        String[] values = {"value1", "value2"};

        String xml = mapper.writeValueAsString(values, new QName("test", "root"));

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, null);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNull(rootNameUri);
        assertEquals("root", rootNameLocal);

        provider._startRootArray(gen, null);

        String firstItem = xml.substring(xml.indexOf("item") + 5, xml.indexOf("item", xml.indexOf("item") + 1));
        assertEquals("value1", firstItem);

        String secondItem = xml.substring(xml.lastIndexOf("item") - 5, xml.lastIndexOf("item"));
        assertEquals("value2", secondItem);

        provider._serializeXmlNull(gen);

        String lastItem = gen.getText().toString();
        assertEquals("<item null=\"true\"/>", lastItem);

        gen.writeEndDocument();

        String expectedXml = "<root><item>value1</item><item>value2</item><item null=\"true\"/></root>";
        assertEquals(expectedXml, gen.getText().toString());
    }

    @Test
    public void testSerializationWithCustomNullSerializer() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new DefaultXmlRootNameLookup());

        ObjectMapper mapper = new ObjectMapper(provider);

        JsonSerializer<Object> nullSerializer = new NullSerializer();
        mapper.setSerializerProvider(provider);
        mapper.registerSerializer(Null.class, nullSerializer);

        String xml = mapper.writeValueAsString(null);

        ToXmlGenerator gen = StaxUtil.createToXmlGenerator(xml);
        provider._initWithRootName(gen, null);

        String rootNameUri = gen.getNamespaceURI();
        String rootNameLocal = gen.getLocalName();

        assertNull(rootNameUri);
        assertEquals("", rootNameLocal);

        String expectedRootName = "<null/>";
        assertEquals(expectedRootName, gen.getText().toString());
    }
}