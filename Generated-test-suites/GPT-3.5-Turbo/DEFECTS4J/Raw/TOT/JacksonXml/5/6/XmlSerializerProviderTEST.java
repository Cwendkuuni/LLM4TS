
package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.IOException;

public class XmlSerializerProviderTest {

    @Test
    public void testCopy(){
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory factory = new SerializerFactory() {
            @Override
            public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType keyType, JsonSerializer<Object> defaultImpl) {
                return null;
            }

            @Override
            public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType baseType) {
                return null;
            }
        };

        // Execute
        DefaultSerializerProvider copyProvider = provider.createInstance(config, factory);

        // Verify
        Assert.assertNotNull(copyProvider);
        Assert.assertNotSame(provider, copyProvider);
    }

    @Test
    public void testSerializeValue() throws IOException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);

        // Execute
        provider.serializeValue(generator, null);

        // Verify
        // No assertion, just verifying if no exception is thrown
    }

    @Test
    public void testSerializeValue_withRootType() throws IOException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);
        JavaType rootType = JavaType.nullType();

        // Execute
        provider.serializeValue(generator, null, rootType, null);

        // Verify
        // No assertion, just verifying if no exception is thrown
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);

        // Execute
        provider._serializeXmlNull(generator);

        // Verify
        // No assertion, just verifying if no exception is thrown
    }

    @Test
    public void testStartRootArray() throws IOException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ToXmlGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);
        QName rootName = new QName("test");

        // Execute
        provider._startRootArray(generator, rootName);

        // Verify
        // No assertion, just verifying if no exception is thrown
    }

    @Test
    public void testInitWithRootName() throws IOException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ToXmlGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);
        QName rootName = new QName("test");

        // Execute
        provider._initWithRootName(generator, rootName);

        // Verify
        // No assertion, just verifying if no exception is thrown
    }

    @Test
    public void testRootNameFromConfig() {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        PropertyName propertyName = new PropertyName("test");

        // Execute
        QName rootName = provider._rootNameFromConfig(propertyName);

        // Verify
        Assert.assertEquals(new QName("test"), rootName);
    }

    @Test
    public void testRootNameFromConfig_withNamespace() {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        PropertyName propertyName = new PropertyName("test", "namespace");

        // Execute
        QName rootName = provider._rootNameFromConfig(propertyName);

        // Verify
        Assert.assertEquals(new QName("namespace", "test"), rootName);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator generator = new XmlMapper().getFactory().createGenerator(System.out);

        // Execute
        ToXmlGenerator xmlGenerator = provider._asXmlGenerator(generator);

        // Verify
        Assert.assertNull(xmlGenerator);
    }

    @Test
    public void testAsXmlGenerator_withTokenBuffer() throws JsonMappingException {
        // Set up
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator generator = new TokenBuffer(new XmlMapper(), false);

        // Execute
        ToXmlGenerator xmlGenerator = provider._asXmlGenerator(generator);

        // Verify
        Assert.assertNull(xmlGenerator);
    }

}

