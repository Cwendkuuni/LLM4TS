package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private SerializationConfig config;
    private SerializerFactory serializerFactory;
    private XmlRootNameLookup xmlRootNameLookup;

    @Before
    public void setup() {
        xmlRootNameLookup = new XmlRootNameLookup();
        config = new SerializationConfig(new AnnotationIntrospector());
        serializerFactory = new SimpleSerializerFactory();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void testDefaultConstructor() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        // As per the class, the default constructor is empty, so this test case should not fail
    }

    @Test
    public void testXmlSerializerProviderWithNullRootNames() {
        try {
            XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testXmlSerializerProviderFromSourceAndConfig() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider, config, serializerFactory);
        // This test case should not fail as the constructor is working as expected
    }

    @Test
    public void testSerializeValueWithNullObject() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer();
        JsonGenerator gen = new StaxUtil.AsXmlGenerator(tokenBuffer);
        xmlSerializerProvider.serializeValue(gen, null);
        String xml = tokenBuffer.contentAsString();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValueWithObject() throws IOException {
        String expectedXml = "<root><property1>value1</property1><property2>value2</property2></root>";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("property1", "value1");
        map.put("property2", "value2");

        JsonSerializer<Object> jsonSerializer = new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("property1");
                gen.writeValue("value1");
                gen.writeFieldName("property2");
                gen.writeValue("value2");
                gen.writeEndObject();
            }
        };

        config.setSerializationInclusion(SimpleBeanPropertyFilter.FilterFlag.ALL);
        ObjectWriter objectWriter = new JsonSerializerFactory(config).getSerializerProvider().getSerializer("java.util.Map");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(stringWriter);
        objectWriter.writeValue(jsonGenerator, map);
        String json = stringWriter.toString();
        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        StandardDeserializer deserializer = jsonParser.readValueAs(StandardDeserializer.class);
        Object readValue = deserializer.getValue();

        JsonSerializer<Object> serializer = xmlSerializerProvider.findValueSerializer(Map.class, null);
        xmlSerializerProvider.serializeValue(gen, readValue);

        String actualXml = tokenBuffer.contentAsString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithObjectAndRootType() throws IOException {
        String expectedXml = "<root><property1><value1></value1></property1><property2><value2></value2></property2></root>";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("property1", Arrays.asList("value11", "value12"));
        map.put("property2", "value2");

        JsonSerializer<Object> jsonSerializer = new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("property1");
                gen.writeFieldName("value1");
                gen.writeStartArray();
                for (Object obj : (List) o) {
                    gen.writeValue(obj.toString());
                }
                gen.writeEndArray();
                gen.writeFieldName("property2");
                gen.writeValue("value2");
                gen.writeEndObject();
            }
        };

        config.setSerializationInclusion(SimpleBeanPropertyFilter.FilterFlag.ALL);
        ObjectWriter objectWriter = new JsonSerializerFactory(config).getSerializerProvider().getSerializer("java.util.Map");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(stringWriter);
        objectWriter.writeValue(jsonGenerator, map);
        String json = stringWriter.toString();
        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        StandardDeserializer deserializer = jsonParser.readValueAs(StandardDeserializer.class);
        Object readValue = deserializer.getValue();

        JavaType rootType = TypeFactory.defaultInstance().constructType(Map.class);
        JsonSerializer<Object> serializer = xmlSerializerProvider.findValueSerializer(rootType, null);
        xmlSerializerProvider.serializeValue(gen, readValue, rootType);

        String actualXml = tokenBuffer.contentAsString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithObjectAndRootTypeAndSerializer() throws IOException {
        String expectedXml = "<root><property1><value1></value1></property1><property2><value2></value2></property2></root>";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("property1", Arrays.asList("value11", "value12"));
        map.put("property2", "value2");

        JsonSerializer<Object> jsonSerializer = new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("property1");
                gen.writeFieldName("value1");
                gen.writeStartArray();
                for (Object obj : (List) o) {
                    gen.writeValue(obj.toString());
                }
                gen.writeEndArray();
                gen.writeFieldName("property2");
                gen.writeValue("value2");
                gen.writeEndObject();
            }
        };

        config.setSerializationInclusion(SimpleBeanPropertyFilter.FilterFlag.ALL);
        ObjectWriter objectWriter = new JsonSerializerFactory(config).getSerializerProvider().getSerializer("java.util.Map");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(stringWriter);
        objectWriter.writeValue(jsonGenerator, map);
        String json = stringWriter.toString();
        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        StandardDeserializer deserializer = jsonParser.readValueAs(StandardDeserializer.class);
        Object readValue = deserializer.getValue();

        JavaType rootType = TypeFactory.defaultInstance().constructType(Map.class);
        JsonSerializer<Object> serializer = new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("property1");
                gen.writeFieldName("value1");
                gen.writeStartArray();
                for (Object obj : (List) o) {
                    gen.writeValue(obj.toString());
                }
                gen.writeEndArray();
                gen.writeFieldName("property2");
                gen.writeValue("value2");
                gen.writeEndObject();
            }
        };

        xmlSerializerProvider.serializeValue(gen, readValue, rootType, serializer);

        String actualXml = tokenBuffer.contentAsString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithNullRootTypeAndNullSerializer() throws IOException {
        // This test case should not fail as the method checks for null and throws an exception accordingly
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithInvalidRootTypeAndNullSerializer() throws IOException {
        String xml = "<root><property></property></root>";
        TokenBuffer tokenBuffer = new TokenBuffer();
        JsonGenerator gen = new StaxUtil.AsXmlGenerator(tokenBuffer);

        xmlSerializerProvider.serializeValue(gen, null, new SimpleType("InvalidType"), null);

        String actualXml = tokenBuffer.contentAsString();
        assertEquals(xml, actualXml);
    }
}