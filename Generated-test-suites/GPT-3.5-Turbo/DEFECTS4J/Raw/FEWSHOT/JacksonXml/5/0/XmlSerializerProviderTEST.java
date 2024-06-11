package com.fasterxml.jackson.dataformat.xml.ser;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {

    @Mock
    private XmlRootNameLookup xmlRootNameLookup;

    @Mock
    private SerializationConfig config;

    @Mock
    private SerializerFactory serializerFactory;

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructorWithRootNames() {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);
        assertEquals(xmlRootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testConstructorWithSrc() {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlSerializerProvider, config, serializerFactory);
        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(xmlSerializerProvider.getAnnotationIntrospector(), provider.getAnnotationIntrospector());
        assertEquals(xmlSerializerProvider.getConfig(), provider.getConfig());
        assertEquals(xmlSerializerProvider.getGeneratorFactory(), provider.getGeneratorFactory());
        assertEquals(xmlSerializerProvider.getGeneratorFeatures(), provider.getGeneratorFeatures());
        assertEquals(xmlSerializerProvider.getSerializationView(), provider.getSerializationView());
        assertEquals(xmlSerializerProvider.getSerializerFactory(), provider.getSerializerFactory());
        assertEquals(xmlSerializerProvider.getSharedAttribute(0), provider.getSharedAttribute(0));
    }

    @Test
    public void testConstructorWithSrc() {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlSerializerProvider);
        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(xmlSerializerProvider.getAnnotationIntrospector(), provider.getAnnotationIntrospector());
        assertEquals(xmlSerializerProvider.getConfig(), provider.getConfig());
        assertEquals(xmlSerializerProvider.getGeneratorFactory(), provider.getGeneratorFactory());
        assertEquals(xmlSerializerProvider.getGeneratorFeatures(), provider.getGeneratorFeatures());
        assertEquals(xmlSerializerProvider.getSerializationView(), provider.getSerializationView());
        assertEquals(xmlSerializerProvider.getSerializerFactory(), provider.getSerializerFactory());
        assertEquals(xmlSerializerProvider.getSharedAttribute(0), provider.getSharedAttribute(0));
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider provider = xmlSerializerProvider.copy();
        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(xmlSerializerProvider.getAnnotationIntrospector(), provider.getAnnotationIntrospector());
        assertEquals(xmlSerializerProvider.getConfig(), provider.getConfig());
        assertEquals(xmlSerializerProvider.getGeneratorFactory(), provider.getGeneratorFactory());
        assertEquals(xmlSerializerProvider.getGeneratorFeatures(), provider.getGeneratorFeatures());
        assertEquals(xmlSerializerProvider.getSerializationView(), provider.getSerializationView());
        assertEquals(xmlSerializerProvider.getSerializerFactory(), provider.getSerializerFactory());
        assertEquals(xmlSerializerProvider.getSharedAttribute(0), provider.getSharedAttribute(0));
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider provider = xmlSerializerProvider.createInstance(config, serializerFactory);
        assertEquals(xmlSerializerProvider._rootNameLookup, provider._rootNameLookup);
        assertEquals(xmlSerializerProvider.getAnnotationIntrospector(), provider.getAnnotationIntrospector());
        assertEquals(xmlSerializerProvider.getConfig(), provider.getConfig());
        assertEquals(xmlSerializerProvider.getGeneratorFactory(), provider.getGeneratorFactory());
        assertEquals(xmlSerializerProvider.getGeneratorFeatures(), provider.getGeneratorFeatures());
        assertEquals(xmlSerializerProvider.getSerializationView(), provider.getSerializationView());
        assertEquals(xmlSerializerProvider.getSerializerFactory(), provider.getSerializerFactory());
        assertEquals(xmlSerializerProvider.getSharedAttribute(0), provider.getSharedAttribute(0));
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        JsonGenerator gen = null;
        Object value = null;
        xmlSerializerProvider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValueWithoutXmlGenerator() throws IOException {
        JsonGenerator gen = new TokenBuffer();
        Object value = new Object();
        xmlSerializerProvider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValueWithAsArray() throws IOException {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        xmlSerializerProvider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValueWithException() throws IOException {
        JsonGenerator gen = new TokenBuffer();
        Object value = new Object();
        xmlSerializerProvider._wrapAsIOE(gen, new Exception());
    }

    @Test
    public void testSerializeValueWithGenException() throws IOException {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        xmlSerializerProvider._wrapAsIOE(gen, new Exception());
    }

    @Test
    public void testSerializeValueWithAsArrayAndGenException() throws IOException {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        xmlSerializerProvider._wrapAsIOE(gen, new Exception());
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws IOException {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        xmlSerializerProvider._rootNameFromConfig();
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("rootName");
        xmlSerializerProvider._startRootArray(xgen, rootName);
    }

    @Test
    public void testInitializeWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("rootName");
        xmlSerializerProvider._initWithRootName(xgen, rootName);
    }

    @Test
    public void testAsXmlGeneratorWithToXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider._asXmlGenerator(gen);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithInvalidJsonGenerator() throws JsonMappingException {
        JsonGenerator gen = new JsonGenerator();
        xmlSerializerProvider._asXmlGenerator(gen);
    }
}