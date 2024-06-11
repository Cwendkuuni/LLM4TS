package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        // Arrange
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";

        // Act
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        loader.publicIdToResourceNameMap = new HashMap<String, String>();
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);

        // Assert
        assertNotNull(loader.publicIdToResourceNameMap);
        assertEquals(publicId, loader.publicIdToResourceNameMap.keySet().toArray()[0]);
        assertEquals(dtdResourceName, loader.publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        // Arrange
        Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        // Act
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        loader.publicIdToResourceNameMap = publicIdToResourceNameMap;

        // Assert
        assertNotNull(loader.publicIdToResourceNameMap);
        assertEquals(publicIdToResourceNameMap.keySet().size(), loader.publicIdToResourceNameMap.keySet().size());
        assertEquals(publicIdToResourceNameMap.get("publicId1"), loader.publicIdToResourceNameMap.get("publicId1"));
        assertEquals(publicIdToResourceNameMap.get("publicId2"), loader.publicIdToResourceNameMap.get("publicId2"));
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // Arrange
        InputSource inputSource = Mockito.mock(InputSource.class);
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        Mockito.doNothing().when(loader.parser).parse(inputSource);

        // Act
        loader.parseInputSource(inputSource);

        // Assert
        Mockito.verify(loader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        // Arrange
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        loader.publicIdToResourceNameMap = new HashMap<String, String>();
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader.getClass();

        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        InputStream inputStream = Mockito.mock(InputStream.class);

        Mockito.when(loader.getClass().getClassLoader()).thenReturn(classLoader);
        Mockito.when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(inputStream);

        // Act
        InputSource result = loader.resolveEntity(publicId, "systemId");

        // Assert
        assertNotNull(result);
        Mockito.verify(classLoader).getResourceAsStream(dtdResourceName);
    }

    @Test
    public void testResolveEntityWithNonMatchingPublicId() throws SAXException, IOException {
        // Arrange
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        loader.publicIdToResourceNameMap = new HashMap<String, String>();
        loader.publicIdToResourceNameMap.put("somePublicId", dtdResourceName);
        loader.getClass();

        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        InputStream inputStream = Mockito.mock(InputStream.class);

        Mockito.when(loader.getClass().getClassLoader()).thenReturn(classLoader);
        Mockito.when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(inputStream);

        // Act
        InputSource result = loader.resolveEntity(publicId, "systemId");

        // Assert
        assertNull(result);
        Mockito.verify(classLoader, Mockito.never()).getResourceAsStream(dtdResourceName);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        // Arrange
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        AbstractLoader loader = new AbstractLoader() {
            @Override
            public void startDocument() throws SAXException {
            }
        };

        // Act
        loader.error(exception);

        // Assert

    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        // Arrange
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        AbstractLoader loader = new AbstractLoader() {
            @Override
            public void startDocument() throws SAXException {
            }
        };

        // Act
        loader.fatalError(exception);

        // Assert

    }
}