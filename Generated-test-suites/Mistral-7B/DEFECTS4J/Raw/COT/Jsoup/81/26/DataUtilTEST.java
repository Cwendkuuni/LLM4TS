package org.jsoup.helper; // make sure the package name matches the class package

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document doc;
    private static final String TEST_FILE_PATH = "test.html"; // replace with the actual path to a test file

    @BeforeClass
    public static void setup() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        doc = DataUtil.load(testFile, "UTF-8", null);
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        Document expectedDoc = DataUtil.load(testFile, null, null);
        Document actualDoc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(actualDoc);
        assertTrue(actualDoc.equals(expectedDoc));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        Document actualDoc = DataUtil.load(inputStream, "UTF-8", null);
        Document expectedDoc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{}), "UTF-8", null, Parser.htmlParser());
        assertNotNull(actualDoc);
        assertTrue(actualDoc.equals(expectedDoc));
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() throws IOException {
        Document doc = DataUtil.load(null, "UTF-8", null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
    }

    @Test
    public void testParseInputStream() throws IOException {
        byte[] inputData = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document actualDoc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Document expectedDoc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{}), "UTF-8", null, Parser.htmlParser());
        assertNotNull(actualDoc);
        assertTrue(actualDoc.equals(expectedDoc));
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInput() throws IOException {
        Document doc = DataUtil.parseInputStream(null, "UTF-8", null, Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullCharset() throws IOException {
        byte[] inputData = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInputFile.txt");
        File outputFile = new File("testOutputFile.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedBytes = new byte[inputFile.length()];
        byte[] actualBytes = new byte[outputFile.length()];

        // read input file
        InputStream newInput = new FileInputStream(inputFile);
        newInput.read(expectedBytes);
        newInput.close();

        // read output file
        InputStream newOutput = new FileInputStream(outputFile);
        newOutput.read(actualBytes);
        newOutput.close();

        assertTrue(new String(expectedBytes, StandardCharsets.UTF_8).equals(new String(actualBytes, StandardCharsets.UTF_8)));
    }
}