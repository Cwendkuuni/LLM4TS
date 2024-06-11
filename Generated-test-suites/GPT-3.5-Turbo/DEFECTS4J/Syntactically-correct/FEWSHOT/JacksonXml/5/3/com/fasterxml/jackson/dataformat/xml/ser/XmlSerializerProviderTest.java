package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;

    @Before
    public void setUp() {
        provider = new XmlSerializerProvider(new XmlRootNameLookup());
    }

    @Test
    public void testConstructor1() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        assertSame(rootNames, provider._rootNameLookup);
    }

    @Test
    public void testConstructor2() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory f = new DefaultSerializerFactory();
        XmlSerializerProvider src = new XmlSerializerProvider(new XmlRootNameLookup());
        XmlSerializerProvider provider = new XmlSerializerProvider(src,
                config, f);

        assertSame(src._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testConstructor3() {
        XmlSerializerProvider src = new XmlSerializerProvider(new XmlRootNameLookup());
        XmlSerializerProvider provider = new XmlSerializerProvider(src);

        assertSame(src._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copy = provider.copy();

        assertNotSame(copy, provider);
        assertSame(copy._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory f = new DefaultSerializerFactory();
        XmlSerializerProvider instance = provider.createInstance(config, f);

        assertNotSame(instance, provider);
        assertSame(instance._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testSerializeValue1() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = null;

        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue2() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = new ArrayList<>();

        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue3() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = "test";

        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue4() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = 123;

        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue5() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = new TestObject();

        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue6() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = new TestObject();

        provider.serializeValue(gen, value, null, null);
    }

    @Test
    public void testSerializeValue7() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = new TestObject();

        provider.serializeValue(gen, value, null, null);
    }

    @Test
    public void test_serializeValue1() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = null;
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;

        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_serializeValue2() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = new ArrayList<>();
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;

        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_serializeValue3() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = "test";
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;

        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_serializeValue4() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Object value = 123;
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;

        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_wrapAsIOE1() {
        JsonGenerator g = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Exception e = new IOException();

        IOException ioe = provider._wrapAsIOE(g, e);

        assertSame(e, ioe);
    }

    @Test
    public void test_wrapAsIOE2() {
        JsonGenerator g = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        Exception e = new RuntimeException();

        IOException ioe = provider._wrapAsIOE(g, e);

        assertNotSame(e, ioe);
        assertEquals(e.getClass().getName(), ioe.getMessage());
    }

    private static class TestObject {
    }
}