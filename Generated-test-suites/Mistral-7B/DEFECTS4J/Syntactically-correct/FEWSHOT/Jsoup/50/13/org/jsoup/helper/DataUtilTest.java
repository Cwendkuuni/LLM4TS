package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16LE\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String charsetName = "UTF-16LE";
        String baseUri = null;
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("UTF-16LE", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String baseUri = null;
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=XYZ\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        String baseUri = null;
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseByteData(byteData, "XYZ", baseUri, parser);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = null;

        File testFile = new File(filePath);
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        assertEquals(Charset.forName(charsetName), doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16LE\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-16LE";
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("UTF-16LE", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=XYZ\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, "XYZ", baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/xml\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("xml", doc.tagName());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "test.html";
        String charsetName = "UTF-16LE";
        String baseUri = null;

        File testFile = new File(filePath);
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithEmptyData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = null;
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertTrue(doc == null || doc.body() == null || doc.body().text().isEmpty());
    }

    @Test
    public void testLoadInputStreamWithMalformedData() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16LE\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "<html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-16LE";
        String baseUri = null;

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            fail("Expected IOException with malformed data.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithLargeData() throws IOException {
        String testData = "Test data with a size of 10 MB.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10 * 1024 * 1024);
        outputStream.write(testData.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = null;

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(testData, doc.body().text());
    }
}