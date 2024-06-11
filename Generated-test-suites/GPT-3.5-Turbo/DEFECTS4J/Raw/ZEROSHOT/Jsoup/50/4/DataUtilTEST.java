import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        Assert.assertNotNull(outputStream);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        int maxSize = 10;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        Assert.assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        Assert.assertNotNull(byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        Assert.assertEquals(0, byteBuffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}