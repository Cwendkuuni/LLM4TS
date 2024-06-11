package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Test;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    // Unit test for copy() method
    @Test
    public void testCopy() throws JsonMappingException, IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider dest = src.copy();

        // Check if the objects are equal but not identical
        assertEquals(src, dest);
        assertNotSame(src, dest);
    }

    // Unit test for createInstance() method
    @Test
    public void testCreateInstance() throws JsonMappingException, IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames, config, null);

        XmlSerializerProvider dest = XmlSerializerProvider.createInstance(config, null);

        // Check if the objects are equal
        assertEquals(src, dest);
    }

    // Unit test for serializeValue() method with null value
    @Test
    public void testSerializeValueWithNull() throws JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider.serializeValue(gen, null);

        String xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<null/>", xml);
    }

    // Unit test for serializeValue() method with non-null value
    @Test
    public void testSerializeValueWithNonNull() throws JsonMappingException, IOException {
        String input = "{\"name\": \"John\", \"age\": 30}";
        JsonParser parser = new ObjectMapper().readTree(input);
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider.serializeValue(gen, parser.get("name"));

        String xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<name>John</name>", xml);

        provider.serializeValue(gen, parser.get("age"));

        xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<age>30</age>", xml);
    }

    // Unit test for serializeValue() method with JavaType and JsonSerializer
    @Test
    public void testSerializeValueWithJavaTypeAndJsonSerializer() throws JsonMappingException, IOException {
        String input = "{\"name\": \"John\", \"age\": 30, \"list\": [1, 2, 3]}";
        JsonParser parser = new ObjectMapper().readTree(input);
        JsonSerializer<Object> serializer = new SimpleSerializer(); // Replace this with a real serializer
        JavaType javaType = new TypeFactory().constructType(List.class, Integer.class);

        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider.serializeValue(gen, parser.get("name"));

        String xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<name>John</name>", xml);

        provider.serializeValue(gen, parser.get("age"));

        xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<age>30</age>", xml);

        provider.serializeValue(gen, parser.get("list"), javaType, serializer);

        xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<list>\n<item>\n<int>1</int>\n</item>\n<item>\n<int>2</int>\n</item>\n<item>\n<int>3</int>\n</item>\n</list>", xml);
    }

    // Unit test for _serializeXmlNull() method
    @Test
    public void testSerializeXmlNull() throws JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._serializeXmlNull(gen);

        String xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<null/>", xml);
    }

    // Unit test for _initWithRootName() method
    @Test
    public void testInitWithRootName() throws JsonMappingException, IOException {
        String rootName = "root";
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._initWithRootName(xgen, new QName(null, rootName));

        assertEquals(rootName, xgen.getCurrentElementName());
    }

    // Unit test for _startRootArray() method
    @Test
    public void testStartRootArray() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._startRootArray(xgen, new QName(null, "items"));

        String xml = xgen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<items>", xml);
    }

    // Unit test for _rootNameFromConfig() method
    @Test
    public void testRootNameFromConfig() {
        SerializationConfig config = new SerializationConfig();
        PropertyName rootName = new PropertyName("root");
        config.setRootValueSeparator("[");
        config.setFullRootName(rootName);

        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        QName actual = provider._rootNameFromConfig();

        QName expected = new QName("", rootName.getSimpleName());
        assertEquals(expected, actual);
    }

    // Unit test for _asXmlGenerator() method
    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        ToXmlGenerator actual = provider._asXmlGenerator(gen);

        assertNotNull(actual);
        assertTrue(actual instanceof ToXmlGenerator);
    }

    // Unit test for _wrapAsIOE() method
    @Test
    public void testWrapAsIOE() throws JsonMappingException {
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        IOException ioException = new IOException("Test IO exception");

        JsonMappingException mappingException = provider._wrapAsIOE(gen, ioException);

        assertEquals("Test IO exception", mappingException.getMessage());
        assertEquals(ioException.getClass(), mappingException.getOriginalException().getClass());
    }

    // Edge case test for serializeValue() method with large number
    @Test
    public void testSerializeValueWithLargeNumber() throws JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        long largeNumber = Long.MAX_VALUE;

        // Test with default serializer
        provider.serializeValue(gen, largeNumber);

        String xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertEquals("<long>9223372036854775807</long>", xml);

        // Test with custom serializer
        JsonSerializer<Long> customSerializer = new CustomLongSerializer(); // Replace this with a real custom serializer
        provider.setSerializerInstances(Long.class, customSerializer);
        provider.serializeValue(gen, largeNumber);

        xml = gen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertNotEquals("<long>9223372036854775807</long>", xml); // Custom serializer should produce different output
    }

    // Exception handling test for serializeValue() method with null generator
    @Test(expected = IllegalStateException.class)
    public void testSerializeValueWithNullGenerator() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider.serializeValue(null, null);
    }

    // Exception handling test for serializeValue() method with non-ToXmlGenerator generator
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNonToXmlGenerator() throws JsonMappingException, IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonFactory());
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider.serializeValue(gen, null);
    }

    // Exception handling test for _initWithRootName() method with null rootName
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullRootName() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._initWithRootName(xgen, null);
    }

    // Exception handling test for _initWithRootName() method with empty rootName
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithEmptyRootName() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._initWithRootName(xgen, new QName(""));
    }

    // Exception handling test for _initWithRootName() method with rootName containing whitespace
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithRootNameContainingWhitespace() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._initWithRootName(xgen, new QName("root ", "name"));
    }

    // Exception handling test for _startRootArray() method with null rootName
    @Test(expected = IllegalArgumentException.class)
    public void testStartRootArrayWithNullRootName() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._startRootArray(xgen, null);
    }

    // Exception handling test for _startRootArray() method with empty rootName
    @Test(expected = IllegalArgumentException.class)
    public void testStartRootArrayWithEmptyRootName() throws JsonMappingException, IOException {
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        provider._startRootArray(xgen, new QName(""));
    }

    // Edge case test for _initWithRootName() method with long rootName
    @Test
    public void testInitWithLongRootName() throws JsonMappingException, IOException {
        String rootName = "root";
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        for (int i = 0; i < 1000; i++) {
            rootName += "_" + i;
            provider._initWithRootName(xgen, new QName(null, rootName));
        }

        String xml = xgen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertTrue(xml.contains(rootName));
    }

    // Edge case test for _startRootArray() method with long rootName
    @Test
    public void testStartRootArrayWithLongRootName() throws JsonMappingException, IOException {
        String rootName = "root";
        ToXmlGenerator xgen = StaxUtil.createEmptyGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        for (int i = 0; i < 1000; i++) {
            rootName += "_" + i;
            provider._startRootArray(xgen, new QName(null, rootName));
        }

        String xml = xgen.getOutputFactory().createXMLStreamWriter(new StringWriter()).getBufferStr();
        assertTrue(xml.contains(rootName));
    }
}

class SimpleSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString("simple");
    }
}

class CustomLongSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.longValue() * 2);
    }
}