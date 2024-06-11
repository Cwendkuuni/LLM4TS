package com.fasterxml.jackson.dataformat.xml.ser; // Import necessary packages here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig.Feature;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookupTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for XmlSerializerProvider class.
 */
public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlRootNameLookup xmlRootNameLookup;
    private XmlSerializerProvider xmlSerializerProvider;
    private SerializationConfig serializationConfig;
    private SerializerFactory serializerFactory;

    @Before
    public void setup() {
        xmlRootNameLookup = new XmlRootNameLookupTest.SimpleXmlRootNameLookup();
        objectMapper = new XmlMapper();
        objectMapper.setRootValueConverter(new XmlRootNameLookup.DefaultXmlRootNameLookup(xmlRootNameLookup));
        serializationConfig = objectMapper.getSerializationConfig();
        serializerFactory = objectMapper.getSerializerFactory();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    /**
     * Test constructor when given XmlRootNameLookup as an argument.
     */
    @Test
    public void testXmlSerializerProviderWithXmlRootNameLookup() {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);
        assertNotNull(provider);
    }

    /**
     * Test constructor when given another XmlSerializerProvider, SerializationConfig, and SerializerFactory as arguments.
     */
    @Test
    public void testXmlSerializerProviderWithXmlSerializerProvider() {
        XmlSerializerProvider provider1 = new XmlSerializerProvider(xmlRootNameLookup);
        XmlSerializerProvider provider2 = new XmlSerializerProvider(provider1, serializationConfig, serializerFactory);
        assertNotNull(provider2);
        assertEquals(provider1, provider2.getProvider());
    }

    /**
     * Test constructor when given another XmlSerializerProvider as an argument.
     */
    @Test
    public void testXmlSerializerProviderWithXmlSerializerProviderWithoutCopiedRootNameLookup() {
        XmlSerializerProvider provider1 = new XmlSerializerProvider(xmlRootNameLookup);
        XmlSerializerProvider provider2 = new XmlSerializerProvider(provider1);
        assertNotNull(provider2);
        assertNotSame(provider1._rootNameLookup, provider2._rootNameLookup);
    }

    /**
     * Test copy() method.
     */
    @Test
    public void testCopy() {
        XmlSerializerProvider provider1 = new XmlSerializerProvider(xmlRootNameLookup);
        XmlSerializerProvider provider2 = provider1.copy();
        assertNotNull(provider2);
        assertEquals(provider1, provider2.getProvider());
    }

    /**
     * Test createInstance() method.
     */
    @Test
    public void testCreateInstance() {
        XmlSerializerProvider provider1 = new XmlSerializerProvider(xmlRootNameLookup);
        XmlSerializerProvider provider2 = xmlSerializerProvider.createInstance(serializationConfig, serializerFactory);
        assertNotNull(provider2);
        assertEquals(provider1, provider2.getProvider());
    }

    /**
     * Test serializeValue() method with null value.
     */
    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        xmlSerializerProvider.serializeValue(gen, null);
        assertEquals("<null/>", gen.getOutputText());
    }

    /**
     * Test serializeValue() method with non-null value.
     */
    @Test
    public void testSerializeValueWithNonNullValue() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        String value = "test value";
        xmlSerializerProvider.serializeValue(gen, value);
        assertEquals("<root>" + value + "</root>", gen.getOutputText());
    }

    /**
     * Test serializeValue() method with non-null value and explicit root name.
     */
    @Test
    public void testSerializeValueWithExplicitRootName() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"), "testRoot");
        String value = "test value";
        xmlSerializerProvider.serializeValue(gen, value);
        assertEquals("<testRoot>" + value + "</testRoot>", gen.getOutputText());
    }

    /**
     * Test serializeValue() method with JavaType and JsonSerializer.
     */
    @Test
    public void testSerializeValueWithJavaTypeAndJsonSerializer() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"), new JavaType(String[].class));
        String[] values = {"value1", "value2"};
        xmlSerializerProvider.serializeValue(gen, values, null, null);
        assertEquals("<root><item>value1</item><item>value2</item></root>", gen.getOutputText());
    }

    /**
     * Test _serializeXmlNull() method.
     */
    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        xmlSerializerProvider._serializeXmlNull(gen);
        assertEquals("<null/>", gen.getOutputText());
    }

    /**
     * Test _startRootArray() method with null root name.
     */
    @Test(expected = RuntimeException.class)
    public void testStartRootArrayWithNullRootName() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"), new JavaType(String[].class));
        xmlSerializerProvider._startRootArray(gen, null);
    }

    /**
     * Test _initWithRootName() method with null root name.
     */
    @Test(expected = RuntimeException.class)
    public void testInitWithRootNameWithNullRootName() throws IOException, XMLStreamException {
        ToXmlGenerator xgen = _createTestToXmlGenerator();
        xmlSerializerProvider._initWithRootName(xgen, null);
    }

    /**
     * Test _initWithRootName() method with null root name and in root.
     */
    @Test(expected = RuntimeException.class)
    public void testInitWithRootNameWithNullRootNameInRoot() throws IOException, XMLStreamException {
        ToXmlGenerator xgen = _createTestToXmlGenerator();
        xgen.setInRoot(true);
        xmlSerializerProvider._initWithRootName(xgen, null);
    }

    /**
     * Test _rootNameFromConfig() method with null config.
     */
    @Test
    public void testRootNameFromConfigWithNullConfig() {
        assertNull(xmlSerializerProvider._rootNameFromConfig());
    }

    /**
     * Test _rootNameFromConfig() method with root name.
     */
    @Test
    public void testRootNameFromConfigWithRootName() {
        PropertyName rootName = new PropertyName("root");
        SerializationConfig config = serializationConfig.withRootValue(new PropertyName("root"));
        assertNotNull(xmlSerializerProvider._rootNameFromConfig());
        assertEquals(rootName.getSimpleName(), xmlSerializerProvider._rootNameFromConfig().getLocalPart());
    }

    /**
     * Test _asXmlGenerator() method with null JsonGenerator.
     */
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithNullJsonGenerator() throws IOException {
        JsonGenerator gen = null;
        xmlSerializerProvider._asXmlGenerator(gen);
    }

    /**
     * Test _asXmlGenerator() method with ToXmlGenerator.
     */
    @Test
    public void testAsXmlGeneratorWithToXmlGenerator() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        ToXmlGenerator xgen = (ToXmlGenerator) xmlSerializerProvider._asXmlGenerator(gen);
        assertNotNull(xgen);
    }

    /**
     * Test _wrapAsIOE() method with IOException.
     */
    @Test
    public void testWrapAsIOEWithIOException() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        Exception e = new IOException();
        assertEquals(IOException.class, xmlSerializerProvider._wrapAsIOE(gen, e).getClass());
    }

    /**
     * Test _wrapAsIOE() method with other exceptions.
     */
    @Test
    public void testWrapAsIOEWithOtherExceptions() throws IOException {
        JsonGenerator gen = _createTestJsonGenerator(new PropertyName("root"));
        Exception e = new RuntimeException();
        assertEquals(JsonMappingException.class, xmlSerializerProvider._wrapAsIOE(gen, e).getClass());
    }

    private JsonGenerator _createTestJsonGenerator(PropertyName rootName, String namespaceURI) throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonGenerator gen = jsonFactory.createXmlGenerator(new StringWriter());
        if (namespaceURI != null) {
            gen.setDefaultNamespace(namespaceURI);
        }
        if (rootName != null) {
            gen.setRootElementName(rootName);
        }
        return gen;
    }

    private JsonGenerator _createTestJsonGenerator(PropertyName rootName) throws IOException {
        return _createTestJsonGenerator(rootName, null);
    }

    private ToXmlGenerator _createTestToXmlGenerator() throws IOException, XMLStreamException {
        TokenBuffer tokenBuffer = new TokenBuffer(objectMapper.getFactory().getXmlFactory().createXmlParser(new StringReader("")));
        return (ToXmlGenerator) StaxUtil.createDefaultXmlGenerator(tokenBuffer);
    }
}