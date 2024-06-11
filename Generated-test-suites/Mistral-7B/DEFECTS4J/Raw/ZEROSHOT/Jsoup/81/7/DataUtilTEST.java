package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(doc);
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputData = "<html><head></head><body><p>Test input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "<html><head></head><body><p>Test input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputData = "<html><head></head><body><p>Test input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test input stream";
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        InputStream loadedInputStream = new FileInputStream(outputFile);
        assertEquals(inputData, Jsoup.connect("").text());

        inputStream.close();
        outputStream.close();
        loadedInputStream.close();
    }

    @Test
    public void testLoadInvalidCharset() throws IOException {
        String inputData = "<?xml version=\"1.0\" encoding=\"testCharset\"?>\n<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        try {
            DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
            fail("Should have thrown an HelperException.");
        } catch (HelperException e) {
            // Expected exception.
        }
    }

    @Test
    public void testLoadWithUnknownCharset() throws IOException {
        String inputData = "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=testCharset\">\n<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testLoadWithValidCharset() throws IOException {
        String inputData = "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        // Add additional assertions for the loaded document as needed.
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "Test byte buffer";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = inputData.getBytes("UTF-8");
        byte[] readBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(readBytes);
        assertArrayEquals(expectedBytes, readBytes);

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String inputData = "Test file byte buffer";
        File inputFile = new File("testFile.txt");
        writeTestFile(inputData, inputFile);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);

        byte[] expectedBytes = inputData.getBytes("UTF-8");
        byte[] readBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(readBytes);
        assertArrayEquals(expectedBytes, readBytes);

        inputFile.delete();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputData = "Test byte buffer with max size";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        assertEquals(10, byteBuffer.limit());
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSizeAndUnlimitedSize() throws IOException {
        String inputData = "Test byte buffer with max size and unlimited size";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedBytes = inputData.getBytes("UTF-8");
        byte[] readBytes = new byte[expectedBytes.length];
        byteBuffer.get(readBytes);
        assertArrayEquals(expectedBytes, readBytes);

        inputStream.close();
    }

    private void writeTestFile(String content, File file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream fos = new FileOutputStream(file);
        outputStream.writeTo(fos);
        outputStream.write(content.getBytes("UTF-8"));
        fos.close();
        outputStream.close();
    }
}