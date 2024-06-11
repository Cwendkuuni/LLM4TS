package org.jsoup.helper;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * JUnit test cases for DataUtil class
 */
public class DataUtilTest {
 
    @Test
    public void testLoadFile_withValidFileAndCharsetAndBaseUri() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        File file = new File("path/to/file.html");

        // act
        Document document = DataUtil.load(file, charset, baseUri);

        // assert
        assertNotNull(document);
        // perform further assertions
    }

    @Test
    public void testLoadFile_withInvalidFile() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        File file = new File("invalid/file.html");

        // act and assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(file, charset, baseUri);
        });
    }

    @Test
    public void testLoadInputStream_withValidInputStreamAndCharsetAndBaseUri() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        InputStream inputStream = new FileInputStream("path/to/input.html");

        // act
        Document document = DataUtil.load(inputStream, charset, baseUri);

        // assert
        assertNotNull(document);
        // perform further assertions
    }

    @Test
    public void testLoadInputStream_withInvalidInputStream() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        InputStream inputStream = new FileInputStream("invalid/file.html");

        // act and assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, charset, baseUri);
        });
    }

    @Test
    public void testLoadInputStream_withValidInputStreamAndCharsetAndBaseUriAndParser() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        InputStream inputStream = new FileInputStream("path/to/input.html");
        Parser parser = Parser.xmlParser();

        // act
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);

        // assert
        assertNotNull(document);
        // perform further assertions
    }

    @Test
    public void testLoadInputStream_withInvalidInputStreamAndParser() {
        // arrange
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        InputStream inputStream = new FileInputStream("invalid/file.html");
        Parser parser = Parser.xmlParser();

        // act and assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(inputStream, charset, baseUri, parser);
        });
    }

    // Add more test cases for other methods

}