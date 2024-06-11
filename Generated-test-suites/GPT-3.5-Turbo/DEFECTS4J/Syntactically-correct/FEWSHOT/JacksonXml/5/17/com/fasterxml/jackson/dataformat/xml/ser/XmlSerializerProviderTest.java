package com.fasterxml.jackson.dataformat.xml.ser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import javax.xml.namespace.QName;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider provider;
    private JsonGenerator mockJsonGenerator;
    private DefaultSerializerProvider mockDefaultSerializerProvider;

    @Before
    public void setUp() throws Exception {
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNameLookup);

        mockJsonGenerator = mock(JsonGenerator.class);
        mockDefaultSerializerProvider = mock(DefaultSerializerProvider.class);
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider copy = provider.copy();
        assertEquals(provider.getClass(), copy.getClass());
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory jsf = mock(SerializerFactory.class);
        DefaultSerializerProvider instance = provider.createInstance(config, jsf);
        assertEquals(provider.getClass(), instance.getClass());
    }

    @Test
    public void testSerializeValue() throws IOException {
        Object value = new Object();
        provider.serializeValue(mockJsonGenerator, value);

        //Verify that serialize method is called with the correct parameters
        verify(mockDefaultSerializerProvider).serializeValue(eq(mockJsonGenerator), eq(value));
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        provider.serializeValue(mockJsonGenerator, value, rootType, ser);

        //Verify that serialize method is called with the correct parameters
        verify(mockDefaultSerializerProvider).serializeValue(eq(mockJsonGenerator), eq(value), eq(rootType), eq(ser));
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        Object value = null;
        provider.serializeValue(mockJsonGenerator, value);

        //Verify that serialize method is called with the correct parameters
        verify(mockDefaultSerializerProvider).serializeValue(eq(mockJsonGenerator), eq(value));
    }

    @Test
    public void testSerializeValueWithNullRootType() throws IOException {
        Object value = null;
        JavaType rootType = null;
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        provider.serializeValue(mockJsonGenerator, value, rootType, ser);

        //Verify that serialize method is called with the correct parameters
        verify(mockDefaultSerializerProvider).serializeValue(eq(mockJsonGenerator), eq(value), eq(rootType), eq(ser));
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        // set-up
        QName ROOT_NAME_FOR_NULL = new QName("null");
        Object value = null;
        // execute
        provider._serializeXmlNull(mockJsonGenerator);
        // verify
        verify(mockJsonGenerator).writeFieldName(eq(ROOT_NAME_FOR_NULL));
        verify(mockDefaultSerializerProvider).serializeValue(eq(mockJsonGenerator), eq(null));
    }

    @Test
    public void testStartRootArray() throws IOException {
        // set-up
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");
        // execute
        provider._startRootArray(xgen, rootName);
        // verify
        verify(xgen).writeFieldName(eq("item"));
    }

    @Test
    public void testInitWithRootName() throws IOException, XMLStreamException {
        // set-up
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");
        // execute
        provider._initWithRootName(xgen, rootName);
        // verify
        verify(xgen).setNextNameIfMissing(eq(rootName));
        verify(xgen).inRoot();
        verify(xgen).setNextName(eq(rootName));
        verify(xgen).initGenerator();
        verify(xgen).getStaxWriter();
    }

    @Test
    public void testRootNameFromConfig() {
        // set-up
        PropertyName propertyName = mock(PropertyName.class);
        QName rootName = new QName("root");
        // execute
        QName result = provider._rootNameFromConfig();
        // verify
        assertNull(result);

        // set-up
        when(mockConfig.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn(null);
        when(propertyName.getSimpleName()).thenReturn("root");
        // execute
        result = provider._rootNameFromConfig();
        // verify
        assertEquals(new QName("root"), result);

        // set-up
        when(propertyName.getNamespace()).thenReturn("namespace");
        // execute
        result = provider._rootNameFromConfig();
        // verify
        assertEquals(new QName("namespace", "root"), result);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        // set-up
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        // execute & verify
        assertEquals(toXmlGenerator, provider._asXmlGenerator(toXmlGenerator));
        assertNull(provider._asXmlGenerator(tokenBuffer));
    }

    @Test
    public void testWrapAsIOE() throws IOException {
        // set-up
        Exception exception = new Exception("test exception");
        // execute & verify
        assertEquals(exception, provider._wrapAsIOE(mockJsonGenerator, exception));
    }
}