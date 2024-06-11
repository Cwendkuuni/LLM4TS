package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.impl.RootNameSerializer;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProviderCopiedInstance() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider originalProvider = new XmlSerializerProvider(rootNames, config, null);

        XmlSerializerProvider copiedProvider = originalProvider.copy();

        assertTrue(copiedProvider instanceof XmlSerializerProvider);
        assertEquals(originalProvider, copiedProvider.getProviderInstance());
        assertEquals(originalProvider._rootNameLookup, copiedProvider._rootNameLookup);
    }

    @Test
    public void testXmlSerializerProviderCreatedInstance() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        SerializerProvider createdProvider = provider.createInstance(config, null);

        assertTrue(createdProvider instanceof XmlSerializerProvider);
        assertEquals(provider, createdProvider.getProviderInstance());
        assertEquals(provider._rootNameLookup, createdProvider._rootNameLookup);
    }

    @Test
    public void testXmlSerializerProviderSerialization() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonSerializer<Object> jsonSerializer = new RootNameSerializer(provider, null);
        ObjectWriter objectWriter = new JsonFactory(config).writerWithRootValueSerializer(jsonSerializer);
        objectWriter.writeValue(stringWriter, null);

        String json = stringWriter.toString();
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider deserializedProvider = mapper.readValue(json, XmlSerializerProvider.class);

        assertTrue(deserializedProvider instanceof XmlSerializerProvider);
        assertEquals(provider, deserializedProvider.getProviderInstance());
        assertEquals(provider._rootNameLookup, deserializedProvider._rootNameLookup);
    }

    @Test
    public void testSerializeValueNull() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        provider.serializeValue(jsonGenerator, null);

        String xml = stringWriter.toString();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValueEmptyArray() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        JavaType javaType = TypeFactory.defaultInstance().constructArrayType(String.class);
        provider.serializeValue(jsonGenerator, new Object[0], javaType, null);

        String xml = stringWriter.toString();
        assertEquals("<arrayName/><item/>", xml);
    }

    @Test
    public void testSerializeValueObject() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        String value = "value";
        provider.serializeValue(jsonGenerator, value, String.class, null);

        String xml = stringWriter.toString();
        assertEquals("<rootElement><value>value</value></rootElement>", xml);
    }

    @Test
    public void testSerializeValueObjectWithRootName() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSerializer(new RootNameSerializer(null, new PropertyName("rootName")));
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        String value = "value";
        provider.serializeValue(jsonGenerator, value, String.class, null);

        String xml = stringWriter.toString();
        assertEquals("<rootName><value>value</value></rootName>", xml);
    }

    @Test
    public void testSerializeValueObjectWithCustomSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        String value = "value";
        JsonSerializer<String> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serialize(value, jsonGenerator, provider)).thenReturn(jsonGenerator);

        provider.setSerializerInstance(String.class, jsonSerializer);
        provider.serializeValue(jsonGenerator, value, String.class, null);

        String xml = stringWriter.toString();
        assertEquals("<rootElement>" + value + "</rootElement>", xml);
    }

    @Test
    public void testSerializeValueObjectWithRootType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        JavaType rootType = TypeFactory.defaultInstance().constructType(Root.class);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        Root root = new Root();
        provider.serializeValue(jsonGenerator, root, rootType, null);

        String xml = stringWriter.toString();
        assertEquals("<root><name>rootName</name></root>", xml);
    }

    @Test
    public void testSerializeValueObjectWithRootTypeAndCustomSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        JavaType rootType = TypeFactory.defaultInstance().constructType(Root.class);

        JsonSerializer<Root> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serialize(any(Root.class), eq(jsonGenerator), eq(provider))).thenReturn(jsonGenerator);

        provider.setSerializerInstance(Root.class, jsonSerializer);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        Root root = new Root();
        provider.serializeValue(jsonGenerator, root, rootType, null);

        String xml = stringWriter.toString();
        assertEquals("<root><name>rootName</name></root>", xml);
    }

    @Test
    public void testSerializeValueObjectWithRootNameAndCustomSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSerializer(new RootNameSerializer(null, new PropertyName("rootName")));
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames, config, null);

        JavaType rootType = TypeFactory.defaultInstance().constructType(Root.class);

        JsonSerializer<Root> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serialize(any(Root.class), eq(jsonGenerator), eq(provider))).thenReturn(jsonGenerator);

        provider.setSerializerInstance(Root.class, jsonSerializer);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = provider.getFactory().createGenerator(stringWriter);

        Root root = new Root();
        provider.serializeValue(jsonGenerator, root, rootType, null);

        String xml = stringWriter.toString();
        assertEquals("<rootName><name>rootName</name></rootName>", xml);
    }

    private static class Root {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}