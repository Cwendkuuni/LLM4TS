
package com.puppycrawl.tools.checkstyle.api;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AbstractLoaderTest {

    @Mock
    private InputSource mockInputSource;
    @Mock
    private SAXParseException mockSAXParseException;
    @Mock
    private InputStream mockInputStream;

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        MockitoAnnotations.initMocks(this);
        abstractLoader = new AbstractLoader(mock(Map.class)) {
            @Override
            public void startElement(String uri, String localName, String qName) throws SAXException { }
        };
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        // Arrange
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");

        // Act
        AbstractLoader loader = new AbstractLoader("publicId1", "dtdResourceName1") {
            @Override
            public void startElement(String uri, String localName, String qName) throws SAXException { }
        };

        // Assert
        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        // Arrange
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        // Act
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName) throws SAXException { }
        };

        // Assert
        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // Arrange

        // Act
        abstractLoader.parseInputSource(mockInputSource);

        // Assert
        verify(abstractLoader.parser).parse(mockInputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        // Arrange
        String publicId = "publicId1";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";
        ClassLoader mockClassLoader = mock(ClassLoader.class);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(mockClassLoader);
        when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(mockSet);
        when(abstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);
        when(mockClassLoader.getResourceAsStream(dtdResourceName)).thenReturn(mockInputStream);

        // Act
        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        // Assert
        verify(mockInputStream).close();
        assertEquals(new InputSource(mockInputStream), result);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        // Arrange

        // Act
        abstractLoader.error(mockSAXParseException);

        // Assert
        verify(mockSAXParseException).getException();
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        // Arrange

        // Act
        abstractLoader.fatalError(mockSAXParseException);

        // Assert
        verify(mockSAXParseException).getException();
    }
}
