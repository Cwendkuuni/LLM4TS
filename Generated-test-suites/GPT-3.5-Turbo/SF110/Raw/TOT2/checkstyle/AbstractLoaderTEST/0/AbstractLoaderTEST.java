
package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;

public class AbstractLoaderTest {

    private AbstractLoader loader;

    @Before
    public void setUp() {
        loader = new AbstractLoader(new HashMap<String, String>());
    }

    @Test
    public void testParseInputSource() throws Exception {
        InputSource inputSource = new InputSource("test.xml");
        assertThrows(IOException.class, () -> loader.parseInputSource(inputSource));
    }

    @Test
    public void testResolveEntity() throws Exception {
        // Test when publicId is in the map
        inputSource = loader.resolveEntity("publicId", "systemId");
        assertNotNull(inputSource);

        // Test when publicId is not in the map
        inputSource = loader.resolveEntity("nonExistingPublicId", "systemId");
        assertNull(inputSource);
    }

    @Test
    public void testError() {
        SAXParseException exception = new SAXParseException("error", null, null);
        assertThrows(SAXException.class, () -> loader.error(exception));
    }

    @Test
    public void testFatalError() {
        SAXParseException exception = new SAXParseException("fatal error", null, null);
        assertThrows(SAXException.class, () -> loader.fatalError(exception));
    }
}
