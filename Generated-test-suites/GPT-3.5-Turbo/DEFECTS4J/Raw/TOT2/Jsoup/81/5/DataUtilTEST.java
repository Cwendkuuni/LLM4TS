import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World!</p></body></html>".getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World!</p></body></html>".getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertEquals("http://example.com", document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Mock InputStream and OutputStream
        InputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        // Add assertions if applicable
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
        assertEquals(16, byteBuffer.capacity());
        // Add more assertions as needed
    }
}