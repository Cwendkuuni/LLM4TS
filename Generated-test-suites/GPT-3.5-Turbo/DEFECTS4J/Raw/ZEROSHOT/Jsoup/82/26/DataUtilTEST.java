import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = mock(Document.class);
        when(DataUtil.parseInputStream(any(InputStream.class), any(String.class), any(String.class), any(Parser.class))).thenReturn(document);

        Document result = dataUtil.load(file, charsetName, baseUri);

        assertNotNull(result);
        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = mock(Document.class);
        when(DataUtil.parseInputStream(any(InputStream.class), any(String.class), any(String.class), any(Parser.class))).thenReturn(document);

        Document result = dataUtil.load(in, charsetName, baseUri);

        assertNotNull(result);
        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        Document document = mock(Document.class);
        when(DataUtil.parseInputStream(any(InputStream.class), any(String.class), any(String.class), any(Parser.class))).thenReturn(document);

        Document result = dataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(result);
        assertEquals(document, result);
    }

}