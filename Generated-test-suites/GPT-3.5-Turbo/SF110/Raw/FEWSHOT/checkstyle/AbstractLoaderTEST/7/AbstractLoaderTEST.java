
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader parser;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        parser = mock(XMLReader.class);
        abstractLoader = new AbstractLoader() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
        };
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        verify(parser).parse(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        String publicId = "checkstyle";
        String systemId = "http://checkstyle.org/dtds/configuration_1_3.dtd";
        String dtdResourceName = "configuration_1_3.dtd";
        ClassLoader loader = abstractLoader.getClass().getClassLoader();
        InputStream dtdIs = loader.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(dtdIs);

        when(abstractLoader.resolveEntity(publicId, systemId)).thenCallRealMethod();
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);

        assertEquals(expectedInputSource, inputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.error(exception);
        verify(parser).error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.fatalError(exception);
        verify(parser).fatalError(exception);
    }
}
