import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        // Test loading file
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        // Assert that the document is not null
        assertNotNull(doc);
        
        // Assert that the document base URI is correct
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        // Test loading input stream
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert that the document is not null
        assertNotNull(doc);
        
        // Assert that the document base URI is correct
        assertEquals(baseUri, doc.baseUri());
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Test loading input stream with parser
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert that the document is not null
        assertNotNull(doc);
        
        // Assert that the document base URI is correct
        assertEquals(baseUri, doc.baseUri());
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        // Test cross streams
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        
        // Assert that the output file exists
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        
        // Close the input and output streams
        in.close();
        out.close();
        
        // Delete the output file
        outputFile.delete();
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test read to byte buffer
        InputStream inputStream = new FileInputStream("example.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        // Assert that the byte buffer is not null
        assertNotNull(byteBuffer);
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Test read file to byte buffer
        File file = new File("example.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        // Assert that the byte buffer is not null
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        // Test empty byte buffer
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        // Assert that the byte buffer is not null
        assertNotNull(byteBuffer);
        
        // Assert that the byte buffer has size 0
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Test get charset from content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert that the charset is not null
        assertNotNull(charset);
        
        // Assert that the charset is correct
        assertEquals("UTF-8", charset);
    }
}