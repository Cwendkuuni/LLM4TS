package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.xml.namespace.QName;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
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
    public void testCreateInstance() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory factory = new SerializerFactory();
        XmlSerializerProvider result = provider.createInstance(config, factory);
        assertEquals(XmlSerializerProvider.class, result.getClass());
        assertSame(config, result.getConfig());
        assertSame(factory, result.getFactory());
        assertSame(lookup, result._rootNameLookup);
    }

    @Test
    public void testSerializeValue_1() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue_2() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        provider.serializeValue(gen, value, rootType);
    }

    @Test
    public void testSerializeValue_3() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<Object>();
        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_serializeXmlNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jgen = new JsonGenerator();
        provider._serializeXmlNull(jgen);
    }

    @Test
    public void test_startRootArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        provider._startRootArray(xgen, rootName);
    }

    @Test
    public void test_initWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        provider._initWithRootName(xgen, rootName);
    }

    @Test
    public void test_rootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        QName result = provider._rootNameFromConfig();
        assertNull(result);
    }

    @Test
    public void test_asXmlGenerator_1() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator gen = new JsonGenerator();
        provider._asXmlGenerator(gen);
    }

    @Test
    public void test_asXmlGenerator_2() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TokenBuffer gen = new TokenBuffer();
        provider._asXmlGenerator(gen);
    }
}