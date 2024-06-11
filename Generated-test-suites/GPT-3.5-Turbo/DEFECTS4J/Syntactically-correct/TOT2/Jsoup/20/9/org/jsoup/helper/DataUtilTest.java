import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Typical Use Case
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
        
        // Edge Case - Empty file
        File emptyFile = new File("empty.html");
        Document emptyDoc = DataUtil.load(emptyFile, "UTF-8", "https://www.example.com");
        assertNotNull(emptyDoc);
        
        // Error Scenario - Non-existent file
        File nonExistentFile = new File("non_existent.html");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistentFile, "UTF-8", "https://www.example.com"));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Typical Use Case
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
        
        // Edge Case - Empty input stream
        InputStream emptyInputStream = new FileInputStream("empty.html");
        Document emptyDoc = DataUtil.load(emptyInputStream, "UTF-8", "https://www.example.com");
        assertNotNull(emptyDoc);
        
        // Error Scenario - Null input stream
        InputStream nullInputStream = null;
        assertThrows(IOException.class, () -> DataUtil.load(nullInputStream, "UTF-8", "https://www.example.com"));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Typical Use Case
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        
        // Edge Case - Different charset
        InputStream inputStreamGBK = new FileInputStream("test_gbk.html");
        Document docGBK = DataUtil.load(inputStreamGBK, "GB2312", "https://www.example.com", Parser.htmlParser());
        assertNotNull(docGBK);
    }

    @Test
    public void testParseByteData() {
        // Typical Use Case
        byte[] data = "Test data".getBytes();
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(data), "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        
        // Edge Case - Null charset
        Document docNullCharset = DataUtil.parseByteData(ByteBuffer.wrap(data), null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(docNullCharset);
        
        // Error Scenario - Invalid charset
        assertThrows(Exception.class, () -> DataUtil.parseByteData(ByteBuffer.wrap(data), "InvalidCharset", "https://www.example.com", Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical Use Case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        
        // Edge Case - No charset in content type
        String noCharsetContentType = "text/html";
        String noCharset = DataUtil.getCharsetFromContentType(noCharsetContentType);
        assertNull(noCharset);
    }
}