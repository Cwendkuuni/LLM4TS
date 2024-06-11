package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

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
import java.util.Locale;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(html, doc.html());
    }

    @Test
    public void testLoadWithDetectedCharset() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII'>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("US-ASCII", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testLoadWithInvalidCharset() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=X-FOO-BAR'>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "X-FOO-BAR", null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals(html, doc.html());
    }

    @Test
    public void testLoadWithBaseUri() throws Exception {
        String html1 = "<html>\n<head>\n<title>Page 1</title>\n</head>\n<body>\n<p><a href='page2.html'>Go to page 2</a></p>\n</body>\n</html>";
        String html2 = "<html>\n<head>\n<title>Page 2</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(html1.getBytes());
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(html2.getBytes());
        Document doc1 = DataUtil.load(inputStream1, null, "/page1.html");
        Document doc2 = DataUtil.load(inputStream2, null, "/page1.html");
        Element link = doc1.select("a[href]").first();
        String href = link.attr("href");
        Document doc3 = DataUtil.load(new FileInputStream(new File(href)), null, "/page1.html");
        assertEquals(html2, doc3.html());
    }

    @Test
    public void testParseByteData() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(html, doc.html());

        String charset = "US-ASCII";
        ByteArrayInputStream inputStreamWithMeta = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteDataWithMeta = DataUtil.readToByteBuffer(inputStreamWithMeta);
        doc = DataUtil.parseByteData(byteDataWithMeta, charset, null, Parser.htmlParser());
        assertEquals(html, doc.html());
        assertEquals(charset, doc.outputSettings().charset());

        charset = "UTF-16";
        ByteArrayInputStream inputStreamWithBom = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, html.getBytes()[0]});
        ByteBuffer byteDataWithBom = DataUtil.readToByteBuffer(inputStreamWithBom);
        doc = DataUtil.parseByteData(byteDataWithBom, null, null, Parser.htmlParser());
        assertEquals(html, doc.html());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expected = html.getBytes();
        byte[] actual = new byte[expected.length];
        byteData.get(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}