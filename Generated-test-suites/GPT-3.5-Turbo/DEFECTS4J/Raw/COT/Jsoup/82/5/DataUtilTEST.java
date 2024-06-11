import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, "UTF-8", ""); // replace with actual charset and baseUri
        assertNotNull(doc);
        // add assertions to check the functionality of the loaded Document
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html"); // replace with actual file path
        Document doc = DataUtil.load(inputStream, "UTF-8", ""); // replace with actual charset and baseUri
        assertNotNull(doc);
        // add assertions to check the functionality of the loaded Document
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml"); // replace with actual file path
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser()); // replace with actual charset and baseUri
        assertNotNull(doc);
        // add assertions to check the functionality of the loaded Document with custom parser
    }

    @Test
    public void testCrossStreams() throws IOException {
        // write test case for crossStreams method
    }

    @Test
    public void testParseInputStream() throws IOException {
        // write test case for parseInputStream method
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // write test case for readToByteBuffer method
    }

    @Test
    public void testMimeBoundary() {
        // write test case for mimeBoundary method
    }

    @Test
    public void testGetCharsetFromContentType() {
        // write test case for getCharsetFromContentType method
    }

    @Test
    public void testValidateCharset() {
        // write test case for validateCharset method
    }

    @Test
    public void testDetectCharsetFromBom() {
        // write test case for detectCharsetFromBom method
    }
}