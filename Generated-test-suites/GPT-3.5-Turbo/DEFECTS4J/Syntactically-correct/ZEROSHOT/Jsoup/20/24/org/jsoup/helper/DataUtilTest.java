import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private String testFile;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() {
        testFile = "test.html";
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @Test
    public void testLoadFromFile() {
        File file = new File(testFile);
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        String docData = "<html><body><p>Hello, world!</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(docData, document.html());
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream inStream = new FileInputStream(testFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charsetName, charset);
    }
}