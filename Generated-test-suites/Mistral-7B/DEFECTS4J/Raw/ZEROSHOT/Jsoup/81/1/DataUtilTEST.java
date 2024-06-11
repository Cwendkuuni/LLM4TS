import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static Document expectedDocument;
    private static final String TEST_FILE_PATH = "testFile.html";

    @BeforeClass
    public static void setUp() throws IOException {
        String testContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        File testFile = new File(TEST_FILE_PATH);
        Files.write(testFile.getAbsoluteFile().toPath(), testContent.getBytes(StandardCharsets.UTF_8));

        expectedDocument = DataUtil.parseInputStream(new FileInputStream(testFile), "UTF-8", null, Parser.htmlParser());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(new File(TEST_FILE_PATH), "UTF-8", null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String testContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testContent = "Test content to be read from the input stream and written to the output stream.";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(testContent, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        Document document = DataUtil.load(new File(TEST_FILE_PATH), "ISO-8859-1", null);
        fail("Expected IOException was not thrown");
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "ISO-8859-1", null);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
        assertNotNull(document);
    }
}