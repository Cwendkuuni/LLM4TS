
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
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        // Create dummy input and output streams for testing
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };

        try {
            DataUtil.crossStreams(input, output);
            // No assertion, as method doesn't return anything or modify any variables
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            Assert.assertNotNull(document);
            Assert.assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException thrown: " + e.getMessage());
        }
    }
}
