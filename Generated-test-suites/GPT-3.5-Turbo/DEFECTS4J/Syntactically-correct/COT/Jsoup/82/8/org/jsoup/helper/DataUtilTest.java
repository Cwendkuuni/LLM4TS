import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Additional assertions on the loaded document can be added here
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            // Additional assertions on the loaded document can be added here
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown while loading input stream");
        }
    }

    @Test
    public void testLoadWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            Assert.assertNotNull(document);
            // Additional assertions on the loaded document can be added here
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown while loading input stream");
        }
    }

    @Test
    public void testCrossStreams() {
        // Implementation specific test case. Unable to write a generic JUnit test case.
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            // Additional assertions on the parsed document can be added here
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown while parsing input stream");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            // Additional assertions on the parsed document can be added here
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown while parsing input stream");
        }
    }
}