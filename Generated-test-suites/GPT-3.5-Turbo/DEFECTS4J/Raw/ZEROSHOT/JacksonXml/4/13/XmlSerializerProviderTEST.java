
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.IOException;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        xmlSerializerProvider = new XmlSerializerProvider(null);
    }

    @Test
    public void createInstanceTest() {
        XmlSerializerProvider provider = (XmlSerializerProvider) xmlSerializerProvider.createInstance(null, null);
        Assert.assertNotNull(provider);
    }

    @Test
    public void serializeXmlNullTest() throws IOException {
        // Arrange

        // Act
        xmlSerializerProvider._serializeXmlNull(null);

        // Assert

    }

    @Test
    public void startRootArrayTest() throws IOException {
        // Arrange
        ToXmlGenerator xgen = new ToXmlGenerator(null, 0, null, null);
        QName rootName = new QName("test");

        // Act
        xmlSerializerProvider._startRootArray(xgen, rootName);

        // Assert

    }

    @Test
    public void initWithRootNameTest() throws IOException {
        // Arrange
        ToXmlGenerator xgen = new ToXmlGenerator(null, 0, null, null);
        QName rootName = new QName("test");

        // Act
        xmlSerializerProvider._initWithRootName(xgen, rootName);

        // Assert

    }

    @Test
    public void rootNameFromConfigTest() {
        // Arrange
        PropertyName propertyName = new PropertyName("test");
        xmlSerializerProvider._config = new SerializationConfig(null, null, null, null, null, propertyName, null, null, null, null, null);

        // Act
        QName result = xmlSerializerProvider._rootNameFromConfig();

        // Assert
        Assert.assertEquals("test", result.getLocalPart());
    }

    @Test
    public void asXmlGeneratorNoGeneratorTest() throws JsonMappingException {
        // Arrange
        TokenBuffer tokenBuffer = new TokenBuffer(null, false);
        boolean exceptionThrown = false;

        // Act
        try {
            xmlSerializerProvider._asXmlGenerator(tokenBuffer);
        } catch (JsonMappingException e) {
            exceptionThrown = true;

            // Assert
            Assert.assertEquals("XmlMapper does not with generators of type other than ToXmlGenerator; got: com.fasterxml.jackson.databind.util.TokenBuffer", e.getMessage());
        }

        Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void asXmlGeneratorWithToXmlGeneratorTest() throws JsonMappingException {
        // Arrange
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(null, 0, null, null);
        boolean exceptionThrown = false;

        // Act
        try {
            xmlSerializerProvider._asXmlGenerator(toXmlGenerator);
        } catch (JsonMappingException e) {
            exceptionThrown = true;

            // Assert
            Assert.fail("Unexpected JsonMappingException");
        }

        Assert.assertFalse(exceptionThrown);
    }
}
