import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    private File testFile;
    private InputStream testInputStream;
    private String testBaseUri;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        testBaseUri = "http://example.com";
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", testBaseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        testInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(testInputStream, "UTF-8", testBaseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        testInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(testInputStream, "UTF-8", testBaseUri, Parser.htmlParser());
        assertNotNull(document);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputByteArray = "test input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputByteArray);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        byte[] outputByteArray = outputStream.toByteArray();
        assertEquals(inputByteArray.length, outputByteArray.length);
        
        for (int i = 0; i < inputByteArray.length; i++) {
            assertEquals(inputByteArray[i], outputByteArray[i]);
        }
    }
    
    @Test
    public void testParseByteDataWithCharsetFromMetaTag() {
        String testCharset = "UTF-8";
        String testContent = "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">";
        byte[] testData = testContent.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(testData);
        
        Document document = DataUtil.parseByteData(byteBuffer, null, testBaseUri, Parser.htmlParser());
        assertNotNull(document);
        
        assertEquals(testCharset, document.outputSettings().charset().displayName());
    }
    
    @Test
    public void testParseByteDataWithCharsetFromHeader() {
        String testCharset = "UTF-8";
        String testContent = "Content-Type: text/html;charset=UTF-8";
        byte[] testData = testContent.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(testData);
        
        Document document = DataUtil.parseByteData(byteBuffer, null, testBaseUri, Parser.htmlParser());
        assertNotNull(document);
        
        assertEquals(testCharset, document.outputSettings().charset().displayName());
    }
    
    @Test
    public void testParseByteDataWithCharsetFromInput() {
        String testCharset = "UTF-8";
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(testCharset);
        ByteBuffer byteBuffer = ByteBuffer.wrap(testDataBytes);
        
        Document document = DataUtil.parseByteData(byteBuffer, null, testBaseUri, Parser.htmlParser());
        assertNotNull(document);
        
        assertEquals(testCharset, document.outputSettings().charset().displayName());
    }
    
    @Test
    public void testParseByteDataWithBOM() {
        String testData = "\uFEFF<html><body></body></html>";
        String expectedData = "<html><body></body></html>";
        byte[] testDataBytes = testData.getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.wrap(testDataBytes);
        
        Document document = DataUtil.parseByteData(byteBuffer, null, testBaseUri, Parser.htmlParser());
        assertNotNull(document);
        
        assertEquals(expectedData, document.html());
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        byte[] inputByteArray = "test input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputByteArray);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] outputByteArray = byteBuffer.array();
        
        assertEquals(inputByteArray.length, outputByteArray.length);
        
        for (int i = 0; i < inputByteArray.length; i++) {
            assertEquals(inputByteArray[i], outputByteArray[i]);
        }
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] inputByteArray = "test input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputByteArray);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        byte[] outputByteArray = byteBuffer.array();
        
        assertEquals(5, outputByteArray.length);
        
        for (int i = 0; i < 5; i++) {
            assertEquals(inputByteArray[i], outputByteArray[i]);
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        byte[] expectedData = "test input".getBytes();
        FileOutputStream fileOutputStream = new FileOutputStream(testFile);
        fileOutputStream.write(expectedData);
        fileOutputStream.close();
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        byte[] outputData = byteBuffer.array();
        
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], outputData[i]);
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertTrue(byteBuffer.capacity() == 0);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String charset = "UTF-8";
        String contentType = "text/html; charset=UTF-8";
        
        String extractedCharset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(charset, extractedCharset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        
        String extractedCharset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(extractedCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}