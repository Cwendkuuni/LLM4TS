import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Mock
    private InputSource inputSource;

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        abstractLoader = mock(AbstractLoader.class, CALLS_REAL_METHODS);

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;
    }

    @Test
    public void testConstructorWithStringString() throws SAXException, ParserConfigurationException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        abstractLoader = new AbstractLoader(publicId, dtdResourceName) {};

        assertEquals(1, abstractLoader.publicIdToResourceNameMap.size());
        assertTrue(abstractLoader.publicIdToResourceNameMap.containsKey(publicId));
        assertEquals(dtdResourceName, abstractLoader.publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testConstructorWithMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {};

        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        abstractLoader.parseInputSource(inputSource);

        verify(abstractLoader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWhenPublicIdExists() throws SAXException, IOException {
        String publicId = "publicId1";
        String dtdResourceName = "dtdResourceName";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream dtdIs = classLoader.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(dtdIs);

        when(abstractLoader.publicIdToResourceNameMap.containsKey(publicId)).thenReturn(true);
        when(abstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(classLoader);

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWhenPublicIdDoesNotExist() throws SAXException, IOException {
        String publicId = "publicId3";
        String systemId = "systemId";

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, systemId);

        assertNull(actualInputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        abstractLoader.fatalError(exception);
    }

}