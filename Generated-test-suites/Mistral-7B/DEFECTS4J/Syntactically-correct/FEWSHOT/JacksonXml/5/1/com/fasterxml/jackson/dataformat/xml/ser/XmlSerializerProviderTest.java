package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class XmlSerializerProviderTest {
    @Test
    public void testNullRootName() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        assertTrue(provider._rootNameLookup.isEmpty());
        assertNull(provider._rootNameFromConfig());
    }

    @Test
    public void testEmptyRootName() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName(""), "root");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        QName rootName = provider._rootNameFromConfig();
        assertEquals("root", rootName.getLocalPart());
        assertEquals("", rootName.getNamespaceURI());
    }

    @Test
    public void testRootNameFromConfig() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("http://example.com", "root"), "exampleRoot");
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator(".");
        config.setRootName("exampleRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);
        QName rootName = provider._rootNameFromConfig();
        assertEquals("exampleRoot", rootName.getLocalPart());
        assertEquals("http://example.com", rootName.getNamespaceURI());
    }

    @Test
    public void testRootNameFromLookup() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("com.example.MyClass"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
        QName rootName = provider._rootNameFromConfig();
        if (rootName == null) {
            rootName = provider._rootNameLookup.findRootName(javaType, _config);
        }
        assertEquals("MyRoot", rootName.getLocalPart());
        assertEquals("", rootName.getNamespaceURI());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidRootName() throws Exception {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("com.example.MyClass"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        provider.serializeValue(gen, new MyClass(), null, null);
    }

    @Test
    public void testSerializeNull() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        provider.serializeValue(gen, null);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<null/>", result);
    }

    @Test
    public void testSerializeNoRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        Object value = new Object();
        provider.serializeValue(gen, value);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        // The actual root name depends on the default root name for the type of 'value'
        // but it should not contain any namespace.
        // The test just checks if the root element is present.
        assertTrue(result.matches("^<.*>$"));
    }

    @Test
    public void testSerializeWithRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("com.example.MyClass"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        Object value = new MyClass();
        provider.serializeValue(gen, value);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyRoot><MyClass/></MyRoot>", result);
    }

    @Test
    public void testSerializeWithRootNameAndNamespace() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("http://example.com/namespace", "MyRoot"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        Object value = new MyClass();
        provider.serializeValue(gen, value);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://example.com/namespace\"><MyClass/></MyRoot>", result);
    }

    @Test
    public void testSerializeArray() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("com.example.MyArray"), "MyArray");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        Object value = new MyArray();
        provider.serializeValue(gen, value);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyArray><item><MyClass/></item></MyArray>", result);
    }

    @Test
    public void testSerializeArrayWithRootNameAndNamespace() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("http://example.com/namespace", "MyArray"), "MyArray");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        Object value = new MyArray();
        provider.serializeValue(gen, value);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyArray xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://example.com/namespace\"><item><MyClass/></item></MyArray>", result);
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = new JsonNumberSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        provider.setSerializerInstance(JsonNumberSerializer.class, serializer);
        provider.serializeValue(gen, new Number(123), null, null, serializer);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<number>123</number>", result);
    }

    @Test
    public void testSerializeValueWithSerializerAndRootName() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("com.example.MyClass"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = new JsonNumberSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        provider.setSerializerInstance(JsonNumberSerializer.class, serializer);
        provider.serializeValue(gen, new Number(123), null, null, serializer);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyRoot><number>123</number></MyRoot>", result);
    }

    @Test
    public void testSerializeValueWithRootNameAndNamespaceAndSerializer() throws IOException {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        rootNames.addRootNameMapping(new QName("http://example.com/namespace", "MyRoot"), "MyRoot");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonSerializer<Object> serializer = new JsonNumberSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), provider);
        provider.setSerializerInstance(JsonNumberSerializer.class, serializer);
        provider.serializeValue(gen, new Number(123), null, null, serializer);
        String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        assertEquals("<MyRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://example.com/namespace\"><number>123</number></MyRoot>", result);
    }

    private static class MyClass {
    }

    private static class MyArray {
        Object[] items = new Object[1];
    }

    private static class MyNumberSerializer extends JsonSerializer<Number> {
        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.doubleValue());
        }
    }
}