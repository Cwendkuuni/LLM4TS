package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.jsoup.helper.DataUtil.defaultCharset;
import static org.jsoup.helper.DataUtil.detectCharsetFromBom;
import static org.jsoup.helper.DataUtil.emptyByteBuffer;
import static org.jsoup.helper.DataUtil.firstReadBufferSize;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.readToByteBuffer;
import static org.jsoup.helper.DataUtil.validateCharset;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    

    @Test
    public void testLoadEmptyFile() {
        Document doc = DataUtil.load(new File("empty.html"), null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        File tempFile = File.createTempFile("test_load", null);
        Files.write(tempFile.getAbsoluteFile().toPath(), html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(tempFile, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());

        tempFile.deleteOnExit();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String html = "<?xml version='1.0' encoding='GBK'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        File tempFile = File.createTempFile("test_load", null);
        Files.write(tempFile.getAbsoluteFile().toPath(), html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(tempFile, "GBK", null);
        Assert.assertNull(doc);

        tempFile.deleteOnExit();
    }

    @Test
    public void testParseInputStreamUTF8() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStreamWithHeaderCharset() throws IOException {
        String html = "<?xml version='1.0' encoding='GBK'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetHeader() throws IOException {
        String html = "<?xml version='1.0' encoding='GBK'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }

        Assert.assertNull(doc);
    }

    @Test(expected = UncheckedIOException.class)
    public void testParseInputStreamWithIOError() throws IOException, InterruptedException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        } catch (IOException e) {
            throw new UncheckedIOException(e.getCause());
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] data = new byte[1024];
        Random random = new Random();
        random.nextBytes(data);

        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, data.length);

        Assert.assertArrayEquals(data, byteBuffer.array());
    }

    @Test
    public void testValidateCharsetDefault() {
        Assert.assertEquals(DataUtil.defaultCharset, DataUtil.validateCharset(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharsetNull() {
        DataUtil.validateCharset(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharsetEmpty() {
        DataUtil.validateCharset("");
    }

    @Test
    public void testValidateCharsetSupported() {
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharsetUnsupported() {
        DataUtil.validateCharset("GBK");
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBomUTF8() {
        ByteBuffer bom = DataUtil.emptyByteBuffer();
        bom.put((byte) 0xFF);
        bom.put((byte) 0xFF);
        bom.put((byte) 0xEF);
        bom.put((byte) 0xBB);
        bom.put((byte) 0xBF);
        bom.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(bom);
        Assert.assertEquals("UTF-8", charset.charset);
        Assert.assertFalse(charset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16BE() {
        ByteBuffer bom = DataUtil.emptyByteBuffer();
        bom.put((byte) 0xFE);
        bom.put((byte) 0xFF);
        bom.put((byte) 0x00);
        bom.put((byte) 0x00);
        bom.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(bom);
        Assert.assertEquals("UTF-16", charset.charset);
        Assert.assertFalse(charset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16LE() {
        ByteBuffer bom = DataUtil.emptyByteBuffer();
        bom.put((byte) 0xFF);
        bom.put((byte) 0xFE);
        bom.put((byte) 0x00);
        bom.put((byte) 0x00);
        bom.flip();

        BomCharset charset = DataUtil.detectCharsetFromBom(bom);
        Assert.assertEquals("UTF-16", charset.charset);
        Assert.assertTrue(charset.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html; charset=UTF-8";
        Assert.assertNull(DataUtil.getCharsetFromContentType(contentType.replace("UTF-8", "")));
    }

    
}