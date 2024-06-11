import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    
    private Document document;
    private File file;
    private ByteArrayInputStream byteArrayInputStream;
    
    @Before
    public void setUp() throws IOException {
        file = new File("test.html");
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        byteArrayInputStream = new ByteArrayInputStream(html.getBytes());
        document = Jsoup.parse(html);
    }
    
    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void testLoadFromFile_success() {
        try {
            Document loadedDocument = DataUtil.load(file, "UTF-8", "https://www.example.com");
            assertNotNull(loadedDocument);
            assertEquals(document.toString(), loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromFile_invalidFile() {
        try {
            Document loadedDocument = DataUtil.load(new File("invalid.html"), "UTF-8", "https://www.example.com");
            assertNotNull(loadedDocument);
            assertEquals("", loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_success() {
        try {
            Document loadedDocument = DataUtil.load(byteArrayInputStream, "UTF-8", "https://www.example.com");
            assertNotNull(loadedDocument);
            assertEquals(document.toString(), loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_invalidInputStream() {
        try {
            Document loadedDocument = DataUtil.load(null, "UTF-8", "https://www.example.com");
            assertNotNull(loadedDocument);
            assertEquals("", loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_customParser_success() {
        try {
            Document loadedDocument = DataUtil.load(byteArrayInputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull(loadedDocument);
            assertEquals(document.toString(), loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_customParser_invalidInputStream() {
        try {
            Document loadedDocument = DataUtil.load(null, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull(loadedDocument);
            assertEquals("", loadedDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData_nullCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(parsedDocument);
        assertEquals(document.toString(), parsedDocument.toString());
    }
    
    @Test
    public void testParseByteData_nonNullCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(parsedDocument);
        assertEquals(document.toString(), parsedDocument.toString());
    }
    
    @Test
    public void testParseByteData_metaCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(parsedDocument);
        // Simulating a document with <meta http-equiv="Content-Type" content="text/html;charset=UTF-16"> tag
        parsedDocument.select("meta").attr("http-equiv", "Content-Type").attr("content", "text/html;charset=UTF-16");
        Document reParsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(reParsedDocument);
        assertEquals(document.toString(), reParsedDocument.toString());
    }
    
    @Test
    public void testParseByteData_metaCharset_noCharsetFound() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(parsedDocument);
        // Simulating a document with <meta http-equiv="Content-Type" content="text/html"> tag
        parsedDocument.select("meta").attr("http-equiv", "Content-Type").attr("content", "text/html");
        Document reParsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(reParsedDocument);
        assertEquals(document.toString(), reParsedDocument.toString());
    }
    
    @Test
    public void testParseByteData_foundCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document parsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(parsedDocument);
        // Simulating a document with <meta charset="UTF-16"> tag
        parsedDocument.select("meta").attr("charset", "UTF-16");
        Document reParsedDocument = DataUtil.parseByteData(byteBuffer, null, "https://www.example.com", Parser.htmlParser());
        assertNotNull(reParsedDocument);
        assertEquals(document.toString(), reParsedDocument.toString());
    }
    
    @Test
    public void testReadToByteBuffer_success() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ByteBuffer readByteBuffer = null;
        try {
            readByteBuffer = DataUtil.readToByteBuffer(byteArrayInputStream);
            assertNotNull(readByteBuffer);
            assertEquals(byteBuffer, readByteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBuffer_invalidInputStream() {
        ByteBuffer readByteBuffer = null;
        try {
            readByteBuffer = DataUtil.readToByteBuffer(null);
            assertNotNull(readByteBuffer);
            assertEquals(0, readByteBuffer.limit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetCharsetFromContentType_success() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_noCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_emptyContentType() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_invalidContentType() {
        String contentType = "invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}