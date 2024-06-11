package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
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
    private XmlRootNameLookup rootNameLookup;
    private SerializationConfig config;
    private SerializerFactory serializerFactory;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        config = mock(SerializationConfig.class);
        serializerFactory = mock(SerializerFactory.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testConstructor1() {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        assertEquals(rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testConstructor2() {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlSerializerProvider, config, serializerFactory);

        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(config, provider._config);
        assertEquals(serializerFactory, provider._serializerFactory);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider provider = xmlSerializerProvider.createInstance(config, serializerFactory);

        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(config, provider._config);
        assertEquals(serializerFactory, provider._serializerFactory);
    }

    @Test
    public void testSerializeValue1() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        
        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(0)).writeFieldName(anyString());
        verify(gen, times(0)).writeNull();
    }

    @Test
    public void testSerializeValue2() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        Class<?> cls = value.getClass();

        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(cls, true, null)).thenReturn(ser);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
        verify(ser, times(1)).serialize(value, gen, xmlSerializerProvider);
        verify(gen, times(0)).writeFieldName(anyString());
        verify(gen, times(0)).writeNull();
    }

    @Test
    public void testSerializeValue3() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        Class<?> cls = value.getClass();

        JavaType rootType = mock(JavaType.class);

        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(rootType, true, null)).thenReturn(ser);

        xmlSerializerProvider.serializeValue(gen, value, rootType);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
        verify(ser, times(1)).serialize(value, gen, xmlSerializerProvider);
        verify(gen, times(0)).writeFieldName(anyString());
        verify(gen, times(0)).writeNull();
    }

    @Test
    public void testSerializeValue4() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        Class<?> cls = value.getClass();

        JavaType rootType = mock(JavaType.class);

        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(rootType, true, null)).thenReturn(ser);

        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
        verify(ser, times(1)).serialize(value, gen, xmlSerializerProvider);
        verify(gen, times(0)).writeFieldName(anyString());
        verify(gen, times(0)).writeNull();
    }
    
    @Test
    public void test_serializeXmlNull() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        Object value = null;
        
        when(jgen instanceof ToXmlGenerator).thenReturn(true);
        when(jgen instanceof TokenBuffer).thenReturn(false);
        when((ToXmlGenerator)jgen.setNextNameIfMissing(any())).thenReturn(false);
        when((ToXmlGenerator)jgen.setNextName(any())).thenReturn(true);
        when(xgen.inRoot()).thenReturn(true);
        when(xgen.setNextNameIfMissing(any())).thenReturn(false);
        when(xgen.setNextName(any())).thenReturn(true);

        xmlSerializerProvider._serializeXmlNull(jgen);

        verify(jgen, times(1)).writeStartObject();
        verify(jgen, times(1)).writeEndObject();
        verify(jgen, times(1)).serializeValue(null);
        verify(xgen, times(0)).setDefaultNamespace(anyString());
    }
    
    @Test
    public void test_startRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("namespace", "root");

        xmlSerializerProvider._startRootArray(xgen, rootName);

        verify(xgen, times(1)).writeStartObject();
        verify(xgen, times(1)).writeFieldName(eq("item"));
    }    

    @Test
    public void test_initWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("namespace", "root");

        xmlSerializerProvider._initWithRootName(xgen, rootName);

        verify(xgen, times(1)).setNextNameIfMissing(rootName);
        verify(xgen, times(0)).setNextName(eq(rootName));
        verify(xgen, times(1)).initGenerator();
    }

    @Test
    public void test_rootNameFromConfig() {
        PropertyName propertyName = mock(PropertyName.class);
        String ns = "namespace";
        String simpleName = "name";
        
        when(config.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn(ns);
        when(propertyName.getSimpleName()).thenReturn(simpleName);

        QName qName = xmlSerializerProvider._rootNameFromConfig();

        assertEquals(ns, qName.getNamespaceURI());
        assertEquals(simpleName, qName.getLocalPart());
    }

    @Test
    public void test_asXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);

        ToXmlGenerator xgen = xmlSerializerProvider._asXmlGenerator(gen);

        assertNull(xgen);
    }
}