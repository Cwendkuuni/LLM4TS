import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private static final String CHARSET = "UTF-8";

    @Before
    public void setUp() {
        // Set up any necessary initialization here
    }

    @Test
    public void testLoadFromFile() throws IOException {
        // Create a temporary HTML file for testing
        File tempHtmlFile = File.createTempFile("test", ".html");

        // Write some test HTML content to the file
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        DataUtilTest.writeToFile(tempHtmlFile, htmlContent);

        // Load the file using DataUtil.load() method
        Document doc = DataUtil.load(tempHtmlFile, CHARSET, "");

        // Check if the document is not null
        assertNotNull(doc);

        // Assert the expected title of the document
        assertEquals("Test", doc.title());

        // Delete the temporary HTML file
        tempHtmlFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFromFile_NonExistingFile() throws IOException {
        // Create a non-existing file for testing
        File nonExistingFile = new File("non_existing_file.html");

        // Load the non-existing file using DataUtil.load() method
        DataUtil.load(nonExistingFile, CHARSET, "");
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        // Create an input stream from the test HTML content
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream(writeToFile(htmlContent));

        // Load the input stream using DataUtil.load() method
        Document doc = DataUtil.load(inputStream, CHARSET, "");

        // Check if the document is not null
        assertNotNull(doc);

        // Assert the expected title of the document
        assertEquals("Test", doc.title());

        // Close the input stream
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStream_IOError() throws IOException {
        // Create an input stream from the test HTML content
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream(writeToFile(htmlContent));

        // Close the input stream before loading it using DataUtil.load() method
        inputStream.close();

        // Load the closed input stream using DataUtil.load() method
        DataUtil.load(inputStream, CHARSET, "");
    }

    @Test
    public void testLoadFromInputStreamUsingParser() throws IOException {
        // Create an input stream from the test HTML content
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream(writeToFile(htmlContent));

        // Load the input stream using DataUtil.load() method with a custom parser
        Document doc = DataUtil.load(inputStream, CHARSET, "", Parser.htmlParser());

        // Check if the document is not null
        assertNotNull(doc);

        // Assert the expected title of the document
        assertEquals("Test", doc.title());

        // Close the input stream
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamUsingParser_IOError() throws IOException {
        // Create an input stream from the test HTML content
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream(writeToFile(htmlContent));

        // Close the input stream before loading it using DataUtil.load() method
        inputStream.close();

        // Load the closed input stream using DataUtil.load() method with a custom parser
        DataUtil.load(inputStream, CHARSET, "", Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Create an input stream from the test HTML content
        String htmlContent = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream(writeToFile(htmlContent));

        // Create an output stream to store the cross streams content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Cross the input stream with the output stream using DataUtil.crossStreams() method
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert the expected content written to the output stream
        assertEquals(htmlContent, outputStream.toString(CHARSET));

        // Close the input stream and output stream
        inputStream.close();
        outputStream.close();
    }

    // Add more test cases for other methods

    private File writeToFile(String content) throws IOException {
        File file = File.createTempFile("test", ".html");

        // Write the content to the file
        FileUtil.writeToFile(file, content, StandardCharsets.UTF_8);

        return file;
    }
}