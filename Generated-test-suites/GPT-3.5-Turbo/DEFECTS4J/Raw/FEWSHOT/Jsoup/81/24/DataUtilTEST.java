package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad() throws Exception {
        String file = "test.html";
        Document doc = DataUtil.load(new File(file), "UTF-8", "");
        Document expected = Jsoup.parse(new File(file), "UTF-8");

        assertEquals(expected.html(), doc.html());
    }

    @Test
    public void testLoadWithInputStream() throws Exception {
        String file = "test.html";
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Document expected = Jsoup.parse(new File(file), "UTF-8");

        assertEquals(expected.html(), doc.html());
    }

    @Test
    public void testLoadWithParser() throws Exception {
        String file = "test.html";
        Document doc = DataUtil.load(new File(file), "UTF-8", "", Parser.htmlParser());
        Document expected = Jsoup.parse(new File(file), "UTF-8");

        assertEquals(expected.html(), doc.html());
    }

    @Test
    public void testCrossStreams() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("test", outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws Exception {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Document expected = Jsoup.parse(html);

        assertEquals(expected.html(), doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(html, new String(buffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFileToByteBuffer() throws Exception {
        String file = "test.html";
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(new File(file));
        Document doc = Jsoup.parse(new File(file), "UTF-8");

        assertEquals(doc.html(), new String(buffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() throws Exception {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();

        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() throws Exception {
        String contentType = "text/html; charset=Invalid-Charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() throws Exception {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals(charset, validatedCharset);
    }
    
    @Test
    public void testValidateCharsetWithInvalidCharset() throws Exception {
        String charset = "Invalid-Charset";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }
    
    @Test
    public void testMimeBoundary() throws Exception {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}