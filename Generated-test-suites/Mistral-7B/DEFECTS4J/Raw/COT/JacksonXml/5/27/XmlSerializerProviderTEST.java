package com.fasterxml.jackson.dataformat.xml.ser;//package of the class under test

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootElement;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.SimpleXmlRootNameLookup;

public class XmlSerializerProviderTest {

    // Given
    private XmlMapper mapper;
    private XmlRootNameLookup rootNameLookup;
    private SerializerProvider serializerProvider;
    private ObjectNode rootNode;

    @Before
    public void setUp() throws Exception {
        rootNameLookup = new SimpleXmlRootNameLookup();
        mapper = new XmlMapper();
        mapper.setRootNameLookup(rootNameLookup);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        serializerProvider = mapper.getSerializerProvider();
        rootNode = mapper.readTree("/testData.xml"); // Assuming you have a test data XML file
    }

    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        assertNotNull(xmlSerializerProvider);
    }

    @Test
    public void testCopyConstructor() {
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copy = new XmlSerializerProvider(src);
        assertSame(src._rootNameLookup, copy._rootNameLookup);
    }

    @Test
    public void testCreateInstanceConstructor() throws Exception {
        XmlSerializerProvider xmlSerializerProvider = serializerProvider.copy();
        XmlSerializerProvider instance = new XmlSerializerProvider(xmlSerializerProvider, mapper.getSerializationConfig(), null);
        assertNotSame(xmlSerializerProvider, instance);
        assertSame(xmlSerializerProvider._rootNameLookup, instance._rootNameLookup);
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        serializerProvider.serializeValue(gen, null);
        assertNotNull(gen.getLastValue());
        assertEquals("null", gen.getLastValue().asText());
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        String rootElementName = "testRoot";
        mapper.getSerializationConfig().setRootValueSeparator(":");
        mapper.getSerializationConfig().setRootValueSeparator(rootElementName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        xmlSerializerProvider.serializeValue(gen, new Object(), null);
        assertEquals(rootElementName, gen.getLastEventName().getLocalPart());
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws Exception {
        String rootElementName = "testRoot";
        rootNameLookup = new DefaultXmlRootNameLookup(Arrays.asList(new XmlRootElement(rootElementName)));
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        xmlSerializerProvider.serializeValue(gen, new Object(), null);
        assertEquals(rootElementName, gen.getLastEventName().getLocalPart());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullSerializer() throws Exception {
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        xmlSerializerProvider.serializeValue(gen, new Object());
    }

    @Test
    public void testSerializeValueWithTypedSerializer() throws Exception {
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        xmlSerializerProvider.setSerializers(new SimpleSerializerProvider(Arrays.asList(stringSerializer)));
        xmlSerializerProvider.serializeValue(gen, "testString", valueType);
        assertEquals("testString", gen.getLastValue().asText());
    }

    @Test
    public void testSerializeArray() throws Exception {
        ObjectNode rootNode = mapper.readTree("/testDataWithArray.xml");
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        xmlSerializerProvider.serializeValue(gen, new String[]{"test1", "test2", "test3"}, mapper.getTypeFactory().constructType(String[].class));
        assertEquals("[test1, test2, test3]", gen.getLastValue().asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithEmptyRootNameFromConfig() throws Exception {
        mapper.getSerializationConfig().setRootValueSeparator("");
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        xmlSerializerProvider.serializeValue(gen, new Object(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithEmptyRootNameFromLookup() throws Exception {
        rootNameLookup = new DefaultXmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        xmlSerializerProvider.serializeValue(gen, new Object(), null);
    }

    @Test(expected = IOException.class)
    public void testSerializeValueWithIOException() throws Exception {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        doReturn(new IOException("Test IO Error")).when(gen).writeStartObject();
        xmlSerializerProvider.serializeValue(gen, new Object());
    }

    @Test(expected = XMLStreamException.class)
    public void testSerializeValueWithXMLStreamException() throws Exception {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = rootNode.getRoot().getGenerator();
        doThrow(new XMLStreamException("Test XML Stream Error")).when(gen).setDefaultNamespace("testNS");
        xmlSerializerProvider.serializeValue(gen, new Object());
    }
}