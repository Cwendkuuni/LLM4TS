package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import javax.xml.namespace.QName;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private DefaultSerializerProvider defaultSerializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private JavaType javaType;

    @Before
    public void setUp() {
        rootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        defaultSerializerProvider = new DefaultSerializerProvider();
        javaType = TypeFactory.defaultInstance().constructType(String.class);
    }

    @Test
    public void testSerializeValue_withNullValue() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        String expectedResult = "<null/>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeValue_withNonNullValue() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider.serializeValue(jsonGenerator, "Example Value");
        String expectedResult = "<String>Example Value</String>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeValue_withNonNullValueAndJavaType() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider.serializeValue(jsonGenerator, "Example Value", javaType, null);
        String expectedResult = "<String>Example Value</String>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeValue_withNonNullValueAndJsonSerializer() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider.serializeValue(jsonGenerator, "Example Value", null, null);
        String expectedResult = "<String>Example Value</String>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copy = xmlSerializerProvider.copy();
        assertNotNull(copy);
        assertEquals(xmlSerializerProvider.getClass(), copy.getClass());
        assertEquals(xmlSerializerProvider.hashCode(), copy.hashCode());
    }

    @Test
    public void testCreateInstance() {
        SerializerFactory serializerFactory = new SerializerFactory();
        DefaultSerializerProvider instance =
                xmlSerializerProvider.createInstance(new ObjectMapper().getSerializationConfig(),
                        serializerFactory);
        assertNotNull(instance);
        assertEquals(DefaultSerializerProvider.class, instance.getClass());
    }

    @Test
    public void testInitWithRootName_withRootNameLookup() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider._initWithRootName((ToXmlGenerator) jsonGenerator, new QName("test"));
        QName expectedResult = new QName("test");
        QName actualResult = ((ToXmlGenerator) jsonGenerator).getRootName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInitWithRootName_withoutRootNameLookup() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider._initWithRootName((ToXmlGenerator) jsonGenerator, null);
        QName actualResult = ((ToXmlGenerator) jsonGenerator).getRootName();
        assertNull(actualResult);
    }

    @Test
    public void testStartRootArray() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        ToXmlGenerator jsonGenerator = (ToXmlGenerator) tokenBuffer.serializer();
        xmlSerializerProvider._startRootArray(jsonGenerator, new QName("test"));
        jsonGenerator.writeEndObject();
        String expectedResult = "<item/>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeXmlNull_withRootNameFromConfig() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        xmlSerializerProvider._serializeXmlNull(jsonGenerator);
        String expectedResult = "<null/>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSerializeXmlNull_withoutRootNameFromConfig() throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        JsonGenerator jsonGenerator = tokenBuffer.serializer();
        QName nullQName = new QName("null");
        XmlSerializerProvider xmlSerializerProvider =
                new XmlSerializerProvider(rootNameLookup) {
                    @Override
                    protected QName _rootNameFromConfig() {
                        return nullQName;
                    }
                };
        xmlSerializerProvider._serializeXmlNull(jsonGenerator);
        String expectedResult = "<null/>";
        String actualResult = tokenBuffer.asParser().nextToken().asXml();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWrapAsIOE_withIOException() throws IOException {
        IOException ioException = new IOException("Test IO Exception");
        IOException wrappedException =
                xmlSerializerProvider._wrapAsIOE(defaultSerializerProvider, ioException);
        assertEquals(ioException, wrappedException);
    }

    @Test
    public void testWrapAsIOE_withOtherException() throws IOException {
        Exception exception = new Exception("Test Exception");
        IOException wrappedException =
                xmlSerializerProvider._wrapAsIOE(defaultSerializerProvider, exception);
        assertEquals(exception.toString(), wrappedException.getMessage());
        assertEquals(JsonProcessingException.class, wrappedException.getClass());
    }

}