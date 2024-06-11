package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.json.JsonSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.ToXmlGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    @Mock
    private XmlRootNameLookup _rootNameLookup;

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    @Mock
    private SerializerFactory factory;

    @Mock
    private SerializationConfig config;

    @Mock
    private JsonGenerator gen;

    @Mock
    private JsonSerializer<Object> serializer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectMapper.DefaultSerializerProvider defaultSerializerProvider;

    @Mock
    private JavaType rootType;

    @Mock
    private QName rootName;

    @Mock
    private ToXmlGenerator toXmlGenerator;

    @Mock
    private OutputStream outputStream;

    @Mock
    private JsonSerializer<Object> rootSerializer;

    private ByteArrayOutputStream byteArrayOutputStream;

    private ObjectMapper mapper;

    private SerializerProvider serializerProvider;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        byteArrayOutputStream = new ByteArrayOutputStream();
        mapper = new XmlMapper();
        serializerProvider = new DefaultSerializerProvider();
    }

    @Test
    public void testCreateInstance() throws Exception {
        when(config.getRootValueSerializer(Object.class)).thenReturn(serializer);
        when(_rootNameLookup.findRootName(Object.class, config)).thenReturn(rootName);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, factory);
        assertEquals(xmlSerializerProvider, xmlSerializerProvider.createInstance(config, factory));
        verify(factory, times(1)).createInstance(config, XmlSerializerProvider.class);
    }

    @Test
    public void testSerializeValueWithNullValue() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);

        xmlSerializerProvider.serializeValue(gen, null);

        verify(gen, times(1)).writeStartElement("null");
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObject() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(rootName);
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(factory.findValueSerializer(Object.class, true, null)).thenReturn(serializer);

        xmlSerializerProvider.serializeValue(gen, new Object());

        verify(serializer, times(1)).serialize(any(Object.class), eq(gen), any(SerializerProvider.class));
        verify(gen, times(1)).writeStartElement(rootName.getLocalPart());
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootType() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(rootName);
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(_rootNameLookup.findRootName(rootType, config)).thenReturn(rootName);
        when(factory.findValueSerializer(rootType, true, null)).thenReturn(rootSerializer);

        xmlSerializerProvider.serializeValue(gen, new Object(), rootType);

        verify(rootSerializer, times(1)).serialize(any(Object.class), eq(gen), any(SerializerProvider.class));
        verify(gen, times(1)).writeStartElement(rootName.getLocalPart());
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootTypeAndSerializer() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(rootName);
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(_rootNameLookup.findRootName(rootType, config)).thenReturn(rootName);
        when(factory.findValueSerializer(rootType, true, null)).thenReturn(rootSerializer);
        when(serializerProvider.findValueSerializer(rootType, true, null)).thenReturn(serializer);

        xmlSerializerProvider.serializeValue(gen, new Object(), rootType, serializer);

        verify(serializer, times(1)).serialize(any(Object.class), eq(gen), any(SerializerProvider.class));
        verify(gen, times(1)).writeStartElement(rootName.getLocalPart());
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithArray() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(rootName);
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(_rootNameLookup.findRootName(Object[].class, config)).thenReturn(rootName);
        when(factory.findValueSerializer(Object[].class, true, null)).thenReturn(serializer);

        xmlSerializerProvider.serializeValue(gen, new Object[0]);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithNullRootName() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(_rootNameFromConfig()).thenReturn(null);

        xmlSerializerProvider.serializeValue(gen, new Object());

        verify(gen, times(1)).writeStartElement("object");
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(new QName("root"));
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);

        xmlSerializerProvider.serializeValue(gen, new Object());

        verify(gen, times(1)).writeStartElement("root");
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(null);
        when(_rootNameLookup.findRootName(Object.class, config)).thenReturn(new QName("root"));
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);

        xmlSerializerProvider.serializeValue(gen, new Object());

        verify(gen, times(1)).writeStartElement("root");
        verify(gen, times(1)).writeEndElement();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullSerializer() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(toXmlGenerator.initGenerator()).thenReturn(true);
        when(_rootNameFromConfig()).thenReturn(new QName("root"));
        when(_asXmlGenerator(gen)).thenReturn(toXmlGenerator);
        when(factory.findValueSerializer(Object.class, true, null)).thenReturn(null);

        xmlSerializerProvider.serializeValue(gen, new Object());

        fail("Expected JsonMappingException not thrown");
    }

    @Test
    public void testStartRootArray() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);

        xmlSerializerProvider._startRootArray(toXmlGenerator, rootName);

        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(toXmlGenerator, times(1)).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn(toXmlGenerator);
        when(toXmlGenerator.getNamespaceURI()).thenReturn("http://example.com");
        when(toXmlGenerator.inRoot()).thenReturn(true);

        xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);

        verify(gen, times(1)).initGenerator();
        verify(gen, times(1)).setDefaultNamespace("http://example.com");
    }

    @Test
    public void testRootNameFromConfig() throws Exception {
        PropertyName propertyName = new PropertyName("rootName");
        when(_config.getFullRootName()).thenReturn(propertyName);

        QName result = xmlSerializerProvider._rootNameFromConfig();

        assertEquals("rootName", result.getLocalPart());
    }

    @Test
    public void testAsXmlGenerator() throws Exception {
        when(gen instanceof ToXmlGenerator).thenReturn((ToXmlGenerator) gen);

        ToXmlGenerator result = xmlSerializerProvider._asXmlGenerator(gen);

        assertEquals(toXmlGenerator, result);
    }
}