import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "testData.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_INPUT_STREAM_CONTENT = "Test content.";

    private Document testDocument;
    private InputStream testInputStream;
    private OutputStream testOutputStream;
    private byte[] testFileContent;

    @Before
    public void setup() throws IOException {
        testFileContent = TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET));
        File testFile = new File(TEST_FILE_Path);
        testFile.createNewFile();
        FileWriter testFileWriter = new FileWriter(testFile);
        testFileWriter.write(TEST_INPUT_STREAM_CONTENT);
        testFileWriter.close();
        testInputStream = new FileInputStream(testFile);
        testOutputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(new File(TEST_FILE_PATH), TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testFileContent);
        Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testFileContent);
        Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream testInput = mock(InputStream.class);
        OutputStream testOutput = mock(OutputStream.class);
        ByteArrayInputStream testByteArrayInputStream = new ByteArrayInputStream(testFileContent);

        when(testInput.read()).thenReturn(testFileContent[0]);
        when(testInput.available()).thenReturn(testFileContent.length);

        DataUtil.crossStreams(testByteArrayInputStream, testOutput);

        byte[] expectedOutput = new byte[testFileContent.length];
        testOutput.read(expectedOutput);

        byte[] actualOutput = testOutputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWhenFileIsEmpty() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWhenCharsetIsNotSupported() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(testFileContent);
        DataUtil.parseInputStream(testInputStream, "X-CHARSET", TEST_BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithMetaTag() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(testFileContent.concat("<meta charset=\"ISO-8859-1\">"));
        Document document = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(testFileContent.concat("<xml encoding=\"ISO-8859-1\"/>"));
        Document document = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=foo"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(null));
        assertEquals(null, DataUtil.validateCharset("foobar"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWhenNoBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(testFileContent);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        byte[] expectedByteBuffer = new byte[testFileContent.length];
        System.arraycopy(testFileContent, 0, expectedByteBuffer, 0, testFileContent.length);
        byteBuffer.rewind();
        assertArrayEquals(expectedByteBuffer, byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(testFileContent);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 10);
        byte[] expectedByteBuffer = new byte[10];
        System.arraycopy(testFileContent, 0, expectedByteBuffer, 0, 10);
        byteBuffer.rewind();
        assertArrayEquals(expectedByteBuffer, byteBuffer.array());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        byte[] expectedByteBuffer = new byte[testFile.length()];
        Files.readAllBytes(testFile.toPath(), expectedByteBuffer);
        byteBuffer.rewind();
        assertArrayEquals(expectedByteBuffer, byteBuffer.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }

    @After
    public void teardown() throws IOException {
        testFile.delete();
    }
}