package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    @Mock
    private SerializationConfig config;
    @Mock
    private SerializerFactory factory;
    @Mock
    private JsonSerializer<Object> serializer;
    @Mock
    private JsonSerializerProvider srcProvider;
    @Mock
    private DefaultSerializerProvider defaultProvider;
    @Mock
    private XmlRootNameLookup rootNameLookup;
    @Mock
    private ToXmlGenerator xmlGenerator;
    @Mock
    private QName rootName;
    @Mock
    private JsonGenerator jsonGenerator;
    @Mock
    private TokenBuffer tokenBuffer;

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(factory.createSerializerProvider()).thenReturn(defaultProvider);
        when(defaultProvider.findValueSerializer(any(Class.class), anyBoolean(), any(JavaType.class)))
                .thenReturn(serializer);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("XmlSerializerProvider", xmlSerializerProvider.getClass().getSimpleName());
        assertNull(xmlSerializerProvider._rootNameLookup);
    }

    @Test
    public void testCopyConstructor() {
        XmlSerializerProvider src = new XmlSerializerProvider(defaultProvider, config, factory);
        XmlSerializerProvider copy = new XmlSerializerProvider(src);
        assertEquals(src, copy);
        assertEquals(src._rootNameLookup, copy._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        assertNotNull(xmlSerializerProvider.createInstance(config, factory));
    }

    @Test
    public void testCreateInstanceWithRootNameLookup() {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(provider);
        assertEquals(rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws IOException {
        when(config.getFullRootName()).thenReturn(new PropertyName("rootName"));
        when(jsonGenerator.writeStartElement(anyString())).thenReturn(xmlGenerator);
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        when(xmlGenerator.writeElement(any(Object.class))).thenReturn(xmlGenerator);
        when(xmlGenerator.writeEndElement()).thenReturn(xmlGenerator);
        ObjectNode rootNode = new ObjectMapper().writeValueAsTree("{}");
        when(jsonGenerator.getCodec()).thenReturn(new ObjectMapper());

        xmlSerializerProvider.serializeValue(jsonGenerator, rootNode);

        verify(jsonGenerator, times(1)).writeStartElement("rootName");
        verify(xmlGenerator, times(1)).writeElement(rootNode);
        verify(xmlGenerator, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithNullRootNameFromConfig() throws IOException {
        when(config.getFullRootName()).thenReturn(null);
        when(jsonGenerator.writeStartElement("null")).thenReturn(xmlGenerator);
        when(xmlGenerator.initGenerator()).thenReturn(xmlGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        verify(jsonGenerator, times(1)).writeStartElement("null");
    }

    @Test
    public void testSerializeValueWithRootNameFromRootNameLookup() throws IOException {
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(xmlGenerator.setNextName(any(QName.class))).thenReturn(xmlGenerator);
        when(xmlGenerator.writeStartElement(anyString())).thenReturn(xmlGenerator);

        xmlSerializerProvider.serializeValue(xmlGenerator, new Object());

        verify(xmlGenerator, times(1)).setNextName(rootName);
        verify(xmlGenerator, times(1)).writeStartElement("rootName");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullGenerator() throws IOException {
        xmlSerializerProvider.serializeValue(null, new Object());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullValue() throws IOException {
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        when(config.getRootValueSerializer(any(Class.class))).thenReturn(serializer);
        when(serializer.serializeValues(any(ObjectNode.class), any(SerializerProvider.class), anyBoolean()))
                .thenReturn(new ObjectMapper().writeValueAsBytes(new ObjectNode(true)));
        when(jsonGenerator.writeRawValue(anyString())).thenReturn(jsonGenerator);

        byte[] result = xmlSerializerProvider.serializeValue(jsonGenerator, new Object());

        verify(jsonGenerator, times(1)).writeRawValue(Arrays.toString(new ObjectMapper().writeValueAsBytes(new ObjectNode(true))));
        assertEquals(Arrays.toString(new ObjectMapper().writeValueAsBytes(new ObjectNode(true))), new String(result));
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        when(xmlGenerator.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(xmlGenerator.setNextName(any(QName.class))).thenReturn(xmlGenerator);
        when(xmlGenerator.writeStartElement(anyString())).thenReturn(xmlGenerator);
        when(xmlGenerator.writeEndElement()).thenReturn(xmlGenerator);
        when(jsonGenerator.getConfig()).thenReturn(config);
        when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(rootName);

        xmlSerializerProvider.serializeValue(jsonGenerator, new Object());

        verify(jsonGenerator, times(1)).writeStartElement("rootName");
        verify(jsonGenerator, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueWithIndexedType() throws IOException {
        when(tokenBuffer.writeStartArray()).thenReturn(tokenBuffer);
        when(jsonGenerator.writeRawValue(anyString())).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, new int[1]);

        verify(jsonGenerator, times(1)).writeRawValue("[");
        verify(jsonGenerator, times(1)).writeRawValue("]");
    }

    @Test
    public void testWrapAsIOE() throws IOException {
        when(jsonGenerator.getCodec()).thenReturn(new ObjectMapper());
        Exception e = new RuntimeException("Test Exception");
        JsonMappingException expected = new JsonMappingException(jsonGenerator, "Test Exception", e);

        IOException ioException = xmlSerializerProvider._wrapAsIOE(jsonGenerator, e);

        assertEquals(expected.getClass(), ioException.getClass());
        assertEquals(expected.getMessage(), ioException.getMessage());
    }
}