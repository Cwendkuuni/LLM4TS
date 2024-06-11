package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.test.BaseTest;

public class XmlSerializerProviderTest extends BaseTest {

    @SuppressWarnings("unchecked")
    private static <T> JsonSerializer<T> createMockSerializer() {
        return mock(JsonSerializer.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> JavaType createMockJavaType() {
        return mock(JavaType.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> SerializerProvider createMockDefaultSerializerProvider() {
        return mock(SerializerProvider.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> XmlRootNameLookup createMockXmlRootNameLookup() {
        return mock(XmlRootNameLookup.class);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderConstruction() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        assertNotNull(xmlSerializerProvider);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNullValue() throws IOException {
        ObjectNullSerializer objectNullSerializer = new ObjectNullSerializer();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(objectNullSerializer);

        xmlSerializerProvider.serializeValue(gen, null);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNonIndexedType() throws IOException {
        String simpleName = "Person";
        String namespaceUri = "http://example.com/";
        QName rootName = new QName(namespaceUri, simpleName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object person = new Person();

        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(mock(ToXmlGenerator.class));

        xmlSerializerProvider.serializeValue(gen, person);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName(simpleName);
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithIndexedType() throws IOException {
        String simpleName = "Person";
        String namespaceUri = "http://example.com/";
        QName rootName = new QName(namespaceUri, simpleName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object[] personArray = new Object[3];
        for (int i = 0; i < personArray.length; i++) {
            personArray[i] = new Person();
        }

        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(mock(ToXmlGenerator.class));

        xmlSerializerProvider.serializeValue(gen, personArray);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        verify(gen, times(personArray.length)).writeStartObject();
        for (int i = 0; i < personArray.length; i++) {
            verify(gen).writeEndObject();
        }
        verify(gen).writeEndObject();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithIndexedTypeAndCustomRootName()
            throws IOException {
        String customRootName = "Entry";
        String simpleName = "Person";
        String namespaceUri = "http://example.com/";
        QName rootName = new QName(namespaceUri, simpleName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object[] personArray = new Object[3];
        for (int i = 0; i < personArray.length; i++) {
            personArray[i] = new Person();
        }

        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(mock(ToXmlGenerator.class));
        when(xmlSerializerProvider._rootNameLookup.findRootName(Array.class, _config)).thenReturn(new QName(customRootName));

        xmlSerializerProvider.serializeValue(gen, personArray);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName(customRootName);
        verify(gen, times(personArray.length)).writeStartObject();
        for (int i = 0; i < personArray.length; i++) {
            verify(gen).writeEndObject();
        }
        verify(gen).writeEndObject();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNonIndexedTypeAndCustomRootName()
            throws IOException {
        String customRootName = "Entry";
        String simpleName = "Person";
        String namespaceUri = "http://example.com/";
        QName rootName = new QName(namespaceUri, simpleName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object person = new Person();

        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(mock(ToXmlGenerator.class));
        when(xmlSerializerProvider._rootNameLookup.findRootName(Person.class, _config)).thenReturn(new QName(customRootName));

        xmlSerializerProvider.serializeValue(gen, person);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName(customRootName);
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNonIndexedTypeAndNullRootNameLookup()
            throws IOException {
        String simpleName = "Person";
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object person = new Person();

        xmlSerializerProvider.serializeValue(gen, person);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName(simpleName);
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNullRootName() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object nullValue = null;

        xmlSerializerProvider.serializeValue(gen, nullValue);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNonIndexedTypeAndEmptyRootNameLookup()
            throws IOException {
        String simpleName = "Person";
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> clazz, SerializationConfig config) {
                return null;
            }
        });
        JsonGenerator gen = mock(JsonGenerator.class);
        Object person = new Person();

        xmlSerializerProvider.serializeValue(gen, person);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName(simpleName);
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithNullSerializer() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();

        xmlSerializerProvider.serializeValue(gen, value, createMockJavaType(), null);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithIOException() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        IOException ioException = new IOException("Test IOException");

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenThrow(ioException);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verifyNoMoreInteractions(gen);
        verify(ioException).printStackTrace();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithJsonMappingException() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenThrow(jsonMappingException);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verify(jsonMappingException).printStackTrace();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithObjectNullSerializerAndIOException()
            throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        IOException ioException = new IOException("Test IOException");

        JsonSerializer<Object> objectNullSerializer = createMockSerializer();
        when(objectNullSerializer.serialize(value, gen, xmlSerializerProvider)).thenThrow(ioException);

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null))
                .thenReturn(objectNullSerializer);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verify(ioException).printStackTrace();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithObjectNullSerializerAndJsonMappingException()
            throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");

        JsonSerializer<Object> objectNullSerializer = createMockSerializer();
        when(objectNullSerializer.serialize(value, gen, xmlSerializerProvider)).thenThrow(jsonMappingException);

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null))
                .thenReturn(objectNullSerializer);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verify(jsonMappingException).printStackTrace();
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testXmlSerializerProviderSerializationWithObjectNullSerializerAndException()
            throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(createMockXmlRootNameLookup());
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        RuntimeException exception = new RuntimeException("Test RuntimeException");

        JsonSerializer<Object> objectNullSerializer = createMockSerializer();
        when(objectNullSerializer.serialize(value, gen, xmlSerializerProvider)).thenThrow(exception);

        when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null))
                .thenReturn(objectNullSerializer);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
        verify(exception).printStackTrace();
    }

    private static class ObjectNullSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("null");
        }
    }

    private static class Person {
    }

    private static class BaseTest {

        protected SerializationConfig _config = new SerializationConfig();

    }
}