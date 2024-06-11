package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        assertEquals(XmlSerializerProvider.class, provider.getClass());
        assertNotNull(provider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider srcProvider = new XmlSerializerProvider(rootNameLookup);
        SerializerProvider newProvider = srcProvider.createInstance(config, null);
        assertEquals(XmlSerializerProvider.class, newProvider.getClass());
        assertSame(srcProvider._rootNameLookup, newProvider._rootNameLookup);
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, null);
        assertTrue(gen.isWritingRootValue());
        assertEquals("null", gen.getOutputValueAsString());
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        QName rootName = new QName("rootName");
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, new Object(), rootName);
        assertTrue(gen.isWritingStartElement());
        assertEquals("rootName", gen.getCurrentElementName().getLocalPart());
        assertFalse(gen.isWritingRootValue());
        assertFalse(gen.isWritingEndElement());
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootValueSeparator(":");
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, null);
        QName rootName = new QName("rootName");
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, new Object(), rootName);
        assertTrue(gen.isWritingStartElement());
        assertEquals("rootName", gen.getCurrentElementName().getLocalPart());
        assertEquals(":", gen.getCurrentElementName().getNamespaceURI());
        assertFalse(gen.isWritingRootValue());
        assertFalse(gen.isWritingEndElement());
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        rootNameLookup.setRootNameForClass("com.example.MyClass", new QName("myRootName"));
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, null);
        JsonGenerator gen = new MockJsonGenerator();
        Object obj = new Object();
        provider.serializeValue(gen, obj, null);
        assertTrue(gen.isWritingStartElement());
        assertEquals("myRootName", gen.getCurrentElementName().getLocalPart());
        assertFalse(gen.isWritingRootValue());
        assertFalse(gen.isWritingEndElement());
    }

    @Test
    public void testSerializeValueAsArray() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, new int[]{}, null);
        assertTrue(gen.isWritingStartObject());
        assertEquals("item", gen.getCurrentName());
        assertTrue(gen.isWritingStartArray());
        assertEquals(Integer.class, gen.getCurrentType());
        assertTrue(gen.isWritingValueAsArrayElement());
        assertEquals(-1, gen.getLastValueAsNumber().intValue());
        assertTrue(gen.isWritingEndArray());
        assertTrue(gen.isWritingEndObject());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullAsXmlGenerator() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(null, config, null);
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, new Object(), null);
    }

    @Test
    public void testFindTypedValueSerializer() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, null);
        JsonSerializer<Object> serializer = provider.findTypedValueSerializer(Object.class, true, null);
        assertNotNull(serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindTypedValueSerializerWithNullType() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, null);
        provider.findTypedValueSerializer(null, true, null);
    }

    @Test
    public void testSerializeValueWithCustomSerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        JsonSerializer<String> stringSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("custom");
            }
        };
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, null);
        provider.setSerializerInstance(String.class, stringSerializer);
        JsonGenerator gen = new MockJsonGenerator();
        provider.serializeValue(gen, "test", null);
        assertEquals("custom", gen.getOutputValueAsString());
    }
}

class MockJsonGenerator implements JsonGenerator {
    private boolean isWritingRootValue = false;
    private boolean isWritingStartElement = false;
    private boolean isWritingStartArray = false;
    private boolean isWritingValueAsArrayElement = false;
    private boolean isWritingEndElement = false;
    private QName currentElementName;
    private JavaType currentType;

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void writeNull() throws IOException {
    }

    @Override
    public void writeFieldName(String name) throws IOException {
    }

    @Override
    public void writeStartElement(String name) throws IOException {
        isWritingStartElement = true;
        currentElementName = new QName(null, name);
    }

    @Override
    public void writeStartArray() throws IOException {
        isWritingStartArray = true;
    }

    @Override
    public void writeEndElement() throws IOException {
        isWritingEndElement = true;
    }

    @Override
    public void writeEndArray() throws IOException {
        isWritingEndArray = true;
    }

    @Override
    public void writeValue(Object value) throws IOException {
        if (value != null) {
            currentType = value.getClass();
        }
        isWritingRootValue = true;
    }

    @Override
    public void writeValueAsArrayElement(Object value) throws IOException {
        isWritingValueAsArrayElement = true;
    }

    @Override
    public void writeValueAsString(String value) throws IOException {
    }

