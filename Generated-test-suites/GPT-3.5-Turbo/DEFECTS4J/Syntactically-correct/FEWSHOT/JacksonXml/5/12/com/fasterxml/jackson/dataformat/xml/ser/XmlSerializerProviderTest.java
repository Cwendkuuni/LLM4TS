package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlRootNameLookup rootNames;
    private XmlSerializerProvider provider;
    private XmlSerializerProvider src;
    private SerializationConfig config;
    private SerializerFactory jsf;
    private XmlSerializerProvider src2;

    @Before
    public void setUp() throws Exception {
        rootNames = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNames);
        src = new XmlSerializerProvider(provider, config, jsf);
        src2 = new XmlSerializerProvider(src);
        config = mock(SerializationConfig.class);
        jsf = mock(SerializerFactory.class);
    }

    @Test
    public void testXmlSerializerProvider() {
        assertEquals(rootNames, provider._rootNameLookup);
    }

    @Test
    public void testXmlSerializerProviderXmlSerializerProviderSerializationConfigSerializerFactory() {
        assertEquals(rootNames, src._rootNameLookup);
    }

    @Test
    public void testXmlSerializerProviderXmlSerializerProvider() {
        assertEquals(src._rootNameLookup, src2._rootNameLookup);
    }

    @Test
    public void testCopy() {
        assertEquals(provider._rootNameLookup, provider.copy()._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        assertEquals(provider._rootNameLookup, provider.createInstance(config, jsf)._rootNameLookup);
    }

    @Test
    public void testSerializeValueObject() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        provider.serializeValue(gen, value);
        verify(gen, times(1)).writeStartObject();
    }

    @Test
    public void testSerializeValueJsonGeneratorObjectJavaTypeJsonSerializerOfObject() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = null;
        provider.serializeValue(gen, value, rootType, ser);
        verify(gen, times(1)).writeStartObject();
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        provider._serializeXmlNull(jgen);
        verify(jgen, times(1)).writeStartObject();
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        provider._startRootArray(xgen, rootName);
        verify(xgen, times(1)).writeStartObject();
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        provider._initWithRootName(xgen, rootName);
        verify(xgen, times(1)).initGenerator();
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = mock(PropertyName.class);
        provider._rootNameFromConfig();
        verify(name, times(1)).getSimpleName();
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        provider._asXmlGenerator(gen);
        verify(gen, times(1)).getClass();
    }

    @Test
    public void testWrapAsIOE() {
        JsonGenerator g = mock(JsonGenerator.class);
        Exception e = new Exception();
        provider._wrapAsIOE(g, e);
        verify(e, times(2)).getMessage();
    }
}