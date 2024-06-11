import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private static File file;
    private static String charsetName;
    private static String baseUri;
    private static Parser parser;

    @BeforeClass
    public static void initialize() {
        file = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "https://www.example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoad_withFile_shouldLoadDocument() throws IOException {
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        // Additional assertions can be added for checking if the document is loaded correctly.
    }

    @Test
    public void testLoad_withInputStream_shouldLoadDocument() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Additional assertions can be added for checking if the document is loaded correctly.
        inputStream.close();
    }

    @Test
    public void testLoad_withInputStreamAndParser_shouldLoadDocument() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Additional assertions can be added for checking if the document is loaded correctly.
        inputStream.close();
    }

    @Test
    public void testParseByteData_withByteDataAndCharsetName_shouldParseDocument() {
        // Create a ByteBuffer with some test data
        String testData = "<html><body><p>This is a test document</p></body></html>";
        Charset charset = Charset.forName(charsetName);
        byte[] testDataBytes = testData.getBytes(charset);
        ByteBuffer byteData = ByteBuffer.wrap(testDataBytes);

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        // Additional assertions can be added for checking if the document is parsed correctly.
    }

    @Test
    public void testReadToByteBuffer_withInputStream_shouldReadIntoByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        // Additional assertions can be added for checking if the data is read into the ByteBuffer correctly.
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType_withContentType_shouldReturnCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset_shouldReturnNull() {
        String contentType = "text/html; charset=ISO-8859-42";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

}