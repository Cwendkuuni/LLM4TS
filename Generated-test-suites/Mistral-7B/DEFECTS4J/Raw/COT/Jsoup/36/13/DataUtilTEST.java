import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final String CHARSET_GB2312 = "GB2312";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CHARSET_INVALID = "INVALID_CHARSET";
    private static final int BUFFER_SIZE = 0x20000; // ~130K.
    private static final int MAX_SIZE = 1024;

    @Test
    public void testLoadFile() throws IOException {
        String testContent = "Test content for file load.";
        File testFile = new File("testFile.txt");
        testFile.createNewFile();
        try (FileWriter testWriter = new FileWriter(testFile)) {
            testWriter.write(testContent);
        }

        Document result = DataUtil.load(testFile, CHARSET_UTF_8, BASE_URI);
        Assert.assertNotNull(result);
        Assert.assertEquals(testContent, result.body().text());
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testContent = "Test content for input stream load.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes())) {
            Document result = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI);
            Assert.assertNotNull(result);
            Assert.assertEquals(testContent, result.body().text());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testContent = "Test content for input stream load with parser.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes())) {
            Document result = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(result);
            Assert.assertEquals(testContent, result.body().text());
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String testContent = "Test content for parseByteData.";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes(Charset.forName(CHARSET_UTF_8)));
        Document result = DataUtil.parseByteData(byteData, CHARSET_UTF_8, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(result);
        Assert.assertEquals(testContent, result.body().text());

        // Test case for meta charset
        String testContentWithMetaCharset = "Test content with meta charset.";
        ByteBuffer byteDataWithMetaCharset = ByteBuffer.wrap(testContentWithMetaCharset.getBytes(Charset.forName(CHARSET_UTF_8)));
        testContentWithMetaCharset += "\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + CHARSET_GB2312 + "\">\n</head>\n<body>\n<p>" + testContentWithMetaCharset + "</p>\n</body>\n</html>";
        Document resultWithMetaCharset = DataUtil.parseByteData(byteDataWithMetaCharset, null, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(resultWithMetaCharset);
        Assert.assertEquals(testContentWithMetaCharset, resultWithMetaCharset.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test content for readToByteBuffer.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes())) {
            ByteBuffer result = DataUtil.readToByteBuffer(inputStream);
            byte[] expectedByteArray = outputStream.toByteArray(testContent.length());
            ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedByteArray);
            Assert.assertArrayEquals(expectedByteArray, result.array());
            Assert.assertEquals(expectedByteBuffer.limit(), result.limit());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String result = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", result);

        String contentTypeWithoutCharset = "text/html";
        result = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);
        Assert.assertNull(result);

        String contentTypeWithInvalidCharset = "text/html; charset=INVALID_CHARSET";
        result = DataUtil.getCharsetFromContentType(contentTypeWithInvalidCharset);
        Assert.assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File testFile = new File("testFileWithIOException.txt");
        Document result = DataUtil.load(testFile, CHARSET_UTF_8, BASE_URI);
        Assert.fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithIOException() throws IOException {
        InputStream testInputStream = new FileInputStream(new File("testFileWithIOException.txt"));
        ByteBuffer result = DataUtil.readToByteBuffer(testInputStream);
        Assert.fail("Expected IOException");
    }
}