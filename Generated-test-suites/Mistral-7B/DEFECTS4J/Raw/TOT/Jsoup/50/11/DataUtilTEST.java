package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String fileContent = "This is a sample file content.";
        File sampleFile = new File("sample.html");
        try {
            sampleFile.createNewFile();
            FileWriter fileWriter = new FileWriter(sampleFile);
            fileWriter.write(fileContent);
            fileWriter.close();

            Document doc = DataUtil.load(sampleFile, "UTF-8", null);
            Assert.assertNotNull(doc);
            Assert.assertEquals(fileContent, doc.body().html());

            // Test error scenario: invalid charset
            Document docWithInvalidCharset = null;
            try {
                docWithInvalidCharset = DataUtil.load(sampleFile, "invalidCharset", null);
            } catch (HelperException e) {
                // Expected HelperException
                Assert.assertNotNull(e);
                Assert.assertEquals("Invalid charset: invalidCharset", e.getMessage());
            }
            Assert.assertNull(docWithInvalidCharset);

            sampleFile.delete();
        } catch (IOException | IllegalCharsetNameException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String sampleContent = "This is a sample input stream content.";
        InputStream inputStream = new ByteArrayInputStream(sampleContent.getBytes(StandardCharsets.UTF_8));

        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
            Assert.assertNotNull(doc);
            Assert.assertEquals(sampleContent, doc.body().html());
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Test error scenario: invalid charset
        InputStream inputStreamWithInvalidCharset = new ByteArrayInputStream("sample content".getBytes());
        try {
            DataUtil.load(inputStreamWithInvalidCharset, "invalidCharset", null, Parser.htmlParser());
            Assert.fail("Expected HelperException");
        } catch (HelperException e) {
            // Expected HelperException
            Assert.assertEquals("Invalid charset: invalidCharset", e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        String sampleContent = "This is a sample content.";
        byte[] sampleBytes = sampleContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(sampleBytes);

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(sampleContent, doc.body().html());

        // Test error scenario: invalid charset
        String invalidCharsetContent = "This is a sample content with invalid charset.";
        byte[] invalidCharsetBytes = invalidCharsetContent.getBytes();
        ByteBuffer invalidCharsetByteData = ByteBuffer.wrap(invalidCharsetBytes);

        try {
            DataUtil.parseByteData(invalidCharsetByteData, "invalidCharset", null, Parser.htmlParser());
            Assert.fail("Expected HelperException");
        } catch (HelperException e) {
            // Expected HelperException
            Assert.assertEquals("Invalid charset: invalidCharset", e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() {
        String inputContent = "This is a sample input content.";
        String outputContent = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream output = outputStream;

        try {
            DataUtil.crossStreams(inputStream, output);
            outputContent = outputStream.toString(StandardCharsets.UTF_8.name());
            Assert.assertEquals(inputContent, outputContent);
        } catch (IOException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
        }
    }
}