package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoad_withExistingFile_thenReturnDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        // Add additional assertions as needed
    }

    @Test
    public void testLoad_withNonExistingFile_thenThrowIOException() {
        // Arrange
        File file = new File("non_existing.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoad_withInputStream_thenReturnDocument() throws IOException {
        // Arrange
        FileInputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        // Add additional assertions as needed
    }

    @Test
    public void testLoad_withInvalidInputStream_thenThrowIOException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("invalid_html_tag".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    // More test cases for the remaining methods in DataUtil class...

}