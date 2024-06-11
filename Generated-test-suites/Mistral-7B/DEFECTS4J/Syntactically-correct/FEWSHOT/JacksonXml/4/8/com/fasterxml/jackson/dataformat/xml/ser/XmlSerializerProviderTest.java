package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.util.ClassName;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;
    private XmlRootNameLookup rootNameLookup;

    @Before
    public void setUp() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider newProvider = provider.createInstance(null, null);
        assertNotNull(newProvider);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        provider._serializeXmlNull(gen);
        assertNotNull(gen);
    }

    @Test
    public void testSerializeValueWithObject() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);

        provider.serializeValue(gen, value);

        // Verify that writeStartObject and writeEndObject were called
        when(gen.writeStartObject()).verify(gen).writeStartObject();
        when(gen.writeEndObject()).verify(gen).writeEndObject();
        when(gen.writeFieldName("item")).verify(gen).writeFieldName("item");
    }

    @Test
    public void testSerializeValueWithJavaType() throws IOException {
        Object value = new Object();
        JavaType rootType = TypeFactory.defaultInstance().constructType(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);

        provider.serializeValue(gen, value, rootType);

        // Verify that writeStartObject and writeEndObject were called
        when(gen.writeStartObject()).verify(gen).writeStartObject();
        when(gen.writeEndObject()).verify(gen).writeEndObject();
        when(gen.writeFieldName("item")).verify(gen).writeFieldName("item");
    }

    @Test
    public void testSerializeValueWithJavaTypeAndSerializer() throws IOException {
        Object value = new Object();
        JavaType rootType = TypeFactory.defaultInstance().constructType(Object.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);

        provider.serializeValue(gen, value, rootType, serializer);

        // Verify that writeStartObject and writeEndObject were called
        when(gen.writeStartObject()).verify(gen).writeStartObject();
        when(gen.writeEndObject()).verify(gen).writeEndObject();
        when(gen.writeFieldName("item")).verify(gen).writeFieldName("item");
        // Verify that the provided serializer was used
        when(serializer.serialize(value, gen, provider)).thenReturn(null);
        verify(serializer).serialize(value, gen, provider);
    }

    @Test
    public void testFindTypedValueSerializer() throws IOException, JsonMappingException {
        Object value = new Object();
        Class<Object> valueClass = value.getClass();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(valueClass)).thenReturn(serializer);

        JsonSerializer<Object> result = provider.findTypedValueSerializer(valueClass, true, null);

        assertSame(serializer, result);
    }

    @Test
    public void testFindValueSerializerWithNullRootType() throws IOException, JsonMappingException {
        Object value = new Object();
        Class<Object> valueClass = value.getClass();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(valueClass)).thenReturn(serializer);

        JsonSerializer<Object> result = provider.findTypedValueSerializer(null, true, null);

        assertSame(serializer, result);
    }

    @Test
    public void testFindValueSerializerWithRootType() throws IOException, JsonMappingException {
        Object value = new Object();
        Class<String> rootType = String.class;
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(rootType)).thenReturn(serializer);

        JsonSerializer<Object> result = provider.findTypedValueSerializer(rootType, true, null);

        assertSame(serializer, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindValueSerializerWithInvalidRootType() throws IOException, JsonMappingException {
        Object value = new Object();
        Class<Integer> rootType = Integer.class;
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(rootType)).thenReturn(serializer);

        provider.findTypedValueSerializer(rootType, true, null);
    }

    @Test
    public void testSetRootName() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        provider._initWithRootName((ToXmlGenerator) gen, new QName("root"));

        // Verify that writeStartObject was called with the expected root name
        when(gen.setNextNameIfMissing(new QName("root"))).thenReturn(true);
        when(gen.writeStartObject()).verify(gen).writeStartObject("root");
    }

    @Test
    public void testSetRootNameWithAlreadySetRootName() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        provider._initWithRootName((ToXmlGenerator) gen, new QName("root"));

        // Verify that writeStartObject was called with the same root name
        when(gen.setNextNameIfMissing(new QName("root"))).thenReturn(false);
        when(gen.writeStartObject()).verifyNoMoreInteractions();
    }

    @Test
    public void testSetDefaultNamespace() throws IOException, XMLStreamException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(toXmlGenerator.getStaxWriter()).thenReturn(mock(XMLStreamWriter.class));

        provider._initWithRootName(toXmlGenerator, new QName("root", "namespace"));

        // Verify that the default namespace was set
        when(toXmlGenerator.getStaxWriter().setDefaultNamespace("namespace")).thenReturn(toXmlGenerator.getStaxWriter());
        when(toXmlGenerator.getStaxWriter()).thenReturn(toXmlGenerator.getStaxWriter());
        provider._initWithRootName(toXmlGenerator, new QName("root"));
        verify(toXmlGenerator.getStaxWriter()).setDefaultNamespace("namespace");
    }

    @Test
    public void testFindRootNameFromConfig() {
        PropertyName rootName = new PropertyName("root");
        QName result = provider._rootNameFromConfig();

        assertSame(new QName("root"), result);
    }

    @Test
    public void testFindRootNameFromConfigWithNullRootName() {
        PropertyName rootName = null;
        QName result = provider._rootNameFromConfig();

        assertNull(result);
    }

    @Test
    public void testFindRootNameFromLookup() {
        Class<Object> clazz = Object.class;
        QName result = provider._rootNameLookup.findRootName(clazz, null);

        assertNull(result);
    }

    @Test
    public void testFindRootNameFromLookupWithRootName() {
        Class<Object> clazz = Object.class;
        QName rootName = new QName("root");
        when(provider._rootNameLookup.findRootName(clazz, null)).thenReturn(Optional.of(rootName));

        QName result = provider._rootNameFromLookup(clazz);

        assertSame(rootName, result);
    }

    @Test
    public void testFindRootNameFromLookupWithNullRootNameInLookup() {
        Class<Object> clazz = Object.class;
        when(provider._rootNameLookup.findRootName(clazz, null)).thenReturn(Optional.empty());

        assertNull(provider._rootNameFromLookup(clazz));
    }

    @Test
    public void testFindRootNameFromLookupWithRootNameFromConfig() {
        Class<Object> clazz = Object.class;
        PropertyName rootName = new PropertyName("root");
        QName rootNameFromConfig = new QName("rootConfig");
        when(provider._config.getFullRootName()).thenReturn(rootName);
        when(provider._rootNameLookup.findRootName(clazz, null)).thenReturn(Optional.empty());

        QName result = provider._rootNameFromLookup(clazz);

        assertSame(rootNameFromConfig, result);
    }
}