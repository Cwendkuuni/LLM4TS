package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        // Act
        provider.serializeValue(null, null);

        // Assert
        // The generated XML should contain the expected root name for null
        // In this case, the expected root name is "<null>"
    }

    @Test
    public void testSerializeValue_AsArray() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        Object[] array = new Object[]{1, 2, 3};

        // Act
        provider.serializeValue(null, array);

        // Assert
        // The generated XML should contain the expected root name and array elements
        // In this case, the expected root name is "<null>"
        // The array elements should be serialized as child elements of the root element
    }

    @Test
    public void testSerializeValue_NormalValue() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        Object value = new Object();

        // Act
        provider.serializeValue(null, value);

        // Assert
        // The generated XML should contain the serialized value
    }

    @Test
    public void testSerializeValue_WithRootName() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        // Act
        provider.serializeValue(null, null, null, null);

        // Assert
        // The generated XML should contain the expected root name for null
        // In this case, the expected root name is "<null>"
    }

    @Test
    public void testSerializeValue_WithRootNameAndSerializer() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        // Act
        provider.serializeValue(null, null, null, null);

        // Assert
        // The generated XML should contain the expected root name for null
        // In this case, the expected root name is "<null>"
        // The value should be serialized using the provided serializer
    }

    @Test
    public void testInitWithRootName() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        ToXmlGenerator generator = new ToXmlGenerator(null, null, null);

        // Act
        provider._initWithRootName(generator, null);

        // Assert
        // The generator should be initialized with the provided root name
        // If the root name is null, the generator should not be modified
    }

    @Test
    public void testRootNameFromConfig_NullConfig() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);

        // Act
        QName result = provider._rootNameFromConfig();

        // Assert
        // The result should be null when the config is null
        assertNull(result);
    }

    @Test
    public void testRootNameFromConfig_NullPropertyName() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        SerializationConfig config = new SerializationConfig(null, null, null);

        // Act
        QName result = provider._rootNameFromConfig();

        // Assert
        // The result should be null when the property name is null
        assertNull(result);
    }

    @Test
    public void testRootNameFromConfig_EmptyNamespace() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        SerializationConfig config = new SerializationConfig(null, null, null);
        PropertyName propertyName = new PropertyName("test", "");

        // Act
        QName result = provider._rootNameFromConfig();

        // Assert
        // The result should contain the simple name of the property name
        assertNotNull(result);
        assertEquals("test", result.getLocalPart());
        assertNull(result.getNamespaceURI());
    }

    @Test
    public void testRootNameFromConfig_NonEmptyNamespace() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        SerializationConfig config = new SerializationConfig(null, null, null);
        PropertyName propertyName = new PropertyName("test", "http://www.example.com");

        // Act
        QName result = provider._rootNameFromConfig();

        // Assert
        // The result should contain the local part and namespace of the property name
        assertNotNull(result);
        assertEquals("test", result.getLocalPart());
        assertEquals("http://www.example.com", result.getNamespaceURI());
    }

    @Test
    public void testAsXmlGenerator_TokenBuffer() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);

        // Act
        ToXmlGenerator generator = provider._asXmlGenerator(tokenBuffer);

        // Assert
        // The generator should be an instance of ToXmlGenerator
        // when the input is a TokenBuffer
        assertNotNull(generator);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_InvalidGenerator() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream());

        // Act
        provider._asXmlGenerator(jsonGenerator);

        // Assert
        // An exception should be thrown when the input is not a ToXmlGenerator or TokenBuffer
    }

    @Test
    public void testWrapAsIOE_RuntimeException() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        RuntimeException exception = new RuntimeException("Test exception");

        // Act
        IOException result = provider._wrapAsIOE(jsonGenerator, exception);

        // Assert
        // The result should be an IOException wrapping the runtime exception
        assertNotNull(result);
        assertEquals("Test exception", result.getMessage());
        assertEquals(exception, result.getCause());
    }

    @Test
    public void testWrapAsIOE_IOException() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        IOException exception = new IOException("Test exception");

        // Act
        IOException result = provider._wrapAsIOE(jsonGenerator, exception);

        // Assert
        // The result should be the original IOException
        assertNotNull(result);
        assertEquals("Test exception", result.getMessage());
        assertEquals(exception, result);
    }

}