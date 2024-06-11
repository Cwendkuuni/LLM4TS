
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerializerProviderTest {

    private final XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
    private final XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

    @Test
    public void testConstructor() {
        Assert.assertEquals(rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copyProvider = provider.copy();
        Assert.assertEquals(provider._rootNameLookup, copyProvider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new XmlMapper().getSerializationConfig();
        SerializerProvider jsf = new XmlMapper().getSerializerFactory().createSerializerProvider();
        XmlSerializerProvider instanceProvider = provider.createInstance(config, jsf);
        Assert.assertEquals(provider._rootNameLookup, instanceProvider._rootNameLookup);
    }

    @Test
    public void testSerializeValue() throws JsonProcessingException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        String value = "test value";

        try {
            provider.serializeValue(gen, value);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeValueWithRootType() throws JsonProcessingException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        String value = "test value";

        try {
            provider.serializeValue(gen, value, String.class, null);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeXmlNull() throws JsonProcessingException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        try {
            provider._serializeXmlNull(gen);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testStartRootArray() throws JsonProcessingException {
        ToXmlGenerator xgen = new ToXmlGenerator(null, 0, null);
        QName rootName = new QName("root");

        try {
            provider._startRootArray(xgen, rootName);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testInitWithRootName() throws JsonProcessingException {
        ToXmlGenerator xgen = new ToXmlGenerator(null, 0, null);
        QName rootName = new QName("root");

        try {
            provider._initWithRootName(xgen, rootName);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName fullName = new PropertyName("root");
        provider._config = new SerializationConfig(new ObjectMapper().getDeserializationConfig(), null, fullName);
        QName expected = new QName("root");

        QName actual = provider._rootNameFromConfig();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAsXmlGenerator() throws JsonProcessingException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        try {
            provider._asXmlGenerator(gen);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWrapAsIOE() throws JsonProcessingException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        Exception exception = new Exception("Test Exception");

        try {
            provider._wrapAsIOE(gen, exception);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
}

