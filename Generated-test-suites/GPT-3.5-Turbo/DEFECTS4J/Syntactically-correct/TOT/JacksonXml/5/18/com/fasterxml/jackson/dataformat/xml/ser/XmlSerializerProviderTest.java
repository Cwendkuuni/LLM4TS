
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Test
    public void testSerializeValue_NullObject() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;

        // Act
        xmlSerializerProvider.serializeValue(gen, value);

        // Assert
        verify(gen).writeFieldName("null");
    }

    @Test
    public void testSerializeValue_NonNullObject() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();

        // Act
        xmlSerializerProvider.serializeValue(gen, value);

        // Assert
        // Assuming value.getClass() returns Object, verify expected method calls and arguments
        verify(gen).writeFieldName(any(String.class));
    }

    @Test
    public void testSerializeValue_AsArray() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();

        // Act
        xmlSerializerProvider.serializeValue(gen, value);

        // Assert
        // Assuming value.getClass() returns Object, verify expected method calls and arguments
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueObject_WithType_NullObject() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        // Act
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);

        // Assert
        verify(gen).writeFieldName("null");
    }

    @Test
    public void testSerializeValueObject_WithType_NonNullObject() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        // Act
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);

        // Assert
        // Assuming value.getClass() returns Object, verify expected method calls and arguments
        verify(gen).writeFieldName(any(String.class));
    }

    @Test
    public void testSerializeValueObject_WithType_AsArray() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);

        // Act
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);

        // Assert
        // Assuming value.getClass() returns Object, verify expected method calls and arguments
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeXmlNull() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);

        // Act
        xmlSerializerProvider._serializeXmlNull(gen);

        // Assert
        verify(gen).writeFieldName("null");
    }

    @Test
    public void testStartRootArray() {
        // Arrange
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");

        // Act
        xmlSerializerProvider._startRootArray(xgen, rootName);

        // Assert
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName() {
        // Arrange
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");

        // Act
        xmlSerializerProvider._initWithRootName(xgen, rootName);

        // Assert
        verify(xgen).setNextNameIfMissing(rootName);
        verify(xgen).initGenerator();
        verify(xgen).getStaxWriter();
    }

    @Test
    public void testRootNameFromConfig_NullConfig() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(null);

        // Act
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void testRootNameFromConfig_SimpleRootName() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        // Act
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        QName expected = new QName("simple");
        assertEquals(expected, result);
    }

    @Test
    public void testRootNameFromConfig_NamespaceRootName() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(
            new XmlRootNameLookup().with(new QName("namespace", "complex"))
        );

        // Act
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        QName expected = new QName("namespace", "complex");
        assertEquals(expected, result);
    }

    @Test
    public void testAsXmlGenerator_Success() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        xmlSerializerProvider = spy(new XmlSerializerProvider(new XmlRootNameLookup()));

        // Act
        ToXmlGenerator result = xmlSerializerProvider._asXmlGenerator(xgen);

        // Assert
        assertEquals(xgen, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_Failure() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        xmlSerializerProvider = spy(new XmlSerializerProvider(new XmlRootNameLookup()));

        // Act
        xmlSerializerProvider._asXmlGenerator(gen);

        // Assert
        // JsonMappingException should be thrown
    }

    @Test
    public void testWrapAsIOE_IOException() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        IOException e = new IOException("test");

        // Act
        IOException result = xmlSerializerProvider._wrapAsIOE(gen, e);

        // Assert
        assertEquals(e, result);
    }

    @Test
    public void testWrapAsIOE_RuntimeException() {
        // Arrange
        JsonGenerator gen = mock(JsonGenerator.class);
        RuntimeException e = new RuntimeException("test");

        // Act
        IOException result = xmlSerializerProvider._wrapAsIOE(gen, e);

        // Assert
        assertEquals(e.getClass().getName(), result.getMessage());
    }

    @Test
    public void testCopy() {
        // Arrange
        XmlSerializerProvider src = mock(XmlSerializerProvider.class);

        // Act
        XmlSerializerProvider result = src.copy();

        // Assert
        assertEquals(src, result);
    }

    @Test
    public void testCreateInstance() {
        // Arrange
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory jsf = mock(SerializerFactory.class);
        xmlSerializerProvider = mock(XmlSerializerProvider.class);

        // Act
        XmlSerializerProvider result = xmlSerializerProvider.createInstance(config, jsf);

        // Assert
        assertEquals(xmlSerializerProvider, result);
    }
}
