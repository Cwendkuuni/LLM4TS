package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class XmlSerializerProviderTest extends TestBase {

    @Test
    public void testSerializingNullWithDefaultRootName() throws JsonProcessingException, IOException {
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(null);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("null", xmlNode.get("name").asText());
        assertEquals("", xmlNode.get("namespace").asText());
    }

    @Test
    public void testSerializingNullWithCustomRootName() throws JsonProcessingException, IOException {
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("http://test.com", "rootNameForNull")));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(null);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("rootNameForNull", xmlNode.get("name").asText());
        assertEquals("http://test.com", xmlNode.get("namespace").asText());
    }

    @Test
    public void testSerializingObjectWithDefaultRootName() throws JsonProcessingException, IOException {
        Object obj = new Object() {
            String name = "Test Object";
        };

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(obj);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("Test Object", xmlNode.get("name").asText());
        assertEquals("", xmlNode.get("namespace").asText());
    }

    @Test
    public void testSerializingObjectWithCustomRootName() throws JsonProcessingException, IOException {
        Object obj = new Object() {
            String name = "Test Object";
        };

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("http://test.com", "rootNameForObject")));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(obj);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("Test Object", xmlNode.get("name").asText());
        assertEquals("http://test.com", xmlNode.get("namespace").asText());
    }

    @Test
    public void testSerializingArrayWithDefaultRootName() throws JsonProcessingException, IOException {
        Object[] arr = {1, 2, 3};

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object[].class);

        String xml = xmlWriter.writeValueAsString(arr);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("item", xmlNode.get("name").asText());
        assertEquals("", xmlNode.get("namespace").asText());

        ObjectNode arrayNode = xmlNode.get("item");
        assertEquals(3, arrayNode.size());
        assertEquals(Integer.class, arrayNode.get(0).getClass());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testSerializingArrayWithCustomRootName() throws JsonProcessingException, IOException {
        Object[] arr = {1, 2, 3};

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("http://test.com", "rootNameForArray")));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object[].class);

        String xml = xmlWriter.writeValueAsString(arr);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("rootNameForArray", xmlNode.get("name").asText());
        assertEquals("http://test.com", xmlNode.get("namespace").asText());

        ObjectNode arrayNode = xmlNode.get("item");
        assertEquals(3, arrayNode.size());
        assertEquals(Integer.class, arrayNode.get(0).getClass());
        assertEquals(1, arrayNode.get(0).asInt());
        assertEquals(2, arrayNode.get(1).asInt());
        assertEquals(3, arrayNode.get(2).asInt());
    }

    @Test
    public void testSerializingObjectWithComplexName() throws JsonProcessingException, IOException {
        Object obj = new Object() {
            String name = "Test Object With Complex Name";
            String anotherName = "Another Name";
        };

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("http://test.com", "rootNameForObjectWithComplexName")));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(obj);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("Test Object With Complex Name", xmlNode.get("name").asText());
        assertEquals("Another Name", xmlNode.get("anotherName").asText());
        assertEquals("http://test.com", xmlNode.get("namespace").asText());
    }

    @Test
    public void testSerializingObjectWithEmptyNamespaceInConfig() throws JsonProcessingException, IOException {
        Object obj = new Object() {
            String name = "Test Object";
        };

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(null));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig()
                .setXmlRootElement(new QName("", "root")))
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(obj);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("root", xmlNode.get("name").asText());
        assertEquals("", xmlNode.get("namespace").asText());
        assertEquals("Test Object", xmlNode.get("name").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingObjectWithCustomRootNameWithInvalidNamespace() throws JsonProcessingException, IOException {
        Object obj = new Object() {
            String name = "Test Object";
        };

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("http://test.com/invalid", "rootNameForObject")));
        ObjectWriter xmlWriter = new JsonFactory(new ObjectMapper.DefaultConfig())
                .withSerializerProvider(xmlProvider)
                .withRootValueSeparator(RootValueSeparator.NONE)
                .withWriterFactory(new XMLFactory(false))
                .writerFor(Object.class);

        String xml = xmlWriter.writeValueAsString(obj);
        JsonParser xmlParser = new ObjectMapper().readerForElement(ObjectNode.class)
                .readValue(new StringReader(xml));

        ObjectNode xmlNode = (ObjectNode) xmlParser.getValue();
        assertEquals("rootNameForObject", xmlNode.get("name").asText());
        assertEquals("http://test.com/invalid", xmlNode.get("namespace").asText());

        // The following line should throw an exception
        xmlWriter.writeValueAsString(Arrays.asList("test", "string"));
    }
}