package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        // Test cases for load(File in, String charsetName, String baseUri)
        File file = new File("test.html");
        Document doc1 = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc1.baseUri());

        Document doc2 = DataUtil.load(file, "ISO-8859-1", "http://example.com");
        assertEquals("http://example.com", doc2.baseUri());

        // Test cases for load(InputStream in, String charsetName, String baseUri)
        InputStream inputStream1 = new FileInputStream("test.html");
        Document doc3 = DataUtil.load(inputStream1, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc3.baseUri());

        InputStream inputStream2 = new FileInputStream("test.html");
        Document doc4 = DataUtil.load(inputStream2, "ISO-8859-1", "http://example.com");
        assertEquals("http://example.com", doc4.baseUri());

        // Test cases for load(InputStream in, String charsetName, String baseUri, Parser parser)
        InputStream inputStream3 = new FileInputStream("test.html");
        Document doc5 = DataUtil.load(inputStream3, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals("http://example.com", doc5.baseUri());

        InputStream inputStream4 = new FileInputStream("test.html");
        Document doc6 = DataUtil.load(inputStream4, "ISO-8859-1", "http://example.com", Parser.htmlParser());
        assertEquals("http://example.com", doc6.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);

        // Verify that the output stream contains the same data as the input stream
        byte[] inputBytes = inputStream.readAllBytes();
        byte[] outputBytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(inputBytes.length, outputBytes.length);
        for (int i = 0; i < inputBytes.length; i++) {
            assertEquals(inputBytes[i], outputBytes[i]);
        }
    }

    @Test
    public void testParseByteData() {
        // Prepare test data
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test parseByteData method
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Verify that the document has the correct base URI
        assertEquals("http://example.com", doc.baseUri());

        // Verify that the document has the correct charset
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test cases for readToByteBuffer(InputStream inStream, int maxSize)
        InputStream inputStream1 = new FileInputStream("input.txt");
        ByteBuffer byteBuffer1 = DataUtil.readToByteBuffer(inputStream1, 0);

        InputStream inputStream2 = new FileInputStream("input.txt");
        ByteBuffer byteBuffer2 = DataUtil.readToByteBuffer(inputStream2, 1024);

        // Verify that the byte buffer contains the correct data
        byte[] expectedBytes = "test data".getBytes();
        assertEquals(expectedBytes.length, byteBuffer1.remaining());
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer1.get());
        }

        assertEquals(expectedBytes.length, byteBuffer2.remaining());
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer2.get());
        }

        // Test cases for readToByteBuffer(InputStream inStream)
        InputStream inputStream3 = new FileInputStream("input.txt");
        ByteBuffer byteBuffer3 = DataUtil.readToByteBuffer(inputStream3);

        // Verify that the byte buffer contains the correct data
        assertEquals(expectedBytes.length, byteBuffer3.remaining());
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer3.get());
        }
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("input.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Verify that the byte buffer contains the correct data
        byte[] expectedBytes = "test data".getBytes();
        assertEquals(expectedBytes.length, byteBuffer.remaining());
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer.get());
        }
    }

    /* Remaining test methods omitted for brevity */

}