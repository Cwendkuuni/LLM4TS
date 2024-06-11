import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test input".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test input".getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    // Additional test methods for the remaining public methods in DataUtil class can be added here
}