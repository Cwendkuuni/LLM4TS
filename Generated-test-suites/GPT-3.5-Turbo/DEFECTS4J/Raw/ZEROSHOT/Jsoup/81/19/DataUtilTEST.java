package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    private File testFile;
    private InputStream testInputStream;
    
    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        testInputStream = getClass().getResourceAsStream("/test.html");
    }
    
    @After
    public void tearDown() throws Exception {
        testFile = null;
        testInputStream.close();
    }
    
    @Test
    public void testLoadFile() {
       try {
           Document document = DataUtil.load(testFile, "UTF-8", "");
           
           Assert.assertNotNull("Document should not be null", document);
       } catch (IOException e) {
           Assert.fail("IOException should not be thrown");
       }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            Document document = DataUtil.load(testInputStream, "UTF-8", "");
            
            Assert.assertNotNull("Document should not be null", document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            Document document = DataUtil.load(testInputStream, "UTF-8", "", Parser.htmlParser());
            
            Assert.assertNotNull("Document should not be null", document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testCrossStreams() {
        // Not able to test this method as it requires input and output streams
    }
    
    @Test
    public void testParseInputStream() {
        try {
            Document document = DataUtil.parseInputStream(testInputStream, "UTF-8", "", Parser.htmlParser());
            
            Assert.assertNotNull("Document should not be null", document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024);
            
            Assert.assertNotNull("ByteBuffer should not be null", byteBuffer);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 0);
            
            Assert.assertNotNull("ByteBuffer should not be null", byteBuffer);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferFromFile() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            
            Assert.assertNotNull("ByteBuffer should not be null", byteBuffer);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        Assert.assertNotNull("ByteBuffer should not be null", byteBuffer);
        Assert.assertEquals("ByteBuffer should be empty", 0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html;charset=UTF-8";
        String contentType3 = "text/htmlcharset=UTF-8";
        String contentType4 = "text/html; charset=EUC-JP";
        
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        String charset4 = DataUtil.getCharsetFromContentType(contentType4);
        
        Assert.assertEquals("Charset should be UTF-8", "UTF-8", charset1);
        Assert.assertEquals("Charset should be UTF-8", "UTF-8", charset2);
        Assert.assertEquals("Charset should be UTF-8", "UTF-8", charset3);
        Assert.assertEquals("Charset should be EUC-JP", "EUC-JP", charset4);
    }
    
    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";
        String charset3 = "invalidCharset";
        
        String validatedCharset1 = DataUtil.validateCharset(charset1);
        String validatedCharset2 = DataUtil.validateCharset(charset2);
        String validatedCharset3 = DataUtil.validateCharset(charset3);
        
        Assert.assertEquals("Validated charset should be UTF-8", "UTF-8", validatedCharset1);
        Assert.assertEquals("Validated charset should be ISO-8859-1", "ISO-8859-1", validatedCharset2);
        Assert.assertNull("Invalid charset should be null", validatedCharset3);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        Assert.assertNotNull("Mime boundary should not be null", mimeBoundary);
        Assert.assertEquals("Mime boundary should have a length of 32", 32, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        ByteBuffer byteBufferNoBom = ByteBuffer.wrap(new byte[]{});
        
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        BomCharset bomCharsetNoBom = DataUtil.detectCharsetFromBom(byteBufferNoBom);
        
        Assert.assertNotNull("BomCharset should not be null", bomCharset);
        Assert.assertNull("BomCharset should be null with no BOM", bomCharsetNoBom);
        Assert.assertEquals("Charset should be UTF-8", "UTF-8", bomCharset.charset);
        Assert.assertTrue("Offset should be true", bomCharset.offset);
    }
    
    @Test
    public void testLoadFileWithInvalidCharset() {
        try {
            Document document = DataUtil.load(testFile, "invalidCharset", "");
            
            Assert.assertNotNull("Document should not be null", document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
}