package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;
    private static final String PUBLIC_ID = "publicId";
    private static final String DTD_RESOURCE_NAME = "dtdResourceName";

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("org.xml.sax.driver", "org.xmlunit.driver");
    }

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        abstractLoader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        abstractLoader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME) {
            // Mocked abstract methods
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {}

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {}
        };

        assertEquals(1, abstractLoader.publicIdToResourceNameMap.size());
        assertTrue(abstractLoader.publicIdToResourceNameMap.containsKey(PUBLIC_ID));
        assertEquals(DTD_RESOURCE_NAME, abstractLoader.publicIdToResourceNameMap.get(PUBLIC_ID));
        assertNotNull(abstractLoader.parser);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(PUBLIC_ID, DTD_RESOURCE_NAME);
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            // Mocked abstract methods
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {}

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {}
        };

        assertEquals(publicIdToResourceNameMap.size(), abstractLoader.publicIdToResourceNameMap.size());
        assertEquals(DTD_RESOURCE_NAME, abstractLoader.publicIdToResourceNameMap.get(PUBLIC_ID));
        assertNotNull(abstractLoader.parser);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = Mockito.mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);

        Mockito.verify(abstractLoader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws SAXException, IOException {
        abstractLoader.publicIdToResourceNameMap.put(PUBLIC_ID, DTD_RESOURCE_NAME);

        String systemId = "systemId";
        InputSource result = abstractLoader.resolveEntity(PUBLIC_ID, systemId);

        ClassLoader classLoader = abstractLoader.getClass().getClassLoader();
        InputStream expectedInputStream = classLoader.getResourceAsStream(DTD_RESOURCE_NAME);

        assertNotNull(result);
        assertEquals(expectedInputStream, result.getByteStream());
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";
        
        InputSource result = abstractLoader.resolveEntity(publicId, systemId);
        
        assertNull(result);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);

        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);

        abstractLoader.fatalError(exception);
    }
}