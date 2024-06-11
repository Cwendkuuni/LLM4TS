package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider() {
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(provider);
        XmlSerializerProvider providerConfig = new XmlSerializerProvider(provider, config, factory);
        assertNotNull(providerConfig);
        XmlSerializerProvider providerCopy = new XmlSerializerProvider(provider);
        assertNotNull(providerCopy);
    }

    @Test
    public void testSerializeValueNull() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        provider.serializeValue(gen, null);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithRootName() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        when(rootType.getRawClass()).thenReturn(String.class);
        when(rootType.getSimpleName()).thenReturn("rootType");
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.getSerializable()).thenReturn(value);
        provider.serializeValue(gen, value, rootType, serializer);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        Object value = new Object();
        PropertyName rootName = new PropertyName("rootName");
        SerializationConfig config = mock(SerializationConfig.class);
        when(config.getFullRootName()).thenReturn(rootName);
        SerializerFactory factory = mock(SerializerFactory.class);
        provider = new XmlSerializerProvider(provider, config, factory);
        provider.serializeValue(gen, value);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithRootNameFromXmlRootNameLookup() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        Object value = new Object();
        final Class<?> valueClass = value.getClass();
        when(mock(XmlRootNameLookup.class).findRootName(valueClass, mock(SerializationConfig.class))).thenReturn(new QName("root"));
        provider.serializeValue(gen, value);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithArray() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        Object[] values = new Object[]{"value1", "value2"};
        JavaType rootType = mock(JavaType.class);
        when(rootType.getRawClass()).thenReturn(Array.class);
        when(rootType.getContentType()).thenReturn(JavaType.constructArrayType(String.class));
        when(rootType.getSimpleName()).thenReturn("rootType");
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.getSerializable()).thenReturn(values[0]);
        provider.serializeValue(gen, values, rootType, serializer);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithArrayWithRootName() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        Object[] values = new Object[]{"value1", "value2"};
        JavaType rootType = mock(JavaType.class);
        when(rootType.getRawClass()).thenReturn(Array.class);
        when(rootType.getContentType()).thenReturn(JavaType.constructArrayType(String.class));
        when(rootType.getSimpleName()).thenReturn("rootType");
        when(mock(XmlRootNameLookup.class).findRootName(JavaType.constructArrayType(String.class), mock(SerializationConfig.class))).thenReturn(new QName("root"));
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.getSerializable()).thenReturn(values[0]);
        provider.serializeValue(gen, values, rootType, serializer);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeValueWithArrayWithRootNameFromConfig() throws JsonProcessingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        Object[] values = new Object[]{"value1", "value2"};
        PropertyName rootName = new PropertyName("rootName");
        SerializationConfig config = mock(SerializationConfig.class);
        when(config.getFullRootName()).thenReturn(rootName);
        SerializerFactory factory = mock(SerializerFactory.class);
        provider = new XmlSerializerProvider(provider, config, factory);
        JavaType rootType = mock(JavaType.class);
        when(rootType.getRawClass()).thenReturn(Array.class);
        when(rootType.getContentType()).thenReturn(JavaType.constructArrayType(String.class));
        when(rootType.getSimpleName()).thenReturn("rootType");
        provider = new XmlSerializerProvider(provider, rootType, null);
        provider.serializeValue(gen, values);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen instanceof ToXmlGenerator).thenReturn(true);
        assertNotNull(provider._asXmlGenerator(gen));
        when(gen instanceof TokenBuffer).thenReturn(true);
        assertNull(provider._asXmlGenerator(gen));
    }

    @Test
    public void testInitializeWithRootName() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        ToXmlGenerator xgen = spy(mock(ToXmlGenerator.class));
        when(xgen.setNextNameIfMissing(new QName("root"))).thenReturn(false);
        provider._initWithRootName(xgen, new QName("root"));
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(xgen));
    }

    @Test(expected = JsonMappingException.class)
    public void testInitializeWithRootNameWhenEnabledNotSet() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider._initWithRootName(xgen, new QName("root"));
    }

    @Test
    public void testInitializeWithRootNameWhenInRoot() throws IOException, XMLStreamException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        provider._initWithRootName(xgen, new QName("root"));
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(xgen));
    }

    @Test
    public void testRootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        PropertyName rootName = new PropertyName("rootName");
        SerializationConfig config = mock(SerializationConfig.class);
        when(config.getFullRootName()).thenReturn(rootName);
        QName result = provider._rootNameFromConfig();
        assertEquals(new QName("rootName"), result);
    }

    @Test
    public void testRootNameFromConfigEmpty() {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        SerializationConfig config = mock(SerializationConfig.class);
        QName result = provider._rootNameFromConfig();
        assertNull(result);
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        provider._serializeXmlNull(gen);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testSerializeXmlNullWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.isEnabled()).thenReturn(true);
        when(gen.getRootElementName()).thenReturn("root");
        provider._serializeXmlNull(gen);
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(gen));
    }

    @Test
    public void testStartRootArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        provider._startRootArray(xgen, new QName("root"));
        // Uncomment the following line to see the generated XML
        // System.out.println(StaxUtil.toString(xgen));
    }
}