package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testRootNameForNull() {
        QName expected = new QName("null");
        QName actual = XmlSerializerProvider.ROOT_NAME_FOR_NULL;
        assertEquals(expected, actual);
    }

    @Test
    public void testXmlSerializerProviderConstructor() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        assertEquals(rootNames, provider._rootNameLookup);
    }

    @Test
    public void testXmlSerializerProviderConstructor1() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory f = mock(SerializerFactory.class);
        XmlSerializerProvider src = mock(XmlSerializerProvider.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(src, config, f);
        assertEquals(src._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory jsf = mock(SerializerFactory.class);
        XmlSerializerProvider src = mock(XmlSerializerProvider.class);
        XmlSerializerProvider provider = src.createInstance(config, jsf);
        assertEquals(src._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider._serializeXmlNull(gen);
        verify(gen).writeStartObject();
        verify(gen).writeStringField(anyString(), anyString());
        verify(gen).writeEndObject();

        provider.serializeValue(gen, value);
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName(anyString());
        verify(xgen).writeEndObject();

        provider.serializeValue(gen, value, rootType);
        verify(xgen, times(2)).writeStartObject();
        verify(xgen, times(2)).writeFieldName(anyString());
        verify(xgen, times(2)).writeEndObject();

        provider.serializeValue(gen, value, rootType, ser);
        verify(ser).serialize(value, gen, provider);
        verify(xgen, times(3)).writeStartObject();
        verify(xgen, times(3)).writeFieldName(anyString());
        verify(xgen, times(3)).writeEndObject();
    }

    @Test
    public void testSerializeValueIOException() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        IOException ioe = mock(IOException.class);
        doThrow(ioe).when(ser).serialize(value, gen, this);

        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        try {
            provider.serializeValue(gen, value, rootType, ser);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertSame(ioe, e.getCause());
        }
    }

    @Test
    public void testSerializeValueException() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        Exception ex = mock(Exception.class);
        doThrow(ex).when(ser).serialize(value, gen, this);

        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        try {
            provider.serializeValue(gen, value, rootType, ser);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertSame(ex, e.getCause());
        }
    }

    @Test
    public void testStartRootArray() {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        provider._startRootArray(xgen, rootName);
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName(anyString());
        verify(xgen).writeEndObject();
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        provider._initWithRootName(xgen, rootName);
        verify(xgen).setNextNameIfMissing(rootName);
        verify(xgen).inRoot();
        verify(xgen).setNextName(rootName);
        verify(xgen).initGenerator();
        verify(rootName).getNamespaceURI();
    }

    @Test
    public void testInitWithRootNameException() {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        IOException ioe = mock(IOException.class);
        doThrow(ioe).when(xgen).setNextNameIfMissing(rootName);
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        try {
            provider._initWithRootName(xgen, rootName);
            fail("Expected IOException");
        } catch(IOException e) {
            assertSame(ioe, e);
        }
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = mock(PropertyName.class);
        SerializationConfig config = mock(SerializationConfig.class);
        when(config.getFullRootName()).thenReturn(name);

        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider._config = config;
        QName actual = provider._rootNameFromConfig();
        assertNull(actual);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        TokenBuffer buffer = mock(TokenBuffer.class);

        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator actual = provider._asXmlGenerator(gen);
        assertNull(actual);

        actual = provider._asXmlGenerator(buffer);
        assertNotNull(actual);
    }

}