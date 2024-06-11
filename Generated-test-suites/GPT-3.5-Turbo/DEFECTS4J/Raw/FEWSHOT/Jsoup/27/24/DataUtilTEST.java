import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        // Create temporary file with test data
        File tempFile = File.createTempFile("tempTestFile", ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("<html><body><p>Hello World!</p></body></html>");
        writer.close();
        
        // Test loading file
        Document doc = DataUtil.load(tempFile, "UTF-8", "");
        assertEquals("Document should contain the correct HTML", "<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.html());
        
        // Clean up temporary file
        tempFile.delete();
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        // Create input stream with test data
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World!</p></body></html>".getBytes());
        
        // Test loading input stream
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals("Document should contain the correct HTML", "<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testLoadCustomParser() throws IOException {
        // Create input stream with test data
        InputStream inputStream = new ByteArrayInputStream("<html><body><p>Hello World!</p></body></html>".getBytes());
        
        // Test loading input stream with custom parser
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertEquals("Document should contain the correct XML", "<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testParseByteData() {
        // Create byte buffer with test data
        String testData = "<html><body><p>Hello World!</p></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(testData);
        
        // Test parsing byte data
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertEquals("Document should contain the correct HTML", "<html>\n <head></head>\n <body>\n  <p>Hello World!</p>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Create temporary file with test data
        File tempFile = File.createTempFile("tempTestFile", ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("<html><body><p>Hello World!</p></body></html>");
        writer.close();
        
        // Create input stream from file
        FileInputStream inputStream = new FileInputStream(tempFile);
        
        // Test reading to byte buffer
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        String data = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals("Data should match the content of the file", "<html><body><p>Hello World!</p></body></html>", data);
        
        // Clean up temporary file
        tempFile.delete();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type header
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Charset should be UTF-8", "UTF-8", charset);
        
        // Test getting charset from invalid content type header
        String invalidContentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(invalidContentType);
        assertNull("Charset should be null as it is not specified", charset);
    }
}