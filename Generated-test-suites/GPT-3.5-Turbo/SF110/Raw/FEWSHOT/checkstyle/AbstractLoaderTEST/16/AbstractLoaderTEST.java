package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private XMLReader mockParser;
    private InputSource mockInputSource;

    @Before
    public void setup() throws ParserConfigurationException, SAXException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");
        loader = Mockito.spy(AbstractLoader.class);
        doReturn(publicIdToResourceNameMap).when(loader).getPublicIdToResourceNameMap();

        SAXParserFactory factory = mock(SAXParserFactory.class);
        SAXParser parser = mock(SAXParser.class);
        mockParser = mock(XMLReader.class);

        when(factory.newSAXParser()).thenReturn(parser);
        when(parser.getXMLReader()).thenReturn(mockParser);
        when(mockParser.getContentHandler()).thenReturn(loader);
        when(mockParser.getEntityResolver()).thenReturn(loader);
        when(mockParser.getErrorHandler()).thenReturn(loader);
        loader.setParser(mockParser);
    }

    @Test
    public void testConstructorWithPublicIdAndResourceName() throws SAXException, ParserConfigurationException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        loader = new AbstractLoader(publicId, dtdResourceName) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {

            }
        };

        assertEquals(1, loader.getPublicIdToResourceNameMap().size());
        assertEquals(dtdResourceName, loader.getPublicIdToResourceNameMap().get(publicId));
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {

            }
        };

        assertEquals(2, loader.getPublicIdToResourceNameMap().size());
        assertTrue(loader.getPublicIdToResourceNameMap().containsKey("publicId1"));
        assertTrue(loader.getPublicIdToResourceNameMap().containsKey("publicId2"));
        assertEquals("dtdResourceName1", loader.getPublicIdToResourceNameMap().get("publicId1"));
        assertEquals("dtdResourceName2", loader.getPublicIdToResourceNameMap().get("publicId2"));
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        mockInputSource = mock(InputSource.class);
        loader.parseInputSource(mockInputSource);
        verify(mockParser, times(1)).parse(mockInputSource);
    }

    @Test
    public void testResolveEntityWithMappedPublicId() throws IOException, SAXException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader.setPublicIdToResourceNameMap(publicIdToResourceNameMap);
        mockParser = mock(XMLReader.class);
        loader.setParser(mockParser);

        ClassLoader mockClassLoader = mock(ClassLoader.class);
        loader.setClassLoader(mockClassLoader);

        InputStream mockInputStream = mock(InputStream.class);
        when(mockClassLoader.getResourceAsStream(dtdResourceName)).thenReturn(mockInputStream);

        InputSource resolvedInputSource = loader.resolveEntity(publicId, null);
        verify(mockClassLoader, times(1)).getResourceAsStream(dtdResourceName);
        assertEquals(mockInputStream, resolvedInputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWithUnmappedPublicId() throws IOException, SAXException {
        String publicId = "unmappedPublicId";
        String systemId = null;
        super.resolveEntity(publicId, systemId);
        verify(mockParser, times(1)).resolveEntity(publicId, systemId);
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException mockException = mock(SAXParseException.class);
        assertThrows(SAXParseException.class, () -> loader.error(mockException));
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException mockException = mock(SAXParseException.class);
        assertThrows(SAXParseException.class, () -> loader.fatalError(mockException));
    }
}