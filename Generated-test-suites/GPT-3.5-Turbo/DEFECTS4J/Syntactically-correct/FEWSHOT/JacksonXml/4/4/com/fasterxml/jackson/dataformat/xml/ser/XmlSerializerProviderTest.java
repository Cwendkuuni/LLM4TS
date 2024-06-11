package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider xmlSerializerProvider;
    private ToXmlGenerator toXmlGenerator;

    @Before
    public void setUp() throws Exception {
        XmlRootNameLookup xmlRootNameLookup = mock(XmlRootNameLookup.class);
        SerializationConfig serializationConfig = mock(SerializationConfig.class);
        SerializerFactory serializerFactory = mock(SerializerFactory.class);
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        toXmlGenerator = mock(ToXmlGenerator.class);

        when(xmlRootNameLookup.findRootName(JavaType.class, serializationConfig)).thenReturn(new QName("namespace", "name"));
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(toXmlGenerator.getStaxWriter().setDefaultNamespace("namespace")).thenReturn(null);
    }

    @Test
    public void testCreateInstance() throws Exception {
        SerializationConfig serializationConfig = mock(SerializationConfig.class);
        SerializerFactory serializerFactory = mock(SerializerFactory.class);
        XmlSerializerProvider instance = xmlSerializerProvider.createInstance(serializationConfig, serializerFactory);

        assertEquals(XmlSerializerProvider.class, instance.getClass());
        assertEquals(serializationConfig, instance.getConfig());
        assertEquals(serializerFactory, instance._serializerFactory);
        assertEquals(xmlSerializerProvider._rootNameLookup, instance._rootNameLookup);
    }

    @Test
    public void testSerializeValue_NullValue() throws Exception {
        xmlSerializerProvider.serializeValue(toXmlGenerator, null);

        verify(toXmlGenerator).setNextNameIfMissing(new QName("null"));
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator.getStaxWriter()).setDefaultNamespace(null);
        verify(toXmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValue_ClassIsNull() throws Exception {
        // Given
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Object value = new Object();

        // When
        xmlSerializerProvider.serializeValue(jsonGenerator, value);

        // Then
        verify(toXmlGenerator).setNextNameIfMissing(new QName("namespace", "name"));
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator.getStaxWriter()).setDefaultNamespace("namespace");
        verify(toXmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValue() throws Exception {
        // Given
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType javaType = mock(JavaType.class);
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);

        // When
        xmlSerializerProvider.serializeValue(jsonGenerator, value, javaType, jsonSerializer);

        // Then
        verify(toXmlGenerator).setNextNameIfMissing(new QName("namespace", "name"));
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator.getStaxWriter()).setDefaultNamespace("namespace");
        verify(toXmlGenerator).writeEndObject();
    }
    
    @Test
    public void test_serializeValue_ExceptionCaught() throws Exception {
        // Given
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType javaType = mock(JavaType.class);
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
        
        when(jsonSerializer.serialize(value, jsonGenerator, xmlSerializerProvider)).thenThrow(new IOException());

        // When
        try {
            xmlSerializerProvider.serializeValue(jsonGenerator, value, javaType, jsonSerializer);
        } catch (IOException e) {
            assertEquals("java.io.IOException", e.getClass().getName());
            assertEquals("null", e.getMessage());
            assertNull(e.getCause());
        }

        // Then
        verify(toXmlGenerator).setNextNameIfMissing(new QName("namespace", "name"));
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator.getStaxWriter()).setDefaultNamespace("namespace");
        verify(toXmlGenerator).writeEndObject();
    }
}