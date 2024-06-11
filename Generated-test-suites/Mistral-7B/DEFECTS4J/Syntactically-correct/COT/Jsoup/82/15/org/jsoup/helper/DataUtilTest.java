package org.jsoup.helper; // Update the package name if it's different

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Pattern;

public class DataUtilTest {

    private static final String RESOURCE_FOLDER = "/test_resources/";
    private static final String HTML_FILE = "testInput.html";

    @BeforeClass
    public static void setup() {
        // Initialize the DataUtil class here if needed
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File(RESOURCE_FOLDER + HTML_FILE);
        Document document = DataUtil.load(inputFile, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File(RESOURCE_FOLDER + HTML_FILE);
        Document document = DataUtil.load(inputFile, "UTF-8");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File(RESOURCE_FOLDER + HTML_FILE);
        Document document = DataUtil.load(inputFile, "INVALID_CHARSET");
        fail("IOException expected but no exception was thrown");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlText = IOUtils.toString(DataUtilTest.class.getResourceAsStream("/testInput.html"), StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlText = IOUtils.toString(DataUtilTest.class.getResourceAsStream("/testInput.html"), StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlText = IOUtils.toString(DataUtilTest.class.getResourceAsStream("/testInput.html"), StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, "INVALID_CHARSET");
            fail("IOException expected but no exception was thrown");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoadInputStreamWithDifferentParser() throws IOException {
        String htmlText = IOUtils.toString(DataUtilTest.class.getResourceAsStream("/testInput.xml"), StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedBytes = FileUtils.readFileToByteArray(inputFile);
        byte[] actualBytes = FileUtils.readFileToByteArray(outputFile);

        assertArrayEquals(expectedBytes, actualBytes);

        inputStream.close();
        outputStream.close();
        inputFile.delete();
        outputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithException() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            inputFile.delete();
            outputFile.delete();
            fail("IOException expected but no exception was thrown");
        } catch (IOException e) {
            // Pass
        }

        inputStream.close();
        outputStream.close();
        inputFile.delete();
        outputFile.delete();
    }
}