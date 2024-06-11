package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document document = DataUtil.load(testFile, "UTF-8", "");
        assertEquals("This is a test file", document.body().text());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals(testString, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testString = "This is a test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
        assertEquals(testString, document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputString = "Input string";
        String outputString = "Output string";

        ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);

        assertEquals(inputString, new String(input.readAllBytes(), StandardCharsets.UTF_8));
        assertEquals(outputString, new String(output.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "This is a test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
        assertEquals(testString, document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "This is a test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = testString.getBytes(StandardCharsets.UTF_8);

        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testString = "This is a test string";
        String invalidCharset = "InvalidCharset";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        try {
            DataUtil.parseInputStream(inputStream, invalidCharset, "", DataUtil.Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}