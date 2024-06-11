package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void load_file_validData_returnsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, expectedCharset, expectedBaseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset().name());
        Assert.assertEquals(expectedBaseUri, document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void load_file_invalidData_throwsIOException() throws IOException {
        // Arrange
        File file = new File("nonexistent.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        // Act and Assert
        try {
            DataUtil.load(file, expectedCharset, expectedBaseUri);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Successful test case
        }
    }

    @Test
    public void load_inputStream_validData_returnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, expectedCharset, expectedBaseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset().name());
        Assert.assertEquals(expectedBaseUri, document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void load_inputStream_invalidData_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        // Act and Assert
        try {
            DataUtil.load(inputStream, expectedCharset, expectedBaseUri);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Successful test case
        }
    }

    @Test
    public void load_inputStream_extraParser_validData_returnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.xml");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, expectedCharset, expectedBaseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset().name());
        Assert.assertEquals(expectedBaseUri, document.baseUri());
        // Add more assertions as needed
    }

    @Test
    public void crossStreams_validData_successful() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("input.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");

        // Act and Assert
        try {
            DataUtil.crossStreams(inputStream, outputStream);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    // Add more test cases for the remaining methods

}