package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        InputSource inputSource = new InputSource();
        assertThrows(SAXException.class, () -> { loader.parseInputSource(inputSource); });
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        assertThrows(SAXException.class, () -> { loader.resolveEntity("publicId", "systemId"); });
    }

    @Test
    public void testError() {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        assertThrows(SAXException.class, () -> { loader.error(new SAXParseException("error", null)); });
    }

    @Test
    public void testFatalError() {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        assertThrows(SAXException.class, () -> { loader.fatalError(new SAXParseException("fatal error", null)); });
    }
}