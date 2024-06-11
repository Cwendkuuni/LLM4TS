package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataUtilTest {
    
    private static File testFile;
    private static Document doc;
    
    @BeforeClass
    public static void setUp() {
        testFile = new File("test.html");
        doc = null;
    }
    
    @Test
    public void testLoadFromFile() {
        try {
            doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertEquals("Example Domain", doc.title());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream in = new FileInputStream(testFile);
            doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertEquals("Example Domain", doc.title());
            in.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream(testFile);
            doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertEquals("Example Domain", doc.title());
            in.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream(testFile);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            byte[] data = out.toByteArray();
            assertNotNull(data);
            assertTrue(data.length > 0);
            in.close();
            out.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteData() {
        try {
            InputStream in = new FileInputStream(testFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
            assertEquals("Example Domain", doc.title());
            in.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream in = new FileInputStream(testFile);
            int maxSize = 1024; // 1KB
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() <= maxSize);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            InputStream in = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.capacity() > 0);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validCharset);
    }
    
    @Test
    public void testValidateInvalidCharset() {
        String charset = "INVALID";
        String validCharset = DataUtil.validateCharset(charset);
        assertNull(validCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        File utf8File = new File("utf8.txt");
        try {
            byte[] bomBytes = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            OutputStream out = new FileOutputStream(utf8File);
            out.write(bomBytes);
            FileInputStream in = new FileInputStream(utf8File);
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            String charset = DataUtil.detectCharsetFromBom(byteData, null);
            assertEquals("UTF-8", charset);
            in.close();
            out.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDetectCharsetFromBomWithUnsupportedCharset() {
        File utf32beFile = new File("utf32be.txt");
        try {
            byte[] bomBytes = new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
            OutputStream out = new FileOutputStream(utf32beFile);
            out.write(bomBytes);
            FileInputStream in = new FileInputStream(utf32beFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            String charset = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
            assertEquals("UTF-32", charset);
            in.close();
            out.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDetectCharsetFromBomWithoutBom() {
        try {
            File noBomFile = new File("nobom.txt");
            FileOutputStream out = new FileOutputStream(noBomFile);
            String content = "No BOM content";
            out.write(content.getBytes());
            FileInputStream in = new FileInputStream(noBomFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            String charset = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
            assertEquals("UTF-8", charset);
            in.close();
            out.close();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}