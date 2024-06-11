package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Document expectedDocument = parseHtmlFile("test.html");

        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Document expectedDocument = parseHtmlString(htmlContent);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String htmlContent = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=UTF-8\"/>" +
                "</head>" +
                "<body>" +
                "<div>Test</div>" +
                "</body>" +
                "</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForXml();

        Document expectedDocument = parseXmlString(htmlContent);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String actualContent = outputStream.toString();
        assertEquals(source, actualContent);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;

        DataUtil.crossStreams(inputStream, outputStream);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutputStream() {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes("UTF-8"));
        OutputStream outputStream = null;

        DataUtil.crossStreams(inputStream, outputStream);
    }

    @Test
    public void testParseByteData() {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForHtml();

        Document expectedDocument = parseHtmlString(htmlContent);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithUnsupportedCharset() {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("GBK")); // unsupported charset
        String charsetName = "GBK";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForHtml();

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(actualDocument);
    }

    @Test
    public void testParseByteDataWithEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForHtml();

        Document actualDocument = DataUtil.parseByteData(emptyByteBuffer, charsetName, baseUri, parser);
        assertNull(actualDocument);
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferFromInputStreamWithMaxSize() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        int maxSize = 50;

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(htmlContent.getBytes(0, Math.min(maxSize, htmlContent.length())));
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferFromFile() throws IOException {
        String htmlContent = "<html><body></body></html>";
        File inputFile = new File("test.html");
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputFile);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferFromFileWithMaxSize() throws IOException {
        String htmlContent = "<html><body></body></html>";
        File inputFile = new File("test.html");
        int maxSize = 50;

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputFile, maxSize);

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[Math.min(maxSize, htmlContent.length())]);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        File inputFile = new File("test.html");

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(inputFile);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "-----TestBoundary";
        String actualBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary, actualBoundary);
    }

    private Document parseHtmlString(String htmlContent) {
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForHtml();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        return document;
    }

    private Document parseXmlString(String xmlContent) {
        ByteBuffer byteData = ByteBuffer.wrap(xmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/path/to/base";
        Parser parser = DataUtil.parserForXml();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        return document;
    }
}