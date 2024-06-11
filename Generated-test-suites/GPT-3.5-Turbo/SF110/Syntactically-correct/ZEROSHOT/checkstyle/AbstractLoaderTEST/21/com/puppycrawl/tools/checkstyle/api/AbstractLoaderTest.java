package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // create mock objects
        InputSource inputSource = mock(InputSource.class);
        AbstractLoader loader = spy(AbstractLoader.class);

        // call the method to be tested
        loader.parseInputSource(inputSource);

        // verify that the parser method was called
        verify(loader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws SAXException, IOException {
        // create mock objects
        String publicId = "validPublicId";
        String dtdResourceName = "validDtdResourceName";
        AbstractLoader loader = spy(AbstractLoader.class);
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader.publicIdToResourceNameMap = publicIdToResourceNameMap;

        // call the method to be tested
        loader.resolveEntity(publicId, "systemId");

        // verify that the correct resource name was retrieved
        verify(publicIdToResourceNameMap).get(publicId);

        // verify that the getResourceAsStream method was called with the correct resource name
        verify(loader.getClass().getClassLoader()).getResourceAsStream(dtdResourceName);

        // verify that a new InputSource was returned
        verify(loader).newInputSource(any(InputStream.class));
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        // create mock objects
        String publicId = "invalidPublicId";
        String systemId = "invalidSystemId";
        AbstractLoader loader = spy(AbstractLoader.class);
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        loader.publicIdToResourceNameMap = publicIdToResourceNameMap;

        // call the method to be tested
        loader.resolveEntity(publicId, systemId);

        // verify that the superclass resolveEntity method was called
        verify(loader).superResolveEntity(publicId, systemId);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        // create mock objects
        SAXParseException exception = mock(SAXParseException.class);
        AbstractLoader loader = new AbstractLoader() {};

        // call the method to be tested
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        // create mock objects
        SAXParseException exception = mock(SAXParseException.class);
        AbstractLoader loader = new AbstractLoader() {};

        // call the method to be tested
        loader.fatalError(exception);
    }
}