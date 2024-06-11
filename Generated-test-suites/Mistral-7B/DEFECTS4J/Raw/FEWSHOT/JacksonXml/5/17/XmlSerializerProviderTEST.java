package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.dataformat.xml.util.*;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueWithNullRootType() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        JsonGenerator gen = StaxUtil.createJsonGenerator("test.xml");
        provider.serializeValue(gen, null);
        gen.close();
    }

    @Test
    public void testSerializeValueWithRootType() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ObjectMapper mapper = new ObjectMapper(new XMLFactory(), new DefaultSerializationConfig());
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        JsonSerializer<Object> ser = mapper.findValueSerializer(String[].class, null);
        provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), provider.getDefaultSerializerFactory());
        Object value = new String[]{"foo", "bar"};
        JsonGenerator gen = StaxUtil.createJsonGenerator("test.xml");
        provider.serializeValue(gen, value, rootType, ser);
        gen.close();
    }

    @Test
    public void testSerializeValueWithNullValue() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        JsonGenerator gen = StaxUtil.createJsonGenerator("test.xml");
        provider.serializeValue(gen, null);
        gen.close();
    }

    @Test
    public void testSerializeValueWithNonIndexedType() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ObjectMapper mapper = new ObjectMapper(new XMLFactory(), new DefaultSerializationConfig());
        JavaType rootType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> ser = mapper.findValueSerializer(String.class, null);
        provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), provider.getDefaultSerializerFactory());
        String value = "foo";
        JsonGenerator gen = StaxUtil.createJsonGenerator("test.xml");
        provider.serializeValue(gen, value, rootType, ser);
        gen.close();
    }

    @Test
    public void testSerializeValueWithIndexedType() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ObjectMapper mapper = new ObjectMapper(new XMLFactory(), new DefaultSerializationConfig());
        JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
        JsonSerializer<Object> ser = mapper.findValueSerializer(String[].class, null);
        provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), provider.getDefaultSerializerFactory());
        String[] value = new String[]{"foo", "bar"};
        JsonGenerator gen = StaxUtil.createJsonGenerator("test.xml");
        provider.serializeValue(gen, value, rootType, ser);
        gen.close();
    }

    @Test
    public void testInitWithRootName() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        provider._initWithRootName(xgen, new QName("test"));
        verify(xgen).setNextName("test");
    }

    @Test
    public void testInitWithRootNameExisting() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(true);
        provider._initWithRootName(xgen, new QName("test"));
        verifyNoMoreInteractions(xgen);
    }

    @Test
    public void testStartRootArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider._startRootArray(xgen, new QName("test"));
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName("item");
    }

    @Test
    public void testRootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        PropertyName rootName = new PropertyName("test:root");
        provider._config.setRootValueSeparator(':');
        assertEquals(new QName("test", "root"), provider._rootNameFromConfig());
    }

    @Test
    public void testRootNameFromConfigEmptyNamespace() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        PropertyName rootName = new PropertyName("root");
        assertEquals(new QName("root"), provider._rootNameFromConfig());
    }

    @Test
    public void testAsXmlGenerator() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        JsonGenerator gen = new TokenBuffer();
        ToXmlGenerator xgen = provider._asXmlGenerator(gen);
        assertNull(xgen);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorInvalidGenerator() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
        JsonGenerator gen = new Object();
        provider._asXmlGenerator(gen);
    }
}