import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws Exception {
        //Given
        AbstractLoader abstractLoader = mock(AbstractLoader.class);
        InputSource inputSource = new InputSource();

        //When
        abstractLoader.parseInputSource(inputSource);

        //Then
        verify(abstractLoader, times(1)).parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws Exception {
        //Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());
        String publicId = "testPublicId";
        String systemId = "testSystemId";

        //When
        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        //Then
        assertNull(result);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        //Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());
        SAXParseException exception = new SAXParseException("", null, null, 0, 0);

        //When
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        //Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());
        SAXParseException exception = new SAXParseException("", null, null, 0, 0);

        //When
        abstractLoader.fatalError(exception);
    }
}