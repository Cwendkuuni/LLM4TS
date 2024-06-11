import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.*;
import static org.junit.Assert.*;

public class DataUtilTest {

    private Document doc;
    private String baseUri = "http://example.com";

    @Before
    public void setUp() {
        doc = new Document(baseUri);
    }

    @After
    public void tearDown() {
        doc = null;
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document loadedDoc = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(loadedDoc);
        assertEquals(doc.baseUri(), loadedDoc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document loadedDoc = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(loadedDoc);
        assertEquals(doc.baseUri(), loadedDoc.baseUri());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document loadedDoc = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
        assertNotNull(loadedDoc);
        assertEquals(doc.baseUri(), loadedDoc.baseUri());
        inputStream.close();
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        Document parsedDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(parsedDoc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test reading bytes to ByteBuffer
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test extracting charset from content type
        String contentType = "text/html; charset=UTF-8";
        String extractedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(extractedCharset);
        assertEquals("UTF-8", extractedCharset);
    }

}