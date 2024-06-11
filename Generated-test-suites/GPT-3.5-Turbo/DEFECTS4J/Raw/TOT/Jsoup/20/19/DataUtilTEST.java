package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            // Test with existing file
            File file = new File("test.html"); // Assuming there is an existing test.html file
            Document document = DataUtil.load(file, "UTF-8", "");
            Assert.assertNotNull(document);

            // Test with non-existing file
            file = new File("non_existing.html");
            document = DataUtil.load(file, "UTF-8", "");
            Assert.assertNull(document);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            // Test with valid input stream and charset
            InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            Assert.assertNotNull(document);

            // Test with null input stream
            document = DataUtil.load(null, "UTF-8", "");
            Assert.assertNull(document);

            // Test with invalid charset
            inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
            document = DataUtil.load(inputStream, "invalid_charset", "");
            Assert.assertNotNull(document); // Should still create document even with invalid charset

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            // Test with valid input stream, charset, and parser
            InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            Assert.assertNotNull(document);

            // Test with null input stream and parser
            document = DataUtil.load(null, "UTF-8", "", Parser.htmlParser());
            Assert.assertNull(document);

            // Test with invalid charset and valid parser
            inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
            document = DataUtil.load(inputStream, "invalid_charset", "", Parser.htmlParser());
            Assert.assertNotNull(document); // Should still create document even with invalid charset

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        try {
            // Test with null charset name
            Document document = DataUtil.parseByteData(ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes()), null, "", Parser.htmlParser());
            Assert.assertNotNull(document);

            // Test with valid charset name
            document = DataUtil.parseByteData(ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes()), "UTF-8", "", Parser.htmlParser());
            Assert.assertNotNull(document);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            // Test with valid input stream
            InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            Assert.assertNotNull(byteBuffer);

            // Test with null input stream
            byteBuffer = DataUtil.readToByteBuffer(null);
            Assert.assertNull(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test with valid content type
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        Assert.assertEquals("UTF-8", charset);

        // Test with content type without charset
        charset = DataUtil.getCharsetFromContentType("text/html");
        Assert.assertNull(charset);

        // Test with null content type
        charset = DataUtil.getCharsetFromContentType(null);
        Assert.assertNull(charset);
    }
}