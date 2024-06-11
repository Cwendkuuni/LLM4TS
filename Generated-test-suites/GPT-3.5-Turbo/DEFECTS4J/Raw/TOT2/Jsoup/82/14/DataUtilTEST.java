import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown while loading input stream");
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            InputStream inStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception thrown while loading input stream with parser");
        }
    }

    @Test
    public void testCrossStreams() {
        // Test case for crossStreams can be added here
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case for readToByteBuffer can be added here
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case for getCharsetFromContentType can be added here
    }

    @Test
    public void testMimeBoundary() {
        // Test case for mimeBoundary can be added here
    }

    @Test
    public void testParseInputStream() {
        // Test case for parseInputStream can be added here
    }

    // Additional test cases for error scenarios and edge cases can be added as needed

}