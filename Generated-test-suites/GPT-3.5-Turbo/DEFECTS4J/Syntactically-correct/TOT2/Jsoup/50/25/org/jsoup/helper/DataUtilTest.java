import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;
    private String baseUri = "http://example.com";

    @Before
    public void setUp() {
        testFile = new File("testFile.html");
        testInputStream = getClass().getResourceAsStream("/testFile.html");
    }

    @Test
    public void testLoadFile() throws IOException {
        DataUtil.load(testFile, "UTF-8", baseUri);
        // Add additional assertions if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        DataUtil.load(testInputStream, "UTF-8", baseUri);
        // Add additional assertions if needed
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        DataUtil.load(testInputStream, "UTF-8", baseUri, Parser.htmlParser());
        // Add additional assertions if needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Add test for crossStreams method
    }

    // Additional test cases for other methods can be added here

}