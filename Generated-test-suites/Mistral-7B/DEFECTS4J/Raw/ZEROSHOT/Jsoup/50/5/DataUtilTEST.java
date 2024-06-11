package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
    public void testLoadFile() throws IOException {
        String filePath = "src/test/resources/testData.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidFile() throws IOException {
        File file = new File("invalidFilePath");
        Document document = DataUtil.load(file, "UTF-8", null);
        fail("Expected IOException, but no exception was thrown.");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidContent() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        fail("Expected IOException, but no exception was thrown.");
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("src/test/resources/testData.html");
        File outputFile = new File("target/testOutput.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        File loadedFile = new File("target/testOutput.html");
        Document document = DataUtil.load(loadedFile, "UTF-8", null);
        assertNotNull(document);

        inputStream.close();
        outputStream.close();
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
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}