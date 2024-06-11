import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical Use Case
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);

        // Edge Case - Empty file
        doc = DataUtil.load(new File("empty.html"), "UTF-8", "http://example.com");
        assertNull(doc);

        // Error Scenario - File not found
        assertThrows(IOException.class, () -> DataUtil.load(new File("notfound.html"), "UTF-8", "http://example.com"));
    }

    @Test
    public void testLoadInputStream() {
        // Typical Use Case
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);

        // Edge Case - Null input stream
        assertThrows(IOException.class, () -> DataUtil.load(null, "UTF-8", "http://example.com"));

        // Error Scenario - Invalid charset
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(new FileInputStream("test.html"), "InvalidCharset", "http://example.com"));
    }

    @Test
    public void testLoadWithParser() {
        // Typical Use Case
        InputStream in = getClass().getResourceAsStream("test.xml");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);

        // Edge Case - Not XML format
        assertThrows(IOException.class, () -> DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com", Parser.xmlParser()));

        // Error Scenario - Parser null
        assertThrows(NullPointerException.class, () -> DataUtil.load(new FileInputStream("test.xml"), "UTF-8", "http://example.com", null));
    }

    @Test
    public void testCrossStreams() {
        // Typical Use Case
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream("test.html"), out);
        assertNotNull(out.toByteArray());

        // Edge Case - Empty input stream
        assertDoesNotThrow(() -> DataUtil.crossStreams(new FileInputStream("empty.html"), out));

        // Error Scenario - Invalid input stream
        assertThrows(IOException.class, () -> DataUtil.crossStreams(new FileInputStream("notfound.html"), out));
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical Use Case
        ByteBuffer buf = DataUtil.readFileToByteBuffer(new File("test.html"));
        assertNotNull(buf);

        // Edge Case - Empty file
        buf = DataUtil.readFileToByteBuffer(new File("empty.html"));
        assertNotNull(buf);

        // Error Scenario - File not found
        assertThrows(IOException.class, () -> DataUtil.readFileToByteBuffer(new File("notfound.html")));
    }

    @Test
    public void testParseByteData() {
        // Typical Use Case
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);

        // Edge Case - Null ByteBuffer
        assertThrows(IOException.class, () -> DataUtil.parseByteData(null, "UTF-8", "http://example.com", Parser.htmlParser()));

        // Error Scenario - Invalid Charset
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(emptyByteBuffer, "InvalidCharset", "http://example.com", Parser.htmlParser()));
    }
}