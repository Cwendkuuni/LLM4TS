import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

class DataUtilTest {

    private static final String TEST_FILE_PATH = "testFile.html";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com";

    @Test
    void testLoadFile() throws IOException {
        // Test case with valid file path and character set name
        File testFile = new File(TEST_FILE_PATH);
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);

        // Verify document is not null
        Assertions.assertNotNull(document);

        // Verify document contains expected elements
        // ...

        // TODO: Add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    void testLoadInputStream() throws IOException {
        // Test case with valid input stream and character set name
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        // Verify document is not null
        Assertions.assertNotNull(document);

        // Verify document contains expected elements
        // ...

        // TODO: Add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    void testLoadInputStreamWithParser() throws IOException {
        // Test case with valid input stream, character set name, and custom parser
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        // Verify document is not null
        Assertions.assertNotNull(document);

        // Verify document contains expected elements
        // ...

        // TODO: Add more assertions for typical use cases, edge cases, and error scenarios
    }

    // TODO: Add more test methods for other public methods in DataUtil class

}