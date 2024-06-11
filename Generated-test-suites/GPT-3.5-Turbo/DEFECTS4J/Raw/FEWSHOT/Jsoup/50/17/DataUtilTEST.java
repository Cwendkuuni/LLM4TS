package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        // Test case 1: Test loading a file with default charset
        String baseUri = "https://www.example.com";
        try {
            Document doc = DataUtil.load(new File("test.html"), null, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test case 2: Test loading a file with specified charset
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        // Test case 1: Test loading an input stream with default charset
        String baseUri = "https://www.example.com";
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, null, baseUri);
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test case 2: Test loading an input stream with specified charset
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", baseUri);
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        // Test case 1: Test loading an input stream with default charset and HTML parser
        String baseUri = "https://www.example.com";
        try {
            InputStream in = new FileInputStream("test.html");
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(in, null, baseUri, parser);
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test case 2: Test loading an input stream with specified charset and XML parser
        try {
            InputStream in = new FileInputStream("test.xml");
            Parser parser = Parser.xmlParser();
            Document doc = DataUtil.load(in, "UTF-8", baseUri, parser);
            assertNotNull(doc);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testCrossStreams() {
        // Test case: Test crossing input and output streams
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            byte[] expectedBytes = Files.readAllBytes(Paths.get("input.txt"));
            byte[] actualBytes = out.toByteArray();
            assertArrayEquals(expectedBytes, actualBytes);
            in.close();
            out.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testParseByteData() {
        // Test case 1: Test parsing byte data with default charset and base URI
        String baseUri = "https://www.example.com";
        String charsetName = null;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("test.html"));
            ByteBuffer byteData = ByteBuffer.wrap(bytes);
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test case 2: Test parsing byte data with specified charset and base URI
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("test.html"));
            ByteBuffer byteData = ByteBuffer.wrap(bytes);
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        // Test case 1: Test reading an input stream to byte buffer with unlimited maxSize
        try {
            InputStream in = new FileInputStream("input.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 0);
            assertNotNull(byteBuffer);
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test case 2: Test reading an input stream to byte buffer with capped maxSize
        try {
            InputStream in = new FileInputStream("input.txt");
            int maxSize = 1024;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
            assertNotNull(byteBuffer);
            assertEquals(maxSize, byteBuffer.capacity());
            in.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadToByteBufferFromFile() {
        // Test case: Test reading a file to byte buffer
        try {
            File file = new File("test.html");
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        // Test case: Test creating an empty byte buffer
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1: Test getting charset from content type header with charset=EUC-JP
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);
        
        // Test case 2: Test getting charset from content type header with charset=gb2312
        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);
        
        // Test case 3: Test getting charset from content type header with no charset
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        // Test case: Test generating a random mime boundary
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}