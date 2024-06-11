package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        File expectedFile = new File("expectedFile.html");
        String expectedCharset = "UTF-8";
        String baseUri = "/test";

        // Write expected file
        FileWriter fileWriter = new FileWriter(expectedFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        // Test
        Document actualDocument = DataUtil.load(testFile, expectedCharset, baseUri);
        Document expectedDocument = DataUtil.load(new File("expectedFile.html"), expectedCharset, baseUri);

        // Assert
        assertEquals(expectedDocument.toString(), actualDocument.toString());

        // Clean up
        testFile.delete();
        expectedFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        String baseUri = "/test";

        // Create input stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // Test
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Document expectedDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(new ByteArrayInputStream(inputStream.readAllBytes())), charsetName, baseUri, Parser.htmlParser());
        assertEquals(expectedDocument.toString(), actualDocument.toString());

        // Clean up
        inputStream.close();
    }

    @Test
    public void testParseByteDataInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String invalidCharset = "InvalidCharset";
        String baseUri = "/test";

        // Create input stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // Test
        try {
            Document actualDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(new ByteArrayInputStream(inputStream.readAllBytes())), null, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Clean up
        inputStream.close();
    }

    @Test
    public void testParseByteDataEmptyInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/test";

        // Test
        try {
            Document actualDocument = DataUtil.parseByteData(DataUtil.emptyByteBuffer(), charsetName, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_1234567890";

        // Test
        String actualMimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertEquals(expectedMimeBoundary, actualMimeBoundary);
    }
}