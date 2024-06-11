import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    private static File file;
    private static InputStream inputStream;
    private static String baseUri;
    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        file = new File("test.html");
        inputStream = null; // Initialize with a valid input stream
        baseUri = "https://www.example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile_ValidCharset_ValidBaseUri() throws IOException {
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.baseUri().equals(baseUri));
        // Add more assertions for typical use cases and edge cases
    }

    @Test
    public void testLoadFile_NullCharset_ValidBaseUri() {
        try {
            Document doc = DataUtil.load(file, null, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Add assertions for error scenarios
        }
    }

    // Complete the rest of the test cases for each method in DataUtil

}