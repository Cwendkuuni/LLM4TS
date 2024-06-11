
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoader loader = mock(AbstractLoader.class);

        InputSource inputSource = new InputSource("test.xml");

        loader.parseInputSource(inputSource);

        verify(loader).parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        String publicId = "publicId";
        String systemId = "systemId";

        loader.resolveEntity(publicId, systemId);

        // Verify that the correct public id and system id are passed to the method
        verify(loader).resolveEntity(publicId, systemId);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        SAXParseException exception = mock(SAXParseException.class);

        loader.error(exception);

        // Verify that the error method throws the exception
        verify(loadby).error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        SAXParseException exception = mock(SAXParseException.class);

        loader.fatalError(exception);

        // Verify that the fatalError method throws the exception
        verify(loader).fatalError(exception);
    }
}
