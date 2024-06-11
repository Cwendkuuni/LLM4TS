package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    @Mock
    private XmlRootNameLookup _rootNameLookup;

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    private SerializationConfig config;
    private SerializerFactory factory;

    @Before
    public void setup() {
        config = new SerializationConfig();
        factory = new DefaultSerializerProvider.StdSerializerFactory();
    }

    // Testing constructor
    @Test
    public void testXmlSerializerProviderConstructor() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                _rootNameLookup);
        assertNotNull(xmlSerializerProvider);
    }

    // Testing copy constructor
    @Test
    public void testXmlSerializerProviderCopyConstructor() {
        XmlSerializerProvider src = new XmlSerializerProvider(config, factory);
        XmlSerializerProvider copy = new XmlSerializerProvider(src);
        assertEquals(src, copy);
    }

    // Testing serializeValue method with null value
    @Test
    public void testSerializeValueWithNullValue() throws IOException,
            JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        xmlSerializerProvider.serializeValue(gen, null);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }

    // Testing serializeValue method with non-null value and rootType is null
    @Test
    public void testSerializeValueWithNonNullValueAndRootTypeIsNull()
            throws IOException, JsonProcessingException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        xmlSerializerProvider.serializeValue(gen, value, null, ser);
        verify(gen).writeStartObject();
        ser.serialize(value, gen, xmlSerializerProvider);
        verify(gen).writeEndObject();
    }

    // Testing serializeValue method with non-null value and rootType is not null
    @Test
    public void testSerializeValueWithNonNullValueAndRootTypeIsNotNull()
            throws IOException, JsonProcessingException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
        verify(gen).writeStartObject();
        ser.serialize(value, gen, xmlSerializerProvider);
        verify(gen).writeEndObject();
    }

    // Testing _serializeXmlNull method with null JsonGenerator
    @Test(expected = NullPointerException.class)
    public void testSerializeXmlNullWithNullJsonGenerator() throws IOException {
        xmlSerializerProvider._serializeXmlNull(null);
    }

    // Testing _serializeXmlNull method with JsonGenerator that is not an instance of ToXmlGenerator
    @Test(expected = JsonMappingException.class)
    public void testSerializeXmlNullWithJsonGeneratorThatIsNotAnInstanceOfToXmlGenerator()
            throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isWritingRootValue()).thenReturn(true);
        when(gen.writeStartObject()).thenThrow(new IOException("Test error"));
        xmlSerializerProvider._serializeXmlNull(gen);
    }

    // Testing _startRootArray method with null ToXmlGenerator
    @Test(expected = NullPointerException.class)
    public void testStartRootArrayWithNullToXmlGenerator() throws IOException {
        ToXmlGenerator xgen = null;
        xmlSerializerProvider._startRootArray(xgen, null);
    }

    // Testing _initWithRootName method with null ToXmlGenerator
    @Test(expected = NullPointerException.class)
    public void testInitWithRootNameWithNullToXmlGenerator() throws IOException,
            XMLStreamException {
        ToXmlGenerator xgen = null;
        QName rootName = new QName("namespace", "rootName");
        xmlSerializerProvider._initWithRootName(xgen, rootName);
    }

    // Testing _initWithRootName method with ToXmlGenerator that cannot set next name
    @Test
    public void testInitWithRootNameWithToXmlGeneratorThatCantSetNextName()
            throws IOException, XMLStreamException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.isWritingRootValue()).thenReturn(true);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(xgen.setNextName(any(QName.class))).thenReturn(true);
        when(xgen.initGenerator()).thenThrow(new IOException("Test error"));
        xmlSerializerProvider._initWithRootName(xgen, new QName("namespace", "rootName"));
    }

    // Testing _rootNameFromConfig method with null config
    @Test
    public void testRootNameFromConfigWithNullConfig() {
        PropertyName rootName = xmlSerializerProvider._rootNameFromConfig();
        assertNull(rootName);
    }

    // Testing _asXmlGenerator method with null JsonGenerator
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithNullJsonGenerator() throws JsonProcessingException {
        JsonGenerator gen = null;
        xmlSerializerProvider._asXmlGenerator(gen);
    }

    // Testing _asXmlGenerator method with JsonGenerator that is not an instance of ToXmlGenerator
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithJsonGeneratorThatIsNotAnInstanceOfToXmlGenerator()
            throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isWritingRootValue()).thenReturn(true);
        when(gen.writeStartObject()).thenThrow(new IOException("Test error"));
        xmlSerializerProvider._asXmlGenerator(gen);
    }

    // Testing _wrapAsIOE method with null JsonGenerator
    @Test(expected = NullPointerException.class)
    public void testWrapAsIOEWithNullJsonGenerator() throws JsonProcessingException {
        JsonGenerator gen = null;
        Exception e = new IOException("Test error");
        xmlSerializerProvider._wrapAsIOE(gen, e);
    }

    // Testing _wrapAsIOE method with Exception that is not an instance of IOException
    @Test(expected = JsonMappingException.class)
    public void testWrapAsIOEWithExceptionThatIsNotAnInstanceOfIOException()
            throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Exception e = new RuntimeException("Test error");
        xmlSerializerProvider._wrapAsIOE(gen, e);
    }

    // Testing _wrapAsIOE method with IOException
    @Test
    public void testWrapAsIOEWithIOException() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Exception e = new IOException("Test error");
        IOException ioException = xmlSerializerProvider._wrapAsIOE(gen, e);
        assertEquals("Test error", ioException.getMessage());
    }

    // Testing copy method with null source
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullSource() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                _rootNameLookup);
        xmlSerializerProvider.copy(null);
    }

    // Testing createInstance method with null config
    @Test(expected = NullPointerException.class)
    public void testCreateInstanceWithNullConfig() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                _rootNameLookup);
        xmlSerializerProvider.createInstance(null, factory);
    }

    // Testing createInstance method with null SerializationFactory
    @Test(expected = NullPointerException.class)
    public void testCreateInstanceWithNullSerializationFactory() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                _rootNameLookup);
        xmlSerializerProvider.createInstance(config, null);
    }

    // Testing createInstance method
    @Test
    public void testCreateInstance() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                _rootNameLookup);
        XmlSerializerProvider newXmlSerializerProvider = xmlSerializerProvider
                .createInstance(config, factory);
        assertEquals(xmlSerializerProvider, newXmlSerializerProvider);
    }
}