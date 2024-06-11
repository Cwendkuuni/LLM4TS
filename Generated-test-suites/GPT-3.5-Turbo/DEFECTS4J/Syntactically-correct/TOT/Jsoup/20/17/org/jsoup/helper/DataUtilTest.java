import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private static final String TEST_FILE_PATH = "test.html";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com/";

    private File testFile;
    private FileInputStream inStream;

    @Before
    public void setUp() throws IOException {
        testFile = createTestFile();
        inStream = new FileInputStream(testFile);
    }

    @After
    public void tearDown() throws IOException {
        if (inStream != null) {
            inStream.close();
        }
        if (testFile != null) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document result = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        assertNotNull(result);
        // Add additional assertions for the loaded document
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document result = DataUtil.load(inStream, CHARSET_NAME, BASE_URI);
        assertNotNull(result);
        // Add additional assertions for the loaded document
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.load(inStream, CHARSET_NAME, BASE_URI, parser);
        assertNotNull(result);
        // Add additional assertions for the loaded document
    }

    // Add additional test cases for edge cases and error scenarios

    private File createTestFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileWriter writer = new FileWriter(file);

        // Write test data to the file
        writer.write("<html><body><h1>Test</h1></body></html>");

        writer.close();
        return file;
    }
}