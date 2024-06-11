
package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    // Test case for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(document);
            // Test if the loaded document has the correct base URI
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() {
        String input = "<html><body><h1>Hello, World!</h1></body></html>";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            assertNotNull(document);
            // Test if the loaded document has the correct base URI
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamWithParser() {
        String input = "<html><body><h1>Hello, World!</h1></body></html>";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            assertNotNull(document);
            // Test if the loaded document has the correct base URI
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for crossStreams(InputStream in, OutputStream out)
    @Test
    public void testCrossStreams() {
        String input = "Hello, World!";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            DataUtil.crossStreams(inputStream, outputStream);
            
            String output = outputStream.toString();
            
            assertEquals(input, output);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for parseInputStream(InputStream input, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseInputStream() {
        String input = "<html><body><h1>Hello, World!</h1></body></html>";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            
            assertNotNull(document);
            // Test if the parsed document has the correct base URI
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for readToByteBuffer(InputStream inStream, int maxSize)
    @Test
    public void testReadToByteBuffer() {
        String input = "Hello, World!";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            int maxSize = input.length();
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            assertNotNull(byteBuffer);
            
            byte[] byteArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteArray);
            
            String output = new String(byteArray);
            
            assertEquals(input, output);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBufferFull() {
        String input = "Hello, World!";
        
        try {
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteBuffer);
            
            byte[] byteArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteArray);
            
            String output = new String(byteArray);
            
            assertEquals(input, output);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for readFileToByteBuffer(File file)
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            
            assertNotNull(byteBuffer);
            
            FileInputStream fis = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fis.read(fileData);
            fis.close();
            
            byte[] byteBufferArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteBufferArray);
            
            assertArrayEquals(fileData, byteBufferArray);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    // Test case for emptyByteBuffer()
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }

    // Test case for getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }

    // Test case for validateCharset(String cs)
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        
        String validCharset = DataUtil.validateCharset(charset);
        
        assertEquals(charset, validCharset);
    }

    // Test case for mimeBoundary()
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}
