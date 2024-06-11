import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private final String publicId = "testPublicId";
    private final String dtdResourceName = "testDtdResourceName";

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);

        // Verify publicIdToResourceNameMap contains the provided publicId and dtdResourceName
        Map<String, String> publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
        assertNotNull(publicIdToResourceNameMap);
        assertEquals(1, publicIdToResourceNameMap.size());
        assertEquals(dtdResourceName, publicIdToResourceNameMap.get(publicId));

        // Verify that the XMLReader is properly initialized
        XMLReader parser = loader.getParser();
        assertNotNull(parser);
        assertEquals(loader, parser.getContentHandler());
        assertEquals(loader, parser.getEntityResolver());
        assertEquals(loader, parser.getErrorHandler());
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicIdToResourceNameMap);

        // Verify publicIdToResourceNameMap is properly set
        assertEquals(publicIdToResourceNameMap, loader.getPublicIdToResourceNameMap());

        // Verify that the XMLReader is properly initialized
        XMLReader parser = loader.getParser();
        assertNotNull(parser);
        assertEquals(loader, parser.getContentHandler());
        assertEquals(loader, parser.getEntityResolver());
        assertEquals(loader, parser.getErrorHandler());
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);
        XMLReader parser = loader.getParser();

        InputSource inputSource = mock(InputSource.class);

        // Verify that the given inputSource is parsed by the XMLReader
        loader.parseInputSource(inputSource);

        verify(parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);
        Map<String, String> publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
        XMLReader parser = loader.getParser();

        String systemId = "testSystemId";

        InputSource expectedInputSource = new InputSource(mock(InputStream.class));

        // Verify that the matching publicId resolves to the expectedInputSource
        InputSource actualInputSource = loader.resolveEntity(publicId, systemId);

        assertEquals(expectedInputSource, actualInputSource);

        // Verify that the entityResolver is utilized by the XMLReader
        verify(parser, times(1)).setEntityResolver(loader);
    }

    @Test
    public void testResolveEntityWithNonMatchingPublicId() throws SAXException, IOException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);
        XMLReader parser = loader.getParser();

        String nonMatchingPublicId = "testNonMatchingPublicId";
        String systemId = "testSystemId";

        // Verify that a non-matching publicId does not resolve to any InputSource
        InputSource actualInputSource = loader.resolveEntity(nonMatchingPublicId, systemId);

        assertEquals(null, actualInputSource);

        // Verify that the entityResolver is utilized by the XMLReader
        verify(parser, times(1)).setEntityResolver(loader);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);
        loader.error(mock(SAXParseException.class));
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        AbstractLoaderLoader loader = new AbstractLoaderLoader(publicId, dtdResourceName);
        loader.fatalError(mock(SAXParseException.class));
    }

    // Inner class for testing AbstractLoader since it is abstract
    private static class AbstractLoaderLoader extends AbstractLoader {

        public AbstractLoaderLoader(String publicId, String dtdResourceName) throws SAXException, ParserConfigurationException {
            super(publicId, dtdResourceName);
        }

        public AbstractLoaderLoader(Map<String, String> publicIdToResourceNameMap) throws SAXException, ParserConfigurationException {
            super(publicIdToResourceNameMap);
        }

        @Override
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            // Override startElement method for testing purposes only, no implementation needed
        }

    }

}