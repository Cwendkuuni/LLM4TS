package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

public class XmlSerializerProviderTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider provider;

    @Mock
    private JsonSerializer<Object> valueSerializer;

    @Mock
    private JavaType rootType;

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copy = xmlSerializerProvider.copy();
        assertFalse(copy == xmlSerializerProvider);
        assertTrue(copy instanceof XmlSerializerProvider);
    }

    @Test
    public void testCreateInstance() {
        SerializerFactory factory = mock(SerializerFactory.class);
        SerializationConfig config = mock(SerializationConfig.class);
        XmlSerializerProvider instance = xmlSerializerProvider.createInstance(config, factory);
        assertTrue(instance instanceof XmlSerializerProvider);
    }

    @Test
    public void testSerializeValue_WithNullValue() throws IOException {
        QName rootName = new QName("null");
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);

        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Verify initialization and serialization
        assertTrue(toXmlGenerator.setNextNameIfMissingInvoked);
        assertTrue(toXmlGenerator.initGeneratorInvoked);
        assertTrue(toXmlGenerator.staxWriterSetDefaultNamespaceInvoked);
        assertTrue(jsonGenerator.serializeValueInvoked);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_WithException() throws IOException {
        QName rootName = new QName("null");
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(jsonGenerator.serializeValueInvoked).thenThrow(new RuntimeException());

        xmlSerializerProvider.serializeValue(jsonGenerator, new Object());
    }

    @Test
    public void testSerializeValue_WithNonNullValue() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(TypeUtil.isIndexedType(Object.class)).thenReturn(true);

        xmlSerializerProvider.serializeValue(jsonGenerator, new Object());

        // Verify initialization and serialization
        assertTrue(toXmlGenerator.setNextNameIfMissingInvoked);
        assertTrue(toXmlGenerator.initGeneratorInvoked);
        assertTrue(toXmlGenerator.staxWriterSetDefaultNamespaceInvoked);
        assertTrue(jsonGenerator.serializeValueInvoked);
        assertTrue(jsonGenerator.writeEndObjectInvoked);
    }

    @Test
    public void testSerializeValue_WithNonNullValue_AndRootType() throws IOException {
        QName rootName = new QName("root");
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(TypeUtil.isIndexedType(rootType)).thenReturn(true);

        xmlSerializerProvider.serializeValue(jsonGenerator, new Object(), rootType, valueSerializer);

        // Verify initialization and serialization
        assertTrue(toXmlGenerator.setNextNameIfMissingInvoked);
        assertTrue(toXmlGenerator.initGeneratorInvoked);
        assertTrue(toXmlGenerator.staxWriterSetDefaultNamespaceInvoked);
        assertTrue(jsonGenerator.serializeValueInvoked);
        assertTrue(jsonGenerator.writeEndObjectInvoked);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_WithNonNullValue_AndRootType_WithException() throws IOException {
        QName rootName = new QName("root");
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        when(jsonGenerator.serializeValueInvoked).thenThrow(new RuntimeException());

        xmlSerializerProvider.serializeValue(jsonGenerator, new Object(), rootType, valueSerializer);
    }

    @Test
    public void testXmlSerializerProvider_WithRootNames() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        assertEquals(rootNameLookup, serializerProvider._rootNameLookup);
    }
}