package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    private SerializationConfig _config;
    private DeserializationConfig _deserializationConfig;
    private XmlSerializerProvider _serializerProvider;
    private XmlRootNameLookup _rootNameLookup;
    private JsonFactory _jsonFactory;

    @Before
    public void setup() {
        _config = new SerializationConfig();
        _deserializationConfig = new DeserializationConfig();
        _rootNameLookup = new XmlRootNameLookup();
        _jsonFactory = new JsonFactory(_config);
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup);
    }

    // Test for constructor with empty argument
    @Test
    public void testXmlSerializerProviderEmptyConstructor() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider();
        assertNull(serializerProvider._rootNameLookup);
    }

    // Test for constructor with XmlRootNameLookup argument
    @Test
    public void testXmlSerializerProviderWithXmlRootNameLookup() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(_rootNameLookup);
        assertEquals(_rootNameLookup, serializerProvider._rootNameLookup);
    }

    // Test for copy constructor
    @Test
    public void testXmlSerializerProviderCopyConstructor() {
        XmlSerializerProvider src = new XmlSerializerProvider(_rootNameLookup);
        XmlSerializerProvider serializedProvider = src.copy();
        assertEquals(src, serializedProvider);
    }

    // Test for createInstance method
    @Test
    public void testCreateInstance() {
        SerializerProvider src = new XmlSerializerProvider(_rootNameLookup);
        XmlSerializerProvider serializedProvider = XmlSerializerProvider.createInstance(_config, _jsonFactory);
        assertEquals(src, serializedProvider);
    }

    // Test for overridden copy method
    @Test
    public void testCopy() {
        XmlSerializerProvider src = new XmlSerializerProvider(_rootNameLookup);
        XmlSerializerProvider serializedProvider = src.copy();
        assertEquals(src, serializedProvider);
    }

    // Test for serializeValue method with null value
    @Test
    public void testSerializeValueNull() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider.serializeValue(gen, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></null>", gen.getOutputSaveStream().toString());
    }

    // Test for serializeValue method with non-null value
    @Test
    public void testSerializeValue() throws IOException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><value>test</value></root>";
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider.serializeValue(gen, "test");
        assertEquals(expected, gen.getOutputSaveStream().toString());
    }

    // Test for serializeValue method with non-null value and rootType and serializer
    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
        JavaType rootType = _deserializationConfig.constructType(String[].class);
        JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("test");
            }
        };
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><value>test</value></root>";
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider.serializeValue(gen, "test", rootType, serializer);
        assertEquals(expected, gen.getOutputSaveStream().toString());
    }

    // Test for _serializeXmlNull method
    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider._serializeXmlNull(gen);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></null>", gen.getOutputSaveStream().toString());
    }

    // Test for _startRootArray method
    @Test
    public void testStartRootArray() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider._startRootArray(gen, new QName("root"));
        assertEquals("<root>", gen.getOutputSaveStream().toString());
    }

    // Test for _initWithRootName method
    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        ToXmlGenerator xgen = StaxUtil.createToXmlGenerator(_jsonFactory.createGenerator(new ByteArrayOutputStream()));
        _serializerProvider._initWithRootName(xgen, new QName("root"));
        assertEquals("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">", xgen.getOutputSaveStream().toString());
    }

    // Test for _rootNameFromConfig method
    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName("root");
        QName expected = new QName("root");
        assertEquals(expected, _serializerProvider._rootNameFromConfig());

        PropertyName nameWithNamespace = new PropertyName("root", "http://example.com/namespace");
        QName expectedWithNamespace = new QName("http://example.com/namespace", "root");
        assertEquals(expectedWithNamespace, _serializerProvider._rootNameFromConfig());
    }

    // Test for _asXmlGenerator method
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorInvalidGenerator() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream());
        _serializerProvider._asXmlGenerator(gen);
    }

    // Test for _wrapAsIOE method
    @Test
    public void testWrapAsIOE() throws IOException {
        IOException e = new IOException("Test IOException");
        JsonMappingException exception = _serializerProvider._wrapAsIOE(null, e);
        assertEquals("Test IOException", exception.getMessage());
    }

    // Test for edge cases - null root name from config
    @Test
    public void testNullRootNameFromConfig() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider.serializeValue(gen, new Object());
        assertEquals("", gen.getOutputSaveStream().toString());
    }

    // Test for edge cases - null root name lookup
    @Test(expected = JsonMappingException.class)
    public void testNullRootNameLookup() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(null);
        try {
            serializerProvider.serializeValue(null, new Object());
        } catch (IOException e) {
            throw _serializerProvider._wrapAsIOE(null, e);
        }
    }

    // Test for edge cases - root name not set in generator
    @Test
    public void testRootNameNotSetInGenerator() throws IOException {
        ToXmlGenerator xgen = StaxUtil.createToXmlGenerator(_jsonFactory.createGenerator(new ByteArrayOutputStream()));
        _serializerProvider._initWithRootName(xgen, null);
        assertEquals("", xgen.getOutputSaveStream().toString());
    }

    // Test for edge cases - null generator
    @Test(expected = JsonMappingException.class)
    public void testNullGenerator() {
        _serializerProvider.serializeValue(null, new Object());
    }

    // Test for edge cases - null value
    @Test
    public void testNullValue() throws IOException {
        JsonGenerator gen = _jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        _serializerProvider.serializeValue(gen, null);
        assertEquals("", gen.getOutputSaveStream().toString());
    }

    // Test for edge cases - null rootType
    @Test(expected = JsonMappingException.class)
    public void testNullRootType() throws IOException {
        JavaType rootType = null;
        JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("test");
            }
        };
        _serializerProvider.serializeValue(null, null, rootType, serializer);
    }

    // Test for edge cases - null serializer
    @Test(expected = JsonMappingException.class)
    public void testNullSerializer() throws IOException {
        JavaType rootType = _deserializationConfig.constructType(String[].class);
        JsonSerializer<Object> serializer = null;
        _serializerProvider.serializeValue(null, null, rootType, serializer);
    }
}