package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    
    private XmlRootNameLookup rootNameLookup;
    private XmlSerializerProvider provider;

    @Before
    public void setUp() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testConstructor() {
        assertNotNull(provider);
    }

    @Test
    public void testConstructorWithConfigAndFactory() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        provider = new XmlSerializerProvider(provider, config, factory);

        assertNotNull(provider);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        DefaultSerializerProvider instance = provider.createInstance(config, factory);

        assertNotNull(instance);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider defaultProvider = mock(SerializerProvider.class);
        provider = mock(XmlSerializerProvider.class);
        provider._serializeXmlNull(generator);

        verify(provider).serializeValue(generator, null);
    }

    @Test
    public void testSerializeValueWithNotNull() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);

        provider.setNullValueSerializer(serializer);
        provider.serializeValue(generator, value);

        verify(serializer).serialize(value, generator, provider);
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);

        provider._startRootArray(generator, rootName);

        verify(generator).writeStartObject();
        verify(generator).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        when(generator.setNextNameIfMissing(rootName)).thenReturn(false);
        when(generator.inRoot()).thenReturn(true);

        provider._initWithRootName(generator, rootName);

        verify(generator).setNextName(rootName);
        verify(generator).initGenerator();
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName propertyName = mock(PropertyName.class);
        when(propertyName.getNamespace()).thenReturn("namespace");
        when(propertyName.getSimpleName()).thenReturn("simple");
        SerializationConfig config = mock(SerializationConfig.class);
        when(config.getFullRootName()).thenReturn(propertyName);

        provider._rootNameFromConfig();

        verify(config).getFullRootName();
    }

    @Test
    public void testAsXmlGeneratorWithToXmlGenerator() throws JsonMappingException {
        ToXmlGenerator generator = mock(ToXmlGenerator.class);

        XmlSerializerProvider result = provider._asXmlGenerator(generator);

        assertEquals(XmlSerializerProvider.class, result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithTokenBuffer() throws JsonMappingException {
        TokenBuffer buffer = mock(TokenBuffer.class);

        provider._asXmlGenerator(buffer);
    }
}