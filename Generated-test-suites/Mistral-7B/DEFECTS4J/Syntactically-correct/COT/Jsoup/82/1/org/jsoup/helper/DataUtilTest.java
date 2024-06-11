package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FileDocument;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        assertTrue(outputFile.length() > 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        try {
            Document document = DataUtil.parseInputStream(inputStream, "INVALID_CHARSET", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
        assertTrue(document.childNodeSize() == 0);
    }

    @Test
    public void testParseInputStreamWithMetaCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-16", document.outputSettings().charset().name());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String htmlContent = "<html xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xml:base=\"http://tempuri.org/\"><head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedBytes = htmlContent.getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        byteBuffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInterruptedInput() throws InterruptedException, IOException {
        Thread thread = new Thread(() -> {
            try (InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes())) {
                byte[] buffer = new byte[bufferSize];
                int index = 0;
                while (index < bufferSize) {
                    int bytesRead = inputStream.read(buffer, index, bufferSize - index);
                    index += bytesRead;
                    Thread.sleep(100);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

        thread.join(500);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(null, 1024 * 32);
        fail("Expected IOException");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";
        String charset3 = "UTF-16";
        String charset4 = "UTF-32";
        String charset5 = "INVALID_CHARSET";

        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset2, DataUtil.validateCharset(charset2));
        assertEquals(charset3, DataUtil.validateCharset(charset3));
        assertEquals(charset1, DataUtil.validateCharset(null));
        assertEquals(charset1, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(charset5));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testFile = "test.txt";
        File file = new File(testFile);
        byte[] expectedBomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] expectedBomUtf16BE = {(byte) 0xFE, (byte) 0xFF};
        byte[] expectedBomUtf16LE = {(byte) 0xFF, (byte) 0xFE};
        byte[] expectedBomUtf32BE = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] expectedBomUtf32LE = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};

        try (InputStream inputStream = new FileInputStream(file)) {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 4);

            DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
            if (bomCharset != null) {
                byte[] bom = byteBuffer.array();
                switch (bomCharset.charset) {
                    case "UTF-8":
                        assertArrayEquals(expectedBomUtf8, bom);
                        break;
                    case "UTF-16":
                        if (bomCharset.offset) {
                            assertArrayEquals(expectedBomUtf16BE, bom);
                        } else {
                            assertArrayEquals(expectedBomUtf16LE, bom);
                        }
                        break;
                    case "UTF-32":
                        if (bomCharset.offset) {
                            assertArrayEquals(expectedBomUtf32BE, bom);
                        } else {
                            assertArrayEquals(expectedBomUtf32LE, bom);
                        }
                        break;
                    default:
                        fail("Unexpected character set detected.");
                }
            } else {
                fail("No bom found.");
            }
        }
    }
}