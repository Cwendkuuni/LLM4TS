import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = System.out;
        DataUtil.crossStreams(inputStream, outputStream);

        // Add more assertions as needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions as needed
    }

    // Add more test methods for the remaining public methods in DataUtil class

}