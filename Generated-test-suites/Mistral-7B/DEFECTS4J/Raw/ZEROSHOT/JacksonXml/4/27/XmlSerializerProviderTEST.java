package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class XmlSerializerProviderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private SerializerProvider provider;

    @Mock
    private JsonSerializer<Object> serializer;

    @Mock
    private JsonGenerator generator;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private XmlRootNameLookup rootNameLookup;

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @After
    public void tearDown() throws Exception {
        xmlSerializerProvider = null;
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // When
        xmlSerializerProvider.serializeValue(generator, null);

        // Then
        verify(generator).writeStartElement("null");
        verify(generator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndNoRootType() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String expectedRootElement = "root";

        // When
        xmlSerializerProvider.serializeValue(generator, new Object(), null);

        // Then
        verify(generator).writeStartElement(expectedRootElement);
        verify(generator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootType() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        SerializationConfig config = mock(SerializationConfig.class);
        JavaType rootType = mapper.getTypeFactory().constructValueType(String.class, new Class[0]);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String expectedRootElement = "root";

        when(config.getRootValueSerializer(eq(String.class)))
                .thenReturn(serializer);
        when(config.getRootValueSerializer(eq(rootType)))
                .thenReturn(serializer);
        when(serializer.getSerializableValue(anyObject()))
                .thenReturn("value");

        // When
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider.serializeValue(generator, new Object(), rootType);

        // Then
        verify(generator).writeStartElement(expectedRootElement);
        verify(generator).writeString("value");
        verify(generator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootTypeAndCustomSerializer() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        SerializationConfig config = mock(SerializationConfig.class);
        JavaType rootType = mapper.getTypeFactory().constructValueType(String.class, new Class[0]);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String expectedRootElement = "root";
        String expectedValue = "customValue";

        when(config.getRootValueSerializer(eq(String.class)))
                .thenReturn(null);
        when(config.getRootValueSerializer(eq(rootType)))
                .thenReturn(serializer);
        when(serializer.getSerializableValue(anyObject()))
                .thenReturn(expectedValue);

        // When
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider.serializeValue(generator, new Object(), rootType);

        // Then
        verify(generator).writeStartElement(expectedRootElement);
        verify(generator).writeString(expectedValue);
        verify(generator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootTypeAndArray() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        SerializationConfig config = mock(SerializationConfig.class);
        JavaType rootType = mapper.getTypeFactory().constructArrayType(String.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String expectedRootElement = "root";

        when(config.getRootValueSerializer(eq(String.class)))
                .thenReturn(null);
        when(config.getRootValueSerializer(eq(rootType)))
                .thenReturn(null);
        when(generator.canWriteValueAsArrayElement(anyBoolean()))
                .thenReturn(true);

        // When
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider.serializeValue(generator, new Object(), rootType);

        // Then
        verify(generator).writeStartElement(expectedRootElement);
        verify(generator).writeStartArray();
        verify(generator).writeEndArray();
        verify(generator).writeEndElement();
    }

    @Test
    public void testSerializeValueWithObjectAndRootTypeAndArrayWithCustomSerializer() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        SerializationConfig config = mock(SerializationConfig.class);
        JavaType rootType = mapper.getTypeFactory().constructArrayType(String.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String expectedRootElement = "root";
        String expectedValue = "customValue";

        when(config.getRootValueSerializer(eq(String.class)))
                .thenReturn(null);
        when(config.getRootValueSerializer(eq(rootType)))
                .thenReturn(serializer);
        when(serializer.getSerializableValue(anyObject()))
                .thenReturn(expectedValue);
        when(generator.canWriteValueAsArrayElement(anyBoolean()))
                .thenReturn(true);

        // When
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider = new XmlSerializerProvider(config, xmlSerializerProvider, null);
        xmlSerializerProvider.serializeValue(generator, new Object(), rootType);

        // Then
        verify(generator).writeStartElement(expectedRootElement);
        verify(generator).writeStartArray();
        verify(generator, times(expectedValue.length()))
                .writeString(expectedValue);
        verify(generator).writeEndArray();
        verify(generator).writeEndElement();
    }

    @Test
    public void testInitializeWithRootName() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // When
        xmlSerializerProvider.serializeValue(generator, new Object(), null);

        // Then
        verify(generator).setDefaultNamespace(StringPool.EMPTY);
    }

    @Test
    public void testInitializeWithRootNameAndNamespace() throws IOException, JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String namespace = "http://example.com";

        // When
        xmlSerializerProvider.serializeValue(generator, new Object(), null);

        // Then
        verify(generator).setDefaultNamespace(namespace);
    }

    @Test
    public void testStartRootArray() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getXmlFactory().createGenerator(System.out);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // When
        xmlSerializerProvider.serializeValue(generator, new Object(), null);

        // Then
        verify(generator).writeStartObject();
        verify(generator).writeFieldName("item");
    }

    @Test
    public void testFindRootName() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);

        // When
        QName result = xmlSerializerProvider.findRootName(new Class[0]);

        // Then
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, result);

        when(rootNameLookup.findRootName(eq(String.class), anyObject()))
                .thenReturn(new QName("root1"));
        when(rootNameLookup.findRootName(eq(int[].class), anyObject()))
                .thenReturn(new QName("root2"));

        // When
        result = xmlSerializerProvider.findRootName(new Class[0]);

        // Then
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, result);

        result = xmlSerializerProvider.findRootName(String.class, null);

        // Then
        assertEquals(new QName("root1"), result);

        result = xmlSerializerProvider.findRootName(int[].class, null);

        // Then
        assertEquals(new QName("root2"), result);
    }

    @Test
    public void testFindRootNameWithNullConfig() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);

        // When
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // Then
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlSerializerProvider.findRootName(null));
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlSerializerProvider.findRootName(null, null));
    }

    @Test
    public void testFindRootNameWithEmptyConfig() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        SerializationConfig config = mock(SerializationConfig.class);

        // When
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, config, null);

        // Then
        assertEquals(Collections.EMPTY_LIST, config.getRootNames());
    }

    @Test
    public void testCreateInstance() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // When
        XmlSerializerProvider result = xmlSerializerProvider.createInstance(config, null);

        // Then
        assertEquals(xmlSerializerProvider, result);
    }
}