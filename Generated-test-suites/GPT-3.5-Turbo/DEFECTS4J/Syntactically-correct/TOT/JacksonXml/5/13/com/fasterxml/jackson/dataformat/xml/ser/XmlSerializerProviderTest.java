package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    // Test data
    private XmlRootNameLookup rootNames;
    private DefaultSerializerProvider provider;
    private SerializationConfig config;
    private SerializerFactory factory;
    private JsonGenerator generator;
    private Object value;
    private JavaType rootType;
    private JsonSerializer<Object> serializer;
    private ToXmlGenerator xmlGenerator;

    @Before
    public void setup() {
        rootNames = mock(XmlRootNameLookup.class);
        config = mock(SerializationConfig.class);
        factory = mock(SerializerFactory.class);
        generator = mock(JsonGenerator.class);
        value = new Object();
        rootType = mock(JavaType.class);
        serializer = mock(JsonSerializer.class);
        xmlGenerator = mock(ToXmlGenerator.class);
        provider = new XmlSerializerProvider(rootNames);
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider copy = provider.copy();
        assertNotNull(copy);
    }

    @Test
    public void testCreateInstance() {
        DefaultSerializerProvider instance = provider.createInstance(config, factory);
        assertNotNull(instance);
    }

    @Test
    public void testSerializeValue_WithNull() throws IOException {
        provider.serializeValue(generator, null);
        verify(generator).writeStartObject();
        verify(generator).writeStringField("item", "null");
        verify(generator).writeEndObject();
    }

    @Test
    public void testSerializeValue_WithNonNull() throws IOException {
        when(generator.getClass()).thenReturn(ToXmlGenerator.class);
        when(rootNames.findRootName(any(), any())).thenReturn(new QName("test"));
        when(xmlGenerator.inRoot()).thenReturn(true);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);

        provider.serializeValue(generator, value);

        verify(xmlGenerator).initGenerator();
        verify(xmlGenerator).setDefaultNamespace(null);
        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeStringField("test", "null");
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValue_WithNonNull_IndexedType() throws IOException {
        when(generator.getClass()).thenReturn(ToXmlGenerator.class);
        when(rootNames.findRootName(any(), any())).thenReturn(new QName("test"));
        when(xmlGenerator.inRoot()).thenReturn(true);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(xmlGenerator.setNextName(any(QName.class))).thenReturn(true);
        when(xmlGenerator.writeFieldName(anyString())).thenReturn(true);
        when(generator.canWriteObjectId()).thenReturn(true);
        when(xmlGenerator.getStaxWriter().setDefaultNamespace(anyString())).thenReturn(mock(XMLStreamWriter.class));

        provider.serializeValue(generator, new Object[1]);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("item");
        verify(xmlGenerator).writeEndObject();
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_Exception() throws IOException {
        when(generator.getClass()).thenReturn(ToXmlGenerator.class);
        when(rootNames.findRootName(any(), any())).thenReturn(new QName("test"));
        when(xmlGenerator.inRoot()).thenReturn(true);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(xmlGenerator.setNextName(any(QName.class))).thenThrow(new XMLStreamException());

        provider.serializeValue(generator, value);
    }

    @Test
    public void testSerializeValue_TypeAndSerializer() throws IOException {
        when(generator.getClass()).thenReturn(ToXmlGenerator.class);
        when(rootNames.findRootName(any(), any())).thenReturn(new QName("test"));
        when(xmlGenerator.inRoot()).thenReturn(true);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        provider.serializeValue(generator, value, rootType, serializer);
        verify(xmlGenerator).initGenerator();
        verify(xmlGenerator).setDefaultNamespace(null);
        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeStringField("test", "null");
        verify(xmlGenerator).writeEndObject();
    }


    @Test(expected = IOException.class)
    public void testSerializeValue_TypeAndSerializer_Exception() throws IOException {
        when(generator.getClass()).thenReturn(ToXmlGenerator.class);
        when(rootNames.findRootName(any(), any())).thenReturn(new QName("test"));
        when(xmlGenerator.inRoot()).thenReturn(true);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(serializer.serialize(any(), any(), any())).thenThrow(new IOException());

        provider.serializeValue(generator, value, rootType, serializer);
    }

    @Test
    public void test_rootNameFromConfig_WithNull() {
        PropertyName name = null;
        QName rootName = provider._rootNameFromConfig();
        assertEquals(null, rootName);
    }

    @Test
    public void test_rootNameFromConfig_WithNonNull() {
        PropertyName name = mock(PropertyName.class);
        when(name.getNamespace()).thenReturn("namespace");
        when(name.getSimpleName()).thenReturn("name");
        when(config.getFullRootName()).thenReturn(name);

        QName rootName = provider._rootNameFromConfig();

        assertEquals("namespace", rootName.getNamespaceURI());
        assertEquals("name", rootName.getLocalPart());
    }

    @Test
    public void test_asXmlGenerator_WithToXmlGenerator() throws JsonMappingException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        JsonGenerator gen = provider._asXmlGenerator(xgen);

        assertNotNull(gen);
        assertEquals(xgen, gen);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_WithOtherGenerator() throws JsonMappingException {
        TokenBuffer buffer = mock(TokenBuffer.class);
        provider._asXmlGenerator(buffer);
    }

    @Test
    public void test_wrapAsIOE_WithIOException() {
        IOException exception = mock(IOException.class);
        JsonMappingException mappingException = provider._wrapAsIOE(generator, exception);
        assertEquals(exception, mappingException);
    }

    @Test
    public void test_wrapAsIOE_WithOtherException() {
        Exception exception = new Exception("Test Exception");
        JsonMappingException mappingException = provider._wrapAsIOE(generator, exception);
        assertEquals(exception, mappingException.getCause());
    }
}