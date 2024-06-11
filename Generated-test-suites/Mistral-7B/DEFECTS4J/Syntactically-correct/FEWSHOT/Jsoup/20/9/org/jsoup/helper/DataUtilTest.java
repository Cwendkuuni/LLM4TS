package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    private static final String HTML_WITH_UTF_8_CHARSET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
    private static final String HTML_WITH_GB2312_CHARSET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";

    @Test
    public void testLoad() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_WITH_UTF_8_CHARSET.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertTrue(doc.text().equals("Test"));

        inputStream = new ByteArrayInputStream(HTML_WITH_GB2312_CHARSET.getBytes());
        doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertTrue(doc.text().equals("Test"));
    }

    @Test
    public void testParseByteData() {
        String htmlWithUtf8Charset = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>Test</p></body></html>";
        String htmlWithGb2312Charset = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body><p>Test</p></body></html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(htmlWithUtf8Charset));
        DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertTrue(outputStream.toString().equals(htmlWithUtf8Charset));

        byteData = Charset.forName("UTF-8").encode(Charset.forName("gb2312").decode(htmlWithGb2312Charset));
        DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertTrue(outputStream.toString().equals(htmlWithGb2312Charset));

        byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(htmlWithUtf8Charset));
        String foundCharset = DataUtil.getCharsetFromContentType(htmlWithUtf8Charset.substring(0, htmlWithUtf8Charset.indexOf(" ")));
        byteData.rewind();
        DataUtil.parseByteData(byteData, foundCharset, null, Parser.htmlParser());
        assertTrue(outputStream.toString().equals(htmlWithUtf8Charset));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = "Test".getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertTrue(new String(actualBytes).equals(new String(expectedBytes)));
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertNull(DataUtil.getCharsetFromContentType(null));
        String contentType = "text/html; charset=UTF-8";
        assertTrue(DataUtil.getCharsetFromContentType(contentType).equals("UTF-8"));
        contentType = "text/html; charset=gb2312";
        assertTrue(DataUtil.getCharsetFromContentType(contentType).equals("gb2312"));
    }
}