package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Mock
    private XMLReader parser;

    private AbstractLoader abstractLoader;

     @Before
     public void setUp() throws SAXException, ParserConfigurationException {
        abstractLoader = new AbstractLoaderImpl(new HashMap<>());
        abstractLoader.parser = parser;
     }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // Prepare
        InputSource inputSource = mock(InputSource.class);

        // Act
        abstractLoader.parseInputSource(inputSource);

        // Assert
        verify(parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicIdAndSystemId() throws SAXException, IOException {
        // Prepare
        String publicId = "publicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";
        ClassLoader loader = mock(ClassLoader.class);
        InputStream dtdIs = mock(InputStream.class);
        InputSource expectedInputSource = new InputSource(dtdIs);

        // Mock
        when(parser.resolveEntity(publicId, systemId)).thenCallRealMethod();
        when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(Collections.singleton(publicId));
        when(abstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(loader);
        when(loader.getResourceAsStream(dtdResourceName)).thenReturn(dtdIs);

        // Act
        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, systemId);

        // Assert
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityWithoutPublicId() throws SAXException, IOException {
        // Prepare
        String publicId = "publicId";
        String systemId = "systemId";
        when(parser.resolveEntity(publicId, systemId)).thenCallRealMethod();
        when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(Collections.emptySet());

        // Act
        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, systemId);

        // Assert
        assertNull(actualInputSource);
        verify(parser, times(1)).resolveEntity(publicId, systemId);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        // Prepare
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        // Prepare
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.fatalError(exception);
    }

    private static class AbstractLoaderImpl extends AbstractLoader {
        public AbstractLoaderImpl(Map<String, String> publicIdToResourceNameMap) throws SAXException, ParserConfigurationException {
            super(publicIdToResourceNameMap);
        }
    }
}