package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFileWithDefaultCharset.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFileWithSpecifiedCharset.html");
        Document doc = DataUtil.load(file, "UTF-16");
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"Shift_JIS\"></head><body>Test</body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"Shift_JIS\"></head><body>Test</body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, "Shift_JIS");
        assertEquals("Shift_JIS", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String testData = "<!DOCTYPE html public \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, null);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"Shift_JIS\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        testData = "<!DOCTYPE html public \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        byteData = ByteBuffer.wrap(testData.getBytes());
        doc = DataUtil.parseByteData(byteData, "Shift_JIS", null, Parser.xmlParser());
        assertEquals("Shift_JIS", doc.outputSettings().charset());

        testData = "<!DOCTYPE html public \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        byteData = ByteBuffer.wrap(testData.getBytes());
        doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        testData = "<!DOCTYPE html public \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=EUC-JP\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        byteData = ByteBuffer.wrap(testData.getBytes());
        doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertFalse(doc.outputSettings().charset().isEmpty());
        assertTrue(doc.outputSettings().charset().length() > 3);
        assertTrue(doc.outputSettings().charset().startsWith("EUC-"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType("text/html; charset=UTF-16"));
        assertEquals("UTF-16BE", DataUtil.getCharsetFromContentType("text/html; charset=UTF-16BE"));
        assertEquals("UTF-16LE", DataUtil.getCharsetFromContentType("text/html; charset=UTF-16LE"));
        assertEquals("Shift_JIS", DataUtil.getCharsetFromContentType("text/html; charset=Shift_JIS"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/plain"));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes()), 0);
        byte[] buffer = outStream.toByteArray();
        ByteBuffer expectedByteData = ByteBuffer.wrap(buffer);
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test Data";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(testData.getBytes()), 10);
        byte[] buffer = outStream.toByteArray();
        ByteBuffer expectedByteData = ByteBuffer.wrap(new byte[] {(byte) 0x00, (byte) 0x05, (byte) 0x4D, (byte) 0x65, (byte) 0x73, (byte) 0x74});
        assertEquals(expectedByteData, byteData);
    }
}