import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() {
        testFile = new File("testFile.html");
        testInputStream = new FileInputStream(testFile);
    }

    @After
    public void tearDown() {
        testInputStream.close();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", "https://example.com");
        // Add assertion here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", "https://example.com");
        // Add assertion here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(testInputStream, "UTF-8", "https://example.com", Parser.htmlParser());
        // Add assertion here
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        // Add assertion here
    }

    // Add more test cases for other methods as needed

}