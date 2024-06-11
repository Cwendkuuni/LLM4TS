package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void test_serializeValue() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator gen = new StaxUtil.ToXmlGenerator(null, null, null, null);

        // Test null value
        serializerProvider.serializeValue(gen, null);
        assertTrue(gen.isStartElement());
        assertTrue(gen.getCurrentName().getLocalPart().equals("null"));
        assertTrue(gen.isEndElement());

        // Test non-null value
        Object value = new Object();
        JsonSerializer<Object> ser = serializerProvider.findValueSerializer(value.getClass(), null);
        serializerProvider.serializeValue(gen, value, ser);
        assertNull(gen.getCurrentName());

        // Test non-null value with root type
        JavaType rootType = serializerProvider.constructType(Object.class);
        serializerProvider.serializeValue(gen, value, rootType, ser);
        assertNull(gen.getCurrentName());

        // Test non-null value with root type and custom serializer
        JsonSerializer<Object> customSerializer = mock(JsonSerializer.class);
        serializerProvider.serializeValue(gen, value, rootType, customSerializer);
        assertNull(gen.getCurrentName());

        // Test non-null value as array
        Object[] arrayValue = new Object[0];
        serializerProvider.serializeValue(gen, arrayValue, rootType, ser);
        assertTrue(gen.isStartObject());
        assertTrue(gen.getCurrentName().getLocalPart().equals("item"));
        assertTrue(gen.isEndObject());
    }

    @Test
    public void test_serializeValue_withNullRootNameLookup() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(null);

        Object value = new Object();
        JsonSerializer<Object> ser = serializerProvider.findValueSerializer(value.getClass(), null);

        JsonGenerator gen = new StaxUtil.ToXmlGenerator(null, null, null, null);
        serializerProvider.serializeValue(gen, value, ser);

        assertNull(gen.getCurrentName());
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_invalidRootNameLookup() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> cls, SerializationConfig config) {
                throw new RuntimeException("Invalid root name lookup");
            }
        };
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        Object value = new Object();
        JsonSerializer<Object> ser = serializerProvider.findValueSerializer(value.getClass(), null);

        JsonGenerator gen = new StaxUtil.ToXmlGenerator(null, null, null, null);
        serializerProvider.serializeValue(gen, value, ser);
    }

    @Test
    public void test_serializeValue_nullSerializer() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        Object value = new Object();
        JavaType rootType = serializerProvider.constructType(Object.class);

        JsonGenerator gen = new StaxUtil.ToXmlGenerator(null, null, null, null);
        serializerProvider.serializeValue(gen, value, rootType, null);

        assertNull(gen.getCurrentName());
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_nullJsonSerializer() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        Object value = new Object();
        JavaType rootType = serializerProvider.constructType(Object.class);

        JsonGenerator gen = new StaxUtil.ToXmlGenerator(null, null, null, null);
        serializerProvider.serializeValue(gen, value, rootType, null);
    }

    @Test
    public void test_startRootArray() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator xgen = new StaxUtil.ToXmlGenerator(null, null, null, null);

        QName rootName = new QName("root");
        serializerProvider._startRootArray(xgen, rootName);

        assertTrue(xgen.isStartObject());
        assertTrue(xgen.getCurrentName().getLocalPart().equals("root"));
        assertTrue(xgen.isFieldName("item"));
    }

    @Test
    public void test_initWithRootName() throws IOException {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator xgen = new StaxUtil.ToXmlGenerator(null, null, null, null);

        QName rootName = new QName("root");
        serializerProvider._initWithRootName(xgen, rootName);

        assertTrue(xgen.isStartElement());
        assertTrue(xgen.getCurrentName().getLocalPart().equals(rootName.getLocalPart()));
        assertTrue(xgen.isEndElement());
    }

    @Test
    public void test_rootNameFromConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        PropertyName rootName = config.getFullRootName();

        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        if (rootName == null) {
            assertNull(serializerProvider._rootNameFromConfig());
        } else {
            QName expectedRootName = new QName(rootName.getNamespace(), rootName.getSimpleName());
            QName actualRootName = serializerProvider._rootNameFromConfig();

            assertEquals(expectedRootName.getLocalPart(), actualRootName.getLocalPart());
            assertEquals(expectedRootName.getNamespaceURI(), actualRootName.getNamespaceURI());
        }
    }

    @Test
    public void test_asXmlGenerator() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonGenerator gen = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider().getDefaultSerializerProvider());

        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        ToXmlGenerator expectedXgen = new StaxUtil.ToXmlGenerator(gen, null, null, null);
        ToXmlGenerator actualXgen = serializerProvider._asXmlGenerator(gen);

        assertEquals(expectedXgen.getClass(), actualXgen.getClass());
    }
}