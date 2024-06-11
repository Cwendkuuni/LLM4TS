package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class XmlSerializerProviderTest {

    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(null);
        assertNotNull(serializerProvider);
    }

    @Test
    public void testXmlSerializerProviderWithRootNameLookup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(mapper.getConfig());
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(serializerProvider);
    }

    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(mapper.getConfig());
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider serializerProvider = src.createInstance(mapper.getSerializationConfig(),
                src.getDefaultSerializerFactory());
        assertNotNull(serializerProvider);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        serializerProvider.serializeValue(gen, null);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = "";
        serializerProvider.serializeValue(gen, value);
        verify(gen).writeStringValue(value.toString());
    }

    @Test
    public void testSerializeValueWithInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = 123;
        serializerProvider.serializeValue(gen, value);
        verify(gen).writeNumber(value.toString());
    }

    @Test
    public void testSerializeValueWithBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = true;
        serializerProvider.serializeValue(gen, value);
        verify(gen).writeBoolean(value.toString().equals("true"));
    }

    @Test
    public void testSerializeValueWithCustomSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        serializerProvider.setSerializerInstance(Integer.class, intSerializer);
        Object value = 123;
        serializerProvider.serializeValue(gen, value);
        verify(gen).writeNumber(value.toString());
        verify(intSerializer).serialize(value, gen, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.getSerializerProvider()).thenReturn(new DefaultSerializerProvider());
        Object value = new Object() {
            public void foo() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        serializerProvider.serializeValue(gen, value);
        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testSerializeValueWithJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        Object value = new String[]{"test", "value"};
        serializerProvider.serializeValue(gen, value, rootType);
        verify(gen).writeStartArray();
        verify(gen, times(2)).writeStringValue(anyString());
        verify(gen).writeEndArray();
    }

    @Test
    public void testSerializeValueWithJavaTypeSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        StdSerializer<String> stringSerializer = new StdSerializer<>(String.class);
        serializerProvider.setSerializerInstance(String.class, stringSerializer);
        Object value = new String[]{"test", "value"};
        serializerProvider.serializeValue(gen, value, rootType, stringSerializer);
        verify(gen).writeStartArray();
        verify(gen, times(2)).writeStringValue(anyString());
        verify(stringSerializer).serialize(any(String.class), gen, serializerProvider);
    }

    @Test
    public void testSerializeValueWithJavaTypeSerializerNullRootName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        StdSerializer<String> stringSerializer = new StdSerializer<>(String.class);
        serializerProvider.setSerializerInstance(String.class, stringSerializer);
        Object value = new String[]{"test", "value"};
        serializerProvider.serializeValue(gen, value, rootType, null);
        verify(gen).writeStartArray();
        verify(gen, times(2)).writeStringValue(anyString());
        verifyNoMoreInteractions(stringSerializer);
    }

    @Test
    public void testSerializeValueWithJavaTypeSerializerNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        StdSerializer<String> stringSerializer = new StdSerializer<>(String.class);
        serializerProvider.setSerializerInstance(String.class, stringSerializer);
        Object value = null;
        serializerProvider.serializeValue(gen, value, rootType, stringSerializer);
        verifyNoInteractions(gen);
        verifyNoInteractions(stringSerializer);
    }

    @Test
    public void testSerializeValueWithJavaTypeSerializerNullGen() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        StdSerializer<String> stringSerializer = new StdSerializer<>(String.class);
        serializerProvider.setSerializerInstance(String.class, stringSerializer);
        Object value = new String[]{"test", "value"};
        try {
            serializerProvider.serializeValue(null, value, rootType, stringSerializer);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testStartRootArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("http://example.com", "itemList");
        serializerProvider._startRootArray(xgen, rootName);
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("http://example.com", "itemList");
        serializerProvider._initWithRootName(xgen, rootName);
        verify(xgen).setNextNameIfMissing(rootName);
        verify(xgen, times(rootName.getNamespaceURI().length() > 0 ? 1 : 0)).setDefaultNamespace(rootName.getNamespaceURI());
        verify(xgen).initGenerator();
    }

    @Test
    public void testRootNameFromConfig() {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        PropertyName rootName = mapper.getSerializationConfig().getFullRootName();
        QName qname = serializerProvider._rootNameFromConfig();
        assertEquals(rootName.getSimpleName(), qname.getLocalPart());
        assertEquals(rootName.getNamespace(), qname.getNamespaceURI());
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(gen).thenReturn(xgen);
        ToXmlGenerator result = serializerProvider._asXmlGenerator(gen);
        assertEquals(xgen, result);
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(mapper.getConfig()));
        JsonGenerator gen = mock(JsonGenerator.class);
        serializerProvider._serializeXmlNull(gen);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("null");
        verify(gen).writeEndObject();
    }
}