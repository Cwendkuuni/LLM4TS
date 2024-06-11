package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void test_copy() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider dest = src.copy();

        assertTrue(dest instanceof XmlSerializerProvider);
        assertNull(dest._rootNameLookup);
        assertEquals(src._rootNameLookup, src._rootNameLookup);
    }

    @Test
    public void test_createInstance() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider dest = new XmlSerializerProvider(src, config, null);

        assertTrue(dest instanceof XmlSerializerProvider);
        assertNotSame(src._rootNameLookup, dest._rootNameLookup);
        assertEquals(src._rootNameLookup, rootNames);
    }

    @Test
    public void test_createInstance_noCopyRootNameLookup() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider dest = new XmlSerializerProvider(src);

        assertTrue(dest instanceof XmlSerializerProvider);
        assertSame(src._rootNameLookup, dest._rootNameLookup);
    }

    @Test
    public void test_serializeValue() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        JsonGenerator gen = new TokenBuffer();
        Object value = null;
        provider.serializeValue(gen, value);
        String result = gen.getAsString();
        assertEquals("<null/>", result);

        value = "";
        provider.serializeValue(gen, value);
        result = gen.getAsString();
        assertEquals("<></>", result);

        value = 123;
        provider.serializeValue(gen, value);
        result = gen.getAsString();
        assertEquals("<123 xmlns=\"\"/>", result);

        value = new Object();
        provider.serializeValue(gen, value);
        result = gen.getAsString();
        String expected = "<rootName xmlns=\"\"><fieldName>...";
        assertTrue(result.startsWith(expected));

        provider.setDefaultSerializerProvider(new DefaultSerializerProvider());
        provider.serializeValue(gen, value);
        result = gen.getAsString();
        assertEquals("<rootName xmlns=\"\"><fieldName>...", result);
    }

    @Test
    public void test_serializeValue_withRootType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        JavaType rootType = null;
        JsonSerializer<Object> ser = null;

        // Test serialization with default root type
        JsonGenerator gen = new TokenBuffer();
        Object value = new Object();
        provider.serializeValue(gen, value, rootType, ser);
        String result = gen.getAsString();
        String expected = "<rootName xmlns=\"\"><fieldName>...";
        assertTrue(result.startsWith(expected));

        // Test serialization with explicit root type
        rootType = new SimpleType("RootType");
        ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                gen.writeString("Value");
            }
        };
        gen = new TokenBuffer();
        value = new Object();
        provider.serializeValue(gen, value, rootType, ser);
        result = gen.getAsString();
        assertEquals("<RootType><fieldName>Value</fieldName></RootType>", result);
    }

    @Test
    public void test_serializeValue_nullRootType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        JsonGenerator gen = new TokenBuffer();
        Object value = new Object();
        provider.serializeValue(gen, value, null, null);
        String result = gen.getAsString();
        String expected = "<rootName xmlns=\"\"><fieldName>...";
        assertTrue(result.startsWith(expected));
    }

    @Test
    public void test_serializeValue_filtered() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

        JsonGenerator gen = new TokenBuffer();
        Object value = new Object();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFilterIDs(new String[]{"MyFilter"});
        SimpleBeanPropertyFilter filter = filterProvider.getFilterInstance("MyFilter");
        provider.setFilterProvider(filterProvider);

        provider.serializeValue(gen, value);
        String result = gen.getAsString();
        assertTrue(result.isEmpty());
    }
}