package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.SerializerFactoryImpl;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.TypeSerializer;
import com.fasterxml.jackson.databind.ser.SerializerCache;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JacksonStdSerializers;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;
    private SerializationConfig config;
    private SerializerFactory factory;
    private XmlRootNameLookup rootNames;

    @Before
    public void setup() {
        config = new SerializationConfig();
        config.setSerializationInclusion(Ememptiness.NON_EMPTY);
        factory = new DefaultSerializerProvider.StandardSerializerFactory();
        rootNames = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNames);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider newInstance = provider.createInstance(config, factory);
        assertNotNull(newInstance);
        assertEquals(provider.getClass(), newInstance.getClass());
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        when(generator.isStartElement()).thenReturn(true);
        provider.serializeValue(generator, null);
        // Calling _serializeXmlNull
        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(false);
        when(xmlGenerator.setNextNameIfMissing(ROOT_NAME_FOR_NULL)).thenReturn(false);
        provider._serializeXmlNull(xmlGenerator);
    }

    @Test
    public void testInitializeWithRootName() throws IOException, XMLStreamException {
        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(true);

        QName rootName = new QName("testRoot");
        provider._initWithRootName(xmlGenerator, rootName);
        ArgumentCaptor<String> nsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(xmlGenerator).setDefaultNamespace(nsCaptor.capture());
        verify(xmlGenerator).setNextName(nameCaptor.capture());
        assertEquals("testRoot", nameCaptor.getValue());
        assertEquals("", nsCaptor.getValue());
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        provider._startRootArray(xmlGenerator, ROOT_NAME_FOR_NULL);
        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("item");
    }

    @Test
    public void testSerializeValueAndWriteEndObject() throws IOException {
        Object value = new Object();
        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(true);

        provider.serializeValue(xmlGenerator, value);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueWithJavaTypeAndRootType() throws IOException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.serialize(value, anyObject(), anyObject())).thenReturn("serializedValue");

        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(true);

        provider.serializeValue(xmlGenerator, value, rootType, serializer);
        String expectedXml = "{\"item\":\"serializedValue\"}";
        verify(xmlGenerator).writeRawValue(expectedXml);
    }

    @Test
    public void testSerializeValueWithJavaTypeAndJsonSerializer() throws IOException {
        Object value = new Object();
        JavaType javaType = mock(JavaType.class);
        JsonSerializer<Object> jsonSerializer = JacksonStdSerializers.findValueSerializer(javaType, null);

        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(true);

        provider.serializeValue(xmlGenerator, value, jsonSerializer);
        verify(jsonSerializer).serialize(value, xmlGenerator, provider);
    }

    @Test
    public void testFindTypedValueSerializer() throws IOException {
        Class<?> testClass = Object.class;
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(factory.findValueSerializer(testClass, null, null)).thenReturn(serializer);

        JsonSerializer<Object> result = provider.findTypedValueSerializer(testClass, true, null);
        assertEquals(serializer, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testConvertValueWithNullSerializer() throws IOException, JsonMappingException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);

        provider.serializeValue(null, value, rootType, null);
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);

        ToXmlGenerator xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.isStartElement()).thenReturn(true);

        provider.serializeValue(xmlGenerator, value, serializer);
        verify(serializer).serialize(value, xmlGenerator, provider);
    }

    private static final QName ROOT_NAME_FOR_NULL = new QName("null");
}