package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String content = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        File file = new File("test.html");
        FileUtils.writeStringToFile(file, content, "UTF-8");

        Document doc = DataUtil.load(file, "UTF-8", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String content = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        String contentType2 = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType2));

        String contentType3 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charset = "UTF-8";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(Charset.forName(charset)));
        Document doc = DataUtil.parseByteData(byteBuffer, charset, null, Parser.htmlParser());

        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());

        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());

        Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = meta != null ? meta.attr("charset") : null;
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = content.getBytes("UTF-8");
        assertArrayEquals(expected, byteBuffer.array());
    }
}