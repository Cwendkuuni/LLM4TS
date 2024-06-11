package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("test.html"); // replace with valid file path
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        String decodedData = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals("Test data", decodedData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Error data".getBytes());
        DataUtil.readToByteBuffer(inputStream); // intentionally left empty
    }

    @Test
    public void testParseByteData() throws IOException {
        String testHtml = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithCharsetFromMeta() throws IOException {
        String testHtml = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'><body><h1>Test</h1></body></html>", document.html());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Error data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser()); // intentionally left empty
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testParseByteDataUnsupportedEncodingException() throws IOException {
        String testHtml = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-17'><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser()); // intentionally left empty
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataIllegalArgumentException() throws IOException {
        String testHtml = "<html><head><meta http-equiv='Content-Type' content='text/html'><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser()); // intentionally left empty
    }
}