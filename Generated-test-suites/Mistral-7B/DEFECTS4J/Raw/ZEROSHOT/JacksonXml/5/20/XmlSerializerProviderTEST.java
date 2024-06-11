package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(mapper.getFactory());
        SerializationConfig config = mapper.getSerializationConfig();
        SerializerFactory serializerFactory = mapper.getSerializerProvider().getDefaultSerializerFactory();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);

        // Test constructor with null rootNameLookup
        try {
            new XmlSerializerProvider(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("rootNameLookup argument cannot be null", e.getMessage());
        }

        // Test constructor with valid rootNameLookup
        XmlSerializerProvider xmlProviderWithRootNameLookup = new XmlSerializerProvider(rootNameLookup, config, serializerFactory);

        // Test createInstance
        XmlSerializerProvider xmlProviderCopy = xmlProviderWithRootNameLookup.copy();
        XmlSerializerProvider xmlProviderConfig = xmlProviderWithRootNameLookup.createInstance(config, serializerFactory);

        // Test copy
        assertEquals(xmlProviderWithRootNameLookup, xmlProviderCopy);
        assertNotSame(xmlProviderWithRootNameLookup, xmlProviderConfig);

        // Test _rootNameFromConfig
        PropertyName rootNameProperty = config.getRootValueSerializer().getPropertyName();
        QName rootName = xmlProviderWithRootNameLookup._rootNameFromConfig();
        assertEquals(rootNameProperty.getSimpleName(), rootName.getLocalPart());
        assertNull(rootName.getNamespaceURI());

        // Test _asXmlGenerator
        ObjectMapper mapperForTest = new ObjectMapper();
        ObjectWriter writer = mapperForTest.writer().withRootValueSerializer(new RootValueSerializer());
        JsonGenerator jsonGenerator = writer.writeValueAsXml(new Object());
        ToXmlGenerator toXmlGenerator = (ToXmlGenerator) jsonGenerator;
        assertNotNull(_rootNameFromConfig(xmlProviderWithRootNameLookup, toXmlGenerator));

        // Test _serializeXmlNull
        JsonGenerator jsonGeneratorForNull = writer.writeValueAsXml(null);
        ObjectNode rootNode = mapperForTest.readTree(jsonGeneratorForNull);
        String rootNameForNull = rootNode.get("name").asText();
        assertEquals("null", rootNameForNull);

        // Test _initWithRootName
        String xmlRootName = "rootElement";
        ObjectNode rootObjectNode = mapper.createObjectNode();
        rootObjectNode.put("name", PropertyName.construct(xmlRootName, null));
        config.setRootValueSerializer(new RootValueSerializer(xmlRootName));
        JsonGenerator xmlGenerator = mapper.writer().withRootValueSerializer(new RootValueSerializer(xmlRootName)).writeValueAsXml(new Object());
        ToXmlGenerator xmlToXmlGenerator = (ToXmlGenerator) xmlGenerator;
        xmlProviderWithRootNameLookup._initWithRootName(xmlToXmlGenerator, new QName(null, xmlRootName));
        assertEquals(xmlRootName, xmlToXmlGenerator.getLocalName());

        // Test _startRootArray
        ObjectNode arrayObjectNode = mapper.createObjectNode();
        arrayObjectNode.put("name", PropertyName.construct("array", null));
        config.setRootValueSerializer(new RootValueSerializer("array"));
        JsonGenerator arrayJsonGenerator = mapper.writer().withRootValueSerializer(new RootValueSerializer("array")).writeValueAsXml(new Object());
        ToXmlGenerator arrayToXmlGenerator = (ToXmlGenerator) arrayJsonGenerator;
        xmlProviderWithRootNameLookup._startRootArray(arrayToXmlGenerator, new QName(null, "array"));
        assertEquals("array", arrayToXmlGenerator.getLocalName());
        assertEquals("item", arrayToXmlGenerator.getNextName());

        // Test serializeValue
        QName rootNameForObject = new QName(null, "object");
        xmlProviderWithRootNameLookup.setSerializerProviders(new SerializerProvider[] { xmlProviderWithRootNameLookup, xmlProviderCopy, xmlProviderConfig });
        Object testObject = new Object();
        ObjectNode testObjectNode = mapper.createObjectNode();
        testObjectNode.put("name", PropertyName.construct("testObject", null));
        config.setRootValueSerializer(new RootValueSerializer(rootNameForObject.getLocalPart()));
        SerializerProvider[] serializerProviders = { xmlProviderWithRootNameLookup, xmlProviderCopy, xmlProviderConfig };
        for (SerializerProvider serializerProvider : serializerProviders) {
            JsonSerializer<Object> serializer = serializerProvider.findValueSerializer(Object.class, null);
            JsonGenerator jsonGeneratorForObject = serializerProvider.getFactory().createGenerator(new ByteArrayOutputStream(), "UTF-8");
            serializer.serialize(testObject, jsonGeneratorForObject, serializerProvider);
            JsonNode jsonNode = mapperForTest.readTree(jsonGeneratorForObject);
            String localName = jsonNode.get("name").asText();
            assertEquals("testObject", localName);
            assertEquals(rootNameForObject.getLocalPart(), jsonNode.get("name").get("@xmlns:xsi").asText());
            assertEquals(rootNameForObject.getLocalPart(), jsonNode.get("name").get("@xmlns").asText());
            jsonGeneratorForObject.close();
        }

        // Test serializeValue with JavaType
        JsonSerializer<Object> serializerForJavaType = mapperForTest.findValueSerializer(Object.class, null);
        ObjectNode testObjectNodeForJavaType = mapperForTest.createObjectNode();
        testObjectNodeForJavaType.put("name", PropertyName.construct("testObject", null));
        config.setRootValueSerializer(new RootValueSerializer(rootNameForObject.getLocalPart()));
        JavaType javaType = mapperForTest.getTypeFactory().constructType(new TypeReference<Object[]>() {});
        Object[] testObjectArray = new Object[] { new Object(), new Object() };
        for (SerializerProvider serializerProvider : serializerProviders) {
            JsonGenerator jsonGeneratorForArray = serializerProvider.getFactory().createGenerator(new ByteArrayOutputStream(), "UTF-8");
            serializerForJavaType.serialize(testObjectArray, jsonGeneratorForArray, serializerProvider);
            JsonNode jsonNodeForArray = mapperForTest.readTree(jsonGeneratorForArray);
            assertEquals("[object]", jsonNodeForArray.get("name").get("@xmlns:xsi").asText());
            assertEquals("[object]", jsonNodeForArray.get("name").get("@xmlns").asText());
            String localNameForArray = jsonNodeForArray.get(0).get("item").get("@name").asText();
            assertEquals("testObject", localNameForArray);
            String localNameForArrayItem = jsonNodeForArray.get(0).get("item").get(0).get("@name").asText();
            assertEquals("testObject", localNameForArrayItem);
            assertEquals("testObject", jsonNodeForArray.get(1).get("item").get(0).get("@name").asText());
            jsonGeneratorForArray.close();
        }

        // Test _wrapAsIOE
        JsonMappingException jsonMappingException = new JsonMappingException("test", null);
        IOException ioException = xmlProviderWithRootNameLookup._wrapAsIOE(null, jsonMappingException);
        assertEquals("Root value serializer failure: test", ioException.getMessage());
    }

    private QName _rootNameFromConfig(XmlSerializerProvider xmlProvider, ToXmlGenerator toXmlGenerator) {
        QName rootName = xmlProvider._rootNameFromConfig();
        if (rootName == null) {
            rootName = xmlProvider._rootNameLookup.findRootName(toXmlGenerator.getSerializerProvider().getClass(), xmlProvider._config);
        }
        return rootName;
    }

}

class RootValueSerializer extends JsonSerializer<Object> {

    private String xmlRootName;

    public RootValueSerializer() {
        this(null);
    }

    public RootValueSerializer(String xmlRootName) {
        this.xmlRootName = xmlRootName;
    }

    @Override
    public void serialize(Object o, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (xmlRootName != null) {
            jgen.writeStartObject();
            jgen.writeFieldName("name");
            jgen.writeFieldName(xmlRootName);
            jgen.writeEndObject();
        } else {
            super.serialize(o, jgen, provider);
        }
    }

}