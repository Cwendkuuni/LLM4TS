package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader parser;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        abstractLoader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);

        parser = Mockito.mock(XMLReader.class);
        Mockito.when(parser.getContentHandler()).thenReturn(abstractLoader);
        Mockito.when(parser.getEntityResolver()).thenReturn(abstractLoader);
        Mockito.when(parser.getErrorHandler()).thenReturn(abstractLoader);

        Mockito.when(abstractLoader.getClass()).thenReturn(AbstractLoader.class);

        SAXParserFactory factory = Mockito.mock(SAXParserFactory.class);
        SAXParser saxParser = Mockito.mock(SAXParser.class);
        Mockito.when(factory.newSAXParser()).thenReturn(saxParser);

        Mockito.when(saxParser.getXMLReader()).thenReturn(parser);

        abstractLoader.parser = parser;
        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;

    }

    @Test
    public void testConstructor_withPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        abstractLoader = new AbstractLoader("publicId", "dtdResourceName") {
            // Empty body for abstract class instantiation
        };

        assertEquals(1, abstractLoader.publicIdToResourceNameMap.size());
        assertEquals("dtdResourceName", abstractLoader.publicIdToResourceNameMap.get("publicId"));
        assertNotNull(abstractLoader.parser);
        assertTrue(abstractLoader.parser.isValidating());
        assertTrue(abstractLoader.parser.isNamespaceAware());
        assertEquals(abstractLoader, abstractLoader.parser.getContentHandler());
        assertEquals(abstractLoader, abstractLoader.parser.getEntityResolver());
        assertEquals(abstractLoader, abstractLoader.parser.getErrorHandler());
    }

    @Test
    public void testConstructor_withPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            // Empty body for abstract class instantiation
        };

        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
        assertNotNull(abstractLoader.parser);
        assertTrue(abstractLoader.parser.isValidating());
        assertTrue(abstractLoader.parser.isNamespaceAware());
        assertEquals(abstractLoader, abstractLoader.parser.getContentHandler());
        assertEquals(abstractLoader, abstractLoader.parser.getEntityResolver());
        assertEquals(abstractLoader, abstractLoader.parser.getErrorHandler());
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = Mockito.mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        Mockito.verify(parser, Mockito.times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntity_withMatchingPublicId() throws IOException, SAXException {
        String publicId = "publicId1";
        String systemId = "systemId1";
        String dtdResourceName = "dtdResourceName1";
        InputSource expectedInputSource = Mockito.mock(InputSource.class);

        Mockito.when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<>(Collections.singletonList(publicId)));
        Mockito.when(abstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);

        ClassLoader loader = Mockito.mock(ClassLoader.class);
        Mockito.when(abstractLoader.getClass().getClassLoader()).thenReturn(loader);

        InputStream dtdIs = Mockito.mock(InputStream.class);
        Mockito.when(loader.getResourceAsStream(dtdResourceName)).thenReturn(dtdIs);

        Mockito.when(expectedInputSource.getByteStream()).thenReturn(dtdIs);

        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        assertEquals(expectedInputSource.getByteStream(), result.getByteStream());
    }

    @Test
    public void testResolveEntity_withoutMatchingPublicId() throws IOException, SAXException {
        String publicId = "publicId";
        String systemId = "systemId";

        Mockito.when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<>());

        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        assertNull(result);
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        try {
            abstractLoader.error(exception);
            fail("Expected exception was not thrown");
        } catch (SAXParseException e) {
            assertSame(exception, e);
        }
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        try {
            abstractLoader.fatalError(exception);
            fail("Expected exception was not thrown");
        } catch (SAXParseException e) {
            assertSame(exception, e);
        }
    }
}