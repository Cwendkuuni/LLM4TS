package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new XmlMapper();
    private static final SerializedString SERIALIZED_NULL = new SerializedString("<?xml version=\"1.0\"?>\n<null/>");
    private static final SerializedString SERIALIZED_STRING = new SerializedString("<?xml version=\"1.0\"?>\n<string>Hello World</string>");
    private static final SerializedString SERIALIZED_ARRAY = new SerializedString("<?xml version=\"1.0\"?>\n<array>\n<item><string>One</string></item>\n<item><string>Two</string></item>\n</array>");

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        assertEquals(SERIALIZED_NULL.getXmlString(), provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), null).getValueAsString());
    }

    @Test
    public void testSerializeString() throws JsonMappingException, JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        String input = "Hello World";
        assertEquals(SERIALIZED_STRING.getXmlString(), provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), input).getValueAsString());
    }

    @Test
    public void testSerializeArray() throws JsonMappingException, JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        String[] input = {"One", "Two"};
        assertEquals(SERIALIZED_ARRAY.getXmlString(), provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), input).getValueAsString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        Integer input = 42;
        provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), input);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullRootName() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null, MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        String input = "Hello World";
        provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), input);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidRootName() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        provider.getRootValueSerializer().setRootName("invalid-root-name");
        String input = "Hello World";
        provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator()), input);
    }

    @Test(expected = IOException.class)
    public void testSerializeIOException() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator(), new IOException("Test IOException")), null);
    }

    @Test(expected = XMLStreamException.class)
    public void testSerializeXmlStreamException() throws JsonProcessingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer(), MAPPER.getSerializationConfig(), MAPPER.getDefaultSerializerProvider().getSerializerFactory());
        provider.getRootValueSerializer().serializeValue(new JsonGenerator(MAPPER.getFactory().createXmlGenerator(), new XMLStreamException("Test XMLStreamException"))
                , null);
    }
}