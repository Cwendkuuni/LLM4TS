package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Test cases for XmlSerializerProvider class.
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testXmlSerializerProviderConstructor() {
        XmlRootNameLookup rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(xmlSerializerProvider);
    }

    @Test
    public void testXmlSerializerProviderCopyConstructor() {
        XmlSerializerProvider srcXmlSerializerProvider = Mockito.mock(XmlSerializerProvider.class);
        XmlSerializerProvider dstXmlSerializerProvider = new XmlSerializerProvider(srcXmlSerializerProvider);
        assertNotNull(dstXmlSerializerProvider);
        assertEquals(srcXmlSerializerProvider, dstXmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        Mockito.verify(jsonGenerator).writeStartElement("null");
        Mockito.verify(jsonGenerator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectValue() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        // Verify that the default serializer is used
        JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(jsonSerializer);
        Mockito.verify(jsonSerializer).serialize(value, jsonGenerator, xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithObjectValueAndRootType() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        JavaType rootType = Mockito.mock(JavaType.class);
        Object value = new Object();
        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, null);
        // Verify that the default serializer is used
        JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(jsonSerializer);
        Mockito.verify(jsonSerializer).serialize(value, jsonGenerator, xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithArrayValue() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        Object[] value = new Object[1];
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        Mockito.verify(jsonGenerator).writeStartObject();
        Mockito.verify(jsonGenerator).writeFieldName("item");
        Mockito.verify(jsonGenerator, times(value.length)).writeStartObject();
        Mockito.verify(jsonGenerator, times(value.length)).writeEndObject();
        Mockito.verify(jsonGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueWithArrayValueAndRootType() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        JavaType rootType = Mockito.mock(JavaType.class);
        Object[] value = new Object[1];
        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, null);
        Mockito.verify(jsonGenerator).writeStartObject();
        Mockito.verify(jsonGenerator).writeFieldName("item");
        for (Object obj : value) {
            JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
            Mockito.when(xmlSerializerProvider.findTypedValueSerializer(rootType.getRawClass(), true, null)).thenReturn(jsonSerializer);
            Mockito.verify(jsonSerializer).serialize(obj, jsonGenerator, xmlSerializerProvider);
        }
        Mockito.verify(jsonGenerator).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithInvalidGenerator() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = Mockito.mock(Object.class);
        xmlSerializerProvider.serializeValue(jsonGenerator, new Object());
        fail("Expected JsonMappingException");
    }

    @Test
    public void testFindTypedValueSerializerWithNullClass() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        assertEquals(null, xmlSerializerProvider.findTypedValueSerializer(null, true, null));
    }

    @Test
    public void testFindTypedValueSerializerWithExistingSerializer() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonSerializer<String> jsonSerializer = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findValueSerializer(String.class)).thenReturn(jsonSerializer);
        assertEquals(jsonSerializer, xmlSerializerProvider.findTypedValueSerializer(String.class, true, null));
    }

    @Test
    public void testFindTypedValueSerializerWithNullSerializer() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        assertEquals(null, xmlSerializerProvider.findTypedValueSerializer(int.class, true, null));
    }

    @Test
    public void testFindTypedValueSerializerWithCustomSerializer() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonSerializer<String> jsonSerializer = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findValueSerializer(String.class)).thenReturn(jsonSerializer);
        JsonSerializer<String> customJsonSerializer = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findValueSerializer(String.class, JsonSerializer.None.class)).thenReturn(customJsonSerializer);
        assertEquals(customJsonSerializer, xmlSerializerProvider.findTypedValueSerializer(String.class, JsonSerializer.None.class, null));
    }

}