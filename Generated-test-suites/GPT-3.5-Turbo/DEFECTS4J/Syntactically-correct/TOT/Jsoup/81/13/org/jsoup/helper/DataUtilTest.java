import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void loadFile_existingFile_validDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void loadFile_nonExistingFile_emptyDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(new File("non_existing.html"), charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void loadInputStream_validInputStream_validDocument() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void loadInputStream_invalidInputStream_emptyDocument() throws IOException {
        InputStream inputStream = new FileInputStream("non_existing.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void parseInputStream_validInputStream_validDocument() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parseInputStream_invalidInputStream_emptyDocument() throws IOException {
        InputStream inputStream = new FileInputStream("non_existing.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void crossStreams_validInputStream_validOutputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedBytes = Jsoup.parse(testFile, "UTF-8", "http://example.com").html().getBytes();
        byte[] actualBytes = outputStream.toByteArray();
        assertEquals(expectedBytes.length, actualBytes.length);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void readToByteBuffer_validInputStream_validByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(testFile.length(), byteBuffer.capacity());
    }

    @Test
    public void readFileToByteBuffer_validFile_validByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
        assertEquals(testFile.length(), byteBuffer.capacity());
    }

    @Test
    public void emptyByteBuffer_validByteBuffer_emptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void getCharsetFromContentType_validContentType_validCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_invalidContentType_nullCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void mimeBoundary_validMimeBoundary_validLength() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void detectCharsetFromBom_validCharsetWithBom_validBomCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }

    @Test
    public void detectCharsetFromBom_invalidCharsetWithBom_nullBomCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}