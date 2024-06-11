import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Create a temporary file with some contents
        File tempFile = File.createTempFile("test", ".html");
        tempFile.deleteOnExit();

        // Write some HTML content to the file
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        java.nio.file.Files.writeString(tempFile.toPath(), htmlContent);

        // Load the file using DataUtil.load()
        Document doc = DataUtil.load(tempFile, "UTF-8", "http://example.com");

        // Verify that the loaded document is not null
        assertNotNull(doc);

        // Verify that the loaded document contains the expected HTML content
        assertEquals(htmlContent, doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Create an InputStream with some HTML content
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        // Load the InputStream using DataUtil.load()
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        // Verify that the loaded document is not null
        assertNotNull(doc);

        // Verify that the loaded document contains the expected HTML content
        assertEquals(htmlContent, doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Create an InputStream with some XML content
        String xmlContent = "<root><element>Test</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        // Load the InputStream using DataUtil.load() with a custom parser
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());

        // Verify that the loaded document is not null
        assertNotNull(doc);

        // Verify that the loaded document contains the expected XML content
        assertEquals(xmlContent, doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create an InputStream with some content
        String content = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Create an OutputStream to capture the written content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Cross the streams using DataUtil.crossStreams()
        DataUtil.crossStreams(inputStream, outputStream);

        // Verify that the captured content matches the original content
        assertEquals(content, outputStream.toString());
    }

    @Test
    public void testParseByteData() {
        // TODO: Implement test for parseByteData method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Create an InputStream with some content
        String content = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Read the InputStream into a ByteBuffer using DataUtil.readToByteBuffer()
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Verify that the ByteBuffer contains the expected content
        assertEquals(content, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Create a temporary file with some contents
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        // Write some content to the file
        String content = "Hello, World!";
        java.nio.file.Files.writeString(tempFile.toPath(), content);

        // Read the file into a ByteBuffer using DataUtil.readFileToByteBuffer()
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(tempFile);

        // Verify that the ByteBuffer contains the expected content
        assertEquals(content, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() {
        // Get an empty ByteBuffer using DataUtil.emptyByteBuffer()
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Verify that the ByteBuffer has capacity 0
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // TODO: Implement test for getCharsetFromContentType method
    }

    @Test
    public void testMimeBoundary() {
        // TODO: Implement test for mimeBoundary method
    }

    @Test
    public void testDetectCharsetFromBom() {
        // TODO: Implement test for detectCharsetFromBom method
    }
}