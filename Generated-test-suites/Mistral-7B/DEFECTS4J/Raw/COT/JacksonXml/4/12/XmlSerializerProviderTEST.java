package com.fasterxml.jackson.dataformat.xml.ser; // package should be the same as the class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PropertyName;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FinderCache;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import com.fasterxml.jackson.databind.ser.impl.ValueSerializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookupTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test cases for XmlSerializerProvider class.
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    @Mock
    private XmlRootNameLookup _rootNameLookup;

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    private SerializationConfig config;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        config = new SerializationConfig();
        config.setRootValueSerializer(new SimpleValueSerializer(null));
        config.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        objectMapper = new ObjectMapper(config);
        when(objectMapper.getSerializerProvider()).thenReturn(xmlSerializerProvider);
    }

    // Test case for constructor
    @Test
    public void testXmlSerializerProviderConstructor() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        assertNull(provider._rootNameLookup);
        XmlSerializerProvider providerWithRootNameLookup = new XmlSerializerProvider(provider, config, null);
        assertEquals(provider._rootNameLookup, providerWithRootNameLookup._rootNameLookup);
    }

    // Test case for createInstance method
    @Test
    public void testCreateInstanceMethod() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        XmlSerializerProvider newProvider = provider.createInstance(config, null);
        assertEquals(provider.getClass(), newProvider.getClass());
        assertEquals(provider._rootNameLookup, newProvider._rootNameLookup);
    }

    // Test case for serializeValue method with Object value and no rootType
    @Test
    public void testSerializeValueWithObjectAndNoRootType() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        provider.serializeValue(gen, "Hello World");
        verifyXmlSerializerCalled(gen, "Hello World", null);
    }

    // Test case for serializeValue method with Object value and rootType
    @Test
    public void testSerializeValueWithObjectAndRootType() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        PropertyName rootName = new PropertyName("root");
        SerializationConfig configWithRootName = new SerializationConfig();
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, null, null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, new QName("root"), null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        ObjectMapper objectMapperWithRootName = new ObjectMapper(configWithRootName);

        XmlSerializerProvider providerWithRootName = new XmlSerializerProvider(new XmlRootNameLookupTest(), objectMapperWithRootName);
        providerWithRootName.serializeValue(gen, new Object(), new JavaType(Object.class));
        verifyXmlSerializerCalled(gen, null, new JavaType(Object.class));
        verifyXmlSerializerCalled(gen, "root", new JavaType(Object.class));
    }

    // Test case for serializeValue method with Object value and rootType and serializer
    @Test
    public void testSerializeValueWithObjectAndRootTypeAndSerializer() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        ValueSerializerBase serializer = mock(ValueSerializerBase.class);
        when(serializer.serializeValues(anyObject(), eq(gen), any(SerializerProvider.class)))
                .thenReturn(new byte[0][0]);

        PropertyName rootName = new PropertyName("root");
        SerializationConfig configWithRootName = new SerializationConfig();
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, null, null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, new QName("root"), null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setSerializerFactory(new DefaultSerializerFactory(configWithRootName, null, null));
        ObjectMapper objectMapperWithRootName = new ObjectMapper(configWithRootName);

        XmlSerializerProvider providerWithRootName = new XmlSerializerProvider(new XmlRootNameLookupTest(), objectMapperWithRootName);
        when(providerWithRootName.findValueSerializer(eq(Object.class), anyBoolean(), any(TypeSerializer.class)))
                .thenReturn(serializer);

        providerWithRootName.serializeValue(gen, new Object(), new JavaType(Object.class), serializer);
        verifyXmlSerializerCalled(gen, null, new JavaType(Object.class));
        verifyXmlSerializerCalled(gen, "root", new JavaType(Object.class));
    }

    // Test case for serializeValue method with null value
    @Test
    public void testSerializeValueWithNullValue() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        provider.serializeValue(gen, null);
        verifyXmlSerializerCalled(gen, "null", null);
    }

    // Test case for serializeValue method with JavaType value and no rootType
    @Test
    public void testSerializeValueWithJavaTypeValueAndNoRootType() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        provider.serializeValue(gen, new JavaType(List.class), new Object[0]);
        verifyXmlSerializerCalled(gen, null, new JavaType(List.class));
    }

    // Test case for serializeValue method with JavaType value and rootType
    @Test
    public void testSerializeValueWithJavaTypeValueAndRootType() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        PropertyName rootName = new PropertyName("root");
        SerializationConfig configWithRootName = new SerializationConfig();
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, null, null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, new QName("root"), null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        ObjectMapper objectMapperWithRootName = new ObjectMapper(configWithRootName);

        XmlSerializerProvider providerWithRootName = new XmlSerializerProvider(new XmlRootNameLookupTest(), objectMapperWithRootName);
        providerWithRootName.serializeValue(gen, new JavaType(List.class), Arrays.asList("one", "two"), null);
        verifyXmlSerializerCalled(gen, "root", new JavaType(List.class));
    }

    // Test case for serializeValue method with JavaType value and rootType and serializer
    @Test
    public void testSerializeValueWithJavaTypeValueAndRootTypeAndSerializer() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        ValueSerializerBase serializer = mock(ValueSerializerBase.class);
        when(serializer.serializeValues(anyObject(), eq(gen), any(SerializerProvider.class)))
                .thenReturn(new byte[0][0]);

        PropertyName rootName = new PropertyName("root");
        SerializationConfig configWithRootName = new SerializationConfig();
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, null, null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setRootValueSerializer(new SimpleBeanPropertySerializer(configWithRootName, null, new QName("root"), null, null));
        configWithRootName.setRootValueSerializer(new SimpleValueSerializer(null));
        configWithRootName.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        configWithRootName.setSerializerFactory(new DefaultSerializerFactory(configWithRootName, null, null));
        ObjectMapper objectMapperWithRootName = new ObjectMapper(configWithRootName);

        XmlSerializerProvider providerWithRootName = new XmlSerializerProvider(new XmlRootNameLookupTest(), objectMapperWithRootName);
        when(providerWithRootName.findValueSerializer(eq(List.class), anyBoolean(), any(TypeSerializer.class)))
                .thenReturn(serializer);

        providerWithRootName.serializeValue(gen, new JavaType(List.class), Arrays.asList("one", "two"), serializer);
        verifyXmlSerializerCalled(gen, "root", new JavaType(List.class));
    }

    // Test case for _asXmlGenerator method
    @Test
    public void testAsXmlGeneratorMethod() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        ToXmlGenerator toXmlGenerator = provider._asXmlGenerator(gen);
        assertNull(toXmlGenerator);
    }

    // Test case for _initWithRootName method
    @Test
    public void testInitWithRootNameMethod() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.setNextNameIfMissing(anyString())).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        QName rootName = new QName("root");
        provider._initWithRootName(gen, rootName);

        verify(gen).setNextName(rootName);
        verify(gen).initGenerator();
        verify(gen).writeStartObject();
    }

    // Test case for _rootNameFromConfig method
    @Test
    public void testRootNameFromConfigMethod() {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSerializer(new SimpleValueSerializer(null));
        config.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);
        config.setRootValueSerializer(new SimpleBeanPropertySerializer(config, null, new QName("root"), null, null));
        config.setRootValueSerializer(new SimpleValueSerializer(null));
        config.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);

        PropertyName rootName = new PropertyName("root");
        config.setRootValueSerializer(new SimpleBeanPropertySerializer(config, null, rootName, null, null));
        config.setRootValueSerializer(new SimpleValueSerializer(null));
        config.setRootValueFactory(DefaultSerializerProvider.NullValueSerializer.instance);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest(), null);

        assertEquals(new QName("root"), provider._rootNameFromConfig());
        assertEquals(new QName("root"), provider._rootNameFromConfig());
        assertEquals(new QName(""), provider._rootNameFromConfig());
    }

    // Test case for _serializeXmlNull method
    @Test
    public void testSerializeXmlNullMethod() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        provider._serializeXmlNull(gen);
        verifyXmlSerializerCalled(gen, "null", null);
    }

    // Test case for _startRootArray method
    @Test
    public void testStartRootArrayMethod() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookupTest());
        provider._startRootArray(gen, new QName("root"));

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
    }

    private void verifyXmlSerializerCalled(JsonGenerator gen, String rootName, JavaType javaType) throws JsonProcessingException, IOException {
        JsonSerializer<?> serializer = findJsonSerializer(xmlSerializerProvider, javaType);
        serializer.serialize(new Object(), gen, xmlSerializerProvider);

        if (rootName != null) {
            verify(gen).writeStartObject();
            verify(gen).writeFieldName(rootName.getLocalPart());
        }
    }

    private JsonSerializer<?> findJsonSerializer(SerializerProvider provider, JavaType javaType) {
        FinderCache cache = provider.getSerializerProvider().getSerializerFinder();
        return cache.findValueSerializer(javaType, true, null);
    }

}