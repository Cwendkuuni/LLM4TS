import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Prepare test inputs
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();

        // Call the method
        DataUtil.crossStreams(inputStream, outputStream);

        // Validate the results
        String expectedOutput = "This is a test input stream";
        String actualOutput = outputStream.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
    }
}