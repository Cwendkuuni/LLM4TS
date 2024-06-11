package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File testFile = new File("testFile.html");
        byte[] expectedBytes = expectedHtml.getBytes(StandardCharsets.UTF_8);
        FileUtils.writeByteArrayToFile(testFile, expectedBytes);

        Document document = DataUtil.load(testFile, null);
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File testFile = new File("testFile.html");
        byte[] expectedBytes = expectedHtml.getBytes(StandardCharsets.UTF_8);
        FileUtils.writeByteArrayToFile(testFile, expectedBytes);

        Document document = DataUtil.load(testFile, "UTF-8");
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null);
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8");
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, "GBK"); // Invalid charset
            Assert.fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals("Test Stream", outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(("InvalidCharset: " + expectedHtml).getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.parseInputStream(inputStream, "GBK", null, Parser.htmlParser());
            Assert.fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        byte[] expectedBytes = (expectedHtml + "\uFEFF").getBytes();
        InputStream inputStream = new ByteArrayInputStream(expectedBytes);

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testParseInputStreamWithBOMAndSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        byte[] expectedBytes = (expectedHtml + "\uFEFF").getBytes();
        InputStream inputStream = new ByteArrayInputStream(expectedBytes);

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 32);

        Assert.assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Hello World</body></html>";
        File testFile = new File("testFile.html");
        byte[] expectedBytes = expectedHtml.getBytes(StandardCharsets.UTF_8);
        FileUtils.writeByteArrayToFile(testFile, expectedBytes);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        Assert.assertEquals(expectedBytes, byteBuffer.array());
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedBoundary.length(), DataUtil.boundaryLength);
    }
}