package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig();
        SerializerFactory serializerFactory = new DefaultSerializerProvider().getSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(provider);
        assertEquals(provider.getClass(), XmlSerializerProvider.class);

        XmlSerializerProvider copy = provider.copy();
        assertNotNull(copy);
        assertEquals(copy.getClass(), XmlSerializerProvider.class);
        assertNotSame(provider, copy);

        XmlSerializerProvider newInstance = XmlSerializerProvider.createInstance(config, serializerFactory);
        assertNotNull(newInstance);
        assertEquals(newInstance.getClass(), XmlSerializerProvider.class);

        XmlSerializerProvider deepCopy = new XmlSerializerProvider(provider);
        assertNotNull(deepCopy);
        assertEquals(deepCopy.getClass(), XmlSerializerProvider.class);
        assertNotSame(provider, deepCopy);
    }

    @Test
    public void testSerializeValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig();
        SerializerFactory serializerFactory = objectMapper.getSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        // Test serializing a null value
        TokenBuffer tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, null);
        String serializedNull = tokenBuffer.asXml();
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></null>", serializedNull);

        // Test serializing a non-null value
        String value = "test";
        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(String.class, null);
        tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, value, serializer);
        String serializedValue = tokenBuffer.asXml();
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test>", serializedValue);

        // Test serializing a non-null value with root type and serializer
        JavaType rootType = objectMapper.getTypeFactory().constructType(String[].class);
        JsonSerializer<Object> rootSerializer = objectMapper.findSerializer(String[].class);
        tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, value, rootType, rootSerializer);
        String serializedValueWithRootType = tokenBuffer.asXml();
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">test</item></test>", serializedValueWithRootType);
    }

    @Test
    public void testSerializeValueWithFilter() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig();
        config.setFilterProvider(new SimpleFilterProvider().setFilter(SimpleBeanPropertyFilter.filterOutAllExcept("name")));
        SerializerFactory serializerFactory = objectMapper.getSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup, config, serializerFactory);

        // Test serializing a null value
        TokenBuffer tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, null);
        String serializedNull = tokenBuffer.asXml();
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></null>", serializedNull);

        // Test serializing a non-null value
        String value = "test";
        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(String.class, null);
        tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, value, serializer);
        String serializedValue = tokenBuffer.asXml();
        assertEquals("<name xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><![CDATA[test]]></name>", serializedValue);
    }

    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.writeStartObject()).thenReturn(xgen);
        when(xgen.setNextName(any(QName.class))).thenReturn(xgen);
        when(xgen.initGenerator()).thenReturn(xgen);

        provider._initWithRootName(xgen, new QName("root"));

        verify(xgen).writeStartObject();
        verify(xgen).setNextName(eq(new QName("root")));
        verify(xgen).initGenerator();
    }

    @Test
    public void testInitWithRootNameIfMissing() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.writeStartObject()).thenReturn(xgen);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(xgen);
        when(xgen.initGenerator()).thenReturn(xgen);

        provider._initWithRootName(xgen, new QName("root"));

        verify(xgen, times(1)).writeStartObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorNull() throws IOException {
        JsonGenerator gen = null;
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._asXmlGenerator(gen);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorNotToXmlGenerator() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig();
        SerializerFactory serializerFactory = objectMapper.getSerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator gen = objectMapper.treeAsTokens(new ObjectMapper().valueToTree(new Object()));

        provider._asXmlGenerator(gen);
    }
}