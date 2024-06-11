import org.junit.*;
import org.xml.sax.Attributes;
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
    public void setUp() throws SAXException, ParserConfigurationException {
        abstractLoader = spy(AbstractLoader.class);
    }

    @Test
    public void testParseInputSource_shouldCallParserParse() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        verify(abstractLoader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntity_shouldReturnInputSourceWhenPublicIdExists() throws IOException, SAXException {
        String publicId = "publicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        AbstractLoader testAbstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            }
        };
        InputSource result = testAbstractLoader.resolveEntity(publicId, systemId);
        InputStream expectedInputStream = testAbstractLoader.getClass().getClassLoader().getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(expectedInputStream);

        Assert.assertEquals(expectedInputSource, result);
    }

    @Test
    public void testResolveEntity_shouldReturnSuperResolveEntityWhenPublicIdDoesNotExist() throws IOException, SAXException {
        String publicId = "nonExistentPublicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("otherPublicId", dtdResourceName);
        AbstractLoader testAbstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            }
        };
        InputSource result = testAbstractLoader.resolveEntity(publicId, systemId);

        Assert.assertEquals(null, result);
        verify(testAbstractLoader, never()).getClass();
    }

    @Test(expected = SAXException.class)
    public void testError_shouldThrowSAXException() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        abstractLoader.error(saxParseException);
    }

    @Test(expected = SAXException.class)
    public void testFatalError_shouldThrowSAXException() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        abstractLoader.fatalError(saxParseException);
    }
}