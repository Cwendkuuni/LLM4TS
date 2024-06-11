package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class XmlSerializerProviderTest {

    @Mock
    private ToXmlGenerator xmlGeneratorMock;

    @Test
    public void testSerializeValue() throws Exception {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = null;
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value);
    }
    
    @Test
    public void testSerializeValueWithRootType() throws Exception {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = null;
        JavaType rootType = Mockito.mock(JavaType.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType);
    }

    @Test
    public void testSerializeValueWithSerializer() throws Exception {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = null;
        JavaType rootType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_serialVersionUID() throws Exception {
        assertEquals(1L, XmlSerializerProvider.serialVersionUID);
    }

    @Test
    public void test_createInstance() throws Exception {
        DefaultSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider.createInstance(Mockito.mock(SerializationConfig.class), Mockito.mock(SerializerFactory.class));
    }

    @Test
    public void test_serializeXmlNull() throws Exception {
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider._serializeXmlNull(jgen);
    }

    @Test
    public void test_startRootArray() throws Exception {
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        QName rootName = Mockito.mock(QName.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider._startRootArray(xgen, rootName);
    }

    @Test
    public void test_initWithRootName() throws Exception {
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        QName rootName = Mockito.mock(QName.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider._initWithRootName(xgen, rootName);
    }

    @Test
    public void test_rootNameFromConfig() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider._rootNameFromConfig();
    }

    @Test
    public void test_asXmlGenerator() throws Exception {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(
                new XmlRootNameLookup());
        provider._asXmlGenerator(gen);
    }
}