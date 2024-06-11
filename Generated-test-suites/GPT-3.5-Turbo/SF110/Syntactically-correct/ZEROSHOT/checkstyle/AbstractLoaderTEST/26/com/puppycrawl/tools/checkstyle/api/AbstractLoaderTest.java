package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        abstractLoader = spy(AbstractLoader.class);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // given
        InputSource inputSource = mock(InputSource.class);

        // when
        abstractLoader.parseInputSource(inputSource);

        // then
        // add assertions here
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        // given
        String publicId = "publicId";
        String systemId = "systemId";

        // when
        abstractLoader.resolveEntity(publicId, systemId);

        // then
        // add assertions here
    }

    @Test
    public void testError() throws SAXException {
        // given
        SAXParseException exception = mock(SAXParseException.class);

        // when
        abstractLoader.error(exception);

        // then
        // add assertions here
    }

    @Test
    public void testFatalError() throws SAXException {
        // given
        SAXParseException exception = mock(SAXParseException.class);

        // when
        abstractLoader.fatalError(exception);

        // then
        // add assertions here
    }
}