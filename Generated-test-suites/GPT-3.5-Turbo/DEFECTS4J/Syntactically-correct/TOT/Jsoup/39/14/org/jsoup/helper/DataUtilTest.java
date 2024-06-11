
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFile_returnsDocument() throws IOException {
        File validFile = new File("valid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(validFile, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoad_withInvalidFile_throwsFileNotFoundException() throws IOException {
        File invalidFile = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        DataUtil.load(invalidFile, charsetName, baseUri);
    }

    @Test
    public void testLoad_withInputStream_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream("valid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_withInputStreamAndParser_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream("valid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_withCharsetNull_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_withCharsetFoundInMeta_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_withCharsetFoundInContentType_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_withUtf8Bom_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 60, 104, 116, 109, 108, 62});
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBuffer_withNegativeMaxSize_throwsIllegalArgumentException() throws IOException {
        InputStream inputStream = new FileInputStream("valid.html");
        int maxSize = -1;
        
        DataUtil.readToByteBuffer(inputStream, maxSize);
    }

    @Test
    public void testReadToByteBuffer_withZeroMaxSize_returnsByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("valid.html");
        int maxSize = 0;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_withPositiveMaxSize_returnsByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("valid.html");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_returnsNull() {
        String contentType = "text/html";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset_returnsNull() {
        String contentType = "text/html; charset=InvalidCharset";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }
}

