package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootNameLookup(rootNameLookup);

        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);
        SerializeTest.serialize(new Object(), serProvider, jsonSerializer, gen, javaType);
        String xmlString = gen.getAsString();
        assertTrue(xmlString.startsWith("<MyClass>"));
    }

    @Test
    public void testSerializationWithNullRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootNameLookup(rootNameLookup);

        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);
        Object nullValue = null;
        try {
            SerializeTest.serialize(nullValue, serProvider, jsonSerializer, gen, javaType);
        } catch (JsonMappingException e) {
            assertNull(e.getCauses());
            String expectedMessage = "Root name cannot be null";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testSerializationWithEmptyRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootNameLookup(rootNameLookup);

        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);
        Object nullValue = null;
        try {
            serProvider.setNullValueSerializer(new StdSerializer<Object>(Object.class) {
                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                    gen.writeNull();
                }
            });
            SerializeTest.serialize(nullValue, serProvider, jsonSerializer, gen, javaType);
        } catch (JsonMappingException e) {
            assertNull(e.getCauses());
            String expectedMessage = "Root name cannot be empty";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testSerializationWithInvalidRootName() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootNameLookup(rootNameLookup);

        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);

        String invalidRootName = "MyClass@123";
        config.setRootName(new QName(invalidRootName));

        Object nullValue = null;
        try {
            SerializeTest.serialize(nullValue, serProvider, jsonSerializer, gen, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertNull(e.getCauses());
            String expectedMessage = "Invalid root name: " + invalidRootName;
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testSerializationWithRootNameFromLookup() throws IOException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        config.setRootNameLookup(rootNameLookup);

        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);

        String className = "MyClass";
        String expectedRootName = "MyClassElement";
        rootNameLookup.addMapping(className, new QName(expectedRootName));

        Object obj = new Object();
        SerializeTest.serialize(obj, serProvider, jsonSerializer, gen, javaType);
        String xmlString = gen.getAsString();
        assertTrue(xmlString.startsWith("<" + expectedRootName + ">"));
    }

    @Test
    public void testSerializationWithNullRootNameLookup() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);

        XmlRootNameLookup rootNameLookup = null;
        try {
            serProvider.setRootNameLookup(rootNameLookup);
            Object nullValue = null;
            SerializeTest.serialize(nullValue, serProvider, jsonSerializer, gen, javaType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Root name lookup cannot be null";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testSerializationWithEmptyRootNameLookup() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        try {
            serProvider.setRootNameLookup(rootNameLookup);
            Object nullValue = null;
            SerializeTest.serialize(nullValue, serProvider, jsonSerializer, gen, javaType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Root name lookup must not be empty";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testSerializationWithInvalidRootNameLookupMapping() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JavaType javaType = new SimpleType("MyClass").withoutPropertyOrder();
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        SerializerProvider serProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider().findSerializerProviderWithRootName(javaType));
        JsonGenerator gen = new TokenBuffer(new StringBuilder(), null, 0, null);

        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        rootNameLookup.addMapping("MyClass", null);
        try {
            serProvider.setRootNameLookup(rootNameLookup);
            Object obj = new Object();
            SerializeTest.serialize(obj, serProvider, jsonSerializer, gen, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}