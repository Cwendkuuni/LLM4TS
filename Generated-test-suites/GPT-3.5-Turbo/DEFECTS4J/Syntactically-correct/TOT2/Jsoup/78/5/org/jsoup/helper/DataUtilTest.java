import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown for loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown for loading input stream");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown for loading input stream with parser");
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            OutputStream outputStream = new FileOutputStream("output.txt");
            DataUtil.crossStreams(inputStream, outputStream);
            assertTrue(new File("output.txt").exists());
        } catch (IOException e) {
            fail("IOException should not be thrown for crossing streams");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown for reading to byte buffer");
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown for reading to byte buffer without max size");
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File("data.bin");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown for reading file to byte buffer");
        }
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
}