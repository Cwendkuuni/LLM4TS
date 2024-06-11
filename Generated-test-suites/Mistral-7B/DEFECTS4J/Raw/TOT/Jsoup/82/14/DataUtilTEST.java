import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws Exception {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Create a mock Document for the test
        Document mockDocument = Mockito.mock(Document.class);

        // Stub the parseInputStream method to return the mockDocument
        DataUtil dataUtil = Mockito.spy(DataUtil.class);
        Mockito.when(dataUtil.parseInputStream(Mockito.any(InputStream.class), Mockito.eq(charsetName), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()))).thenReturn(mockDocument);

        // Call the method under test
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);

        // Assert the expected result
        assertEquals(mockDocument, document);
    }

    @Test
    public void testLoadFileWithNullCharset() throws Exception {
        String filePath = "test.html";
        String baseUri = "http://example.com";

        // Call the method under test
        assertThrows(NullPointerException.class, () -> DataUtil.load(new File(filePath), null, baseUri));
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws Exception {
        String filePath = "test.html";
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // Call the method under test
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new File(filePath), charsetName, baseUri));
    }

    @Test
    public void testLoadFileWithInvalidCharsetAndContentType() throws Exception {
        String filePath = "test.html";
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        String contentType = "text/html; charset=UTF-8";

        // Create a mock Document for the test
        Document mockDocument = Mockito.mock(Document.class);

        // Stub the parseInputStream method to return the mockDocument
        DataUtil dataUtil = Mockito.spy(DataUtil.class);
        Mockito.when(dataUtil.parseInputStream(Mockito.any(InputStream.class), Mockito.eq(charsetName), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()))).thenReturn(mockDocument);

        // Call the method under test
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri, contentType);

        // Assert the expected result
        assertEquals(mockDocument, document);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Create a mock Document for the test
        Document mockDocument = Mockito.mock(Document.class);

        // Create a test input stream
        String testContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        // Stub the parseInputStream method to return the mockDocument
        DataUtil dataUtil = Mockito.spy(DataUtil.class);
        Mockito.when(dataUtil.parseInputStream(Mockito.eq(inputStream), Mockito.eq(charsetName), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()))).thenReturn(mockDocument);

        // Call the method under test
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert the expected result
        assertEquals(mockDocument, document);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws Exception {
        String baseUri = "http://example.com";

        // Create a test input stream
        String testContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        // Call the method under test
        assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, null, baseUri));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // Create a test input stream
        String testContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        // Call the method under test
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithContentType() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String contentType = "text/html; charset=UTF-8";

        // Create a mock Document for the test
        Document mockDocument = Mockito.mock(Document.class);

        // Create a test input stream
        String testContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        // Stub the parseInputStream method to return the mockDocument
        DataUtil dataUtil = Mockito.spy(DataUtil.class);
        Mockito.when(dataUtil.parseInputStream(Mockito.eq(inputStream), Mockito.eq(charsetName), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()))).thenReturn(mockDocument);

        // Call the method under test
        Document document = DataUtil.load(inputStream, charsetName, baseUri, contentType);

        // Assert the expected result
        assertEquals(mockDocument, document);
    }

    @Test
    public void testLoadInputStreamWithInvalidContentType() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String contentType = "text/plain; charset=UTF-8";

        // Create a test input stream
        String testContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        // Call the method under test
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, contentType));
    }

    @Test
    public void testCrossStreams() throws Exception {
        String testInput = "Test Input";
        String testOutput = "Test Output";

        // Create test input stream
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes(StandardCharsets.UTF_8));

        // Create test output stream
        OutputStream outputStream = new ByteArrayOutputStream();

        // Call the method under test
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert the expected result
        assertEquals(testOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }
}