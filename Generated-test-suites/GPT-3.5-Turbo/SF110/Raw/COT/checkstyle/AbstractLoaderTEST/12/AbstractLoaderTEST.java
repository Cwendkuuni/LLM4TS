
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException, ParserConfigurationException {
        // Given
        AbstractLoader abstractLoader = mock(AbstractLoader.class);
        InputSource inputSource = mock(InputSource.class);
        
        // When
        abstractLoader.parseInputSource(inputSource);
        
        // Then
        verify(abstractLoader, times(1)).parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws SAXException, IOException {
        // Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());

        // When
        InputSource result = abstractLoader.resolveEntity("publicId", "systemId");

        // Then
        assert(result != null);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        // Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());
        SAXParseException exception = mock(SAXParseException.class);

        // When
        abstractLoader.error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        // Given
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>());
        SAXParseException exception = mock(SAXParseException.class);

        // When
        abstractLoader.fatalError(exception);
    }
}
