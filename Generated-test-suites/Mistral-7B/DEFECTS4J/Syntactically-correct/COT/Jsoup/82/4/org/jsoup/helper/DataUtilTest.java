import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class})
public class DataUtilTest {

    private static final String TEST_FILE_PATH = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com/";
    private static final int TEST_BUFFER_SIZE = 1024 * 32;

    private DataUtil dataUtil;
    private InputStream testInputStream;
    private OutputStream testOutputStream;
    private File testFile;
    private Random random;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        testInputStream = mock(InputStream.class);
        testOutputStream = mock(OutputStream.class);
        testFile = new File(TEST_FILE_PATH);
        random = new Random();
    }

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("inputFile.html");
        when(testInputStream.available()).thenReturn(inputFile.length());
        when(testInputStream.read()).thenAnswer(invocation -> invocation.callRealMethod());
        when(testInputStream.markSupported()).thenReturn(true);

        Document document = dataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
        PowerMockito.verifyStatic(DataUtil.class).parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        when(testInputStream.available()).thenReturn(2048);
        when(testInputStream.read()).thenAnswer(invocation -> invocation.callRealMethod());
        when(testInputStream.markSupported()).thenReturn(true);

        Document document = dataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
        PowerMockito.verifyStatic(DataUtil.class).parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        when(testInputStream.available()).thenReturn(2048);
        when(testInputStream.read()).thenAnswer(invocation -> invocation.callRealMethod());
        when(testInputStream.markSupported()).thenReturn(true);

        Document document = dataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        Assert.assertNotNull(document);
        PowerMockito.verifyStatic(DataUtil.class).parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        OutputStream outputStream = new FileOutputStream(new File("outputFile.html"));

        dataUtil.crossStreams(inputStream, outputStream);

        byte[] buffer = new byte[1024];
        int len;

        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.close();
        outputStream.close();

        File outputFile = new File("outputFile.html");
        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, TEST_BUFFER_SIZE);
        String charsetName = TEST_CHARSET;
        String baseUri = TEST_BASE_URI;
        Parser parser = Parser.htmlParser();

        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, TEST_BUFFER_SIZE);
        Assert.assertNotNull(buffer);

        inputStream.close();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(buffer);
        Assert.assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(Charset.forName(TEST_CHARSET), Charset.forName(charset));

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validCharset = DataUtil.validateCharset(cs);
        Assert.assertEquals(cs, validCharset);

        cs = "invalidCharset";
        validCharset = DataUtil.validateCharset(cs);
        Assert.assertNull(validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertFalse(boundary.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testDetectCharsetFromBomUtf32() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDetectCharsetFromBomUtf16BE() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bom = new byte[]{ (byte) 0xFE, (byte) 0xFF };
        when(testInputStream.read(bom)).thenReturn(2);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertEquals(false, bomCharset.offset);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDetectCharsetFromBomUtf16LE() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bom = new byte[]{ (byte) 0xFF, (byte) 0xFE };
        when(testInputStream.read(bom)).thenReturn(2);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertEquals(true, bomCharset.offset);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDetectCharsetFromBomUtf8() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bom = new byte[]{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        when(testInputStream.read(bom)).thenReturn(3);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertEquals(true, bomCharset.offset);

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomNotSupported() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] bom = new byte[]{ (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF };
        when(testInputStream.read(bom)).thenReturn(4);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertNull(bomCharset);

        inputStream.close();
    }
}