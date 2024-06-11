import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        FileInputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions as needed
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        // Add more assertions as needed
    }

    // Add more test cases for the remaining methods

}