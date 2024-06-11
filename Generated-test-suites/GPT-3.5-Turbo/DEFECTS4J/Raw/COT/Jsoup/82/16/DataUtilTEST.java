import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    
    // Test load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        // Test loading a valid file
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        
        // Test loading a non-existing file
        File nonExistingFile = new File("non_existing.html");
        Document docNonExisting = DataUtil.load(nonExistingFile, charsetName, baseUri);
        assertNotNull(docNonExisting);
    }

    // Test load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        // Test loading a valid input stream
        Document doc = DataUtil.load(input, charsetName, baseUri);
        assertNotNull(doc);
        
        // Test loading a null input stream
        Document docNullInput = DataUtil.load(null, charsetName, baseUri);
        assertNotNull(docNullInput);
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        // Test loading a valid input stream with custom parser
        Document doc = DataUtil.load(input, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    // Test crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        OutputStream output = new ByteArrayOutputStream();
        
        // Test cross-streams with valid input and output streams
        DataUtil.crossStreams(input, output);
        assertNotNull(output);
    }

    // Test readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        
        // Test reading to byte buffer with a valid input stream and maximum size
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, maxSize);
        assertNotNull(buffer);
    }

    // Test getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        // Test extracting charset from content type
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}