package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

public class XmlSerializerProviderTest {

    private XmlRootNameLookup _rootNameLookup;
    private XmlSerializerProvider _serializerProvider;
    private SerializationConfig _serializationConfig;

    @Before
    public void setup() {
        _rootNameLookup = new XmlRootNameLookup();
        _serializationConfig = new SerializationConfig();
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup);
    }

    @Test
    public void testDefaultSerialization() throws IOException {
        String expectedXml = "<root><value>Hello World</value></root>";
        Object value = "Hello World";
        String rootName = "root";

        JsonMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(_serializationConfig);
        SerializerProvider provider = mapper.getSerializerProvider();
        assertEquals(expectedXml, provider.serializeValueAsString(_serializerProvider, new ObjectMapper().writeValueAsBytes(value), value.getClass()));
    }

    @Test
    public void testSerializationWithRootName() throws IOException {
        String expectedXml = "<root><value>Hello World</value></root>";
        Object value = "Hello World";
        String rootName = "root";

        _serializationConfig.setRootValueSeparator(new PropertyName("/" + rootName));
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup, _serializationConfig);
        String result = _serializerProvider.serializeValueAsString(_serializerProvider, new ObjectMapper().writeValueAsBytes(value), value.getClass());
        assertEquals(expectedXml, result);
    }

    @Test
    public void testSerializationWithNullValue() throws IOException {
        String expectedXml = "<null/>";
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup);
        String result = _serializerProvider.serializeValueAsString(_serializerProvider, null, Object.class);
        assertEquals(expectedXml, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidGenerator() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        _serializerProvider.serializeValue(gen, null);
    }

    @Test
    public void testSerializationWithArray() throws IOException {
        Object[] values = {"value1", "value2"};
        String rootName = "root";
        String expectedXml = "<root><item><value>value1</value></item><item><value>value2</value></item></root>";

        _serializationConfig.setRootValueSeparator(new PropertyName("/" + rootName));
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup, _serializationConfig);
        JavaType javaType = TypeFactory.defaultInstance().constructArrayType(String.class);
        byte[] bytes = _serializerProvider.serializeValueAsBytes(_serializerProvider, new ObjectMapper().writeValueAsBytes(values), javaType);
        assertEquals(expectedXml, StaxUtil.toString(bytes));
    }

    @Test
    public void testSerializationWithCustomRootName() throws IOException {
        Object value = new Object();
        String customRootName = "customRoot";
        String expectedXml = "<customRoot><value>Test Object</value></customRoot>";

        _serializationConfig.setRootValueSeparator(new PropertyName("/" + customRootName));
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup, _serializationConfig);
        SerializerProvider provider = _serializerProvider.createInstance(_serializationConfig, null);
        JavaType javaType = TypeFactory.defaultInstance().constructType(expectedXml);
        byte[] bytes = provider.serializeValueAsBytes(provider, new ObjectMapper().writeValueAsBytes(value), javaType.getRawClass());
        assertEquals(expectedXml, StaxUtil.toString(bytes));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidClass() throws IOException {
        _serializerProvider = new XmlSerializerProvider(_rootNameLookup, _serializationConfig);
        _serializerProvider.serializeValue(_serializerProvider.getFactory().createGenerator(System.out), new Object(), int.class);
    }
}