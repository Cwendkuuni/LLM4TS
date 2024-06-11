import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        writer.write(fileContent.getBytes(StandardCharsets.UTF_8));
        writer.close();

        Document document = DataUtil.load(testFile, StandardCharsets.UTF_8.name(), null);
        assertNotNull(document);
        assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.load(testInputStream, null, null);
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.load(testInputStream, "UTF-16LE", null);
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().text()); // This test assumes UTF-16LE is supported by the library
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.load(testInputStream, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input stream.";
        String outputContent = "Received data from input stream.";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        assertEquals(outputContent, new String(outputBytes, StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        writer.write(fileContent.getBytes(StandardCharsets.UTF_8));
        writer.close();

        Document document = DataUtil.load(testFile, "UTF-437", null); // Unsupported charset
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.load(testInputStream, null, null);
        DataUtil.load(testInputStream, null, null); // Second call to test side-effects
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");

        Document document = DataUtil.load(testFile, StandardCharsets.UTF_8.name(), null);
        DataUtil.load(testFile, StandardCharsets.UTF_8.name(), null); // Second call to test side-effects
    }

    @Test(expected = NullPointerException.class)
    public void testLoadInputStreamWithNullParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.load(testInputStream, null, null, null);
    }
}