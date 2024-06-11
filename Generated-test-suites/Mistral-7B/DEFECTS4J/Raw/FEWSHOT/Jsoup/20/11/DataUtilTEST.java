package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document);

        File file = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        document = DataUtil.load(file, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document);

        // Test with invalid charset
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new File("test.html"), "", null));
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document);

        // Test with no charset specified
        byteData = ByteBuffer.wrap(html.getBytes());
        document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.length(), byteData.limit());
    }

    @Test
    public void testDetectCharsetFromHtml() throws IOException {
        String htmlWithUtf8Charset = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/></head><body>Hello World</body></html>";
        ByteBuffer byteDataWithUtf8Charset = ByteBuffer.wrap(htmlWithUtf8Charset.getBytes());
        Document documentWithUtf8Charset = DataUtil.parseByteData(byteDataWithUtf8Charset, null, null, Parser.htmlParser());
        assertEquals("UTF-8", documentWithUtf8Charset.outputSettings().charset());

        String htmlWithGB2312Charset = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=GB2312\"/></head><body>Hello World</body></html>";
        ByteBuffer byteDataWithGB2312Charset = ByteBuffer.wrap(htmlWithGB2312Charset.getBytes());
        Document documentWithGB2312Charset = DataUtil.parseByteData(byteDataWithGB2312Charset, null, null, Parser.htmlParser());
        assertEquals("GB2312", documentWithGB2312Charset.outputSettings().charset());

        String htmlWithNoCharset = "<html><head></head><body>Hello World</body></html>";
        ByteBuffer byteDataWithNoCharset = ByteBuffer.wrap(htmlWithNoCharset.getBytes());
        Document documentWithNoCharset = DataUtil.parseByteData(byteDataWithNoCharset, null, null, Parser.htmlParser());
        assertEquals("UTF-8", documentWithNoCharset.outputSettings().charset());
    }
}