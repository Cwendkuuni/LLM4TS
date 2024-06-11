package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private File testFile;
    private String baseUri;

    @Before
    public void setUp() throws IOException {
        testFile = createTestFile("test.html"); // Creating a test HTML file
        baseUri = "http://example.com"; // Example base URI for resolving relative links
    }

    @Test
    public void testLoad_withFile_returnsDocument() throws IOException {
        // Arrange
        String charset = "UTF-8";

        // Act
        Document doc = DataUtil.load(testFile, charset, baseUri);

        // Assert
        assertNotNull(doc);
    }

    @Test
    public void testLoad_withInputStream_returnsDocument() throws IOException {
        // Arrange
        FileInputStream inStream = new FileInputStream(testFile);
        String charset = "UTF-8";

        // Act
        Document doc = DataUtil.load(inStream, charset, baseUri);

        // Assert
        assertNotNull(doc);

        // Clean up
        inStream.close();
    }

    @Test
    public void testLoad_withInputStreamAndParser_returnsDocument() throws IOException {
        // Arrange
        FileInputStream inStream = new FileInputStream(testFile);
        String charset = "UTF-8";
        Parser parser = Parser.htmlParser();

        // Act
        Document doc = DataUtil.load(inStream, charset, baseUri, parser);

        // Assert
        assertNotNull(doc);

        // Clean up
        inStream.close();
    }

    /*
    Additional test cases can be added here to cover typical use cases, edge cases, and error scenarios for the other
    methods and functionalities of DataUtil class.
    */

    // Helper method to create a temporary test file with given content
    private File createTestFile(String fileName) throws IOException {
        String content = "<html><head><title>Test</title></head><body><h1>Test Page</h1><p>This is a test.</p></body></html>";
        Path filePath = Paths.get(fileName);
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        return filePath.toFile();
    }
}