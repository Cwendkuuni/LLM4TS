import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");

        // Assert
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("test.html");

        // Act
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");

        // Assert
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());

        // Cleanup
        in.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("test.xml");

        // Act
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());

        // Assert
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());

        // Cleanup
        in.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");

        // Act
        DataUtil.crossStreams(in, out);

        // Assert
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());

        // Cleanup
        in.close();
        out.close();
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("test.html");

        // Act
        Document doc = DataUtil.parseInputStream(in, "UTF-8", "http://example.com", Parser.htmlParser());

        // Assert
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());

        // Cleanup
        in.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("input.txt");

        // Act
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);

        // Assert
        assertNotNull(buffer);
        assertEquals(1024, buffer.limit());

        // Cleanup
        in.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        assertNotNull(buffer);

        // Cleanup
        file.delete();
    }

    @Test
    public void testMimeBoundary() {
        // Act
        String boundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}