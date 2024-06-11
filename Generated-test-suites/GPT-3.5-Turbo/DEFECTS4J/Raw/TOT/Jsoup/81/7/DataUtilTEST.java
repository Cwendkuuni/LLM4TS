import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    private File testFile;
    private String baseUri;
    
    @Before
    public void setUp() {
        testFile = new File("test.html");
        baseUri = "http://example.com";
    }

    @Test
    public void testLoad_withValidFile_returnDocument() throws IOException {
        String charsetName = "UTF-8";

        Document document = DataUtil.load(testFile, charsetName, baseUri);

        Assertions.assertNotNull(document);
        Assertions.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInvalidFile_throwIOException() {
        File invalidFile = new File("invalid.html");
        String charsetName = "UTF-8";

        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.load(invalidFile, charsetName, baseUri);
        });
    }

    @Test
    public void testLoad_withValidInputStream_returnDocument() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        Assertions.assertNotNull(document);
        Assertions.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_withInvalidInputStream_throwIOException() {
        InputStream invalidInputStream = null;
        String charsetName = "UTF-8";

        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.load(invalidInputStream, charsetName, baseUri);
        });
    }

    @Test
    public void testCrossStreams_withValidInputStreamAndOutputStream_noExceptionsThrown() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        OutputStream outputStream = System.out;

        Assertions.assertDoesNotThrow(() -> {
            DataUtil.crossStreams(inputStream, outputStream);
        });
    }

    @Test
    public void testParseInputStream_withValidInputStreamAndCharset_returnDocument() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        Assertions.assertNotNull(document);
        Assertions.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseInputStream_withInvalidInputStream_throwIOException() {
        InputStream invalidInputStream = null;
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();

        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.parseInputStream(invalidInputStream, charsetName, baseUri, parser);
        });
    }

    @Test
    public void testReadToByteBuffer_withValidInputStreamAndMaxSize_returnByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        Assertions.assertNotNull(byteBuffer);
        Assertions.assertTrue(byteBuffer.remaining() <= maxSize);
    }

    @Test
    public void testReadToByteBuffer_withInvalidInputStream_throwIOException() {
        InputStream invalidInputStream = null;

        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.readToByteBuffer(invalidInputStream);
        });
    }

    @Test
    public void testReadFileToByteBuffer_withValidFile_returnByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        Assertions.assertNotNull(byteBuffer);
        Assertions.assertTrue(byteBuffer.remaining() == testFile.length());
    }

    @Test
    public void testReadFileToByteBuffer_withInvalidFile_throwIOException() {
        File invalidFile = new File("invalid.html");

        Assertions.assertThrows(IOException.class, () -> {
            DataUtil.readFileToByteBuffer(invalidFile);
        });
    }

    @Test
    public void testEmptyByteBuffer_returnEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assertions.assertNotNull(byteBuffer);
        Assertions.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnCharset() {
        String contentType = "text/html; charset=EUC-JP";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assertions.assertNotNull(charset);
        Assertions.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_returnNull() {
        String invalidContentType = "application/json";

        String charset = DataUtil.getCharsetFromContentType(invalidContentType);

        Assertions.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withNullContentType_returnNull() {
        String nullContentType = null;

        String charset = DataUtil.getCharsetFromContentType(nullContentType);

        Assertions.assertNull(charset);
    }
    
    @Test
    public void testValidateCharset_withValidCharset_returnValidCharset() {
        String validCharset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(validCharset);

        Assertions.assertNotNull(validatedCharset);
        Assertions.assertEquals(validCharset, validatedCharset);
    }

    @Test
    public void testValidateCharset_withInvalidCharset_returnNull() {
        String invalidCharset = "InvalidCharset";

        String validatedCharset = DataUtil.validateCharset(invalidCharset);

        Assertions.assertNull(validatedCharset);
    }

}