
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException, ParserConfigurationException {
        // Arrange
        InputSource inputSource = mock(InputSource.class);
        AbstractLoader abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);
        XMLReader parser = mock(XMLReader.class);

        doCallRealMethod().when(abstractLoader).parseInputSource(inputSource);
        doReturn(parser).when(abstractLoader).getXMLReader();

        // Act
        abstractLoader.parseInputSource(inputSource);

        // Assert
        verify(parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntity_withValidPublicId() throws IOException, SAXException {
        // Arrange
        String publicId = "validPublicId";
        String resourceName = "validResourceName";
        ClassLoader loader = mock(ClassLoader.class);
        InputStream dtdIs = mock(InputStream.class);

        AbstractLoader abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, resourceName);

        doReturn(publicIdToResourceNameMap).when(abstractLoader).getPublicIdToResourceNameMap();
        doReturn(loader).when(abstractLoader).getClassLoader();
        doReturn(dtdIs).when(loader).getResourceAsStream(resourceName);

        // Act
        InputSource inputSource = abstractLoader.resolveEntity(publicId, null);

        // Assert
        assertEquals(dtdIs, inputSource.getByteStream());
    }

    @Test
    public void testResolveEntity_withInvalidPublicId() throws IOException, SAXException {
        // Arrange
        String publicId = "invalidPublicId";
        String resourceName = "validResourceName";
        String systemId = "validSystemId";

        AbstractLoader abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(resourceName, "validResourceName");

        doReturn(publicIdToResourceNameMap).when(abstractLoader).getPublicIdToResourceNameMap();

        // Act
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);

        // Assert
        assertEquals(super.resolveEntity(publicId, systemId), inputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        // Arrange
        AbstractLoader abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);
        SAXParseException exception = mock(SAXParseException.class);

        doCallRealMethod().when(abstractLoader).error(exception);

        // Act & Assert
        abstractLoader.error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        // Arrange
        AbstractLoader abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);
        SAXParseException exception = mock(SAXParseException.class);

        doCallRealMethod().when(abstractLoader).fatalError(exception);

        // Act & Assert
        abstractLoader.fatalError(exception);
    }
}

