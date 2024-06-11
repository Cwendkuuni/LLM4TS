
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;
    private Parser testParser;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.html"); // Replace with actual test file path
        testInputStream = new FileInputStream(testFile);
        testParser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(document);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", "");
        assertNotNull(document);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", "", testParser);
        assertNotNull(document);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testParseByteData() {
        ByteBuffer testByteData = ByteBuffer.allocate(128); // Replace with actual test byte data
        Document document = DataUtil.parseByteData(testByteData, "UTF-8", "", testParser);
        assertNotNull(document);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024);
        assertNotNull(byteBuffer);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(byteBuffer);
        // Add assertion statements for the expected result of this method
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add assertion statements for other potential content types and charsets
    }
}
