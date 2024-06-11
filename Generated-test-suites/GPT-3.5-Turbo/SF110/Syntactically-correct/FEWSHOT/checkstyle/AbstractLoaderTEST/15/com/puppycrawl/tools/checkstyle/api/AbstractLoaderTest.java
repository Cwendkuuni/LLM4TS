package com.puppycrawl.tools.checkstyle.api;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    @Test(expected = SAXException.class)
    public void testParseInputSource_invalidFile() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());
        loader.parseInputSource(new InputSource("invalidFile.xml"));
    }

    @Test
    public void testParseInputSource_validFile() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());
        loader.parseInputSource(new InputSource("validFile.xml"));
    }

    @Test
    public void testResolveEntity_publicIdExists() throws IOException, SAXException {
        String publicId = "testPublicId";
        String resourceName = "testResourceName.dtd";
        Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>();
        publicIdToResourceNameMap.put(publicId, resourceName);
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        InputSource inputSource = loader.resolveEntity(publicId, "testSystemId");
        assertNotNull(inputSource);
    }

    @Test
    public void testResolveEntity_publicIdDoesNotExist() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());
        InputSource inputSource = loader.resolveEntity("invalidPublicId", "testSystemId");
        assertNull(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());
        loader.error(new SAXParseException("Test error", null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());
        loader.fatalError(new SAXParseException("Test error", null));
    }
}