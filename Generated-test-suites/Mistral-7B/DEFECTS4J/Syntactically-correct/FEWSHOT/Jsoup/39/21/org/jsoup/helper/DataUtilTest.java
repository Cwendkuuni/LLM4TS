package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String testFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("test.html");
        FileOutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write(testFileContent.getBytes());
        outputStream.close();

        Document result = DataUtil.load(testFile, "gb2312", null);

        assertEquals(testFileContent, result.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream testInputStream = new ByteArrayInputStream(testStreamContent.getBytes());

        Document result = DataUtil.load(testInputStream, "gb2312", null);

        assertEquals(testStreamContent, result.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream testInputStream = new ByteArrayInputStream(testStreamContent.getBytes());

        Document result = DataUtil.load(testInputStream, null);

        assertEquals(testStreamContent, result.html());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testByteDataContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer testByteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document result = DataUtil.parseByteData(testByteData, "UTF-8", null, Parser.htmlParser());

        assertEquals(testByteDataContent, result.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testByteDataContent = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer testByteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document result = DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());

        assertEquals(testByteDataContent, result.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testInputStreamContent = "Test";

        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(testInputStream);

        byte[] expectedByteData = testInputStreamContent.getBytes();

        byte[] byteData = new byte[expectedByteData.length];
        result.get(byteData);

        assertArrayEquals(expectedByteData, byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testInputStreamContent = "Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890Test1234567890";

        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(testInputStream, 10);

        byte[] expectedByteData = new byte[10];

        testInputStream.read(expectedByteData);

        byte[] byteData = new byte[10];
        result.get(byteData);

        assertArrayEquals(expectedByteData, byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=GB2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; CHARSET=gb2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset= gb2312 "));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312 "));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312; foo=bar"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=\"gb2312\""));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset='gb2312'"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; charset=gb2312"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer testByteData = ByteBuffer.wrap("Test".getBytes());

        DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullParser() {
        ByteBuffer testByteData = ByteBuffer.wrap("Test".getBytes());

        DataUtil.parseByteData(testByteData, "UTF-8", null, null);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer testByteData = ByteBuffer.wrap("Test".getBytes());

        ByteArrayInputStream inputStreamSpy = Mockito.spy(new ByteArrayInputStream("Test".getBytes()));
        Mockito.doReturn(inputStreamSpy).when(DataUtil).readToByteBuffer(Mockito.any(InputStream.class));

        try {
            DataUtil.parseByteData(testByteData, "invalidCharset", null, Parser.htmlParser());
            assertFalse(true);
        } catch (IOException e) {
            // expected
        }

        Mockito.verify(inputStreamSpy).close();
    }

    @Test
    public void testParseByteDataWithMetaHttpEquivTag() throws IOException {
        String testByteDataContent = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">Test";

        ByteBuffer testByteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document result = DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());

        assertEquals("Test", result.html());
        assertEquals("gb2312", result.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithMetaCharsetTag() throws IOException {
        String testByteDataContent = "<meta charset=\"gb2312\">Test";

        ByteBuffer testByteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document result = DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());

        assertEquals("Test", result.html());
        assertEquals("gb2312", result.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithUTF8BOMIndicator() throws IOException {
        String testByteDataContent = "\uFEFFTest";

        ByteBuffer testByteData = ByteBuffer.wrap(testByteDataContent.getBytes());

        Document result = DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());

        assertEquals("Test", result.html());
        assertEquals("UTF-8", result.outputSettings().charset());
    }
}