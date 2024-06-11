package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XmlSerializerProviderTest {

    private XmlMapper mapper;
    private SerializerProvider provider;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mapper = new XmlMapper();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JacksonXmlModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        provider = mapper.getSerializerProvider();
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, IOException {
        String expectedXml = "<null/>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = xmlProvider.getFactory().createGenerator(outputStream);
        xmlProvider.serializeValue(jsonGenerator, null);
        jsonGenerator.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        byte[] jsonBytes = objectMapper.writeValueAsBytes(null);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("null", elements.get(0).getLocalPart());
    }

    @Test
    public void testSerializeEmptyString() throws JsonGenerationException, IOException {
        String expectedXml = "<string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:string\"></string>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<String> serializer = provider.findValueSerializer(String.class, null);
        serializer.serialize(Collections.emptyList(), outputStream, provider);
        outputStream.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        String emptyString = "";
        byte[] jsonBytes = objectMapper.writeValueAsBytes(emptyString);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("string", elements.get(0).getLocalPart());
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        String inputString = "Hello World";
        String expectedXml = "<string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"xsd:string\">Hello World</string>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<String> serializer = provider.findValueSerializer(String.class, null);
        serializer.serialize(inputString, outputStream, provider);
        outputStream.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        byte[] jsonBytes = objectMapper.writeValueAsBytes(inputString);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("string", elements.get(0).getLocalPart());
        Assert.assertEquals("Hello World", elements.get(0).getLocalPart() + "][1]");
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        Integer inputInt = 123;
        String expectedXml = "<int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:int\">123</int>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<Number> serializer = provider.findValueSerializer(Number.class, null);
        serializer.serialize(inputInt, outputStream, provider);
        outputStream.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        byte[] jsonBytes = objectMapper.writeValueAsBytes(inputInt);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("int", elements.get(0).getLocalPart());
        Assert.assertEquals("123", elements.get(0).getLocalPart() + "][1]");
    }

    @Test
    public void testSerializeList() throws JsonGenerationException, IOException {
        List<Integer> inputList = Arrays.asList(1, 2, 3);
        String expectedXml = "<int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:int\" xsi:arrayType=\"xsd:int[3]\">" +
                "<int>1</int>" +
                "<int>2</int>" +
                "<int>3</int>" +
                "</int>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<List<Number>> serializer = provider.findValueSerializer(List.class, Number.class);
        serializer.serialize(inputList, outputStream, provider);
        outputStream.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        byte[] jsonBytes = objectMapper.writeValueAsBytes(inputList);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("int", elements.get(0).getLocalPart());
        Assert.assertEquals("int[3]", elements.get(0).getLocalPart() + "[1]");
        List<QName> arrayElements = elements.get(0).getChildElements();
        Assert.assertEquals(3, arrayElements.size());
        for (int i = 0; i < arrayElements.size(); i++) {
            Assert.assertEquals("int", arrayElements.get(i).getLocalPart());
        }
    }

    @Test
    public void testSerializeCustomObject() throws JsonGenerationException, IOException {
        CustomObject inputObject = new CustomObject("Test");
        String expectedXml = "<customObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:type=\"customObject\">" +
                "<string>Test</string>" +
                "</customObject>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<CustomObject> serializer = provider.findValueSerializer(CustomObject.class, null);
        serializer.serialize(inputObject, outputStream, provider);
        outputStream.flush();
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);

        // Test with Jackson provider
        byte[] jsonBytes = objectMapper.writeValueAsBytes(inputObject);
        String jsonString = new String(jsonBytes, "UTF-8");
        Assert.assertNotNull(jsonString);
        JsonSchema schema = SchemaFactory.getXmlSchema(new ByteArrayInputStream(jsonBytes));
        JsonSchema rootSchema = schema.getRootNode();
        List<QName> elements = rootSchema.getElements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("customObject", elements.get(0).getLocalPart());
        List<QName> properties = elements.get(0).getChildElements();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("string", properties.get(0).getLocalPart());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullSerializer() throws JsonGenerationException, IOException {
        String inputString = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<String> serializer = provider.findValueSerializer(String.class, null);
        serializer.serialize(inputString, outputStream, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidSerializer() throws JsonGenerationException, IOException {
        Object invalidSerializer = new Object();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<String> serializer = provider.findValueSerializer(String.class, null);
        serializer = (JsonSerializer<String>) invalidSerializer;
        serializer.serialize(null, outputStream, provider);
    }
}