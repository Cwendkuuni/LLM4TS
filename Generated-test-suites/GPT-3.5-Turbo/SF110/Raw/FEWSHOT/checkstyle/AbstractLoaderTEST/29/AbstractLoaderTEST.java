package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        // TODO: Implement test case
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        // TODO: Implement test case
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // TODO: Implement test case
    }

    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        // TODO: Implement test case
    }

    @Test
    public void testResolveEntityWithoutMatchingPublicId() throws SAXException, IOException {
        // TODO: Implement test case
    }

    @Test
    public void testError() throws SAXException {
        // TODO: Implement test case
    }

    @Test
    public void testFatalError() throws SAXException {
        // TODO: Implement test case
    }
}