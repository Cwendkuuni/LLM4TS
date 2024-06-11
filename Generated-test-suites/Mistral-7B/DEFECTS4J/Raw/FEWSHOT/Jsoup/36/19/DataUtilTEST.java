package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset="));
    }

    @Test
    public void testLoad() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);

        assertEquals(htmlContent, document.body().html());
        assertEquals("UTF-8", document.outputSettings().charset());

        document = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertEquals(htmlContent, document.body().html());
        assertEquals("UTF-8", document.outputSettings().charset());

        document = DataUtil.load(inputStream, null, baseUri, Parser.xmlParser());
        assertEquals(htmlContent, document.body().html());
        assertEquals("UTF-8", document.outputSettings().charset());

        document = DataUtil.load(inputStream, "ISO-8859-1", baseUri, Parser.xmlParser());
        assertEquals(htmlContent, document.body().html());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        String baseUri = "http://test.com";

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(document);

        assertEquals(htmlContent, document.body().html());
        assertEquals("UTF-8", document.outputSettings().charset());

        String charset = "ISO-8859-1";
        String decodedContent = new String(byteData.array(), Charset.forName(charset).newDecoder());
        document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertEquals(decodedContent, document.body().html());
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(content.length(), byteData.limit());

        byte[] expectedBytes = content.getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);

        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(content.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
        assertEquals(5, byteData.limit());
        byte[] expectedBytes = new byte[]{'H', 'e', 'l', 'l', 'o'};
        byte[] actualBytes = new byte[5];
        byteData.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);

        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(content.length(), byteData.limit());
    }
}