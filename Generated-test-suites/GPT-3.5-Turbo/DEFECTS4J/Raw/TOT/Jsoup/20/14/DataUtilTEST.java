import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFile_typical() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), "");
        assertNotNull(document);
    }
    
    @Test
    public void testLoadFile_noCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, "");
        assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoadFile_invalidFile() throws IOException {
        File file = new File("invalid.html");
        DataUtil.load(file, StandardCharsets.UTF_8.name(), "");
    }
    
    @Test
    public void testLoadInputStream_typical() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream_noCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoadInputStream_invalidStream() throws IOException {
        InputStream inputStream = null;
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
    }
    
    @Test
    public void testLoadInputStreamWithParser_typical() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "", Parser.htmlParser());
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser_noCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, null, "", Parser.htmlParser());
        assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParser_invalidStream() throws IOException {
        InputStream inputStream = null;
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "", Parser.htmlParser());
    }
    
    @Test
    public void testParseByteData_typical() {
        // TODO: Implement this test case
    }
    
    @Test
    public void testParseByteData_noCharset() {
        // TODO: Implement this test case
    }
    
    @Test
    public void testGetCharsetFromContentType_typical() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_noCharset() {
        String contentType = "text/html;";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_noContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
}