    @Override
    public void writeNumber(Number value) throws IOException {
    }

    @Override
    public void writeBoolean(boolean value) throws IOException {
    }

    @Override
    public void writeBinaryData(byte[] value, int offset, int length) throws IOException {
    }

    @Override
    public void writeRawValue(String value) throws IOException {
    }

    @Override
    public void writeStartObject() throws IOException {
    }

    @Override
    public void writeObject(Object value) throws IOException {
    }

    @Override
    public void writeFieldValue(String name, Object value) throws IOException {
    }

    @Override
    public JavaType getCurrentValueType() {
        return currentType;
    }

    @Override
    public void setDefaultPrettyPrinter(JsonGenerator.DefaultPrettyPrinter prettyPrinter) {
    }

    @Override
    public void setRootValueSeparator(String rootValueSeparator) {
    }

    @Override
    public void setRootValueSerializer(JsonSerializer<Object> rootValueSerializer) {
    }

    @Override
    public void setSerializationConfig(SerializationConfig serializationConfig) {
    }

    @Override
    public void setSerializerInstance(Class<Object> serializableType, JsonSerializer<Object> serializer) {
    }

    @Override
    public void setSerializationConfig(SerializationConfig serializationConfig, boolean writeNullForEmptyListAndArray) {
    }

    @Override
    public void setDefaultSerializerProvider(SerializerProvider defaultProvider) {
    }

    @Override
    public void setDefaultSerializerProvider(SerializerProvider defaultProvider, boolean cacheAndSynchronize) {
    }

    @Override
    public void setDefaultSerializerProvider(SerializerProvider defaultProvider, boolean useDefaultPolymorphicSerializer) {
    }

    @Override
    public void setDefaultSerializerProvider(SerializerProvider defaultProvider, boolean useDefaultSerializers) {
    }

    @Override
    public void setDefaultSerializerProvider(SerializerProvider defaultProvider, boolean useDefaultSerializer) {
    }

    @Override
    public void setSerializerProvider(SerializerProvider provider) {
    }

    @Override
    public void setRootName(PropertyName rootName) {
    }

    @Override
    public void setRootName(PropertyName rootName, boolean localOnly) {
    }

    @Override
    public void setRootName(PropertyName rootName, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(PropertyName rootName, String prefix) {
    }

    @Override
    public void setRootName(PropertyName rootName, String prefix, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(String rootName) {
    }

    @Override
    public void setRootName(String rootName, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(String rootName, String prefix) {
    }

    @Override
    public void setRootName(String rootName, String prefix, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(QName rootName) {
    }

    @Override
    public void setRootName(QName rootName, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(QName rootName, String prefix) {
    }

    @Override
    public void setRootName(QName rootName, String prefix, JsonSerializer<Object> rootSerializer) {
    }

    @Override
    public void setRootName(String rootName, JsonSerializer<Object> rootSerializer) {
        currentElementName = new QName(rootName, null);
    }

    @Override
    public void setRootName(String rootName, String prefix, JsonSerializer<Object> rootSerializer) {
        currentElementName = new QName(rootName, prefix);
    }

    @Override
    public void setRootName(QName rootName) {
        currentElementName = rootName;
    }

    @Override
    public void setRootName(QName rootName, JsonSerializer<Object> rootSerializer) {
        currentElementName = rootName;
    }

    @Override
    public void setRootName(QName rootName, String prefix) {
        currentElementName = new QName(rootName.getNamespaceURI(), rootName.getLocalPart(), null, prefix);
    }

    @Override
    public void setRootName(QName rootName, String prefix, JsonSerializer<Object> rootSerializer) {
        currentElementName = new QName(rootName.getNamespaceURI(), rootName.getLocalPart(), null, prefix);
    }

    @Override
    public void setSerializationInclusion(JsonInclude.Include inclusion) {
    }

    public boolean isWritingRootValue() {
        return isWritingRootValue;
    }

    public boolean isWritingStartElement() {
        return isWritingStartElement;
    }

    public boolean isWritingStartArray() {
        return isWritingStartArray;
    }

    public boolean isWritingValueAsArrayElement() {
        return isWritingValueAsArrayElement;
    }

    public boolean isWritingEndElement() {
        return isWritingEndElement;
    }

    public QName getCurrentElementName() {
        return currentElementName;
    }
}