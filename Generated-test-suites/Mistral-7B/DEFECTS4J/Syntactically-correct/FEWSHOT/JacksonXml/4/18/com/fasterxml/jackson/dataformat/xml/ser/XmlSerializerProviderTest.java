package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void test_serializeValue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);

        int[] numbers = {1, 2, 3};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.serializeValue(xmlGenerator, numbers);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<item>" +
                "<item>1</item>" +
                "<item>2</item>" +
                "<item>3</item>" +
                "</item>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_serializeValueWithRootType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JavaType javaType = mapper.getTypeFactory().constructType(new Object() {}.getClass().getDeclaredField("root").getType());

        Object root = new Object() {{
            this.root = new int[] {1, 2, 3};
        }};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.serializeValue(xmlGenerator, root, javaType);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<root>" +
                "<item>" +
                "<item>1</item>" +
                "<item>2</item>" +
                "<item>3</item>" +
                "</item>" +
                "</root>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_serializeValueWithJsonSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JsonSerializer<Object> intSerializer = mapper.findValueSerializer(int[].class, null);

        int[] numbers = {1, 2, 3};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.serializeValue(xmlGenerator, numbers, intSerializer);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<item>" +
                "<item>1</item>" +
                "<item>2</item>" +
                "<item>3</item>" +
                "</item>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_serializeValueWithNull() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.serializeValue(xmlGenerator, null);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<null/>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_initWithRootName() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);

        int[] numbers = {1, 2, 3};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.initWithRootName(xmlGenerator, "root");

        xmlProvider.serializeValue(xmlGenerator, numbers);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<root>" +
                "<item>" +
                "<item>1</item>" +
                "<item>2</item>" +
                "<item>3</item>" +
                "</item>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_initWithRootNameWithNamespace() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);

        int[] numbers = {1, 2, 3};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        String namespaceUri = "xmlns:myxml";

        xmlProvider.initWithRootName(xmlGenerator, "root", namespaceUri);

        xmlProvider.serializeValue(xmlGenerator, numbers);

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<myxml:root xmlns:myxml=\"" + namespaceUri + "\">" +
                "<item>" +
                "<item>1</item>" +
                "<item>2</item>" +
                "<item>3</item>" +
                "</item>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_startRootArray() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        int[] numbers = {1, 2, 3};

        ToXmlGenerator xmlGenerator = new ToXmlGenerator(config.getFactory(), null, null, null, null, false, null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.startRootArray(xmlGenerator, "root");

        String actualXml = xmlGenerator.getXmlStreamWriter().asXML();

        String expectedXml = "<root>" +
                "<item>" +
                "</item>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_serializeXmlNull() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JsonGenerator xmlGenerator = mapper.getFactory().createXmlGenerator(new ByteArrayOutputStream(), null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        xmlProvider.serializeXmlNull(xmlGenerator);

        String actualXml = xmlGenerator.asXML();

        String expectedXml = "<null/>";

        assertTrue(actualXml.equals(expectedXml));
    }

    @Test
    public void test_findRootNameFromConfig() throws Exception {
        SerializationConfig config = new SerializationConfig();

        String expectedRootName = "root";

        config.setRootValueSeparator(":");
        config.setRootName("myxml:" + expectedRootName);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();

        String actualRootName = xmlSerializerProvider.findRootNameFromConfig(config);

        assertTrue(actualRootName.equals(expectedRootName));
    }

    @Test
    public void test_asXmlGenerator() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new DefaultSerializerProvider().getFactory();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(new JacksonAnnotationIntrospector(), config, factory);

        JsonGenerator jsonGenerator = mapper.createGenerator(new ByteArrayOutputStream(), null);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(serializerProvider, config, factory);

        ToXmlGenerator xmlGenerator = xmlProvider.asXmlGenerator(jsonGenerator);

        assertNull(xmlGenerator);
    }

    private Object field;

    static class Root {
        int[] root;
    }
}