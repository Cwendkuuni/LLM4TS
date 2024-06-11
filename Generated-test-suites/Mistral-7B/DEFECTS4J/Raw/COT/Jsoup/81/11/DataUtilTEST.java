package org.jsoup.helper; // update this package name if needed

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.jsoup.helper.DataUtil.charsetPattern;
import static org.jsoup.helper.DataUtil.defaultCharset;
import static org.jsoup.helper.DataUtil.firstReadBufferSize;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.parseInputStream;
import static org.jsoup.helper.DataUtil.readToByteBuffer;

public class DataUtilTest {
    // Basic test cases

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html"); // replace with an actual test file
        Document doc = DataUtil.load(testFile, defaultCharset, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, defaultCharset, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, defaultCharset, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.input.txt");
        File outputFile = new File("test.output.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);
        outputFile.delete();
        inputFile.delete();
    }

    // Edge cases and exception handling tests

    @Test(expected = IOException.class)
    public void testLoadFromFileFailure() throws IOException {
        File testFile = new File("inexistent_file.html");
        Document doc = DataUtil.load(testFile, defaultCharset, null);
        Assert.assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamFailure() throws IOException {
        String testHtml = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, null, null); // null charset will trigger an IOException
        } finally {
            inputStream.close();
        }
        Assert.assertNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamFailure() throws IOException {
        byte[] testHtmlBom = {0xFF, 0xFE, 0x00, 0x00}; // UTF-16 LE BOM
        InputStream inputStream = new ByteArrayInputStream(testHtmlBom);
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, defaultCharset, null, Parser.htmlParser());
        } finally {
            inputStream.close();
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputStreamCorruptedData() throws IOException {
        String testHtml = "<html><body><script src='javascript:void(0)'></script></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.parseInputStream(inputStream, defaultCharset, null, Parser.htmlParser());
            Assert.assertNotNull(doc);
        } finally {
            inputStream.close();
        }

        InputStream corruptedInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        corruptedInputStream.skip(testHtml.length() + 1); // skip one byte past the end of the data

        try {
            Document doc = DataUtil.parseInputStream(corruptedInputStream, defaultCharset, null, Parser.htmlParser());
            Assert.assertNull(doc);
        } finally {
            corruptedInputStream.close();
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(testData.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferFailure() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]); // empty input stream
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertFalse(byteBuffer.hasRemaining());
        inputStream.close();

        try {
            DataUtil.readToByteBuffer(inputStream); // will throw an IOException
        } finally {
            inputStream.close();
        }
    }
}