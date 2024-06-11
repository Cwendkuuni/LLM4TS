package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    @Test
    public void testConstructor1() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        assertSame(rootNames, provider._rootNameLookup);
    }

    @Test
    public void testConstructor2() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory factory = new DefaultSerializerProvider.Impl();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        src.setConfig(config);
        src.setSerializerFactory(factory);
        XmlSerializerProvider provider = new XmlSerializerProvider(src, config, factory);
        assertSame(src._rootNameLookup, provider._rootNameLookup);
        assertSame(config, provider.getConfig());
        assertSame(factory, provider.getSerializerFactory());
    }

    @Test
    public void testConstructor3() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory factory = new DefaultSerializerProvider.Impl();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        src.setConfig(config);
        src.setSerializerFactory(factory);
        XmlSerializerProvider provider = new XmlSerializerProvider(src);
        assertSame(src._rootNameLookup, provider._rootNameLookup);
        assertNotSame(config, provider.getConfig());
        assertSame(factory, provider.getSerializerFactory());
    }

    @Test
    public void testCopy() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        src.setConfig(new SerializationConfig(null, null, null));
        src.setSerializerFactory(new DefaultSerializerProvider.Impl());
        XmlSerializerProvider provider = src.copy();
        assertEquals(src._rootNameLookup, provider._rootNameLookup);
        assertNotSame(src.getConfig(), provider.getConfig());
        assertSame(src.getSerializerFactory(), provider.getSerializerFactory());
    }

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        src.setConfig(new SerializationConfig(null, null, null));
        src.setSerializerFactory(new DefaultSerializerProvider.Impl());
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory factory = new DefaultSerializerProvider.Impl();
        XmlSerializerProvider provider = src.createInstance(config, factory);
        assertEquals(src._rootNameLookup, provider._rootNameLookup);
        assertNotSame(src.getConfig(), provider.getConfig());
        assertSame(factory, provider.getSerializerFactory());
    }

    @Test
    public void testSerializeValue1() {
        TokenBuffer buf = new TokenBuffer(null, false);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            provider.serializeValue(buf, null);
            fail("Expected IOException");
        } catch (IOException e) {}
    }

    @Test
    public void testSerializeValue2() {
        JsonGenerator gen = new ToXmlGenerator(null, 0, null);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            provider.serializeValue(gen, null);
            fail("Expected IOException");
        } catch (IOException e) {}
    }

    @Test
    public void testSerializeValue3() {
        TokenBuffer buf = new TokenBuffer(null, false);
        JavaType rootType = new JavaType(0, null, null);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            provider.serializeValue(buf, null, rootType, null);
            fail("Expected IOException");
        } catch (IOException e) {}
    }

    @Test
    public void testSerializeValue4() {
        JsonGenerator gen = new ToXmlGenerator(null, 0, null);
        JavaType rootType = new JavaType(0, null, null);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            provider.serializeValue(gen, null, rootType, null);
            fail("Expected IOException");
        } catch (IOException e) {}
    }

    @Test
    public void testSerializeValue5() {
        JsonGenerator gen = new ToXmlGenerator(null, 0, null);
        Object value = new Object();
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        try {
            provider.serializeValue(gen, value);
            fail("Expected IOException");
        } catch (IOException e) {}
    }
}