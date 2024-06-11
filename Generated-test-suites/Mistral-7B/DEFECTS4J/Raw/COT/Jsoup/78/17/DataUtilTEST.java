import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document doc;
    private static InputStream testFileInputStream;

    @BeforeClass
    public static void setUp() {
        // You can initialize any class level variables here
    }

    @Test
    public void testLoadFile() throws IOException {
        Document expectedDocument = DataUtil.load(new File("test.html"), "UTF-8", null);
        doc = DataUtil.load(new File("test.html"), "UTF-8", null);
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertDocumentEquals(expectedDocument, doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document expectedDocument = DataUtil.load(inputStream, "UTF-8", null);
        doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertDocumentEquals(expectedDocument, doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        Document expectedDocument = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertDocumentEquals(expectedDocument, doc);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("test.html");
        File testOutputFile = new File("testOutput.html");
        InputStream testFileInputStream = new FileInputStream(testFile);
        OutputStream testOutputStream = new FileOutputStream(testOutputFile);

        DataUtil.crossStreams(testFileInputStream, testOutputStream);
        testFileInputStream.close();
        testOutputStream.close();

        Document expectedDocument = DataUtil.load(new File("testOutput.html"), "UTF-8", null);
        Document actualDocument = DataUtil.load(new File("testOutput.html"), "UTF-8", null);
        assertDocumentEquals(expectedDocument, actualDocument);
        testOutputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        DataUtil.load(new File("test.html"), null, null);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        DataUtil.load(inputStream, null, null);
        fail("Expected IOException");
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        try {
            DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
            fail("Expected IOException");
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testBOM.html"));
        Document expectedDocument = DataUtil.load(new File("testBOM.html"), null, null);
        Document actualDocument = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertDocumentEquals(expectedDocument, actualDocument);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidBOM() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testInvalidBOM.html"));
        try {
            DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertByteBuffersEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024); // 1MB
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024);
        assertByteBuffersEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer expectedByteBuffer = DataUtil.readFileToByteBuffer(file);
        ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(file);
        assertByteBuffersEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer actualByteBuffer = DataUtil.emptyByteBuffer();
        assertByteBuffersEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(actualCharset);
        assertEquals(expectedCharset, actualCharset);

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = DataUtil.mimeBoundary();
        String actualBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, actualBoundary);
    }

    private void assertDocumentEquals(Document expectedDocument, Document actualDocument) {
        assertEquals(expectedDocument.title(), actualDocument.title());
        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    private void assertByteBuffersEquals(ByteBuffer expectedByteBuffer, ByteBuffer actualByteBuffer) {
        assertEquals(expectedByteBuffer.capacity(), actualByteBuffer.capacity());
        assertEquals(expectedByteBuffer.limit(), actualByteBuffer.limit());
        assertEquals(expectedByteBuffer.position(), actualByteBuffer.position());
        byte[] expectedBytes = new byte[expectedByteBuffer.limit()];
        expectedByteBuffer.get(expectedBytes);
        byte[] actualBytes = new byte[actualByteBuffer.limit()];
        actualByteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }
}