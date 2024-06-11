
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Test
    public void testLoadWithFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadWithCustomParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
            OutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            String result = outputStream.toString(Charset.forName("UTF-8"));
            Assert.assertEquals("Test", result);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assert.assertNotNull(byteBuffer);
            Assert.assertTrue(byteBuffer.remaining() > 0);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}
