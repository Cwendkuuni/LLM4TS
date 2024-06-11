package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testCopy() {
        XmlSerializerProvider src = new XmlSerializerProvider(new XmlRootNameLookup());
        XmlSerializerProvider copy = src.copy();
        assertNotSame(src, copy);
        assertEquals(src._rootNameLookup, copy._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider instance = XmlSerializerProvider.createInstance(src._config, src._factory);
        assertEquals(src._rootNameLookup, instance._rootNameLookup);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        QName rootName = new QName("testRootName");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider._initWithRootName(gen, rootName);
        verify(gen).setNextName(rootName);
    }

    @Test
    public void testInitWithRootNameEmptyNamespace() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        QName rootName = new QName("testRootName");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider._initWithRootName(gen, rootName);
        verify(gen).setNextName(rootName);
    }

    @Test
    public void testInitWithRootNameNonEmptyNamespace() throws IOException, XMLStreamException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        QName rootName = new QName("http://test.com/ns", "testRootName");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider._initWithRootName(gen, rootName);
        verify(gen).setDefaultNamespace("http://test.com/ns");
        verify(gen).setNextName(rootName);
    }

    @Test
    public void testSetNextNameIfMissing() throws IOException, XMLStreamException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        QName rootName1 = new QName("root1");
        QName rootName2 = new QName("root2");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider.setNextNameIfMissing(gen, rootName1);
        assertTrue(provider.isNextNameSet(gen));
        provider.setNextNameIfMissing(gen, rootName2);
        assertFalse(provider.isNextNameSet(gen));
    }

    @Test
    public void testSetNextNameIfMissingRoot() throws IOException, XMLStreamException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        provider.setNextNameIfMissing(gen, null);
        assertTrue(provider.isNextNameSet(gen));
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        provider._serializeXmlNull(gen);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueNullWithRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        provider._initWithRootName(gen, new QName("testRootName"));
        provider._serializeXmlNull(gen);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("testRootName");
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValue() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        provider.serializeValue(gen, value);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider.serializeValue(xgen, value);
        verify(xgen).writeStartObject();
        verifyNoMoreInteractions(xgen);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        Object value = new Object();
        QName rootName = new QName("testRootName");
        provider._initWithRootName(gen, rootName);
        provider.serializeValue(gen, value);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName(rootName.getLocalPart());
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider.serializeValue(xgen, value);
        verify(xgen).writeStartObject();
        verifyNoMoreInteractions(xgen);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = createType(Integer[].class);
        provider.serializeValue(gen, value, rootType, null);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider.serializeValue(xgen, value, rootType, null);
        verify(xgen).writeStartArray();
        verifyNoMoreInteractions(xgen);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = createType(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        provider.serializeValue(gen, value, rootType, ser);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider.serializeValue(xgen, value, rootType, ser);
        verify(xgen).writeStartObject();
        verifyNoMoreInteractions(xgen);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testSerializeValueWithRootTypeAndNullSerializer() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = createType(JavaType.class);
        provider.serializeValue(gen, value, rootType, null);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider.serializeValue(xgen, value, rootType, null);
        verify(xgen).writeStartObject();
        verifyNoMoreInteractions(xgen);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testStartRootArray() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        QName rootName = new QName("testRootName");
        provider._startRootArray(gen, rootName);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
    }

    private JavaType createType(Class<?> clazz) {
        return new SimpleTypeModifier().constructType(clazz);
    }

    private JavaType createType(JavaType javaType) {
        return new SimpleTypeModifier().constructType(javaType.getClass());
    }

}