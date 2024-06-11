package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializer;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootNameFromConfig() throws Exception {
        QName expectedRootName = new QName("rootElement");
        SerializationConfig config = new SerializationConfig().withRootName("rootElement");
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(Object.class, config)).thenReturn(expectedRootName);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        Object value = new Object();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        assertEquals(expectedRootName.getLocalPart(), getLocalPartFromByteArray(actualBytes));
    }

    @Test
    public void testSerializationWithRootNameFromXmlRootNameLookup() throws Exception {
        QName expectedRootName = new QName("rootElement");
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(Object.class, config)).thenReturn(expectedRootName);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        Object value = new Object();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        assertEquals(expectedRootName.getLocalPart(), getLocalPartFromByteArray(actualBytes));
    }

    @Test
    public void testSerializationWithNullRootName() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        Object value = new Object();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        // The null root name is considered equivalent to "null", as per the code
        assertEquals("null", new String(actualBytes));
    }

    @Test
    public void testSerializationWithEmptyRootName() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setRootName("");
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        Object value = new Object();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        assertEquals("", new String(actualBytes));
    }

    @Test
    public void testSerializationWithRootNameFromJavaType() throws Exception {
        QName expectedRootName = new QName("rootElement");
        JavaType rootType = new SimpleType("com.test.RootElement");
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(rootType, config)).thenReturn(expectedRootName);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        RootElement value = new RootElement();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        assertEquals(expectedRootName.getLocalPart(), getLocalPartFromByteArray(actualBytes));
    }

    @Test
    public void testSerializationWithRootNameFromValueType() throws Exception {
        QName expectedRootName = new QName("rootElement");
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(RootElement.class, config)).thenReturn(expectedRootName);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        RootElement value = new RootElement();
        byte[] actualBytes = serializeToByteArray(serializer, value);
        assertEquals(expectedRootName.getLocalPart(), getLocalPartFromByteArray(actualBytes));
    }

    @Test
    public void testSerializationWithRootArray() throws Exception {
        JavaType rootType = new ArrayType(new SimpleType(Object.class), null);
        Object value = Arrays.asList("value1", "value2").toArray();
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        byte[] actualBytes = serializeToByteArray(serializer, value);
        String expectedXml = "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"java.util.ArrayList\"><item>value1</item><item>value2</item></item>";
        assertEquals(expectedXml, new String(actualBytes));
    }

    @Test
    public void testSerializationWithRootArrayAndCustomRootName() throws Exception {
        JavaType rootType = new ArrayType(new SimpleType(Object.class), new PropertyName("rootName"));
        Object value = Arrays.asList("value1", "value2").toArray();
        SerializationConfig config = new SerializationConfig();
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationConfig(config);
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(rootType, config)).thenReturn(new QName("customRootName"));
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializer serializer = mapper.getXmlSerializer();
        JsonSerializer<Object> serializedValueSerializer = mapper.findValueSerializer(Object.class);
        byte[] actualBytes = serializeToByteArray(serializer, value);
        String expectedXml = "<customRootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"java.util.ArrayList\"><item>value1</item><item>value2</item></customRootName>";
        assertEquals(expectedXml, new String(actualBytes));
    }

    private byte[] serializeToByteArray(XmlSerializer serializer, Object value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.writeValueAsXml(outputStream, value);
        return outputStream.toByteArray();
    }

    private String getLocalPartFromByteArray(byte[] byteArray) throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer();
        ObjectReader reader = new MinimalObjectMapper().readerForXML();
        reader.readValue(new ByteArrayInputStream(byteArray), null);
        tokenBuffer.copyCurrentToken();
        return tokenBuffer.getCurrentName();
    }

    private static class RootElement {}
}