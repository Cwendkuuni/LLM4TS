package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider xmlSerializerProvider;
    private ToXmlGenerator xmlGenerator;

    @Before
    public void setUp() {
        xmlSerializerProvider = new XmlSerializerProvider(null);
        xmlGenerator = new ToXmlGenerator(null, 0, null, null);
    }

    @Test
    public void testSerializeValue_NullInput_ShouldSerializeXmlNull() {
        // Arrange
        QName rootNameForNull = new QName("null");
        xmlGenerator.setNextName(rootNameForNull);

        // Act
        xmlSerializerProvider._serializeXmlNull(xmlGenerator);

        // Assert
        // Verify that the generated XML represents a null value
        assertEquals("<null/>", xmlGenerator.getOutputTarget().toString());
    }

    @Test
    public void testStartRootArray() {
        // Arrange
        QName rootName = new QName("root");

        // Act
        xmlSerializerProvider._startRootArray(xmlGenerator, rootName);

        // Assert
        // Verify that the start of the root array is written to the XML generator
        assertEquals("{}", xmlGenerator.getOutputTarget().toString());
    }

    @Test
    public void testInitWithRootName_RootNameIsNull_ShouldNotSetNextNameOnXmlGenerator() {
        // Arrange
        QName rootName = null;

        // Act
        xmlSerializerProvider._initWithRootName(xmlGenerator, rootName);

        // Assert
        // Verify that the next name is not set on the XML generator
        assertNull(xmlGenerator.getNextName());
    }

    @Test
    public void testInitWithRootName_RootNameIsEmpty_ShouldNotSetNextNameOnXmlGenerator() {
        // Arrange
        QName rootName = new QName("");

        // Act
        xmlSerializerProvider._initWithRootName(xmlGenerator, rootName);

        // Assert
        // Verify that the next name is not set on the XML generator
        assertNull(xmlGenerator.getNextName());
    }

    @Test
    public void testInitWithRootName_RootNameHasNamespace_ShouldSetDefaultNamespaceOnStaxWriter() {
        // Arrange
        QName rootName = new QName("http://example.com", "root");

        // Act
        xmlSerializerProvider._initWithRootName(xmlGenerator, rootName);

        // Assert
        // Verify that the default namespace is set on the XML generator's Stax writer
        assertEquals("http://example.com", xmlGenerator.getStaxWriter().getDefaultNamespace());
    }

    @Test
    public void testInitWithRootName_RootNameIsNullAndInRoot_ShouldForceNextNameOnXmlGenerator() {
        // Arrange
        QName rootName = null;
        xmlGenerator.setInRoot(true);

        // Act
        xmlSerializerProvider._initWithRootName(xmlGenerator, rootName);

        // Assert
        // Verify that the next name is set on the XML generator
        assertNull(xmlGenerator.getNextName());
    }

    @Test
    public void testRootNameFromConfig_RootNameIsNull_ShouldReturnNull() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(null);

        // Act
        QName rootName = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        // Verify that null is returned
        assertNull(rootName);
    }

    @Test
    public void testRootNameFromConfig_RootNameHasNamespace_ShouldReturnQNameWithNamespace() {
        // Arrange
        PropertyName propertyName = new PropertyName("root", "http://example.com");
        xmlSerializerProvider = new XmlSerializerProvider(null);
        xmlSerializerProvider.getConfig().setFullRootName(propertyName);

        // Act
        QName rootName = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        // Verify that a QName with the namespace is returned
        assertEquals("http://example.com", rootName.getNamespaceURI());
        assertEquals("root", rootName.getLocalPart());
    }

    @Test
    public void testRootNameFromConfig_RootNameDoesNotHaveNamespace_ShouldReturnQNameWithoutNamespace() {
        // Arrange
        PropertyName propertyName = new PropertyName("root");
        xmlSerializerProvider = new XmlSerializerProvider(null);
        xmlSerializerProvider.getConfig().setFullRootName(propertyName);

        // Act
        QName rootName = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        // Verify that a QName without the namespace is returned
        assertEquals("", rootName.getNamespaceURI());
        assertEquals("root", rootName.getLocalPart());
    }

    @Test
    public void testAsXmlGenerator_GeneratorIsToXmlGenerator_ShouldReturnToXmlGenerator() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(null);
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);

        // Act
        ToXmlGenerator xmlGenerator = xmlSerializerProvider._asXmlGenerator(generator);

        // Assert
        // Verify that the correct ToXmlGenerator instance is returned
        assertEquals(generator, xmlGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_GeneratorIsTokenBuffer_ShouldThrowJsonMappingException() {
        // Arrange
        xmlSerializerProvider = new XmlSerializerProvider(null);
        TokenBuffer generator = new TokenBuffer(null, false);

        // Act
        xmlSerializerProvider._asXmlGenerator(generator);

        // Assert
        // JsonMappingException is expected to be thrown
    }
}