package org.jsoup.helper; // make sure to change the package name if needed

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DataUtilTest {

    private InputStream inputStream;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        String testFileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        testFile = Files.createTempFile("test", ".html");
        Files.write(testFile.toPath(), testFileContent.getBytes(StandardCharsets.UTF_8));

        inputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("Test Document", document.title());
        assertEquals("This is a test document.", document.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document document = null;
        try {
            document = DataUtil.load(testFile, "InvalidCharset", null);
        } catch (IOException e) {
            // Expected
        }
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Test Document", document.title());
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testParseInputStreamWithUTF8Charset() throws IOException {
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test Document", document.title());
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStreamWithInvalidCharset = new FileInputStream(new File("non_existent_file.txt"));
        try {
            DataUtil.parseInputStream(inputStreamWithInvalidCharset, "InvalidCharset", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("test.txt");
        OutputStream outputStream = new FileOutputStream("output.txt");

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(Files.size(new File("test.txt")), Files.size(new File("output.txt")));

        inputStream.close();
        outputStream.close();
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) inputStream.close();
        if (testFile != null && testFile.exists()) testFile.delete();
    }
}