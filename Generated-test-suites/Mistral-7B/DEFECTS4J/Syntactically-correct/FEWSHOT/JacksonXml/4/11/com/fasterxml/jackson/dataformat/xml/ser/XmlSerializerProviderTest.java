package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

import org.junit.Test;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new XmlModule());
    }

    @Test
    public void testCreateInstance() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider newSerializerProvider = serializerProvider.createInstance(config, null);
        assertTrue(newSerializerProvider instanceof XmlSerializerProvider);
    }

    @Test
    public void testSerializeValueNull() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        serializerProvider.serializeValue(gen, null);
        gen.flush();
        assertTrue(gen.getOutputContent().toString().contains("<null>"));
    }

    @Test
    public void testSerializeValueWithRootName() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        String rootName = "root";
        Object value = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        serializerProvider.initWithRootName(gen, new QName(null, rootName));
        serializerProvider.serializeValue(gen, value);
        gen.writeEndObject();
        gen.flush();
        String result = gen.getOutputContent().toString();
        assertTrue(result.contains("<" + rootName + ">"));
    }

    @Test
    public void testSerializeValueWithRootType() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JavaType rootType = objectMapper.getTypeFactory().constructType(String[].class);
        Object value = new Object[]{"value1", "value2"};
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        serializerProvider.initWithRootName(gen, rootType.getRawClass());
        serializerProvider.serializeValue(gen, value, rootType);
        gen.writeEndObject();
        gen.flush();
        String result = gen.getOutputContent().toString();
        assertTrue(result.contains("<item>value1</item>"));
        assertTrue(result.contains("<item>value2</item>"));
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer()
            throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JavaType rootType = objectMapper.getTypeFactory().constructType(String[].class);
        Object value = new Object[]{"value1", "value2"};
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(String[].class, null);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        serializerProvider.initWithRootName(gen, rootType.getRawClass());
        serializerProvider.serializeValue(gen, value, rootType, serializer, typeSerializer);
        gen.writeEndObject();
        gen.flush();
        String result = gen.getOutputContent().toString();
        assertTrue(result.contains("<item>value1</item>"));
        assertTrue(result.contains("<item>value2</item>"));
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        ToXmlGenerator xmlGenerator = serializerProvider._asXmlGenerator(gen);
        assertNull(xmlGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorInvalidGenerator()
            throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = new Object();
        ToXmlGenerator xmlGenerator = serializerProvider._asXmlGenerator(gen);
        assertNull(xmlGenerator);
    }

    @Test
    public void testStartRootArray() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        serializerProvider._startRootArray(gen, null);
        String result = gen.getOutputContent().toString();
        assertTrue(result.contains("<item>"));
    }

    @Test
    public void testInitWithRootName() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
        QName rootName = new QName(null, "root");
        serializerProvider._initWithRootName(gen, rootName);
        String result = gen.getOutputContent().toString();
        assertTrue(result.contains("<root>"));
    }

    @Test
    public void testRootNameFromConfig() throws JsonMappingException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        PropertyName rootName = config.getFullRootName();
        QName expectedRootName = new QName(rootName.getNamespace(), rootName.getSimpleName());
        QName actualRootName = serializerProvider._rootNameFromConfig();
        assertEquals(expectedRootName, actualRootName);
    }
}