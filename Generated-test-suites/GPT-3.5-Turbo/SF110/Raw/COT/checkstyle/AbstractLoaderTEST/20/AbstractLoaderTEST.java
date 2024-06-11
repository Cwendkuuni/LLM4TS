
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        abstractLoader = mock(AbstractLoader.class);
    }

    @Test
    public void testParseInputSource() {
        try {
            abstractLoader.parseInputSource(new InputSource());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResolveEntity() {
        try {
            abstractLoader.resolveEntity("publicId", "systemId");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testError() {
        SAXParseException exception = mock(SAXParseException.class);
        try {
            abstractLoader.error(exception);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFatalError() {
        SAXParseException exception = mock(SAXParseException.class);
        try {
            abstractLoader.fatalError(exception);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
