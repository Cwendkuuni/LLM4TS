
package com.puppycrawl.tools.checkstyle.api;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class AbstractLoaderTest {

    @Mock
    private XMLReader mockParser;

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        MockitoAnnotations.initMocks(this);

        abstractLoader = spy(new AbstractLoader(new HashMap<String, String>()));
        abstractLoader.parser = mockParser;

        SAXParserFactory factory = mock(SAXParserFactory.class);
        when(factory.newSAXParser()).thenReturn(mockParser);
        doNothing().when(mockParser).setContentHandler(abstractLoader);
        doNothing().when(mockParser).setEntityResolver(abstractLoader);
        doNothing().when(mockParser).setErrorHandler(abstractLoader);
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        AbstractLoader loader = new AbstractLoader("publicId", "dtdResourceName");

        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();

        abstractLoader.parseInputSource(inputSource);

        verify(mockParser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        InputSource expectedInputSource = new InputSource();

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;

        ClassLoader mockClassLoader = mock(ClassLoader.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(this.abstractLoader.getClass().getClassLoader()).thenReturn(mockClassLoader);
        when(mockClassLoader.getResourceAsStream(dtdResourceName)).thenReturn(mockInputStream);

        InputSource result = abstractLoader.resolveEntity(publicId, null);

        assertNotNull(result);
        assertEquals(expectedInputSource, result);
    }

    @Test
    public void testResolveEntityWithNonMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";

        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        assertNull(result);
        verifySuperResolveEntityCalled(publicId, systemId);
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        try {
            abstractLoader.error(exception);
        } catch (SAXParseException e) {
            assertEquals(exception, e);
            return;
        }
        throw new AssertionError("Expected SAXParseException");
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        try {
            abstractLoader.fatalError(exception);
        } catch (SAXParseException e) {
            assertEquals(exception, e);
            return;
        }
        throw new AssertionError("Expected SAXParseException");
    }

    private void verifySuperResolveEntityCalled(String publicId, String systemId) throws SAXException, IOException {
        doReturn(mock(InputSource.class)).when(abstractLoader).super.resolveEntity(publicId, systemId);

        abstractLoader.resolveEntity(publicId, systemId);

        verify(abstractLoader, times(1)).super.resolveEntity(publicId, systemId);
    }
}
