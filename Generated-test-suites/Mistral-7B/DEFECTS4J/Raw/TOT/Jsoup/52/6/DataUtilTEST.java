package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    private Document doc;
    private Parser parser;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test for file loading with default charset.";
        File file = new File("testFile.html");
        Writer writer = new Writer("testFile.html");
        writer.write(fileContent);
        writer.close();

        Document actual = DataUtil.load(file, null, null);
        assertEquals("Test document loaded with default charset", fileContent, actual.body().text());

        file.delete(); // cleanup
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test for file loading with specified charset.";
        File file = new File("testFile.html");
        Writer writer = new Writer("testFile.html");
        writer.write(fileContent, StandardCharsets.ISO_8859_1);
        writer.close();

        Document actual = DataUtil.load(file, "ISO-8859-1", null);
        assertEquals("Test document loaded with specified charset", fileContent, actual.body().text());

        file.delete(); // cleanup
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "This is a test for input stream loading with default charset.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.close();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document actual = DataUtil.load(inputStream, null, null);
        assertEquals("Test document loaded with default charset", inputStreamContent, actual.body().text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "This is a test for input stream loading with specified charset.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.close();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document actual = DataUtil.load(inputStream, "UTF-16", null); // invalid charset to test error handling
        assertNull("Test document should be null with invalid charset", actual);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>This is a test for input stream loading with Parser.</body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.close();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document actual = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertEquals("Test document loaded with xmlParser", inputStreamContent, actual.body().html());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputData = "This is a test for crossStreams".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String actual = outputStream.toString(StandardCharsets.UTF_8.name());
        assertEquals("Test data written to output stream", inputData.length > 0 ? new String(inputData, StandardCharsets.UTF_8) : "", actual);

        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputs() {
        InputStream nullInputStream = null;
        OutputStream nullOutputStream = null;

        DataUtil.crossStreams(nullInputStream, nullOutputStream);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String byteDataContent = "This is a test for parseByteData with default charset.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document actual = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
        assertEquals("Test document parsed with default charset", byteDataContent, actual.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String byteDataContent = "This is a test for parseByteData with specified charset.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document actual = DataUtil.parseByteData(byteBuffer, "UTF-16", null, DataUtil.Parser.htmlParser()); // invalid charset to test error handling
        assertNull("Test document should be null with invalid charset", actual);
    }
}