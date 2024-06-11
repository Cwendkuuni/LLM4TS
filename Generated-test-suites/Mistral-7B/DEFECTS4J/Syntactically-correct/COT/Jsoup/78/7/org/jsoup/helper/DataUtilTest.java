package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("validFile.html"); // Replace with an existing file with a UTF-8 encoding.
        Document document = DataUtil.load(file, charsetName, "");
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithNullCharset() {
        File file = new File("validFile.html");
        DataUtil.load(file, null, "");
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName(charsetName)));
        Document document = DataUtil.load(inputStream, charsetName, "");
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithNullCharsetName() {
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        DataUtil.load(inputStream, null);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        try {
            Document document = DataUtil.load(inputStream, charsetName, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName(charsetName)));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithNullCharsetName() {
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithInvalidCharset() {
        String charsetName = "InvalidCharset";
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        try {
            DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source content.";
        String destination = "Destination content.";
        byte[] buffer = new byte[DataUtil.bufferSize];

        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String readContent = outputStream.toString();
        assertEquals(destination, readContent);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithIOException() throws IOException {
        String source = "Source content.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = new ByteArrayInputStream(source.getBytes(Charset.forName("UTF-8")))) {
            DataUtil.crossStreams(inputStream, outputStream);
            fail("Expected IOException");
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Valid content.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = content.getBytes(Charset.forName("UTF-8"));
        byte[] readBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(readBytes);

        assertArrayEquals(expectedBytes, readBytes);
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        assertEquals(0, byteBuffer.limit());
    }

    @Test(expected = EOFException.class)
    public void testReadToByteBufferWithEOFException() throws IOException {
        byte[] content = "Valid content".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(content);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        inputStream.close();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);

        byteBuffer = DataUtil.readToByteBuffer(inputStream);

        fail("Expected EOFException");
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String content = "Valid content.";
        File file = new File("tempFile.txt");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.writeBytes(content.getBytes(Charset.forName("UTF-8")));
        }
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String readContent = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals(content, readContent);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals("UTF-16", DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.validateCharset(charset));

        charset = "GB2312";
        assertEquals("GB2312", DataUtil.validateCharset(charset));

        charset = "InvalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == DataUtil.boundaryLength);
    }
}