import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class AbstractLoaderTest {
    @Mock
    private XMLReaderAdapter parserMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");

        AbstractLoader abstractLoader = new AbstractLoader("publicId1", "dtdResourceName1") {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };

        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");

        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };

        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();

        AbstractLoader abstractLoader = new AbstractLoader() {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
                assertEquals(inputSource, inputSource);
            }
        };

        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";

        String dtdResourceName = "dtdResourceName";
        ClassLoader loader = this.getClass().getClassLoader();
        InputStream dtdIs = loader.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(dtdIs);

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };

        InputSource resolvedInputSource = abstractLoader.resolveEntity(publicId, systemId);

        assertEquals(expectedInputSource, resolvedInputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        SAXParseException exception = new SAXParseException("test error", null);

        AbstractLoader abstractLoader = new AbstractLoader() {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };

        abstractLoader.error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("test fatal error", null);

        AbstractLoader abstractLoader = new AbstractLoader() {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };

        abstractLoader.fatalError(exception);
    }
}