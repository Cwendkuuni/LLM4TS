import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private final String baseUri = "https://example.com";

    @Test
    public void testLoadWithFile() throws IOException {
        // Create a temporary file and write some HTML content
        File tempFile = File.createTempFile("test", ".html");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write("<html><body><h1>Hello World!</h1></body></html>");
        fileWriter.close();

        // Load the file using DataUtil.load and assert the loaded document is not null
        Document document = DataUtil.load(tempFile, StandardCharsets.UTF_8.name(), baseUri);
        Assert.assertNotNull(document);

        // Assert the content of the loaded document is correct
        Assert.assertEquals("Hello World!", document.select("h1").text());

        // Delete the temporary file
        tempFile.delete();
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        // Create an input stream with some HTML content
        String htmlContent = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        // Load the input stream using DataUtil.load and assert the loaded document is not null
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        Assert.assertNotNull(document);

        // Assert the content of the loaded document is correct
        Assert.assertEquals("Hello World!", document.select("h1").text());

        // Close the input stream
        inputStream.close();
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        // Create an input stream with some XML content
        String xmlContent = "<root><item>Hello World!</item></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());

        // Load the input stream using DataUtil.load with a custom parser and assert the loaded document is not null
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), baseUri, Parser.xmlParser());
        Assert.assertNotNull(document);

        // Assert the content of the loaded document is correct
        Assert.assertEquals("Hello World!", document.select("item").text());

        // Close the input stream
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create an input stream with some binary content
        byte[] binaryContent = {0x41, 0x42, 0x43, 0x44}; // ASCII values for "ABCD"
        InputStream inputStream = new ByteArrayInputStream(binaryContent);

        // Create an output stream to write the input stream to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Cross the input and output streams using DataUtil.crossStreams
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert the content of the output stream is correct
        Assert.assertArrayEquals(binaryContent, outputStream.toByteArray());

        // Close the input and output streams
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithCharsetFromContentType() {
        // Test parsing a charset from a content type header with a valid charset
        String contentTypeWithCharset = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentTypeWithCharset);
        Assert.assertEquals("UTF-8", charset);

        // Test parsing a charset from a content type header with an unsupported charset
        String contentTypeWithUnsupportedCharset = "text/html; charset=INVALID_CHARSET";
        String unsupportedCharset = DataUtil.getCharsetFromContentType(contentTypeWithUnsupportedCharset);
        Assert.assertNull(unsupportedCharset);

        // Test parsing a charset from a content type header without a charset
        String contentTypeWithoutCharset = "text/html";
        String withoutCharset = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);
        Assert.assertNull(withoutCharset);
    }

    @Test
    public void testMimeBoundary() {
        // Generate a mime boundary using DataUtil.mimeBoundary
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert the length of the mime boundary is correct
        Assert.assertEquals(32, mimeBoundary.length());

        // Assert the mime boundary contains only valid characters
        Assert.assertTrue(mimeBoundary.matches("[\\-_0-9a-zA-Z]+"));
    }
}