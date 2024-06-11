
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.junit.Before;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        abstractLoader = spy(AbstractLoader.class);
    }

    @Test
    public void parseInputSource_ValidInputSource_ParsesSuccessfully() throws IOException, SAXException {
        // Arrange
        InputSource inputSource = mock(InputSource.class);

        // Act
        abstractLoader.parseInputSource(inputSource);

        // Assert
        verify(abstractLoader.parser).parse(inputSource);
    }

    @Test
    public void resolveEntity_KnownPublicId_ResolveDtdResource() throws SAXException, IOException {
        // Arrange
        String publicId = "publicId";
        String systemId = "systemId";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, "dtdResourceName");
        AbstractLoader abstractLoader = new AbstractLoader(publicId, "dtdResourceName") {
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return super.resolveEntity(publicId, systemId);
            }
        };

        // Act
        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        // Assert
        InputStream expectedInputStream = abstractLoader.getClass().getClassLoader().getResourceAsStream("dtdResourceName");
        verify(abstractLoader).getClass().getClassLoader();
        verify(expectedInputStream).getInputSource();
    }

    @Test(expected = SAXException.class)
    public void error_SaxParseException_ThrowsSaxException() throws SAXException {
        // Arrange
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void fatalError_SaxParseException_ThrowsSaxException() throws SAXException {
        // Arrange
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.fatalError(exception);
    }
}
